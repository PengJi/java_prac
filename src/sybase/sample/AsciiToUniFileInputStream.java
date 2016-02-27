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
import java.sql.*;
import java.io.*;

/**
 *
 *  AsciiToUniFileInputStream class assists in reading a byte stream and
 *  converting it so that it appears to be a unicode stream
 *
 *
 * <P>A char/varchar/longvarchar column value can be retrieved as a
 * stream of Unicode characters. This class extends PadByteInputStream by
 * converting stream of SQL char data to a stream of unicode chacters.
 * 
 * <P>How Object is created:<br>
 * The object is created when user calls ResultSet.getUnicodeStream().
 * <P>Lifetime of the object:<br>
 * It may be passed to the user (from getFooStream()), and will not be
 * garbage collected until the user discards all references to it.
 * <P>What happens when object is destroyed:<br>
 * No special action is needed on cleanup.
 * <P> This class is used by the sample program UnicodeStream.java
 *
 * @see java.io.FilterInputStream
 * @see com.sybase.jdbcx.SybResultSet#getUnicodeStream
 * @see Sample
 * @see UnicodeStream
 */
public class AsciiToUniFileInputStream extends FilterInputStream
{

    protected int _padByteLengthRemaining;
    // Array holding two consecutive read() return values. Each read() returns
    // one of the slots in this array
    protected int _bytes[];
    // This boolean flag indicates which of the two slots in the above
    // byte[] should be returned from the read.
    protected boolean _even;

    // Constructors:
    /**
    * <P> Create a new AsciiToUniFileInputStream
    * @param stream the FileInputStream this is a wrapper on
    * @param length how many characters are in the stream
    */
    public AsciiToUniFileInputStream(FileInputStream stream, int length )
        throws IOException
    {

        super(stream);
        _bytes = new int[2];
        _padByteLengthRemaining = length * 2;
        _even = true;
    }

    /**
     * <P>Return the next byte of a Unicode character.
     * @exception IOException .
     */
    public int read() throws IOException
    {
        //* DONE
        if (_padByteLengthRemaining == 0)
        {
            return -1;
        }
        if (_even)
        {
            _bytes[1] = super.read();
            if (-1 == _bytes[1])
            {
                return -1;
            }
        }
        _padByteLengthRemaining--;
        _even = !_even;
        return _even ? _bytes[1] : _bytes[0];
    }


}
// end of class AsciiToUniFileInputStream
