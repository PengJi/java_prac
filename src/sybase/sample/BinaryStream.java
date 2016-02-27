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
 * BinaryStream class demonstrates how to use the setBinaryStream
 * and getBinaryStream methods.<p>
 *
 * BinaryStream may be invoked with the optional parameters:<br>
 * -f filename        (default is atextfile)<br>
 * -U username<br>
 * -P password<br>
 * -D debuglibraries<br>
 * -S server<p>
 *
 *  @see Sample
 */
public class BinaryStream extends Sample
{

    static String _blobFile =  null;
    //   "sample" + File.separatorChar + "Ping.java";  // Default Textfile to load

    static String _setTextSize = "set textsize   100000"; // Default text size
    static String _extraCmdOption = "-f";


    BinaryStream()
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
                _blobFile = value;
                break;
            }
        }
    }

    public void sampleCode()
    {
        if (_blobFile == null)
        {
            _blobFile = _dir+"Ping.java";
        }
        String au_id = "409-56-7008";
        String createQuery = "create table #blobtable(f1 int, f2 Image)";
        String insertQuery = "insert #blobtable values(1, ?)";
        String selectQuery = "select f2 from #blobtable where f1=1";

        try
        {

            // set the the text column size for retrieval
            execDDL(_setTextSize);

            // Create our table
            execDDL(createQuery); 


            // Read Blob file and insert it into our table
            // Note that the file must exist

            InputStream instream = null;
            if(!_anApplet)
            {
                error("Opening file: " +_blobFile + "\n");
                File fd = new File(_blobFile);
                instream = new FileInputStream(fd);
            }
            else
            {
                error("Opening url: "+ _blobFile + "\n");
                java.net.URL source = new java.net.URL(_blobFile);
                instream = source.openStream();
            }
            PreparedStatement pstmt =
                _con.prepareStatement(insertQuery );

            output("\n****Now inserting a blob****\n");
            output("Executing " + insertQuery + "\n" );

            error("Inserting blob: " + _blobFile );
            pstmt.setBinaryStream(1, instream, (int)instream.available());
            pstmt.execute();
            pstmt.close();

            // Now Retrieve the image column and display the size of the column

            output("\n****Now Retrieving blob****\n");
            Statement stmt = _con.createStatement();;
            output("Executing: " + selectQuery + "\n");
            ResultSet rs = stmt.executeQuery(selectQuery);

            int numRead= 0;
            int numtot= 0;
            int tot = 0;
            while(rs.next())
            {
                byte stuff[] = new byte[10000];
                numRead= 0;
                numtot= 0;
                InputStream is = rs.getBinaryStream(1);
                for(;;)
                {
                    numRead= is.read(stuff);
                    if(numRead == -1)
                    {
                        break;
                    }
                    numtot += numRead;
                }
                tot++;
                output("\nBytes read for blob " + tot + "= " + numtot
                    +"\n");
            }

            rs.close();
            stmt.close();


        }
        catch (SQLException ex)
        {
            displaySQLEx(ex);
        }
        catch (FileNotFoundException ex)
        {
            error("****Cannot open: " + _blobFile + "\n");
        }
        catch (java.lang.Exception ex)
        {

            // Got some other type of exception.  Dump it.
            ex.printStackTrace ();
        }

    }
}
