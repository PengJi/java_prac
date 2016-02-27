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
 * ExecuteQueryNumeric class demonstrates how to use the executeQuery method when Numeric Overflow errors<br>
 * occur.  3 scenarios are tested:<br>
 * 1. Arithmetic overflow - select power(2,31)<br>
 * 2. Divide by Zero - select 1/0<br>
 * 3. Domain error - select sqrt(-3)<br>
 *
 * <P>ExecuteQuery may be invoked with the optional parameters:<br>
 * -U username<br>
 * -P password<br>
 * -D debuglibraries<br
 * -S server<p>
 *
 *  @see Sample
 */

public class ExecuteQueryNumeric extends Sample
{


    ExecuteQueryNumeric()
    {
        super();
    }

    public void sampleCode()
    {
	String arithOverflowQuery = "select power(2,31)";
	String divideByZeroQuery = "select 1/0";
	String domainErrorQuery= "select sqrt(-1)";
	
        try 
        {


		// Execute the desired DML statement and then call dispResultSet to
		// display the rows and columns
		// Arithmetic overflow sample
		Statement stmt = _con.createStatement();;
		output("Executing: " + arithOverflowQuery + "\n");
		ResultSet rs = stmt.executeQuery (arithOverflowQuery);
		dispResultSet(rs);

		// Check for the Warning.  We need to check after checking
		// for the ResultSet - these types of numeric errors are 
		// sent back from server in TDS_EED as severity 10 in most cases
		// Since the query first sends back an empty result set we need to 
		// check for the SQLWarning after we have checked for ResultSet
		checkForWarning(rs.getWarnings());

		// close result set and statement and do another...
		rs.close();
		stmt.close();

		// Divide by Zero
		stmt = _con.createStatement();;
		output("Executing: " + divideByZeroQuery + "\n");
		rs = stmt.executeQuery (divideByZeroQuery);
		dispResultSet(rs);
		checkForWarning(rs.getWarnings());

		// close result set and statement and do another...
		rs.close();
		stmt.close();

		// Domain Error
		stmt = _con.createStatement();;
		output("Executing: " + domainErrorQuery + "\n");
		rs = stmt.executeQuery (domainErrorQuery);
		dispResultSet(rs);
		checkForWarning(rs.getWarnings());

		// close result set and statement
		rs.close();
		stmt.close();
	}
	catch (SQLException ex)   
	{
		displaySQLEx(ex);
	}
    }
}

