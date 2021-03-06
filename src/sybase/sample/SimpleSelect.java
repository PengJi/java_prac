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
 * The SimpleSelect class demonstrates how a simple select 
 * statement is executed.
 * <UL>
 * <LI>It creates a connection and a statement.
 * <LI>Then query is executed. The pubs2 database needs to be installed
 * in your SQL Server. If it is not installed change the query string
 * and recompile SimpleSelect.
 * <LI>The result set is displayed.
 * <LI>Finally, the statement and connection are closed.
 * </UL>
 *
 * <P>SimpleSelect may be invoked with the optional parameters:<br>
 * -U username<br>
 * -P password<br>
 * -D debuglibraries<br>
 * -S server<p>
 *
 *  @see java.sql.Connection
 *  @see java.sql.Statement
 *  @see Sample
 */

public class SimpleSelect extends Sample
{


    SimpleSelect()
    {
        super();
    }

    public void sampleCode()
    {
        String query = "SELECT * FROM pubs2..titles"; 

        try 
        {

            // Get the DatabaseMetaData object and display
            // some information about the connection
            // by calling  a method that displays driver name and version

            printDriverInfo();

            // Execute the desired DML statement and then call dispResultSet to
            // display the rows and columns

            Statement stmt = _con.createStatement();;
            output("Executing: " + query + "\n");
            ResultSet rs = stmt.executeQuery (query);
            dispResultSet(rs);

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
