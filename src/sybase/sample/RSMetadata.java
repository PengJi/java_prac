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
 * RSMetadata class demonstrates how to use the ResultSetMetaData class<br>
 *
 * <P>RSMetadata may be invoked with the optional parameters:<br>
 * -U username<br>
 * -P password<br>
 * -D debuglibraries<br>
 * -S server<p>
 *
 *  @see Sample
 */
public class RSMetadata extends Sample
{
    // Available ResultSetMetaData Methods 
    static String _methods[] =
    {
        "getCatalogName", "getColumnCount", "getColumnDisplaySize",
            "getColumnLabel", "getColumnName",  "getColumnType",
            "getColumnTypeName",
            "getPrecision",   "getScale",       "getSchemaName",
            "getTableName",   "isAutoIncrement","isCaseSensitive",
            "isCurrency",     "isDefinitelyWritable", "isNullable",
            "isReadOnly",     "isSearchable",   "isSigned", "isWritable"
    }
    ;


    RSMetadata()
    {
        super();
    }

    public void sampleCode()
    {
        String query = "select title_id, price from titles";

        try
        {

            // Execute the Query so that we can examine the ResultSetMetaData
            Statement stmt = _con.createStatement();
            output("Executing: " + query + "\n");
            ResultSet rs = stmt.executeQuery(query);

            // Display the ResultSetMetaData Fields

            output("ResultSetMetaData.columnNoNulls= " + 
                ResultSetMetaData.columnNoNulls  + "\n");
            output("ResultSetMetaData.columnNullable= " + 
                ResultSetMetaData.columnNullable  + "\n");
            output("ResultSetMetaData.columnNullableUnknown= " + 
                ResultSetMetaData.columnNullableUnknown  + "\n");

            // Get the ResultSetMetaData.  This will be used for
            // the column headings

            ResultSetMetaData rsmd = rs.getMetaData ();
            output(
                "Display ResultSetMetaData for query using column 1\n\n");


            for(int offset= 0; offset < _methods.length; offset++)
            {
                output("method= " + _methods[offset] +
                    ", offset= " + offset + "\n");
                try
                {
                    switch(offset)
                    {
                        case 0:
                            output("getCatalogName(1)= " +
                                rsmd.getCatalogName(1) + "\n");
                            break;
                        case 1:
                            output("getColumnCount()= " +
                                rsmd.getColumnCount ()  + "\n");
                            break;
                        case 2:
                            output("getColumnDisplaySize(1)= " +
                                rsmd.getColumnDisplaySize(1) + "\n");
                            break;
                        case 3:
                            output("getColumnLabel(1)= " +
                                rsmd.getColumnLabel(1) + "\n");
                            break;
                        case 4:
                            output("getColumnName(1)= " +
                                rsmd.getColumnName(1) + "\n");
                            break;
                        case 5:
                            output("getColumnType(1)= " +
                                rsmd.getColumnType(1) + "\n");
                            break;
                        case 6:
                            output("getColumnTypeName(1)= " +
                                rsmd.getColumnTypeName(1) + "\n");
                            break;
                        case 7:
                            output("getPrecision(1)= " +
                                rsmd.getPrecision(1) + "\n");
                            break;
                        case 8:
                            output("getScale(1)= " +
                                rsmd.getScale(1) + "\n");
                            break;
                        case 9:
                            output("getSchemaName(1)= " +
                                rsmd.getSchemaName(1) + "\n");
                            break;
                        case 10:
                            output("getTableName(1)= " +
                                rsmd.getTableName(1) + "\n");
                            break;
                        case 11:
                            output("isAutoIncrement(1)= " +
                                rsmd.isAutoIncrement(1) + "\n");
                            break;
                        case 12:
                            output("isCaseSensitive(1)= " +
                                rsmd.isCaseSensitive(1) + "\n");
                            break;
                        case 13:
                            output("isCurrency(1)= " +
                                rsmd.isCurrency(1) + "\n");
                            break;
                        case 14:
                            output("isDefinitelyWritable(1)= " +
                                rsmd.isDefinitelyWritable(1) + "\n");
                            break;
                        case 15:
                            output("isNullable(1)= " +
                                rsmd.isNullable(1) + "\n");
                            break;
                        case 16:
                            output("isReadOnly(1)= " +
                                rsmd.isReadOnly(1) + "\n");
                            break;
                        case 17:
                            output("isSearchable(1)= " +
                                rsmd.isSearchable(1) + "\n");
                            break;
                        case 18:
                            output("isSigned(1)= " +
                                rsmd.isSigned(1) + "\n");
                            break;
                        case 19:
                            output("isWritable(1)= " +
                                rsmd.isWritable(1) + "\n");
                            break;
                    }
                }
                catch (SQLException sqe)
                {
                    String sqlstate = sqe.getSQLState();
                    String message = sqe.toString();
                    output("*** " + _methods[offset] +
                        "() not suported ***\n");

                }


            }

            stmt.close();
            rs.close();
        }
        catch (SQLException ex)
        {
            displaySQLEx(ex);
        }
    }
}
