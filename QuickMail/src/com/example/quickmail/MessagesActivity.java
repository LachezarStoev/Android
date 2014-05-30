package com.example.quickmail;

import Database.DBMessages;
import Database.MessagesAdapter;
import Database.SimpleCursorLoader;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class MessagesActivity extends FragmentActivity implements
		LoaderCallbacks<Cursor>, OnItemLongClickListener, OnClickListener,
		OnItemClickListener, android.view.View.OnClickListener {
	private ListView messageList;
	private Button delAllButton;
	private AlertDialog delAllDialog;
	private AlertDialog delMessageDialog;
	private MessagesAdapter mAdapter;
	private static final int MAIL_LOADER = 1;
	private long positionOfList;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.messages);
		mAdapter = new MessagesAdapter(this, null);
		delAllButton = (Button) findViewById(R.id.delAll);
		delAllButton.setOnClickListener(this);
		messageList = (ListView) findViewById(R.id.allMessages);
		messageList.setOnItemClickListener(this);
		messageList.setOnItemLongClickListener(this);
		messageList.setAdapter(mAdapter);
		getSupportLoaderManager().initLoader(MAIL_LOADER, null, this);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2,
			final long id) {
		positionOfList = id;
		AlertDialog.Builder aDialog = new AlertDialog.Builder(this);
		aDialog.setMessage("Do you want to delete this?");
		aDialog.setPositiveButton("yes", this);
		aDialog.setNegativeButton("no", null);
		delMessageDialog = aDialog.create();
		delMessageDialog.show();
		return false;
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == delAllButton.getId()) {
			AlertDialog.Builder aDialog = new AlertDialog.Builder(this);
			aDialog.setMessage("Do you want to delete all messages?");
			aDialog.setPositiveButton("yes", this);
			aDialog.setNegativeButton("no", null);
			delAllDialog = aDialog.create();
			delAllDialog.show();
		}

	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		if (dialog == delMessageDialog) {
			if (which == AlertDialog.BUTTON_POSITIVE) {
				((QuickMailApplication) getApplication()).getDBMessages()
						.deleteMessage(positionOfList);

			}
		} else if (dialog == delAllDialog) {
			if (which == AlertDialog.BUTTON_POSITIVE) {
				((QuickMailApplication) getApplication()).getDBMessages()
						.deleteAllMessages();
			}
		}
		getSupportLoaderManager().restartLoader(MAIL_LOADER, null, this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
		TextView title = (TextView) view.findViewById(R.id.title);
		TextView desc = (TextView) view.findViewById(R.id.description);
		if (title.getVisibility() == View.VISIBLE) {
			title.setVisibility(View.GONE);
			desc.setVisibility(View.GONE);
		} else {
			title.setVisibility(View.VISIBLE);
			desc.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		return new MailsCursorLoader(this,
				((QuickMailApplication) getApplication()).getDBMessages());
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor cursor) {
		mAdapter.swapCursor(cursor);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		mAdapter.swapCursor(null);

	}

	public static final class MailsCursorLoader extends SimpleCursorLoader {
		private DBMessages dbMessages;

		public MailsCursorLoader(Context context, DBMessages dbMessages) {
			super(context);
			this.dbMessages = dbMessages;
		}

		@Override
		public Cursor loadInBackground() {
			return dbMessages.getMessagesCursor();
		}
	}

}
