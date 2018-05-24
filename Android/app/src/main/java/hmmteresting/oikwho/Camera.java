package hmmteresting.oikwho;

import android.content.Intent;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Button;

public class Camera extends AppCompatActivity {

    private static SurfaceView cameraPreview;
    private SurfaceHolder holder;
    private static Button btn_cameraPreview;
    private static Camera myCamera;
    private int RESULT_PERMISSION = 100;
    private static MainActivity getInstance;

    static final int REQUEST_IMAGE_CAPTURE = 1;

    private void sendTakePhotoIntent() {
        Intent takePicIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(takePicIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePicIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
    }

    public static Camera getCamera() {
        return myCamera;
    }
    public void setInit(){

    }
}
