package pe.com.jclpsoft.waveapps.adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

import pe.com.jclpsoft.waveapps.DetailTransacActivity;
import pe.com.jclpsoft.waveapps.R;
import pe.com.jclpsoft.waveapps.TransactionFragment;
import pe.com.jclpsoft.waveapps.WaveAppsApplication;
import pe.com.jclpsoft.waveapps.models.Transact;
import pe.com.jclpsoft.waveapps.models.WaveAppsService;

public class TransactionRecyclerViewAdapter extends RecyclerView.Adapter<TransactionRecyclerViewAdapter.ViewHolder> {

    private List<Transact> mTransacts;
    private final TransactionFragment.OnListFragmentInteractionListener mListener;
    private final Activity activity;
    private WaveAppsService waveAppsService;

    public TransactionRecyclerViewAdapter(Activity activity,List<Transact> items, TransactionFragment.OnListFragmentInteractionListener listener) {
        this.mTransacts = items;
        this.mListener = listener;
        this.activity=activity;
        this.waveAppsService=((WaveAppsApplication) this.activity.getApplication()).getWaveAppsService();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_transaction, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mTransact = mTransacts.get(position);
        holder.mDescriptionTextView.setText(mTransacts.get(position).description);
        holder.mDateTextView.setText(mTransacts.get(position).date);

        final Locale locale = new Locale("es", "PE");
        final NumberFormat nf = NumberFormat.getCurrencyInstance(locale);

        holder.mAmountTextView.setText(nf.format(mTransacts.get(position).amount));
        holder.mAmountTextView.setTextColor(activity.getResources().getColor((String.valueOf(mTransacts.get(position).type.getId()).equals("1")) ? android.R.color.holo_green_dark : android.R.color.holo_red_dark));
        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(activity, "Transact with id : " + String.valueOf(mTransacts.get(position).getId()), Toast.LENGTH_SHORT).show();
            }
        });

        holder.mViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*MaterialDialog materialDialog=new MaterialDialog.Builder(activity)
                        .title("Transact # "+String.valueOf(mTransacts.get(position).getId()))
                        .customView(R.layout.custom_view, true)
                        .positiveText("OK")
                        .positiveColorRes(R.color.colorAccent)
                        .titleColorRes(R.color.colorPrimaryDark)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
                View mDialogView=materialDialog.getCustomView();
                TextView mDescriptionTextView= (TextView)mDialogView.findViewById(R.id.descriptionTextView);
                TextView mTypeTextView=(TextView)mDialogView.findViewById(R.id.typeTextView);
                TextView mCategoryTextView=(TextView)mDialogView.findViewById(R.id.categoryTextView);
                TextView mAmountTextView=(TextView)mDialogView.findViewById(R.id.amountTextView);
                ImageView mPhotoImageView=(ImageView)mDialogView.findViewById(R.id.photoImageView);
                mDescriptionTextView.setText(mTransacts.get(position).description);
                mTypeTextView.setText((waveAppsService.findTypeById(Integer.parseInt(String.valueOf(mTransacts.get(position).getId())))).type);
                mCategoryTextView.setText((waveAppsService.findCategoryById(Integer.parseInt(String.valueOf(mTransacts.get(position).getId())))).category);
                mAmountTextView.setText(nf.format(mTransacts.get(position).amount));
                Picasso.with(activity).load("file:"+Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)+ File.separator+mTransacts.get(position).url+".jpg").resize(150, 150).centerCrop().into(mPhotoImageView);
            */
                Intent intent = new Intent(activity, DetailTransacActivity.class);
                intent.putExtra("id", Integer.parseInt(String.valueOf(mTransacts.get(position).getId())));
                activity.startActivity(intent);
            }
        });

        holder.mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                waveAppsService.deleteTransaction(Integer.parseInt(String.valueOf(mTransacts.get(position).getId())));
                mTransacts=waveAppsService.listAllTransactions();
                notifyDataSetChanged();
            }
        });


       /* holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onListFragmentInteraction(holder.mTransact);
                }
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return mTransacts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final View mView;
        private final TextView mDescriptionTextView;
        private final CardView mCardView;
        private final TextView mDateTextView;
        private final TextView mAmountTextView;
        private Transact mTransact;
        private final ImageView mDeleteButton;
        private final ImageView mViewButton;

        private ViewHolder(View view) {
            super(view);
            mView = view;
            mCardView=(CardView)view.findViewById(R.id.transactionCardView);
            mDescriptionTextView = (TextView) view.findViewById(R.id.descriptionTextView);
            mDateTextView = (TextView) view.findViewById(R.id.dateTextView);
            mAmountTextView=(TextView)view.findViewById(R.id.amountTextView);
            mDeleteButton=(ImageView)view.findViewById(R.id.deleteButton);
            mViewButton=(ImageView)view.findViewById(R.id.viewButton);
        }

        /*@Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }*/
    }
}
