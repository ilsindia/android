package com.crinis.db;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBaseHandler {

	SQLiteDatabase sdb;
	Context context;

	// Database related varibles

	private final String DATABASE_NAME = "blc_db";
	private final int DATABASE_VERSION = 1;

	// Table realted Variables

	private final String TABLE_NAME = "information_table";
	private final String INDEX_COL = "indx_no";
	private final String ISBN = "isbn";
	private final String TITLE = "title";
	private final String PUBLISHER_DETAIL = "publishr_detail";
	private final String AUTHOR = "author";

	private final String TABLE_REGISTRATION = "registrationt_table";
	private final String SL_NO = "sl_no";
	private final String UNAME = "uname";
	private final String DEV_ID = "device_id";
	private final String MOB_No = "mob_no";
	private final String LATITUDE="latitute";
	private final String LONGITUDE="longitude";

	private final String CREATE_REG_TABLE = "create table "
			+ TABLE_REGISTRATION + "(Sl_no integer primary key autoincrement, "
			+ UNAME + " text, " + DEV_ID + " text, " + MOB_No + " text, "+LATITUDE+" text, "+LONGITUDE+" text);";
	// sql string
	private final String CREATE_TABLE = "create table " + TABLE_NAME
			+ "(indx_no integer primary key autoincrement, " + ISBN + " text, "
			+ TITLE + " text, " + AUTHOR + " text, " + PUBLISHER_DETAIL
			+ " text );";

	private String[] column_Array = { INDEX_COL, ISBN, TITLE, AUTHOR,
			PUBLISHER_DETAIL };

	private String[] column_regArray = { SL_NO, UNAME, DEV_ID, MOB_No,LATITUDE,LONGITUDE};
	
	
	public DBaseHandler(Context context) {
		this.context = context;
		OpenHelper myHelper = new OpenHelper(context, DATABASE_NAME, null,
				DATABASE_VERSION);
		sdb = myHelper.getWritableDatabase();

	}

	public void insertRow(String a, String b, String c, String d) {
		ContentValues dv = new ContentValues();

		dv.put(ISBN, a);
		dv.put(TITLE, b);
		dv.put(AUTHOR, c);
		dv.put(PUBLISHER_DETAIL, d);

		sdb.insert(TABLE_NAME, null, dv);
	}

	public void insertReg(String usrname, String phoneno, String lat, String longi) {
		ContentValues dv = new ContentValues();

		dv.put(UNAME, usrname);
		dv.put(MOB_No, phoneno);
		dv.put(LATITUDE, lat);
		dv.put(LONGITUDE, longi);

		sdb.insert(TABLE_REGISTRATION, null, dv);
	}
	
	public void updateRow(int rowid, String a, String b, String c, String d) {
		ContentValues dv = new ContentValues();

		dv.put(ISBN, a);
		dv.put(TITLE, b);
		dv.put(AUTHOR, c);
		dv.put(PUBLISHER_DETAIL, d);

		sdb.update(TABLE_NAME, dv, INDEX_COL + "=" + rowid, null);
	}

	public void deleteRow(int rowID) {
		// Delete from Table_name where sNo.=2;

		sdb.delete(TABLE_NAME, INDEX_COL + "=" + rowID, null);
	}

	public void deleteAllRow() {

		sdb.delete(TABLE_NAME, null, null);
	}

	public ArrayList<Object> retriveOneRow(int rowId) {
		Cursor cursor;
		ArrayList<Object> rowArray = new ArrayList<Object>();
		try {

			cursor = sdb.query(TABLE_NAME, column_Array, INDEX_COL + "="
					+ rowId, null, null, null, null);

			cursor.moveToFirst();

			if (!cursor.isAfterLast()) {

				do {
					rowArray.add(cursor.getInt(0));
					rowArray.add(cursor.getString(1));
					rowArray.add(cursor.getString(2));
					rowArray.add(cursor.getString(3));
					rowArray.add(cursor.getString(4));
				} while (cursor.moveToNext());

			}
			cursor.close();

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println(e);
		}
		return rowArray;

	}
	
	public ArrayList<Object> reteriveReg() {


		Cursor cursor;
		ArrayList<Object> rowData = null;
		try {
			cursor = sdb.query(TABLE_REGISTRATION, column_regArray, null, null, null,
					null, null);

			cursor.moveToFirst();

			if (!cursor.isAfterLast()) {
				do {
					 rowData = new ArrayList<Object>();

					rowData.add(cursor.getInt(0));
					rowData.add(cursor.getString(1));
					rowData.add(cursor.getString(2));
					rowData.add(cursor.getString(3));
					rowData.add(cursor.getString(4));
				} while (cursor.moveToNext());

			}
			cursor.close();
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
			System.out.println(e);
		}

		return rowData;

	}
	

	public ArrayList<ArrayList<Object>> reteriveRow() {

		ArrayList<ArrayList<Object>> tableData = new ArrayList<ArrayList<Object>>();

		Cursor cursor;
		try {
			cursor = sdb.query(TABLE_NAME, column_Array, null, null, null,
					null, null);

			cursor.moveToFirst();

			if (!cursor.isAfterLast()) {
				do {
					ArrayList<Object> rowData = new ArrayList<Object>();

					rowData.add(cursor.getInt(0));
					rowData.add(cursor.getString(1));
					rowData.add(cursor.getString(2));
					rowData.add(cursor.getString(3));
					rowData.add(cursor.getString(4));
					tableData.add(rowData);
				} while (cursor.moveToNext());

			}
			cursor.close();
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
			System.out.println(e);
		}

		return tableData;

	}

	public void closeDB() {

		sdb.close();
	}

	class OpenHelper extends SQLiteOpenHelper {

		public OpenHelper(Context context, String name, CursorFactory factory,
				int version) {
			super(context, name, factory, version);

		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(CREATE_TABLE);
			db.execSQL(CREATE_REG_TABLE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		}

	}

}
