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
 * Version class  displays the version and expiration date (if any) for
 * the jConnect driver being used.<br>
 *
 * <P>Version may be invoked with the optional parameters:<br>
 * -U username<br>
 * -P password<br>
 * -D debuglibraries<br>
 * -S server<p>
 * -e = display expiration string
 *
 *  @see Sample
 */
public class Version extends Sample
{

    static Properties _props = null;  // Local copy of Cmdline properties
    static Driver _driver = null;

    Version()
    {
        super();
    }

    public void run()
    {
        try
        {

            addMoreProps(_cmdline);

            // Load the Driver

            DriverManager.registerDriver((Driver)
                Class.forName("com.sybase.jdbc4.jdbc.SybDriver").newInstance());

            _driver = DriverManager.getDriver(
                _cmdline._props.getProperty("server"));
            int major = _driver.getMajorVersion();
            int minor = _driver.getMinorVersion();
            output("Using JDBC driver version " +
                major + "." + minor + "\n");

            // Save off CommandLine Properties
            _props = _cmdline._props;
            //Inorder to get Licensee information (Trial, Production Version, PowerJ, ..),
            //need to make a connection otherwise it simply states that
            //this is an (Unlicensed Version).  Therefore we will try to make a 
            //connection, and catch the Exception and print what version info we have
            //if the connection couldn't be made.
            _con = null;
            try
            {
                String prox = _cmdline._props.getProperty("proxy");
                error(" PROXY = "+prox+"\n");
                _con = DriverManager.getConnection(_cmdline._props.getProperty("server"), 
                    _cmdline._props);
            }
            catch(SQLException connectExcep)
            {
                //Oh well we can't access Licensee information because the connection
                // failed, but will Display as much version info we can
            }


            // Run the sample specific code
            sampleCode();

        }
        catch (SQLException ex)
        {
            displaySQLEx(ex);
        }
        catch (ClassNotFoundException e)
        {
            error("Unexpected exception : " + e.toString() + "\n");
            error("\nThis error usually indicates that " +
                "your Java CLASSPATH environment has not been set properly.\n");
            e.printStackTrace();
        }
        catch (Exception e)
        {
            error("Unexpected exception : " + e.toString() + "\n");
            error("\nCould not Load the jConnect driver\n");
            e.printStackTrace();
        }

    }

    public void sampleCode()
    {

        String url = _props.getProperty("server");

        try
        {

            DriverPropertyInfo dpi[] = _driver.getPropertyInfo(url, _props);
            // get the version string
            for (int i = 0; i < dpi.length; i++)
            {
                if (dpi[i].name.equals("VERSIONSTRING"))
                {
                    output(dpi[i].value + "\n");
                    break;
                }
                if (dpi[i].name.equals("EXPIRESTRING"))
                {
                    String expires = dpi[i].value;
                    if(expires.equals(""))
                    {
                        output("\n Driver does not contain an expiration"
                            + " date\n");
                    }
                    else
                    {
                        output("\n This Driver expires on: "
                            + " expires\n");
                    }
                    break;
                }
            }


        }
        catch (SQLException sqe)
        {
            displaySQLEx(sqe);

        }
    }
}
