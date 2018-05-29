package hmmteresting.oikwho;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class TakePhoto extends AppCompatActivity {
    CamPreview global_preview;
    Camera global_camera;
    Context global_ctx;

    private final static int PERMISSIONS_REQUEST_CODE = 100;
    private final static int CAMERA_FACE = Camera.CameraInfo.CAMERA_FACING_FRONT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.activity_take_photo);

        Button shutter = findViewById(R.id.btn_shutter);
        shutter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                global_camera.takePicture(shutterCallback, null, jpegCallback);
            }
        });

        if(getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                int hasCameraPermission = ContextCompat.checkSelfPermission(this,
                        Manifest.permission.CAMERA);
                int hasWriteExternalStoragePermission = ContextCompat.checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE);

                if((hasCameraPermission == PackageManager.PERMISSION_GRANTED) &&
                        (hasWriteExternalStoragePermission == PackageManager.PERMISSION_GRANTED)) {
                    Log.d("TakePhoto","이미 카메라랑 sd카드 저장 권한 갖고있음");
                } else  {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            PERMISSIONS_REQUEST_CODE);
                }
            } else {            }
        } else {
            Toast.makeText(TakePhoto.this, "Camera not supported", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        startCamera();
    }

    @Override
    protected void onPause() {
        super.onPause();

        if(global_camera != null) {
            global_camera.stopPreview();
            global_preview.setCamera(null);
            global_camera.release();
            global_camera = null;
        }

        ((LinearLayout)findViewById(R.id.layout_takePhoto)).removeView(global_preview);
        global_preview = null;
    }

    ShutterCallback shutterCallback = new ShutterCallback() {
        @Override
        public void onShutter() {
            Log.d("TakePhoto","셔터 버튼 눌렀음");
        }
    };

    private void resetCam() {
        startCamera();
    }
int pictureTime = 1;
    PictureCallback jpegCallback = new PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            Log.d("TakePhoto","jpeg로 저장할것임");

            //카메라로 얻은 사진 jpeg로 저장하는 과정이 들어갈것임
            int wide = global_camera.getParameters().getPictureSize().width;
            int high = global_camera.getParameters().getPictureSize().height;

            int orientation = 90;

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);

            Matrix matrix = new Matrix();
            matrix.postRotate(270);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, wide, high, matrix,true);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] currentData = stream.toByteArray();

            new SaveImageTask().execute(currentData);
            resetCam();
            Log.d("TakePhoto","사진이 잘 저장되었다");
            pictureTime ++;
            if(pictureTime > 3) {
                pictureTime = 1;
                finish();
            }
       }
    };

    public void startCamera() {
        if(global_preview == null) {
            global_preview = new CamPreview(this, (SurfaceView)findViewById(R.id.view_camSurface));
            global_preview.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
            ((LinearLayout)findViewById(R.id.layout_takePhoto)).addView(global_preview);
            global_preview.setKeepScreenOn(true);
        }
        global_preview.setCamera(null);
        if(global_camera != null) {
            global_camera.release();
            global_camera = null;
        }

        int numCams = Camera.getNumberOfCameras();
        int orientationDegree = 90;

        if (numCams > 0) {
            try{

                global_camera = Camera.open(CAMERA_FACE);
                global_camera.setDisplayOrientation(orientationDegree);  //이게 내 생각보다 90도 돌아가있을 가능성 있다

                Camera.Parameters params = global_camera.getParameters();
                params.setRotation(orientationDegree);

                global_camera.startPreview();
            } catch (RuntimeException r) {
                Toast.makeText(global_ctx, "Camera_not_found__"+r.getMessage().toString(), Toast.LENGTH_LONG).show();
                Log.d("TakePhoto","camera_not_found__"+r.getMessage().toString());
            }
        }

        global_preview.setCamera(global_camera);
    }

    private class SaveImageTask extends AsyncTask<byte[], Void, Void> {

        @Override
        protected Void doInBackground(byte[]... bytes) {
            FileOutputStream outStream = null;

            try{
                File sdCard = Environment.getExternalStorageDirectory();
                File dir = new File (sdCard.getAbsolutePath() + "/oikwho");
                dir.mkdirs();

                String fileName = String.format("suhyun_%d.jpg", pictureTime);
                File outFile = new File(dir, fileName);

                outStream = new FileOutputStream(outFile);
                outStream.write(bytes[0]);
                outStream.flush();
                outStream.close();

                Log.d("TakePhoto","사진을 찍었다 크기는 " + bytes.length+"byte고 "
                        + outFile.getAbsolutePath() + "에 저장됨");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}
