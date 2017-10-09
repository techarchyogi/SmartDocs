package android.froyo.smartdocs;

import android.app.IntentService;
import android.content.Intent;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Notification;

public class ReminderService extends WakeReminderIntentService {

	public ReminderService() {
		super("ReminderService");
	}

	@Override
	void doReminderWork(Intent intent) {
		int rowId = intent.getExtras().getInt(SmartDbaAdapter.KEY_ROWID);
		// Status bar notification Code Goes here.
		SmartDbaAdapter mDb = new SmartDbaAdapter(this);
		NotificationManager mgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		// Intent notificationIntent = new Intent (this,
		// SmartDocsActivity.class);
		// Intent notificationIntent = new Intent (this, DocListActivity.class);
		Intent notificationIntent = new Intent(this, displayDoc.class);
		notificationIntent.putExtra(SmartDbaAdapter.KEY_ROWID, rowId);
		// notificationIntent.putExtra("SmartDocsActivity.FromList", true);
		System.out.println("ReminderService rowId: " + rowId);
		PendingIntent pi = PendingIntent.getActivity(this, 0,
				notificationIntent, PendingIntent.FLAG_ONE_SHOT);

		Notification note = new Notification(
				android.R.drawable.stat_sys_warning,
				getString(R.string.notify_new_task_message),
				System.currentTimeMillis());
		/*
		 * Notification note=new
		 * Notification(android.R.drawable.stat_sys_warning,
		 * mDb.getTitle(rowId), System.currentTimeMillis());
		 */
		note.setLatestEventInfo(this,
				getString(R.string.notifiy_new_task_title),
				getString(R.string.notify_new_task_message), pi);

		note.defaults |= Notification.DEFAULT_SOUND;
		note.flags |= Notification.FLAG_AUTO_CANCEL;

		// An issue could occur if user ever enters over 2,147,483,647 tasks.
		// (Max int value).

		// I highly doubt this will ever happen. But is good to note.

		int id = (int) ((long) rowId);
		// int id = 100;
		mgr.notify(id, note);

	}
}
