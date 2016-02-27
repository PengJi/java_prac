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
 * GetObject class demonstrates how to use the getObject method. In
 * This example, we will get a datetime column using getObject() and
 * getTimestamp() and demonstrate that they are both Timestamp objects<p>
 *
 * GetObject may be invoked with the optional parameters:<br>
 * -U username<br>
 * -P password<br>
 * -D debuglibraries<br>
 * -S server<p>
 *
 *  @see Sample
 */
public class GetObject extends Sample
{

    GetObject()
    {
        super();
    }

    public void sampleCode()
    {

        String query = "select pub_id, pubdate, pubdate from titles";

        try
        {

            // Create our table
            Statement statement = _con.createStatement();
            output("Executing: " + query + "\n");

            ResultSet rs = statement.executeQuery (query);
            // Get the ResultSetMetaData.  This will be used for
            // the column headings
            ResultSetMetaData rsmd = rs.getMetaData ();

            // Get the number of columns in the result set

            int numCols = rsmd.getColumnCount ();

            // Display column headings

            for (int i=1; i<=numCols; i++) 
            {
                if (i > 1) 
                output("\t\t");
                output(rsmd.getColumnLabel(i));
            }
            output("\n");


            // Display data, fetching until end of the result set

            while (rs.next ())
            {

                // Loop through each column, getting the column data 
                // Columns 2 and 3 are defined as datetime.
                // We will get the column 2 using getObject and column 3
                // using getTimestamp.

                output(rs.getString(1));
                Object a = rs.getObject(2);
                output("\t" + a);

                if(a instanceof Timestamp)
                output(" (is a Java Timestamp)");
                Timestamp ts = rs.getTimestamp(3);
                output("\t" + ts);

                if(ts instanceof Timestamp)
                output(" (is a Java Timestamp)");
                output("\n");

                // Fetch the next result set row

            }
            rs.close();

        }
        catch (SQLException ex)
        {
            displaySQLEx(ex);
        }
    }
}
