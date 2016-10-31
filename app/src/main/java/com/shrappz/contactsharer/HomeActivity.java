package com.shrappz.contactsharer;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shrappz.contactsharer.utils.Global;

public class HomeActivity extends AppCompatActivity {

    LinearLayout add, share;
    Boolean onbackpressonce = false;
    TextView header, footer;
    private final int REQUEST_CODE_ASK_PERMISSIONS = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 23) {
            if (!Global.readBooleanPreference(HomeActivity.this, Global.permissions)) {
                requestPermission();
            }
        }
        setContentView(R.layout.activity_home);
        add = (LinearLayout) findViewById(R.id.add);
        share = (LinearLayout) findViewById(R.id.share);
        header = (TextView) findViewById(R.id.title);
        footer = (TextView) findViewById(R.id.footer);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, Add.class));
            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, SendActivity.class));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.home, menu);
        return true;
    }

    public void requestPermission() {
        int hasWriteContactsPermission = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            hasWriteContactsPermission = checkSelfPermission(Manifest.permission.READ_CONTACTS);
            if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_CONTACTS,
                        Manifest.permission.WRITE_CONTACTS}, REQUEST_CODE_ASK_PERMISSIONS
                );
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (onbackpressonce) {
            finish();
        } else {
            onbackpressonce = true;
            Toast.makeText(this, "Press Back again to exit", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    onbackpressonce = false;
                }
            }, 2000);
        }
    }


    public void onRequestPermissionsResult(final int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 1
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Global.writeBooleanPreference(HomeActivity.this, Global.permissions, true);

                } else {
                    // permission was not granted
                    Toast.makeText(this, "Accept the permission to start using the application", Toast.LENGTH_SHORT).show();
                    AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                    builder.setTitle("Permissions Deny Issue");
                    builder.setMessage("Accept Permissions to continue or restart the application");
                    builder.setCancelable(false);
                    builder.setPositiveButton("PERMISSIONS", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            requestPermission();
                        }
                    });
                    builder.setNegativeButton("DENY", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    });
                    builder.show();
                }
                return;
            }

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.profile:
                startActivity(new Intent(HomeActivity.this, MyProfileSettings.class));
        }
        return super.onOptionsItemSelected(item);
    }
}

//TODO Splash and Demo Screen ,Google Analytics and Crach Reporting -- Crashlytics