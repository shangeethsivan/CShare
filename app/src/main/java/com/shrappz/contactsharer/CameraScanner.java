package com.shrappz.contactsharer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.zxing.client.result.AddressBookParsedResult;

import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;

public class CameraScanner extends AppCompatActivity implements ZBarScannerView.ResultHandler {

    private ZBarScannerView mScannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mScannerView = new ZBarScannerView(this);   // Programmatically initialize the scanner view
        setContentView(mScannerView);
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
            AddressBookParsedResult addressBookParsedResult = VCardParser.parseData(result);
            Intent returnIntent = new Intent();
            returnIntent.putExtra("name", addressBookParsedResult.getNames()[0]);
            returnIntent.putExtra("number", addressBookParsedResult.getPhoneNumbers()[0]);
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
            Log.d("Add.java", addressBookParsedResult.getPhoneNumbers()[0]);
            Log.d("Add.java", addressBookParsedResult.getNames()[0]);
        }
        Log.v("Add.java", result.getContents());
        Log.v("Add.java", result.getBarcodeFormat().getName());
    }
}
