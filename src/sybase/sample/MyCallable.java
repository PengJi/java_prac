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
 * MyCallable class demonstrates how to use the Callable class<br>
 *
 * <UL>
 *   <LI> Prepares a callable statement
 *   <LI> Passes parameters to the Callable Statement
 *   <LI> Executes the Callable Statement
 *   <LI> Retrieves and displays the return value, and output 
 *        parameters. 
 * </UL> 
 * Due to permissions restricting a guest from creating procedures, 
 * we will not be creating a stored procedure, only executing it.
 * The required stored procedures have been pre-loaded
 * onto our demo server, and included in pubs2_sql.sql or pubs2_any.sql
 * for you to be able to run them from your server.
 * We have included comments on how you would actually execute the
 * creation of a stored procedure and drop within the *CREATE PROCEDURE*
 * comments throughout the sample.
 *
 * <P>MyCallable may be invoked with the optional parameters:<br>
 * -U username<br>
 * -P password<br>
 * -D debuglibraries<br>
 * -S server<p>
 *
 *  @see Sample
 */

public class MyCallable extends Sample
{

    MyCallable()
    {
        super();
    }

    public void sampleCode()
    {


        String sproc = "{?=call sp_who}";
        String sproc2 = "{? = call sp_callableSample(?,?)}";
        /* *CREATE PROCEDURE*
           //Query to create the procedure
           String createSproc2 =
               "create procedure sp_callableSample"+
               "   (@p1 int, @p2 varchar(255) out)"+
               "   as"+
               "   begin"+
               "     select @p1, @p2"+
               "     select @p2 = 'The Answer to Life, the Universe, and Everything.'"+
               "     return  42"+
               "   end";
           String dropSproc2 = "drop proc sp_callableSample";
        */

        try 
        {
            // Excecute a sproc which has a return status only -sp_who
            CallableStatement cstmt = _con.prepareCall(sproc);
            cstmt.registerOutParameter(1, Types.INTEGER);
            output("Executing: " + sproc);
            displayRows(cstmt);

            // Display our Return Parameters
            output("\n\nReturn Parameters\n");
            output("OUT Param 1= " + cstmt.getString(1) + "\n");
            cstmt.close();

            // Now Excecute a sproc which has IN and INOUT params as
            // well as a return stuat
            // Note: SQL Server does not have an OUT only param.  jConnect 
            // will pass a NULL silently as an IN param if one is not passed.

            /* *CREATE PROCEDURE*
               //execute the creation of the stored procedure
               execDDL(createSproc2);
            */
            cstmt = _con.prepareCall(sproc2);
            output("Executing: " + sproc2 + "\n");

            // Declare the IN Params.  Note, you must skip the Return Status
            cstmt.setInt(2, 1961);
            cstmt.setString(3,"Hello");

            // Now declare our OUT Params
            cstmt.registerOutParameter(1, Types.INTEGER);
            cstmt.registerOutParameter(3, Types.VARCHAR);

            displayRows(cstmt);
            // Display our Return Parameters
            output("\n\nReturn Parameters\n");
            output("OUT Param 1= " + cstmt.getString(1) + "\n");
            output("OUT Param 3= " + cstmt.getString(3) + "\n");

            /* *CREATE PROCEDURE*
               // Now delete the sproc
               execDDL(dropSproc2);
            */

            // close our resources
            cstmt.close();

        }
        catch (SQLException ex)
        {
            displaySQLEx(ex);
        }
    }


    /**
     * Execute the desired Callable statement and then call dispResultSet to
     * display the rows and columns
     * @param  stmt  CallableStatement object to be processed
     * @exception SQLException .
     */
    public void displayRows( CallableStatement stmt)
        throws SQLException
    {

        boolean results = stmt.execute();
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

    }

}
