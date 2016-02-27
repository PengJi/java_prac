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
 * AsciiStream class demonstrates how to use the setAsciiStream
 * and getAsciiStream methods.<p>
 *
 * AsciiStream may be invoked with the optional parameters:<br>
 * -f filename        (default is atextfile)<br>
 * -U username<br>
 * -P password<br>
 * -D debuglibraries<br>
 * -S server<p>
 *
 *  @see Sample
 */

public class AsciiStream extends Sample
{
    static String _setTextSize = "set textsize   100000"; // Default text size
    static String _extraCmdOption = "-f";
    static String _textFile = null;        


    AsciiStream()
    {
        super();
    }
    /**
     * Location where you can add commandline properties to the connection
     * The Super class will call this function before creating the
     * connection
     * @param cmdLine command line operations
     * @see CommandLine
     */
    public void addMoreProps(CommandLine cmdLine)
    {
        Enumeration extras  = cmdLine._extraArgs.elements();
        Enumeration options = cmdLine._extraOptions.elements();
        while( extras.hasMoreElements())
        {

            String option = (String) extras.nextElement();
            String value = (String) options.nextElement();
            error("Extra options= " + option + " " + value + "\n");

            if(option.equals(_extraCmdOption))
            {
                _textFile = value;
                break;
            }
        }
    }

    /**
     * Demonstate AsciiStreams
     */
    public void sampleCode()
    {
        if (_textFile == null)
        {
            _textFile = _dir+"Ping.java";
        }
        String au_id = "409-56-7008";
        String createQuery = "select * into #blurbs from blurbs";
        String selectQuery = "select au_id, copy from #blurbs";
        String selectQuery2 = "select au_id, copy from #blurbs where au_id= '"
            + au_id + "'";
        String updateQuery = "update #blurbs set copy = ? where au_id = ?";

        try
        {

            // set the the text column size for retrieval
            error("\nExecuting setTextSize\n");
            execDDL(_setTextSize);

            // Create our table
            error("Executing createQuery\n");
            execDDL(createQuery); 

            // Read the data from our table display the text column
            error("Executing selectQuery\n");
            displayText(selectQuery);

            // Read in text and store it in our table
            // Note that the file must exist   
            InputStream instream = null;
            if(!_anApplet)
            {
                error("Opening file: " +_textFile + "\n");
                File fd = new File(_textFile);
                instream = (InputStream)new FileInputStream(fd);
            }
            else
            {
                error("Opening url: "+ _textFile + "\n");  
                java.net.URL source = new java.net.URL(_textFile);
                instream = source.openStream(); 
            }
            PreparedStatement pstmt =
                _con.prepareStatement(updateQuery );

            output("\n****Now updating text column where au_id= "
                + au_id + "****\n");
            output("Executing " + updateQuery + "\n" );
            pstmt.setAsciiStream(1, instream, (int) instream.available());
            pstmt.setString(2, au_id);
            pstmt.execute();
            pstmt.close();

            // Display the data so that we can demonstrate that we changed
            // the contents of the text column
            displayText(selectQuery2);


        }
        catch (SQLException ex)
        {
            displaySQLEx(ex);
        }
        catch (FileNotFoundException ex)
        {
            error("****Cannot open: " + _textFile + "\n");
        }
        catch (java.lang.Exception ex)
        {

            // Got some other type of exception.  Dump it.
            ex.printStackTrace ();
        }
        error("AsciiStream is done");
    }

    /**
    * Retrieve the contents of a textcolumn using getAsciiStream()
    * @param query sql statement to query to server
    * @exception SQLException .
    * @exception IOException .
    */
    public void displayText(String query)
        throws SQLException, IOException
    {
        Statement stmt = _con.createStatement();;
        output("Executing: " + query + "\n");
        ResultSet rs = stmt.executeQuery(query);

        int numRead= 0;
        int numtot= 0;
        int tot = 0;
        output("\n****Now Retrieving Text(s)****\n");
        while(rs.next())
        {
            byte stuff[] = new byte[10000];
            numRead= 0;
            numtot= 0;
            output("\nau_id= " + rs.getString(1) + "\n");
            StringBuffer resultBuffer = new StringBuffer();
            InputStream is = rs.getAsciiStream(2);
            for(;;)
            {
                numRead= is.read(stuff);
                if(numRead == -1)
                {
                    break;
                }
                numtot += numRead;
                resultBuffer.append(new String(stuff));
            }
            tot++;
            output("copy = " + resultBuffer.toString() + "\n");
            output("\nBytes read for copy " + tot + "= " + numtot +"\n");
        }

        rs.close();
        stmt.close();
    }


}
