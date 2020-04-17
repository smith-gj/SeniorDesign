package com.example.testapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.graphics.Paint;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCamera2View;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {

    JavaCamera2View javaCamera2View;
    File casFile;
    private Mat mRgba, mGrey;
    CascadeClassifier faceDetected;
    List<Rect> faces = new ArrayList<Rect>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        javaCamera2View = (JavaCamera2View) findViewById(R.id.javaCamera);
        javaCamera2View.setCvCameraViewListener(this);
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

        Mat mRgbaT = mRgba.t();
        Core.flip(mRgba.t(), mRgbaT, 1);
        Imgproc.resize(mRgbaT, mRgbaT, mRgba.size());
        //detect face

        MatOfRect faceDetections = new MatOfRect();

        int rectangleXMargin = 10;
        int rectangleYMargin = 10;

        Scalar textColor = new Scalar(255, 69, 0);
        Scalar rectangleColor = new Scalar(255, 140, 0);

        faceDetected.detectMultiScale(mRgbaT, faceDetections);
        faces = faceDetections.toList();
        if (!faces.isEmpty()) {
            Rect rect = faces.get(0);

            String input = "This subtitle demonstrates what happens as subtitles grow in length and span multiple lines";

            List<String> subStrs = splitString(input);

            Paint p = new Paint();
            float textWidth = 0;
            for (int i = 0; i < subStrs.size(); i++) {
                textWidth = Math.max(p.measureText(subStrs.get(i)), textWidth);
            }

            //this will need to change based on the font type and size that is used. Scales the measured text size to actual opencv units
            float widthCoeff = 3.5f;

            float offset = textWidth * widthCoeff / 2;

            //do some math to center the text below the face
            double leftXCoord = rect.x + (.5 * rect.width - offset);
            double rightXCoord = leftXCoord + (textWidth * widthCoeff);

            int textHeight = 28;

            double bottomYCoord = rect.y + rect.height;
            double topYCoord = bottomYCoord - subStrs.size() * textHeight;

            //draw text
            for (int i = 0; i < subStrs.size(); i++) {
                // the -5 is weird but for some reason it needs to be there to center the text. Maybe the rectangle is being created at the wrong coords somehow...
                Imgproc.putText(mRgbaT, subStrs.get(i), new Point(leftXCoord, topYCoord + (i + 1) * textHeight - 5), 4, 1, textColor);
            }

            //add margins to rectangle which will be drawn below
            leftXCoord -= rectangleXMargin;
            rightXCoord += rectangleXMargin;
            topYCoord -= rectangleYMargin;
            bottomYCoord += rectangleYMargin;

            fancyRectangle(mRgbaT, new Point(leftXCoord, topYCoord), new Point(rightXCoord, bottomYCoord), rectangleColor, 3, 5 * subStrs.size(), (10.0f/3.0f) * subStrs.size());
        }
        return mRgbaT;
    }

    private List<String> splitString(String s) {
        LinkedList<String> result = new LinkedList<String>();
        Paint p = new Paint();
        float textWidth = p.measureText(s);

        boolean done = false;

        String currentString = s;

        //change this for different subtitle length per line
        int targetWidth = 210;

        if (textWidth < targetWidth) {
            result.add(s);
        } else {
            //while loop for each final substring
            while (!done) {
                int strLen = currentString.length();
                int searchIndex;

                searchIndex = Math.min(strLen, 40);

                float lengthDiff = 0;
                String subStr;
                //while loop to get substring from current string
                do {
                    subStr = currentString.substring(0, searchIndex);
                    float w = p.measureText(subStr);
                    if (w > targetWidth)
                        searchIndex -= 1;
                    else
                        searchIndex = searchIndex + 1;
                    lengthDiff = Math.abs(w - targetWidth);
                } while (lengthDiff > 3 && searchIndex <= strLen);

                //check if we have reached the end of the string
                if (subStr.length() == currentString.length()) {
                    done = true;
                    result.add(subStr);
                } else {
                    //find rightmost space in the substring and add it to the list of final substrings
                    int splitIndex = subStr.lastIndexOf(" ");
                    result.add(subStr.substring(0, splitIndex));
                    currentString = currentString.substring(splitIndex, currentString.length()).trim();
                }
            }
        }

        return result;
    }


    private void fancyRectangle(Mat img, Point pt1, Point pt2, Scalar color, int thickness, float r, float d) {
        double x1 = pt1.x;
        double y1 = pt1.y;
        double x2 = pt2.x;
        double y2 = pt2.y;

        //Top left
        Imgproc.line(img, new Point(x1 + r, y1), new Point(x1 + r + d, y1),color, thickness);
        Imgproc.line(img, new Point(x1, y1 + r), new Point(x1, y1 + r + d),color, thickness);
        Imgproc.ellipse(img, new Point(x1 + r, y1 + r), new Size(r, r),180, 0, 90, color, thickness);


        //Top right
        Imgproc.line(img, new Point(x2 - r, y1), new Point(x2 - r - d, y1),color, thickness);
        Imgproc.line(img, new Point(x2, y1 + r), new Point(x2, y1 + r + d),color, thickness);
        Imgproc.ellipse(img, new Point(x2 - r, y1 + r), new Size(r, r),270, 0, 90, color, thickness);


        //Bottom left
        Imgproc.line(img, new Point(x1 + r, y2), new Point(x1 + r + d, y2),color, thickness);
        Imgproc.line(img, new Point(x1, y2 - r), new Point(x1, y2 - r - d),color, thickness);
        Imgproc.ellipse(img, new Point(x1 + r, y2 - r), new Size(r, r),90, 0, 90, color, thickness);


        //Bottom right
        Imgproc.line(img, new Point(x2 - r, y2), new Point(x2 - r - d, y2),color, thickness);
        Imgproc.line(img, new Point(x2, y2 - r), new Point(x2, y2 - r - d),color, thickness);
        Imgproc.ellipse(img, new Point(x2 - r, y2 - r), new Size(r, r),0, 0, 90, color, thickness);
    }


    @Override
    protected void onResume() {
        super.onResume();

        if(!OpenCVLoader.initDebug()){
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION,this,baseCallback);
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
        javaCamera2View.disableView();
    }

    private void Draw(Rect rect, Mat mga)
    {

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

                    javaCamera2View.enableView();

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
