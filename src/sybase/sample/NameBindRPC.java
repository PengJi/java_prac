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
import com.sybase.jdbcx.*;

/**
 *
 * NameBindRPC class demonstrates how to execute a stored procedure
 * via a language command in a Callable Statement using T-SQL
 * Name-Binding for parameters.<p>
 * 
 * Due to permissions restricting a guest from creating procedures,
 * we will not be creating a stored procedure, only executing it.
 * The required stored procedures have been pre-loaded
 * onto our demo server, and included in pubs2_sql.sql or pubs2_any.sql
 * for you to be able to run them from your server.
 * We have included comments on how you would actually execute the
 * creation of a stored procedure and drop within the *CREATE PROCEDURE*
 * comments throughout the sample.
 *
 * NameBindRPC may be invoked with the optional parameters:<br>
 * -U username<br>
 * -P password<br>
 * -D debuglibraries<br>
 * -S server<p>
 *
 *  @see Sample
 */

class NameBindRPC extends Sample
{

    NameBindRPC()
    {
        super();
    }

    public void sampleCode()
    {


        String procName = "sp_nameBindRPCSample";
        String procStmt = "{?=call " + procName + " @p4=?, @p2=?}";

        /*   *CREATE PROCEDURE*
           String dropProc = "drop proc " + procName;
           String createProc =
               "create proc " + procName  +
               " (@p2 int, @p3 int = 47, @p4 char(30)) " +
               " as " +
               " print 'This is a print statement' " +
               " select @p2, @p3, @p4" +
               " return 1";

        */


        try
        {


            /* *CREATE PROCEDURE*
               //  Create the Proc
               execDDL(createProc);
            */

            CallableStatement cstmt = _con.prepareCall(procStmt);
            output("Executing: " + procName + "\n");

            // Define the input/output params
            cstmt.registerOutParameter(1, Types.INTEGER);
            cstmt.setString(2, "Yikes");
            cstmt.setInt(3, 123);

            ResultSet rs = cstmt.executeQuery();


            while(rs.next())
            {

                // we expect 123, 47, "Yikes" -- we passed in the first
                // and 3rd values (out of order) and picked up the
                // 2nd one as the default value.
                output("Expecting output of: 123, 47, 'Yikes'\n");
                for (int i = 1; i <= 3; i++)
                {
                    output("Column " + i + ": " + rs.getString(i) + "\n");
                }
                output("return status= " + cstmt.getString(1) + "\n");

            }
            cstmt.close();

            /*  *CREATE PROCEDURE*
               //  drop the Proc
               execDDL(dropProc);
            */


        }
        catch (SQLException ex)
        {
            displaySQLEx(ex);
        }
    }

}
