package com.example.sightofsound;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;

import com.google.ar.core.Anchor;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.Color;
import com.google.ar.sceneform.rendering.MaterialFactory;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.ShapeFactory;
import com.google.ar.sceneform.ux.ArFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ArFragment arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);

        arFragment.setOnTapArPlaneListener(((hitResult, plane, motionEvent) -> {
            Anchor anchor = hitResult.createAnchor();

            MaterialFactory.makeOpaqueWithColor(this, new Color(android.graphics.Color.RED))
                    .thenAccept(material -> {
                        ModelRenderable renderable = ShapeFactory.makeSphere(1.0f, new Vector3(0f,1f, 1f), material);
                        AnchorNode anchorNode = new AnchorNode(anchor);
                        anchorNode.setRenderable(renderable);
                        arFragment.getArSceneView().getScene().addChild(anchorNode);
                    });
            }));
    }
}
