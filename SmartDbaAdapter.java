package android.froyo.smartdocs;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;

import java.lang.String;
import java.lang.Byte;

public class SmartDbaAdapter {

	private static final String DATABASE_NAME = "docs_dataNew1";
	private static final String DATABASE_TABLE = "SmartDocsNew1";
	private static final int DATABASE_VERSION = 1;

	public static final String KEY_TITLE = "title";
	public static final String KEY_ROWID = "row_id";
	public static final String KEY_DATE_TIME = "doc_DateTime";
	public static final String KEY_BODY = "body";
	public static final String KEY_IMAGE = "image";

	private SQLiteDatabase mDb = null;
	private DatabaseHelper mDbHelper; // custom class

	private static final String DATABASE_CREATE = "create table "
			+ DATABASE_TABLE + " (" + KEY_ROWID
			+ " INTEGER PRIMARY KEY NOT NULL UNIQUE, " + KEY_TITLE
			+ " text not null, " + KEY_DATE_TIME + " text not null, "
			+ KEY_BODY + " text, " + KEY_IMAGE + " BLOB );";

	private final Context mCtx;

	public SmartDbaAdapter(Context context) {
		this.mCtx = context;
		open();
	}

	public SmartDbaAdapter open() throws android.database.SQLException {
		System.err.println("opening the database ");
		mDbHelper = new DatabaseHelper(mCtx);
		/*
		 * try { mDb = mDbHelper.getWritableDatabase(); } catch (
		 * SQLiteException e) {
		 * System.err.println("Could not open the database "); throw e ; }
		 * System.err.println("opened the database ");
		 */
		return this;
	}

	public SQLiteDatabase open(int i) throws android.database.SQLException {
		try {
			return mDbHelper.getWritableDatabase();
		} catch (SQLiteException e) {
			System.err.println("Could not open the database ");
			throw e;
		}
	}

	public void close() {
		mDbHelper.close();
	}

	public int CreateDoc(String name, String dt, String reminderDateTime,
			byte[] imageInByte) {
		ContentValues initialValues = new ContentValues();

		initialValues.put(KEY_TITLE, name);
		initialValues.put(KEY_DATE_TIME, reminderDateTime);
		initialValues.put(KEY_BODY, dt);

		initialValues.put(KEY_IMAGE, imageInByte);// .put(KEY_IMAGE,
													// imageInByte);
		// initialValues.put(KEY_IMAGE, imageInByte);

		// first check if the database indeed exsists.
		isTableExists();
		System.out.println("Inserting value into database " + name + "  " + dt
				+ "  " + reminderDateTime);
		SQLiteDatabase dBh = null;
		dBh = open(1);
		long rv = dBh.insert(DATABASE_TABLE, null, initialValues);
		dBh.close();

		System.out.println("Return value = " + rv);
		return (int) rv;

	}

	public boolean deleteDoc(int rowId) {
		/*
		 * if ( mDb != null ){ return mDb.delete(DATABASE_TABLE, KEY_ROWID + "="
		 * + rowId , null) > 0; }
		 */
		SQLiteDatabase dBh = null;
		dBh = open(1);
		if (dBh != null) {
			boolean tmp;
			tmp = dBh.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
			dBh.close();
			return tmp;
		}
		// dBh.close();
		return true;
	}

	public Cursor fetchAllDocs(SQLiteDatabase dBh) {
		String qry = "SELECT rowid _id, * FROM  " + DATABASE_TABLE;
		System.out.println("Query is " + qry);
		// SQLiteDatabase dBh = null;
		dBh = open(1);
		if (dBh != null) {
			Cursor tmp;
			tmp = dBh.rawQuery(qry, null);

			// dBh.close();
			return tmp;
		}
		// return mDb.rawQuery("SELECT * FROM " + DATABASE_TABLE, null);
		/*
		 * if ( mDb != null ) { return mDb.rawQuery(qry, null); }
		 */
		// return mDb.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_TITLE,
		// KEY_BODY, KEY_DATE_TIME, KEY_IMAGE}, null, null, null, null,null);
		// return mDb.rawQuery( "select rowid _id,* from "+ DATABASE_TABLE,
		// null);
		// dBh.close();
		return null;
	}

