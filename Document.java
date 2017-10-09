package android.froyo.smartdocs;

import java.util.Date;
import android.froyo.smartdocs.*;
import android.net.Uri;

public class Document {

	/*
	 * The type describes type of the element e.g Electric bill, phone bill ...
	 * Name is any string e.g Elec_bill_1_Apr_13 which should make it easier to
	 * search.
	 */
	private String type;
	private String name;

	/*
	 * URI is an important field and actually mentions the name of the file.
	 * path and URI will combine to give location of the picture.
	 */
	private Uri fileUri;
	private String path;

	/*
	 * Alarm is an instance of the alarm on the document. If there is an alarm
	 * associated with a document e.g elec bill pay date then this is populated.
	 * It can be checked by checking the alarmSet field. forgeinKey . -1 => no
	 * alarm set.
	 */
	private int alarmId;

	/*
	 * Due date is the date on which a bill is due e.g elec bill due date.
	 * expiryDate is a date which contains the date till which a document is
	 * valid, e.g. a license or a passport or so on.....
	 */
	private Date dueDate;
	private Date exipryDate;

	/**
	 * Could be used in future. Commented since we dont want to have longer
	 * classes and for footprint requirement. Time createTime ; Time lastUpdated
	 * ;
	 */
	/*
	 * Gives the document and bunch that it is associated with. bunchId of 0 =>
	 * stand alone document.
	 */
	private int docId;
	private int bunchId;

	/*
	 * No need at present String desc ;
	 */

	public Document() {
		// TODO Auto-generated constructor stub
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Uri getFileUri() {
		return fileUri;
	}

	public void setFileUri(Uri fileUri) {
		this.fileUri = fileUri;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public int getAlarmId() {
		return alarmId;
	}

	public void setAlarm(int id) {
		this.alarmId = id;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public Date getExipryDate() {
		return exipryDate;
	}

	public void setExipryDate(Date exipryDate) {
		this.exipryDate = exipryDate;
	}

	public int getDocId() {
		return docId;
	}

	public void setDocId(int docId) {
		this.docId = docId;
	}

	public int getBunchId() {
		return bunchId;
	}

	public void setBunchId(int bunchId) {
		this.bunchId = bunchId;
	}

	public int Update() {
		return 0;
	}

	public int Delete() {
		return 0;
	}

	public int View() {
		return 0;
	}
}
