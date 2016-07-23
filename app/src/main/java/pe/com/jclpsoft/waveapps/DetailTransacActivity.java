package pe.com.jclpsoft.waveapps;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.NumberFormat;
import java.util.Locale;

import pe.com.jclpsoft.waveapps.models.Transact;
import pe.com.jclpsoft.waveapps.models.WaveAppsService;

public class DetailTransacActivity extends AppCompatActivity {
    private int id;
    private WaveAppsService waveAppsService;
    private Transact mTransact;
    private TextView mDescriptionTextView;
    private TextView mTypeTextView;
    private TextView mCategoryTextView;
    private TextView mAmountTextView;
    private ImageView mPhotoImageView;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_transac);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.waveAppsService=((WaveAppsApplication)getApplication()).getWaveAppsService();
        id = getIntent().getExtras().getInt("id");
        mTransact=waveAppsService.obtainTransactById(id);
        mDescriptionTextView= (TextView)findViewById(R.id.descriptionTextView);
        mTypeTextView=(TextView)findViewById(R.id.typeTextView);
        mCategoryTextView=(TextView)findViewById(R.id.categoryTextView);
        mAmountTextView=(TextView)findViewById(R.id.amountTextView);
        mPhotoImageView=(ImageView)findViewById(R.id.photoImageView);
        loadData();
    }

    public void loadData(){
        final Locale locale = new Locale("es", "PE");
        final NumberFormat nf = NumberFormat.getCurrencyInstance(locale);
        mDescriptionTextView.setText(mTransact.description);
        mTypeTextView.setText((waveAppsService.findTypeById(Integer.parseInt(String.valueOf(mTransact.getId())))).type);
        mCategoryTextView.setText((waveAppsService.findCategoryById(Integer.parseInt(String.valueOf(mTransact.getId())))).category);
        mAmountTextView.setText(nf.format(mTransact.amount));
        Picasso.with(this).load("file:"+ Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)+ File.separator+mTransact.url+".jpg").into(mPhotoImageView);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            case R.id.action_settings:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
