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
 * Execute class demonstrates how to use the execute method<br>
 * Also uses Statement.setMaxRows() to limit the output<br>
 *
 * <P>Execute may be invoked with the optional parameters:<br>
 * -U username<br>
 * -P password<br>
 * -D debuglibraries<br>
 * -S server<p>
 *
 *  @see Sample
 */

public class Execute extends Sample
{
    // Max number of rows to  return in a ResultSet
    static public final int   MAXROWS = 5;  

    Execute()
    {
        super();
    }

    public void sampleCode()
    {


        String query = "sp_help titles"; 
        String query1 = "select pub_id, pub_name from publishers";
        String query2 = "select pub_id, pub_name from publishers" +
            "\n select au_fname, au_lname, state from authors";


        try 
        {

            // Execute sp_help which returns multiple ResultSets
            displayRows(query);

            // Execute a query returning 1 ResultSet
            displayRows(query1);

            // Execute a batch containing 2 select statements 
            displayRows(query2);
        }
        catch (SQLException ex)
        {
            displaySQLEx(ex);
        }
    }


    /**
     * Execute the desired DML statement and then call dispResultSet to
     * display the rows and columns
     * @param query  SQL Query to execute
     * @exception SQLException .
     */

    public void displayRows( String query)
        throws SQLException
    {

        Statement stmt = _con.createStatement();
        output("Executing: " + query + "\n");

        stmt.setMaxRows(MAXROWS);
        boolean results = stmt.execute (query);
        int rsnum = 0;                   // Number of Result Sets processed
        int rowsAffected = 0;       
        do
        {
            if(results)
            {
                ResultSet rs = stmt.getResultSet();
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
        }
        while (results || rowsAffected != -1);

        stmt.close();
    }

}
