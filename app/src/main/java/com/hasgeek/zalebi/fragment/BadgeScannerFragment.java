package com.hasgeek.zalebi.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.hasgeek.zalebi.R;
import com.hasgeek.zalebi.activity.TalkFunnelActivity;
import com.hasgeek.zalebi.model.Attendee;
import com.hasgeek.zalebi.model.Contact;
import com.hasgeek.zalebi.model.ContactQueue;
import com.hasgeek.zalebi.model.ScannedData;
import com.hasgeek.zalebi.model.Space;
import com.hasgeek.zalebi.network.ContactFetcher;
import com.hasgeek.zalebi.util.Config;

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
    private Attendee mAttendee;
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
            mScannedData = ScannedData.parse(result.getContents());
            List<Attendee> attendees = Attendee.find(Attendee.class, "puk = ?", mScannedData.getPuk());
            if (!attendees.isEmpty()) {
                mAttendee = attendees.get(0);
                List<Space> spaces = Space.find(Space.class, "space_id = ?", TalkFunnelActivity.SPACE_ID);
                if (!spaces.isEmpty()) {
                    mParticipantUrl = spaces.get(0).getUrl() + "participant" +
                            "?key=" + mScannedData.getKey() +
                            "&puk=" + mScannedData.getPuk();
                }
                Log.d("hasgeek", "participation URL" + mParticipantUrl);
                new AlertDialog.Builder(getActivity())
                        .setView(buildView(mAttendee))
                        .setCancelable(false)
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                addIncompleteContact();
                                new ContactFetcher(parentActivity, BadgeScannerFragment.this).fetch(mParticipantUrl);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).create().show();

            } else {
                Toast.makeText(getActivity(), "Attendee not found!", Toast.LENGTH_LONG).show();
            }
        } catch (ScannedData.UnknownBadgeException e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "Unknown badge!", Toast.LENGTH_LONG).show();
        }
    }

    private void addIncompleteContact() {
        Contact contact = new Contact();
        contact.setUserId(mAttendee.getUserId());
        contact.setFullname(mAttendee.getFullname());
        contact.setJobTitle(mAttendee.getJobTitle());
        contact.setCompany(mAttendee.getCompany());
        contact.save();
        mListener.onContactFetchComplete();
    }

    private View buildView(Attendee attendee) {
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.add_attendee, null);
        TextView attendeeName = (TextView) view.findViewById(R.id.attendee_name);
        TextView attendeeCompany = (TextView) view.findViewById(R.id.attendee_organization);
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
        if (ContactQueue.find(ContactQueue.class, "user_puk = ? and user_key = ?", mScannedData.getPuk(), mScannedData.getKey()).isEmpty()) {
            ContactQueue contactQueue = new ContactQueue(mScannedData.getPuk(), mScannedData.getKey(), mAttendee.getUserId(), TalkFunnelActivity.SPACE_ID, mParticipantUrl);
            contactQueue.save();
        }
    }

    public interface onContactFetchListener {
        void onContactFetchComplete();
    }
}
