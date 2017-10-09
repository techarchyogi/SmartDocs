package android.froyo.smartdocs;

import android.content.Context;
import android.app.AlarmManager;
import java.util.Calendar;
import android.app.PendingIntent;
import android.content.Intent;

public class reminderManager {

	private Context mContext;
	private AlarmManager mAlarmManager;

	public reminderManager(Context context) {
		mContext = context;
		mAlarmManager = (AlarmManager) context
				.getSystemService(context.ALARM_SERVICE);
	}

	public void setReminder(int taskId, Calendar when) {
		Intent i = new Intent(mContext, OnAlarmReceiver.class);
		i.putExtra(SmartDbaAdapter.KEY_ROWID, (int) taskId);

		PendingIntent pi = PendingIntent.getBroadcast(mContext, 0, i,
				PendingIntent.FLAG_ONE_SHOT);
		mAlarmManager.set(AlarmManager.RTC_WAKEUP, when.getTimeInMillis(), pi);
	}
}
