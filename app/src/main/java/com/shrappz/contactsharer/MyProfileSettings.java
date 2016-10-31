package com.shrappz.contactsharer;


import android.app.ProgressDialog;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.zxing.WriterException;
import com.shrappz.contactsharer.utils.Global;
import com.shrappz.contactsharer.utils.PrefConnector;

public class MyProfileSettings extends PreferenceActivity implements Preference.OnPreferenceChangeListener, SharedPreferences.OnSharedPreferenceChangeListener {

    String header_info = "BEGIN:VCARD"; //N: is the prefix for MECARD Name
    String footer_info = "END:VCARD"; //N: is the prefix for MECARD Name

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout root = (LinearLayout) findViewById(android.R.id.list).getParent().getParent().getParent();
        Toolbar bar = (Toolbar) LayoutInflater.from(this).inflate(R.layout.toolbar, root, false);
        bar.setTitleTextColor(getResources().getColor(R.color.white));
        root.addView(bar, 0); // insert at top
        bar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        addPreferencesFromResource(R.xml.pref_general);
        bindPreferenceSummaryToValue(findPreference(getString(R.string.user_name)));
        bindPreferenceSummaryToValue(findPreference(getString(R.string.user_mobile)));
        bindPreferenceSummaryToValue(findPreference(getString(R.string.user_email)));
    }

    private void bindPreferenceSummaryToValue(Preference preference) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(this);

        // Trigger the listener immediately with the preference's
        // current value.
        onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object o) {
        String stringValue = o.toString();
        if (preference instanceof ListPreference) {
            // For list preferences, look up the correct display value in
            // the preference's 'entries' list (since they have separate labels/values).
            ListPreference listPreference = (ListPreference) preference;
            int prefIndex = listPreference.findIndexOfValue(stringValue);
            if (prefIndex >= 0) {
                preference.setSummary(listPreference.getEntries()[prefIndex]);
            }
        } else {
            // For other preferences, set the summary to the value's simple string representation.
            preference.setSummary(stringValue);
        }

        return true;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        String name = "N:", phone_number = "TEL:", email = "EMAIL:";
        name += PrefConnector.readString(MyProfileSettings.this, getString(R.string.user_name));
        phone_number += PrefConnector.readString(MyProfileSettings.this, getString(R.string.user_mobile));
        email += PrefConnector.readString(MyProfileSettings.this, getString(R.string.user_email));
        String qrcodetext = String.format("%s%n%s%n%s%n%s%n%s%n",
                header_info, name, phone_number, email, footer_info);
        new LoadQrCode().execute(qrcodetext);

    }

    @Override
    protected void onResume() {
        super.onResume();
        // Set up a listener whenever a key changes
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Unregister the listener whenever a key changes
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    class LoadQrCode extends AsyncTask<String, Void, Void> {

        final ProgressDialog progressDialog = new ProgressDialog(MyProfileSettings.this);
        Bitmap bitmap = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Please Wait");
            progressDialog.setCancelable(false);
            progressDialog.show();

        }


        @Override
        protected Void doInBackground(String... strings) {
            try {
                bitmap = Global.encodeAsBitmap(strings[0], MyProfileSettings.this);
            } catch (WriterException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();

            Global.updateWidget(MyProfileSettings.this);
            PrefConnector.writeSting(MyProfileSettings.this, getString(R.string.key_qr_path),
                    Global.saveToInternalStorage(MyProfileSettings.this, bitmap));
            PrefConnector.writeBoolean(MyProfileSettings.this, getString(R.string.key_first_open), true);
            Intent intent = new Intent(MyProfileSettings.this, MyContactWidget.class);
            intent.setAction("android.appwidget.action.APPWIDGET_UPDATE");
            int ids[] = AppWidgetManager.getInstance(getApplication()).getAppWidgetIds(new ComponentName(getApplication(), MyContactWidget.class));
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
            sendBroadcast(intent);
            Toast.makeText(MyProfileSettings.this, "Successfully Updated widget data", Toast.LENGTH_SHORT).show();
        }
    }
}
