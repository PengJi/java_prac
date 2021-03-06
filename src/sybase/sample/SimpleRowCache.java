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
import java.util.Vector;
import java.util.Enumeration;

/**
 * An implementation of the DataRowCache interface that stores
 * a set of DataRow objects in a vector.
 * @author  Eric Giguere
 * @version 1.01, 03 Jul 1997
 */
public final class SimpleRowCache implements  sybase.sample.DataRowCache
{
    /** Container used to store the rows */
    private Vector _rowList = null;

    /** Constructs an initially empty row cache. */
    public SimpleRowCache()
    {
        _rowList = new Vector( 100, 20 );
    }

    /**
     * Clears all DataRow objects out of the row cache.
	  * Each data row is explicitly cleared before being removed.
     */
    public synchronized void clear( )
    {
        int i = _rowList.size(); 

        while( i-- > 0 )
        {
            DataRow dr = (DataRow) _rowList.elementAt( i );
            if( dr != null )
            {
                dr.clear();
            }
        }
        _rowList.removeAllElements();
    }

    /**
     * Determines if the given DataRow has been cached.
     * @return true if it is currently in this cache, else false.
     */
    public boolean isCached( int index )
    {
        return index <= _rowList.size(); 
    }

    /** @return number of rows that have been cached. */
    public final int getDataRowCount()
    {
        return _rowList.size();
    }

    /**
     * Retrieves the DataRow element at the specified index.
     *
     * @param index The index of the DataRow element to be retrieve.
     *  Note that this is not necessarily the same as the 
     *  row number, it is simply an index into the cache.
     *
     * @return The desired DataRow object or null if there is no
     *  DataRow object at the specified index.
     */
    public final DataRow getDataRow( int index )
    {
        try 
        {
            return (DataRow) _rowList.elementAt( index-1 ); 
        }
        catch( Exception e )
        {
            return null;
        }
    }

    /**
     * Retrieves the index of the specified DataRow object.
     *
     * @param row The DataRow object to find.
     * @return The index of the specified DataRow in the cache, 
     *  or -1 if the DataRow object is not in the cache.
     */
    public final int getDataRowIndex( DataRow row )
    {
        try 
        {
            return _rowList.indexOf( row );
        }
        catch( Exception e )
        {
            return -1;
        }
    }

    /**
     * Retrieves the list of DataRow elements in the cache.
     *
     * @return An enumeration of the DataRow objects contained in 
     *  the cache.
     */
    public final Enumeration getDataRows()
    {
        return _rowList.elements();
    }

    /**
     * Adds a DataRow object to the cache.
     *
     * @param row The DataRow object to add.
     * @return <code>true</code> if the DataRow was added;
     *   <code>false</code> otherwise.
     */
    public final synchronized boolean addDataRow( DataRow row )
    {
        if( row == null ) return false;

        _rowList.addElement( row );
        return true;
    }

    /**
     * Inserts a DataRow object into the cache.
     *
     * @param index The index at which to insert the object.
     * @param row The object to insert.
     * @return If the row was inserted, returns <code>true</code>,
     *         otherwise returns <code>false</code>.
     */
    public boolean insertDataRow( int index, DataRow row )
    {
        try 
        {
            _rowList.insertElementAt( row, index );
            return true;
        }
        catch( Exception e )
        {
            return false;
        }
    }

    /**
     * Removes a DataRow object from the cache.
     *
     * @param row The DataRow object to remove.
     * @return <code>true</code> if the DataRow was removed;
     *   <code>false</code> otherwise.
     */
    public final boolean removeDataRow( DataRow row )
    {
        try 
        {
            return _rowList.removeElement( row );
        }
        catch( Exception e )
        {
            return false;
        }
    }

    /**
     * Removes a DataRow object from the cache.
     *
     * @param index The index of the DataRow object to remove.
     *
     * @return <code>true</code> if the DataRow was removed;
     *   <code>false</code> otherwise.
     */
    public final boolean removeDataRow( int index )
    {
        try 
        {
            _rowList.removeElementAt( index );
            return true;
        }
        catch( Exception e )
        {
            return false;
        }
    }

    /**
     * Replaces a DataRow object with another DataRow object.
     *
     * @param oldRow The object to be replaced.
     * @param newRow The new object that will replace <code>oldRow</code>.
     *
     * @return <code>true</code> if the DataRow object was successfully 
     *  replaced; <code>false</code> otherwise.
     */
    public final synchronized boolean replaceDataRow( DataRow oldRow, 
        DataRow newRow )
    {
        try 
        {
            int pos = _rowList.indexOf( oldRow );
            if( pos < 0 ) return false;

            _rowList.setElementAt( newRow, pos );
            return true;
        }
        catch( Exception e )
        {
            return false;
        }
    }

    /**
     * Replaces a DataRow object with another DataRow object.
     *
     * @param index The index of the object to be replaced.
     * @param newRow The new object that will replace <code>oldRow</code>.
     *
     * @return <code>true</code> if the DataRow object was successfully 
     *  replaced; <code>false</code> otherwise.
     */
    public final synchronized boolean replaceDataRow( int index, 
        DataRow newRow )
    {
        try 
        {
            _rowList.setElementAt( newRow, index );
            return true;
        }
        catch( Exception e )
        {
            return false;
        }
    }

}
