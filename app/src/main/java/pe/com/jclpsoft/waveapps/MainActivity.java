package pe.com.jclpsoft.waveapps;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import pe.com.jclpsoft.waveapps.adapters.PagerAdapter;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private Handler mHandler=new Handler();
    private FloatingActionButton fab;
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private Uri lastOutputMediaFileUri = null;
    private PagerAdapter pagerAdapter;
    private ViewPager mViewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
            case R.id.action_settings:
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
}
