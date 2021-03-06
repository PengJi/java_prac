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

// The next import is needed to pick up SybConnection, SybMessageHandler 
// and SybStatement
import com.sybase.jdbcx.*;

/**
 *
 * MessageHandler class demonstrates the use of the SybMessageHandler
 * class to create  a MessageHandler for a Connection and a Statement object
 *
 * SybMessageHandler implements the functionality provided by
 * callback messages in ctlib
 *
 * MessageHandler may be invoked with the optional parameters:<br>
 * -U username<br>
 * -P password<br>
 * -D debuglibraries<br>
 * -S server<p>
 *
 *  @see Sample
 */

class MessageHandler extends Sample
{

    static final int AVOID_SQLE = 20001;
    String _url = null;
    String _user = null;

    MessageHandler()
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
    }

    public void sampleCode()
    {

        try
        {
            String command = "raiserror " + AVOID_SQLE +
                "'I am a raiserror message!'";
            SybStatement stmt = null;

            output("Creating SybConnection instance\n");
            SybConnection con = (SybConnection) _con;
            Statement aStmt =  con.createStatement();

            // Execute Raiserror command without message handler
            output("Executing: " + command );
            output(" without a message handler\n");
            try
            {
                aStmt.executeUpdate(command);
            }
            catch(SQLException ex)
            {
                processSQLException(ex);
            }
            checkForWarning(aStmt.getWarnings());


            //  Now install message handler on a Connection and execute the
            //  Raiserror

            output("Installing Connection Message Handler\n");
            con.setSybMessageHandler(new ConnectionMsgHandler());
            stmt = (SybStatement) con.createStatement();
            output("Executing: " + command);
            output(" with a Connection message handler\n");
            stmt.executeUpdate(command);
            checkForWarning(stmt.getWarnings());
            stmt.clearWarnings();

            //  Now do the same thing for a Statement
            output("Installing Statement Message Handler\n");
            stmt.setSybMessageHandler(new StatementMsgHandler());
            output("Executing: " + command);
            output(" with a Statement message handler\n");
            stmt.executeUpdate(command);
            checkForWarning(stmt.getWarnings());

        }
        catch (Exception ex)
        {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }



    }
    /**
    * Overload checkForWarning
    * Checks for and displays warnings.  Returns true if a warning
    * existed
    * @param warn   SQLWarning object
    * @return       True if we displayed a warning
    */

    public boolean checkForWarning (SQLWarning warn) throws SQLException
    {
        boolean rc = false;

        // If a SQLWarning object was given, display the
        // warning messages.  Note that there could be
        // multiple warnings chained together

        if (warn != null)
        {
            rc = true;
            while (warn != null)
            {
                if(warn.getErrorCode() == 0 && warn.getSQLState() == null)
                {

                    // Have encountered a t-sql print command
                    output (warn.getMessage () + "\n");
                }
                else
                {
                    output("\n *** Warning ***\n");
                    output ("Error:   " + warn.getErrorCode () +"\n");
                    output ("Message:  " + warn.getMessage () + "\n");
                    output ("SQLState: " + warn.getSQLState () + "\n")
                        ;
                }
                warn = warn.getNextWarning ();
            }
        }
        return rc;
    }
    /**
    * Message Handler for a specific Connection
    * Downgrades Raiserror 2001 to a SQLWarning
    * @param warn   SQLExeception object
    * @return       SQLExeception object
    */
    class ConnectionMsgHandler implements SybMessageHandler
    {
        public SQLException messageHandler(SQLException sqe)
        {
            output("\n###ConnectionMsgHandler###\n");
            dispException(sqe);
            int code = sqe.getErrorCode();  
            if (code == AVOID_SQLE)
            {
                output("User " + _user + " downgrading " +
                    AVOID_SQLE + " to a warning\n");
                sqe = new SQLWarning(sqe.getMessage(), sqe.getSQLState(),
                    sqe.getErrorCode());
            }
            output("###Leaving ConnectionMsgHandler###\n");
            return sqe;
        }
    }
    /**
    * Message Handler for a specific Connection
    * Downgrades Raiserror 2001 to a SQLWarning
    * @param warn   SQLExeception object
    * @return       SQLExeception object
    */
    class StatementMsgHandler implements SybMessageHandler
    {
        public SQLException messageHandler(SQLException sqe)
        {
            output("\n###StatementMsgHandler###\n");
            dispException(sqe);
            int code = sqe.getErrorCode();
            if (code == AVOID_SQLE)
            {
                output("User " + _user + " downgrading " +
                    AVOID_SQLE + " to a warning\n");
                sqe = new SQLWarning(sqe.getMessage(), sqe.getSQLState(),
                    sqe.getErrorCode());
            }
            output("###Leaving StatementMsgHandler###\n");
            return sqe;
        }
    }

    protected void dispException(SQLException sqe)
    {
        output("### Message = " + sqe.toString());
        output("\n### sqlState = " + sqe.getSQLState() +
            ", Error Num = " + sqe.getErrorCode());

        if (sqe instanceof EedInfo)
        {
            output("\n### state = " + ((EedInfo)sqe).getState() +
                ", severity = " + ((EedInfo)sqe).getSeverity());
            output("\n### serverName = " + ((EedInfo)sqe).getServerName() + 
                ", procName = " + ((EedInfo)sqe).getProcedureName() +
                ", lineNum = " + ((EedInfo)sqe).getLineNumber() + "\n");

            ResultSet params = ((EedInfo)sqe).getEedParams();
            try
            {
                dispResultSet(params);
            }
            catch (SQLException e)
            {
                output("Unable to display Eed Params: " + e.toString() +
                    ": sqlState = " + e.getSQLState());
            }
        }
    }

    /**
    * @param ex  SQLException object
    * @return    A boolean stating whether we are here due to Raiserror
    */
    protected boolean processSQLException(SQLException ex)
    {
        // A SQLException was generated.  Catch it and
        // display the error information.  Note that there
        // could be multiple error objects chained
        // together

        boolean gotRaiserror = false;


        while (ex != null)
        {
            if(ex.getSQLState()== null && ex.getErrorCode() >= 17000)

            {
                // The SQLException is due to a raiserror command.

                output("***Raiserror encountered***\n");
                output ("Error:   " + ex.getErrorCode () + "\n");
                output ("Message:  " + ex.getMessage () + "\n");
                gotRaiserror = true;

            }
            else
            {
                output ("\n*** SQLException caught ***\n");
                output ("Error:   " + ex.getErrorCode ()+ "\n");
                output ("Message:  " + ex.getMessage () + "\n");
                output ("SQLState: " + ex.getSQLState () + "\n\n");
            }
            ex = ex.getNextException ();
        }

        return(gotRaiserror);

    }
}
