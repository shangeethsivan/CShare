package com.shrappz.contactsharer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Add extends AppCompatActivity {
    EditText name;
    TextView number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        startActivityForResult(new Intent(this, CameraScanner.class), 0);
        name = (EditText) findViewById(R.id.name);
        number = (TextView) findViewById(R.id.number);
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
                Toast.makeText(this, "Error Occoured Please Try Again", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //Activity Result Format  MECARD:N:Siva;TEL:(944) 279-1117;;
}
