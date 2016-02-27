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
import com.sybase.jdbcx.*;

/**
 *  
 *
 * PrintExample class demonstrates how to use the SQLWarning class
 * to process the T-SQL Print command<p>
 * 
 * Note:  This example will also have a SQLWarning occur with a
 * message and state of 010P4.  This occurs due to the fact that
 * I executed a stored procedure and ignored the return status. This
 * is normal behaviour when I execute a sproc via Statement.execute().<p>
 *
 * Due to permissions restricting a guest from creating procedures,
 * we will not be creating a stored procedure, only executing it.
 * The required stored procedures have been pre-loaded
 * onto our demo server, and included in pubs2_sql.sql or pubs2_any.sql
 * for you to be able to run them from your server.
 * We have included comments on how you would actually execute the
 * creation of a stored procedure and drop within the *CREATE PROCEDURE*
 * comments throughout the sample.
 *
 * PrintExample may be invoked with the optional parameters:<br>
 * -U username<br>
 * -P password<br>
 * -D debuglibraries<br>
 * -S server<p>
 *
 *  @see Sample
 */

class PrintExample extends Sample
{

    PrintExample()
    {
        super();
    }

    public void sampleCode()
    {


        String procName = "sp_printExampleSample";

        /*   *CREATE PROCEDURE*
           String dropProc = "drop proc " + procName;
           String createProc =
               "create proc " + procName  +
               " as " +
               "print 'print statement 1'"  +
               "print 'print statement 2'"  +
               "\nselect au_id,au_fname, au_lname from pubs2..authors "  +
               "print 'print statement 3'"  +
               "\nselect title_id, type, price from pubs2..titles"  ;
        */


        try
        {


            // Demonstrate the use of the print SQLWarning
            execDDL("print 'hello world'");

            /* *CREATE PROCEDURE*
               //  Create the Proc
               execDDL(createProc);
            */

            Statement stmt = _con.createStatement();;
            output("Executing: " + procName + "\n");
            int rsnum = 0;                   // Number of Result Sets processed
            int rowsAffected = 0;
            boolean results = false;
            ResultSet rs = null;

            try
            {
                results = stmt.execute (procName);
                checkForWarning(stmt.getWarnings());
                stmt.clearWarnings();
            }
            catch(SQLException ex)
            {
                output("Exception on execute()\n");
                displaySQLEx( ex);

                // Because we received an SQLException, we need to poll to see
                // If there are more results to process.
                results = stmt.getMoreResults();
                checkForWarning(stmt.getWarnings());
                stmt.clearWarnings();
            }
            do
            {

                if(results)
                {
                    try
                    {
                        rs = stmt.getResultSet();
                        checkForWarning(stmt.getWarnings());
                        stmt.clearWarnings();
                    }
                    catch(SQLException ex)
                    {
                        output("Exception while invoking getResultSet\n");
                        displaySQLEx(ex);
                    }
                    output("\n\nDisplaying ResultSet: " + rsnum + "\n");
                    dispResultSet(rs);
                    rsnum++;

                    rs.close();
                }
                else
                {
                    rowsAffected = stmt.getUpdateCount();
                    if (rowsAffected >= 0)
                    output(rowsAffected + " rows Affected.\n");
                }
                results = stmt.getMoreResults();
                checkForWarning(stmt.getWarnings());
                stmt.clearWarnings();
            }
            while (results || rowsAffected != -1);

            stmt.close();

            /*  *CREATE PROCEDURE*
               //  drop the Proc
               execDDL(dropProc);
            */


        }
        catch (SQLException ex)
        {
            displaySQLEx(ex);
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
                    output ("SQLState: " + warn.getSQLState () + "\n");
                }
                warn = warn.getNextWarning ();
            }
        }
        return rc;
    }

}
