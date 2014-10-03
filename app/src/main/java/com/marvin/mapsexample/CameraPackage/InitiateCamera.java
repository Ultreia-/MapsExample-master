package com.marvin.mapsexample.CameraPackage;

import android.app.Activity;
import android.hardware.Camera;
import android.os.Bundle;

import com.marvin.mapsexample.CameraPackage.CameraHelper;
import com.marvin.mapsexample.CameraPackage.CameraOverlay;
import com.marvin.mapsexample.R;

public class InitiateCamera extends Activity {// implements PictureCallback {

    //protected static final String EXTRA_IMAGE_PATH = "com.marvin.mapsexample.CameraActivity.EXTRA_IMAGE_PATH";

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

    //if we wanterooni to capture dem pictures and save em
//Remember BitmapHelper and MediaHelper

/*
    public void onCaptureClick(View button){
        // Take a picture with a callback when the photo has been created
        // Here you can add callbacks if you want to give feedback when the picture is being taken
        camera.takePicture(null, null, this);
    }

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
        //Log.d("Picture taken");
        String path = savePictureToFileSystem(data);
        setResult(path);
        finish();
    }

    private static String savePictureToFileSystem(byte[] data) {
        File file = MediaHelper.getOutputMediaFile();
        MediaHelper.saveToFile(data, file);
        return file.getAbsolutePath();
    }

    private void setResult(String path) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_IMAGE_PATH, path);
        setResult(RESULT_OK, intent);
    }
    */
}