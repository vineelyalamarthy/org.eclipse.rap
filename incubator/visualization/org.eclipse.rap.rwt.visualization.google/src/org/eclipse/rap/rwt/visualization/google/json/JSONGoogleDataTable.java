/*******************************************************************************
 * Copyright (c) 2009-2010 David Donahue.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     David Donahue - initial API, implementation and documentation
 ******************************************************************************/
package org.eclipse.rap.rwt.visualization.google.json;
import org.eclipse.rap.rwt.visualization.google.internal.json.JSONArray;
import org.eclipse.rap.rwt.visualization.google.internal.json.JSONException;
import org.eclipse.rap.rwt.visualization.google.internal.json.JSONObject;

/**
 * Java object for creating Javascript/JSON objects of class google.visualization.DataTable.
 * 
 * @see http://code.google.com/apis/visualization/documentation/reference.html#DataTable
 */
public class JSONGoogleDataTable {
  
  private static final String JSON_NAME_TABLE = "table";
  
  private static final String JSON_NAME_COLUMNS = "cols";
  private static final String JSON_NAME_COLUMNS_ID = "id";
  private static final String JSON_NAME_COLUMNS_LABEL = "label";
  private static final String JSON_NAME_COLUMNS_TYPE = "type";
  private static final String JSON_NAME_COLUMNS_PATTERN = "pattern";
  
  private static final String JSON_NAME_ROWS = "rows";
  private static final String JSON_NAME_ROWS_V = "v";
  private static final String JSON_NAME_ROWS_F = "f";

  private JSONArray columns = new JSONArray();
  private JSONArray rows = new JSONArray();
  
  public JSONObject createTable() throws JSONException{
    JSONObject table = new JSONObject();
    table.put(JSON_NAME_COLUMNS, columns);
    table.put(JSON_NAME_ROWS, rows);    
    return table;
  }
  
  private JSONObject createCell(Object v, String f) throws JSONException{
    JSONObject cell = new JSONObject();
    cell.put(JSON_NAME_ROWS_V, v);
    if(f != null)
      cell.put(JSON_NAME_ROWS_F, f);
    return cell;
  }
  
  /**
   * Add a column of data.
   * id is required and it must be unique, the rest are optional
   * 
   * type should be one of
   * 'boolean' - JavaScript boolean value ('true' or 'false'). Example value: v:'true'
   * 'number' - JavaScript number value. Example values: v:7 , v:3.14, v:-55
   * 'string' - JavaScript string value. Example value: v:'hello'
   * 'date' - JavaScript Date object (zero-based month), with the time truncated. Example value: v:new Date(2008, 0, 15)
   * 'datetime' - JavaScript Date object including the time. Example value: v:new Date(2008, 0, 15, 14, 30, 45)
   * 'timeofday' - Array of three numbers and an optional fourth, representing hour (0 indicates midnight), minute, second, and optional millisecond. Example values: v:[8, 15, 0], v: [6, 12, 1, 144]
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
    columns.put( column );
  }
  
  /**
   * Add a row of data
   */
  public void addRow(Object[] vals) {
    addRow(vals, false);
  }
  
  /**
   * Add a row of data.  Each row is an array of Strings.
   * If hasFormattedValues is true, then each column is represented by
   * a pair of values: the first is the value (an Object, should be one of:
   * Boolean, Double, Integer, JSONArray, JSONObject, Long, String, or the JSONObject.NULL object.
   * The second is that should be displayed.  Otherwise each column should be 
   * represented by a single value in the array.
   * The number of columns in the array should correspond to the
   * number of columns added to this object. 
   * 
   * @see http://www.json.org/javadoc/org/json/JSONObject.html#put(java.lang.String,%20java.lang.Object)
   */
  public void addRow(Object[] vals, boolean hasFormattedValues) {
    JSONArray rowArray = new JSONArray();
    JSONObject row = new JSONObject();
    for (int i=0; i<vals.length; i++) {
      Object v = vals[i];
      Object f = null;
      if (hasFormattedValues && i+1 < vals.length) {
        f = vals[i+1];
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
      row.put( "c", rowArray );
    } catch (Exception e) {
      //leave row blank
    }
    rows.put( row );
  }
  
  public String toString() {
//    JSONObject json = new JSONObject();
//    try {
//      
//      json.put("requestId", "0");
//      json.put("status", "ok");
////      json.put("signature", "6173382439516707022");
//      json.put(JSON_NAME_TABLE, this.createTable());
//      return json.toString();
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
//    return null;
    try {
      return createTable().toString();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

}