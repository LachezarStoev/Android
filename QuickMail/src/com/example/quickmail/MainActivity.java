package com.example.quickmail;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.ListIterator;
import java.util.Locale;

import Database.DBHelper;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {
	public static int VOICECODE = 1;
	private SimpleDateFormat time;
	private Date date;
	private AutoCompleteTextView mailText;
	private EditText titleText;
	private EditText descText;
	private Button sendButton;
	private Button voiceButton;
	private String[] mailsFromDB;
	private ArrayAdapter<String> autoMailsAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mail_screen);
		mailText = (AutoCompleteTextView) findViewById(R.id.mail);
		titleText = (EditText) findViewById(R.id.title);
		descText = (EditText) findViewById(R.id.description);
		sendButton = (Button) findViewById(R.id.send);
		voiceButton = (Button) findViewById(R.id.voice);
		sendButton.setOnClickListener(this);
		voiceButton.setOnClickListener(this);
		mailText.requestFocus();
		time = new SimpleDateFormat("MMMM/d/yyyy hh:mm:ss");
		mailsFromDB = ((QuickMailApplication) getApplication()).getDBMessages()
				.getAllSentMails();
		mailsFromDB = makeSet(mailsFromDB);
		autoMailsAdapter = new ArrayAdapter<String>(this,
				R.layout.autocompletelist, mailsFromDB);
		mailText.setAdapter(autoMailsAdapter);
		mailText.setThreshold(1);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == VOICECODE) {
			if (resultCode == RESULT_OK) {
				ArrayList<String> results = data
						.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
				int focusId = this.getCurrentFocus().getId();
				if (focusId == mailText.getId()) {
					mailText.setText(results.get(0));
				} else if (focusId == titleText.getId()) {
					titleText.setText(results.get(0));
				} else if (focusId == descText.getId()) {
					descText.setText(results.get(0));
				}
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.allMessages) {
			Intent i = new Intent(this, MessagesActivity.class);
			startActivity(i);
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == sendButton.getId()) {
			String mail = mailText.getText().toString();
			String title = titleText.getText().toString();
			String description = descText.getText().toString();

			String toSend[] = { mail };
			Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
			emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, toSend);
			emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, title);
			emailIntent.setType("plain/text");
			emailIntent
					.putExtra(android.content.Intent.EXTRA_TEXT, description);
			startActivity(emailIntent);
			date = new Date();
			((QuickMailApplication) getApplication()).getDBMessages()
					.addMessage(mail, title, description, time.format(date));

			mailsFromDB = addDynamicallyToAutoComplete(mail);
			mailsFromDB = makeSet(mailsFromDB);
			autoMailsAdapter = new ArrayAdapter<String>(this,
					R.layout.autocompletelist, mailsFromDB);
			mailText.setAdapter(autoMailsAdapter);

		} else if (v.getId() == voiceButton.getId()) {
			Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
			i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
					RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
			i.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak up!");
			startActivityForResult(i, VOICECODE);
		}

	}

	public String[] addDynamicallyToAutoComplete(String mail) {
		if (!mail.contains("@")) {
			return mailsFromDB;
		}
		String[] result = new String[mailsFromDB.length + 1];
		for (int i = 0; i < mailsFromDB.length; i++) {
			result[i] = mailsFromDB[i];
		}
		result[mailsFromDB.length] = mail;
		return result;
	}

	public String[] makeSet(String[] mails) {
		ArrayList<String> list = new ArrayList<String>();

		for (int i = 0; i < mails.length; i++) {
			boolean isUnique = true;
			for (int j = i + 1; j < mails.length; j++) {
				if (mails[i].equals(mails[j])) {
					isUnique = false;
				}
			}
			if (isUnique) {
				list.add(mails[i]);
			}
		}
		String[] uniqueStrings = new String[list.size()];
		for (int i = 0; i < list.size(); i++) {
			uniqueStrings[i] = list.get(i);
		}
		return uniqueStrings;
	}
}
