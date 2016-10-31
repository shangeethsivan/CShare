package com.shrappz.contactsharer;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.zxing.client.result.AddressBookParsedResult;
import com.shrappz.contactsharer.utils.VCardParser;

import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;

public class CameraScannerActivity extends AppCompatActivity implements ZBarScannerView.ResultHandler {

    private ZBarScannerView mScannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mScannerView = new ZBarScannerView(this);   // Programmatically initialize the scanner view
        setContentView(mScannerView);
        setUpActionBar();
    }

    public void setUpActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result result) {

        if (result.getBarcodeFormat().getName().equalsIgnoreCase("qrcode")) {
            if (result.getContents().contains("BEGIN:VCARD")) {
                try {
                    MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.alert);
                    mediaPlayer.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                AddressBookParsedResult addressBookParsedResult = VCardParser.parseData(result);
                Intent returnIntent = new Intent();
                returnIntent.putExtra("name", addressBookParsedResult.getNames()[0]);
                returnIntent.putExtra("number", addressBookParsedResult.getPhoneNumbers()[0]);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
                Log.d("Add.java", addressBookParsedResult.getPhoneNumbers()[0]);
                Log.d("Add.java", addressBookParsedResult.getNames()[0]);
            } else {
                Toast.makeText(this, "Sorry I only scan Contacts", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(CameraScannerActivity.this, HomeActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        }
    }

}
