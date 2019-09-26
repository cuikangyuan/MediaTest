package com.tiangou.mp3encoder;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {


    static {

        System.loadLibrary("audioencoder");

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Mp3Encoder mp3Encoder = new Mp3Encoder();
        mp3Encoder.encode();
    }
}
