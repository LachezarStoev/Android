package Database;


import com.example.quickmail.R;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MessagesAdapter extends CursorAdapter {
	
	public MessagesAdapter(Context context, Cursor c) {
		super(context, c,false);
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		ViewHolder viewHolder=(ViewHolder)view.getTag();
		viewHolder.mail.setText("to: " + cursor.getString(cursor.getColumnIndex("mail")));
		viewHolder.title.setText("title: " + cursor.getString(cursor.getColumnIndex("title")));
		viewHolder.desc.setText("description: " + cursor.getString(cursor.getColumnIndex("description")));
		viewHolder.time.setText("sent: " + cursor.getString(cursor.getColumnIndex("time")));
	}

	@Override
	public View newView(Context context, Cursor arg1, ViewGroup viewGroup) {
		View view=LayoutInflater.from(context).inflate(R.layout.messagelayout, viewGroup, false);
		ViewHolder viewHolder=new ViewHolder();
		viewHolder.mail=(TextView)view.findViewById(R.id.mail);
		viewHolder.title=(TextView)view.findViewById(R.id.title);
		viewHolder.desc=(TextView)view.findViewById(R.id.description);
		viewHolder.time=(TextView)view.findViewById(R.id.time);
		view.setTag(viewHolder);
		return view;
	}
	public class ViewHolder {
			TextView mail;
			TextView title;
			TextView desc;
			TextView time;
	}

}
