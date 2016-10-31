package com.shrappz.contactsharer;

import android.content.ContentProviderOperation;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.RawContacts;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.shrappz.contactsharer.utils.Global;

import java.util.ArrayList;

public class Add extends AppCompatActivity {
    EditText name;
    EditText number;
    private Button btn_save;

//TODO Save Contact -- fin add some design to button

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        startActivityForResult(new Intent(this, CameraScannerActivity.class), 0);
        init();
        listeners();
        setUpActionBar();
    }

    public void init() {
        name = (EditText) findViewById(R.id.edt_name);
        number = (EditText) findViewById(R.id.edt_phone);
        btn_save = (Button) findViewById(R.id.btn_save);
    }

    public void listeners() {

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                writeContacts(name.getText().toString(), number.getText().toString());
            }
        });
    }

    public void setUpActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
    }


    public void writeContacts(String name, String number) {
        ArrayList contentProviderOperations = new ArrayList();

        contentProviderOperations.add(ContentProviderOperation.newInsert(RawContacts.CONTENT_URI)
                .withValue(RawContacts.ACCOUNT_TYPE, null).withValue(RawContacts.ACCOUNT_NAME, null).build());

        contentProviderOperations.add(ContentProviderOperation.newInsert(Data.CONTENT_URI)
                .withValueBackReference(Data.RAW_CONTACT_ID, 0).withValue(Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                .withValue(StructuredName.DISPLAY_NAME, name).build());

        contentProviderOperations.add(ContentProviderOperation.newInsert(Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0).withValue(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE)
                .withValue(Phone.NUMBER, number).withValue(Phone.TYPE, Phone.TYPE_MOBILE).build());
        try {
            getApplicationContext().getContentResolver().
                    applyBatch(ContactsContract.AUTHORITY, contentProviderOperations);
            AlertDialog.Builder builder = Global.alerter(Add.this, "Contact Saved Successfully");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                }
            });
            builder.show();
        } catch (RemoteException e) {
            e.printStackTrace();
            Toast.makeText(this, "Problem occoured while saving contact", Toast.LENGTH_SHORT).show();
            finish();
        } catch (OperationApplicationException e) {
            e.printStackTrace();
            Toast.makeText(this, "Problem occoured while saving contact", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                name.setText(data.getStringExtra("name"));
                number.setText(data.getStringExtra("number"));
            }
            if (resultCode == RESULT_CANCELED) {
                //handle cancel
                finish();
                //Toast.makeText(this, "Error Occoured Please Try Again", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
