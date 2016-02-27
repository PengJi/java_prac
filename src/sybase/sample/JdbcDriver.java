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
 * JdbcDriver class demonstrates how to use the System property "jdbc.drivers"
 * to load the jConnect driver.<br>
 *
 * <P>JdbcDriver may be invoked with the optional parameters:<br>
 * -U username<br>
 * -P password<br>
 * -D debuglibraries<br>
 * -S server<p>
 *
 *  @see Sample
 */
public class JdbcDriver extends Sample
{

    JdbcDriver()
    {
        super();
    }

    public void run()
    {

        try
        {

            addMoreProps(_cmdline);

            // Define the driver using the System Property

            Properties sysProps = System.getProperties();
            StringBuffer drivers = new 
                StringBuffer("com.sybase.jdbc4.jdbc.SybDriver");

            String oldDrivers = sysProps.getProperty("jdbc.drivers");

            if (oldDrivers != null)
            drivers.append(":" + oldDrivers);

            sysProps.put("jdbc.drivers", drivers.toString());



            //  Attempt to connect to a driver.  This will also Load the
            //  jConnect driver.
            _con = DriverManager.getConnection(
                _cmdline._props.getProperty("server"), _cmdline._props);

            // If we were unable to connect, an exception
            // would have been thrown.  So, if we get here,
            // we are successfully connected to the URL

            // Check for, and display and warnings generated
            // by the connect.

            checkForWarning (_con.getWarnings ());

            // Run the sample specific code
            sampleCode();

            // Close the connection

            _con.close();
        }
        catch (SQLException ex)
        {
            displaySQLEx(ex);
        }
        catch (java.lang.Exception ex)
        {

            // Got some other type of exception.  Dump it.

            ex.printStackTrace ();
        }
    }

    public void sampleCode()
    {
        try
        {

            // Get the DatabaseMetaData object and display
            // some information about the connection

            DatabaseMetaData dma = _con.getMetaData ();

            output("\nConnected to " + dma.getURL() + "\n");
            output("Driver       " + dma.getDriverName() + "\n");
            output("Version      " + dma.getDriverVersion() + "\n");

        }
        catch (SQLException ex)
        {
            displaySQLEx(ex);
        }
    }
}
