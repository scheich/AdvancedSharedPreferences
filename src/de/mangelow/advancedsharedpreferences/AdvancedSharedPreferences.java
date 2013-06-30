/***
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may obtain
 * a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package de.mangelow.advancedsharedpreferences;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.util.Log;

/**
 * @author Philipp Mangelow <pmangelow at googlemail com>
 */
public class AdvancedSharedPreferences {

	// DEBUG

	private final String TAG = getClass().getSimpleName();
	private final boolean PRINT_E = false;

	private final boolean DEBUG = false;
	private boolean debug = DEBUG;

	// SHARED PREFERENCES

	private Context context;

	private SharedPreferences.Editor sp_save;
	private SharedPreferences sp_load;

	private final int MODE = Context.MODE_PRIVATE;

	private final String FILENAME = "Preferences";
	private String filename = FILENAME;

	private final String VALUE_DELIMITER = "µ";
	private String value_delimiter = VALUE_DELIMITER;

	private final int VALUE_MAXSIZE = 8192;
	private int value_maxsize = VALUE_MAXSIZE;

	private final String KEY_DELIMITER = "_";

	private final String KEY_ARRAY = "a";
	private final String KEY_ARRAYLIST = "al";

	// DATA TYPES

	private final String KEY_BOOLEAN = "boolean";
	private final String KEY_BYTE = "byte";
	private final String KEY_SHORT = "short";
	private final String KEY_INTEGER = "integer";
	private final String KEY_LONG = "long";
	private final String KEY_BIGINTEGER = "biginteger";
	private final String KEY_FLOAT = "float";
	private final String KEY_DOUBLE = "double";
	private final String KEY_BIGDECIMAL = "bigdecimal";
	private final String KEY_CHARACTER = "char";
	private final String KEY_STRING = "string";

	private final String KEY_DATE = "date";
	private final String KEY_GREGORIANCALENDAR = "gregoriancalendar";

	private final String KEY_LOCATION_PROVIDER = "location_provider";
	private final String KEY_LOCATION_LATITUDE = "location_latitude";
	private final String KEY_LOCATION_LONGITUDE = "location_longitude";
	private final String KEY_LOCATION_TIMESTAMP = "location_timestamp";
	private final String KEY_LOCATION_ACCURACY = "location_accuracy";
	private final String KEY_LOCATION_ALTITUDE = "location_altitude";
	private final String KEY_LOCATION_BEARING = "location_bearing";
	private final String KEY_LOCATION_SPEED = "location_speed";


	/**
	 * AdvancedSharedPreferences - Library for Android to extend SharedPreferences 
	 *  
	 * @param context  - The context to use. 
	 */
	public AdvancedSharedPreferences(Context context) {

		this.context = context;

		setEditors(context);
	}
	/**
	 * AdvancedSharedPreferences - Library for Android to extend SharedPreferences 
	 *  
	 * @param context  - The context to use. 
	 * @param filename  - Desired preferences file. (default: Preferences)
	 */
	public AdvancedSharedPreferences(Context context, String filename) {

		this.context = context;
		if(filename!=null)this.filename = filename;

		setEditors(context);
	}
	/**
	 * AdvancedSharedPreferences - Library for Android to extend SharedPreferences 
	 *  
	 * @param context  - The context to use. 
	 * @param filename  - Desired preferences file. (default: Preferences)
	 * @param value_delimiter  - The delimiter, that is used for splitting values. (default: µ) 
	 */
	public AdvancedSharedPreferences(Context context, String filename, String value_delimiter) {

		this.context = context;
		if(filename!=null)this.filename = filename;
		if(value_delimiter!=null)this.value_delimiter = value_delimiter;

		setEditors(context);
	}
	/**
	 * AdvancedSharedPreferences - Library for Android to extend SharedPreferences 
	 *  
	 * @param context  - The context to use. 
	 * @param filename  - Desired preferences file. (default: Preferences)
	 * @param value_delimiter  - The delimiter, that is used for splitting values. (default: µ) 
	 * @param value_maxsize  - The maximal preference value length. (default: 8192)
	 */
	public AdvancedSharedPreferences(Context context, String filename, String value_delimiter, int value_maxsize) {

		this.context = context;
		if(filename!=null)this.filename = filename;
		if(value_delimiter!=null)this.value_delimiter = value_delimiter;
		if(value_maxsize>0)this.value_maxsize = value_maxsize;

		setEditors(context);
	}	
	/**
	 * AdvancedSharedPreferences - Library for Android to extend SharedPreferences 
	 *  
	 * @param context  - The context to use. 
	 * @param filename  - Desired preferences file. (default: Preferences)
	 * @param value_delimiter  - The delimiter, that is used for splitting values. (default: µ) 
	 * @param value_maxsize  - The maximal preference value length. (default: 8192)
	 * @param debug  - Print errors to console (default: false)
	 */
	public AdvancedSharedPreferences(Context context, String filename, String value_delimiter, int value_maxsize, boolean debug) {

		this.context = context;
		if(filename!=null)this.filename = filename;
		if(value_delimiter!=null)this.value_delimiter = value_delimiter;
		if(value_maxsize>0)this.value_maxsize = value_maxsize;
		this.debug = debug;

		setEditors(context);
	}

	//

	private void setEditors(Context context) {

		this.sp_save = context.getSharedPreferences(filename, MODE).edit();
		this.sp_load = context.getSharedPreferences(filename, MODE);

	}

	private void savePref(String key, String value) throws Exception {
		int length = 0;
		if(value!=null)length = value.length();

		int count = 0;

		if(value==null||length<=value_maxsize) {
			sp_save.putString(key + KEY_DELIMITER + count, value);
		}
		else {
			for (int i = 0; i < length; i += value_maxsize) {
				int end = i + value_maxsize;
				if (end > length)end = length;
				sp_save.putString(key + KEY_DELIMITER + count, value.substring(i, end));
				count++;
			}
		}
		sp_save.commit();		
	}
	private String loadPref(String key, String defValue) throws Exception {
		String value = "";
		String temp;
		int count = 0;
		while (true) {
			temp = sp_load.getString(key + KEY_DELIMITER + count, defValue);
			if(temp==defValue) {
				if(count==0) {
					return defValue;
				}
				else {
					break;
				}
			}
			else {
				value += temp;
			}
			count++;				
		}
		return value;
	}
	private boolean delPref(String key) throws Exception {

		int foundandremoved = 0;

		if(sp_load.contains(key)) {
			sp_save.remove(key);
			foundandremoved++;
		}

		int count = 0;
		while (true) {
			if(sp_load.contains(key + KEY_DELIMITER + count)) {
				sp_save.remove(key + KEY_DELIMITER + count);
				count++;
				foundandremoved++;
			}
			else {
				break;
			}				
		}

		sp_save.commit();	

		if(foundandremoved>0)return true;
		return false;
	}
	
	//
	
	/**
	 * Clear all preferences in preference file
	 * 
	 * @return Returns true, if all preferences are cleared 
	 * 
	 */
	public boolean clearAllPref() {

		try {
			sp_save.clear();
			sp_save.commit();
			return true;
		} 
		catch (Exception e) {
			if(debug)Log.e(TAG, "Error clearing " + filename + ".xml");
			if(PRINT_E)Log.e(TAG, "Exception", e);
		}
		return false;
	}
	/**
	 * Delete preference file
	 * 
	 * @return Returns true, if preference file deleted.
	 * 
	 */
	public boolean deletePrefFile() {
		try {
			File file= new File(context.getApplicationInfo().dataDir + "/shared_prefs/" + filename + ".xml");
			file.delete();
			return true;
		} 
		catch (Exception e) {
			if(debug)Log.e(TAG, "Error deleting " + filename + ".xml");
			if(PRINT_E)Log.e(TAG, "Exception", e);
		}
		return false;
	}

	// BOOLEAN
	/**
	 * Save/modify preference
	 * 
	 * @param key 	The name of the preference to modify.
	 * @param value	The new value for the preference.
	 * @return 		Returns true if preference is saved.
	 */
	public boolean saveBooleanPref(String key, boolean value) {
		try {
			sp_save.putBoolean(KEY_BOOLEAN + KEY_DELIMITER + key, value);
			sp_save.commit();
			return true;
		} 
		catch (Exception e) {
			if(debug)Log.e(TAG, "Error saving " + key);
			if(PRINT_E)Log.e(TAG, "Exception", e);
		}
		return false;
	}
	/**
	 * Save/modify preference
	 * 
	 * @param key 	The name of the preference to modify.
	 * @param value	The new value for the preference.
	 * @return 		Returns true if preference is saved.
	 */
	public boolean saveBooleanArrayPref(String key, boolean [] value) {		
		int length = value.length;				
		try {
			String stringvalue = "";
			for (int i = 0; i < length; i++) {
				stringvalue += value[i];
				if(i<length-1)stringvalue += value_delimiter;
			}
			savePref(KEY_BOOLEAN + KEY_DELIMITER + KEY_ARRAY + KEY_DELIMITER + key, stringvalue);
			return true;
		} 
		catch (Exception e) {
			if(debug)Log.e(TAG, "Error saving " + key);
			if(PRINT_E)Log.e(TAG, "Exception", e);
		}
		return false;
	}	
	/**
	 * Save/modify preference
	 * 
	 * @param key 	The name of the preference to modify.
	 * @param value	The new value for the preference.
	 * @return 		Returns true if preference is saved.
	 */
	public boolean saveBooleanArrayListPref(String key, ArrayList<Boolean> value) {		
		int length = value.size();				
		try {
			String stringvalue = "";
			for (int i = 0; i < length; i++) {
				stringvalue += value.get(i);
				if(i<length-1)stringvalue += value_delimiter;
			}
			savePref(KEY_BOOLEAN + KEY_DELIMITER + KEY_ARRAYLIST + KEY_DELIMITER + key, stringvalue);
			return true;
		} 
		catch (Exception e) {
			if(debug)Log.e(TAG, "Error saving " + key);
			if(PRINT_E)Log.e(TAG, "Exception", e);
		}
		return false;
	}
	
	/**
	 * Load preference
	 * 
	 * @param key 	The name of the preference to retrieve.
	 * @param defValue	Value to return if this preference does not exist.
	 * @return 		Returns the preference value if it exists, or defValue.
	 */
	public boolean loadBooleanPref(String key, boolean defValue) {
		try {
			return sp_load.getBoolean(KEY_BOOLEAN + KEY_DELIMITER + key, defValue);
		} catch (Exception e) {
			if(debug)Log.e(TAG, "Error loading " + key);
			if(PRINT_E)Log.e(TAG, "Exception", e);
		}
		return defValue;
	}
	/**
	 * Load preference
	 * 
	 * @param key 	The name of the preference to retrieve.
	 * @param defValue	Value to return if this preference does not exist.
	 * @return 		Returns the preference value if it exists, or defValue.
	 */
	public boolean [] loadBooleanArrayPref(String key, boolean [] defValue) {
		try {
			String stringvalue = loadPref(KEY_BOOLEAN + KEY_DELIMITER + KEY_ARRAY + KEY_DELIMITER + key, "");
			if(stringvalue.length()==0)return defValue;

			String string_array [] = stringvalue.split(value_delimiter, -1);
			int length = string_array.length;

			boolean [] value = new boolean [length];
			for (int i = 0; i < length; i++) {
				value[i] = Boolean.parseBoolean(string_array[i]); 
			}

			return value;
		} 
		catch (Exception e) {
			if(debug)Log.e(TAG, "Error loading " + key);
			if(PRINT_E)Log.e(TAG, "Exception", e);
		}	
		return defValue;
	}
	/**
	 * Load preference
	 * 
	 * @param key 	The name of the preference to retrieve.
	 * @param defValue	Value to return if this preference does not exist.
	 * @return 		Returns the preference value if it exists, or defValue.
	 */
	public ArrayList<Boolean> loadBooleanArrayListPref(String key, ArrayList<Boolean> defValue) {
		try {
			String stringvalue = loadPref(KEY_BOOLEAN + KEY_DELIMITER + KEY_ARRAYLIST + KEY_DELIMITER + key, "");
			if(stringvalue.length()==0)return defValue;

			String string_array [] = stringvalue.split(value_delimiter, -1);
			int length = string_array.length;

			ArrayList<Boolean> value = new ArrayList<Boolean>();
			for (int i = 0; i < length; i++) {
				value.add(Boolean.parseBoolean(string_array[i]));
			}			
			return value;
		} 
		catch (Exception e) {
			if(debug)Log.e(TAG, "Error loading " + key);
			if(PRINT_E)Log.e(TAG, "Exception", e);
		}	
		return defValue;
	}
	
	/**
	 * Delete preference
	 * 
	 * @return Returns true, if preference deleted.
	 * 
	 */
	public boolean delBooleanPref(String key) {
		boolean value = false;

		try {
			value = delPref(KEY_BOOLEAN + KEY_DELIMITER + key);
		}
		catch (Exception e) {
			if(debug)Log.e(TAG, "Error deleting " + key);
			if(PRINT_E)Log.e(TAG, "Exception", e);
		}
		return value;
	}
	/**
	 * Delete preference
	 * 
	 * @return Returns true, if preference deleted.
	 * 
	 */
	public boolean delBooleanArrayPref(String key) {
		boolean value = false;

		try {
			value = delPref(KEY_BOOLEAN + KEY_DELIMITER + KEY_ARRAY + KEY_DELIMITER + key);
		}
		catch (Exception e) {
			if(debug)Log.e(TAG, "Error deleting " + key);
			if(PRINT_E)Log.e(TAG, "Exception", e);
		}
		return value;
	}
	/**
	 * Delete preference
	 * 
	 * @return Returns true, if preference deleted.
	 * 
	 */
	public boolean delBooleanArrayListPref(String key) {
		boolean value = false;

		try {
			value = delPref(KEY_BOOLEAN + KEY_DELIMITER + KEY_ARRAYLIST + KEY_DELIMITER + key);
		}
		catch (Exception e) {
			if(debug)Log.e(TAG, "Error deleting " + key);
			if(PRINT_E)Log.e(TAG, "Exception", e);
		}
		return value;
	}

