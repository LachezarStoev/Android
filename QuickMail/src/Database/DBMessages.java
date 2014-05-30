package Database;



import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DBMessages {
	private static final String TABLE_NAME = "messages";
	private SQLiteDatabase db;

	public DBMessages(SQLiteDatabase db) {
		this.db = db;
	}

	public void addMessage(String mail, String title, String description,
			String time) {
		if (!mail.contains("@")) {
			return;
		}
		ContentValues vals = new ContentValues();
		vals.put("mail", mail);
		vals.put("title", title);
		vals.put("description", description);
		vals.put("time", time);
		db.insert(TABLE_NAME, null, vals);
	}

	public void deleteMessage(long id) {
		db.delete(TABLE_NAME, "_id=?", new String[] { String.valueOf(id) });
	}

	public Cursor getMessagesCursor() {
		return db.query(TABLE_NAME, new String[] { "_id", "mail", "title",
				"description", "time" }, null, null, null, null, null);
	}

	public String[] getAllSentMails() {
		Cursor cursor = db.query(TABLE_NAME, new String[] { "mail" }, null,
				null, null, null, null);
		String[] mails = new String[cursor.getCount()];
		int index = 0;
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			mails[index] = cursor.getString(cursor.getColumnIndex("mail"));
			index++;
			cursor.moveToNext();
		}
		return mails;

	}

	public void deleteAllMessages() {
		db.delete(TABLE_NAME, null, null);
	}
}
