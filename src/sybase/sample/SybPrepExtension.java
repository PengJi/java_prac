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
import java.math.BigDecimal;
import com.sybase.jdbcx.SybPreparedStatement;

/**
 * SybPrepExtension class demonstrates how to use the SAP extensions
 * to the PreparedStatement interface. Specifically it shows how you can
 * explicitly control the precision and scale of a NUMERIC parameter that
 * is sent to the server through setBigDecimal.
 * <P> There are situations where the precision and scale of the parameter
 * must precisely match the precision/scale of the corresponding SQL object,
 * whether it be a stored procedure parameter or a column.
 *
 * <UL>
 *   <LI> Prepares a parameterized select statement
 *   <LI> Passes parameters to the Statement
 *   <LI> Executes the Callable Statement
 *   <LI> Retrieves and displays the returned result set
 * </UL> 
 *
 * <P>SybPrepExtension may be invoked with the optional parameters:<br>
 * -U username<br>
 * -P password<br>
 * -D debuglibraries<br>
 * -S server<p>
 *
 *  @see Sample
 */

public class SybPrepExtension extends Sample
{

    SybPrepExtension()
    {
        super();
    }

    public void sampleCode()
    {
        String query = "select * from discounts where discount > ?";

        try 
        {
            PreparedStatement cstmt = _con.prepareStatement(query);
            // Downcast this to a SybPreparedStatement so we can access
            // the extension
            SybPreparedStatement sps = (SybPreparedStatement) cstmt;
            BigDecimal discountThreshold = new BigDecimal("8.5");
            // force jConnect to send this big decimal with a precision of
            // 5 digits, and a scale of 2 digits
            sps.setBigDecimal(1, discountThreshold, 5, 2);
            displayRows(sps);
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
     * @param  stmt  PreparedStatement object to be processed
     * @exception SQLException .
     */
    public void displayRows( PreparedStatement stmt)
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
