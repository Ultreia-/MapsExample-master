package com.marvin.mapsexample.DialClasses;

import android.app.Activity;
import android.hardware.Camera;
import android.os.Bundle;

import com.marvin.mapsexample.CameraPackage.CameraHelper;
import com.marvin.mapsexample.CameraPackage.CameraOverlay;
import com.marvin.mapsexample.R;

public class InitiateSineCurve extends Activity {

    private Camera camera;
    private CameraOverlay cameraOverlay;

    // Show the camera view on the activity
    private void initCameraPreview() {
        cameraOverlay = (CameraOverlay) findViewById(R.id.camera_preview);
        cameraOverlay.init(camera);
    }

    // ALWAYS remember to release the camera when you are finished
    @Override
    protected void onPause() {
        super.onPause();
        releaseCamera();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_overlay);
        setResult(RESULT_CANCELED);
        // Camera may be in use by another activity or the system or not available at all
        camera = CameraHelper.getCameraInstance();
        if(CameraHelper.cameraAvailable(camera)){
            initCameraPreview();
        } else {
            finish();
        }
    }

    private void releaseCamera() {
        if(camera != null){
            camera.release();
            camera = null;
        }
    }
}