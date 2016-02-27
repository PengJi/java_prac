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

import java.util.Enumeration;
import java.util.Properties;
import java.io.IOException;
import java.io.FileInputStream;
import java.sql.*;
import com.sybase.jdbcx.*;
import com.sybase.jdbc4.jdbc.DynamicObjectInputStream;

/**
 * Both ASA 6+ and ASE 12+ offer the JCS (Java Classes in SQL)
 * feature. This allows Java objects to be used as user-defined types in
 * a table, and accessed from within SQL statements. Prior to the
 * existence of jConnect's Dynamic Class Loader, only those classes
 * which appeared in the client (jConnect) class path were accessible.
 * Attempting to access an instance of a class that did not exist in the
 * class path would result in an exception.
 * 
 * <P>This sample allows you to load an object from a file or from a database 
 * without having the object's class in your classpath. The class is loaded 
 * from another database. 
 *
 * <P>Command line arguments:
 *  <UL> 
 *     <LI>  &lt;Sample Name&gt;
 *     <LI>  -U &lt;user name&gt;
 *     <LI>  -u &lt;class loader user name&gt;
 *     <LI>  -P &lt;password&gt;
 *     <LI>  -p &lt;class loader password&gt;
 *     <LI>  -S &lt;server URL&gt;
 *     <LI>  -s &lt;class loader server URL&gt;
 *     <LI>  -G &lt;gateway&gt;
 *     <LI>  -D &lt;debug-class-list&gt;
 *     <LI>  -H &lt;hostname&gt;
 *     <LI>  -J &lt;character set&gt;
 *     <LI>  -z &lt;language&gt;
 *     <LI>  -f &lt;name of serialized file&gt;
 *     <LI>  -q &lt;query&gt;
 *  </UL>
 *
 * <P>You must specify either a serialized file (-f) or a query to execute (-q)
 * to return a Java object. If you specify both, you'll get an error message.
 * 
 * <P>When using the -q option, this sample assumes that a Java object is 
 * returned in column 1 of a one-row result set. The class must not exist
 * in your class path, but must have been installed in a database in the 
 * "loader" server. If you don't provide a URL for that server (-s), this 
 * sample uses the same server as the the other samples - the one specified
 * by the -S option. Note also that the -p and -u options correspond to their 
 * upper-case equivalents, but refer to the server from which the class is to
 * be loaded. In order to install a Java class on the server, use the <CODE>
 * installjava</CODE> utility.
 *
 * <P>When using the -f option, the file you specify must contain a serialized
 * instance of the class you installed.
 */

public class ClassLoaderSample extends Sample 
{

    static String loaderUserFlag = "-u";
    static String loaderPasswordFlag = "-p";
    static String loaderURLFlag = "-s";
    static String fileNameFlag = "-f";
    static String queryFlag = "-q";

    private String _fileName = null;
    private String _query = null;

    private boolean _failed = false;
    private DynamicClassLoader _loader;

    /**
     * Default constructor.
     */
    public ClassLoaderSample() 
    {
    }


    /**
     * Add command line options to the connection properties.
     * The superclass will call this method before creating the
     * connection. This method assumes that two parallel vectors have
     * been set up, containing command line flags and their arguments
     * respectively. The parsing done in the superclass is nonexistent,
     * so <I>caveat utor!</I> The flag and its argument must be separated
     * by at least one space, unlike the "normal" command line arguments.
     *
     * @param commandLine command line settings
     */
    public void addMoreProps(CommandLine commandLine)  
    {
        // Use the regular server if no class loader server was specified.
        String loaderUser = (String)commandLine._props.get("user");
        String loaderPassword = (String)commandLine._props.get("password");
        String loaderURL = (String)commandLine._props.get("server");

        Enumeration extras  = commandLine._extraArgs.elements();
        Enumeration options = commandLine._extraOptions.elements();
        while (extras.hasMoreElements()) 
        {
            String flag = (String) extras.nextElement();
            String value = (String) options.nextElement();
            error("Extra flag = " + flag + " " + value + "\n");

            if (flag.equals(loaderUserFlag)) 
            {
                loaderUser = value;
            }
            else if (flag.equals(loaderPasswordFlag)) 
            {
                loaderPassword = value;
            }
            else if (flag.equals(loaderURLFlag)) 
            {
                loaderURL = value;
            }
            else if (flag.equals(fileNameFlag)) 
            {
                _fileName = value;
            }
            else if (flag.equals(queryFlag)) 
            {
                _query = value;
            }
        }

        // Validate the flags a little.
        if (_query == null) 
        {
            if (_fileName == null) 
            {
                error("*** You must specify either a query or a serialized file.\n");
                _failed = true;
            }
        }
        else 
        {
            if (_fileName != null) 
            {
                error("*** Both a query and a serialized file were specified. " +
                    "Pick one and try again.\n");
                _failed = true;
            }
        }

        if (!_failed) 
        {
            // Connection properties for connecting to class loader server.
            Properties props = (Properties)_cmdline._props.clone();
            props.put("user", loaderUser);
            props.put("password", loaderPassword);
            props.put("JCONNECT_VERSION", "5");

            // Ask the SybDriver for a new class loader.
            _loader = _sybDriver.getClassLoader(loaderURL, props);

            // The new class loader needs to be made available to the statement
            // that executes the query above. That's what the CLASS_LOADER
            // connection property is for. Once the class loader is created, it's
            // passed to subsequent connections as shown below. 

            // Stash the class loader into the original connection properties 
            // so that other connection(s) can know about it.
            _cmdline._props.put("CLASS_LOADER", _loader);
        }
    }

    /**
     * There are two modes of operation. 
     *
     * <P>If a serialized file was specified, t's deserialized, loading 
     * its class from the server. 
     * 
     * <P>If a query was specified, it's executed on the assumption that 
     * it will return a Java object in the first column of the first row 
     * of the result set. The class of that object is loaded from the server.
     */
    public void sampleCode() 
    {

        // Bail on fail.
        if (_failed) 
        {
            return;		// ----------------------------------------->
        }


        try 
        {
            Object obj;
            if (_fileName != null) 
            {
                // Deserialize the specified file.
                // With the class installed on the server, the class is 
                // available via the class loader.

                FileInputStream fileStream = new FileInputStream(_fileName);
                DynamicObjectInputStream stream =
                    new DynamicObjectInputStream(fileStream, _loader);
                obj = stream.readObject();
                stream.close();
                output("--->> Deserialized object is \n\"" + obj + "\"\n");
            }
            else 
            {
                // A connection has already been opened for us, so just go.
                Statement stmnt = _con.createStatement();

                // Retrieve some rows from the table that has a Java class
                // as its first field.
                ResultSet rs = stmnt.executeQuery(_query);
                if (rs.next()) 
                {
                    // Even though the class is not in our class path,
                    // we should be able to access its instance.
                    // Note that we're assuming the object is in column 1.
                    obj = rs.getObject(1);

                    // Write the object to the output window just to 
                    // prove we can do it.
                    output("--->> Object in RS is \n\"" + obj + "\"\n");

                    // The class has been loaded from the server,
                    // so let's take a look.
                    Class c = obj.getClass();      

                    // Some introspection stuff can be done here
                    // to access the fields of obj.
                    // ...
                }
            }
        }
        catch (Exception ex) 
        {
            ex.printStackTrace();
        }
    }

}
