package com.tiangou.mediatest.test1.mediacodecTest;

import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;

public class AudioCodec {

    private static final String TAG = "AudioCodec";

    private MediaExtractor mediaExtractor;

    private MediaCodec mediaDecoder;
    private ByteBuffer[] decoderInputBuffers;
    private ByteBuffer[] decoderOutputBuffers;
    private MediaCodec.BufferInfo decoderBufferInfo;

    private MediaCodec mediaEncoder;
    private ByteBuffer[] encoderInputBuffers;
    private ByteBuffer[] encoderOutputBuffers;
    private MediaCodec.BufferInfo encoderBufferInfo;


    private FileOutputStream fos;
    private BufferedOutputStream bos;
    private FileInputStream fis;
    private BufferedInputStream bis;
    private ArrayList<byte[]> chunkPCMDataContainer;//PCM数据块容器

    private String encodeType = MediaFormat.MIMETYPE_AUDIO_AAC;
    private String srcPath = "";
    private String dstPath = "";

    private long decodeSize;
    private long fileTotalSize;

    private boolean decodeOver = false;

    private onCompleteListener onCompleteListener;


    public AudioCodec.onCompleteListener getOnCompleteListener() {
        return onCompleteListener;
    }

    public void setOnCompleteListener(AudioCodec.onCompleteListener onCompleteListener) {
        this.onCompleteListener = onCompleteListener;
    }

    private AudioCodec() {

    }

    public static AudioCodec newInstance() {
        return new AudioCodec();
    }

    public void setEncodeType(String encodeType) {

        this.encodeType = encodeType;
    }

    public void setIOPath(String srcPath, String dstPath) {

        this.srcPath = srcPath;
        this.dstPath = dstPath;
    }


    public void prepare() {

        if (encodeType == null) {
            throw new IllegalArgumentException("encodeType can't be null");
        }

        if (srcPath == null) {
            throw new IllegalArgumentException("srcPath can't be null");
        }

        if (dstPath == null) {
            throw new IllegalArgumentException("dstPath can't be null");
        }

        try {

            fos = new FileOutputStream(new File(dstPath));
            bos = new BufferedOutputStream(fos, 200 * 1024);
            File file = new File(srcPath);
            fileTotalSize = file.length();

        } catch (IOException e) {

            e.printStackTrace();
        }

        chunkPCMDataContainer = new ArrayList<>();
        initMediaDecoder();

        if (encodeType == MediaFormat.MIMETYPE_AUDIO_AAC) {

            initAACMediaEncoder();

        } else {
            //TODO
        }


    }

    public void release() {

        try {

            if (bos != null) {

                bos.flush();

            }

        } catch (IOException e) {

            e.printStackTrace();
        } finally {
            if (bos != null) {

                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    bos = null;
                }

            }
        }

