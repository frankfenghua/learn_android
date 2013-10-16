package com.androidbook.simplerenderscript;

import android.content.res.Resources;
import android.renderscript.Mesh;
import android.renderscript.RenderScriptGL;
import android.renderscript.ScriptC;

public class SnowRS {
    public static final int SNOW_FLAKES = 4000;
    private ScriptC_snow mScript;

    private Resources mResources;
    private RenderScriptGL mRS;
    
    public SnowRS() {
    }

    public void stop() {
        mRS.bindRootScript(null);
    }

    public void start() {
        mRS.bindRootScript(mScript);
    }
    
    public void init(RenderScriptGL rs, Resources res) {
        mRS = rs;
        mResources = res;
        mScript = (ScriptC_snow) createScript();
    }
    
    public ScriptC createScript() {
        ScriptField_Snow snow = new ScriptField_Snow(mRS, SNOW_FLAKES);
        Mesh.AllocationBuilder smb = new Mesh.AllocationBuilder(mRS);
        smb.addVertexAllocation(snow.getAllocation());
        smb.addIndexSetType(Mesh.Primitive.POINT);
        Mesh sm = smb.create();

        ScriptC_snow script;
        script = new ScriptC_snow(mRS, mResources, R.raw.snow);
        script.set_snowMesh(sm);
        script.bind_snow(snow);
        script.invoke_initSnow();
        return script;
    }
}
