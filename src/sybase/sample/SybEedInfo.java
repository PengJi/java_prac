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
import com.sybase.jdbcx.EedInfo;

/**
 * SybEedInfo class demonstrates how to use the EedInfo interface
 * class which extends SQLException which extends SQLWarnings.<p>
 *
 * The program also demonstrates how to obtain the output from the
 * T-SQL print command via SQLWarning.<p>
 *
 * SybEedInfo may be invoked with the optional parameters:<br>
 * -U username<br>
 * -P password<br>
 * -D debuglibraries<br>
 * -S server<p>
 *
 *  @see Sample
 */
public class SybEedInfo extends Sample
{

    SybEedInfo()
    {
        super();
    }

    public void sampleCode()
    {

        String createQuery = "create table #test(f1 int, f2 char(10))";
        String insertQuery = "insert #test values(1,NULL)";


        try
        {

            // Demonstrate SQLWarning

            execDDL("print 'hello world'");

            // Create our table

            execDDL(createQuery);

            // Now insert our data which will result in an SQLException
            // due to inserting a NULL into a non-NULL column

            execDDL(insertQuery);

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
     * @exception SQLException .
     */

    public boolean checkForWarning (SQLWarning warn) throws SQLException
    {
        boolean rc = false;

        // If a SQLWarning object was given, display the
        // warning messages.  Note that there could be
        // multiple warnings chained together

        if (warn != null)
        {
            output("\n *** Warning ***\n");
            rc = true;
            while (warn != null)
            {

                output ("Error:   " + warn.getErrorCode () +"\n");
                output ("Message:  " + warn.getMessage () + "\n");

                if(warn instanceof EedInfo)
                {
                    // This SQLWarning contains additional SAP Adaptive
                    // Server error message info.

                    EedInfo eed = (EedInfo) warn;
                    output("      Severity: " + eed.getSeverity() + "\n");
                    output("   Line Number: " + eed.getLineNumber() +
                        "\n");
                    output("   Server Name: " + eed.getServerName() +
                        "\n");
                    output("   Error State: " + eed.getState() + "\n");
                    output("Procedure Name: " + eed.getProcedureName() +
                        "\n");

                }

                output ("SQLState: " + warn.getSQLState () + "\n");
                warn = warn.getNextWarning ();
            }
        }
        return rc;
    }

    /**
     * Overload displaySQLEx to support EedInfo
     * @param ex   SQLException object
     */
    public void displaySQLEx(SQLException ex)
    {
        // A SQLException was generated.  Catch it and
        // display the error information.  Note that there
        // could be multiple error objects chained
        // together

        output ("\n*** SQLException caught ***\n");

        while (ex != null) 
        {

            output ("Error:   " + ex.getErrorCode ()+ "\n");
            output ("Message:  " + ex.getMessage () + "\n");

            if(ex instanceof EedInfo)
            {
                // This SQLException contains additional SAP Adaptive
                // Server error message info.

                EedInfo eed = (EedInfo) ex;
                output("      Severity: " + eed.getSeverity() + "\n");
                output("   Line Number: " + eed.getLineNumber()+"\n");
                output("   Server Name: " + eed.getServerName()+"\n");
                output("   Error State: " + eed.getState() + "\n");
                output("Procedure Name: " + eed.getProcedureName()
                    + "\n");

            }

            output ("SQLState: " + ex.getSQLState () + "\n\n");
            ex = ex.getNextException ();
        }

    }
}
