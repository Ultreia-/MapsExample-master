package com.marvin.mapsexample;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Rasmus on 9/24/2014.
 */
public class CameraLandingPage extends FragmentActivity{

    private static final int REQ_CAMERA_IMAGE = 123;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.landing_page);

        String message = "Click the button below to start";
        if(cameraNotDetected()){
            message = "No camera detected, clicking the button below will have unexpected behaviour.";
        }
        TextView cameraDescriptionTextView = (TextView) findViewById(R.id.text_view_camera_description);
        cameraDescriptionTextView.setText(message);
    }


    public void onUseCameraClick(View button){
        Intent intent = new Intent(this, InitiateCamera.class);
        startActivityForResult(intent, REQ_CAMERA_IMAGE);
    }

    private boolean cameraNotDetected() {
        return !getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

/*
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQ_CAMERA_IMAGE && resultCode == RESULT_OK){
            String imgPath = data.getStringExtra(CameraActivity.EXTRA_IMAGE_PATH);
            displayImage(imgPath);
        } else
        if(requestCode == REQ_CAMERA_IMAGE && resultCode == RESULT_CANCELED){
        }
    }

    private void displayImage(String path) {
        ImageView imageView = (ImageView) findViewById(R.id.image_view_captured_image);
        imageView.setImageBitmap(BitmapHelper.decodeSampledBitmap(path, 300, 250));
    }
*/
}
