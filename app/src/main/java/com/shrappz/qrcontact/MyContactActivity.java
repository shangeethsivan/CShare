package com.shrappz.qrcontact;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MyContactActivity extends AppCompatActivity {

    private ImageView qr_image;
    private Button btn_edt;
    private TextView tv_noqr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_contact);
        init();
        setUpActionBar();
        setListeners();
        loadImage();
    }

    public void init() {
        qr_image = (ImageView) findViewById(R.id.qr_image);
        btn_edt = (Button) findViewById(R.id.btn_edit);
        tv_noqr = (TextView) findViewById(R.id.tv_noqr);
        tv_noqr.setVisibility(View.INVISIBLE);
    }

    public void setUpActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
    }

    public void setListeners() {
        btn_edt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MyContactActivity.this, MyProfileSettings.class));
            }
        });
    }

    public void loadImage() {
        Bitmap bitmap = BitmapFactory.decodeFile("data/data/com.shrappz.qrcontact/app_images/qr_profile.png");
        if (bitmap != null) {
            qr_image.setImageBitmap(bitmap);
        } else {
            tv_noqr.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        qr_image.setImageBitmap(null);
        loadImage();
    }
}
