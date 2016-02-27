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

// The next import is needed to pick up SybConnection, SybEventHandler
import com.sybase.jdbcx.*;

/**
 *
 * EventHandler class demonstrates the use of the SybEventHandler
 * class to create  a EventHandler for a Connection to a SAP
 * Open Server.
 *
 * SybEventHandler implements the functionality provided by
 * event notification callbacks in ctlib.  This example should be run
 * with the registered procedure example provided with the SAP OCS.
 * This program replaces the function of the 'ctwait' and 'dbwait'
 * examples.
 *
 * EventHandler may be invoked with the optional parameters:<br>
 * -U username<br>
 * -P password<br>
 * -D debuglibraries<br>
 * -S server<p>
 *
 *  @see Sample
 */

class EventHandler extends Sample
{
    private static final String REG_PROC = "rp_version";

    String _url = null;
    String _user = null;

    EventHandler()
    {
        super();
    }

    public void run()
    {

        try
        {
            addMoreProps(_cmdline);

            _url=  _cmdline._props.getProperty("server");
            _user= _cmdline._props.getProperty("user");

            // Load the Driver

            DriverManager.registerDriver((Driver)
                Class.forName("com.sybase.jdbc4.jdbc.SybDriver").newInstance());

            _con = DriverManager.getConnection(
                _cmdline._props.getProperty("server"), _cmdline._props);

            // Run the sample specific code
            sampleCode();
        }
        catch (SQLException ex)
        {
            displaySQLEx(ex);
            output("Could not connect RDBMS using the URL: ");
            output(_url + "\n");

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
        finally
        {
            try
            {
                // Close the connection
                if (_con != null)
                _con.close();
            }
            catch(SQLException sqe)
            {
                // ignore
            }
        }
    }

    public void sampleCode()
    {

        try
        {
            output("Creating SybConnection instance\n");
            SybConnection con = (SybConnection) _con;
            output("Creating Event Handler instance\n");
            MyEventHandler myHdlr = new MyEventHandler();

            output("Register '" + REG_PROC + "' as the event to watch\n");
            con.regWatch(REG_PROC, myHdlr, SybEventHandler.NOTIFY_ALWAYS);

            //  Now wait for the event to happen

            synchronized (this)
            {
                output("Waiting for event to occur\n");
                wait();
                output("Running again after event occurred\n");
            }

            output("Unregister the event '" + REG_PROC + "'\n");
            con.regNoWatch(REG_PROC);
        }
        catch (Exception ex)
        {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
    }

    /**
    * Main application thread is notified that an event occurred
    * @param name Name of the event.
    */
    public synchronized void eventNotice(String name)
    {
        output("event handler " + name + " got notified.");
        // free the wait state of the 
        notify();
    }

    /**
    * Event Handler for a specific Connection
    * @param eventName  Name of the event.
    * @param params  Input parameters that were passed to the event 
    */
    class MyEventHandler implements SybEventHandler
    {
        public void event(String eventName, ResultSet params)
        {
            output("Event thread notified for event, " + eventName);
            try
            {
                dispResultSet(params);
            }
            catch (SQLException e)
            {
                output("Unable to display event Params: " + e.toString() +
                    ": sqlState = " + e.getSQLState());
            }
            eventNotice(eventName);
        }
    }
}

