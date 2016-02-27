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
 * MyConnection class demonstrates how to use the Connection class methods<br>
 *
 * <P>MyConnection may be invoked with the optional parameters:<br>
 * -U username<br>
 * -P password<br>
 * -D debuglibraries<br>
 * -S server<p>
 *
 *  @see Sample
 */
public class MyConnection extends Sample
{

    MyConnection()
    {
        super();
    }

    public void sampleCode()
    {


        try
        {
            // use tempdb as pubs2 on the jConnect server is read only
            execDDL("use tempdb");

            // Display what the JDBC funtion database() is on this RDBMS

            output("Native SQL for: select {fn database()} =  ");
            output("\t\t" + _con.nativeSQL("select {fn database()}") 
                + "\n");

            output("getAutoCommit= " +  _con.getAutoCommit() + "\n");

            // Change AutoCommit State
            _con.setAutoCommit (false);
            output("getAutoCommit() after setAutoCommit(false)= " +
                _con.getAutoCommit() + "\n");

            output("getTransactionIsolation()= " +
                _con.getTransactionIsolation() + "\n");

            // Connection Transaction levels
            // TRANSACTION_READ_UNCOMMITTED=1   (iso 0)
            // TRANSACTION_READ_COMMITTED=2     (iso 1)
            // TRANSACTION_REPEATABLE_READ=4    (iso 2)
            // TRANSACTION_SERIALIZABLE=8       (iso 3)

            output("TRANSACTION_READ_UNCOMMITTED= " +
                _con.TRANSACTION_READ_UNCOMMITTED +"  (iso 0)\n");
            output("TRANSACTION_READ_COMMITTED= " +
                _con.TRANSACTION_READ_COMMITTED + "   (iso 1)\n");
            output("TRANSACTION_REPEATABLE_READ= " +
                _con.TRANSACTION_REPEATABLE_READ + "   (iso 2)\n");
            output("TRANSACTION_SERIALIZABLE= " +
                _con.TRANSACTION_SERIALIZABLE   + "   (iso 3)\n");

            _con.setTransactionIsolation(
                Connection.TRANSACTION_READ_UNCOMMITTED);
            output("getTransactionIsolation() after calling " +
                "setTransactionIsolation( " +
                "Connection.TRANSACTION_READ_UNCOMMITTED)="
                + _con.getTransactionIsolation() + "\n");

            output("isReadOnly=  " +  _con.isReadOnly() + "\n");

            // Note: setReadOnly() can not be implemented on Adaptive Server
            // and Adaptive Server Anywhere

            _con.setReadOnly(true);
            output("isReadOnly() after calling setReadOnly(true)=  " +
                _con.isReadOnly() + "\n");

            output("getCatalog()=  " +  _con.getCatalog() +"\n");
            _con.setCatalog("master");
            output("getCatalog() after calling setCatalog()=  " +
                _con.getCatalog() + "\n");

            output("isClosed()=  " +  _con.isClosed() + "\n");


        }
        catch (SQLException ex)
        {
            displaySQLEx(ex);
        }
    }
}