	// BYTE
	/**
	 * Save/modify preference
	 * 
	 * @param key 	The name of the preference to modify.
	 * @param value	The new value for the preference.
	 * @return 		Returns true if preference is saved.
	 */
	public boolean saveBytePref(String key, byte value) {
		try {
			savePref(KEY_BYTE + KEY_DELIMITER + key, String.valueOf(value));
			return true;
		} 
		catch (Exception e) {
			if(debug)Log.e(TAG, "Error saving " + key);
			if(PRINT_E)Log.e(TAG, "Exception", e);
		}
		return false;
	}
	/**
	 * Save/modify preference
	 * 
	 * @param key 	The name of the preference to modify.
	 * @param value	The new value for the preference.
	 * @return 		Returns true if preference is saved.
	 */
	public boolean saveByteArrayPref(String key, byte [] value) {		
		int length = value.length;				
		try {
			String stringvalue = "";
			for (int i = 0; i < length; i++) {
				stringvalue += value[i];
				if(i<length-1)stringvalue += value_delimiter;
			}
			savePref(KEY_BYTE + KEY_DELIMITER + KEY_ARRAY + KEY_DELIMITER + key, stringvalue);
			return true;
		} 
		catch (Exception e) {
			if(debug)Log.e(TAG, "Error saving " + key);
			if(PRINT_E)Log.e(TAG, "Exception", e);
		}
		return false;
	}	
	/**
	 * Save/modify preference
	 * 
	 * @param key 	The name of the preference to modify.
	 * @param value	The new value for the preference.
	 * @return 		Returns true if preference is saved.
	 */
	public boolean saveByteArrayListPref(String key, ArrayList<Byte> value) {		
		int length = value.size();				
		try {
			String stringvalue = "";
			for (int i = 0; i < length; i++) {
				stringvalue += value.get(i);
				if(i<length-1)stringvalue += value_delimiter;
			}
			savePref(KEY_BYTE + KEY_DELIMITER + KEY_ARRAYLIST + KEY_DELIMITER + key, stringvalue);
			return true;
		} 
		catch (Exception e) {
			if(debug)Log.e(TAG, "Error saving " + key);
			if(PRINT_E)Log.e(TAG, "Exception", e);
		}
		return false;
	}
	
	/**
	 * Load preference
	 * 
	 * @param key 	The name of the preference to retrieve.
	 * @param defValue	Value to return if this preference does not exist.
	 * @return 		Returns the preference value if it exists, or defValue.
	 */
	public byte loadBytePref(String key, byte defValue) {
		try {
			return Byte.parseByte(loadPref(KEY_BYTE + KEY_DELIMITER + key, String.valueOf(defValue)));
		} catch (Exception e) {
			if(debug)Log.e(TAG, "Error loading " + key);
			if(PRINT_E)Log.e(TAG, "Exception", e);
		}
		return defValue;
	}
	/**
	 * Load preference
	 * 
	 * @param key 	The name of the preference to retrieve.
	 * @param defValue	Value to return if this preference does not exist.
	 * @return 		Returns the preference value if it exists, or defValue.
	 */
	public byte [] loadByteArrayPref(String key, byte [] defValue) {
		try {
			String stringvalue = loadPref(KEY_BYTE + KEY_DELIMITER + KEY_ARRAY + KEY_DELIMITER + key, "");
			if(stringvalue.length()==0)return defValue;

			String string_array [] = stringvalue.split(value_delimiter, -1);
			int length = string_array.length;

			byte [] value = new byte [length];
			for (int i = 0; i < length; i++) {
				value[i] = Byte.parseByte(string_array[i]); 
			}

			return value;
		} 
		catch (Exception e) {
			if(debug)Log.e(TAG, "Error loading " + key);
			if(PRINT_E)Log.e(TAG, "Exception", e);
		}	
		return defValue;
	}
	/**
	 * Load preference
	 * 
	 * @param key 	The name of the preference to retrieve.
	 * @param defValue	Value to return if this preference does not exist.
	 * @return 		Returns the preference value if it exists, or defValue.
	 */
	public ArrayList<Byte> loadByteArrayListPref(String key, ArrayList<Byte> defValue) {
		try {
			String stringvalue = loadPref(KEY_BYTE + KEY_DELIMITER + KEY_ARRAYLIST + KEY_DELIMITER + key, "");
			if(stringvalue.length()==0)return defValue;

			String string_array [] = stringvalue.split(value_delimiter, -1);
			int length = string_array.length;

			ArrayList<Byte> value = new ArrayList<Byte>();
			for (int i = 0; i < length; i++) {
				value.add(Byte.parseByte(string_array[i]));
			}			
			return value;
		} 
		catch (Exception e) {
			if(debug)Log.e(TAG, "Error loading " + key);
			if(PRINT_E)Log.e(TAG, "Exception", e);
		}	
		return defValue;
	}
	
	/**
	 * Delete preference
	 * 
	 * @return Returns true, if preference deleted.
	 * 
	 */
	public boolean delBytePref(String key) {
		boolean value = false;

		try {
			value = delPref(KEY_BYTE + KEY_DELIMITER + key);
		}
		catch (Exception e) {
			if(debug)Log.e(TAG, "Error deleting " + key);
			if(PRINT_E)Log.e(TAG, "Exception", e);
		}
		return value;
	}
	/**
	 * Delete preference
	 * 
	 * @return Returns true, if preference deleted.
	 * 
	 */
	public boolean delByteArrayPref(String key) {
		boolean value = false;

		try {
			value = delPref(KEY_BYTE + KEY_DELIMITER + KEY_ARRAY + KEY_DELIMITER + key);
		}
		catch (Exception e) {
			if(debug)Log.e(TAG, "Error deleting " + key);
			if(PRINT_E)Log.e(TAG, "Exception", e);
		}
		return value;
	}
	/**
	 * Delete preference
	 * 
	 * @return Returns true, if preference deleted.
	 * 
	 */
	public boolean delByteArrayListPref(String key) {
		boolean value = false;

		try {
			value = delPref(KEY_BYTE + KEY_DELIMITER + KEY_ARRAYLIST + KEY_DELIMITER + key);
		}
		catch (Exception e) {
			if(debug)Log.e(TAG, "Error deleting " + key);
			if(PRINT_E)Log.e(TAG, "Exception", e);
		}
		return value;
	}

	// SHORT
	
	/**
	 * Save/modify preference
	 * 
	 * @param key 	The name of the preference to modify.
	 * @param value	The new value for the preference.
	 * @return 		Returns true if preference is saved.
	 */
	public boolean saveShortPref(String key, short value) {
		try {
			savePref(KEY_SHORT + KEY_DELIMITER + key, String.valueOf(value));
			return true;
		} 
		catch (Exception e) {
			if(debug)Log.e(TAG, "Error saving " + key);
			if(PRINT_E)Log.e(TAG, "Exception", e);
		}
		return false;
	}
	/**
	 * Save/modify preference
	 * 
	 * @param key 	The name of the preference to modify.
	 * @param value	The new value for the preference.
	 * @return 		Returns true if preference is saved.
	 */
	public boolean saveShortArrayPref(String key, short [] value) {		
		int length = value.length;				
		try {
			String stringvalue = "";
			for (int i = 0; i < length; i++) {
				stringvalue += value[i];
				if(i<length-1)stringvalue += value_delimiter;
			}
			savePref(KEY_SHORT + KEY_DELIMITER + KEY_ARRAY + KEY_DELIMITER + key, stringvalue);
			return true;
		} 
		catch (Exception e) {
			if(debug)Log.e(TAG, "Error saving " + key);
			if(PRINT_E)Log.e(TAG, "Exception", e);
		}
		return false;
	}
	/**
	 * Save/modify preference
	 * 
	 * @param key 	The name of the preference to modify.
	 * @param value	The new value for the preference.
	 * @return 		Returns true if preference is saved.
	 */
	public boolean saveShortArrayListPref(String key, ArrayList<Short> value) {		
		int length = value.size();				
		try {
			String stringvalue = "";
			for (int i = 0; i < length; i++) {
				stringvalue += value.get(i);
				if(i<length-1)stringvalue += value_delimiter;
			}
			savePref(KEY_SHORT + KEY_DELIMITER + KEY_ARRAYLIST + KEY_DELIMITER + key, stringvalue);
			return true;
		} 
		catch (Exception e) {
			if(debug)Log.e(TAG, "Error saving " + key);
			if(PRINT_E)Log.e(TAG, "Exception", e);
		}
		return false;
	}
	
	/**
	 * Load preference
	 * 
	 * @param key 	The name of the preference to retrieve.
	 * @param defValue	Value to return if this preference does not exist.
	 * @return 		Returns the preference value if it exists, or defValue.
	 */
	public short loadShortPref(String key, short defValue) {
		try {
			return Short.parseShort(loadPref(KEY_SHORT + KEY_DELIMITER + key, String.valueOf(defValue)));
		} catch (Exception e) {
			if(debug)Log.e(TAG, "Error loading " + key);
			if(PRINT_E)Log.e(TAG, "Exception", e);
		}
		return defValue;
	}
	/**
	 * Load preference
	 * 
	 * @param key 	The name of the preference to retrieve.
	 * @param defValue	Value to return if this preference does not exist.
	 * @return 		Returns the preference value if it exists, or defValue.
	 */
	public short [] loadShortArrayPref(String key, short [] defValue) {
		try {
			String stringvalue = loadPref(KEY_SHORT + KEY_DELIMITER + KEY_ARRAY + KEY_DELIMITER + key, "");
			if(stringvalue.length()==0)return defValue;

			String string_array [] = stringvalue.split(value_delimiter, -1);
			int length = string_array.length;

			short [] value = new short [length];
			for (int i = 0; i < length; i++) {
				value[i] = Short.parseShort(string_array[i]); 
			}

			return value;
		} 
		catch (Exception e) {
			if(debug)Log.e(TAG, "Error loading " + key);
			if(PRINT_E)Log.e(TAG, "Exception", e);
		}	
		return defValue;
	}
	/**
	 * Load preference
	 * 
	 * @param key 	The name of the preference to retrieve.
	 * @param defValue	Value to return if this preference does not exist.
	 * @return 		Returns the preference value if it exists, or defValue.
	 */
	public ArrayList<Short> loadShortArrayListPref(String key, ArrayList<Short> defValue) {
		try {
			String stringvalue = loadPref(KEY_SHORT + KEY_DELIMITER + KEY_ARRAYLIST + KEY_DELIMITER + key, "");
			if(stringvalue.length()==0)return defValue;

			String string_array [] = stringvalue.split(value_delimiter, -1);
			int length = string_array.length;

			ArrayList<Short> value = new ArrayList<Short>();
			for (int i = 0; i < length; i++) {
				value.add(Short.parseShort(string_array[i]));
			}			
			return value;
		} 
		catch (Exception e) {
			if(debug)Log.e(TAG, "Error loading " + key);
			if(PRINT_E)Log.e(TAG, "Exception", e);
		}	
		return defValue;
	}
	
	/**
	 * Delete preference
	 * 
	 * @return Returns true, if preference deleted.
	 * 
	 */
	public boolean delShortPref(String key) {
		boolean value = false;

		try {
			value = delPref(KEY_SHORT + KEY_DELIMITER + key);
		}
		catch (Exception e) {
			if(debug)Log.e(TAG, "Error deleting " + key);
			if(PRINT_E)Log.e(TAG, "Exception", e);
		}
		return value;
	}
	/**
	 * Delete preference
	 * 
	 * @return Returns true, if preference deleted.
	 * 
	 */
	public boolean delShortArrayPref(String key) {
		boolean value = false;

		try {
			value = delPref(KEY_SHORT + KEY_DELIMITER + KEY_ARRAY + KEY_DELIMITER + key);
		}
		catch (Exception e) {
			if(debug)Log.e(TAG, "Error deleting " + key);
			if(PRINT_E)Log.e(TAG, "Exception", e);
		}
		return value;
	}
	/**
	 * Delete preference
	 * 
	 * @return Returns true, if preference deleted.
	 * 
	 */
	public boolean delShortArrayListPref(String key) {
		boolean value = false;

		try {
			value = delPref(KEY_SHORT + KEY_DELIMITER + KEY_ARRAYLIST + KEY_DELIMITER + key);
		}
		catch (Exception e) {
			if(debug)Log.e(TAG, "Error deleting " + key);
			if(PRINT_E)Log.e(TAG, "Exception", e);
		}
		return value;
	}

	// INTEGER
	
	/**
	 * Save/modify preference
	 * 
	 * @param key 	The name of the preference to modify.
	 * @param value	The new value for the preference.
	 * @return 		Returns true if preference is saved.
	 */
	public boolean saveIntegerPref(String key, int value) {		
		try {
			sp_save.putInt(KEY_INTEGER + KEY_DELIMITER + key, value);
			sp_save.commit();
			return true;
		} 
		catch (Exception e) {
			if(debug)Log.e(TAG, "Error saving " + key);
			if(PRINT_E)Log.e(TAG, "Exception", e);
		}
		return false;
	}
	/**
	 * Save/modify preference
	 * 
	 * @param key 	The name of the preference to modify.
	 * @param value	The new value for the preference.
	 * @return 		Returns true if preference is saved.
	 */
	public boolean saveIntegerArrayPref(String key, int [] value) {		
		int length = value.length;				
		try {
			String stringvalue = "";
			for (int i = 0; i < length; i++) {
				stringvalue += value[i];
				if(i<length-1)stringvalue += value_delimiter;
			}
			savePref(KEY_INTEGER + KEY_DELIMITER + KEY_ARRAY + KEY_DELIMITER + key, stringvalue);
			return true;
		} 
		catch (Exception e) {
			if(debug)Log.e(TAG, "Error saving " + key);
			if(PRINT_E)Log.e(TAG, "Exception", e);
		}
		return false;
	}
	/**
	 * Save/modify preference
	 * 
	 * @param key 	The name of the preference to modify.
	 * @param value	The new value for the preference.
	 * @return 		Returns true if preference is saved.
	 */
	public boolean saveIntegerArrayListPref(String key, ArrayList<Integer> value) {		
		int length = value.size();				
		try {
			String stringvalue = "";
			for (int i = 0; i < length; i++) {
				stringvalue += value.get(i);
				if(i<length-1)stringvalue += value_delimiter;
			}
			savePref(KEY_INTEGER + KEY_DELIMITER + KEY_ARRAYLIST + KEY_DELIMITER + key, stringvalue);
			return true;
		} 
		catch (Exception e) {
			if(debug)Log.e(TAG, "Error saving " + key);
			if(PRINT_E)Log.e(TAG, "Exception", e);
		}
		return false;
	}
	
