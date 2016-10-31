package com.shrappz.contactsharer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class DemoSlider extends AppCompatActivity {
    String[] slider_headings = {"Share or scan a QR Contact at a click", "Access your contacts and create QRCodes", "Create a widget to have your personalised QR Contact"};
    int[] images = {R.drawable.one, R.drawable.two, R.drawable.three};
    private ViewPager viewpager;
    private LinearLayout dotsLayout;
    private LinearLayout.LayoutParams layoutParams;
    Button btn_next, btn_finish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_slider);
        init();
        viewpager.setAdapter(new CustomSliderAdapter());
        listeners();
        addDots();
        selectDot(0);
        btn_finish.setVisibility(View.GONE);
    }

    public void init() {
        viewpager = (ViewPager) findViewById(R.id.viewpager);
        dotsLayout = (LinearLayout) findViewById(R.id.dots_layout);
        btn_next = (Button) findViewById(R.id.btn_next);
        btn_finish = (Button) findViewById(R.id.btn_finish);
    }

    public void listeners() {
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                selectDot(position);
                if (position == slider_headings.length - 1) {
                    btn_finish.setVisibility(View.VISIBLE);
                    btn_next.setVisibility(View.GONE);
                } else {
                    btn_finish.setVisibility(View.GONE);
                    btn_next.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewpager.setCurrentItem(viewpager.getCurrentItem() + 1);
            }
        });
        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DemoSlider.this, UserDetailsActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });

    }

    public void addDots() {
        layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(5, 0, 5, 0);
        for (int i = 0; i < slider_headings.length - 1; i++) {
            ImageView imageView = new ImageView(DemoSlider.this);
            imageView.setImageResource(R.drawable.dot_unselected);
            imageView.setLayoutParams(layoutParams);
            dotsLayout.addView(imageView);
        }
    }

    public void selectDot(int position) {
        dotsLayout.removeAllViews();
        for (int i = 0; i < slider_headings.length; i++) {
            ImageView imageView = new ImageView(DemoSlider.this);

            if (i == position) {
                imageView.setImageResource(R.drawable.dot_selected);
            } else {
                imageView.setImageResource(R.drawable.dot_unselected);
            }
            imageView.setLayoutParams(layoutParams);
            dotsLayout.addView(imageView);
        }
    }

    public class CustomSliderAdapter extends PagerAdapter {

        private LayoutInflater layoutinflater;

        public CustomSliderAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutinflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = layoutinflater.inflate(R.layout.viewpager_layout, container, false);
            TextView textView = (TextView) v.findViewById(R.id.header);
            textView.setText(slider_headings[position]);
            ((RelativeLayout) v.findViewById(R.id.viewpager_container)).setBackgroundResource(R.color.colorPrimary);
            ((ImageView) v.findViewById(R.id.img_slider)).setImageResource(images[position]);
            container.addView(v);
            return v;
        }

        @Override
        public int getCount() {
            return slider_headings.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }

}
