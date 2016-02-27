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
/**
 * The Address class is used by the HandleObject sample, which shows
 * how to insert, update and delete data stored as a Java Object in a SAP ASE and SQLAnywhere 
 * Database.
 *
 * The HandleObject sample requires that this class and the AddressSubclass
 * class be put together into a jar and installed on the targeted server.
 * Use the following instructions to install the jar file:
 *
 * 1) Build the samples using the Makefile (for Unix) or make_nt.bat (NT).
 * 2) Place the sample2/Address.class and sample2/AddressSubclass.class files
 *    into a jar file:
 *    a) go to the $JDBC_HOME/classes directory
 *    b) run the jar command:
 *     jar cvf0 Address.jar sample2/Address.class sample2/AddressSubclass.class
 * 3) Install it onto the server
 *    a) for ASE (SQL Server), use:
 *       $SYBASE/bin/installjava -f <dir>/Address.jar -new -U sa -P passwd
 *    b) for ASA (Anywhere):
 *       install java new jar from file '<dir>/Address.jar'
 *
 * When running HandleObject sample, add the 'Address.jar' to the classpath.
 *
 */
public class Address implements java.io.Serializable 
{
    public String _street;
    public String _zip;

    // A default constructor
    public Address ( ) 
    {
        _street = "Unknown";
        _zip = "None";
    }

    // A constructor with parameters
    public Address (String s, String z) 
    {
        _street = s;
        _zip = z;
    }

    // A method to return a display representation of the full address
    public String display( ) 
    {
        return "Street= " + _street + " ZIP= " + _zip;
    }
}