	/**
	 * Load preference
	 * 
	 * @param key 	The name of the preference to retrieve.
	 * @param defValue	Value to return if this preference does not exist.
	 * @return 		Returns the preference value if it exists, or defValue.
	 */
	public int loadIntegerPref(String key, int defValue) {
		try {
			return sp_load.getInt(KEY_INTEGER + KEY_DELIMITER + key, defValue);
		} catch (Exception e) {
			if(debug)Log.e(TAG, "Error loading " + key);
			if(PRINT_E)Log.e(TAG, "Exception", e);
		}
		return defValue;
	}
	/**
	 * Load preference
	 * 
	 * @param key 	The name of the preference to retrieve.
	 * @param defValue	Value to return if this preference does not exist.
	 * @return 		Returns the preference value if it exists, or defValue.
	 */
	public int [] loadIntegerArrayPref(String key, int [] defValue) {
		try {
			String stringvalue = loadPref(KEY_INTEGER + KEY_DELIMITER + KEY_ARRAY + KEY_DELIMITER + key, "");
			if(stringvalue.length()==0)return defValue;

			String string_array [] = stringvalue.split(value_delimiter, -1);
			int length = string_array.length;

			int [] value = new int [length];
			for (int i = 0; i < length; i++) {
				value[i] = Integer.parseInt(string_array[i]); 
			}

			return value;
		} 
		catch (Exception e) {
			if(debug)Log.e(TAG, "Error loading " + key);
			if(PRINT_E)Log.e(TAG, "Exception", e);
		}	
		return defValue;
	}
	/**
	 * Load preference
	 * 
	 * @param key 	The name of the preference to retrieve.
	 * @param defValue	Value to return if this preference does not exist.
	 * @return 		Returns the preference value if it exists, or defValue.
	 */
	public ArrayList<Integer> loadIntegerArrayListPref(String key, ArrayList<Integer> defValue) {
		try {
			String stringvalue = loadPref(KEY_INTEGER + KEY_DELIMITER + KEY_ARRAYLIST + KEY_DELIMITER + key, "");
			if(stringvalue.length()==0)return defValue;

			String string_array [] = stringvalue.split(value_delimiter, -1);
			int length = string_array.length;

			ArrayList<Integer> value = new ArrayList<Integer>();
			for (int i = 0; i < length; i++) {
				value.add(Integer.parseInt(string_array[i]));
			}			
			return value;
		} 
		catch (Exception e) {
			if(debug)Log.e(TAG, "Error loading " + key);
			if(PRINT_E)Log.e(TAG, "Exception", e);
		}	
		return defValue;
	}
	
	/**
	 * Delete preference
	 * 
	 * @return Returns true, if preference deleted.
	 * 
	 */
	public boolean delIntegerPref(String key) {
		boolean value = false;

		try {
			value = delPref(KEY_INTEGER + KEY_DELIMITER + key);
		}
		catch (Exception e) {
			if(debug)Log.e(TAG, "Error deleting " + key);
			if(PRINT_E)Log.e(TAG, "Exception", e);
		}
		return value;
	}
	/**
	 * Delete preference
	 * 
	 * @return Returns true, if preference deleted.
	 * 
	 */
	public boolean delIntegerArrayPref(String key) {
		boolean value = false;

		try {
			value = delPref(KEY_INTEGER + KEY_DELIMITER + KEY_ARRAY + KEY_DELIMITER + key);
		}
		catch (Exception e) {
			if(debug)Log.e(TAG, "Error deleting " + key);
			if(PRINT_E)Log.e(TAG, "Exception", e);
		}
		return value;
	}
	/**
	 * Delete preference
	 * 
	 * @return Returns true, if preference deleted.
	 * 
	 */
	public boolean delIntegerArrayListPref(String key) {
		boolean value = false;

		try {
			value = delPref(KEY_INTEGER + KEY_DELIMITER + KEY_ARRAYLIST + KEY_DELIMITER + key);
		}
		catch (Exception e) {
			if(debug)Log.e(TAG, "Error deleting " + key);
			if(PRINT_E)Log.e(TAG, "Exception", e);
		}
		return value;
	}

	// LONG
	
	/**
	 * Save/modify preference
	 * 
	 * @param key 	The name of the preference to modify.
	 * @param value	The new value for the preference.
	 * @return 		Returns true if preference is saved.
	 */
	public boolean saveLongPref(String key, long value) {
		try {
			sp_save.putLong(KEY_LONG + KEY_DELIMITER + key, value);
			sp_save.commit();
			return true;
		} 
		catch (Exception e) {
			if(debug)Log.e(TAG, "Error saving " + key);
			if(PRINT_E)Log.e(TAG, "Exception", e);
		}
		return false;
	}
	/**
	 * Save/modify preference
	 * 
	 * @param key 	The name of the preference to modify.
	 * @param value	The new value for the preference.
	 * @return 		Returns true if preference is saved.
	 */
	public boolean saveLongArrayPref(String key, long [] value) {		
		int length = value.length;				
		try {
			String stringvalue = "";
			for (int i = 0; i < length; i++) {
				stringvalue += value[i];
				if(i<length-1)stringvalue += value_delimiter;
			}
			savePref(KEY_LONG + KEY_DELIMITER + KEY_ARRAY + KEY_DELIMITER + key, stringvalue);
			return true;
		} 
		catch (Exception e) {
			if(debug)Log.e(TAG, "Error saving " + key);
			if(PRINT_E)Log.e(TAG, "Exception", e);
		}
		return false;
	}
	/**
	 * Save/modify preference
	 * 
	 * @param key 	The name of the preference to modify.
	 * @param value	The new value for the preference.
	 * @return 		Returns true if preference is saved.
	 */
	public boolean saveLongArrayListPref(String key, ArrayList<Long> value) {		
		int length = value.size();				
		try {
			String stringvalue = "";
			for (int i = 0; i < length; i++) {
				stringvalue += value.get(i);
				if(i<length-1)stringvalue += value_delimiter;
			}
			savePref(KEY_LONG + KEY_DELIMITER + KEY_ARRAYLIST + KEY_DELIMITER + key, stringvalue);
			return true;
		} 
		catch (Exception e) {
			if(debug)Log.e(TAG, "Error saving " + key);
			if(PRINT_E)Log.e(TAG, "Exception", e);
		}
		return false;
	}
	
	/**
	 * Load preference
	 * 
	 * @param key 	The name of the preference to retrieve.
	 * @param defValue	Value to return if this preference does not exist.
	 * @return 		Returns the preference value if it exists, or defValue.
	 */
	public long loadLongPref(String key, long defValue) {
		try {
			return sp_load.getLong(KEY_LONG + KEY_DELIMITER + key, defValue);
		} catch (Exception e) {
			if(debug)Log.e(TAG, "Error loading " + key);
			if(PRINT_E)Log.e(TAG, "Exception", e);
		}
		return defValue;
	}
	/**
	 * Load preference
	 * 
	 * @param key 	The name of the preference to retrieve.
	 * @param defValue	Value to return if this preference does not exist.
	 * @return 		Returns the preference value if it exists, or defValue.
	 */
	public long [] loadLongArrayPref(String key, long [] defValue) {
		try {
			String stringvalue = loadPref(KEY_LONG + KEY_DELIMITER + KEY_ARRAY + KEY_DELIMITER + key, "");
			if(stringvalue.length()==0)return defValue;

			String string_array [] = stringvalue.split(value_delimiter, -1);
			int length = string_array.length;

			long [] value = new long [length];
			for (int i = 0; i < length; i++) {
				value[i] = Long.parseLong(string_array[i]); 
			}

			return value;
		} 
		catch (Exception e) {
			if(debug)Log.e(TAG, "Error loading " + key);
			if(PRINT_E)Log.e(TAG, "Exception", e);
		}	
		return defValue;
	}
	/**
	 * Load preference
	 * 
	 * @param key 	The name of the preference to retrieve.
	 * @param defValue	Value to return if this preference does not exist.
	 * @return 		Returns the preference value if it exists, or defValue.
	 */
	public ArrayList<Long> loadLongArrayListPref(String key, ArrayList<Long> defValue) {
		try {
			String stringvalue = loadPref(KEY_LONG + KEY_DELIMITER + KEY_ARRAYLIST + KEY_DELIMITER + key, "");
			if(stringvalue.length()==0)return defValue;

			String string_array [] = stringvalue.split(value_delimiter, -1);
			int length = string_array.length;

			ArrayList<Long> value = new ArrayList<Long>();
			for (int i = 0; i < length; i++) {
				value.add(Long.parseLong(string_array[i]));
			}			
			return value;
		} 
		catch (Exception e) {
			if(debug)Log.e(TAG, "Error loading " + key);
			if(PRINT_E)Log.e(TAG, "Exception", e);
		}	
		return defValue;
	}
	
	/**
	 * Delete preference
	 * 
	 * @return Returns true, if preference deleted.
	 * 
	 */
	public boolean delLongPref(String key) {
		boolean value = false;

		try {
			value = delPref(KEY_LONG + KEY_DELIMITER + key);			
		}
		catch (Exception e) {
			if(debug)Log.e(TAG, "Error deleting " + key);
			if(PRINT_E)Log.e(TAG, "Exception", e);
		}
		return value;
	}
	/**
	 * Delete preference
	 * 
	 * @return Returns true, if preference deleted.
	 * 
	 */
	public boolean delLongArrayPref(String key) {
		boolean value = false;

		try {
			value = delPref(KEY_LONG + KEY_DELIMITER + KEY_ARRAY + KEY_DELIMITER + key);
		}
		catch (Exception e) {
			if(debug)Log.e(TAG, "Error deleting " + key);
			if(PRINT_E)Log.e(TAG, "Exception", e);
		}
		return value;
	}
	/**
	 * Delete preference
	 * 
	 * @return Returns true, if preference deleted.
	 * 
	 */
	public boolean delLongArrayListPref(String key) {
		boolean value = false;

		try {
			value = delPref(KEY_LONG + KEY_DELIMITER + KEY_ARRAYLIST + KEY_DELIMITER + key);
		}
		catch (Exception e) {
			if(debug)Log.e(TAG, "Error deleting " + key);
			if(PRINT_E)Log.e(TAG, "Exception", e);
		}
		return value;
	}

	// BIGDECIMAL
	
	/**
	 * Save/modify preference
	 * 
	 * @param key 	The name of the preference to modify.
	 * @param value	The new value for the preference.
	 * @return 		Returns true if preference is saved.
	 */
	public boolean saveBigIntegerPref(String key, BigInteger value) {
		try {
			sp_save.putLong(KEY_BIGINTEGER + KEY_DELIMITER + key, value.longValue());
			sp_save.commit();
			return true;
		} 
		catch (Exception e) {
			if(debug)Log.e(TAG, "Error saving " + key);
			if(PRINT_E)Log.e(TAG, "Exception", e);
		}
		return false;
	}
	/**
	 * Save/modify preference
	 * 
	 * @param key 	The name of the preference to modify.
	 * @param value	The new value for the preference.
	 * @return 		Returns true if preference is saved.
	 */
	public boolean saveBigIntegerArrayPref(String key, BigInteger [] value) {		
		int length = value.length;				
		try {
			String stringvalue = "";
			for (int i = 0; i < length; i++) {
				stringvalue += value[i];
				if(i<length-1)stringvalue += value_delimiter;
			}
			savePref(KEY_BIGINTEGER + KEY_DELIMITER + KEY_ARRAY + KEY_DELIMITER + key, stringvalue);
			return true;
		} 
		catch (Exception e) {
			if(debug)Log.e(TAG, "Error saving " + key);
			if(PRINT_E)Log.e(TAG, "Exception", e);
		}
		return false;
	}
	/**
	 * Save/modify preference
	 * 
	 * @param key 	The name of the preference to modify.
	 * @param value	The new value for the preference.
	 * @return 		Returns true if preference is saved.
	 */
	public boolean saveBigIntegerArrayListPref(String key, ArrayList<BigInteger> value) {		
		int length = value.size();				
		try {
			String stringvalue = "";
			for (int i = 0; i < length; i++) {
				stringvalue += value.get(i);
				if(i<length-1)stringvalue += value_delimiter;
			}
			savePref(KEY_BIGINTEGER + KEY_DELIMITER + KEY_ARRAYLIST + KEY_DELIMITER + key, stringvalue);
			return true;
		} 
		catch (Exception e) {
			if(debug)Log.e(TAG, "Error saving " + key);
			if(PRINT_E)Log.e(TAG, "Exception", e);
		}
		return false;
	}
	
	/**
	 * Load preference
	 * 
	 * @param key 	The name of the preference to retrieve.
	 * @param defValue	Value to return if this preference does not exist.
	 * @return 		Returns the preference value if it exists, or defValue.
	 */
	public BigInteger loadBigIntegerPref(String key, BigInteger defValue) {
		try {
			return BigInteger.valueOf(Long.parseLong(loadPref(KEY_BIGINTEGER + KEY_DELIMITER + key, String.valueOf(defValue))));
		} catch (Exception e) {
			if(debug)Log.e(TAG, "Error loading " + key);
			if(PRINT_E)Log.e(TAG, "Exception", e);
		}
		return defValue;
	}
	/**
	 * Load preference
	 * 
	 * @param key 	The name of the preference to retrieve.
	 * @param defValue	Value to return if this preference does not exist.
	 * @return 		Returns the preference value if it exists, or defValue.
	 */
	public BigInteger [] loadBigIntegerArrayPref(String key, BigInteger [] defValue) {
		try {
			String stringvalue = loadPref(KEY_BIGINTEGER + KEY_DELIMITER + KEY_ARRAY + KEY_DELIMITER + key, "");
			if(stringvalue.length()==0)return defValue;

			String string_array [] = stringvalue.split(value_delimiter, -1);
			int length = string_array.length;

			BigInteger [] value = new BigInteger [length];
			for (int i = 0; i < length; i++) {
				value[i] = BigInteger.valueOf(Long.parseLong(string_array[i])); 
			}

			return value;
		} 
		catch (Exception e) {
			if(debug)Log.e(TAG, "Error loading " + key);
			if(PRINT_E)Log.e(TAG, "Exception", e);
		}	
		return defValue;
	}
	/**
	 * Load preference
	 * 
	 * @param key 	The name of the preference to retrieve.
	 * @param defValue	Value to return if this preference does not exist.
	 * @return 		Returns the preference value if it exists, or defValue.
	 */
	public ArrayList<BigInteger> loadBigIntegerArrayListPref(String key, ArrayList<BigInteger> defValue) {
		try {
			String stringvalue = loadPref(KEY_BIGINTEGER + KEY_DELIMITER + KEY_ARRAYLIST + KEY_DELIMITER + key, "");
			if(stringvalue.length()==0)return defValue;

			String string_array [] = stringvalue.split(value_delimiter, -1);
			int length = string_array.length;

			ArrayList<BigInteger> value = new ArrayList<BigInteger>();
			for (int i = 0; i < length; i++) {
				value.add(BigInteger.valueOf(Long.parseLong(string_array[i])));
			}			
			return value;
		} 
		catch (Exception e) {
			if(debug)Log.e(TAG, "Error loading " + key);
			if(PRINT_E)Log.e(TAG, "Exception", e);
		}	
		return defValue;
	}
	
