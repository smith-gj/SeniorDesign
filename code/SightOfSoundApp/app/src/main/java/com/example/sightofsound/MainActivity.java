package com.example.sightofsound;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;

import com.google.ar.core.Anchor;
import com.google.ar.core.AugmentedFace;
import com.google.ar.core.Frame;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.Color;
import com.google.ar.sceneform.rendering.MaterialFactory;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.Renderable;
import com.google.ar.sceneform.rendering.ShapeFactory;
import com.google.ar.sceneform.rendering.Texture;
import com.google.ar.sceneform.rendering.ViewRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.AugmentedFaceNode;
import com.google.ar.sceneform.ux.TransformableNode;

import java.util.Collection;

import static com.google.ar.sceneform.rendering.ShapeFactory.makeSphere;

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

       customArFragment.getArSceneView().setCameraStreamRenderPriority(Renderable.RENDER_PRIORITY_FIRST);
       customArFragment.getArSceneView().getScene().addOnUpdateListener(frameTime -> {
           speechFrame.setTimer(frameTime.getDeltaSeconds());

           Frame frame = customArFragment.getArSceneView().getArFrame();

           Collection<AugmentedFace> augmentedFaces = frame.getUpdatedTrackables(AugmentedFace.class);


           ViewRenderable.builder()
                   .setView(this, R.layout.testrenderable)
                   .build()
                   .thenAccept(renderable -> testViewRenderable = renderable);

           for (AugmentedFace augmentedFace : augmentedFaces) {
               if(isAdded){
                   return;
               }
               AugmentedFaceNode augmentedFaceNode = new AugmentedFaceNode(augmentedFace);
               augmentedFaceNode.setParent(customArFragment.getArSceneView().getScene());
               Collection<Anchor> a = augmentedFace.getAnchors();

               augmentedFaceNode.setRenderable(testViewRenderable);

               isAdded = true;
               if (speechFrame.getTimer() >  100000){

               }
           }

       });


    }

}
