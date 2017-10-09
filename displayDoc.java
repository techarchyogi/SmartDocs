package android.froyo.smartdocs;

import java.io.ByteArrayInputStream;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.database.sqlite.SQLiteDatabase;
import android.database.Cursor;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;
import android.content.Intent;

public class displayDoc extends Activity {

	private int rowId;
	private SmartDbaAdapter mDb;

	protected void onCreate(Bundle savedInstanceState) {
		TextView title, description, date;
		ImageView image;

		mDb = new SmartDbaAdapter(this);

		super.onCreate(savedInstanceState);

		setContentView(R.layout.display_doc);

		Bundle extras = getIntent().getExtras();
		rowId = extras != null ? extras.getInt(SmartDbaAdapter.KEY_ROWID)
				: null;

		SQLiteDatabase dBh = null;
		Cursor cursor = mDb.fetchDoc(rowId, dBh);

		title = (TextView) findViewById(R.id.dispTitle);
		description = (TextView) findViewById(R.id.dispDesc);
		date = (TextView) findViewById(R.id.dispDate);

		image = (ImageView) findViewById(R.id.dispImage);

		title.setText(cursor.getString(cursor
				.getColumnIndex(SmartDbaAdapter.KEY_TITLE)));
		description.setText(cursor.getString(cursor
				.getColumnIndex(SmartDbaAdapter.KEY_BODY)));
		date.setText(cursor.getString(cursor
				.getColumnIndex(SmartDbaAdapter.KEY_DATE_TIME)));

		// code to populate the image
		byte[] outImage = cursor.getBlob(cursor
				.getColumnIndexOrThrow(SmartDbaAdapter.KEY_IMAGE));
		ByteArrayInputStream imageStream = new ByteArrayInputStream(outImage);
		Bitmap bImage = BitmapFactory.decodeStream(imageStream);
		image.setImageBitmap(bImage);

		Button delete = (Button) findViewById(R.id.dispBtnDelete);
		delete.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mDb.deleteDoc(rowId);

			}
		});

		Button ok = (Button) findViewById(R.id.dispBtnOk);
		ok.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				System.exit(0);

			}
		});

		Button edit = (Button) findViewById(R.id.dispBtnEdit);
		edit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent i = new Intent(displayDoc.this, SmartDocsActivity.class);
				i.putExtra(SmartDbaAdapter.KEY_ROWID, rowId);
				i.putExtra("SmartDocsActivity.FromList", true);
				startActivity(i);

			}
		});

	}

	@Override
	protected void onResume() {
		super.onResume();

	}
}
