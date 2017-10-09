package android.froyo.smartdocs;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.Calendar;
import java.text.SimpleDateFormat;

import java.text.ParseException;

public class OnBootReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		reminderManager reminderMgr = new reminderManager(context);
		SmartDbaAdapter dbHelper = new SmartDbaAdapter(context);
		SQLiteDatabase dBh = null;

		dbHelper.open();
		Cursor cursor = dbHelper.fetchAllDocs(dBh);

		if (cursor != null) {
			cursor.moveToFirst();

			int rowIdColumnIndex = cursor
					.getColumnIndex(SmartDbaAdapter.KEY_ROWID);
			int dateTimeColumnIndex = cursor
					.getColumnIndex(SmartDbaAdapter.KEY_DATE_TIME);

			while (cursor.isAfterLast() == false) {
				int rowId = cursor.getInt(rowIdColumnIndex);
				String dateTime = cursor.getString(dateTimeColumnIndex);

				Calendar cal = Calendar.getInstance();
				SimpleDateFormat format = new SimpleDateFormat(
						SmartDocsActivity.DATE_TIME_FORMAT);

				try {
					java.util.Date date = format.parse(dateTime);
					cal.setTime(date);
					reminderMgr.setReminder(rowId, cal);

				} catch (ParseException pe) {
					Log.e("OnBootReceiver", pe.getMessage(), pe);
				}
				cursor.moveToNext();
			}
			cursor.close();

		}
		dbHelper.close();
	}
}
