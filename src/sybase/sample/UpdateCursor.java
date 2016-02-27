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
 * UpdateCursor class demonstrates how to use  multiple Statement objects
 * to do an update using a cursor<br>
 *
 * <P>UpdateCursor may be invoked with the optional parameters:<br>
 * -U username<br>
 * -P password<br>
 * -D debuglibraries<br>
 * -S server<p>
 *
 *  @see Sample
 */
public class UpdateCursor extends Sample
{


    UpdateCursor()
    {
        super();
    }

    public void sampleCode()
    {

        String query =
            "select au_id, au_lname, au_fname from #authors  for update";
        String createQuery = "select * into #authors from authors" +
            "\ncreate unique clustered index myind on #authors(au_id)";
        String au_id= "486-29-1786";
        String au_fname = "Lance";
        String au_lname = "Andersen";
        String selectQuery =
            "select au_id, au_lname, au_fname from #authors where au_id ='" +
            au_id + "'";


        try
        {

            // Create our temp table and unique index for our cursor
            execDDL(createQuery);

            // Demonstrate how to use multiple Statement objects  to modify
            // an existing row using a cursor

            ResultSet rs = null;
            String cursorName = new String("read_authors");
            Statement stmt1 = _con.createStatement();
            Statement stmt2 = _con.createStatement();
            Statement stmt3 = _con.createStatement();

            // Display the row before the update
            output("Executing: " + selectQuery + "\n");
            rs = stmt3.executeQuery (selectQuery);
            dispResultSet(rs);
            rs.close();
            // Open our cursor
            stmt1.setCursorName(cursorName);
            output("Executing: " + query + "\n");
            rs = stmt1.executeQuery(query);

            String cursor = rs.getCursorName();
            while(rs.next())
            {
                if(rs.getString("au_id").equals(au_id))
                {
                    // Update our row using the current cursor position

                    output( "\n\nModifying: au_id= " + au_id +
                        ", au_lname= " + rs.getString("au_lname") +
                        ", au_fname= " + rs.getString("au_fname") + "\n");
                    String updateQuery = 
                        "update #authors set au_lname = '" + au_lname
                        + "',au_fname= '" + au_fname + 
                        "' where current of " + cursor;

                    output("Executing: " + updateQuery + "\n");
                    stmt2.executeUpdate(updateQuery);
                }
            }

            // Display the updated row
            output("Executing: " + selectQuery + "\n");
            rs = stmt3.executeQuery (selectQuery);
            dispResultSet(rs);
            rs.close();

            stmt1.close();
            stmt2.close();
            stmt3.close();

        }
        catch (SQLException ex)
        {
            displaySQLEx(ex);
        }
    }
}
