package android.froyo.smartdocs;

//import com.example.android.photobyintent.R;

import java.util.Date;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.lang.System;
import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.io.ByteArrayInputStream;
import java.lang.Boolean;
import android.os.Bundle;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.graphics.BitmapFactory;
import android.widget.ImageView;
import android.app.Dialog;

import android.app.DatePickerDialog;
                                            
import android.app.TimePickerDialog;
import android.graphics.PorterDuff;
//import android.widget.MediaController;
import android.provider.Settings.NameValueTable;
import android.widget.Toast;
import android.graphics.drawable.Drawable;
import android.graphics.PorterDuff.Mode;


public class SmartDocsActivity extends Activity {

	// boolean firstTime;
	Document d;
	public static Boolean FromList;
	
	public static Boolean FromCamera;
	
	private ImageView mImageView;
	private Bitmap mImageBitmap;
	private EditText name;
	private EditText note;
//	private Date dueDate;
	private Calendar mCalendar;

	private Button dateBtn;
	private Button timeBtn;

	private String strTime;
	private String strDate;
	
	private String orgDate;
	private String orgTime;

	private Integer mrowId;

	private static final int ACTION_TAKE_PHOTO_B = 1;
	private static final int ACTION_TAKE_PHOTO_S = 2;

	private static final int DATE_PICKER_DIALOG = 0;
	private static final int TIME_PICKER_DIALOG = 1;

	private static final String TIME_FORMAT = "kk:mm";// “kk:mm”;
	private static final String DATE_FORMAT = "yyyy-MM-dd";// "yy-MM-DD";//“yyyy-MM-dd”;
	public static final String DATE_TIME_FORMAT = "yyyy-MM-dd kk:mm:ss";

	SmartDbaAdapter mDbHelper; // = new SmartDbaAdapter(this);

	// SmartDbaAdapter.DatabaseHelper mDbHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		Bitmap defaultImage;
		super.onCreate(savedInstanceState);
		// firstTime = true;

		mDbHelper = new SmartDbaAdapter(this);
		// mDbHelper.open();

		setContentView(R.layout.activity_smart_docs);

		FromList = false;
		if(savedInstanceState == null)
		{
		    FromCamera = false;
		}

		View title = getWindow().findViewById(android.R.id.title);
		View titleBar = (View) title.getParent();
		titleBar.setBackgroundColor(0xff000000);
		this.setTitleColor(0xff00ff00);

		Button b = (Button) findViewById(R.id.button1);
		b.getBackground().setColorFilter(0xff000000, PorterDuff.Mode.MULTIPLY); // black
																				// color
																				// button

		name = (EditText) findViewById(R.id.editText1);
		mImageView = (ImageView) findViewById(R.id.imageView1);

		defaultImage = BitmapFactory.decodeResource(getResources(),
				R.drawable.no_image_6);
		mImageView.setImageBitmap(defaultImage);

		note = (EditText) findViewById(R.id.editText2);

		mrowId = (FromList && savedInstanceState != null) ? savedInstanceState
				.getInt(SmartDbaAdapter.KEY_ROWID) : null;

		if ( mrowId != null ) {
			System.out.println("Non-null row ID please save if you have not saved." );
		} else {
		}
		Button btnShowList = (Button) findViewById(R.id.btnShowList);
		btnShowList.getBackground().setColorFilter(0xff000000,
				PorterDuff.Mode.MULTIPLY);
		btnShowList.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(SmartDocsActivity.this,
						DocListActivity.class);
				// mDbHelper.open() ;
				/*
				 * Cursor temp = mDbHelper.fetchAllDocs(); while (
				 * temp.moveToNext())
				 * 
				 * { System.out.println("title : " + temp.getString(1) + "  "+
				 * temp.getString(2));
				 * 
				 * } // OPen a new window. temp.close();
				 */

