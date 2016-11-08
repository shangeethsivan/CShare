package com.shrappz.qrcontact;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.zxing.WriterException;
import com.shrappz.qrcontact.Adapters.ContactLoadAdapter;
import com.shrappz.qrcontact.Models.Item;
import com.shrappz.qrcontact.utils.Global;

import java.util.ArrayList;

import io.fabric.sdk.android.Fabric;


public class SendActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView listView;
    private ArrayList<Item> items;
    private ImageView qrCodeImageview;
    TextView qrTitle;
    Dialog dialog;
    private ContactLoadAdapter adapter;
    String header_info = "BEGIN:VCARD"; //N: is the prefix for MECARD Name
    String footer_info = "END:VCARD"; //N: is the prefix for MECARD Name
    private final int REQUEST_CODE_ASK_PERMISSIONS = 0;
    Boolean permission = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_send);

       // Ads
        MobileAds.initialize(getApplicationContext(), "ca-app-pub-3701495917205916~3425272185");

        AdView mAdView = (AdView) findViewById(R.id.adView);
       AdRequest adRequest = new AdRequest.Builder().build();
       //test AdRequest request = new AdRequest.Builder()   // All emulators
       //         .addTestDevice("")  // An example device ID
        //        .build();
        mAdView.loadAd(adRequest);

        dialog = new Dialog(SendActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_qrcode);
        dialog.getWindow().setLayout(500, 550);
        dialog.getWindow().setGravity(Gravity.CENTER);
        qrCodeImageview = (ImageView) dialog.findViewById(R.id.img_qr_code_image);
        qrTitle = (TextView) dialog.findViewById(R.id.qr_title);
        items = new ArrayList<>();
        listView = (ListView) findViewById(R.id.listview);
        loadContacts();
        listView.setOnItemClickListener(this);
        setUpActionBar();
        View parentLayout = findViewById(R.id.activity_send);
        Snackbar snackbar = Snackbar
                .make(parentLayout, "Please click on a contact to share.", Snackbar.LENGTH_LONG);

        snackbar.show();
    }

    public void setUpActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
    }

    public void loadContacts() {
        getAllContacts(this.getContentResolver());
        adapter = new ContactLoadAdapter(SendActivity.this, R.layout.list_contacts, items);
        listView.setAdapter(adapter);
    }

    public void getAllContacts(ContentResolver cr) {
        Cursor phones = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
        while (phones.moveToNext()) {
            String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            System.out.println(".................." + phoneNumber);
            items.add(new Item(name, phoneNumber));
        }
        phones.close();
    }

    /*BEGIN:VCARD
    VERSION:3.0
    N:test
    ORG:test
    TEL:9159816148
    END:VCARD
    */
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        String name = "N:", phone_number = "TEL:";
        qrTitle.setText(items.get(i).getName());
        name += items.get(i).getName();
        phone_number += items.get(i).getNumber();

        String qrcodetext = String.format("%s%n%s%n%s%n%s%n", header_info, name, phone_number, footer_info);

        new LoadQrCode().execute(qrcodetext);
        //loadQr(qrcodetext);
    }

    class LoadQrCode extends AsyncTask<String, Void, Void> {

        final ProgressDialog progressDialog = new ProgressDialog(SendActivity.this);
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
                bitmap = Global.encodeAsBitmap(strings[0], SendActivity.this);
            } catch (WriterException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
            dialog.show();
            qrCodeImageview.setImageBitmap(bitmap);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.send_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = null;
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

            SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextChange(String newText) {
                    Log.i("onQueryTextChange", newText);
                    adapter.filter(newText);
                    return true;
                }

                @Override
                public boolean onQueryTextSubmit(String query) {
                    Log.i("onQueryTextSubmit", query);

                    return true;
                }
            };
            searchView.setOnQueryTextListener(queryTextListener);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}