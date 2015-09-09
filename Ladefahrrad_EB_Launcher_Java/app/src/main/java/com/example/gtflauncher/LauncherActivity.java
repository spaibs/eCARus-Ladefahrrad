package com.example.gtflauncher;

import android.app.Activity;
import android.os.Bundle;

import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceView;
import android.view.View;

import com.elektrobit.gtf.GtfBridge;
import com.elektrobit.gtf.jni.callback.OnShutdownFromNativeListener;

import java.io.File;

public class LauncherActivity extends Activity implements OnShutdownFromNativeListener
{
    private SurfaceView gtfSurface;

    private final String LOG_TAG = "LauncherActivity";

    protected final String GTF_STARTUP_CFG = "gtfStartup.cfg";

    private GtfBridge gtfBridge;

    /* Android lifecycle management */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        String startupConfigFile = getStartupConfigFile();

        setContentView(R.layout.main);

        if(!startupConfigFile.equals(""))
        {
            // Initialize GTF
            initGtfSurface();
            gtfBridge = new GtfBridge(this);
            gtfBridge.loadGtfLibs(getModelPath(), getAdditionalNativeLibs(), null);
            gtfBridge.initNativeGtf(getApplicationInfo(), startupConfigFile, this, null);
            gtfBridge.setGtfSurfaceView(gtfSurface);
        }
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus)
    {
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus)
        {
            // Set fullscreen immersive
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
                            //| View.SYSTEM_UI_FLAG_IMMERSIVE);
        }
    }

    private void initGtfSurface()
    {
        gtfSurface = (SurfaceView) findViewById(R.id.hmiSurface);
        gtfSurface.bringToFront();
        gtfSurface.setZOrderOnTop(true);

        gtfSurface.getHolder().setKeepScreenOn(true);
    }

    /* Touch passthrough */
    @Override
    public boolean dispatchKeyEvent(KeyEvent event)
    {
        if (gtfBridge != null)
        {
            gtfBridge.dispatchKeyEvent(event);
        }

        return super.dispatchKeyEvent(event);
    }


    /**
     * Standard, but generic version of returning the method to return
     * the path to the model files.
     * <p/>
     * Use this if you placed your model files in the correct standard model
     * files directory on the device storage.
     *
     * @return The <b>standard model path</b> which is recommended to use by our guidelines
     * or <b>null</b> if the path could not be found.
     */
    protected String getStandardModelPath()
    {
        File f = getExternalFilesDir(null);
        if (isExistingDirectory(f))
        {
            return f.getAbsolutePath();
        }
        else
        {
            Log.e(LOG_TAG, "The default model path could not be found, please specify another directory!");
            return null;
        }
    }

    /**
     * This method is used to return the path where the Gtf Startup configuration file
     * is located on the device storage.<br>
     * The getModelPath() method has to be overwritten or this method will return a empty string.
     *
     * @return The path to the GTF Startup config file based on the getModelPath() value.     *
     */
    public String getStartupConfigFile()
    {
        String temp = getModelPath();
        if (temp != null)
        {
            File f = new File(temp, GTF_STARTUP_CFG);
            if (f.exists())
            {
                return f.getAbsolutePath();
            }
            else
            {
                Log.e(LOG_TAG, "gtfStartup.cfg file does not exist!");
                return "";
            }
        }
        else
        {
            return "";
        }
    }

    /* Helper methods */
    private boolean isExistingDirectory(File f)
    {
        return f != null && f.exists() && f.isDirectory();
    }

    /* Callback from native code when gtf has shutdown */
    @Override
    public void onNativeShutdown()
    {
        finish();
    }

    private String getModelPath()
    {
        // @TUM: Return alternative model path here if required
        Log.i(LOG_TAG, "Using model path: " + getStandardModelPath());
        return getStandardModelPath();
    }

    private String[] getAdditionalNativeLibs()
    {
        // @TUM: Return an array of additional libraries instead of null if required

        return new String[]{"/libmyapp.so"};
        //return null;
    }
}