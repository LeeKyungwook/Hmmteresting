package hmmteresting.oikwho;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;
import java.io.IOException;

public class CamPreview extends ViewGroup implements SurfaceHolder.Callback{

    SurfaceView mSurfaceView;
    SurfaceHolder mSurfaceHolder;
    Size mPreviewSize;
    List<Size> mSupportedPreviewSizes;
    Camera mCamera;

    CamPreview(Context context, SurfaceView sv) {
        super(context);

        mSurfaceView = sv;

        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.addCallback(this);
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public void setCamera(Camera camera) {
        if(mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }

        mCamera = camera;

        if(mCamera != null) {
            List<Size> localSizes = mCamera.getParameters().getSupportedPreviewSizes();
            mSupportedPreviewSizes = localSizes;
            requestLayout();

            Camera.Parameters params = mCamera.getParameters();

            List<String> focusModes = params.getSupportedFocusModes();
            if(focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
                params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
                mCamera.setParameters(params);
            }

            try {
                mCamera.setPreviewDisplay(mSurfaceHolder);
            } catch (IOException e) {
                e.printStackTrace();
            }

            mCamera.startPreview();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int width = resolveSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        final int height = resolveSize(getSuggestedMinimumHeight(), heightMeasureSpec);

        setMeasuredDimension(width,height);

        if(mSupportedPreviewSizes != null) {
            mPreviewSize = getOptimalPreviewSize(mSupportedPreviewSizes,width,height);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if(changed && getChildCount() > 0) {
            final View child = getChildAt(0);

            final int width = r-l;
            final int height = b-t;

            int previewWidth = width;
            int previewHeight = height;

            if(mPreviewSize != null) {
                previewHeight = mPreviewSize.height;
                previewWidth = mPreviewSize.width;
            }

            if(width*previewHeight > height*previewWidth) {
                final int scaledChildWidth = previewWidth*height / previewHeight*width;
                child.layout((width-scaledChildWidth)/2, 0,
                        (width+scaledChildWidth)/2, height);
            } else {
                final int scaledChildHeight = previewHeight*width / previewWidth*height;
                child.layout(0, (height-scaledChildHeight)/2,
                        width, (height+scaledChildHeight)/2);
            }
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.d("CameraPreview", "서페이스 생성 진입");

        try {
            if(mCamera != null) {
                mCamera.setPreviewDisplay(holder);


            }
        } catch (IOException e) {
            Log.e("CameraPreview", "서페이스 생성의 setPreviewDisplay()가 IO익셉션을 일으켯다");
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if(mCamera != null) {
            Camera.Parameters params = mCamera.getParameters();
            List<Size> allSizes = params.getSupportedPreviewSizes();
            Camera.Size size = allSizes.get(0);

            for(int i = 0; i < allSizes.size(); i++) {
                if(allSizes.get(i).width > size.width) { size = allSizes.get(i); }
            }

            params.setPreviewSize(size.width, size.height);
            mCamera.startPreview();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if(mCamera != null) { mCamera.stopPreview(); }
    }

    private Size getOptimalPreviewSize(List<Size> sizes, int w, int h) {
        final double ASPECT_TOLERANCE = 0.1;
        double targetRatio = (double)w/h;
        if(sizes == null) { return null; }

        Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;

        int targetHeight = h;

        for( Size i : sizes) {
            double ratio = (double)(i.width/i.height);

            if(Math.abs(ratio-targetRatio) > ASPECT_TOLERANCE) continue;
            if(Math.abs(i.height-targetHeight) < minDiff) {
                optimalSize = i;
                minDiff = Math.abs(i.height - targetHeight);
            }
        }

        if(optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Size i : sizes) {
                if(Math.abs(i.height-targetHeight) < minDiff) {
                    optimalSize = i;
                    minDiff = Math.abs(i.height - targetHeight);
                }
            }
        }

        return optimalSize;
    }
}
