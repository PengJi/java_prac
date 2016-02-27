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
import java.util.Properties;

/**
 * The HandleObject class demonstrates how to insert, update, and delete 
 * data stored as a Java Object in a SAP ASE and SQLAnywhere database.
 *
 * Command line arguments for this sample:<br>
 * -U username<br>
 * -P password<br>
 * -S server<br>                           
 *
 * This sample can only work for SAP ASE and SQLAnywhere which have the ablility to
 * store Java Objects.  Server which have been tested against this sample
 * are; Adaptive Server Anywhere (ASA) Version 6.001 and higher, and
 * Adaptive Server Enterprise (ASE) Version 12.0 and higher.
 *
 * This sample requires the 'sample2.Address' and the 'sample2.AddressSubclass'
 * classes to be installed on the targeted server.
 * Use the following instructions to install those classes.
 * 
 * 1) Build the samples using the Makefile (for Unix) or make_nt.bat (NT).
 * 2) Place the sample2/Address.class and sample2/AddressSubclass.class files
 *    into a jar file:
 *    a) go to the $JDBC_HOME/classes directory
 *    b) run the jar command: 
 *      jar cvf0 Address.jar sample2/Address.class sample2/AddressSubclass.class
 * 3) Install it onto the server
 *    a) for ASE (SQL Server), use: 
 *       $SYBASE/bin/installjava -f <dir>/Address.jar -new -U sa -P passwd
 *    b) for ASA (Anywhere):  
 *       install java new from file '<dir>/Address.jar' 
 * 
 * When running this sample, add the 'Address.jar' to the classpath.
 *
 */

public class HandleObject
{
    // Constants
    final static String TABLE_NAME = "emps";
    final static String PROC_NAME = "empsproc";
    final static String DRIVER_PREFIX = "jdbc:sybase:Tds:";
    final static String USAGE = "Usage: java HandleObject " + 
        "-S <servername:portnumber>|<url>" +
        " -U <username> -P <password>";

    // members
    static String _server = "";
    static Connection _con;
    static Properties _props; // for "user" and "password"
    static String _url = "";

