package com.example.sightofsound;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.ar.core.AugmentedFace;
import com.google.ar.core.Frame;
import com.google.ar.sceneform.rendering.Renderable;
import com.google.ar.sceneform.rendering.ViewRenderable;
import com.google.ar.sceneform.ux.AugmentedFaceNode;

import java.util.Collection;

public class MainActivity extends AppCompatActivity {

    private boolean isAdded = false;
    private ViewRenderable testViewRenderable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_main);

       CustomSpeechBubble customArFragment = (CustomSpeechBubble) getSupportFragmentManager().findFragmentById(R.id.arFragment);
       SpeechFrame speechFrame = new SpeechFrame();
       speechFrame.setTimer(0);
       speechFrame.setOldTimer(0);

       customArFragment.getArSceneView().setCameraStreamRenderPriority(Renderable.RENDER_PRIORITY_FIRST);
       customArFragment.getArSceneView().getScene().addOnUpdateListener(frameTime -> {
           //speechFrame.setTimer(frameTime.getDeltaSeconds());

           Frame frame = customArFragment.getArSceneView().getArFrame();

           Collection<AugmentedFace> augmentedFaces = frame.getUpdatedTrackables(AugmentedFace.class);


           ViewRenderable.builder()
                   .setView(this, R.layout.testrenderable)
                   .build()
                   .thenAccept(renderable -> testViewRenderable = renderable);

           TextView subtitleView = null;

           String character = "a";

           for (AugmentedFace augmentedFace : augmentedFaces) {
               if(!isAdded){

                   AugmentedFaceNode augmentedFaceNode = new AugmentedFaceNode(augmentedFace);
                   augmentedFaceNode.setParent(customArFragment.getArSceneView().getScene());

                   augmentedFaceNode.setRenderable(testViewRenderable);
                   testViewRenderable.setHorizontalAlignment(ViewRenderable.HorizontalAlignment.LEFT);


                   isAdded = true;
               }
               subtitleView = testViewRenderable.getView().findViewById(R.id.SubtitleWidget);
               speechFrame.addFrame(frameTime.getDeltaSeconds());

               if(speechFrame.getTimerInt() != speechFrame.getOldTimer()){
                   //do something once a second
                   speechFrame.setOldTimer(speechFrame.getTimerInt());

                   speechFrame.setCurrentSubtitle(speechFrame.getCurrentSubtitle() + character);
               }

               //subtitleView.setText(speechFrame.getCurrentSubtitle());
               String s = speechFrame.getCurrentSubtitle();
               subtitleView.setText(s);
               if (speechFrame.getTimer() >  100000){

               }
           }

       });


    }

}