	/**
	 * Delete preference
	 * 
	 * @return Returns true, if preference deleted.
	 * 
	 */
	public boolean delBigIntegerPref(String key) {
		boolean value = false;

		try {
			value = delPref(KEY_BIGINTEGER + KEY_DELIMITER + key);
		}
		catch (Exception e) {
			if(debug)Log.e(TAG, "Error deleting " + key);
			if(PRINT_E)Log.e(TAG, "Exception", e);
		}
		return value;
	}
	/**
	 * Delete preference
	 * 
	 * @return Returns true, if preference deleted.
	 * 
	 */
	public boolean delBigIntegerArrayPref(String key) {
		boolean value = false;

		try {
			value = delPref(KEY_BIGINTEGER + KEY_DELIMITER + KEY_ARRAY + KEY_DELIMITER + key);
		}
		catch (Exception e) {
			if(debug)Log.e(TAG, "Error deleting " + key);
			if(PRINT_E)Log.e(TAG, "Exception", e);
		}
		return value;
	}
	/**
	 * Delete preference
	 * 
	 * @return Returns true, if preference deleted.
	 * 
	 */
	public boolean delBigIntegerArrayListPref(String key) {
		boolean value = false;

		try {
			value = delPref(KEY_BIGINTEGER + KEY_DELIMITER + KEY_ARRAYLIST + KEY_DELIMITER + key);
		}
		catch (Exception e) {
			if(debug)Log.e(TAG, "Error deleting " + key);
			if(PRINT_E)Log.e(TAG, "Exception", e);
		}
		return value;
	}

	// FLOAT
	
	/**
	 * Save/modify preference
	 * 
	 * @param key 	The name of the preference to modify.
	 * @param value	The new value for the preference.
	 * @return 		Returns true if preference is saved.
	 */
	public boolean saveFloatPref(String key, float value) {		
		try {
			sp_save.putFloat(KEY_FLOAT + KEY_DELIMITER + key, value);
			sp_save.commit();
			return true;
		} 
		catch (Exception e) {
			if(debug)Log.e(TAG, "Error saving " + key);
			if(PRINT_E)Log.e(TAG, "Exception", e);
		}
		return false;
	}
	/**
	 * Save/modify preference
	 * 
	 * @param key 	The name of the preference to modify.
	 * @param value	The new value for the preference.
	 * @return 		Returns true if preference is saved.
	 */
	public boolean saveFloatArrayPref(String key, float [] value) {		
		float length = value.length;				
		try {
			String stringvalue = "";
			for (int i = 0; i < length; i++) {
				stringvalue += value[i];
				if(i<length-1)stringvalue += value_delimiter;
			}
			savePref(KEY_FLOAT + KEY_DELIMITER + KEY_ARRAY + KEY_DELIMITER + key, stringvalue);
			return true;
		} 
		catch (Exception e) {
			if(debug)Log.e(TAG, "Error saving " + key);
			if(PRINT_E)Log.e(TAG, "Exception", e);
		}
		return false;
	}
	/**
	 * Save/modify preference
	 * 
	 * @param key 	The name of the preference to modify.
	 * @param value	The new value for the preference.
	 * @return 		Returns true if preference is saved.
	 */
	public boolean saveFloatArrayListPref(String key, ArrayList<Float> value) {		
		int length = value.size();				
		try {
			String stringvalue = "";
			for (int i = 0; i < length; i++) {
				stringvalue += value.get(i);
				if(i<length-1)stringvalue += value_delimiter;
			}
			savePref(KEY_FLOAT + KEY_DELIMITER + KEY_ARRAYLIST + KEY_DELIMITER + key, stringvalue);
			return true;
		} 
		catch (Exception e) {
			if(debug)Log.e(TAG, "Error saving " + key);
			if(PRINT_E)Log.e(TAG, "Exception", e);
		}
		return false;
	}
	
	/**
	 * Load preference
	 * 
	 * @param key 	The name of the preference to retrieve.
	 * @param defValue	Value to return if this preference does not exist.
	 * @return 		Returns the preference value if it exists, or defValue.
	 */
	public float loadFloatPref(String key, float defValue) {
		try {
			return sp_load.getFloat(KEY_FLOAT + KEY_DELIMITER + key, defValue);
		} catch (Exception e) {
			if(debug)Log.e(TAG, "Error loading " + key);
			if(PRINT_E)Log.e(TAG, "Exception", e);
		}
		return defValue;
	}
	/**
	 * Load preference
	 * 
	 * @param key 	The name of the preference to retrieve.
	 * @param defValue	Value to return if this preference does not exist.
	 * @return 		Returns the preference value if it exists, or defValue.
	 */
	public float [] loadFloatArrayPref(String key, float [] defValue) {
		try {
			String stringvalue = loadPref(KEY_FLOAT + KEY_DELIMITER + KEY_ARRAY + KEY_DELIMITER + key, "");
			if(stringvalue.length()==0)return defValue;

			String string_array [] = stringvalue.split(value_delimiter, -1);
			int length = string_array.length;

			float [] value = new float [length];
			for (int i = 0; i < length; i++) {
				value[i] = Float.parseFloat(string_array[i]); 
			}

			return value;
		} 
		catch (Exception e) {
			if(debug)Log.e(TAG, "Error loading " + key);
			if(PRINT_E)Log.e(TAG, "Exception", e);
		}	
		return defValue;
	}
	/**
	 * Load preference
	 * 
	 * @param key 	The name of the preference to retrieve.
	 * @param defValue	Value to return if this preference does not exist.
	 * @return 		Returns the preference value if it exists, or defValue.
	 */
	public ArrayList<Float> loadFloatArrayListPref(String key, ArrayList<Float> defValue) {
		try {
			String stringvalue = loadPref(KEY_FLOAT + KEY_DELIMITER + KEY_ARRAYLIST + KEY_DELIMITER + key, "");
			if(stringvalue.length()==0)return defValue;

			String string_array [] = stringvalue.split(value_delimiter, -1);
			int length = string_array.length;

			ArrayList<Float> value = new ArrayList<Float>();
			for (int i = 0; i < length; i++) {
				value.add(Float.parseFloat(string_array[i]));
			}			
			return value;
		} 
		catch (Exception e) {
			if(debug)Log.e(TAG, "Error loading " + key);
			if(PRINT_E)Log.e(TAG, "Exception", e);
		}	
		return defValue;
	}
	
	/**
	 * Delete preference
	 * 
	 * @return Returns true, if preference deleted.
	 * 
	 */
	public boolean delFloatPref(String key) {
		boolean value = false;

		try {
			value = delPref(KEY_FLOAT + KEY_DELIMITER + key);
		}
		catch (Exception e) {
			if(debug)Log.e(TAG, "Error deleting " + key);
			if(PRINT_E)Log.e(TAG, "Exception", e);
		}
		return value;
	}
	/**
	 * Delete preference
	 * 
	 * @return Returns true, if preference deleted.
	 * 
	 */
	public boolean delFloatArrayPref(String key) {
		boolean value = false;

		try {
			value = delPref(KEY_FLOAT + KEY_DELIMITER + KEY_ARRAY + KEY_DELIMITER + key);
		}
		catch (Exception e) {
			if(debug)Log.e(TAG, "Error deleting " + key);
			if(PRINT_E)Log.e(TAG, "Exception", e);
		}
		return value;
	}
	/**
	 * Delete preference
	 * 
	 * @return Returns true, if preference deleted.
	 * 
	 */
	public boolean delFloatArrayListPref(String key) {
		boolean value = false;

		try {
			value = delPref(KEY_FLOAT + KEY_DELIMITER + KEY_ARRAYLIST + KEY_DELIMITER + key);
		}
		catch (Exception e) {
			if(debug)Log.e(TAG, "Error deleting " + key);
			if(PRINT_E)Log.e(TAG, "Exception", e);
		}
		return value;
	}

	// DOUBLE
	
	/**
	 * Save/modify preference
	 * 
	 * @param key 	The name of the preference to modify.
	 * @param value	The new value for the preference.
	 * @return 		Returns true if preference is saved.
	 */
	public boolean saveDoublePref(String key, double value) {
		try {
			savePref(KEY_DOUBLE + KEY_DELIMITER + key, String.valueOf(value));
			return true;
		} 
		catch (Exception e) {
			if(debug)Log.e(TAG, "Error saving " + key);
			if(PRINT_E)Log.e(TAG, "Exception", e);
		}
		return false;
	}
	/**
	 * Save/modify preference
	 * 
	 * @param key 	The name of the preference to modify.
	 * @param value	The new value for the preference.
	 * @return 		Returns true if preference is saved.
	 */
	public boolean saveDoubleArrayPref(String key, double [] value) {		
		double length = value.length;				
		try {
			String stringvalue = "";
			for (int i = 0; i < length; i++) {
				stringvalue += value[i];
				if(i<length-1)stringvalue += value_delimiter;
			}
			savePref(KEY_DOUBLE + KEY_DELIMITER + KEY_ARRAY + KEY_DELIMITER + key, stringvalue);
			return true;
		} 
		catch (Exception e) {
			if(debug)Log.e(TAG, "Error saving " + key);
			if(PRINT_E)Log.e(TAG, "Exception", e);
		}
		return false;
	}
	/**
	 * Save/modify preference
	 * 
	 * @param key 	The name of the preference to modify.
	 * @param value	The new value for the preference.
	 * @return 		Returns true if preference is saved.
	 */
	public boolean saveDoubleArrayListPref(String key, ArrayList<Double> value) {		
		int length = value.size();				
		try {
			String stringvalue = "";
			for (int i = 0; i < length; i++) {
				stringvalue += value.get(i);
				if(i<length-1)stringvalue += value_delimiter;
			}
			savePref(KEY_DOUBLE + KEY_DELIMITER + KEY_ARRAYLIST + KEY_DELIMITER + key, stringvalue);
			return true;
		} 
		catch (Exception e) {
			if(debug)Log.e(TAG, "Error saving " + key);
			if(PRINT_E)Log.e(TAG, "Exception", e);
		}
		return false;
	}
	
	/**
	 * Load preference
	 * 
	 * @param key 	The name of the preference to retrieve.
	 * @param defValue	Value to return if this preference does not exist.
	 * @return 		Returns the preference value if it exists, or defValue.
	 */
	public double loadDoublePref(String key, double defValue) {
		try {
			return Double.parseDouble(loadPref(KEY_DOUBLE + KEY_DELIMITER + key, String.valueOf(defValue)));
		} catch (Exception e) {
			if(debug)Log.e(TAG, "Error loading " + key);
			if(PRINT_E)Log.e(TAG, "Exception", e);
		}
		return defValue;
	}
	/**
	 * Load preference
	 * 
	 * @param key 	The name of the preference to retrieve.
	 * @param defValue	Value to return if this preference does not exist.
	 * @return 		Returns the preference value if it exists, or defValue.
	 */
	public double [] loadDoubleArrayPref(String key, double [] defValue) {
		try {
			String stringvalue = loadPref(KEY_DOUBLE + KEY_DELIMITER + KEY_ARRAY + KEY_DELIMITER + key, "");
			if(stringvalue.length()==0)return defValue;

			String string_array [] = stringvalue.split(value_delimiter, -1);
			int length = string_array.length;

			double [] value = new double [length];
			for (int i = 0; i < length; i++) {
				value[i] = Double.parseDouble(string_array[i]); 
			}

			return value;
		} 
		catch (Exception e) {
			if(debug)Log.e(TAG, "Error saving " + key);
			if(PRINT_E)Log.e(TAG, "Exception", e);
		}	
		return defValue;
	}
	/**
	 * Load preference
	 * 
	 * @param key 	The name of the preference to retrieve.
	 * @param defValue	Value to return if this preference does not exist.
	 * @return 		Returns the preference value if it exists, or defValue.
	 */
	public ArrayList<Double> loadDoubleArrayListPref(String key, ArrayList<Double> defValue) {
		try {
			String stringvalue = loadPref(KEY_DOUBLE + KEY_DELIMITER + KEY_ARRAYLIST + KEY_DELIMITER + key, "");
			if(stringvalue.length()==0)return defValue;

			String string_array [] = stringvalue.split(value_delimiter, -1);
			int length = string_array.length;

			ArrayList<Double> value = new ArrayList<Double>();
			for (int i = 0; i < length; i++) {
				value.add(Double.parseDouble(string_array[i]));
			}			
			return value;
		} 
		catch (Exception e) {
			if(debug)Log.e(TAG, "Error saving " + key);
			if(PRINT_E)Log.e(TAG, "Exception", e);
		}	
		return defValue;
	}
	
	/**
	 * Delete preference
	 * 
	 * @return Returns true, if preference deleted.
	 * 
	 */
	public boolean delDoublePref(String key) {
		boolean value = false;

		try {
			value = delPref(KEY_DOUBLE + KEY_DELIMITER + key);
		}
		catch (Exception e) {
			if(debug)Log.e(TAG, "Error deleting " + key);
			if(PRINT_E)Log.e(TAG, "Exception", e);
		}
		return value;
	}
	/**
	 * Delete preference
	 * 
	 * @return Returns true, if preference deleted.
	 * 
	 */
	public boolean delDoubleArrayPref(String key) {
		boolean value = false;

		try {
			value = delPref(KEY_DOUBLE + KEY_DELIMITER + KEY_ARRAY + KEY_DELIMITER + key);
		}
		catch (Exception e) {
			if(debug)Log.e(TAG, "Error deleting " + key);
			if(PRINT_E)Log.e(TAG, "Exception", e);
		}
		return value;
	}
	/**
	 * Delete preference
	 * 
	 * @return Returns true, if preference deleted.
	 * 
	 */
	public boolean delDoubleArrayListPref(String key) {
		boolean value = false;

		try {
			value = delPref(KEY_DOUBLE + KEY_DELIMITER + KEY_ARRAYLIST + KEY_DELIMITER + key);
		}
		catch (Exception e) {
			if(debug)Log.e(TAG, "Error deleting " + key);
			if(PRINT_E)Log.e(TAG, "Exception", e);
		}
		return value;
	}

