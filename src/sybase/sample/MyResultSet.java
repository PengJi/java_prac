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
 * MyResultSet class demonstrates how to use the  ResultSet class.
 * This example uses the wasNull() method to check for SQL NULLs
 * <p>
 *
 * MyResultSet may be invoked with the optional parameters:<br>
 * -U username<br>
 * -P password<br>
 * -D debuglibraries<br>
 * -S server<p>
 *
 *
 *  @see Sample
 */
public class MyResultSet extends Sample
{


    MyResultSet()
    {
        super();
    }

    public void sampleCode()
    {
        String createQuery = "create table #foobar(f1 varchar(20) null,"  +
            " f2 int null)";
        String insertQuery = "insert #foobar values(null, 1)";
        String insertQuery1 = "insert #foobar values('Hello Lance',null)";
        String selectQuery = "select * from #foobar"; 

        try 
        {

            // create our table and populate it with two rows
            execDDL(createQuery);
            execDDL(insertQuery);
            execDDL(insertQuery1);

            // Execute the desired DML statement and then  display the 
            // rows and columns

            Statement stmt = _con.createStatement();;
            output("Executing: " + selectQuery + "\n");
            ResultSet rs = stmt.executeQuery (selectQuery);

            // Get the ResultSetMetaData.  This will be used for
            // the column headings

            ResultSetMetaData rsmd = rs.getMetaData ();
            int numCols = rsmd.getColumnCount ();

            // Display column headings

            for (int i=1; i<=numCols; i++) 
            {
                if (i > 1) output("\t\t");
                output(rsmd.getColumnLabel(i));
            }

            output("\n");

            // Display data, fetching until end of the result set

            while (rs.next ())
            {

                // Loop through each column, getting the
                // column data and displaying

                for (int i=1; i<=numCols; i++) 
                {
                    if (i > 1) 
                    output("\t\t");
                    String  foobar = rs.getString(i);

                    // Display NULL if a SQL NULL was encountered
                    output( rs.wasNull() ?   "NULL" : foobar);
                }
                output("\n");

                // Fetch the next result set row
            }

            // Close our resources

            rs.close();
            stmt.close();


        }
        catch (SQLException ex)   
        {
            displaySQLEx(ex);
        }
    }

}
