package com.ayysir.paek.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;

import com.ayysir.paek.R;
import com.ayysir.paek.tools.Constants;

public class SplashActivity extends Activity {

    public static final String KEY_PREFERENCES = "Intro";
    public static final String KEY_FIRST_RUN = "first_run";

    Activity mActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mActivity = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        Log.e(Constants.LOG_TAG, ">>> APP START");
        PreLoad preLoad = new PreLoad();
        preLoad.execute();
    }

    private class PreLoad extends AsyncTask<String, Integer, Void> {
        ProgressBar pb;

        @Override
        protected Void doInBackground(String... params) {
            long splashStartTime = System.currentTimeMillis();
            double currentProgress;
            long splashElapsedTime = 0;
            int minimumStayTime = 3000;
            while ((splashElapsedTime) < minimumStayTime) {
                splashElapsedTime = System.currentTimeMillis()
                        - splashStartTime;
                currentProgress = 100.0 * splashElapsedTime / minimumStayTime;
                publishProgress((int) currentProgress);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            /*super.onPostExecute(result);
            Intent intent = new Intent(SplashActivity.this, KernelTuner.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);*/

            //Button button = (Button) findViewById(R.id.introbtn);
            //button.setOnClickListener(new View.OnClickListener() {

            //  @Override
            //  public void onClick(View view) {
            Intent intent = new Intent(SplashActivity.this, WelcomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            //   }
            // });


        }

        @Override
        protected void onPreExecute() {
            pb = (ProgressBar) findViewById(R.id.progressBar1);
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            pb.setProgress(progress[0]);
        }

    }
}