	// BIGDECIMAL
	
	/**
	 * Save/modify preference
	 * 
	 * @param key 	The name of the preference to modify.
	 * @param value	The new value for the preference.
	 * @return 		Returns true if preference is saved.
	 */
	public boolean saveBigDecimalPref(String key, BigDecimal value) {
		try {
			sp_save.putFloat(KEY_BIGDECIMAL + KEY_DELIMITER + key, value.floatValue());
			sp_save.commit();
			return true;
		} 
		catch (Exception e) {
			if(debug)Log.e(TAG, "Error saving " + key);
			if(PRINT_E)Log.e(TAG, "Exception", e);
		}
		return false;
	}
	/**
	 * Save/modify preference
	 * 
	 * @param key 	The name of the preference to modify.
	 * @param value	The new value for the preference.
	 * @return 		Returns true if preference is saved.
	 */
	public boolean saveBigDecimalArrayPref(String key, BigDecimal [] value) {		
		int length = value.length;				
		try {
			String stringvalue = "";
			for (int i = 0; i < length; i++) {
				stringvalue += value[i];
				if(i<length-1)stringvalue += value_delimiter;
			}
			savePref(KEY_BIGDECIMAL + KEY_DELIMITER + KEY_ARRAY + KEY_DELIMITER + key, stringvalue);
			return true;
		} 
		catch (Exception e) {
			if(debug)Log.e(TAG, "Error saving " + key);
			if(PRINT_E)Log.e(TAG, "Exception", e);
		}
		return false;
	}
	/**
	 * Save/modify preference
	 * 
	 * @param key 	The name of the preference to modify.
	 * @param value	The new value for the preference.
	 * @return 		Returns true if preference is saved.
	 */
	public boolean saveBigDecimalArrayListPref(String key, ArrayList<BigDecimal> value) {		
		int length = value.size();				
		try {
			String stringvalue = "";
			for (int i = 0; i < length; i++) {
				stringvalue += value.get(i);
				if(i<length-1)stringvalue += value_delimiter;
			}
			savePref(KEY_BIGDECIMAL + KEY_DELIMITER + KEY_ARRAYLIST + KEY_DELIMITER + key, stringvalue);
			return true;
		} 
		catch (Exception e) {
			if(debug)Log.e(TAG, "Error saving " + key);
			if(PRINT_E)Log.e(TAG, "Exception", e);
		}
		return false;
	}

	/**
	 * Load preference
	 * 
	 * @param key 	The name of the preference to retrieve.
	 * @param defValue	Value to return if this preference does not exist.
	 * @return 		Returns the preference value if it exists, or defValue.
	 */
	public BigDecimal loadBigDecimalPref(String key, BigDecimal defValue) {
		try {
			return BigDecimal.valueOf(Double.parseDouble(loadPref(KEY_BIGDECIMAL + KEY_DELIMITER + key, String.valueOf(defValue))));
		} catch (Exception e) {
			if(debug)Log.e(TAG, "Error loading " + key);
			if(PRINT_E)Log.e(TAG, "Exception", e);
		}
		return defValue;
	}
	/**
	 * Load preference
	 * 
	 * @param key 	The name of the preference to retrieve.
	 * @param defValue	Value to return if this preference does not exist.
	 * @return 		Returns the preference value if it exists, or defValue.
	 */
	public BigDecimal [] loadBigDecimalArrayPref(String key, BigDecimal [] defValue) {
		try {
			String stringvalue = loadPref(KEY_BIGDECIMAL + KEY_DELIMITER + KEY_ARRAY + KEY_DELIMITER + key, "");
			if(stringvalue.length()==0)return defValue;

			String string_array [] = stringvalue.split(value_delimiter, -1);
			int length = string_array.length;

			BigDecimal [] value = new BigDecimal [length];
			for (int i = 0; i < length; i++) {
				value[i] = BigDecimal.valueOf(Double.parseDouble(string_array[i])); 
			}

			return value;
		} 
		catch (Exception e) {
			if(debug)Log.e(TAG, "Error loading " + key);
			if(PRINT_E)Log.e(TAG, "Exception", e);
		}	
		return defValue;
	}
	/**
	 * Load preference
	 * 
	 * @param key 	The name of the preference to retrieve.
	 * @param defValue	Value to return if this preference does not exist.
	 * @return 		Returns the preference value if it exists, or defValue.
	 */
	public ArrayList<BigDecimal> loadBigDecimalArrayListPref(String key, ArrayList<BigDecimal> defValue) {
		try {
			String stringvalue = loadPref(KEY_BIGDECIMAL + KEY_DELIMITER + KEY_ARRAYLIST + KEY_DELIMITER + key, "");
			if(stringvalue.length()==0)return defValue;

			String string_array [] = stringvalue.split(value_delimiter, -1);
			int length = string_array.length;

			ArrayList<BigDecimal> value = new ArrayList<BigDecimal>();
			for (int i = 0; i < length; i++) {
				value.add(BigDecimal.valueOf(Double.parseDouble(string_array[i])));
			}			
			return value;
		} 
		catch (Exception e) {
			if(debug)Log.e(TAG, "Error loading " + key);
			if(PRINT_E)Log.e(TAG, "Exception", e);
		}	
		return defValue;
	}
	
	/**
	 * Delete preference
	 * 
	 * @return Returns true, if preference deleted.
	 * 
	 */
	public boolean delBigDecimalPref(String key) {
		boolean value = false;

		try {
			value = delPref(KEY_BIGDECIMAL + KEY_DELIMITER + key);
		}
		catch (Exception e) {
			if(debug)Log.e(TAG, "Error deleting " + key);
			if(PRINT_E)Log.e(TAG, "Exception", e);
		}
		return value;
	}
	/**
	 * Delete preference
	 * 
	 * @return Returns true, if preference deleted.
	 * 
	 */
	public boolean delBigDecimalArrayPref(String key) {
		boolean value = false;

		try {
			value = delPref(KEY_BIGDECIMAL + KEY_DELIMITER + KEY_ARRAY + KEY_DELIMITER + key);
		}
		catch (Exception e) {
			if(debug)Log.e(TAG, "Error deleting " + key);
			if(PRINT_E)Log.e(TAG, "Exception", e);
		}
		return value;
	}
	/**
	 * Delete preference
	 * 
	 * @return Returns true, if preference deleted.
	 * 
	 */
	public boolean delBigDecimalArrayListPref(String key) {
		boolean value = false;

		try {
			value = delPref(KEY_BIGDECIMAL + KEY_DELIMITER + KEY_ARRAYLIST + KEY_DELIMITER + key);
		}
		catch (Exception e) {
			if(debug)Log.e(TAG, "Error deleting " + key);
			if(PRINT_E)Log.e(TAG, "Exception", e);
		}
		return value;
	}

	// CHAR
	
	/**
	 * Save/modify preference
	 * 
	 * @param key 	The name of the preference to modify.
	 * @param value	The new value for the preference.
	 * @return 		Returns true if preference is saved.
	 */
	public boolean saveCharPref(String key, char value) {
		try {
			savePref(KEY_CHARACTER + KEY_DELIMITER + key, String.valueOf(value));
			return true;
		} 
		catch (Exception e) {
			if(debug)Log.e(TAG, "Error saving " + key);
			if(PRINT_E)Log.e(TAG, "Exception", e);
		}
		return false;
	}
	/**
	 * Save/modify preference
	 * 
	 * @param key 	The name of the preference to modify.
	 * @param value	The new value for the preference.
	 * @return 		Returns true if preference is saved.
	 */
	public boolean saveCharArrayPref(String key, char [] value) {		
		int length = value.length;				
		try {
			String stringvalue = "";
			for (int i = 0; i < length; i++) {
				if(value[i]==value_delimiter.charAt(0)) {
					if(debug)Log.e(TAG, "The value delimiter " + value_delimiter + " is not allowed in value " + i + ": " + value[i] + " in key " + key);
					return false;
				}
				stringvalue += value[i];
				if(i<length-1)stringvalue += value_delimiter;
			}
			savePref(KEY_CHARACTER + KEY_DELIMITER + KEY_ARRAY + KEY_DELIMITER + key, stringvalue);
			return true;
		} 
		catch (Exception e) {
			if(debug)Log.e(TAG, "Error saving " + key);
			if(PRINT_E)Log.e(TAG, "Exception", e);
		}
		return false;
	}
	/**
	 * Save/modify preference
	 * 
	 * @param key 	The name of the preference to modify.
	 * @param value	The new value for the preference.
	 * @return 		Returns true if preference is saved.
	 */
	public boolean saveCharArrayListPref(String key, ArrayList<Character> value) {		
		int length = value.size();				
		try {
			String stringvalue = "";
			for (int i = 0; i < length; i++) {
				if(value.get(i)==value_delimiter.charAt(0)) {
					if(debug)Log.e(TAG, "The value delimiter " + value_delimiter + " is not allowed at position " + i + ": " + value.get(i) + " in key " + key);
					return false;
				}
				stringvalue += value.get(i);
				if(i<length-1)stringvalue += value_delimiter;
			}
			savePref(KEY_CHARACTER + KEY_DELIMITER + KEY_ARRAYLIST + KEY_DELIMITER + key, stringvalue);
			return true;
		} 
		catch (Exception e) {
			if(debug)Log.e(TAG, "Error saving " + key);
			if(PRINT_E)Log.e(TAG, "Exception", e);
		}
		return false;
	}
	
	/**
	 * Load preference
	 * 
	 * @param key 	The name of the preference to retrieve.
	 * @param defValue	Value to return if this preference does not exist.
	 * @return 		Returns the preference value if it exists, or defValue.
	 */
	public char loadCharPref(String key, char defValue) {
		try {
			return loadPref(KEY_CHARACTER + KEY_DELIMITER + key, String.valueOf(defValue)).charAt(0);
		} catch (Exception e) {
			if(debug)Log.e(TAG, "Error loading " + key);
			if(PRINT_E)Log.e(TAG, "Exception", e);
		}
		return defValue;
	}
	/**
	 * Load preference
	 * 
	 * @param key 	The name of the preference to retrieve.
	 * @param defValue	Value to return if this preference does not exist.
	 * @return 		Returns the preference value if it exists, or defValue.
	 */
	public char [] loadCharArrayPref(String key, char [] defValue) {
		try {
			String stringvalue = loadPref(KEY_CHARACTER + KEY_DELIMITER + KEY_ARRAY + KEY_DELIMITER + key, "");
			if(stringvalue.length()==0)return defValue;

			String string_array [] = stringvalue.split(value_delimiter, -1);
			int length = string_array.length;

			char [] value = new char [length];
			for (int i = 0; i < length; i++) {
				value[i] = string_array[i].charAt(0); 
			}

			return value;
		} 
		catch (Exception e) {
			if(debug)Log.e(TAG, "Error loading " + key);
			if(PRINT_E)Log.e(TAG, "Exception", e);
		}	
		return defValue;
	}
	/**
	 * Load preference
	 * 
	 * @param key 	The name of the preference to retrieve.
	 * @param defValue	Value to return if this preference does not exist.
	 * @return 		Returns the preference value if it exists, or defValue.
	 */
	public ArrayList<Character> loadCharArrayListPref(String key, ArrayList<Character> defValue) {
		try {
			String stringvalue = loadPref(KEY_CHARACTER + KEY_DELIMITER + KEY_ARRAYLIST + KEY_DELIMITER + key, "");
			if(stringvalue.length()==0)return defValue;

			String string_array [] = stringvalue.split(value_delimiter, -1);
			int length = string_array.length;

			ArrayList<Character> value = new ArrayList<Character>();
			for (int i = 0; i < length; i++) {
				value.add(string_array[i].charAt(0));
			}			
			return value;
		} 
		catch (Exception e) {
			if(debug)Log.e(TAG, "Error loading " + key);
			if(PRINT_E)Log.e(TAG, "Exception", e);
		}	
		return defValue;
	}
	
	/**
	 * Delete preference
	 * 
	 * @return Returns true, if preference deleted.
	 * 
	 */
	public boolean delCharPref(String key) {
		boolean value = false;

		try {
			value = delPref(KEY_CHARACTER + KEY_DELIMITER + key);
		}
		catch (Exception e) {
			if(debug)Log.e(TAG, "Error deleting " + key);
			if(PRINT_E)Log.e(TAG, "Exception", e);
		}
		return value;
	}
	/**
	 * Delete preference
	 * 
	 * @return Returns true, if preference deleted.
	 * 
	 */
	public boolean delCharArrayPref(String key) {
		boolean value = false;

		try {
			value = delPref(KEY_CHARACTER + KEY_DELIMITER + KEY_ARRAY + KEY_DELIMITER + key);
		}
		catch (Exception e) {
			if(debug)Log.e(TAG, "Error deleting " + key);
			if(PRINT_E)Log.e(TAG, "Exception", e);
		}
		return value;
	}
	/**
	 * Delete preference
	 * 
	 * @return Returns true, if preference deleted.
	 * 
	 */
	public boolean delCharArrayListPref(String key) {
		boolean value = false;

		try {
			value = delPref(KEY_CHARACTER + KEY_DELIMITER + KEY_ARRAYLIST + KEY_DELIMITER + key);
		}
		catch (Exception e) {
			if(debug)Log.e(TAG, "Error deleting " + key);
			if(PRINT_E)Log.e(TAG, "Exception", e);
		}
		return value;
	}

