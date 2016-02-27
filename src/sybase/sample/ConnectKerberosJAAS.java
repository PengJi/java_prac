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
import java.util.*;
import com.sybase.jdbc4.jdbc.*;
import java.io.*;
import java.security.Principal;
import java.security.PrivilegedAction;
import javax.security.auth.*;
import javax.security.auth.kerberos.*;
import javax.security.auth.callback.*;
import javax.security.auth.login.*;
import javax.security.auth.spi.*;
import com.sun.security.auth.callback.TextCallbackHandler;

/** This class provides a somewhat complex example of making a Kerberos-enabled
 *  Connection to Adaptive Server Enterprise (ASE) version 12.0 and higher.
 *  This example is more complex than the ConnectKerberos sample in that this
 *  demonstrates the kind of code that might be found inside an application
 *  server that is implementing Kerberos-login support.
 *
 *  Note that to successfully connect using this sample, several conditions 
 *  must be met:
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
 *  8. The client must set up a valid kerberos configuration file 
 *     (i.e. krb5.conf or krb5.ini). In lieu of using such a file, a client
 *     can set the java.security.krb5.realm and java.security.krb5.kdc
 *     system properties. Please refer to the Java documentation for more
 *     information. 
 */


public class ConnectKerberosJAAS
{
    public static void main(java.lang.String[] args)
        throws Exception
    {
 
        // Load the jConnect driver
        SybDriver sybD = (SybDriver) Class.forName
            ("com.sybase.jdbc4.jdbc.SybDriver").newInstance();

        // You'll want to set your own values for the url and
        // servicePrincipalName
        final String url = "jdbc:sybase:Tds:hostname:portnum";
        final String servicePrincipalName = "myserver";

        // Initialize the Kerberos configuration options
        // We do this in lieu of having a login configuration file
        KerberosConfiguration.setConfiguration();

        // Next, we do a JAAS login to initialize the kerberos login
        // credentials
        LoginContext lc = null;
        try
        {

            System.out.println ("Attempting JAAS authentication");
            lc = new LoginContext("Kerberos jconnect test", 
                new TextCallbackHandler());

            // Since we set the useTicketCache configuration option to true
            // in the KerberosConfiguration inner class, java will try
            // to locate your login credentials from the Kerberos ticket
            // cache. However, if it cannot find credentials, it will 
            // prompt the user for username and password.
            lc.login();

            System.out.println ("JAAS authentication succeeded");
        }
        catch (LoginException le)
        {
            System.out.println("JAAS authentication failed with " +
                "exception: " + le);
            System.exit(0);
        }

        // Now that we've authenticated, let's output some information on
        // the user.

        Iterator principalIterator =
            lc.getSubject().getPrincipals().iterator();
        System.out.println("Authenticated user has the following Principals:");
        while (principalIterator.hasNext())
        {
            Principal p = (Principal)principalIterator.next();
            System.out.println("\t" + p.toString());
        }

        System.out.println("User has " +
            lc.getSubject().getPublicCredentials().size() +
            " Public Credential(s)");

        System.out.println("User has " +
            lc.getSubject().getPrivateCredentials().size() +
            " Private Credential(s)");

        // Now try to login as the Subject. We must do this because the
        // Kerberos Module will look for the ticket in the Subject's
        // credentials
        System.out.println("Trying to connect to: " + url);
        System.out.println("Service Principal Name is " + servicePrincipalName);
        Subject.doAs(lc.getSubject(), new PrivilegedAction()
        {
            public Object run()
            {
                try
                {
                    Properties props = new Properties();
                    props.put ("REQUEST_KERBEROS_SESSION", "true");
                    props.put ("SERVICE_PRINCIPAL_NAME", servicePrincipalName);
                    Connection conn = DriverManager.getConnection(url, props);
                    System.out.println ("Kerberos connection succeeded");

                    System.out.println ("\nExecuting a simple query -- " +
                        "select 1\n");
                    Statement st = conn.createStatement();
                    ResultSet rs = st.executeQuery ("select 1");
                    while (rs.next())
                    {
                        System.out.println ("Select returned " + 
                            rs.getString(1));
                    }
                    rs.close();
                    st.close();
                    conn.close();
                }
                catch (SQLException sqe)
                {
                    System.out.println ("Could not connect to server. "  +
                        "Got Exception: " + sqe);
                    sqe.printStackTrace();
                }

                return null;
            }
        });
    }
}
