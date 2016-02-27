/*
 Copyright Notice and Disclaimer
 -------------------------------
(c) Copyright 2013.
SAP AG or an SAP affiliate company. All rights reserved.
Unpublished rights reserved under U.S. copyright laws.

SAP grants Licensee a non-exclusive license to use, reproduce,
modify, and distribute the sample source code below (the "Sample Code"),
subject to the following conditions:

(i) redistributions must retain the above copyright notice;

(ii) SAP shall have no obligation to correct errors or deliver
updates to the Sample Code or provide any other support for the
Sample Code;

(iii) Licensee may use the Sample Code to develop applications
(the "Licensee Applications") and may distribute the Sample Code in
whole or in part as part of such Licensee Applications, however in no
event shall Licensee distribute the Sample Code on a standalone basis;

(iv) and subject to the following disclaimer:
THIS SOFTWARE IS PROVIDED "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY
AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL
SAP AG or an SAP affiliate company OR ITS LICENSORS BE LIABLE FOR ANY DIRECT,
INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES 
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR 
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE
USE OF THE SAMPLE CODE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH
DAMAGE.
*/

package sybase.sample;

import java.sql.*;
import java.util.Properties;
import com.sybase.jdbc4.jdbc.*;
import java.io.*;

/** This class provides a simple example of making a Kerberos-enabled
 *  Connection to Adaptive Server Enterprise (ASE) version 12.0 and higher.
 *  Note that to successfully connect using Kerberos, several conditions must
 *  be met:
 *
 *  1. The server must be licensed to use the security option.
 *  2. The server should be configured with logins and users for the  
 *     authenticated user.
 *  3. The server should have the proper configuration settings relating
 *     to the keytab, and to the default realm.
 *  4. The Java environment should be using JDK 1.4 or later (we strongly
 *     recommend 1.4.2 or higher; if you don't use 1.4.2, you may run into
 *     problems when authenticating to an Active Directory KDC)
 *  5. The client must use jConnect 6.0 or higher
 *  6. The REQUEST_KERBEROS_SESSION connection property should be set to true
 *  7. The SERVICE_PRINCIPAL_NAME should be set to the name that the server is
 *     running under. Generally, this name is set with the -s option when
 *     the server is started.
 *  8. The application can specify a login configuration file (this is 
 *     required by the Java Authentication and Authorization Service (JAAS)
 *     API). Alternatively, you may programatically specify a JAAS login 
 *     configuration. We give an example of how that is done in the 
 *     ConnectKerberosJAAS sample. When using a login configuration file,
 *     as in this example, the user must set the Java system property
 *        java.security.auth.login.config
 *     to the location of the file.
 *  9. The application should set the Java system property 
 *        javax.security.auth.useSubjectCredsOnly
 *     to false. The reasons for this are explained in the JAAS documentation
 *     online, but in short, it is so that Java can look for the login
 *     credentials in a well-defined location on the client machine.
 *     Note that this system property does not have to be set; see the
 *     ConnectKerberosJAAS sample for an example.
 * 10. The client must set up a valid kerberos configuration file 
 *     (i.e. krb5.conf or krb5.ini). In lieu of using such a file, a client
 *     can set the java.security.krb5.realm and java.security.krb5.kdc
 *     system properties. Please refer to the Java documentation for more
 *     information. 
 * 11. Finally, the client should ensure that he/she has performed a
 *     Kerberos authentication prior to running the application; otherwise
 *     Java will prompt the user for a username/password. This authentication
 *     can be done through a simple unix login (if the client machine has
 *     been set up to do so), by using kinit, or through a Windows 2K login to
 *     an Active Directory domain.  
 */


public class ConnectKerberos 
{
    public static void main(java.lang.String[] args)
        throws Exception
    {
        /*
         * Set up the connection. 
         */
        SybDriver sybD = (SybDriver) Class.forName
            ("com.sybase.jdbc4.jdbc.SybDriver").newInstance();

        // You'll want to change the URL to point to your database server
        String url = "jdbc:sybase:Tds:hostname:portnum";

        Properties props = new Properties();

        // You must set REQUEST_KERBEROS_SESSION to true to enable a Kerberos
        // connection
        props.put("REQUEST_KERBEROS_SESSION", "true");

        // Set the SERVICE_PRINCIPAL_NAME to the value which corresponds to
        // your server
        props.put("SERVICE_PRINCIPAL_NAME", "myserver");

        // You may optionally set the GSSMANAGER_CLASS property value, if
        // you wish to use a GSSManager other than the default Java
        // implementation. This class uses the default implementation, so
        // we do not set the connection property here.

        System.setProperty("javax.security.auth.useSubjectCredsOnly", "false");

        // If you wish to execute this sample program from a directory other
        // than the sample2 directory, you'll need to change this property
        // so it points to the absolute location of the login config file.
        System.setProperty("java.security.auth.login.config", 
            "exampleLogin.conf");
        try
        {
            System.out.println ("Attempting kerberized connection");
            Connection conn = sybD.connect(url, props);

            System.out.println ("Kerberos connection succeeded");

            System.out.println ("\nExecuting a simple query -- select 1\n"); 
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery ("select 1");
            while (rs.next())
            {
                System.out.println ("Select returned " + rs.getString(1));
            }
            rs.close();
            st.close(); 
            conn.close();

        }
        catch (SQLException sqle)
        {
            System.out.println ("exceptions:");
            while (sqle != null)
            {
                System.out.println (sqle);
                sqle.printStackTrace();
                sqle = sqle.getNextException();
            }
        }
    }
}
