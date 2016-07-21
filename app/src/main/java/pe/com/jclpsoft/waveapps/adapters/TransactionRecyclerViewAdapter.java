package pe.com.jclpsoft.waveapps.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;

import pe.com.jclpsoft.waveapps.R;
import pe.com.jclpsoft.waveapps.TransactionFragment;
import pe.com.jclpsoft.waveapps.models.Transact;

public class TransactionRecyclerViewAdapter extends RecyclerView.Adapter<TransactionRecyclerViewAdapter.ViewHolder> {

    private final List<Transact> mTransacts;
    private final TransactionFragment.OnListFragmentInteractionListener mListener;

    public TransactionRecyclerViewAdapter(List<Transact> items, TransactionFragment.OnListFragmentInteractionListener listener) {
        mTransacts = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_transaction, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mTransact = mTransacts.get(position);
        holder.mDescriptionTextView.setText(mTransacts.get(position).description);
        holder.mDateTextView.setText(mTransacts.get(position).date);
        holder.mAmountTextView.setText(String.valueOf(mTransacts.get(position).amount));

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mTransact);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mTransacts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final View mView;
        private final TextView mDescriptionTextView;
        private final TextView mDateTextView;
        private final TextView mAmountTextView;
        private Transact mTransact;

        private ViewHolder(View view) {
            super(view);
            mView = view;
            mDescriptionTextView = (TextView) view.findViewById(R.id.descriptionTextView);
            mDateTextView = (TextView) view.findViewById(R.id.dateTextView);
            mAmountTextView=(TextView)view.findViewById(R.id.amountTextView);
        }

        /*@Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }*/
    }
}
