package pe.com.jclpsoft.waveapps;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import pe.com.jclpsoft.waveapps.adapters.PagerAdapter;
import pe.com.jclpsoft.waveapps.models.Transact;
import pe.com.jclpsoft.waveapps.models.WaveAppsService;
import pe.com.jclpsoft.waveapps.network.Constantes;
import pe.com.jclpsoft.waveapps.network.VolleySingleton;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private Handler mHandler=new Handler();
    private FloatingActionButton fab;
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    public Uri lastOutputMediaFileUri = null;
    private PagerAdapter pagerAdapter;
    private ViewPager mViewPager;
    private TabLayout tabLayout;
    WaveAppsService service;
    private Gson gson = new Gson();
    private static final String TAG = SplashScreenActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        service = ((WaveAppsApplication) getApplication()).getWaveAppsService();
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mViewPager=(ViewPager) findViewById(R.id.content_main);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        commitFragment("Home",1);
        fab= (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.GONE);
        fab.setOnClickListener(onFabButtonClickListener());
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        loadTransactions();
    }

    public void loadTransactions(){
        VolleySingleton.getInstance(this).addToRequestQueue(
                new JsonObjectRequest(Request.Method.GET, Constantes.GET, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                // Procesar la respuesta Json
                                procesarRespuesta(response);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d(TAG, "Error Volley: " + error.getMessage());
                            }
                        }
                )
        );
    }

    /**
     * Interpreta los resultados de la respuesta y así
     * realizar las operaciones correspondientes
     *
     * @param response Objeto Json con la respuesta
     */
    private void procesarRespuesta(JSONObject response) {
        try {
            // Obtener atributo "estado"
            String estado = response.getString("estado");

            switch (estado) {
                case "1": // EXITO
                    Toast.makeText(this,"ok",Toast.LENGTH_SHORT).show();
                    // Obtener array "transact" Json
                    JSONArray mensaje = response.getJSONArray("transact");

                    // Parsear con Gson
                    for (int i=0; i<mensaje.length(); i++){

                        JSONObject json_data;
                        try {
                            json_data = mensaje.getJSONObject(i);

                            Transact t = new Transact();

                            t.date  =  json_data.getString("date").trim();
                            t.amount  =  Float.parseFloat(json_data.getString("amount").trim());
                            t.url  =  json_data.getString("url").trim();
                            t.category  =  service.findCategoryById(Integer.parseInt(json_data.getString("category").trim()));
                            t.type  =  service.findTypeById(Integer.parseInt(json_data.getString("type").trim()));
                            t.enable  =  Integer.parseInt(json_data.getString("enable").trim());
                            t.state = Integer.parseInt(json_data.getString("state").trim());

                            service.addNewTransaction(t);

                        } catch (JSONException e) { }
                    }
                    break;
                case "2": // FALLIDO
                    String mensaje2 = response.getString("mensaje");
                    Toast.makeText(this,mensaje2,Toast.LENGTH_LONG).show();
                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private View.OnClickListener onFabButtonClickListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = pagerAdapter.getFragment(tabLayout.getSelectedTabPosition());
                if(fragment!=null){
                    ((TransactionFragment)fragment).addNewTransaction();
                }
            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if(resultCode == RESULT_OK) {
                Log.d("Camerify", "ResultCode: RESULT_OK");
                String fileName = data != null ? data.getData().getPath() : lastOutputMediaFileUri.getPath();
                Toast.makeText(this, "Image saved to: "+ fileName, Toast.LENGTH_LONG).show();
            } else if(resultCode == RESULT_CANCELED) {
                Log.d("Camerify", "ResultCode: RESULT_CANCELED");
            } else {
                Log.d("Camerify", "ResultCode: "+Integer.toString(resultCode));
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_sync:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.nav_home:
                fab.setVisibility(View.GONE);
                commitFragment("Home",1);
                break;
            case R.id.nav_dashboard:
                fab.setVisibility(View.GONE);
                commitFragment("Dashboard",2);
                break;
            case R.id.nav_transactions:
                fab.setVisibility(View.VISIBLE);
                commitFragment("Transactions",3);
                break;
            case R.id.nav_manage:
                fab.setVisibility(View.GONE);
                break;
            case R.id.nav_share:
                fab.setVisibility(View.GONE);
                backup("1");
                break;
            case R.id.nav_send:
                fab.setVisibility(View.GONE);
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void commitFragment(String title,int position) {
        mHandler.post(new CommitFragmentRunnable(title,position));
    }

    private class CommitFragmentRunnable implements Runnable {

        private int position;
        private String title;

        CommitFragmentRunnable(String title,int position) {
            this.position=position;
            this.title=title;
        }

        @Override
        public void run() {
            pagerAdapter = new PagerAdapter(getSupportFragmentManager(),title,position);
            mViewPager.setAdapter(pagerAdapter);
            tabLayout.setupWithViewPager(mViewPager);
            //FragmentManager fragmentManager = getSupportFragmentManager();
            // fragmentManager.beginTransaction().replace(R.id.content_main, fragment).commit();

        }
    }

    private void backup(String x){
        File sd = new File(Environment.getExternalStorageDirectory() + "/WaveApps/");
        File data = Environment.getDataDirectory();
        if (!sd.exists()) { sd.mkdir(); }

        this.getPackageName();
        String currentDBPath = "/data/data/" + this.getPackageName() + "/databases/wave_express.db";
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd-HHmmss");
        Date date = new Date();
        String backupDBPath = x + "_backup" + dateFormat.format(date) + ".db";
        File currentDB = new File(currentDBPath);
        File backupDB = new File(sd, backupDBPath);

        if (currentDB.exists()) {
            try {
                FileChannel src = new FileInputStream(currentDB).getChannel();
                FileChannel dst = new FileOutputStream(backupDB).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