	// STRING
	/**
	 * Save/modify preference
	 * 
	 * @param key 	The name of the preference to modify.
	 * @param value	The new value for the preference.
	 * @return 		Returns true if preference is saved.
	 */
	public boolean saveStringPref(String key, String value) {		
		try {
			savePref(KEY_STRING + KEY_DELIMITER + key, value);
			return true;
		} 
		catch (Exception e) {
			if(debug)Log.e(TAG, "Error saving " + key);
			if(PRINT_E)Log.e(TAG, "Exception", e);
		}
		return false;
	}
	/**
	 * Save/modify preference
	 * 
	 * @param key 	The name of the preference to modify.
	 * @param value	The new value for the preference.
	 * @return 		Returns true if preference is saved.
	 */
	public boolean saveStringArrayPref(String key, String [] value) {		
		int length = value.length;				
		try {
			String stringvalue = "";
			for (int i = 0; i < length; i++) {
				if(value[i].contains(value_delimiter)) {
					if(debug)Log.e(TAG, "The value delimiter \"" + value_delimiter + "\" is not allowed at position " + i + ": \"" + value[i] + "\" in key \"" + key + "\"");
					return false;
				}
				stringvalue += value[i];
				if(i<length-1)stringvalue += value_delimiter;
			}
			savePref(KEY_STRING + KEY_DELIMITER + KEY_ARRAY + KEY_DELIMITER + key, stringvalue);
			return true;
		} 
		catch (Exception e) {
			if(debug)Log.e(TAG, "Error saving " + key);
			if(PRINT_E)Log.e(TAG, "Exception", e);
		}
		return false;
	}	
	/**
	 * Save/modify preference
	 * 
	 * @param key 	The name of the preference to modify.
	 * @param value	The new value for the preference.
	 * @return 		Returns true if preference is saved.
	 */
	public boolean saveStringArrayListPref(String key, ArrayList<String> value) {		
		int length = value.size();				
		try {
			String stringvalue = "";
			for (int i = 0; i < length; i++) {
				if(value.get(i).contains(value_delimiter)) {
					if(debug)Log.e(TAG, "The value delimiter " + value_delimiter + " is not allowed at position " + i + ": " + value.get(i) + " in key " + key);
					return false;
				}
				stringvalue += value.get(i);
				if(i<length-1)stringvalue += value_delimiter;
			}
			savePref(KEY_STRING + KEY_DELIMITER + KEY_ARRAYLIST + KEY_DELIMITER + key, stringvalue);
			return true;
		} 
		catch (Exception e) {
			if(debug)Log.e(TAG, "Error saving " + key);
			if(PRINT_E)Log.e(TAG, "Exception", e);
		}
		return false;
	}
	
	/**
	 * Load preference
	 * 
	 * @param key 	The name of the preference to retrieve.
	 * @param defValue	Value to return if this preference does not exist.
	 * @return 		Returns the preference value if it exists, or defValue.
	 */
	public String loadStringPref(String key, String defValue) {
		try {
			return loadPref(KEY_STRING + KEY_DELIMITER + key, defValue);
		} catch (Exception e) {
			if(debug)Log.e(TAG, "Error loading " + key);
			if(PRINT_E)Log.e(TAG, "Exception", e);
		}
		return defValue;
	}
	/**
	 * Load preference
	 * 
	 * @param key 	The name of the preference to retrieve.
	 * @param defValue	Value to return if this preference does not exist.
	 * @return 		Returns the preference value if it exists, or defValue.
	 */
	public String [] loadStringArrayPref(String key, String [] defValue) {
		try {
			String stringvalue = loadPref(KEY_STRING + KEY_DELIMITER + KEY_ARRAY + KEY_DELIMITER + key, "");
			if(stringvalue.length()==0)return defValue;

			return stringvalue.split(value_delimiter, -1);
		} 
		catch (Exception e) {
			if(debug)Log.e(TAG, "Error loading " + key);
			if(PRINT_E)Log.e(TAG, "Exception", e);
		}	
		return defValue;
	}
	/**
	 * Load preference
	 * 
	 * @param key 	The name of the preference to retrieve.
	 * @param defValue	Value to return if this preference does not exist.
	 * @return 		Returns the preference value if it exists, or defValue.
	 */
	public ArrayList<String> loadStringArrayListPref(String key, ArrayList<String> defValue) {
		try {
			String stringvalue = loadPref(KEY_STRING + KEY_DELIMITER + KEY_ARRAYLIST + KEY_DELIMITER + key, "");
			if(stringvalue.length()==0)return defValue;

			String string_array [] = stringvalue.split(value_delimiter, -1);
			int length = string_array.length;

			ArrayList<String> value = new ArrayList<String>();
			for (int i = 0; i < length; i++) {
				value.add(string_array[i]);
			}			
			return value;
		} 
		catch (Exception e) {
			if(debug)Log.e(TAG, "Error loading " + key);
			if(PRINT_E)Log.e(TAG, "Exception", e);
		}	
		return defValue;
	}
	
	/**
	 * Delete preference
	 * 
	 * @return Returns true, if preference deleted.
	 * 
	 */
	public boolean delStringPref(String key) {
		boolean value = false;

		try {
			value = delPref(KEY_STRING + KEY_DELIMITER + key);
		}
		catch (Exception e) {
			if(debug)Log.e(TAG, "Error deleting " + key);
			if(PRINT_E)Log.e(TAG, "Exception", e);
		}
		return value;
	}
	/**
	 * Delete preference
	 * 
	 * @return Returns true, if preference deleted.
	 * 
	 */
	public boolean delStringArrayPref(String key) {
		boolean value = false;

		try {
			value = delPref(KEY_STRING + KEY_DELIMITER + KEY_ARRAY + KEY_DELIMITER + key);
		}
		catch (Exception e) {
			if(debug)Log.e(TAG, "Error deleting " + key);
			if(PRINT_E)Log.e(TAG, "Exception", e);
		}
		return value;
	}
	/**
	 * Delete preference
	 * 
	 * @return Returns true, if preference deleted.
	 * 
	 */
	public boolean delStringArrayListPref(String key) {
		boolean value = false;

		try {
			value = delPref(KEY_STRING + KEY_DELIMITER + KEY_ARRAYLIST + KEY_DELIMITER + key);
		}
		catch (Exception e) {
			if(debug)Log.e(TAG, "Error deleting " + key);
			if(PRINT_E)Log.e(TAG, "Exception", e);
		}
		return value;
	}

	// DATE
	
	/**
	 * Save/modify preference
	 * 
	 * @param key 	The name of the preference to modify.
	 * @param value	The new value for the preference.
	 * @return 		Returns true if preference is saved.
	 */
	public boolean saveDatePref(String key, Date value) {
		try {
			sp_save.putLong(KEY_DATE + KEY_DELIMITER + key, value.getTime());
			sp_save.commit();
			return true;
		} 
		catch (Exception e) {
			if(debug)Log.e(TAG, "Error saving " + key);
			if(PRINT_E)Log.e(TAG, "Exception", e);
		}
		return false;
	}
	/**
	 * Save/modify preference
	 * 
	 * @param key 	The name of the preference to modify.
	 * @param value	The new value for the preference.
	 * @return 		Returns true if preference is saved.
	 */
	public boolean saveDateArrayPref(String key, Date [] value) {		
		int length = value.length;				
		try {
			String stringvalue = "";
			for (int i = 0; i < length; i++) {
				stringvalue += value[i].getTime();
				if(i<length-1)stringvalue += value_delimiter;
			}
			savePref(KEY_DATE + KEY_DELIMITER + KEY_ARRAY + KEY_DELIMITER + key, stringvalue);
			return true;
		} 
		catch (Exception e) {
			if(debug)Log.e(TAG, "Error saving " + key);
			if(PRINT_E)Log.e(TAG, "Exception", e);
		}
		return false;
	}
	/**
	 * Save/modify preference
	 * 
	 * @param key 	The name of the preference to modify.
	 * @param value	The new value for the preference.
	 * @return 		Returns true if preference is saved.
	 */
	public boolean saveDateArrayListPref(String key, ArrayList<Date> value) {		
		int length = value.size();				
		try {
			String stringvalue = "";
			for (int i = 0; i < length; i++) {
				stringvalue += value.get(i).getTime();
				if(i<length-1)stringvalue += value_delimiter;
			}
			savePref(KEY_DATE + KEY_DELIMITER + KEY_ARRAYLIST + KEY_DELIMITER + key, stringvalue);
			return true;
		} 
		catch (Exception e) {
			if(debug)Log.e(TAG, "Error saving " + key);
			if(PRINT_E)Log.e(TAG, "Exception", e);
		}
		return false;
	}
	
	/**
	 * Load preference
	 * 
	 * @param key 	The name of the preference to retrieve.
	 * @param defValue	Value to return if this preference does not exist.
	 * @return 		Returns the preference value if it exists, or defValue.
	 */
	public Date loadDatePref(String key, Date defValue) {
		try {
			return new Date((sp_load.getLong(KEY_DATE + KEY_DELIMITER + key, defValue.getTime())));
		} catch (Exception e) {
			if(debug)Log.e(TAG, "Error loading " + key);
			if(PRINT_E)Log.e(TAG, "Exception", e);
		}
		return defValue;

	}
	/**
	 * Load preference
	 * 
	 * @param key 	The name of the preference to retrieve.
	 * @param defValue	Value to return if this preference does not exist.
	 * @return 		Returns the preference value if it exists, or defValue.
	 */
	public Date [] loadDateArrayPref(String key, Date [] defValue) {
		try {
			String stringvalue = loadPref(KEY_DATE + KEY_DELIMITER + KEY_ARRAY + KEY_DELIMITER + key, "");
			if(stringvalue.length()==0)return defValue;

			String string_array [] = stringvalue.split(value_delimiter, -1);
			int length = string_array.length;

			Date [] value = new Date [length];
			for (int i = 0; i < length; i++) {
				value[i] = new Date(Long.parseLong(string_array[i]));
			}

			return value;
		} 
		catch (Exception e) {
			if(debug)Log.e(TAG, "Error loading " + key);
			if(PRINT_E)Log.e(TAG, "Exception", e);
		}	
		return defValue;
	}
	/**
	 * Load preference
	 * 
	 * @param key 	The name of the preference to retrieve.
	 * @param defValue	Value to return if this preference does not exist.
	 * @return 		Returns the preference value if it exists, or defValue.
	 */
	public ArrayList<Date> loadDateArrayListPref(String key, ArrayList<Date> defValue) {
		try {
			String stringvalue = loadPref(KEY_DATE + KEY_DELIMITER + KEY_ARRAYLIST + KEY_DELIMITER + key, "");
			if(stringvalue.length()==0)return defValue;

			String string_array [] = stringvalue.split(value_delimiter, -1);
			int length = string_array.length;

			ArrayList<Date> value = new ArrayList<Date>();
			for (int i = 0; i < length; i++) {
				value.add(new Date(Long.parseLong(string_array[i])));
			}			

			return value;
		} 
		catch (Exception e) {
			if(debug)Log.e(TAG, "Error loading " + key);
			if(PRINT_E)Log.e(TAG, "Exception", e);
		}	
		return defValue;
	}
	
	/**
	 * Delete preference
	 * 
	 * @return Returns true, if preference deleted.
	 * 
	 */
	public boolean delDatePref(String key) {
		boolean value = false;

		try {
			value = delPref(KEY_DATE + KEY_DELIMITER + key);
		}
		catch (Exception e) {
			if(debug)Log.e(TAG, "Error deleting " + key);
			if(PRINT_E)Log.e(TAG, "Exception", e);
		}
		return value;
	}
	/**
	 * Delete preference
	 * 
	 * @return Returns true, if preference deleted.
	 * 
	 */
	public boolean delDateArrayPref(String key) {
		boolean value = false;

		try {
			value = delPref(KEY_DATE + KEY_DELIMITER + KEY_ARRAY + KEY_DELIMITER + key);
		}
		catch (Exception e) {
			if(debug)Log.e(TAG, "Error deleting " + key);
			if(PRINT_E)Log.e(TAG, "Exception", e);
		}
		return value;
	}
	/**
	 * Delete preference
	 * 
	 * @return Returns true, if preference deleted.
	 * 
	 */
	public boolean delDateArrayListPref(String key) {
		boolean value = false;

		try {
			value = delPref(KEY_DATE + KEY_DELIMITER + KEY_ARRAYLIST + KEY_DELIMITER + key);
		}
		catch (Exception e) {
			if(debug)Log.e(TAG, "Error deleting " + key);
			if(PRINT_E)Log.e(TAG, "Exception", e);
		}
		return value;
	}

	// GREGORIANCALENDAR
	
	/**
	 * Save/modify preference
	 * 
	 * @param key 	The name of the preference to modify.
	 * @param value	The new value for the preference.
	 * @return 		Returns true if preference is saved.
	 */
	public boolean saveGregorianCalendarPref(String key, GregorianCalendar value) {
		try {
			sp_save.putLong(KEY_GREGORIANCALENDAR + KEY_DELIMITER + key, value.getTimeInMillis());
			sp_save.commit();
			return true;
		} 
		catch (Exception e) {
			if(debug)Log.e(TAG, "Error saving " + key);
			if(PRINT_E)Log.e(TAG, "Exception", e);
		}
		return false;
	}
	/**
	 * Save/modify preference
	 * 
	 * @param key 	The name of the preference to modify.
	 * @param value	The new value for the preference.
	 * @return 		Returns true if preference is saved.
	 */
	public boolean saveGregorianCalendarArrayPref(String key, GregorianCalendar [] value) {		
		int length = value.length;				
		try {
			String stringvalue = "";
			for (int i = 0; i < length; i++) {
				stringvalue += value[i].getTime();
				if(i<length-1)stringvalue += value_delimiter;
			}
			savePref(KEY_GREGORIANCALENDAR + KEY_DELIMITER + KEY_ARRAY + KEY_DELIMITER + key, stringvalue);
			return true;
		} 
		catch (Exception e) {
			if(debug)Log.e(TAG, "Error saving " + key);
			if(PRINT_E)Log.e(TAG, "Exception", e);
		}
		return false;
	}
	/**
	 * Save/modify preference
	 * 
	 * @param key 	The name of the preference to modify.
	 * @param value	The new value for the preference.
	 * @return 		Returns true if preference is saved.
	 */
	public boolean saveGregorianCalendarArrayListPref(String key, ArrayList<GregorianCalendar> value) {		
		int length = value.size();				
		try {
			String stringvalue = "";
			for (int i = 0; i < length; i++) {
				stringvalue += value.get(i).getTime();
				if(i<length-1)stringvalue += value_delimiter;
			}
			savePref(KEY_GREGORIANCALENDAR + KEY_DELIMITER + KEY_ARRAYLIST + KEY_DELIMITER + key, stringvalue);
			return true;
		} 
		catch (Exception e) {
			if(debug)Log.e(TAG, "Error saving " + key);
			if(PRINT_E)Log.e(TAG, "Exception", e);
		}
		return false;
	}
	
