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
 * Escape class demonstrates how to use  jConnect Escape Syntax<p>
 *
 * Escape may be invoked with the optional parameters:<br>
 * -U username<br>
 * -P password<br>
 * -D debuglibraries<br>
 * -S server<p>
 *
 *  @see Sample
 */
public class Escape  extends Sample
{

    static public final int   MAXROWS = 5;
    static public final int   MAJORVERSION = 2;
    static public final int   JDBC_CLIENT = 5;

    Escape()
    {
        super();
    }

    public void sampleCode()
    {

        String queryFN = "select {fn database()}"; 
        String queryLike = 
            "select name from syscolumns where name like '%\\_%' {escape '\\'}"; 
        String queryDate =
            "select * from sales where date = {d '1991-03-20'}"; 
        String queryTimestamp =
            "select * from sales where date = {ts '1988-01-13 00:00:00'}"; 
        String queryCall = "{call sp_mda(?,?)}";

        //  Note, this currently is only supported by SQL Anywhere
        String queryOuterJoin=
            "select au_fname, au_lname, pub_name from {oj authors " +
            "left outer join publishers on authors.city = publishers.city}";


        try 
        {

            //  Display escape scalar function example
            displayRows(queryFN);

            //  Display like escape example
            displayRows(queryLike);

            //  Display date escape example
            displayRows(queryDate);

            //  Display timestamp escape example
            displayRows(queryTimestamp);

            //  Display outer join escape example
            try
            {
                displayRows(queryOuterJoin);
            }
            catch(SQLException ex)
            {

                // If this is Adaptive (SQL) Server, ANSI Left Outer Join
                // is not supported so display a message
                // For other RDBMS, a different error code might occur
                // Adapative Server Anywhere does support this.

                if (ex.getErrorCode() == 102)
                {
                    output("**ANSI Left Outer Join is not supported**\n");
                }
                else
                {
                    throw ex;
                }
            }

            //  Demonstrate escape call example
            CallableStatement cs = _con.prepareCall(queryCall);
            cs.setInt(1, JDBC_CLIENT);
            cs.setInt(2, MAJORVERSION);

            cs.setMaxRows(MAXROWS);
            output("Executing: " + queryCall +"\n");

            ResultSet rs = cs.executeQuery ();
            dispResultSet(rs);
            rs.close();
            cs.close();


        }
        catch (SQLException sqe)
        {
            displaySQLEx(sqe);
        }
    }


    /**
     * Execute the desired DML statement and then call dispResultSet to
     * display thre rows and columns 
     * @param query  SQL query to execute
     * @exception SQLException .
     */
    public void displayRows( String query) 
        throws SQLException
    {

        Statement stmt = _con.createStatement();
        output("Executing: " + query + "\n");
        ResultSet rs = stmt.executeQuery (query);
        dispResultSet(rs);
        rs.close();
    }


}
