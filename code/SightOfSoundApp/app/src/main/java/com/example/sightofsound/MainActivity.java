package com.example.sightofsound;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {

    JavaCameraView javaCameraView;
    File casFile;
    private Mat mRgba,mGrey;
    CascadeClassifier  faceDetected;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        javaCameraView = (JavaCameraView)findViewById(R.id.javaCamera);
        javaCameraView.setCvCameraViewListener(this);
        /*if(!OpenCVLoader.initDebug())
        {
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_11,this,baseCallback);
        }
        else
        {
            try {
                baseCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }*/

        javaCameraView.setCvCameraViewListener(this);

    }

    @Override
    public void onCameraViewStarted(int width, int height) {
        mRgba = new Mat();
        mGrey = new Mat();
    }

    @Override
    public void onCameraViewStopped() {
        mRgba.release();
        mGrey.release();
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {

        mRgba = inputFrame.rgba();
        mGrey = inputFrame.gray();

        //detect face

        MatOfRect faceDetections = new MatOfRect();

        faceDetected.detectMultiScale(mRgba,faceDetections);

        for(Rect rect: faceDetections.toArray())
        {
            Core.rectangle(mRgba,new Point(rect.x,rect.y), new Point(rect.x + rect.width, rect.y + rect.height),
                    new Scalar(255,0,0));
        }
        return mRgba;
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(!OpenCVLoader.initDebug()){
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_11,this,baseCallback);
        }
        else
        {
            try {
                baseCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        javaCameraView.disableView();
    }

    private BaseLoaderCallback baseCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) throws IOException {
            switch (status)
            {
                case LoaderCallbackInterface.SUCCESS:
                {
                    InputStream is = getResources().openRawResource(R.raw.haarcascade_frontalface_alt2);
                    File cascade = getDir("cascade", Context.MODE_PRIVATE);
                    casFile = new File(cascade,"haarcascade_frontalface_alt2.xml");

                    FileOutputStream fos = new FileOutputStream(casFile);

                    byte[] buffer = new byte[4096];
                    int byteRead;

                    while((byteRead = is.read(buffer)) != -1){
                        fos.write(buffer,0,byteRead);
                    }

                    is.close();
                    fos.close();

                    faceDetected = new CascadeClassifier(casFile.getAbsolutePath());

                    if(faceDetected.empty()){
                        faceDetected = null;
                    }
                    else{
                        cascade.delete();
                    }

                    javaCameraView.enableView();

                }
                break;

                default:
                {
                    super.onManagerConnected(status);
                }
                break;

            }
        }
    };
}
