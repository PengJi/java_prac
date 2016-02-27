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
import java.awt.*;
import java.net.*;
import java.io.*;
import java.sql.*;
import java.util.*; //Properties
import java.awt.event.*;

/** 
 * SybSampleApplet Class creates an applet
 * which consists of a run button.  When selected 
 * it instantiates the SybSample class, 
 * which is a sample driver application. 
 *
 */

public class SybSampleApplet extends java.applet.Applet implements ActionListener
{
    String _iHost = "";
    int    _iPort = 8000;
    String _sampleName;
    String _docBase;
    Button _run;

    public SybSampleApplet()
    {
        super();   
    }
    public void init()
    {
        super.init();
        createAppletForm();
    }
    /**
     * The interface for this applet is one
     * run button. It also retrieves the
     * host and port number in order to access 
     * the sql server.
    */   
    public void createAppletForm()
    {
        _iHost = getCodeBase().getHost();
        _iPort = getCodeBase().getPort();

        // When you are accessing the samples from an applet, 
        // in order to access and open the files without security
        // restrictions, you need to open up as a URL.  
        // Therefore the _docBase should be in http://localhost:8000/sample2/

        _docBase = getDocumentBase().toString();  
        _docBase = _docBase.substring(0, _docBase.lastIndexOf('/')+1);
        _run = new Button("Run");
        _run.addActionListener(this);
        setBackground(new Color(0, 130, 230));
        add(_run);
        setVisible(true);
    }
    /** 
     * Controls the event for the run button
     * which is to instantiate the SybSample Driver
     * @param evt    the java ActionEvent
     */
    public void actionPerformed (ActionEvent evt)
    {
        String arg = evt.getActionCommand(); // get the button that we used
        if ("Run".equals(arg))
        {
            //getting the sample name from the html file parameter
            _sampleName = getParameter("sample");
            try
            {

                //setting it as an arg, so we can pass it like a 
                //commandline option to SybSample.
                String args[] = 
                {
                    _sampleName 
                }
                ;

                // instantiate the SybSample class,
                SybSample ss = new SybSample(args, _iHost+":"+_iPort,_docBase);               
            }
            catch (Exception ex)
            {
                System.out.println("Exception in Applet");
                showStatus(ex.toString());
                ex.printStackTrace();          
            }
            return ;
        }
        return ;
    }
}




