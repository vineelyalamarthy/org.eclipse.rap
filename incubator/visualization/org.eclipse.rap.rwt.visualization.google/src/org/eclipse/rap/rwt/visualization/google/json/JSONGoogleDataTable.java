/*******************************************************************************
 * Copyright (c) 2009-2010 David Donahue and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     David Donahue - initial API, implementation and documentation
 *     Austin Riddle (Texas Center for Applied Technology) - performance enhancements and javadoc cleanup
 ******************************************************************************/
package org.eclipse.rap.rwt.visualization.google.json;
import org.eclipse.rap.rwt.visualization.google.internal.json.JSONArray;
import org.eclipse.rap.rwt.visualization.google.internal.json.JSONException;
import org.eclipse.rap.rwt.visualization.google.internal.json.JSONObject;

/**
 * <p>
 * Provides a JSON data transport mechanism for visualization data.
 * </p>
 * <p>This implementation is not synchronized.</p>
 * <p>
 * @see <a href="http://code.google.com/apis/visualization/documentation/reference.html#DataTable">Corresponding Google Counterpart</a>
 * </p>
 */
public class JSONGoogleDataTable {
  
  public static final String TYPE_BOOLEAN = "boolean";
  public static final String TYPE_NUMBER = "number";
  public static final String TYPE_STRING = "string";
  public static final String TYPE_DATE = "date";
  public static final String TYPE_DATETIME = "datetime";
  public static final String TYPE_TIMEOFDAY = "timeofday";
  
  private static final String JSON_NAME_COLUMNS = "cols";
  private static final String JSON_NAME_COLUMNS_ID = "id";
  private static final String JSON_NAME_COLUMNS_LABEL = "label";
  private static final String JSON_NAME_COLUMNS_TYPE = "type";
  private static final String JSON_NAME_COLUMNS_PATTERN = "pattern";
  
  private static final String JSON_NAME_ROWS = "rows";
  private static final String JSON_NAME_ROWS_V = "v";
  private static final String JSON_NAME_ROWS_F = "f";

  private JSONArray columns;
  private JSONArray rows;
  
  /**
   * Creates a JSON object table for portability within the JSON API.
   * @return a JSON object that contains the columns and rows from this table. Will not return <code>null</code>.
   */
  public JSONObject createTable() {
    JSONObject table = new JSONObject();
    try {
       if (columns != null) {
          table.put(JSON_NAME_COLUMNS, columns);
       }
       if (rows != null) {
          table.put(JSON_NAME_ROWS, rows);    
       }
    }
    catch (JSONException e) {
       //This really shouldn't ever happen
       e.printStackTrace();
    }
    return table;
  }
  
  private JSONObject createCell(Object v, String f) throws JSONException {
    JSONObject cell = new JSONObject();
    cell.put(JSON_NAME_ROWS_V, v);
    if(f != null)
      cell.put(JSON_NAME_ROWS_F, f);
    return cell;
  }
  
  /**
   * <p>Adds a column of data to this table.</p>
   *
   * @param id unique column identifier, cannot be <code>null</code>.
   * @param label display label of column, can be <code>null</code>.
   * @param type should be one of: <ul>
   * <li>boolean - corresponds to javascript boolean type</li>
   * <li>number - corresponds to javascript number value</li>
   * <li>string - corresponds to javascript string value
   * <li>date - corresponds to javascript Date object (Zero-based month, with the time truncated)</li>
   * <li>datetime - corresponds to javascript Date object including the time</li>
   * <li>timeofday - corresponds to javascript array of three numbers and an optional fourth, 
   * representing hour (0 indicates midnight), minute, second, and optional millisecond.</li>
   * <p><b>See convenience TYPE_* constants in this class</b></p>
   * </ul> 
   * 
   * @param pattern formatting string, can be <code>null</code>
   * <p>
   * @see <a href="http://code.google.com/apis/visualization/documentation/reference.html#DataTable">Google Data Table Specification</a>
   * </p>
   */
  public void addColumn(String id, String label, String type, String pattern) {
    JSONObject column = new JSONObject();
    try {
      column.put(JSON_NAME_COLUMNS_ID, id);
      if (label != null) column.put(JSON_NAME_COLUMNS_LABEL, label);
      column.put(JSON_NAME_COLUMNS_TYPE, type);
      column.put(JSON_NAME_COLUMNS_PATTERN, pattern);
    } catch (Exception e) {
      //add null column
    }
    if (columns == null) {
       columns = new JSONArray();
    }
    columns.put( column );
  }

  /**
   * Adds a row of data that corresponds to the columns in this table.
   * Columns need not be added before rows.
   * @param row - an array of objects whose indices correspond to a column in this table. 
   * Each value should be of the type that is defined for that column.
   * <p>Namely:
   * Boolean, Double, Integer, JSONArray, JSONObject, Long, String, or the JSONObject.NULL object</p>
   */
  public void addRow(Object[] row) {
    addRow(row, false);
  }
  
  /**
   * Adds a row if data that corresponds to the columns in this table.  
   * @param row
   * @param hasFormattedValues if <b>true</b>, then each column is represented by a pair of values in the row: 
   * <p>The first value is the data format type and should be one of:
   * Boolean.class, Double.class, Integer.class, JSONArray.class, JSONObject.class, Long.class, String.class, or the JSONObject.NULL object.</p>
   * <p>The second value is the actual row value.</p>  
   * 
   * <p>If <code>false</code> each column should be represented by a single value in the row array. This is the same as <code>addRow(Object[] row)</code></p>
   * <p> 
   * @see <a href="http://www.json.org/javadoc/org/json/JSONObject.html#put(java.lang.String,%20java.lang.Object)">JSONObject Specification</a>
   * </p>
   */
  public void addRow(Object[] row, boolean hasFormattedValues) {
    JSONArray rowArray = new JSONArray();
    JSONObject rowObj = new JSONObject();
    for (int i=0; i<row.length; i++) {
      Object v = row[i];
      Object f = null;
      if (hasFormattedValues && i+1 < row.length) {
        f = row[i+1];
        i++;
      }
      JSONObject cell = null;
      try {
        if (f==null) {
          cell = createCell(v, null);
        } else {
          cell = createCell(v, String.valueOf(f));
        }
      } catch (Exception e) {
        //skip the row
      }
      rowArray.put( cell );
    }
    try {
      rowObj.put( "c", rowArray );
    } catch (Exception e) {
      //leave row blank
    }
    if (rows == null) {
       rows = new JSONArray();
    }
    rows.put( rowObj );
  }
  
  /**
   * Returns a JSON string representation of this table. If any errors occur, <code>null</code> is returned.
   */
  public String toString() {
    try {
      return createTable().toString();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

}