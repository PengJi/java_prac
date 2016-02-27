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
 * ReadCursor class demonstrates how to use a read only cursor<p>
 *
 * ReadCursor may be invoked with the optional parameters:<br>
 * -U username<br>
 * -P password<br>
 * -D debuglibraries<br>
 * -S server<p>
 *
 *  @see Sample
 */

public class ReadCursor  extends Sample
{
    // Class specific global vars
    static public final int   MAXROWS = 1;

    ReadCursor()
    {
        super();
    }

    /**
     * Location where you can add commandline properties to the connection
     * The Super class will call this function before creating the
     * connection
     * @param  cmdLine    CommandLine settings
     * @see CommandLine
     */
    public void addMoreProps(CommandLine cmdline) 
    {
        // Optimize for READONLY cursor to fetch MAXROWS at a time
        // If we are using FOR UPDATE, then set CURSOR_ROWS to 1
        // as this is a driver wide property.

        cmdline._props.put("CURSOR_ROWS", Integer.toString(MAXROWS));
    }

    public void sampleCode()
    {
        String fname[] = 
        {
            "Ringer", "Green" 
        }
        ;
        String query = "select au_id, au_lname, au_fname from authors " +
            "where au_lname = ? for read only";

        try
        {

            // Execute the desired DML statement and then call dispResultSet to
            // display the rows and columns
            // This version uses setCursorName() with a read only cursor

            PreparedStatement pstmt = _con.prepareStatement(query );
            output("Executing: " + query+"\n");

            pstmt.setCursorName("author_Cursor");
            for(int i=0; i < fname.length; i++)
            {
                output("Current value for au_lname=" + 
                    fname[i]+"\n");
                pstmt.setString(1, fname[i]);

                ResultSet rs = pstmt.executeQuery ();
                output("Cursor Name = " + rs.getCursorName() +"\n");
                dispResultSet(rs);

                //  Clear our IN params
                pstmt.clearParameters();
                rs.close();
            }

            pstmt.close();
        }
        catch (SQLException sqe)
        {
            displaySQLEx(sqe);
        }
    }
}
