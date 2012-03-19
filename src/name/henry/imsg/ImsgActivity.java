package name.henry.imsg;

import name.henry.imsg.R;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ImsgActivity extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		final Button b = (Button) findViewById(R.id.btnMsg);
		b.setEnabled(false);
		b.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				EditText et = (EditText) findViewById(R.id.from);
				String from = et.getText().toString();
				et = (EditText) findViewById(R.id.sms);
				String sms = et.getText().toString();
				smsInbox(getContentResolver(), from, sms);
				refreshInbox(getContentResolver());
				Toast.makeText(ImsgActivity.this, R.string.sent, Toast.LENGTH_SHORT).show();
			}

		});
		final EditText from = (EditText) findViewById(R.id.from);
		from.setHint(R.string.from);
		final EditText sms = (EditText) findViewById(R.id.sms);
		sms.setHint(R.string.sms);
		TextWatcher tw = new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence paramCharSequence,
					int paramInt1, int paramInt2, int paramInt3) {
			}

			@Override
			public void onTextChanged(CharSequence paramCharSequence,
					int paramInt1, int paramInt2, int paramInt3) {
				updateButton(b, from, sms);
			}

			@Override
			public void afterTextChanged(Editable paramEditable) {
			}
		};
		from.addTextChangedListener(tw);
		sms.addTextChangedListener(tw);
	}

	private void updateButton(Button b, EditText from, EditText sms) {
		String strFrom = from.getText().toString();
		String strSms = sms.getText().toString();

		boolean e = "".equals(strFrom) || "".equals(strSms);
		b.setEnabled(!e);
	}

	private void refreshInbox(ContentResolver cr) {
		Uri inbox = Uri.parse("content://sms/inbox");
		Cursor c = cr.query(inbox, null, null, null, null);
		if (null != c) {
			try {
				c.moveToNext();
			} finally {
				c.close();
			}
		}
	}

	private void smsInbox(ContentResolver cr, String from, String sms) {
		ContentValues values = new ContentValues();
		values.put("address", from);
		values.put("body", sms);
		Uri inbox = Uri.parse("content://sms/inbox");
		cr.insert(inbox, values);
	}
}