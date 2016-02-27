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
import sample2.ExtendedResultSet;

/**
 * ScrollableSample class demonstrates how to use the methods in the
 * ExtendedResultSet class.  This provides some of the functionality
 * found in the JDBC 2.0 ResultSet class for scrollable ResultSet support.<br>
 *
 * <P>ScrollableSample may be invoked with the optional parameters:<br>
 * -U username<br>
 * -P password<br>
 * -D debuglibraries<br>
 * -S server<p>
 *
 *  @see Sample
 */

public class ScrollableSample extends Sample
{


    ScrollableSample()
    {
        super();
    }

    public void sampleCode()
    {
        String query = "select pub_id, pub_name from publishers"; 

        try 
        {


            // Execute the desired DML statement and then call dispResultSet to
            // display the rows and columns

            Statement stmt = _con.createStatement();;
            output("Executing: " + query + "\n");
            ExtendedResultSet rs = new ExtendedResultSet(
                stmt.executeQuery (query));

            // Save the direction, concurrencty the Scrolling type for the
            // ResultSet.
            String direction = null;
            String concurrency = null;
            String scrollType = null;

            switch(rs.getFetchDirection())
            {
                case ExtendedResultSet.FETCH_FORWARD:
                    direction = "FETCH_FORWARD";
                    break;
                case ExtendedResultSet.FETCH_REVERSE:
                    direction = "FETCH_REVERSE";
                    break;
                case ExtendedResultSet.FETCH_UNKNOWN:
                    direction = "FETCH_UNKNOWN";
                    break;
            }

            switch(rs.getConcurrency())
            {
                case ExtendedResultSet.CONCUR_READ_ONLY:
                    concurrency = "CONCUR_READ_ONLY";
                    break;
                case ExtendedResultSet.CONCUR_UPDATABLE:
                    concurrency = "CONCUR_UPDATABLE";
                    break;
            }
            switch(rs.getType())
            {
                case ExtendedResultSet.TYPE_FORWARD_ONLY:
                    scrollType = "TYPE_FORWARD_ONLY";
                    break;
                case ExtendedResultSet.TYPE_SCROLL_INSENSITIVE:
                    scrollType = "TYPE_SCROLL_INSENSITIVE";
                    break;
            }

            output("\ngetFetchDirection()= " + direction + " (" +
                rs.getFetchDirection()  + ")\n" 
                + "getFetchSize() = " + rs.getFetchSize() + "\n"
                + "getType() = " + scrollType + " (" + rs.getType() + ")\n"
                + "getConcurrency() = " + concurrency + " (" +
                rs.getConcurrency() + ")");

            // Now demonstrate some of the Scrollable features
            output("\nisBeforeFirst() = " + rs.isBeforeFirst() + "\n");
            dispResultSet(rs);
            output("\nisAfterLast() = " + rs.isAfterLast());

            // Move to 1st row in ResultSet
            output("\nMove to 1st Row");
            rs.first();
            output("\nrow number = " + rs.getRow() + "  " +
                rs.getString(1) + ", " + rs.getString(2));
            output("\nisFirst() = " + rs.isFirst());

            // Move to 3rd row
            output("\nMove to Row 3");
            rs.absolute(3);
            output("\nrow number = " + rs.getRow() + "  " +
                rs.getString(1) + ", " + rs.getString(2));

            // Move to back 1 row
            output("\nMove back  1 Row ");
            rs.relative(-1);
            output("\nrow number = " + rs.getRow() + "  " +
                rs.getString(1) + ", " + rs.getString(2));

            // Move to back 1 row
            output("\nMove back  1 Row ");
            rs.previous();
            output("\nrow number = " + rs.getRow() + "  " +
                rs.getString(1) + ", " + rs.getString(2));

            // Move to last row
            output("\nMove to last Row ");
            rs.last();
            output("\nrow number = " + rs.getRow() + "  " +
                rs.getString(1) + ", " + rs.getString(2));
            output("\nisLast()= " + rs.isLast());


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