	/**
	 * Load preference
	 * 
	 * @param key 	The name of the preference to retrieve.
	 * @param defValue	Value to return if this preference does not exist.
	 * @return 		Returns the preference value if it exists, or defValue.
	 */
	public GregorianCalendar loadGregorianCalendarPref(String key, GregorianCalendar defValue) {
		try {
			GregorianCalendar value = new GregorianCalendar();
			value.setTimeInMillis((sp_load.getLong(KEY_GREGORIANCALENDAR + KEY_DELIMITER + key, defValue.getTimeInMillis())));
			return value;
		} catch (Exception e) {
			if(debug)Log.e(TAG, "Error loading " + key);
			if(PRINT_E)Log.e(TAG, "Exception", e);
		}
		return defValue;

	}
	/**
	 * Load preference
	 * 
	 * @param key 	The name of the preference to retrieve.
	 * @param defValue	Value to return if this preference does not exist.
	 * @return 		Returns the preference value if it exists, or defValue.
	 */
	public GregorianCalendar [] loadGregorianCalendarArrayPref(String key, GregorianCalendar [] defValue) {
		try {
			String stringvalue = loadPref(KEY_GREGORIANCALENDAR + KEY_DELIMITER + KEY_ARRAY + KEY_DELIMITER + key, "");
			if(stringvalue.length()==0)return defValue;

			String string_array [] = stringvalue.split(value_delimiter, -1);
			int length = string_array.length;

			GregorianCalendar [] value = new GregorianCalendar [length];
			for (int i = 0; i < length; i++) {
				GregorianCalendar v = new GregorianCalendar();
				v.setTimeInMillis(Long.parseLong(string_array[i]));
				value[i] = v;
			}

			return value;
		} 
		catch (Exception e) {
			if(debug)Log.e(TAG, "Error loading " + key);
			if(PRINT_E)Log.e(TAG, "Exception", e);
		}	
		return defValue;
	}
	/**
	 * Load preference
	 * 
	 * @param key 	The name of the preference to retrieve.
	 * @param defValue	Value to return if this preference does not exist.
	 * @return 		Returns the preference value if it exists, or defValue.
	 */
	public ArrayList<GregorianCalendar> loadGregorianCalendarArrayListPref(String key, ArrayList<GregorianCalendar> defValue) {
		try {
			String stringvalue = loadPref(KEY_GREGORIANCALENDAR + KEY_DELIMITER + KEY_ARRAYLIST + KEY_DELIMITER + key, "");
			if(stringvalue.length()==0)return defValue;

			String string_array [] = stringvalue.split(value_delimiter, -1);
			int length = string_array.length;

			ArrayList<GregorianCalendar> value = new ArrayList<GregorianCalendar>();
			for (int i = 0; i < length; i++) {
				GregorianCalendar v = new GregorianCalendar();
				v.setTimeInMillis(Long.parseLong(string_array[i]));
				value.add(v);
			}			

			return value;
		} 
		catch (Exception e) {
			if(debug)Log.e(TAG, "Error loading " + key);
			if(PRINT_E)Log.e(TAG, "Exception", e);
		}	
		return defValue;
	}
	
	/**
	 * Delete preference
	 * 
	 * @return Returns true, if preference deleted.
	 * 
	 */
	public boolean delGregorianCalendarPref(String key) {
		boolean value = false;

		try {
			value = delPref(KEY_GREGORIANCALENDAR + KEY_DELIMITER + key);
			return true;
		}
		catch (Exception e) {
			if(debug)Log.e(TAG, "Error deleting " + key);
			if(PRINT_E)Log.e(TAG, "Exception", e);
		}
		return value;
	}
	/**
	 * Delete preference
	 * 
	 * @return Returns true, if preference deleted.
	 * 
	 */
	public boolean delGregorianCalendarArrayPref(String key) {
		boolean value = false;

		try {
			value = delPref(KEY_GREGORIANCALENDAR + KEY_DELIMITER + KEY_ARRAY + KEY_DELIMITER + key);
		}
		catch (Exception e) {
			if(debug)Log.e(TAG, "Error deleting " + key);
			if(PRINT_E)Log.e(TAG, "Exception", e);
		}

		return value;
	}
	/**
	 * Delete preference
	 * 
	 * @return Returns true, if preference deleted.
	 * 
	 */
	public boolean delGregorianCalendarArrayListPref(String key) {
		boolean value = false;

		try {
			value = delPref(KEY_GREGORIANCALENDAR + KEY_DELIMITER + KEY_ARRAYLIST + KEY_DELIMITER + key);
		}
		catch (Exception e) {
			if(debug)Log.e(TAG, "Error deleting " + key);
			if(PRINT_E)Log.e(TAG, "Exception", e);
		}
		return value;
	}

	// LOCATION
	
	/**
	 * Save/modify preference
	 * 
	 * @param key 	The name of the preference to modify.
	 * @param value	The new value for the preference.
	 * @return 		Returns true if preference is saved.
	 */
	public boolean saveLocationPref(String key, Location value) {
		try {

			String provider = null;
			double latitude = 0;
			double longitude = 0;
			long timestamp = 0;

			float accuracy = 0.0f;
			float speed = 0.0f;
			float bearing = 0.0f;
			double altitude = 0.0d;

			try {
				provider = value.getProvider();
				latitude = value.getLatitude();
				longitude = value.getLongitude();
				timestamp = value.getTime();
				accuracy = value.getAccuracy();
				speed = value.getSpeed();
				bearing = value.getBearing();
				altitude = value.getAltitude();

			} catch (Exception e) {}

			savePref(KEY_LOCATION_PROVIDER + KEY_DELIMITER + key, provider);

			savePref(KEY_LOCATION_LATITUDE + KEY_DELIMITER + key, String.valueOf(latitude));
			savePref(KEY_LOCATION_LONGITUDE + KEY_DELIMITER + key, String.valueOf(longitude));
			sp_save.putLong(KEY_LOCATION_TIMESTAMP + KEY_DELIMITER + key, timestamp);

			sp_save.putFloat(KEY_LOCATION_ACCURACY + KEY_DELIMITER + key, accuracy);
			sp_save.putFloat(KEY_LOCATION_SPEED + KEY_DELIMITER + key, speed);
			sp_save.putFloat(KEY_LOCATION_BEARING + KEY_DELIMITER + key, bearing);
			savePref(KEY_LOCATION_ALTITUDE + KEY_DELIMITER + key, String.valueOf(altitude));

			sp_save.commit();
			return true;
		} 
		catch (Exception e) {
			if(debug)Log.e(TAG, "Error saving " + key);
			if(PRINT_E)Log.e(TAG, "Exception", e);
		}
		return false;
	}
	/**
	 * Save/modify preference
	 * 
	 * @param key 	The name of the preference to modify.
	 * @param value	The new value for the preference.
	 * @return 		Returns true if preference is saved.
	 */
	public boolean saveLocationArrayPref(String key, Location [] value) {		
		int length = value.length;				
		try {		
			String stringvalue = "";			

			String provider = null;
			double latitude = 0;
			double longitude = 0;
			long timestamp = 0;

			float accuracy = 0.0f;
			float speed = 0.0f;
			float bearing = 0.0f;
			double altitude = 0.0d;

			for (int i = 0; i < length; i++) {
				try {
					provider = value[i].getProvider();
					if(provider.contains(value_delimiter)) {
						if(debug)Log.e(TAG, "The value delimiter \"" + value_delimiter + "\" is not allowed at position " + i + ": \"" + value[i] + "\" in key \"" + key + "\"");
						return false;
					}
				} catch (Exception e) {}
				stringvalue += provider;
				provider = null;
				if(i<length-1)stringvalue += value_delimiter;
			}
			savePref(KEY_LOCATION_PROVIDER + KEY_DELIMITER + KEY_ARRAY + KEY_DELIMITER + key, stringvalue);
			stringvalue = "";

			for (int i = 0; i < length; i++) {
				try {
					latitude = value[i].getLatitude();
				} catch (Exception e) {}
				stringvalue += latitude;
				latitude = 0;
				if(i<length-1)stringvalue += value_delimiter;
			}
			savePref(KEY_LOCATION_LATITUDE + KEY_DELIMITER + KEY_ARRAY + KEY_DELIMITER + key, stringvalue);
			stringvalue = "";

			for (int i = 0; i < length; i++) {
				try {
					longitude = value[i].getLongitude();
				} catch (Exception e) {}
				stringvalue += longitude;
				longitude = 0;
				if(i<length-1)stringvalue += value_delimiter;
			}
			savePref(KEY_LOCATION_LONGITUDE + KEY_DELIMITER + KEY_ARRAY + KEY_DELIMITER + key, stringvalue);
			stringvalue = "";

			for (int i = 0; i < length; i++) {
				try {
					timestamp = value[i].getTime();
				} catch (Exception e) {}
				stringvalue += timestamp;
				timestamp = 0;
				if(i<length-1)stringvalue += value_delimiter;
			}
			savePref(KEY_LOCATION_TIMESTAMP + KEY_DELIMITER + KEY_ARRAY + KEY_DELIMITER + key, stringvalue);
			stringvalue = "";

			for (int i = 0; i < length; i++) {
				try {
					accuracy = value[i].getAccuracy();
				} catch (Exception e) {}
				stringvalue += accuracy;
				accuracy = 0;
				if(i<length-1)stringvalue += value_delimiter;
			}
			savePref(KEY_LOCATION_ACCURACY + KEY_DELIMITER + KEY_ARRAY + KEY_DELIMITER + key, stringvalue);
			stringvalue = "";

			for (int i = 0; i < length; i++) {
				try {
					speed = value[i].getSpeed();
				} catch (Exception e) {}
				stringvalue += speed;
				speed = 0;
				if(i<length-1)stringvalue += value_delimiter;
			}
			savePref(KEY_LOCATION_SPEED + KEY_DELIMITER + KEY_ARRAY + KEY_DELIMITER + key, stringvalue);
			stringvalue = "";

			for (int i = 0; i < length; i++) {
				try {
					bearing = value[i].getBearing();
				} catch (Exception e) {}
				stringvalue += bearing;
				bearing = 0;
				if(i<length-1)stringvalue += value_delimiter;
			}
			savePref(KEY_LOCATION_BEARING + KEY_DELIMITER + KEY_ARRAY + KEY_DELIMITER + key, stringvalue);
			stringvalue = "";

			for (int i = 0; i < length; i++) {
				try {
					altitude = value[i].getAltitude();
				} catch (Exception e) {}
				stringvalue += altitude;
				altitude = 0;
				if(i<length-1)stringvalue += value_delimiter;
			}

			savePref(KEY_LOCATION_ALTITUDE + KEY_DELIMITER + KEY_ARRAY + KEY_DELIMITER + key, stringvalue);
			return true;
		} 
		catch (Exception e) {
			if(debug)Log.e(TAG, "Error saving " + key);
			if(PRINT_E)Log.e(TAG, "Exception", e);
		}
		return false;
	}
	/**
	 * Save/modify preference
	 * 
	 * @param key 	The name of the preference to modify.
	 * @param value	The new value for the preference.
	 * @return 		Returns true if preference is saved.
	 */
	public boolean saveLocationArrayListPref(String key, ArrayList<Location> value) {		
		int length = value.size();				
		try {		
			String stringvalue = "";			

			String provider = null;
			double latitude = 0;
			double longitude = 0;
			long timestamp = 0;

			float accuracy = 0.0f;
			float speed = 0.0f;
			float bearing = 0.0f;
			double altitude = 0.0d;

			for (int i = 0; i < length; i++) {
				try {
					provider = value.get(i).getProvider();
					if(provider.contains(value_delimiter)) {
						if(debug)Log.e(TAG, "The value delimiter \"" + value_delimiter + "\" is not allowed at position " + i + ": \"" + value.get(i) + "\" in key \"" + key + "\"");
						return false;
					}
				} catch (Exception e) {}

				stringvalue += provider;
				provider = "";
				if(i<length-1)stringvalue += value_delimiter;
			}
			savePref(KEY_LOCATION_PROVIDER + KEY_DELIMITER + KEY_ARRAYLIST + KEY_DELIMITER + key, stringvalue);
			stringvalue = null;

			for (int i = 0; i < length; i++) {
				try {
					latitude = value.get(i).getLatitude();
				} catch (Exception e) {}
				stringvalue += latitude;
				latitude = 0;
				if(i<length-1)stringvalue += value_delimiter;
			}
			savePref(KEY_LOCATION_LATITUDE + KEY_DELIMITER + KEY_ARRAYLIST + KEY_DELIMITER + key, stringvalue);
			stringvalue = "";

			for (int i = 0; i < length; i++) {
				try {
					longitude = value.get(i).getLongitude();
				} catch (Exception e) {}
				stringvalue += longitude;
				longitude = 0;
				if(i<length-1)stringvalue += value_delimiter;
			}
			savePref(KEY_LOCATION_LONGITUDE + KEY_DELIMITER + KEY_ARRAYLIST + KEY_DELIMITER + key, stringvalue);
			stringvalue = "";

			for (int i = 0; i < length; i++) {
				try {
					timestamp = value.get(i).getTime();
				} catch (Exception e) {}
				stringvalue += timestamp;
				timestamp = 0;
				if(i<length-1)stringvalue += value_delimiter;
			}
			savePref(KEY_LOCATION_TIMESTAMP + KEY_DELIMITER + KEY_ARRAYLIST + KEY_DELIMITER + key, stringvalue);
			stringvalue = "";

			for (int i = 0; i < length; i++) {
				try {
					accuracy = value.get(i).getAccuracy();
				} catch (Exception e) {}
				stringvalue += accuracy;
				accuracy = 0;
				if(i<length-1)stringvalue += value_delimiter;
			}
			savePref(KEY_LOCATION_ACCURACY + KEY_DELIMITER + KEY_ARRAYLIST + KEY_DELIMITER + key, stringvalue);
			stringvalue = "";

			for (int i = 0; i < length; i++) {
				try {
					speed = value.get(i).getSpeed();
				} catch (Exception e) {}
				stringvalue += speed;
				speed = 0;
				if(i<length-1)stringvalue += value_delimiter;
			}
			savePref(KEY_LOCATION_SPEED + KEY_DELIMITER + KEY_ARRAYLIST + KEY_DELIMITER + key, stringvalue);
			stringvalue = "";

			for (int i = 0; i < length; i++) {
				try {
					bearing = value.get(i).getBearing();
				} catch (Exception e) {}
				stringvalue += bearing;
				bearing = 0;
				if(i<length-1)stringvalue += value_delimiter;
			}
			savePref(KEY_LOCATION_BEARING + KEY_DELIMITER + KEY_ARRAYLIST + KEY_DELIMITER + key, stringvalue);
			stringvalue = "";

			for (int i = 0; i < length; i++) {
				try {
					altitude = value.get(i).getAltitude();
				} catch (Exception e) {}
				stringvalue += altitude;
				altitude = 0;
				if(i<length-1)stringvalue += value_delimiter;
			}
			savePref(KEY_LOCATION_ALTITUDE + KEY_DELIMITER + KEY_ARRAYLIST + KEY_DELIMITER + key, stringvalue);
			return true;
		} 
		catch (Exception e) {
			if(debug)Log.e(TAG, "Error saving " + key);
			if(PRINT_E)Log.e(TAG, "Exception", e);
		}
		return false;
	}
	
