package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.annotation.SuppressLint;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;



public class MainActivity extends AppCompatActivity {
    private BatteryHorizontalView batteryHorizontalView;
    private SeekBar seekBar;
    private ImageView imageView;
    private ImageView batteryEmpty;
    private TextView tvPercent;
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        batteryHorizontalView = findViewById(R.id.batteryView);
        seekBar = findViewById(R.id.seekBar);
        imageView = findViewById(R.id.gif);
        batteryEmpty = findViewById(R.id.imgEmpty);
        tvPercent = findViewById(R.id.tvPercent);
        btn = findViewById(R.id.btn);
        Thread thread = new Thread(mrunnable);
        thread.start();
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                batteryHorizontalView.setPercent(i);
                imageView.setVisibility(View.VISIBLE);
                iniAnimation(i);
                tvPercent.setText(i + "%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageView.setVisibility(View.GONE);
            }
        });
    }

    private void iniAnimation(int percent) {
        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(DensityUtil.dip2px(this, 189), DensityUtil.dip2px(this, 42));
        params.topToTop = R.id.imgEmpty;
        params.bottomToBottom = R.id.imgEmpty;
        if (batteryHorizontalView.getPercent() < 70 && batteryHorizontalView.getPercent() > 3) {
            params.leftToLeft = R.id.imgEmpty;
            params.leftMargin = (percent * DensityUtil.dip2px(this, (float) 6.25));

        } else if (batteryHorizontalView.getPercent() <= 3) {
            params.leftToLeft = R.id.imgEmpty;
            params.leftMargin = DensityUtil.dip2px(this, 22);
        } else {
            params.rightToRight = R.id.imgEmpty;
            params.rightMargin = DensityUtil.dip2px(this, 14);
        }
        imageView.setLayoutParams(params);
        int animationId = 0;
        if (percent >= 51) {
            animationId = R.drawable.battery_animation_green;
        } else if (percent <= 50 && percent >= 31) {
            animationId = R.drawable.battery_animation_yellow;
        } else {
            animationId = R.drawable.battery_animation_red;
        }
        imageView.setBackgroundResource(animationId);
        AnimationDrawable anim = (AnimationDrawable) imageView.getBackground();
        anim.start();
    }

    private Runnable mrunnable = new Runnable() {
        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(1000);
                    mHandler.sendEmptyMessage(0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }
    };
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what==0) {
                int ran = (int) (Math.random() * 100);
                batteryHorizontalView.setPercent(ran);
                iniAnimation(batteryHorizontalView.getPercent());
            }
        }
    };

}
