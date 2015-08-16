package com.hasgeek.zalebi.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hasgeek.zalebi.R;
import com.hasgeek.zalebi.activity.TalkFunnelActivity;
import com.hasgeek.zalebi.model.Attendee;
import com.hasgeek.zalebi.model.Contact;
import com.hasgeek.zalebi.model.ContactQueue;
import com.hasgeek.zalebi.model.ScannedData;
import com.hasgeek.zalebi.network.ContactFetcher;
import com.hasgeek.zalebi.network.CustomJsonObjectRequest;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import me.dm7.barcodescanner.zbar.BarcodeFormat;
import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;


public class BadgeScannerFragment extends DialogFragment implements ZBarScannerView.ResultHandler, ContactFetcher.ContactFetchListener {
    private onContactFetchListener mListener;
    private RequestQueue mRequestQueue;
    private ScannedData mScannedData;
    private String mParticipantUrl;
    public ZBarScannerView mScannerView;

    public BadgeScannerFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mScannerView = new ZBarScannerView(getActivity());
        ArrayList<BarcodeFormat> list = new ArrayList<>();
        list.add(BarcodeFormat.CODE128);
        list.add(BarcodeFormat.QRCODE);
        mScannerView.setFormats(list);
        getDialog().setTitle("Scan badge");
        mRequestQueue = Volley.newRequestQueue(getActivity());
        return mScannerView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (onContactFetchListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement onContactFetchListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void handleResult(Result result) {
        final Activity parentActivity = getActivity();
        mScannerView.stopCamera();
        dismiss();
        try {
            //Only for testing, in future the space_id will be fetched from a global space context
            String ROOT_CONF_SPACE_ID = "54";
            mScannedData = ScannedData.parse(result.getContents());
            mParticipantUrl = "https://rootconf.talkfunnel.com/2015/participant" +
                    "?key=" + mScannedData.getKey() +
                    "&puk=" + mScannedData.getPuk();
            List<Attendee> attendees = Attendee.find(Attendee.class, "puk = ? and space_id = ?", mScannedData.getPuk(), ROOT_CONF_SPACE_ID);
            if(!attendees.isEmpty()){
                Attendee attendee = attendees.get(0);
                new AlertDialog.Builder(getActivity())
                        .setView(buildView(attendee))
                        .setCancelable(false)
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                new ContactFetcher(parentActivity, BadgeScannerFragment.this).fetch(mParticipantUrl);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).create().show();

            }else{
                Toast.makeText(getActivity(), "Attendee not found!", Toast.LENGTH_LONG).show();
            }
        } catch (ScannedData.UnknownBadgeException e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "Unknown badge!", Toast.LENGTH_LONG).show();
        }
    }

    private View buildView(Attendee attendee) {
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.add_attendee, null);
        TextView attendeeName = (TextView) view.findViewById(R.id.attendee_name);
        TextView attendeeCompany = (TextView) view.findViewById(R.id.attendee_company);
        attendeeName.setText(attendee.getFullname());
        attendeeCompany.setText(attendee.getCompany());
        return view;
    }

    @Override
    public void onContactFetchSuccess(Contact contact) {
        mListener.onContactFetchComplete();
    }

    @Override
    public void onContactFetchFailure() {
        if(ContactQueue.find(ContactQueue.class, "user_puk = ? and user_key = ?", mScannedData.getPuk(), mScannedData.getKey()).isEmpty()){
            ContactQueue contactQueue = new ContactQueue(mScannedData.getPuk(), mScannedData.getKey(), TalkFunnelActivity.SPACE_ID, mParticipantUrl);
            contactQueue.save();
        }
    }

    public interface onContactFetchListener {
        public void onContactFetchComplete();
    }
}