	/**
	 * Load preference
	 * 
	 * @param key 	The name of the preference to retrieve.
	 * @param defValue	Value to return if this preference does not exist.
	 * @return 		Returns the preference value if it exists, or defValue.
	 */
	public Location loadLocationPref(String key, Location defValue) {
		try {

			String provider = null;
			double latitude = 0;
			double longitude = 0;
			long timestamp = 0;

			float accuracy = 0.0f;
			float speed = 0.0f;
			float bearing = 0.0f;
			double altitude = 0.0d;

			try {
				provider = defValue.getProvider();
				latitude = defValue.getLatitude();
				longitude = defValue.getLongitude();
				timestamp = defValue.getTime();
				accuracy = defValue.getAccuracy();
				speed = defValue.getSpeed();
				bearing = defValue.getBearing();
				altitude = defValue.getAltitude();
			} catch (Exception e) {}

			Location value = new Location(loadPref(KEY_LOCATION_PROVIDER + KEY_DELIMITER + key, provider));
			if(latitude>0)value.setLatitude(latitude);
			if(longitude>0)value.setLongitude(longitude);
			if(timestamp>0)value.setTime(timestamp);
			if(accuracy>0)value.setAccuracy(accuracy);
			if(speed>0)value.setSpeed(speed);
			if(bearing>0)value.setBearing(bearing);
			if(altitude>0)value.setAltitude(altitude);

			return value;
		} catch (Exception e) {
			if(debug)Log.e(TAG, "Error loading " + key);
			if(PRINT_E)Log.e(TAG, "Exception", e);
		}
		return defValue;

	}
	/**
	 * Load preference
	 * 
	 * @param key 	The name of the preference to retrieve.
	 * @param defValue	Value to return if this preference does not exist.
	 * @return 		Returns the preference value if it exists, or defValue.
	 */
	public Location [] loadLocationArrayPref(String key, Location [] defValue) {
		try {
			String stringvalue = loadPref(KEY_LOCATION_PROVIDER + KEY_DELIMITER + KEY_ARRAY + KEY_DELIMITER + key, "");
			if(stringvalue.length()==0)return defValue;

			String location_provider_array [] = stringvalue.split(value_delimiter, -1);
			int length = location_provider_array.length;

			stringvalue = loadPref(KEY_LOCATION_LATITUDE + KEY_DELIMITER + KEY_ARRAY + KEY_DELIMITER + key, "");
			String location_latitude_array [] = stringvalue.split(value_delimiter, -1);

			stringvalue = loadPref(KEY_LOCATION_LONGITUDE + KEY_DELIMITER + KEY_ARRAY + KEY_DELIMITER + key, "");
			String location_longitude_array [] = stringvalue.split(value_delimiter, -1);

			stringvalue = loadPref(KEY_LOCATION_TIMESTAMP + KEY_DELIMITER + KEY_ARRAY + KEY_DELIMITER + key, "");
			String location_timestamp_array [] = stringvalue.split(value_delimiter, -1);

			stringvalue = loadPref(KEY_LOCATION_ACCURACY + KEY_DELIMITER + KEY_ARRAY + KEY_DELIMITER + key, "");
			String location_accuracy_array [] = stringvalue.split(value_delimiter, -1);

			stringvalue = loadPref(KEY_LOCATION_SPEED + KEY_DELIMITER + KEY_ARRAY + KEY_DELIMITER + key, "");
			String location_speed_array [] = stringvalue.split(value_delimiter, -1);

			stringvalue = loadPref(KEY_LOCATION_BEARING + KEY_DELIMITER + KEY_ARRAY + KEY_DELIMITER + key, "");
			String location_bearing_array [] = stringvalue.split(value_delimiter, -1);

			stringvalue = loadPref(KEY_LOCATION_ALTITUDE + KEY_DELIMITER + KEY_ARRAY + KEY_DELIMITER + key, "");
			String location_altitude_array [] = stringvalue.split(value_delimiter, -1);

			Location [] value = new Location [length];

			for (int i = 0; i < length; i++) {
				Location value_entry = new Location(location_provider_array[i]);

				double latitude = Double.parseDouble(location_latitude_array[i]);
				if(latitude>0)value_entry.setLatitude(latitude);

				double longitude = Double.parseDouble(location_longitude_array[i]);
				if(longitude>0)value_entry.setLongitude(longitude);

				long timestamp = Long.parseLong(location_timestamp_array[i]);
				if(timestamp>0)value_entry.setTime(timestamp);

				float accuracy = Float.parseFloat(location_accuracy_array[i]);
				if(accuracy>0)value_entry.setAccuracy(accuracy);

				float speed = Float.parseFloat(location_speed_array[i]);
				if(speed>0)value_entry.setSpeed(speed);

				float bearing = Float.parseFloat(location_bearing_array[i]);
				if(bearing>0)value_entry.setBearing(bearing);

				double altitude = Double.parseDouble(location_altitude_array[i]);
				if(altitude>0)value_entry.setAltitude(altitude);

				value[i] = value_entry;
			}

			return value;
		} 
		catch (Exception e) {
			if(debug)Log.e(TAG, "Error loading " + key);
			if(PRINT_E)Log.e(TAG, "Exception", e);
		}	
		return defValue;
	}
	/**
	 * Load preference
	 * 
	 * @param key 	The name of the preference to retrieve.
	 * @param defValue	Value to return if this preference does not exist.
	 * @return 		Returns the preference value if it exists, or defValue.
	 */
	public ArrayList<Location> loadLocationArrayPref(String key, ArrayList<Location> defValue) {
		try {
			String stringvalue = loadPref(KEY_LOCATION_PROVIDER + KEY_DELIMITER + KEY_ARRAYLIST + KEY_DELIMITER + key, "");
			if(stringvalue.length()==0)return defValue;

			String location_provider_array [] = stringvalue.split(value_delimiter, -1);
			int length = location_provider_array.length;

			stringvalue = loadPref(KEY_LOCATION_LATITUDE + KEY_DELIMITER + KEY_ARRAYLIST + KEY_DELIMITER + key, "");
			String location_latitude_array [] = stringvalue.split(value_delimiter, -1);

			stringvalue = loadPref(KEY_LOCATION_LONGITUDE + KEY_DELIMITER + KEY_ARRAYLIST + KEY_DELIMITER + key, "");
			String location_longitude_array [] = stringvalue.split(value_delimiter, -1);

			stringvalue = loadPref(KEY_LOCATION_TIMESTAMP + KEY_DELIMITER + KEY_ARRAYLIST + KEY_DELIMITER + key, "");
			String location_timestamp_array [] = stringvalue.split(value_delimiter, -1);

			stringvalue = loadPref(KEY_LOCATION_ACCURACY + KEY_DELIMITER + KEY_ARRAYLIST + KEY_DELIMITER + key, "");
			String location_accuracy_array [] = stringvalue.split(value_delimiter, -1);

			stringvalue = loadPref(KEY_LOCATION_SPEED + KEY_DELIMITER + KEY_ARRAYLIST + KEY_DELIMITER + key, "");
			String location_speed_array [] = stringvalue.split(value_delimiter, -1);

			stringvalue = loadPref(KEY_LOCATION_BEARING + KEY_DELIMITER + KEY_ARRAYLIST + KEY_DELIMITER + key, "");
			String location_bearing_array [] = stringvalue.split(value_delimiter, -1);

			stringvalue = loadPref(KEY_LOCATION_ALTITUDE + KEY_DELIMITER + KEY_ARRAYLIST + KEY_DELIMITER + key, "");
			String location_altitude_array [] = stringvalue.split(value_delimiter, -1);

			ArrayList<Location> value = new ArrayList<Location>();

			for (int i = 0; i < length; i++) {
				Location value_entry = new Location(location_provider_array[i]);

				double latitude = Double.parseDouble(location_latitude_array[i]);
				if(latitude>0)value_entry.setLatitude(latitude);

				double longitude = Double.parseDouble(location_longitude_array[i]);
				if(longitude>0)value_entry.setLongitude(longitude);

				long timestamp = Long.parseLong(location_timestamp_array[i]);
				if(timestamp>0)value_entry.setTime(timestamp);

				float accuracy = Float.parseFloat(location_accuracy_array[i]);
				if(accuracy>0)value_entry.setAccuracy(accuracy);

				float speed = Float.parseFloat(location_speed_array[i]);
				if(speed>0)value_entry.setSpeed(speed);

				float bearing = Float.parseFloat(location_bearing_array[i]);
				if(bearing>0)value_entry.setBearing(bearing);

				double altitude = Double.parseDouble(location_altitude_array[i]);
				if(altitude>0)value_entry.setAltitude(altitude);

				value.add(value_entry);
			}

			return value;
		} 
		catch (Exception e) {
			if(debug)Log.e(TAG, "Error loading " + key);
			if(PRINT_E)Log.e(TAG, "Exception", e);
		}	
		return defValue;
	}
	
	/**
	 * Delete preference
	 * 
	 * @return Returns true, if preference deleted.
	 * 
	 */
	public boolean delLocationPref(String key) {
		boolean value = false;

		try {
			value = delPref(KEY_LOCATION_PROVIDER + KEY_DELIMITER + key);
			value = delPref(KEY_LOCATION_LATITUDE + KEY_DELIMITER + key);
			value = delPref(KEY_LOCATION_LONGITUDE + KEY_DELIMITER + key);
			value = delPref(KEY_LOCATION_TIMESTAMP + KEY_DELIMITER + key);
			value = delPref(KEY_LOCATION_ACCURACY + KEY_DELIMITER + key);
			value = delPref(KEY_LOCATION_SPEED + KEY_DELIMITER + key);
			value = delPref(KEY_LOCATION_BEARING + KEY_DELIMITER + key);
			value = delPref(KEY_LOCATION_ALTITUDE + KEY_DELIMITER + key);
		}
		catch (Exception e) {
			if(debug)Log.e(TAG, "Error deleting " + key);
			if(PRINT_E)Log.e(TAG, "Exception", e);
		}
		return value;
	}
	/**
	 * Delete preference
	 * 
	 * @return Returns true, if preference deleted.
	 * 
	 */
	public boolean delLocationArrayPref(String key) {
		boolean value = false;

		try {
			value = delPref(KEY_LOCATION_PROVIDER + KEY_DELIMITER + KEY_ARRAY + KEY_DELIMITER + key);
			value = delPref(KEY_LOCATION_LATITUDE + KEY_DELIMITER + KEY_ARRAY + KEY_DELIMITER + key);
			value = delPref(KEY_LOCATION_LONGITUDE + KEY_DELIMITER + KEY_ARRAY + KEY_DELIMITER + key);
			value = delPref(KEY_LOCATION_TIMESTAMP + KEY_DELIMITER + KEY_ARRAY + KEY_DELIMITER + key);
			value = delPref(KEY_LOCATION_ACCURACY + KEY_DELIMITER + KEY_ARRAY + KEY_DELIMITER + key);
			value = delPref(KEY_LOCATION_SPEED + KEY_DELIMITER + KEY_ARRAY + KEY_DELIMITER + key);
			value = delPref(KEY_LOCATION_BEARING + KEY_DELIMITER + KEY_ARRAY + KEY_DELIMITER + key);
			value = delPref(KEY_LOCATION_ALTITUDE + KEY_DELIMITER + KEY_ARRAY + KEY_DELIMITER + key);
		}
		catch (Exception e) {
			if(debug)Log.e(TAG, "Error deleting " + key);
			if(PRINT_E)Log.e(TAG, "Exception", e);
		}
		return value;
	}
	/**
	 * Delete preference
	 * 
	 * @return Returns true, if preference deleted.
	 * 
	 */
	public boolean delLocationArrayListPref(String key) {		
		boolean value = false;

		try {
			value = delPref(KEY_LOCATION_PROVIDER + KEY_DELIMITER + KEY_ARRAYLIST + KEY_DELIMITER + key);
			value = delPref(KEY_LOCATION_LATITUDE + KEY_DELIMITER + KEY_ARRAYLIST + KEY_DELIMITER + key);
			value = delPref(KEY_LOCATION_TIMESTAMP + KEY_DELIMITER + KEY_ARRAYLIST + KEY_DELIMITER + key);
			value = delPref(KEY_LOCATION_ACCURACY + KEY_DELIMITER + KEY_ARRAYLIST + KEY_DELIMITER + key);
			value = delPref(KEY_LOCATION_SPEED + KEY_DELIMITER + KEY_ARRAYLIST + KEY_DELIMITER + key);
			value = delPref(KEY_LOCATION_BEARING + KEY_DELIMITER + KEY_ARRAYLIST + KEY_DELIMITER + key);
			value = delPref(KEY_LOCATION_ALTITUDE + KEY_DELIMITER + KEY_ARRAYLIST + KEY_DELIMITER + key);
		}
		catch (Exception e) {
			if(debug)Log.e(TAG, "Error deleting " + key);
			if(PRINT_E)Log.e(TAG, "Exception", e);
		}
		return value;
	}
}