    public static void main(String args[])
    {
        _props = new Properties();

        if (!processCommandline(args))
        {
            System.out.println(USAGE);
            System.exit(0);
        }

        try
        {
            // Load the driver
            Class.forName("com.sybase.jdbc4.jdbc.SybDriver");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        // Get a connection
        try
        {
            _con = DriverManager.getConnection(_url, _props);
            Statement stmt = _con.createStatement();

            // Get Database Metadata
            DatabaseMetaData md = _con.getMetaData();

            // Check if the table we are going to create already exists
            ResultSet rs = md.getTables(null, null, TABLE_NAME, null);

            // If there is a result we need to delete the table first
            int res = 0;

            if (rs.next())
            {
                res = stmt.executeUpdate("drop table " + TABLE_NAME);
                System.out.println("Table " + TABLE_NAME + " deleted");
            }
            // create the table
            res = stmt.executeUpdate("create table "+ TABLE_NAME +" ( name "+
                "varchar(30), home_addr sample2.Address)");
            System.out.println("Table " + TABLE_NAME + " created");
            rs.close ();

            // Check if the stored procedure we are going to create
            // already exists
            rs = md.getProcedures(null, null, PROC_NAME);

            // If there is a result we need to delete the procedure first
            res = 0;
            if (rs.next())
            {
                res = stmt.executeUpdate("drop proc " + PROC_NAME);
                System.out.println("Procedure " + PROC_NAME + " deleted");
            }
            // create the proc
            String proc = "create proc "+ PROC_NAME +
                " @name varchar(30) , @A sample2.Address output as " +
                "select @A = "+
                " home_addr from "+ TABLE_NAME +" where name = @name " +
                " select @A";
            res = stmt.executeUpdate(proc);
            System.out.println("Procedure " + PROC_NAME + " created");
            rs.close ();

            // Enter data
            // It can be done either by sending T-SQL commands
            enterObjectsBySQL();
            // or by creating the Object first and then just send that one
            enterRealObjects();
            // Finally we want to check the Data
            System.out.println("Select all");
            doSelect("select * from "+ TABLE_NAME);
            // Update the Object values
            updateAddressSQL("99999", "666 Heaven's Gate");
            // select again
            System.out.println("Select all");
            doSelect("select * from "+ TABLE_NAME);
            // As a demonstrative exercise, call the stored proc to show that
            // it works
            doSubclassProc();
        }
        catch (SQLException sqe)
        {
            sqe.printStackTrace();
        }
    }
    /** Updates the Address object 
     * @param set new value
     * @param where old value
     */
    public static void updateAddressSQL(String set, String where)
    {
        // NOTE: This is ASE syntax. It may be different on other backends
        // define a String for use with PreparedStatement
        String updateString = "update "+ TABLE_NAME +
            " set home_addr>>_zip = ? where home_addr>>_street = ?";
        try
        {
            // define Prepared Statement
            PreparedStatement pstmt = _con.prepareStatement(updateString);
            // Set the Name
            pstmt.setString(1, set);
            // Set the Address Object
            pstmt.setObject(2, where);
            // execute the Statement
            int res = pstmt.executeUpdate();
            System.out.println("update Object affected "+ res +" rows");
        }
        catch (SQLException sqe)
        {
            sqe.printStackTrace();
        }
    }

    /** Inserts the Address object by using setObject on a 
     * prepared statement
     */
    public static void enterRealObjects()
    {
        String name = "Joe Black";
        sample2.Address add = new sample2.Address ("666 Heaven's Gate","11111");
        // define a String for use with PreparedStatement
        String insertString = "insert into "+ TABLE_NAME +" values (?,?)";
        try
        {
            // define Prepared Statement
            PreparedStatement pstmt = _con.prepareStatement(insertString);
            // Set the Name
            pstmt.setString(1, name);
            // Set the Address Object
            pstmt.setObject(2, add);
            // execeute the Statement
            int res = pstmt.executeUpdate();
            System.out.println("Insert a Real Object affected "+ res +" rows");
        }
        catch (SQLException sqe)
        {
            sqe.printStackTrace();
        }
    }

    /** 
     *  Inserts the Address object by using plain Transact SQL commands
     */
    public static void enterObjectsBySQL()
    {
        // NOTE: This is ASE syntax. It may be different on other backends
        // declare the variable
        String sqlVar = "declare @A sample2.Address select @A = " + 
            " new sample2.Address('432 Post Lane', '99444')";
        // Insert it using a variable
        String insert1 = " insert into "+ TABLE_NAME +
            " values ('Don Green',@A)";
        // or using a constructor
        String insert2 = "insert into "+ TABLE_NAME + 
            " values ('Bob Blue', " +
            " new sample2.Address('234 Stone Road', '99777'))";
        // or insert a subclass of Address into the column, using a 
        // constructor
        String insert3 = "insert into " + TABLE_NAME +
            " values ('Floyd Pink', " +
            " new sample2.AddressSubclass('987 Main Street','12321','Floyd'))";
        try
        {
            Statement stmt = _con.createStatement();
            // send the batch
            int res = stmt.executeUpdate(sqlVar + insert1);
            System.out.println("Insert with variable affected "+ res +" rows");
            // send the insert command
            res = stmt.executeUpdate(insert2);
            System.out.println(
                "Insert with constructor affected "+ res +" rows");
            res = stmt.executeUpdate(insert3);
            System.out.println(
                "Insert of subclass with constructor affected "+ res +" rows");
        }
        catch (SQLException sqe)
        {
            sqe.printStackTrace();
        }
    }

    /** 
     *  Selects the rows: name, Address object
     *  @param selectStmt Select Statement
     */
    public static void doSelect(String selectStmt)
    {
        try
        {
            Statement stmt = _con.createStatement();
            ResultSet rs = stmt.executeQuery(selectStmt);
            // Loop over the result set
            while (rs.next())
            {
                // Get the name
                String name = rs.getString(1);
                // Get the address
                sample2.Address obj = (sample2.Address)rs.getObject(2);
                System.out.println("Name: "+ name +", "+ obj.display());
            }
        }
        catch (SQLException sqe)
        {
            sqe.printStackTrace();
        }
    }

    /** 
     *  Illustrates how to use inheritance properly with in/out parameters
     *  in stored procedures.
     */
    public static void doSubclassProc()
    {
        try
        {
            CallableStatement cs =
                _con.prepareCall("{call "+ PROC_NAME +" (?,?)}");
            cs.setString(1,"Floyd Pink");
            cs.setObject (2, new sample2.AddressSubclass ("Main St.", "12345",
                "Joe White"));
            // Note that even though the input/output parameter in the stored
            // procedure is declared as a sample2.Address, we can still pass
            // in a parameter of type AddressSubclass. That's because, as its
            // name states, an AddressSubclass *is* an Address object, by
            // virtue of Java inheritance rules. However, we still need to
            // register the output parameter as an Address object; otherwise
            // the server would have class casting problems, trying to send
            // back an Address object cast as an AddressSubclass object --
            // a definite no-no in Java. 
            cs.registerOutParameter
                (2, java.sql.Types.JAVA_OBJECT, "sample2.Address");
            ResultSet rs = cs.executeQuery();
            int res = 0;
            while (rs.next())
            {
                res++;
            }
            System.out.println("Exec of proc affected "+ res +" rows");
            Object o = cs.getObject (2);
            // We should get back an instance of sample2.AddressSubclass as
            // our output parameter because that was the actual type of the
            // object we passed in as a parameter
            System.out.println ("The output class type was " +
                o.getClass().getName());
            // We can assign this object to an Address reference, and
            // then downcast it to an AddressSubclass to retrieve the
            // _name field, which only the AddressSubclass has
            sample2.Address sa = (sample2.Address) o;
            String name = ((sample2.AddressSubclass) sa)._name;
            System.out.println ("The name for the AddressSubclass we " +
                "retrieved was " + name);
            rs.close();
        }
        catch (SQLException sqe)
        {
            sqe.printStackTrace();
        }
    }

    /**
     *  Handles command line options for all samples.<br>
     *  Possible arguments include:
     *  <ul> 
     *     <li>  -U &ltusername&gt
     *     <li>  -P &ltpassword&gt 
     *     <li>  -S &ltservername&gt
     *  </ul>
     */
    public static boolean processCommandline(String args[])
    {
        boolean retVal = true;

        if (args.length != 6)
        {
            System.out.println("Wrong number of parameters");
            return false;
        }

        for (int i = 0; retVal == true && i < args.length; i++)       
        {
            String arg = args[i];
            if (arg.regionMatches(0, "-", 0, 1))
            {
                try
                {
                    switch (arg.charAt(1))
                    {
                        case 'U':
                            i++;
                            _props.put("user", args[i]);
                            break;                            
                        case 'P':
                            i++;
                            _props.put("password", args[i]);
                            break;                            
                        case 'S':
                            i++;
                            if (args[i].startsWith(DRIVER_PREFIX))
                            {
                                _url = args[i];
                            }
                            else
                            {
                                _url = DRIVER_PREFIX + args[i];
                            }
                            break;  
                        default:
                            System.out.println("Invalid Parameter: '" + 
                                arg.charAt(1) + "'");
                            retVal = false;
                            break;
                    }
                }
                catch (Exception ex)
                {
                    retVal = false;
                }
            }
        }

        return (retVal);
    }
}
