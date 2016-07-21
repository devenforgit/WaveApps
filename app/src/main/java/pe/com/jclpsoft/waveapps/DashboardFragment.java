package pe.com.jclpsoft.waveapps;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.Locale;

import pe.com.jclpsoft.waveapps.models.WaveAppsService;

public class DashboardFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private TextView mIncomeTextView;
    private TextView mExpenseTextView;
    private TextView mNetIncomeTextView;
    private WaveAppsService service;

    public DashboardFragment() {
    }

    public static DashboardFragment newInstance() {
        return new DashboardFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        service = ((WaveAppsApplication) this.getActivity().getApplication()).getWaveAppsService();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_dashboard, container, false);
        mIncomeTextView=(TextView) view.findViewById(R.id.incomeTextView);
        mExpenseTextView=(TextView)view.findViewById(R.id.expenseTextView);
        mNetIncomeTextView=(TextView)view.findViewById(R.id.netIncomeTextView);
        loadTextView();
        return view;
    }

    private void loadTextView(){
        float income=service.incomeThisMonth();
        float expense=service.expenseThisMonth();
        float netIncome=service.netIncomeThisMonth();
        Locale locale = new Locale("es","PE");
        NumberFormat nf = NumberFormat.getCurrencyInstance(locale);
        mIncomeTextView.setText(nf.format(income));
        mExpenseTextView.setText(nf.format(expense));
        mNetIncomeTextView.setTextColor((netIncome>=0)?getResources().getColor(android.R.color.holo_green_dark):getResources().getColor(android.R.color.holo_red_dark));
        mNetIncomeTextView.setText(nf.format(netIncome));
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
/*
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
*/
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
