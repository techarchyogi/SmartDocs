package android.froyo.smartdocs;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.Context;

public class OnAlarmReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		int rowid = intent.getExtras().getInt(SmartDbaAdapter.KEY_ROWID);// getLong(SmartDbaAdapter.KEY_ROWID);

		WakeReminderIntentService.acquireStaticLock(context);
		Intent i = new Intent(context, ReminderService.class);

		i.putExtra(SmartDbaAdapter.KEY_ROWID, rowid);
		context.startService(i);

	}

}
