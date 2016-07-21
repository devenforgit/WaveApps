package pe.com.jclpsoft.waveapps;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.afollestad.materialdialogs.MaterialDialog;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import pe.com.jclpsoft.waveapps.adapters.TransactionRecyclerViewAdapter;
import pe.com.jclpsoft.waveapps.models.Category;
import pe.com.jclpsoft.waveapps.models.Transact;
import pe.com.jclpsoft.waveapps.models.Type;
import pe.com.jclpsoft.waveapps.models.WaveAppsService;

public class TransactionFragment extends Fragment {

    public List<Transact> transactList;
    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    WaveAppsService service;
    private Type type;
    private List<Category> categoryList;
    private List<Type> typeList;
    private static final int MEDIA_TYPE_IMAGE = 1;
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private Uri fileUri;
    private String url="";
    private Uri lastOutputMediaFileUri = null;
    private TransactionRecyclerViewAdapter transactionRecyclerViewAdapter;
    private View view;

    public TransactionFragment() {
    }

    public static TransactionFragment newInstance(int columnCount) {
        TransactionFragment fragment = new TransactionFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        service = ((WaveAppsApplication) this.getActivity().getApplication()).getWaveAppsService();
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_transaction_list, container, false);
        transactList =new ArrayList<>();
        loadRecycler();
        return view;
    }

    public void loadRecycler(){
        transactList =service.listAllTransactions();
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            transactionRecyclerViewAdapter=new TransactionRecyclerViewAdapter(transactList, mListener);
            recyclerView.setAdapter(transactionRecyclerViewAdapter);
        }
    }

    public void addNewTransaction(){
        final MaterialDialog dialog = new MaterialDialog.Builder(getContext())
                .title("Add New Transaction")
                .titleColorRes(R.color.colorPrimaryDark)
                .cancelable(false)
                .customView(R.layout.dialog_new_transaction, true)
                .show();
        View v=dialog.getCustomView();
        final AppCompatSpinner mTypeSpinner=(AppCompatSpinner)v.findViewById(R.id.typeSpinner);
        final AppCompatSpinner mCategorySpinner=(AppCompatSpinner)v.findViewById(R.id.categorySpinner);
        final AppCompatEditText mDescriptionEditText=(AppCompatEditText)v.findViewById(R.id.descriptionEditText);
        final AppCompatEditText mAmountEditText=(AppCompatEditText)v.findViewById(R.id.amountEditText);
        final AppCompatImageView mPhotoImageView=(AppCompatImageView)v.findViewById(R.id.photoImageView);
        final TextView mAddButton=(TextView)v.findViewById(R.id.addButton);
        final TextView mCancelButton=(TextView)v.findViewById(R.id.cancelButton);
        typeList=service.listAllTypes();
        ArrayAdapter<Type> typeArrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, typeList);
        typeArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mTypeSpinner.setAdapter(typeArrayAdapter);

        mTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                type=(Type)mTypeSpinner.getSelectedItem();
                categoryList=type.getCategories();
                ArrayAdapter<Category>categoryArrayAdapter=new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, categoryList);
                categoryArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mCategorySpinner.setAdapter(categoryArrayAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        mPhotoImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                getActivity().startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
                lastOutputMediaFileUri = fileUri;
            }
        });

        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        mAddButton.setOnClickListener(new View.OnClickListener() {
            boolean validate(){
                if(!mDescriptionEditText.getText().toString().equals("")){
                    if(!mAmountEditText.getText().toString().equals("")){
                        return true;
                    }else{
                        mAmountEditText.setError("Input Amount");
                        return false;
                    }
                }else{
                    mDescriptionEditText.setError("Input Description");
                    return false;
                }
            }

            Transact transaction(){
                Calendar calendar = GregorianCalendar.getInstance();
                Date dateTime = calendar.getTime();
                System.out.println(dateTime);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE d MMMM yyyy HH:mm:ss");
                String date=simpleDateFormat.format(dateTime);
                String description=mDescriptionEditText.getText().toString();
                float amount=Float.parseFloat(mAmountEditText.getText().toString());
                Category category=(Category)mCategorySpinner.getSelectedItem();
                Type type=(Type)mTypeSpinner.getSelectedItem();
                return new Transact(date,description,amount,url,category,type);
            }

            @Override
            public void onClick(View view) {
                if(validate()){
                    service.addNewTransaction(transaction());
                    loadRecycler();
                    dialog.dismiss();
                }
            }
        });
    }

    private Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    private File getOutputMediaFile(int type) {
        File mediaStorageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        if(!mediaStorageDir.exists()) {
            if(!mediaStorageDir.mkdirs()) {
                Log.d("Camerify", "Failed to create directory");
                return null;
            }
        }
        else {
            Log.d("Camerify", "Directory found");
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        url="IMG_"+timeStamp;
        File mediaFile;
        if(type == MEDIA_TYPE_IMAGE) mediaFile = new File(mediaStorageDir.getPath() + File.separator + url+ ".jpg");
        else return null;

        try {
            Log.d("Camerify", mediaFile.getCanonicalPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mediaFile;
    }

   /* @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }*/

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Transact item);
    }
}
