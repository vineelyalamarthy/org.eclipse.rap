/*******************************************************************************
 * Copyright (c) 2009-2010 David Donahue and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     David Donahue - initial API, implementation and documentation
 *     Austin Riddle (Texas Center for Applied Technology) - 
 *         performance enhancements, javadoc cleanup, port of internal 
 *         org.json code to external depenency.
 ******************************************************************************/
package org.eclipse.rap.rwt.visualization.google.json;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONString;

/**
 * <p>
 * Provides a JSON data transport mechanism for google visualization data.
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
   * However, the JSON object will not be compatible with the google widgets if java.util.Date instances have
   * been added to this table.
   * <p>
   * @see JSONGoogleDataTable.toString() to transform this table into a string that is compatible with the
   * google visualization widgets.
   * </p>  
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
   * This method returns a string that is compatible for use with the google visualization widgets.
   * Date objects are processed according to Google visualization requirements. 
   */
  //HACK [ar] : accomodation for bug 305278 - this is a bypass of JSONObject.toString().
  public String toString() {
    try {
      JSONObject table = createTable();
      Iterator     keys = table.keys();
      StringBuffer sb = new StringBuffer("{");
      
      while (keys.hasNext()) {
        if (sb.length() > 1) {
          sb.append(',');
        }
        Object o = keys.next();
        sb.append(JSONObject.quote(o.toString()));
        sb.append(':');
        sb.append(valueToString(table.get((String)o)));
      }
      sb.append('}');
      return sb.toString();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }
  
  /**
   * Make a JSON text of an Object value. If the object has an
   * value.toJSONString() method, then that method will be used to produce
   * the JSON text. The method is required to produce a strictly
   * conforming text. If the object does not contain a toJSONString
   * method (which is the most common case), then a text will be
   * produced by other means. If the value is an array or Collection,
   * then a JSONArray will be made from it and its toJSONString method
   * will be called. If the value is a MAP, then a JSONObject will be made
   * from it and its toJSONString method will be called. Otherwise, the
   * value's toString method will be called, and the result will be quoted.
   * Date objects are processed according to Google visualization requirements.
   *
   * <p>
   * Warning: This method assumes that the data structure is acyclical.
   * @param value The value to be serialized.
   * @return a printable, displayable, transmittable
   *  representation of the object, beginning
   *  with <code>{</code>&nbsp;<small>(left brace)</small> and ending
   *  with <code>}</code>&nbsp;<small>(right brace)</small>.
   * @throws JSONException If the value is or contains an invalid number.
   */
  //HACK [ar] : accomodation for bug 305278 - this is a bypass of JSONObject.valueToString(). 
  public static String valueToString(Object value) throws JSONException {
    if (value == null || value.equals(null)) {
      return "null";
    }
    if (value instanceof JSONString) {
      Object o;
      try {
        o = ((JSONString)value).toJSONString();
      } catch (Exception e) {
        throw new JSONException(e);
      }
      if (o instanceof String) {
        return (String)o;
      }
      throw new JSONException("Bad value from toJSONString: " + o);
    }
    if (value instanceof Number) {
      return JSONObject.numberToString((Number) value);
    }
    if (value instanceof JSONArray) {
      try {
        return '[' + join((JSONArray)value,",") + ']';
      } catch (Exception e) {
        return null;
      }
    }
    if (value instanceof Boolean) {
      return value.toString();
    }
    if (value instanceof JSONObject) {
      return jsonObjectToString((JSONObject)value);
    }
    if (value instanceof Date) {
      return dateToString((Date)value);
    }
    if (value instanceof Map) {
      return new JSONObject((Map)value).toString();
    }
    if (value instanceof Collection) {
      return new JSONArray((Collection)value).toString();
    }
    if (value.getClass().isArray()) {
      return new JSONArray(value).toString();
    }
    return JSONObject.quote(value.toString());
  }
  
  /*
   * Converts a date into the form required by the google api.
   */
  private static String dateToString( Date d ) throws JSONException {
    if (d == null) {
      throw new JSONException("Null pointer");
    }
    Calendar c = Calendar.getInstance();
    c.setTime( d );
    String s = "new Date(";
    s += c.get(Calendar.YEAR) + ",";
    s += c.get(Calendar.MONTH) + ",";
    s += c.get(Calendar.DAY_OF_MONTH) + ",";
    s += c.get(Calendar.HOUR_OF_DAY) + ",";
    s += c.get(Calendar.MINUTE) + ",";
    s += c.get(Calendar.SECOND);
    s += ")";
    return s;
  }
  
  /*
   * Converts a JSONObject instance to string in a way that is compatible with the google api.
   * 
   */
  //HACK [ar] : accomodation for bug 305278 - this is a bypass of JSONObject.toString().
  private static String jsonObjectToString (JSONObject jObj) {
    try {
      Iterator keys = jObj.keys();
      StringBuffer sb = new StringBuffer("{");
      
      while (keys.hasNext()) {
        if (sb.length() > 1) {
          sb.append(',');
        }
        Object o = keys.next();
        sb.append(JSONObject.quote(o.toString()));
        sb.append(':');
        sb.append(valueToString(jObj.get((String)o)));
      }
      sb.append('}');
      return sb.toString();
    } catch (Exception e) {
      return null;
    }
  }
  
  /*
   * Converts a JSONArray instance to string in a way that is compatible with the google api.
   * 
   */
  //HACK [ar] : accomodation for bug 305278 - this is a bypass of JSONArray.join().
  private static String join(JSONArray arr, String separator) throws JSONException {
    int len = arr.length();
    StringBuffer sb = new StringBuffer();
    
    for (int i = 0; i < len; i += 1) {
      if (i > 0) {
        sb.append(separator);
      }
      sb.append(valueToString(arr.get(i)));
    }
    return sb.toString();
  }

}