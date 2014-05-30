package com.example.quickmail;

import Database.DBHelper;
import Database.DBMessages;
import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

public class QuickMailApplication extends Application {
	private SQLiteDatabase db = null;
	private DBMessages dbMessages = null;

	public SQLiteDatabase getDB() {
		if (db == null) {
			db = new DBHelper(getApplicationContext()).open();
		}
		return db;
	}

	public DBMessages getDBMessages() {
		if (dbMessages == null) {
			dbMessages = new DBMessages(getDB());
		}
		return dbMessages;
	}
}
