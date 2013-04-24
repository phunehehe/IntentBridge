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
import android.view.Menu;

public class MainActivity extends Activity {

	private static void log(String message) {
		Log.d("IB", message);
	}

	private Serializable parseExtra(String type, Object value) {
		if (type == "android.net.Uri[]") {
			List<Uri> values = new ArrayList<Uri>();
			for (String v : (Iterable<String>) value) {
				values.add(Uri.parse(v));
			}
			return values.toArray();
		}
		return null;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Uri data = getIntent().getData();
		if (data == null) {
			log("You are not supposed to use me this way.");
			return;
		}
		List<String> segments = new ArrayList<String>();
		for (String segment : data.getPathSegments()) {
			try {
				String result = URLDecoder.decode(segment, "UTF-8");
				log(result);
				segments.add(result);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		String action = segments.get(0);
		Intent intent = new Intent(action);

		Uri videoUri = Uri.parse(segments.get(1));
		intent.setData(videoUri);

		try {
			JSONArray extras = new JSONArray(segments.get(2));
			for (int i = 0; i < extras.length(); i++) {
				JSONObject extra = extras.getJSONObject(i);
				String name = extra.getString("name");
				Serializable value = parseExtra(extra.getString("type"),
						extra.get("value"));
				intent.putExtra(name, value);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
