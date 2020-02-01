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
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.AugmentedFaceNode;
import com.google.ar.sceneform.ux.TransformableNode;

import java.util.Collection;

import static com.google.ar.sceneform.rendering.ShapeFactory.makeSphere;

public class MainActivity extends AppCompatActivity {

    private ModelRenderable modelRenderable;
    private Texture texture;
    private boolean isAdded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_main);

       CustomSpeechBubble customArFragment = (CustomSpeechBubble) getSupportFragmentManager().findFragmentById(R.id.arFragment);

       ModelRenderable.builder().setSource(this, R.raw.fox_face)
               .build()
               .thenAccept(renderable -> {
                   modelRenderable = renderable;
                   modelRenderable.setShadowCaster(false);
                   modelRenderable.setShadowReceiver(false);
               });
       Texture.builder().setSource(this,R.drawable.fox_face_mesh_texture)
               .build()
               .thenAccept(texture -> this.texture = texture);
       customArFragment.getArSceneView().setCameraStreamRenderPriority(Renderable.RENDER_PRIORITY_FIRST);
       customArFragment.getArSceneView().getScene().addOnUpdateListener(frameTime -> {
           if( modelRenderable == null || texture == null)
               return;
           Frame frame = customArFragment.getArSceneView().getArFrame();

           Collection<AugmentedFace> augmentedFaces = frame.getUpdatedTrackables(AugmentedFace.class);

           for (AugmentedFace augmentedFace : augmentedFaces) {
               if(isAdded){
                   return;
               }
               AugmentedFaceNode augmentedFaceNode = new AugmentedFaceNode(augmentedFace);
               augmentedFaceNode.setParent(customArFragment.getArSceneView().getScene());
               augmentedFaceNode.setFaceRegionsRenderable(modelRenderable);
               augmentedFaceNode.setFaceMeshTexture(texture);

               isAdded = true;
           }
       });


    }

}
