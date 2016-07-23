package pe.com.jclpsoft.waveapps;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.google.gson.Gson;

import pe.com.jclpsoft.waveapps.models.WaveAppsService;

public class SplashScreenActivity extends AppCompatActivity {
    private static final int SPLASH_TIME_OUT = 3000;
    private Gson gson = new Gson();
    private static final String TAG = SplashScreenActivity.class.getSimpleName();
    WaveAppsService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
               new SplashScreen().execute();
            }
        }, SPLASH_TIME_OUT);
    }



    public class SplashScreen extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            service = ((WaveAppsApplication) getApplication()).getWaveAppsService();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            service.initDatabase();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            super.onPostExecute(aVoid);
            startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
            finish();
        }
    }
}
