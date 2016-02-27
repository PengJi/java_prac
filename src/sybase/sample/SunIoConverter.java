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
import sun.io.*;

/**
* <P>This is an impure (but efficient) implementation of
* <CODE>CharsetConverter</CODE>
* which relies on the <CODE>sun.io</CODE> package, especially classes
* <CODE>ByteToCharConverter</CODE> and <CODE>CharToByteConverter</CODE>.
* It may be used with jConnect instead of the 100% Pure
* Java
* implementation by setting the <CODE>CHARSET_CONVERTER_CLASS</CODE>
* connection property to <CODE>sample2.SunIoConverter</CODE>.
* <P>
* <STRONG>NOTE:</STRONG> This class uses classes from the package
* <CODE>sun.io</CODE>, which may not be
* present in all Java VMs.  JavaSoft is free to change anything
* in the <CODE>sun.io</CODE> package in future releases.  If you decide to use
* this converter or any derivative of it in your application for
* performance reasons, you may need to
* make code changes
* in the future if <CODE>sun.io</CODE> changes.
* @sybshow
*/
public class SunIoConverter implements com.sybase.jdbcx.CharsetConverter
{

    // Members:
    private ByteToCharConverter _toUnicode;
    private CharToByteConverter _fromUnicode;
    // Constructor:
    public SunIoConverter() 
    {
        _toUnicode = ByteToCharConverter.getDefault();
        _fromUnicode = CharToByteConverter.getDefault();
    }

    //Methods:
    /**
    * @see com.sybase.jdbcx.CharsetConverter#setEncoding
    */
    public void setEncoding(String encoding) 
        throws UnsupportedEncodingException
    {
        _toUnicode = ByteToCharConverter.getConverter(encoding);
        _fromUnicode = CharToByteConverter.getConverter(encoding);
    }
    /**
    * @see com.sybase.jdbcx.CharsetConverter#fromUnicode
    */
    // NOTE: this method does not throw CharConversionException
    public byte[] fromUnicode(String fromString) throws CharConversionException
    {
        return(_fromUnicode.convertAll(fromString.toCharArray()));
    }

    /**
    * @see com.sybase.jdbcx.CharsetConverter#toUnicode
    */
    // NOTE: this method does not throw CharConversionException
    public String toUnicode(byte[] fromBytes) throws CharConversionException
    {
        return new String(_toUnicode.convertAll(fromBytes));
    }
}
