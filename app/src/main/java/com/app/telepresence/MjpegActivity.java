package com.app.telepresence;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

public class MjpegActivity extends Activity {
	private static final boolean DEBUG=false;
    private static final String TAG = "MJPEG";

    private MjpegView mv = null;
    private MjpegView mv2 = null;
    
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
        WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        setContentView(R.layout.main);
        mv = (MjpegView) findViewById(R.id.mv);
        mv2= (MjpegView) findViewById(R.id.mv2);
        
        // receive parameters from PreferenceActivity
        Bundle bundle = getIntent().getExtras();
        String hostname = bundle.getString( Preferences.KEY_HOSTNAME);
        String portnum =  bundle.getString( Preferences.KEY_PORTNUM);
        new DoRead().execute( hostname, portnum);
        new DoRead1().execute(hostname, portnum);
    }

    
    public void onResume() {
    	if(DEBUG) Log.d(TAG,"onResume()");
        super.onResume();
       /* if(mv2!=null){
        	mv2.resumePlayback();
            //mv2.resumePlayback();
        }*/
        if(mv!=null){
            mv.resumePlayback();
            //mv2.resumePlayback();
        }

    }

    public void onStart() {
    	if(DEBUG) Log.d(TAG,"onStart()");
        super.onStart();
    }
    public void onPause() {
    	if(DEBUG) Log.d(TAG,"onPause()");
        super.onPause();
        if(mv2!=null){
            mv2.stopPlayback();

        }
        if(mv!=null){
            mv.stopPlayback();
        }
    }
    public void onStop() {
    	if(DEBUG) Log.d(TAG,"onStop()");
        super.onStop();
    }

    public void onDestroy() {
    	if(DEBUG) Log.d(TAG,"onDestroy()");
    	
    	if(mv2!=null){
    		mv2.freeCameraMemory();
            //mv2.freeCameraMemory();
    	}
        if(mv!=null){
            mv.freeCameraMemory();
        }
    	
        super.onDestroy();
    }
    
    public class DoRead extends AsyncTask<String, Void, MjpegInputStream> {
    	protected MjpegInputStream doInBackground( String... params){
    		Socket socket = null;
    		try {
				socket = new Socket( params[0], Integer.valueOf( params[1]));
	    		return (new MjpegInputStream(socket.getInputStream()));
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		return null;
    	}
    	
        protected void onPostExecute(MjpegInputStream result) {
            mv.setSource(result);
            if(result!=null) result.setSkip(1);
            mv.setDisplayMode(MjpegView.SIZE_BEST_FIT);
            mv.showFps(true);
//            mv2.setDisplayMode(MjpegView.SIZE_BEST_FIT);
//            mv2.showFps(true);
        }
    }


    public class DoRead1 extends AsyncTask<String, Void, MjpegInputStream> {
        protected MjpegInputStream doInBackground( String... params){
            Socket socket = null;
            try {
                socket = new Socket( params[0], Integer.valueOf( params[1]));
                return (new MjpegInputStream(socket.getInputStream()));
            } catch (UnknownHostException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(MjpegInputStream result) {
            mv2.setSource(result);
            if(result!=null) result.setSkip(1);
            mv2.setDisplayMode(MjpegView.SIZE_BEST_FIT);
            mv2.showFps(true);
//            mv2.setDisplayMode(MjpegView.SIZE_BEST_FIT);
//            mv2.showFps(true);
        }
    }
}