				startActivity(i);
			}
		});

		Button cancelBtn = (Button) findViewById(R.id.buttonCancel);
		cancelBtn.getBackground().setColorFilter(0xff000000,
				PorterDuff.Mode.MULTIPLY);
		// Drawable dr = ((Button)cancelBtn).getBackground();
		// dr.setColorFilter(R.color.btntextcolormj, PorterDuff.Mode.MULTIPLY);
		// ((Button)cancelBtn).setBackgroundDrawable(dr);

		cancelBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// System.exit(0);
				System.out.println("Checking Database..."
						+ mDbHelper.isTableExists());

			}
		});

		
		mCalendar = Calendar.getInstance();
		System.out.println("Calendar is instantiated");
		// setText("Hello World");//debug
		dateBtn = (Button) findViewById(R.id.btn_remind_date);
		dateBtn.getBackground().setColorFilter(0xff000000,
				PorterDuff.Mode.MULTIPLY);
		dateBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDialog(DATE_PICKER_DIALOG);

			}
		});

		timeBtn = (Button) findViewById(R.id.btn_remind_time);
		timeBtn.getBackground().setColorFilter(0xff000000,
				PorterDuff.Mode.MULTIPLY);
		timeBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDialog(TIME_PICKER_DIALOG);
			}
		});

		Button saveBtn = (Button) findViewById(R.id.buttonSave);
		saveBtn.getBackground().setColorFilter(0xff000000,
				PorterDuff.Mode.MULTIPLY);
		saveBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// try{

				// setText("remind1+");

				System.out.println(" rowId : " + mrowId);
				saveState();
				System.out.println("Doc Alarm stared with rowId : " + mrowId);
				new reminderManager(SmartDocsActivity.this).setReminder(mrowId,
						mCalendar);
				// setResult(RESULT_OK);
				// Toast.makeText(SmartDocsActivity.this, "Doc Saved",
				// Toast.LENGTH_SHORT).show();
				// finish();

				/*
				 * File storageDir= new File(
				 * Environment.getExternalStoragePublicDirectory(
				 * Environment.DIRECTORY_PICTURES ), "myphoto.png");//
				 * getAlbumName() FileOutputStream out = new
				 * FileOutputStream(storageDir); //ObjectOutputStream oos =new
				 * ObjectOutputStream(out);
				 * 
				 * mImageBitmap.compress(Bitmap.CompressFormat.PNG,100 , out);
				 * out.flush(); out.close();
				 */

				// } catch (Exception ee){
				//
				// System.out.println("Exception Save " + ee.toString());

				// }

			}
		});

		b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// Start the camera.
				//orgTime = strTime;
				//orgDate = strDate;
				FromCamera = true;
				Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(i, 2);

			}
		});

	}

	private void saveState() {
		// Bitmap defaultImage;

		SimpleDateFormat dateTimeFormat = new SimpleDateFormat(DATE_TIME_FORMAT);
		String reminderDateTime = dateTimeFormat.format(mCalendar.getTime());

		// code to convert image to array of bytes
		if (mImageBitmap == null) {
			// mImageBitmap = (Bitmap) R.drawable.no_image_4;
			mImageBitmap = BitmapFactory.decodeResource(getResources(),
					R.drawable.no_image_4);

		}
		

		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		mImageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
		byte imageInByte[] = stream.toByteArray();
		String n = name.getEditableText().toString();
		String d = note.getEditableText().toString();
		String rdt = reminderDateTime.toString();//strDate+strTime;//
		System.out.println("###Inserting out: " + n + " " + d + " " + rdt + "mrowId "+ mrowId);

		if (mrowId == null) {
			System.out.println ( " mrowI d is null in function saveState() " );
			int id = mDbHelper.CreateDoc(n, d, rdt, imageInByte);
			if (id > 0) {
				mrowId = id;
			}
		} else {
			System.out.println ( " mrowId is not null in function saveState() " );
			mDbHelper.updateDoc(mrowId, n, d, rdt, imageInByte);
		}

		name.setText("");
		note.setText("");
		dateBtn.setText("Date");
		timeBtn.setText("Time");
	}

	private void handleSmallCameraPhoto(Intent imgIntent) {
		// System.out.println("Getting the image ... handleSmallCameraPhoto ");
		Bundle extras = imgIntent.getExtras();
		mImageBitmap = (Bitmap) extras.get("data");
		mImageView.setImageBitmap(mImageBitmap);
		mImageView.setVisibility(View.VISIBLE);
		
		//strDate = orgDate;
		//strTime = orgTime;
		
		
		//System.out.println("Original Date After Camera activity is " + strDate +" " + strTime);
		
		
		 //UpdateTimeButtonText();
		// UpdateDateButtonText();
		 

	}

	private void handleBigCameraPhoto() {
		// System.out.println("Getting the image ... handleBigCameraPhoto ...");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.smart_docs, menu);
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case ACTION_TAKE_PHOTO_B: {
			if (resultCode == RESULT_OK) {
				handleBigCameraPhoto();
			}
			break;
		} // ACTION_TAKE_PHOTO_B

		case ACTION_TAKE_PHOTO_S: {
			if (resultCode == RESULT_OK) {
				handleSmallCameraPhoto(data);
			}
			break;
		} // ACTION_TAKE_PHOTO_S

		} // switch
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_PICKER_DIALOG:
			return showDatePicker();

		case TIME_PICKER_DIALOG:
			return showTimePicker();
		}
		return super.onCreateDialog(id);
	}

	private TimePickerDialog showTimePicker() {
		TimePickerDialog timePicker;

		timePicker = new TimePickerDialog(this,
				new TimePickerDialog.OnTimeSetListener() {

					@Override
					public void onTimeSet(TimePicker view, int hourOfDay,
							int minute) {
						// TODO Auto-generated method stub
						mCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
						mCalendar.set(Calendar.MINUTE, minute);
						UpdateTimeButtonText();
					}
				}, mCalendar.get(Calendar.HOUR_OF_DAY),
				mCalendar.get(Calendar.MINUTE), false);

		return timePicker;
	}

	private void UpdateTimeButtonText() {
		SimpleDateFormat timeFormat = new SimpleDateFormat(TIME_FORMAT);
		String timeForButton = timeFormat.format(mCalendar.getTime());
		strTime = timeForButton;
		timeBtn.setText(timeForButton);
	}

	private DatePickerDialog showDatePicker() {
		DatePickerDialog datePicker;

		datePicker = new DatePickerDialog(SmartDocsActivity.this,
				new DatePickerDialog.OnDateSetListener() {

					@Override
					public void onDateSet(DatePicker view, int year,
							int monthOfYear, int dayOfMonth) {
						// TODO Auto-generated method stub
						mCalendar.set(Calendar.YEAR, year);
						mCalendar.set(Calendar.MONTH, monthOfYear);
						mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
						UpdateDateButtonText();

					}
				}, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH),
				mCalendar.get(Calendar.DAY_OF_MONTH));

		return datePicker;
	}

	private void UpdateDateButtonText() {
		SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);//
		String dateForButton = dateFormat.format(mCalendar.getTime());
		// dateForButton = dateFormat.format(mCalendar.getTime());
		strDate = dateForButton;
		dateBtn.setText(dateForButton);// dateFormat.format(mCalendar.getTime()));//dateForButton);

	}

	public int showItems(Cursor c) {
		Intent i = new Intent(this, ShowItemsActivity.class);
		startActivity(i);
		return 0;
	}

	private void setRowIdFromIntent() {
		if (mrowId == null) {
			Bundle extras = getIntent().getExtras();
			mrowId = extras != null ? extras.getInt(SmartDbaAdapter.KEY_ROWID)
					: null;
			FromList = extras != null ? extras
					.getBoolean("SmartDocsActivity.FromList") : false;

		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		mDbHelper.close();
	}

	private void populateFields() {
		SQLiteDatabase dBh = null;
		if (mrowId != null) {
			Cursor doc = mDbHelper.fetchDoc(mrowId, dBh);
			startManagingCursor(doc);
			name.setText(doc.getString(doc
					.getColumnIndexOrThrow(SmartDbaAdapter.KEY_TITLE)));
			note.setText(doc.getString(doc
					.getColumnIndexOrThrow(SmartDbaAdapter.KEY_BODY)));

			// code to populate the image
			byte[] outImage = doc.getBlob(doc
					.getColumnIndexOrThrow(SmartDbaAdapter.KEY_IMAGE));
			ByteArrayInputStream imageStream = new ByteArrayInputStream(
					outImage);
			Bitmap image = BitmapFactory.decodeStream(imageStream);
			mImageView.setImageBitmap(image);
			mImageView.setVisibility(View.VISIBLE);
			mImageBitmap = image;

			SimpleDateFormat dateTimeFormat = new SimpleDateFormat(
					DATE_TIME_FORMAT);
			Date date = null;
			try {
				String strDate1 = doc.getString(doc
						.getColumnIndexOrThrow(SmartDbaAdapter.KEY_DATE_TIME));
				date = dateTimeFormat.parse(strDate1);
				mCalendar.setTime(date);

			} catch (ParseException e) {
				Log.e("SmartDocActivity ", e.getMessage(), e);
			}
			UpdateDateButtonText();
			UpdateTimeButtonText();
		}
		//UpdateDateButtonText();
		//UpdateTimeButtonText();
		if (dBh != null) {
			dBh.close();
		}

	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if(FromCamera)
		{
			outState.putString("Date", strDate);
			outState.putString("Time", strTime);
			System.out.println("### "+strDate +strTime);
		}
		
		
		if (mrowId != null) {
			outState.putInt(SmartDbaAdapter.KEY_ROWID, mrowId);
		}
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle outState)
	{
		super.onRestoreInstanceState(outState);
		
		if(FromCamera)
		{
		 FromCamera = false;
		
		strDate = outState.getString("Date");
		 strTime = outState.getString("Time");
		
		System.out.println("Restored Date :" + strDate +" Time: " +strTime);
		
		strDate = strDate+" "+strTime+":00";
		
		SimpleDateFormat dateTimeFormat = new SimpleDateFormat(
				DATE_TIME_FORMAT);
		Date date;
		try{
		date = dateTimeFormat.parse(strDate);
		mCalendar.setTime(date);
		}catch(Exception e){ System.out.println("RestoreD parsing error" + e);}
		
		SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);//
		String dateForButton = dateFormat.format(mCalendar.getTime());
		dateBtn.setText(dateForButton);
		
		SimpleDateFormat timeFormat = new SimpleDateFormat(TIME_FORMAT);//
		String timeForButton = timeFormat.format(mCalendar.getTime());
		timeBtn.setText(timeForButton);
		
		}
		//UpdateDateButtonText();
		//UpdateTimeButtonText();
		
	}

	@Override
	protected void onResume() {
		super.onResume();

		setRowIdFromIntent();
		
		
		if (FromList == true) {
			mDbHelper.open();
			populateFields();
		}

	}

}
