package com.shrappz.contactsharer;

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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.shrappz.contactsharer.Adapters.ContactLoadAdapter;
import com.shrappz.contactsharer.Models.Item;

import java.util.ArrayList;

public class Send extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView listView;
    private ArrayList<Item> items;
    private ImageView qrCodeImageview;
    public final static int WIDTH = 500;
    public final static int HEIGHT = 500;
    Dialog dialog;
    private ContactLoadAdapter adapter;
    String header_info = "BEGIN:VCARD"; //N: is the prefix for MECARD Name
    String footer_info = "END:VCARD"; //N: is the prefix for MECARD Name


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);
        dialog = new Dialog(Send.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_qrcode);
        dialog.getWindow().setLayout(400, 430);
        qrCodeImageview = (ImageView) dialog.findViewById(R.id.img_qr_code_image);
        items = new ArrayList<>();
        getAllContacts(this.getContentResolver());
        listView = (ListView) findViewById(R.id.listview);
        adapter = new ContactLoadAdapter(Send.this, R.layout.list_contacts, items);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
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
        name += items.get(i).getName();
        phone_number += items.get(i).getNumber();

        String qrcodetext = String.format("%s%n%s%n%s%n%s%n", header_info, name, phone_number, footer_info);

        new LoadQrCode().execute(qrcodetext);
        //loadQrcode(qrcodetext);
    }


    class LoadQrCode extends AsyncTask<String, Void, Void> {

        final ProgressDialog progressDialog = new ProgressDialog(Send.this);
        Bitmap bitmap = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Please Wait");
            progressDialog.setCancelable(false);
            progressDialog.show();

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
            dialog.show();
            qrCodeImageview.setImageBitmap(bitmap);
        }

        @Override
        protected Void doInBackground(String... strings) {
            try {
                bitmap = encodeAsBitmap(strings[0]);
            } catch (WriterException e) {
                e.printStackTrace();
            }
            return null;
        }

    }

    void loadQrcode(final String qrtext) {
        final ProgressDialog progressDialog = new ProgressDialog(Send.this);
        progressDialog.setMessage("Please Wait");
        progressDialog.setCancelable(false);
        progressDialog.show();

// create thread to avoid ANR Exception
        Thread t = new Thread(new Runnable() {
            public void run() {
// this is the msg which will be encode in QRcode
                try {
                    synchronized (this) {
                        wait(2000);
// runOnUiThread method used to do UI task in main thread.
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Bitmap bitmap = null;
                                    bitmap = encodeAsBitmap(qrtext);
                                    qrCodeImageview.setImageBitmap(bitmap);
                                    progressDialog.dismiss();
                                    dialog.show();
                                } catch (WriterException e) {
                                    e.printStackTrace();
                                } // end of catch block

                            } // end of run method
                        });
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


            }
        });
        t.start();
    }

    // this is method call from on create and return bitmap image of QRCode.
   Bitmap encodeAsBitmap(String str) throws WriterException {
        BitMatrix result;
        try {
            result = new MultiFormatWriter().encode(str,
                    BarcodeFormat.QR_CODE, WIDTH, HEIGHT, null);
        } catch (IllegalArgumentException iae) {
            // Unsupported format
            return null;
        }
        int w = result.getWidth();
        int h = result.getHeight();
        int[] pixels = new int[w * h];
        for (int y = 0; y < h; y++) {
            int offset = y * w;
            for (int x = 0; x < w; x++) {
                pixels[offset + x] = result.get(x, y) ? getResources().getColor(R.color.black) : getResources().getColor(R.color.white);
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, 500, 0, 0, w, h);
        return bitmap;
    } /// end of this method

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
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}