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

import javax.net.ssl.*;
import java.io.*;   
import java.util.*; 
import java.net.*; 

import com.sybase.jdbcx.SybSocketFactory;

/**
 * <HR>
 * <H1><U>SSL Security Protocol</U></H1>
 * <P>
 * Security Sockets Layer (SSL) is a security protocol for protecting TCP/IP 
 * communication between a client and server.  SSL provides three basic
 * security features:
 * <UL>
 *    <LI> Encryption, to ensure the privacy of communication
 *    <LI> Authentication, to verify the identity of a client or server and 
 *         prevent impersonations or message forgeries
 *    <LI> Message verification, to make sure that a message has not been 
 *         tampered with.
 * </UL>
 * When jConnect is used with SSL, all TDS communication sent to the server 
 * is encrypted.
 *
 * <P><B>NOTE:</B> This sample should <B>not</B> be used if you are testing
 * an SSL connection to a SAP ASE which supports SSL (ASE provided
 * SSL support beginning with version 12.5). If you wish to see how to make
 * SSL connections to ASE 12.5+, you should look at the
 * MySSLSocketFactoryASE.java sample file in this directory.
 * <P>An applet that needs a secure connection should use an HTTPS gateway.  
 * See Appendix A, "Web Server Gateways" in the jConnect Programmer's 
 * Reference.
 * 
 * See sample/Encrypt.java for a sample on how to execute/use this 
 * SybSocketFactory implementation.
 */
public class MySSLSocketFactory extends SSLSocketFactory 
    implements SybSocketFactory
{
    /**
    * Create a socket, set the cipher suites it can use, return the
    * socket.
    * <BR> Demonstrates how cipher suites could be hard
    * coded into the implementation.
    * 
    * @param host - server host name
    * @param port - server port number
    * @return Socket - SSLSocket instance
    * @see javax.net.SSLSocketFactory#createSocket
    */
    public Socket createSocket(String host, int port)
        throws IOException, UnknownHostException
    {
        return createSocket(host, port, (Properties) null, 0);
    }

    public Socket createSocket(String host, int port, Properties props,
            int timeout) throws IOException, UnknownHostException
    {
        //Prepare an array containing the cipher suites that are to be 
        //enabled
        String enableThese[] = 
        {
            "SSL_DH_anon_EXPORT_WITH_RC4_40_MD5"
        }
        ;
        Socket s = 
            SSLSocketFactory.getDefault().createSocket(host, port);
        ((SSLSocket)s).setEnabledCipherSuites(enableThese);
        s.setSoTimeout(timeout);
        return s;
    }
    /**
    * return an SSLSocket
    * <BR> Demonstrates how to set cipher suites based on
    * connection properties.
    *
    *  <P>The following is an example of other cipher suites that could
    *  be set, assuming your <code>ServerSocket</code> has them enabled.
    * 
    *  <UL>
    *  <LI> Properties _props = new Properties();
    *  <LI> // Set other url, password, etc properties.
    *  <LI> _props.put("CIPHER_SUITES_1",
    *           "SSL_DH_anon_EXPORT_WITH_RC4_40_MD5");
    *  <LI> _props.put("CIPHER_SUITES_2",
    *           "SSL_DH_DSS_EXPORT_WITH_DES40_CBC_SHA"); 
    *  <LI> _props.put("CIPHER_SUITES_3",
    *           "SSL_RSA_EXPORT_WITH_RC2_CBC_40_MD5");
    *  <LI> _props.put("CIPHER_SUITES_4",
    *           "SSL_DH_RSA_EXPORT_WITH_DES40_CBC_SHA");
    *  <LI> _conn = _driver.getConnection(url, _props);
    * </UL>
    * 
    * @param host - server host name
    * @param port - server port number
    * @param props - connection properties
    * @return Socket - SSLSocket instance
    * @exception IOException
    * @exception UnknownHostException
    * @see sample2.Encrypt
    * @see com.sybase.jdbcx.SybSocketFactory#createSocket
    */
    public Socket createSocket(String host, int port, 
        Properties props)
        throws IOException, UnknownHostException
    {

        // check to see if cipher suites are set in the connection
        // properties
        Vector cipherSuites = new Vector();
        String cipherSuiteVal = null;
        int cipherIndex = 1;

        // Loop through possible multiple cipher suites
        do
        {
            if((cipherSuiteVal = props.getProperty("CIPHER_SUITES_"
            + cipherIndex++)) == null)
            {
                if(cipherIndex <= 2)
                {
                    // No cipher suites available
                    // return what the object considers its default
                    // SSLSocket, with cipher suites enabled.
                    return createSocket(host, port);
                }
                else
                {
                    // we have at least one cipher suite to enable 
                    // per request on the connection
                    break;
                }
            }
            else 
            {
                // add to the cipher suite Vector, so that
                // we may enable them together
                cipherSuites.addElement(cipherSuiteVal);
            }
        }
        while(true);

        // let's create a String[] out of the created vector
        String enableThese[] = new String[cipherSuites.size()];
        cipherSuites.copyInto(enableThese);

        // enable the cipher suites
        Socket s = 
            SSLSocketFactory.getDefault().createSocket(host, port);
        ((SSLSocket)s).setEnabledCipherSuites(enableThese);

        // return the SSLSocket
        return s;
    }

    // javax.net.SSLSocketFactory contains several other methods
    // that need to be implemented because they are abstract.
    // They are not necessary for this sample, so we shall
    // simply stub them.

    /**
     *  Not implemented for this sample
     *  @return null
     */
    public java.lang.String[] getDefaultCipherSuites()
    {
        return null;
    }

    /**
     *  Not implemented for this sample
     *  @return null
     */
    public java.lang.String[] getSupportedCipherSuites()
    {
        return null;
    }

    /**
     *  Not implemented for this sample
     *  @return null
     */
    public java.net.Socket createSocket(java.lang.String host, int port,
        java.net.InetAddress clientAddress, int clientPort)
    {
        return null;
    }

    /**
     *  Not implemented for this sample
     *  @return null
     */
    public java.net.Socket createSocket(java.net.InetAddress host, int port)
    {
        return null;
    }

    /**
     *  Not implemented for this sample
     *  @return null
     */
    public java.net.Socket createSocket(java.net.InetAddress host, int
        port, java.net.InetAddress clientAddress, int clientPort)
    {
        return null;

    }

    /**
     *  Not implemented for this sample
     *  @return null
     */
    public java.net.Socket createSocket(java.net.Socket socket, String host,
        int port, boolean autoClose)
    {
        return null;

    }
}

