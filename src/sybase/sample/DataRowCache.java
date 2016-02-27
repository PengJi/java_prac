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

/**
 * An interface for manipulating a cache of DataRow objects.
 * Used internally by the Query and ExtendedResultSet objects.
 * @author Eric Giguere 
 * @version 1.0, 06 Jun 1997
 */
public interface DataRowCache
{

    /**
     * Determines if the given DataRow has been cached.
     * @return true if it is currently in this cache, else false.
     */
    boolean isCached(int index);

    /**
     * Gets the number of DataRow elements in the cache.
     * @return The number of elements.
     */
    int getDataRowCount();

    /**
     * Gets a specific DataRow element from the cache.
     *
     * @param index A value from 0 to <tt>getDataRowCount()</tt>-1.
     *              Do not confuse this value with the row number,
     *              it is simply the index into the cache.
     * @return The desired row or <tt>null</tt> if the row does
     *         not exist.
     */
    DataRow getDataRow(int index);

    /**
     * Gets the index of a DataRow element from the cache.
     *
     * @param row The DataRow object to find.
     * @return The index of the element in the cache, or -1 if
     *         the element is not in the cache.
     */
    int getDataRowIndex(DataRow row);

    /** @return An enumeration of DataRow elements in the cache. */
    Enumeration getDataRows();

    /**
     * Adds a DataRow object to the cache.
     *
     * @param row The object to add.
     * @return If the row was added, returns <code>true</code>,
     *         otherwise returns <code>false</code>.
     */
    boolean addDataRow(DataRow row);

    /**
     * Inserts a DataRow object into the cache.
     *
     * @param index The index at which to insert the object.
     * @param row The object to insert.
     * @return If the row was inserted, returns <code>true</code>,
     *         otherwise returns <code>false</code>.
     */
    boolean insertDataRow(int index, DataRow row);

    /**
     * Removes a DataRow object from the cache.
     *
     * @param row The object to remove.
     * @return If the row was removed, returns <code>true</code>,
     *         otherwise returns <code>false</code>.
     */
    boolean removeDataRow(DataRow row);

    /**
     * Removes a DataRow object from the cache.
     *
     * @param index The index of the object to remove.
     * @return If the row was removed, returns <code>true</code>,
     *         otherwise returns <code>false</code>.
     */
    boolean removeDataRow(int index);

    /**
     * Replaces a DataRow object with another.
     *
     * @param oldRow The current object in the cache.
     * @param newRow The new object that will take its place.
     * @return If the row was replaced, returns <code>true</code>,
     *         otherwise returns <code>false</code>.
     */
    boolean replaceDataRow(DataRow oldRow, DataRow newRow);

    /**
     * Replaces a DataRow object with another.
     *
     * @param index The index of the object in the cache.
     * @param newRow The new object that will take its place.
     *
     * @return If the row was replaced, returns <code>true</code>,
     *         otherwise returns <code>false</code>.
     */
    boolean replaceDataRow(int index, DataRow newRow);

    /** Removes all DataRow objects from the cache. */
    void clear();
}
