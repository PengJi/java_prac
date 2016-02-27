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
import sample2.Person;
import java.lang.reflect.*;
import com.sybase.jdbcx.*;
import com.sybase.jdbc4.jdbc.DynamicObjectInputStream;


/**
 * PreLoadSample class  may be used to demonstrate the use of the Dynamic
 * Class Loader  and the pre-loading of a jar feature of jConnect.<br>
 * The use of the PRELOAD_JARS property allows the user to provide
 * a comma separated list of jars which reside in the backend and
 * will then be downloaded to the client app once the connection is
 * made.<p>
 *  This demo requires:<br>
 * - an ASE 12.0 or ASA 6 or higher database with java enabled<br>
 * - you to have install the classes sample2.Person and sample2.Employee
 *   into your database and retain the jar file.<br>
 *   + for ASE, create a jar file called Sample2:<br>
 *   cd $JDBC_HOME/classes<br>
 *   jar cvf0 Sample2.jar sample2/Person.class sample2/Employee.class<br>
 *   + install the jar file into ASE:
 *   installjava -f 'Sample2.jar' -new -j 'Sample2' -Ddbname -Usa -P<br>
 * -  You then create a table called staff in database dbname:<br>
 * create table staff(f1 int, anEmployee sample2.Employee)<br><br>
 * -  insert at least 1 row into the database:<br>
 *insert staff values(1, new sample2.Employee('Lance', 'Andersen', 'J'))<br>
 *
 * <P>PreLoadSample may be invoked with the parameters:<br>
 * -U username<br>
 * -P password<br>
 * -D debuglibraries<br>
 * -S server<p>
 * -u ClassLoaderURL (this is required)<p>
 *
 * Example:<p>
 * java sample2.SybSample PreLoadSample -U sa -P -u jdbc:sybase:Tds:dbname:1111/dbname -S jdbc:sybase:Tds:dbname:1111/dbname<br>
 *
 *  @see Sample
 */
public class PreLoadSample extends Sample
{

    private String _extraCmdOption = "-u";
    private DynamicClassLoader _loader;
    private boolean _gotClassLoader = false;

    PreLoadSample()
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
            String loaderURL = null;
            error("Extra options= " + option + " " + value + "\n");

            if(option.equals(_extraCmdOption) && loaderURL == null)
            {
                loaderURL=value;
                Properties props = (Properties)_cmdline._props.clone();
                _loader = _sybDriver.getClassLoader(loaderURL, props);
                if(_loader == null)
                {
                    error("Could not get a CLASS_LOADER");
                    break;
                }
                _cmdline._props.put("CLASS_LOADER", _loader);
                _cmdline._props.put("PRELOAD_JARS", "Sample2");
                _gotClassLoader = true;
                break;
            }
        }
    }


    /**
     * Demonstrate the functionality if a CLASS_LOADER URL is supplied
     */
    public void sampleCode()
    {

        if(_gotClassLoader)
        {
            executeSample();
        }
        else
        {
            error("You must specify a URL for the CLASS_LOADER property");
        }
    }

    /**
     *  private method which actually will do all of our work for
     *  the sample
     */
    private void executeSample()
    {

        String query = "select anEmployee from staff2";
        try
        {
            Statement stmt = _con.createStatement();;

            // Execute the query which will return an Employee object
            // We will cast this using the Person interface. Note the
            // Person interface class MUST be in your CLASSPATH. You
            // Do not need Employee in your CLASSPATH.
            ResultSet rs = stmt.executeQuery(query);

            output("***Using interface class\n");
            while(rs.next())
            {
                Person aPerson = (Person)rs.getObject(1);
                displayMethods(aPerson.getClass());
                output("The person is: " + aPerson.toString() +
                    "\nFirst Name= " + aPerson.getFirstName()+
                    "\nLast Name= " + aPerson.getLastName() + "\n");

            }
            // Now execute the same query, but this time we will use
            // reflection to access the class.  Again, only the interface
            // Person is required in the CLASSPATH
            rs = stmt.executeQuery(query);

            output("***Using reflection\n");
            Object theObj = null;
            while(rs.next())
            {
                theObj = rs.getObject(1);
                output("The person is: " + theObj.toString() + "\n");
                Class theClass = theObj.getClass();
                displayMethods(theClass);
                Method m1= theClass.getMethod("toString", new Class[0]);
                Method m2= theClass.getMethod("getFirstName", new Class[0]);
                Method m3 = theClass.getMethod("getLastName", new Class[0]);
                output("The person is: " + (Object)
                    m1.invoke(theObj, new Object[0]) +
                    "\nFirst Name= " + (Object) m2.invoke(theObj, new Object[0]) +
                    "\nLast Name= " + (Object) m3.invoke(theObj, new Object[0])
                    + "\n");

            }
            rs.close();
            stmt.close();
        }
        catch (SQLException sqe)
        {
            displaySQLEx(sqe);
        }
        catch (Exception e)
        {
            error("Unexpected exception : " + e.toString() + "\n");
            e.printStackTrace();
        }

    }

    /**
     * Display the methods in the Class that we obtained using the
     * CLASS_LOADER
     */
    private void displayMethods(Class theClass)
    {
        Method[] m = theClass.getDeclaredMethods();
        for (int i=0; i<m.length; i++)
        {
            output("" + theClass + " method #" + (i+1) +
                " : " + m[i].getName() + "\n");
        }
    }

}
