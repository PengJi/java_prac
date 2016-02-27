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
import java.io.*;
import java.sql.*;
import java.util.*;

/**
 * EncryptASE demonstrates how to use the SSL encryption with jConnect
 * by utilizing a custom socket implementation of javax.net.SSLSocket, 
 * and javax.net.SSLSocketFactory.
 *
 * <P> NOTE: This sample differs from the Encrypt.java sample in that this
 *     sample must be run under JDK 1.4. In order to use this sample: 
 * <UL> 
 *     <LI> You must run this using JDK 1.4 or higher. JDK 1.4
 *          is available for free from Sun.
 *     <LI> Before you run this sample, you must separately compile
 *          MySSLSocketFactoryASE.java with JDK 1.4 or higher. Please read
 *          the source code for MySSLSocketFactoryASE.java for details on
 *          how to configure that class for your SSL environment.
 *     <LI> The SSLSocket can only communicate to a server that speaks
 *          SSL and has common cipher suites enabled. This sample is 
 *          designed to exhibit how to connect to an ASE (version
 *          12.5 or higher) which has SSL enabled and has an SSL port
 *          in operation. See the ASE documentation for information on how
 *          to configure ASE for SSL communication. 
 * </UL>
 *
 * <P>EncryptASE.java must be invoked with the parameters:
 * -U username<br>
 * -P password<br>
 * -D debuglibraries<br>
 * -S server<p>
 *
 *  @see Sample
 */
public class EncryptASE extends Sample
{

    /**
     * Language query - you may wish to customize this query
     */
    public static final String QUERY = "select name from syslogins";

    /**
     * constructor
     */
    EncryptASE()
    {
        super();
    }

    /**
     * Set connection properties
     * @param cmdline - commandline args object
     */
    public void addMoreProps(CommandLine cmdline)
    {
        // set the SYBSOCKET_FACTORY property
        cmdline._props.put("SYBSOCKET_FACTORY", 
            "sample2.MySSLSocketFactoryASE");
    }

    /**
     * Demonstrate the use of SSL Encryption
     * with a PreparedStatement object
     */
    public void sampleCode()
    {
        try
        {

            PreparedStatement pstmt = _con.prepareStatement(QUERY);
            output("Executing: " + QUERY + "\n");

            ResultSet rs = pstmt.executeQuery ();
            dispResultSet(rs);

            rs.close();
            pstmt.close();
        }
        catch (SQLException ex)
        {
            displaySQLEx(ex);
        }
    }
}
