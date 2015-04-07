package org.jumpingtree.randomology.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.jumpingtree.randomology.entities.ContactItem;

public class DatabaseHelper extends SQLiteOpenHelper {

	public static final String TABLE_CONTACTS = "TABLE_CONTACTS";
	public static final String CONTACTS_ID = "_id";
	public static final String CONTACTS_NAME = "CONTACTS_NAME";
	public static final String CONTACTS_NUMBER = "CONTACTS_NUMBER";
	public static final String CONTACTS_PHOTO = "CONTACTS_PHOTO";

	private static final String CREATE_TABLE_CONTACTS = "CREATE TABLE "
			+ TABLE_CONTACTS + " (" + CONTACTS_ID + " INTEGER PRIMARY KEY, "
			+ CONTACTS_NAME + " TEXT, " + CONTACTS_NUMBER + " INTEGER, " + CONTACTS_PHOTO
			+ " TEXT );";
	private static final String DROP_TABLE_CONTACTS = "DROP TABLE IF EXISTS NAME";

	private static final String[] CREATE = new String[] { CREATE_TABLE_CONTACTS };
	// private static final String[] CLEAR;
	private static final String[] DROP = new String[] { DROP_TABLE_CONTACTS };

	private Context context = null;

	public DatabaseHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		this.context = context;
	}

	public void onCreate(SQLiteDatabase paramSQLiteDatabase) {
		String[] arrayOfString = CREATE;

		for (int i = 0; i < arrayOfString.length; i++) {
			paramSQLiteDatabase.execSQL(arrayOfString[i]);
		}

		Log.i(DatabaseHelper.class.getSimpleName(), "Database Created.");

	}

	public void onDowngrade(SQLiteDatabase paramSQLiteDatabase, int paramInt1,
			int paramInt2) {
		Log.i(DatabaseHelper.class.getSimpleName(),
				"WARNING: Database Drop. ALL data will be LOST.");
		resetDatabase(paramSQLiteDatabase);
		onCreate(paramSQLiteDatabase);
	}

	public void onUpgrade(SQLiteDatabase paramSQLiteDatabase, int paramInt1,
			int paramInt2) {
		Log.i(DatabaseHelper.class.getSimpleName(),
				"WARNING: Database Drop. ALL data will be LOST.");
		resetDatabase(paramSQLiteDatabase);
		onCreate(paramSQLiteDatabase);
	}

	public ContactItem fetchContact(SQLiteDatabase paramSQLiteDatabase,
			long paramLong) {
		String[] arrayOfString = new String[] { CONTACTS_ID, CONTACTS_NAME,
				CONTACTS_NUMBER, CONTACTS_PHOTO };

		Cursor localCursor = paramSQLiteDatabase.query(TABLE_CONTACTS,
				arrayOfString, CONTACTS_ID + " == " + paramLong, null, null, null,
				null);

		ContactItem contactItem = null;

		if (localCursor.moveToFirst()) {

			contactItem = new ContactItem();
			contactItem.setId(localCursor.getString(localCursor
					.getColumnIndex(CONTACTS_ID)));
			contactItem.setName(localCursor.getString(localCursor
					.getColumnIndex(CONTACTS_NAME)));
			contactItem.setNumber(localCursor.getString(localCursor
					.getColumnIndex(CONTACTS_NUMBER)));
			contactItem.setPhoto(localCursor.getString(localCursor
					.getColumnIndex(CONTACTS_PHOTO)));
		}
		localCursor.close();
		return contactItem;
	}

	public boolean isDatabaseEmpty(SQLiteDatabase paramSQLiteDatabase) {

		boolean databaseEmpty = true;

		try {
			int numberOfNames = paramSQLiteDatabase.rawQuery(
					"SELECT " + CONTACTS_ID + " FROM " + TABLE_CONTACTS, null)
					.getCount();
			if (numberOfNames > 0) {
				databaseEmpty = false;
			}
		} catch (Exception e) {
			Log.e(DatabaseHelper.class.getSimpleName().toString(),
					"isDatabaseEmpty", e);

		}

		return databaseEmpty;
	}

	public void resetDatabase(SQLiteDatabase paramSQLiteDatabase) {
		String[] arrayOfString = DROP;

		for (int i = 0; i < arrayOfString.length; i++) {
			paramSQLiteDatabase.execSQL(arrayOfString[i]);
		}

	}

	// public void clearAllTables(SQLiteDatabase paramSQLiteDatabase) {
	// String[] arrayOfString = CLEAR;
	// int i = arrayOfString.length;
	// for (int j = 0;; j++) {
	// if (j >= i)
	// return;
	// paramSQLiteDatabase.execSQL(arrayOfString[j]);
	// }
	// }
}