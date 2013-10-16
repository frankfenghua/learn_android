package com.androidbook.simplerenderscript;

import android.content.Context;
import android.renderscript.RSSurfaceView;
import android.renderscript.RenderScriptGL;
import android.view.SurfaceHolder;

public class FallingSnowView extends RSSurfaceView {
	private RenderScriptGL mRSGL; 
	private SnowRS mRender;
	
	public FallingSnowView(Context context) {
		super(context);
	}

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        super.surfaceChanged(holder, format, w, h);

        if (mRSGL == null) {
            RenderScriptGL.SurfaceConfig sc = new RenderScriptGL.SurfaceConfig();
            mRSGL = createRenderScriptGL(sc);
            mRSGL.setSurface(holder, w, h);
            mRender = new SnowRS();
            mRender.init(mRSGL, getResources());
            mRender.start();
        }
    }
    
    @Override
    protected void onDetachedFromWindow() {
        if (mRSGL != null) {
            mRSGL = null;
            destroyRenderScriptGL();
        }
    }
	
}
