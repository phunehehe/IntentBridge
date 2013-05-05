package com.cogini.intentbridge;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends Activity {

	private static int ACTION = 1;
	private static int TARGET = 2;
	private static int EXTRAS = 3;

	private static void log(String message) {
		Log.d("IB", message);
	}

	private Serializable parseExtra(String type, Object value)
			throws JSONException {
		if (type.equals("android.net.Uri[]")) {
			List<Uri> values = new ArrayList<Uri>();
			JSONArray v = (JSONArray) value;
			for (int i = 0; i < v.length(); i++) {
				values.add(Uri.parse(v.getString(i)));
			}
			return values.toArray(new Uri[0]);
		}
		return null;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Uri data = getIntent().getData();
		if (data == null) {
			Toast.makeText(getApplicationContext(),
					"You are not supposed to run me directly.",
					Toast.LENGTH_SHORT).show();
			finish();
			return;
		}

		List<String> segments = new ArrayList<String>();
		for (String segment : data.getPathSegments()) {
			try {
				String result = URLDecoder.decode(segment, "UTF-8");
				segments.add(result);
			} catch (UnsupportedEncodingException e) {
				Toast.makeText(getApplicationContext(),
						"Could not decode " + segment, Toast.LENGTH_SHORT)
						.show();
				finish();
				return;
			}
		}

		int segmentSize = segments.size();

		// One more segment for the path filter
		if (segmentSize < ACTION + 1) {
			Toast.makeText(
					getApplicationContext(),
					"I need at least one parameter for the action. None found.",
					Toast.LENGTH_SHORT).show();
			finish();
			return;
		}

		String action = segments.get(ACTION);
		Intent intent = new Intent(action);

		if (segmentSize > TARGET) {
			Uri uri = Uri.parse(segments.get(TARGET));
			intent.setData(uri);
		}

		if (segmentSize > EXTRAS) {
			try {
				JSONArray extras = new JSONArray(segments.get(EXTRAS));
				for (int i = 0; i < extras.length(); i++) {
					JSONObject extra = extras.getJSONObject(i);
					String name = extra.getString("name");
					Serializable value = parseExtra(extra.getString("type"),
							extra.get("value"));
					intent.putExtra(name, value);
				}
			} catch (JSONException e) {
				Toast.makeText(getApplicationContext(),
						"Could not parse extras.", Toast.LENGTH_SHORT).show();
				finish();
				return;
			}
		}

		startActivity(intent);
		finish();
	}
}
