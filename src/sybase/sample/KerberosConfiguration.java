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

import java.util.Hashtable;

import javax.security.auth.login.Configuration;
import javax.security.auth.login.AppConfigurationEntry;

/**
 * This class illustrates how to programatically set the values that would
 * otherwise be set in a JAAS login configuration file
 */

public class KerberosConfiguration extends Configuration
{

    static AppConfigurationEntry []ace;

    static
    {
        Hashtable map = new Hashtable();
       
        // By setting useTicketCache to true, we are telling Java to try and
        // locate a Keberos location in a well-defined location (for example,
        // in the /tmp/krb5cc_{userId} file on a Solaris machine or in the
        // in-memory cache on a Windows 2000 client machine which has 
        // authenticated to Active Directory Server).
        map.put("useTicketCache", "true");
        AppConfigurationEntry ac = new AppConfigurationEntry(
            "com.sun.security.auth.module.Krb5LoginModule",
            AppConfigurationEntry.LoginModuleControlFlag.REQUIRED,
            map);
        ace = new AppConfigurationEntry[1];
        ace[0] = ac;
    }

    public KerberosConfiguration()
    {
        super();
    }

    public AppConfigurationEntry[] getAppConfigurationEntry (String
        applicationName)
    {

        return ace;
    }

    public void refresh()
    {
    }

    static public void setConfiguration() 
    {
        Configuration.setConfiguration(new KerberosConfiguration());
    }
}
