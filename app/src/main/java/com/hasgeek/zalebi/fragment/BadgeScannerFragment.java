package com.hasgeek.zalebi.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hasgeek.zalebi.model.Contact;
import com.hasgeek.zalebi.model.ScannedData;
import com.hasgeek.zalebi.network.CustomJsonObjectRequest;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import me.dm7.barcodescanner.zbar.BarcodeFormat;
import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;

public class BadgeScannerFragment extends DialogFragment implements ZBarScannerView.ResultHandler {
    private onContactFetchListener mListener;
    private RequestQueue mRequestQueue;
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
        ScannedData scannedData;
        GsonBuilder gsonBuilder = new GsonBuilder();
        final Gson gson = gsonBuilder.create();
        try {
            scannedData = ScannedData.parse(result.getContents());
            String participantUrl = "https://rootconf.talkfunnel.com/2015/participant" +
                    "?key=" + scannedData.getKey() +
                    "&puk=" + scannedData.getPuk();
            CustomJsonObjectRequest customJsonObjectRequest = new CustomJsonObjectRequest(Request.Method.GET, participantUrl, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Contact contact = gson.fromJson(response.optString("participant", "{}"), Contact.class);
                    List<Contact> persistedContacts = Contact.find(Contact.class, "user_id = ?", contact.getUserId());
                    if (persistedContacts.isEmpty()) {
                        contact.save();
                    } else {
                        for (Contact persistedContact : persistedContacts) {
                            persistedContact.delete();
                        }
                        contact.save();
                    }
                    mListener.onContactFetchComplete();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            }, getActivity());
            mRequestQueue.add(customJsonObjectRequest);
        } catch (ScannedData.UnknownBadgeException e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "Unknown badge!", Toast.LENGTH_LONG).show();
        } finally {
            mScannerView.stopCamera();
            dismiss();
        }
    }

    public interface onContactFetchListener {
        public void onContactFetchComplete();
    }
}
