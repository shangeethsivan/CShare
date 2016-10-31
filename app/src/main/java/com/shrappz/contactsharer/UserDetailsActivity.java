package com.shrappz.contactsharer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.zxing.WriterException;
import com.shrappz.contactsharer.utils.Global;
import com.shrappz.contactsharer.utils.PrefConnector;

public class UserDetailsActivity extends AppCompatActivity {

    private EditText edt_moblie, edt_email, edt_name;
    Button btn_save;

    String header_info = "BEGIN:VCARD"; //N: is the prefix for MECARD Name
    String footer_info = "END:VCARD"; //N: is the prefix for MECARD Name

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);
        init();
        listeners();
    }

    public void init() {
        edt_name = (EditText) findViewById(R.id.edt_name);
        edt_moblie = (EditText) findViewById(R.id.edt_phone);
        edt_email = (EditText) findViewById(R.id.edt_email);
        btn_save = (Button) findViewById(R.id.btn_save);
    }

    public void listeners() {
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edt_name.getText().toString().equalsIgnoreCase("")) {
                    edt_name.setError("Enter Name First");
                } else if (edt_moblie.getText().toString().equalsIgnoreCase("")) {
                    edt_name.setError("Enter Mobile Number");
                } else if (edt_email.getText().toString().equalsIgnoreCase("")) {
                    edt_email.setError("Enter Email id");
                } else if (!Global.isValidEmail(edt_email.getText().toString())) {
                    edt_email.setError("Email id Invaild");
                } else {
                    String name = "N:", phone_number = "TEL:", email = "EMAIL:";
                    name += edt_name.getText().toString();
                    phone_number += edt_moblie.getText().toString();
                    email += edt_email.getText().toString();
                    String qrcodetext = String.format("%s%n%s%n%s%n%s%n%s%n",
                            header_info, name, phone_number, email, footer_info);

                    new LoadQrCode().execute(qrcodetext);
                }
            }
        });
    }

    class LoadQrCode extends AsyncTask<String, Void, Void> {

        final ProgressDialog progressDialog = new ProgressDialog(UserDetailsActivity.this);
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
                bitmap = Global.encodeAsBitmap(strings[0], UserDetailsActivity.this);
            } catch (WriterException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();

            PrefConnector.writeSting(UserDetailsActivity.this, getString(R.string.user_name), edt_name.getText().toString());
            PrefConnector.writeSting(UserDetailsActivity.this, getString(R.string.user_mobile), edt_moblie.getText().toString());
            PrefConnector.writeSting(UserDetailsActivity.this, getString(R.string.user_email), edt_email.getText().toString());

            PrefConnector.writeSting(UserDetailsActivity.this, getString(R.string.key_qr_path),
                    Global.saveToInternalStorage(UserDetailsActivity.this, bitmap));
            PrefConnector.writeBoolean(UserDetailsActivity.this, getString(R.string.key_first_open), true);
            startActivity(new Intent(UserDetailsActivity.this, HomeActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
        }
    }

}
