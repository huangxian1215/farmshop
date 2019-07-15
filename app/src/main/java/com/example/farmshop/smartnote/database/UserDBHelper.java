package com.example.farmshop.smartnote.database;

import java.util.ArrayList;
import java.util.Map;

import com.example.farmshop.MainApplication;
import com.example.farmshop.smartnote.bean.TableItem;
import com.example.farmshop.smartnote.util.JsonDataTtransfer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class UserDBHelper extends SQLiteOpenHelper {
	private static final String TAG = "UserDBHelper";
	private static final String DB_NAME = MainApplication.getInstance().savePath + "user.db";
	private static final int DB_VERSION = 1;
	private static UserDBHelper mHelper = null;
	private SQLiteDatabase mDB = null;
    private static final String TABLE_INFO_NAME = "tabInfo";
	private static final String TABLE_NAME = "user_info";

	private UserDBHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	private UserDBHelper(Context context, int version) {
		super(context, DB_NAME, null, version);
	}

	public static UserDBHelper getInstance(Context context, int version) {
		if (version > 0 && mHelper == null) {
			mHelper = new UserDBHelper(context, version);
		} else if (mHelper == null) {
			mHelper = new UserDBHelper(context);
		}
		return mHelper;
	}

	public static UserDBHelper getmHelper(){
		return mHelper;
	}

	public SQLiteDatabase openReadLink() {
		if (mDB == null || mDB.isOpen() != true) {
			mDB = mHelper.getReadableDatabase();
		}
		return mDB;
	}

	public SQLiteDatabase openWriteLink() {
		if (mDB == null || mDB.isOpen() != true) {
			mDB = mHelper.getWritableDatabase();
		}
		return mDB;
	}

	public void closeLink() {
		if (mDB != null && mDB.isOpen() == true) {
			mDB.close();
			mDB = null;
		}
	}
	
	public String getDBName() {
		if (mHelper != null) {
			return mHelper.getDatabaseName();
		} else {
			return DB_NAME;
		}
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.d(TAG, "onCreate");
			String drop_sql = "DROP TABLE IF EXISTS " + TABLE_INFO_NAME + ";";
			Log.d(TAG, "drop_sql:" + drop_sql);
			db.execSQL(drop_sql);
			String create_sql = "CREATE TABLE IF NOT EXISTS " + TABLE_INFO_NAME + " ("
					+ "_id INTEGER PRIMARY KEY  AUTOINCREMENT NOT NULL,"
					+ "name VARCHAR NOT NULL,"
					+ "descript VARCHAR NOT NULL"
					//+ "age INTEGER NOT NULL,"
					//+ "height LONG NOT NULL," + "weight FLOAT NOT NULL,"
					//+ "married INTEGER NOT NULL," + "update_time VARCHAR NOT NULL"
					//演示数据库升级时要先把下面这行注释
					//+ ",phone VARCHAR" + ",password VARCHAR"
					+ ");";
			Log.d(TAG, "create_sql:" + create_sql);
			db.execSQL(create_sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.d(TAG, "onUpgrade oldVersion="+oldVersion+", newVersion="+newVersion);
		if (newVersion > 1) {
			//Android的ALTER命令不支持一次添加多列，只能分多次添加
//			String alter_sql = "ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + "phone VARCHAR;";
//			Log.d(TAG, "alter_sql:" + alter_sql);
//			db.execSQL(alter_sql);
//			alter_sql = "ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + "password VARCHAR;";
//			Log.d(TAG, "alter_sql:" + alter_sql);
//			db.execSQL(alter_sql);
            //dynamicCreateTable();
		}
	}

	public void dynamicCreateTable(String tabname, String key){//
        if(tabname != "" && key != "") {
			SQLiteDatabase db = getWritableDatabase();
            String drop_sql = "DROP TABLE IF EXISTS " + tabname + ";";
            Log.d(TAG, "drop_sql:" + drop_sql);
			db.execSQL(drop_sql);
            ArrayList<String> itemList;
            itemList = JsonDataTtransfer.anaylse(key);
            String[] arr = itemList.toArray(new String[0]);
            String createTab_sql = "CREATE TABLE IF NOT EXISTS " + tabname + " ("
                    + "_id INTEGER PRIMARY KEY  AUTOINCREMENT NOT NULL,"
                    + "time VARCHAR NOT NULL";

            for (int i = 0, j = 0; i < arr.length; i = i + 2, j++) {//value; type
                switch (arr[i + 1]) {
                    case "txt":
                        createTab_sql += ", item" + String.valueOf(j) + " VARCHAR NOT NULL";
                        break;
                    case "num":
                        createTab_sql += ", item" + String.valueOf(j) + " FLOAT NOT NULL";
                        break;
                    case "buer":
                        createTab_sql += ", item" + String.valueOf(j) + " INTEGER NOT NULL";
                        break;
                }
            }
            createTab_sql += ");";
			db.execSQL(createTab_sql);
        }
	}

	public int delete(String condition, String tabname) {
		openReadLink();
		int count = mDB.delete(tabname, condition, null);
		return count;
	}

	public int deleteAll(String tabname) {
		openReadLink();
		int count = mDB.delete(tabname, "1=1", null);
		return count;
	}

	public long insert(TableItem info, String name) {
		ArrayList<TableItem> infoArray = new ArrayList<TableItem>();
		infoArray.add(info);
		return insert(infoArray, name);
	}
	
	public long insert(ArrayList<TableItem> infoArray, String name) {
		openWriteLink();
		long result = -1;
		for (int i = 0; i < infoArray.size(); i++) {
			TableItem info = infoArray.get(i);
			ArrayList<TableItem> tempArray = new ArrayList<TableItem>();
			// 不存在唯一性重复的记录，则插入新记录
			ContentValues cv = new ContentValues();
			cv.put("name", info.name);
			cv.put("descript", info.desc);

			result = mDB.insert(name, "", cv);
			// 添加成功后返回行号，失败后返回-1
			if (result == -1) {
				return result;
			}
		}
		return result;
	}

	public long insert(String[] arr, String[] type, String tabname) {
		openWriteLink();
		long result = -1;
		ContentValues cv = new ContentValues();
		//时间
		cv.put("time", arr[0]);
		for(int i = 1; i < arr.length; i++){
			switch (type[i-1]){
				case "txt":
					cv.put("item" + String.valueOf(i-1), arr[i]);
					break;
				case "num":
					cv.put("item" + String.valueOf(i-1), Float.parseFloat(arr[i]));
					break;
				case "buer":
					int buer = 0;
					if(arr[i].equals("true")) buer = 1;
					cv.put("item" + String.valueOf(i-1), buer);
					break;
			}

		}
		result = mDB.insert(tabname, "", cv);
		return result;
	}

	public int update(TableItem info, String condition, String tabname) {
		openReadLink();
		ContentValues cv = new ContentValues();
		cv.put("name", info.name);
		int count = mDB.update(tabname, cv, condition, null);
		return count;
	}

	public int update(String[] arr, String[] type, String tabname) {
		openReadLink();
		//arr: _id, time, item0, item1, item2....
		ContentValues cv = new ContentValues();
		//时间
		cv.put("time", arr[1]);
		for(int i = 2; i < arr.length; i++){
			switch (type[i-2]){
				case "txt":
					cv.put("item" + String.valueOf(i-2), arr[i]);
					break;
				case "num":
					cv.put("item" + String.valueOf(i-2), Float.parseFloat(arr[i]));
					break;
				case "buer":
					cv.put("item" + String.valueOf(i-2), arr[i].equals("true") ? 1 : 0);
					break;
			}
		}
		String condition = "_id = "  + Integer.parseInt(arr[0]);
		int count = mDB.update(tabname, cv, condition, null);
		return count;
	}

	public ArrayList<TableItem> query_tab(String condition, String tabname) {
		openReadLink();
		String sql = String.format("select _id,name,descript" +
				" from %s where %s;", tabname, condition);
		Log.d(TAG, "query sql: "+sql);
		ArrayList<TableItem> infoArray = new ArrayList<TableItem>();
		Cursor cursor = mDB.rawQuery(sql, null);
		if (cursor.moveToFirst()) {
			int count = 0;
			for (;; cursor.moveToNext()) {
				TableItem info = new TableItem();
				//info.num = Integer.valueOf(cursor.getString(0));
				info.num = count++;
				info.dbNum = cursor.getString(0);
				info.name = cursor.getString(1);
				info.desc = cursor.getString(2);
				infoArray.add(info);
				if (cursor.isLast() == true) {
					break;
				}
			}
		}
		cursor.close();
		return infoArray;
	}

	public ArrayList<String> queryTabByDesc(String condition, String tabname, int num){
		ArrayList<String> data = new ArrayList<String>();
		openReadLink();
		String sql = "select _id,time";
		for(int i = 0; i < num; i++){
			sql += ",item" + String.valueOf(i);
		}
		sql = String.format(sql + " from %s where %s order by time desc;", tabname, condition);
		Cursor cursor = mDB.rawQuery(sql, null);
		if(cursor.moveToFirst()){
			for(;; cursor.moveToNext()){
				data.add(cursor.getString(0)); //_id
				data.add(cursor.getString(1)); //time
				for(int n = 0; n < num; n++){
					data.add(cursor.getString(n+2));
				}
				if (cursor.isLast() == true) {
					break;
				}
			}
		}
		return data;
	}

	public TableItem queryByname(String name, String tabname) {
		openReadLink();
		TableItem info = null;
		ArrayList<TableItem> infoArray = query_tab(String.format("name='%s'", name), tabname);
		if (infoArray.size() > 0) {
			info = infoArray.get(0);
		}
		return info;
	}
}
