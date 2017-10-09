package android.froyo.smartdocs;

import android.app.ListActivity;
import android.os.Bundle;
import android.database.Cursor;
import android.widget.SimpleCursorAdapter;
import android.view.View.OnCreateContextMenuListener;
import android.view.ContextMenu;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.widget.SimpleCursorAdapter.ViewBinder;
import android.widget.ImageView;
import android.graphics.BitmapFactory;
import java.io.ByteArrayInputStream;

public class DocListActivity extends ListActivity {

	private static final int ACTIVITY_CREATE = 0;
	private static final int ACTIVITY_EDIT = 1;

	private SmartDbaAdapter mDbHelper;
	SQLiteDatabase dBh;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.smart_doc_list);
		mDbHelper = new SmartDbaAdapter(this);

		fillData();

		registerForContextMenu(getListView());

	}

	@SuppressWarnings("unused")
	private void fillData() {
		SQLiteDatabase dBh = null;
		Cursor docsCursor = mDbHelper.fetchAllDocs(dBh);

		while (docsCursor.moveToNext()) {
			System.out.println("title : "
					+ docsCursor
							.getColumnIndexOrThrow(SmartDbaAdapter.KEY_TITLE));
		}
		// Cursor cur = db.rawQuery( "select rowid _id,* from your_table",
		// null);
		startManagingCursor(docsCursor);

		String[] from = new String[] { SmartDbaAdapter.KEY_TITLE,
				SmartDbaAdapter.KEY_DATE_TIME, SmartDbaAdapter.KEY_IMAGE };//

		int[] to = new int[] { R.id.textView1, R.id.textDate, R.id.imageIcon };//

		/*
		 * dBh= mDbHelper.open(1);
		 * 
		 * System.out.println(from[0]);
		 */
		SimpleCursorAdapter docs = new SimpleCursorAdapter(this,
				R.layout.simple_row_wimage, docsCursor, from, to);

		docs.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
			@Override
			public boolean setViewValue(View view, Cursor cursor,
					int columnIndex) {
				if (view.getId() == R.id.imageIcon) {
					ImageView iv = (ImageView) view;
					// int resID =
					// getApplicationContext().getResources().getIdentifier(cursor.getString(columnIndex),
					// "drawable", getApplicationContext().getPackageName());
					byte[] byteArray = cursor.getBlob(columnIndex);
					iv.setImageBitmap(BitmapFactory.decodeByteArray(byteArray,
							0, byteArray.length));
					return true;
				}
				return false;
			}

		});

		setListAdapter(docs);

		/*
		 * ViewBinder viewBinder = new ViewBinder(){ public boolean
		 * setViewValue(View view, Cursor cursor, int columnIndex) { ImageView
		 * image = (ImageView) view; byte [] byteArray =
		 * cursor.getBlob(columnIndex); String name =
		 * cursor.getString(cursor.getColumnIndexOrThrow
		 * (SmartDbaAdapter.KEY_TITLE));
		 * System.out.println("Title: from setvalue : " + name);
		 * ByteArrayInputStream imageStream = new
		 * ByteArrayInputStream(byteArray); if(imageStream != null) { if(image
		 * != null) {
		 * image.setImageBitmap(BitmapFactory.decodeStream(imageStream)); } else
		 * { System.out.println("image null"); } } else {
		 * System.out.println(" imagestream is null"); }
		 * //image.setImageBitmap(BitmapFactory.decodeByteArray(byteArray, 0,
		 * byteArray.length)); return true; } };
		 * 
		 * if( docsCursor.moveToFirst() == false) {
		 * System.out.println("Cursor is empty"); }
		 * 
		 * System.out.println("column : "
		 * +docsCursor.getColumnIndexOrThrow(SmartDbaAdapter.KEY_IMAGE));
		 * ImageView image = (ImageView) findViewById(R.id.imageIcon);
		 * if(image== null) { System.out.println("Image is Null"); }
		 * viewBinder.setViewValue(image, docsCursor,
		 * docsCursor.getColumnIndexOrThrow(SmartDbaAdapter.KEY_IMAGE));
		 * docs.setViewBinder(viewBinder); setListAdapter(docs); if ( dBh !=
		 * null ) { dBh.close(); }
		 */

	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater mi = getMenuInflater();
		mi.inflate(R.menu.list_menu_item_longpress, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_delete:
			AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
					.getMenuInfo();
			mDbHelper.deleteDoc((int) info.id);
			fillData();
			return true;
		case R.id.menu_edit:
			Intent i = new Intent(this, SmartDocsActivity.class);
			info = (AdapterContextMenuInfo) item.getMenuInfo();
			int id = (int) info.id;
			i.putExtra(SmartDbaAdapter.KEY_ROWID, id);
			i.putExtra("SmartDocsActivity.FromList", true);
			startActivityForResult(i, ACTIVITY_EDIT);

		}
		return super.onContextItemSelected(item);
	}

}