	public Cursor fetchDoc(int rowId, SQLiteDatabase dBh) throws SQLException {
		dBh = open(1);

		Cursor mCursor = dBh.query(DATABASE_TABLE, new String[] { KEY_ROWID,
				KEY_TITLE, KEY_BODY, KEY_DATE_TIME, KEY_IMAGE }, KEY_ROWID
				+ "=" + rowId, null, null, null, null);
		// dBh.close();
		if (mCursor != null) {
			mCursor.moveToFirst();
		}

		return mCursor;
	}

	public boolean updateDoc(int rowId, String title, String desc,
			String dateTime, byte[] image) {
		SQLiteDatabase dBh = open(1);
		ContentValues value = new ContentValues();
		value.put(KEY_TITLE, title);
		value.put(KEY_BODY, desc);
		value.put(KEY_DATE_TIME, dateTime);
		value.put(KEY_IMAGE, image);
		if (dBh != null) {
			boolean tmp;
			tmp = dBh.update(DATABASE_TABLE, value, KEY_ROWID + "=" + rowId,
					null) > 0;
			dBh.close();

			return tmp;

		}
		/*
		 * if ( mDb != null ){ return (mDb.update(DATABASE_TABLE, value,
		 * KEY_ROWID +"="+ rowId, null) > 0); }
		 */
		if (dBh != null) {
			dBh.close();
		}
		return false;
	}

	boolean isTableExists() {
		SQLiteDatabase dBh = null;

		dBh = open(1);

		if (DATABASE_NAME == null || dBh == null || !dBh.isOpen()) {
			System.out.println("1....");
			return false;
		}
		Cursor cursor = dBh
				.rawQuery(
						"SELECT COUNT(*) FROM sqlite_master WHERE type = ? AND name = ?",
						new String[] { "table", DATABASE_TABLE });
		if (!cursor.moveToFirst()) {
			System.out.println("2....");
			return false;
		}
		System.out.println("3....");
		// To check the column names
		Cursor ti = dBh.rawQuery("PRAGMA table_info(" + DATABASE_TABLE + ")",
				null);
		if (ti.moveToFirst()) {
			do {
				System.out.println("col: " + ti.getString(1));
			} while (ti.moveToNext());
		}

		int count = cursor.getInt(0);
		cursor.close();

		// mDb.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
		// deleteDatabase();
		if (dBh != null) {
			dBh.close();
		}
		return count > 0;
	}

	public void deleteDatabase() {
		mCtx.deleteDatabase(DATABASE_NAME + ".db");
	}

	public String getTitle(int rowId) {
		// Cursor cursor = mDb.query(DATABASE_TABLE, new String[] {KEY_ROWID,
		// KEY_TITLE, KEY_BODY, KEY_DATE_TIME, KEY_IMAGE} , KEY_ROWID +"="+
		// rowId, null, null, null, null);
		// name.setText(doc.getString(doc.getColumnIndexOrThrow(SmartDbaAdapter.KEY_TITLE)));
		// startManagingCursor(cursor);
		SQLiteDatabase dBh = null;
		Cursor cursor = fetchDoc(rowId, dBh);
		System.out.println("gettile for : " + rowId);
		String title = cursor
				.getString(cursor.getColumnIndexOrThrow(KEY_TITLE));

		if (dBh != null) {
			dBh.close();
		}

		return title;

	}

	private static class DatabaseHelper extends SQLiteOpenHelper {
		public DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			System.out.println("onCreate DB started");
			db.execSQL(DATABASE_CREATE);
			System.out.println("onCreate DB done");
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// not used
		}

	}

}
