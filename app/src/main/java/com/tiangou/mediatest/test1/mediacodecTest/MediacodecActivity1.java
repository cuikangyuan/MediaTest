package com.tiangou.mediatest.test1.mediacodecTest;

import android.Manifest;
import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tiangou.mediatest.R;
import com.tiangou.mediatest.test1.MyPermissionUtil;

import java.io.IOException;
import java.nio.ByteBuffer;


//MP3 -> PCM -> AAC
public class MediacodecActivity1 extends AppCompatActivity {


    private static final String TAG = "MediacodecActivity1";
    
    private TextView textView;
    private Button button;


    private String srcPath = "";
    private String outPath = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mediacodec1);


        textView = findViewById(R.id.textView);
        button = findViewById(R.id.button);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String inputPath = "/storage/emulated/0/Movies/bgm.mp3";
                String ouputPath = "/storage/emulated/0/Movies/output_mediacodec.aac";

                final AudioCodec audioCodec = AudioCodec.newInstance();

                audioCodec.setEncodeType(MediaFormat.MIMETYPE_AUDIO_AAC);
                audioCodec.setIOPath(inputPath, ouputPath);
                audioCodec.prepare();
                audioCodec.startAsync();
                audioCodec.setOnCompleteListener(new AudioCodec.onCompleteListener() {
                    @Override
                    public void completed() {
                        audioCodec.release();
                    }
                });
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        MyPermissionUtil.checkAndRequestPermission(
                MediacodecActivity1.this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                true,
                MyPermissionUtil.CHECK_STORAGE_PERMISSION);

    }


}
