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
 * Stores a row of data values.
 *
 */
public class DataRow 
{
    /** The row has been modified. */
    public static final int ROW_MODIFIED       = 0x0001;

    /** The row has been added. */
    public static final int ROW_ADDED          = 0x0002;

    /** The row has been updated into the database. */
    public static final int ROW_UPDATED        = 0x8000;

    /** The column value has been fetched. */
    public static final int COLUMN_FETCHED     = 0x0001;

    /** The column value should not be automatically fetched. */
    public static final int COLUMN_NOAUTOFETCH = 0x0002;

    /** The column value has been modified. */
    public static final int COLUMN_MODIFIED    = 0x0004;

    /** The index could not be mapped properly. */
    private static final int BAD_INDEX = -9;

    /** Number of columns in the row */
    private int 	  				  _numColumns; 

    /** Array of values representing the column entries for a row. */
    private SQLValue[] _columnValues;


    // don't allow no-arg constructor
    private DataRow()
    {
    }

    /**
     * Constructs an empty row with storage for a fixed number of columns.
     *
     * @param column The number of columns to allocate.
     */
    public DataRow( int columns )
    {
        if ( columns > 0 )
        {
            _numColumns = columns;
            _columnValues = new SQLValue[ _numColumns ];
        }
    }

    /**
     * Assign a SQLValue to a given column
     *
     * @param dv The new data value for the column.
     * @param col The column to assign the new data value to.
     */
    public void setValueAt(int col, SQLValue dv)
    {
        int trueIndex = mapToIndex(col);

        if (trueIndex != BAD_INDEX) 
        {
            _columnValues[trueIndex] = dv;
        }
        else
        {
            throw new RuntimeException("Invalid column index: ("+col+")");
        }
    }

    /**
     * Retrieve a SQLValue from the given column
     *
     * @param col A column to access.
	  * @return The datavalue contained in the column
     */
    public SQLValue getValueAt(int col)
    {
        int trueIndex = mapToIndex(col);
        if (trueIndex != BAD_INDEX)
        {
            return _columnValues[trueIndex];
        }
        else
        {
            throw new RuntimeException("Invalid column index: ("+col+")");
        }
    }

    /**
     * Clears out the row values for quicker disposal. To delete a
     * DataRow object from a result set, use the delete() method.
     */
    public synchronized void clear()
    {
        _columnValues = null;
    }

    private int mapToIndex(int col)
    {
        int trueIndex = col-1; // arrays are 0-based, columns are 1-based

        if ((trueIndex < 0) || (trueIndex >= _numColumns)) 
        {
            trueIndex = BAD_INDEX;  // failure code
        }
        return trueIndex;
    }
}
