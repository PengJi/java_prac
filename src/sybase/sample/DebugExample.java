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
import com.sybase.jdbcx.Debug;
import java.io.*;
import java.sql.*;
import java.util.*;

/**
 * DebugExample class   Demonstrates how to use the Debug Class<br>
 *
 * Note:  You must use $JDBC_HOME/devclasses
 *
 * <P>DebugExample may be invoked with the optional parameters:<br>
 * -U username<br>
 * -P password<br>
 * -D debuglibraries<br>
 * -S server<p>
 *
 *  @see Sample
 */
public class DebugExample extends Sample
{

    DebugExample()
    {
        super();
    }

    public void sampleCode()
    {

        Debug debug = _sybDriver.getDebug();

        String traceOutputFile = "." + System.getProperty("file.separator")
            + "Debug.trace";

        // Trace specific classes.  Could have specified "ALL" to trace
        // all classes
        String debugLibs = "SybConnection:SybStatement:Debug:STATIC";
        String selectQuery = "select pub_id, title_id, title from titles";

        try
        {

            // Open a PrintStream where to redirect the trace output
            PrintStream debugLogStream = null;
            if(!_anApplet)
            {
                debugLogStream = new PrintStream(new
                    FileOutputStream(traceOutputFile));
            }
            else
            {
                //since can't create a file over the wire, 
                //simply print to standard out - Java Console
                error("Since you are running as an applet, \n"
                    +" A log file could not be created, so \n"
                    +" any log output will be sent to stdout.\n");
                debugLogStream = System.out;
            }

            // Enable Debug Tracing
            debug.debug(true, debugLibs, debugLogStream);

            // Write a message to the Debug PrintStream
            debug.println( "*** Calling Connection.createStatement ***");
            Statement stmt = _con.createStatement();
            debug.startTimer(null);
            output("Executing: " + selectQuery + "\n");
            ResultSet rs = stmt.executeQuery (selectQuery);
            debug.stopTimer(null,"*****executeQuery snapshot");
            dispResultSet(rs);

            // Demonstrate the use of asrt()
            // Note that we don't use assert(), because in JDK1.4, assert
            // is a keyword.

            debug.asrt(null, false,"Test of debug.asrt");
            stmt.close();
            rs.close();
        }
        catch (SQLException ex)
        {
            displaySQLEx(ex);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
}
