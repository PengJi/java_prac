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
 * ExecuteUpdate class demonstrates how to use the executeUpdate method<br>
 *
 * <P>ExecuteUpdate may be invoked with the optional parameters:<br>
 * -U username<br>
 * -P password<br>
 * -D debuglibraries<br>
 * -S server<p>
 *
 *  @see Sample
 */
public class ExecuteUpdate extends Sample
{

    ExecuteUpdate()
    {
        super();
    }

    public void sampleCode()
    {

        String createQuery = "create table #test(f1 int, f2 char(10))"; 
        String insertQuery = "insert #test values(1, 'Lance')"; 
        String updateQuery = "update #test set f2= 'Tonya'"; 
        String selectQuery = "select * from #test";

        try
        {

            // Create our table
            Statement statement = _con.createStatement();
            output("Executing: " + createQuery + "\n");
            int numrows = statement.executeUpdate(createQuery);
            output("Number of rows affected= " + numrows + "\n");

            // Now insert  our data
            output("Executing: " + insertQuery);
            numrows = statement.executeUpdate(insertQuery);
            output("Number of rows affected= " + numrows + "\n");

            // Display the new row
            output("Executing: " + selectQuery + "\n");
            ResultSet rs = statement.executeQuery (selectQuery);
            dispResultSet(rs);
            rs.close();


            // Now Update the row
            output("Executing: " + updateQuery + "\n");
            numrows = statement.executeUpdate(updateQuery);
            output("Number of rows affected= " + numrows + "\n");

            // Display the updated row
            output("Executing: " + selectQuery + "\n");
            rs = statement.executeQuery (selectQuery);
            dispResultSet(rs);
            rs.close();
        }
        catch (SQLException ex)
        {
            displaySQLEx(ex);
        }
    }
}
