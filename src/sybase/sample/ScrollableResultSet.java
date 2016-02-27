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

/**
* <P>This interface is an extension of the JDBC 1.0 
* <CODE>java.sql.ResultSet</CODE> interface.  The purpose is to define
* the additional methods for scrollable behavior.  The methods are duplicates
* of the methods added to the JDBC 2.0 API for the <CODE>ResultSet</CODE>
* interface.  However, this interface is a subset of the JDBC 2.0 ResultSet.
* This interface does not provide update or delete methods.
* <P>
* The constants defined on this interface have the same semantics as those
* found in the JDBC 2.0 API for java.sql.ResultSet. No guarantee is made
* that ScrollableResultSet.FETCH_FORWARD == ResultSet.FETCHFORWARD.
*
* @see java.sql.ResultSet
*/
public interface ScrollableResultSet extends java.sql.ResultSet
{

}
