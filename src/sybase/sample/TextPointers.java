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
import java.awt.*;

import com.sybase.jdbcx.*;

/**
 * TextPointers shows how to use the TextPointer class to speed up blob 
 * insertions<br>
 *
 * <P>ExecuteQuery may be invoked with the optional parameters:<br>
 * -U username<br>
 * -P password<br>
 * -D debuglibraries<br>
 * -S server<p>
 *
 *  @see Sample
 */

public class TextPointers extends Sample
{


    TextPointers()
    {
        super();
    }

    public void sampleCode()
    {
        int res = 0;
        Statement stmt = null;
        SybResultSet rs = null;
        try 
        {
	    // A nice little picture of my german hometown
	    String fileName = _dir+"mb1.jpg";
	    java.net.URL source = new java.net.URL("file","localhost",fileName);

            // This can throw an IOException. make sure to catch that
	    InputStream in = source.openStream();
	    
	    // define a Statement
            stmt = _con.createStatement();

            // create a table and fill it with initial data
            res = stmt.executeUpdate("create table sampleimage(i int, t image)");
            res = stmt.executeUpdate("insert sampleimage values(1,0x00)");

            // Now we need the textpointer. The textpointer and the associated
            // timestamp are hidden in the beginning of the imagedata.
            String getText = "select t from sampleimage where i = 1";

            // Instead of transfering the whole (maybe big) picture over
            // the wire it's a good idea to limit the textsize to get just the
            // necessary bytes
            res = stmt.executeUpdate("set textsize 1");

            // We need to cast ResultSet to SybResultSet because the method
            // getTextPointer() (coming below) is an extension
            rs = (SybResultSet) stmt.executeQuery(getText);
            if (!rs.next())
	    {
	        output("Error reading initial Data\n");
	    }

            // Now fill the TextPointer class with information
            TextPointer tp = rs.getSybTextPointer(1);

            // After receiving the textpointer and timestamp (under the cover)
            // we want to transfer the new image. Hence, the textsize needs t
            // be adjusted again
            res = stmt.executeUpdate("set textsize 800000");

            // Should the update being logged?
            boolean log = false;

            // Now actually send the image
            tp.sendData(in, log);
	    
            // Check if it's really in there
	    Statement stmt2 = _con.createStatement();

            rs = (SybResultSet) stmt2.executeQuery("select datalength(t), t from sampleimage where i = 1");
            if (!rs.next())
	    {
	        output("Error reading the inserted Data\n");
	    }

	    // Read the image and its size
            int numBytes = rs.getInt(1);
	    InputStream inStream = rs.getBinaryStream(2);

	    int newBytes = 0;
	    byte[] buffer = new byte[numBytes];
	    newBytes = inStream.read(buffer);

            output(numBytes+ " Bytes of image data had been inserted\n");
            output(newBytes+ " Bytes of image data had been read again\n");
	    
	    // Display the image
	    ImageFrames f1 = new ImageFrames(); 
	    displayIt(f1, buffer);

            // That's all folks
        }
        catch (SQLException ex)   
        {
            displaySQLEx(ex);
        }
        catch (Exception io)   
        {
            io.printStackTrace();
        }
        finally
        {
            try
            {
               // Close our resources
               res = stmt.executeUpdate("drop table sampleimage");
               rs.close();
               stmt.close();
            }
            catch (Exception e)   
            {
               e.printStackTrace();
            }
        }
    }

    public static void displayIt(ImageFrames f, byte[] buffer) throws Exception
    {
        Image myImage = Toolkit.getDefaultToolkit().createImage(buffer);
	f.setImage (myImage);
    }
}

class ImageFrames extends Frame
{
  ImageFrames()
    {
      super();
      this.setSize(1135,765);
    }    
  
  public void paint (Graphics g)
    {
      try {
        if (image_to_display != null){
          g.drawImage(image_to_display, 0, 0, this);
        }
        else System.out.println("NULL");
      }    
      catch (Exception e){
        System.out.println("PAINT " + e);
      }    
    }
  public void setImage(Image new_image)
    {
      image_to_display = new_image;
      this.setTitle("Example for jConnect's new TextPointer. Image: Paderborn, Germany");
      this.setVisible(true);
      this.repaint();
      this.setVisible(true);
    }
  Image image_to_display;
}