        if (fos != null) {

            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                fos = null;
            }
        }

        if (mediaEncoder != null) {

            mediaEncoder.stop();
            mediaEncoder.release();
            mediaEncoder = null;
        }

        if (mediaDecoder != null) {


            mediaDecoder.stop();
            mediaDecoder.release();
            mediaDecoder = null;
        }

        if (mediaExtractor != null) {

            mediaExtractor.release();
            mediaExtractor = null;
        }

    }

    private class DecodeRunnable implements Runnable {
        @Override
        public void run() {
            while (!decodeOver) {

                srcAudioToPCM();

            }
        }
    }

    private class EncodeRunnable implements Runnable {
        @Override
        public void run() {

            long l = System.currentTimeMillis();

            while (!decodeOver || !chunkPCMDataContainer.isEmpty()) {
                dstAudioFormatFromPCM();
            }

            if (onCompleteListener != null) {


                onCompleteListener.completed();
            }


            Log.d(TAG, "EncodeRunnable finished >>>  fileTotalSize: " + fileTotalSize + " decodeSize: " + decodeSize + " time: " + (System.currentTimeMillis() - l));

        }
    }

    public void startAsync() {

        new Thread(new DecodeRunnable()).start();
        new Thread(new EncodeRunnable()).start();

    }

    private void initMediaDecoder() {

        try {

            mediaExtractor = new MediaExtractor();
            mediaExtractor.setDataSource(srcPath);

            for (int i = 0 ; i < mediaExtractor.getTrackCount(); i++) {

                MediaFormat trackFormat = mediaExtractor.getTrackFormat(i);

                String mine = trackFormat.getString(MediaFormat.KEY_MIME);

                if (mine.startsWith("audio")) {

                    mediaExtractor.selectTrack(i);

                    mediaDecoder = MediaCodec.createDecoderByType(mine);
                    mediaDecoder.configure(trackFormat, null, null, 0);

                    break;
                }

            }


        } catch (IOException e) {

            e.printStackTrace();

        }

        if (mediaDecoder == null) {

            Log.d(TAG, "create mediaDecoder failed.");

            return;

        }

        mediaDecoder.start();
        decoderInputBuffers = mediaDecoder.getInputBuffers();
        decoderOutputBuffers = mediaDecoder.getOutputBuffers();
        decoderBufferInfo = new MediaCodec.BufferInfo();
    }

    private void initAACMediaEncoder() {

        try {

            MediaFormat encodeFormat = MediaFormat.createAudioFormat(encodeType, 44100, 2);
            encodeFormat.setInteger(MediaFormat.KEY_BIT_RATE, 96000);
            encodeFormat.setInteger(MediaFormat.KEY_AAC_PROFILE, MediaCodecInfo.CodecProfileLevel.AACObjectLC);
            encodeFormat.setInteger(MediaFormat.KEY_MAX_INPUT_SIZE, 100 * 1024);

            mediaEncoder = MediaCodec.createEncoderByType(encodeType);
            mediaEncoder.configure(encodeFormat, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);

        } catch (IOException e) {

            e.printStackTrace();

        }

        if (mediaEncoder == null) {

            Log.d(TAG, "create mediaEncoder failed.");

        }

        mediaEncoder.start();
        encoderInputBuffers = mediaEncoder.getInputBuffers();
        encoderOutputBuffers = mediaEncoder.getOutputBuffers();
        encoderBufferInfo = new MediaCodec.BufferInfo();
    }


    private void dstAudioFormatFromPCM() {
        int inputIndex;
        ByteBuffer inputBuffer;
        int outputIndex;
        ByteBuffer outputBuffer;
        byte[] chunkAudio;
        int outBitSize;
        int outPacketSize;
        byte[] chunkPCM;

        for (int i = 0; i < encoderInputBuffers.length; i++) {

            chunkPCM = getPCMData();

            if (chunkPCM == null) {

                break;
            }

            inputIndex = mediaEncoder.dequeueInputBuffer(-1);
            inputBuffer = encoderInputBuffers[inputIndex];
            inputBuffer.clear();
            inputBuffer.limit(chunkPCM.length);
            inputBuffer.put(chunkPCM);
            mediaEncoder.queueInputBuffer(inputIndex, 0, chunkPCM.length, 0, 0);

        }

        outputIndex = mediaEncoder.dequeueOutputBuffer(encoderBufferInfo, 10000);

        while (outputIndex >= 0) {

            outBitSize = encoderBufferInfo.size;
            outPacketSize = outBitSize + 7;
            outputBuffer = encoderOutputBuffers[outputIndex];
            outputBuffer.position(encoderBufferInfo.offset);
            outputBuffer.limit(encoderBufferInfo.offset + outBitSize);
            chunkAudio = new byte[outPacketSize];
            addADTStoPacket(chunkAudio, outPacketSize);
            outputBuffer.get(chunkAudio, 7, outBitSize);
            outputBuffer.position(encoderBufferInfo.offset);

            try {

                bos.write(chunkAudio, 0, chunkAudio.length);

            } catch (IOException e) {

                e.printStackTrace();
            }

            mediaEncoder.releaseOutputBuffer(outputIndex, false);
            outputIndex = mediaEncoder.dequeueOutputBuffer(encoderBufferInfo, 10000);
        }

    }

    private void srcAudioToPCM() {

        for (int i = 0; i < decoderInputBuffers.length; i++) {

            int inputIndex = mediaDecoder.dequeueInputBuffer(-1);
            if (inputIndex < 0) {

                decodeOver = true;
                return;
            }

            ByteBuffer inputBuffer = decoderInputBuffers[inputIndex];
            inputBuffer.clear();

            int sampleSize = mediaExtractor.readSampleData(inputBuffer, 0);

            if (sampleSize < 0) {
                decodeOver = true;
            } else {
                mediaDecoder.queueInputBuffer(inputIndex, 0, sampleSize, 0, 0);
                mediaExtractor.advance();
                decodeSize += sampleSize;
            }

        }

        int outputIndex = mediaDecoder.dequeueOutputBuffer(decoderBufferInfo, 1000);
        ByteBuffer outputBuffer;

        byte[] chunkPCM;

        while(outputIndex >= 0) {

            outputBuffer = decoderOutputBuffers[outputIndex];
            chunkPCM = new byte[decoderBufferInfo.size];
            outputBuffer.get(chunkPCM);
            outputBuffer.clear();
            putPCMData(chunkPCM);
            mediaDecoder.releaseOutputBuffer(outputIndex, false);
            outputIndex = mediaDecoder.dequeueOutputBuffer(decoderBufferInfo, 1000);
        }

    }

    private void putPCMData(byte[] pcmChunk) {

        synchronized (AudioCodec.class) {

            chunkPCMDataContainer.add(pcmChunk);

        }
    }

    private byte[] getPCMData() {
        synchronized (AudioCodec.class) {

            if (chunkPCMDataContainer.isEmpty()) {
                return null;
            }


            byte[] pcmChunk = chunkPCMDataContainer.get(0);
            chunkPCMDataContainer.remove(pcmChunk);
            return pcmChunk;
        }
    }

    private void addADTStoPacket(byte[] packet, int packetLen) {

        int profile = 2;
        int freqIdx = 4;
        int chanCfg = 2;

        packet[0] = (byte) 0xFF;
        packet[1] = (byte) 0xF9;
        packet[2] = (byte) (((profile - 1) << 6) + (freqIdx << 2) + (chanCfg >> 2));
        packet[3] = (byte) (((chanCfg & 3) << 6) + (packetLen >> 11));
        packet[4] = (byte) ((packetLen & 0x7FF) >> 3);
        packet[5] = (byte) (((packetLen & 7) << 5) + 0x1F);
        packet[6] = (byte) 0xFC;


    }

    public interface onCompleteListener {
        void completed();
    }
}
