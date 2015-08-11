package com.hasgeek.zalebi.fragment;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hasgeek.zalebi.R;
import com.hasgeek.zalebi.adapter.ContactListAdapter;
import com.hasgeek.zalebi.model.Contact;

import java.util.List;

public class ContactFragment extends Fragment {
    RecyclerView mRecyclerView;
    ContactListAdapter mContactListAdapter;
    public ContactFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        bindScanBadgeFab();
        mRecyclerView = (RecyclerView) inflater.inflate(R.layout.fragment_contact, container, false);
        mRecyclerView = (RecyclerView) mRecyclerView.findViewById(R.id.contact_list);
        //recyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mRecyclerView.getContext()));
        List<Contact> contacts = Contact.listAll(Contact.class);
        mContactListAdapter = new ContactListAdapter(contacts);
        mRecyclerView.setAdapter(mContactListAdapter);
        return mRecyclerView;
    }

    private void bindScanBadgeFab() {
        FloatingActionButton scanBadgeFab = (FloatingActionButton) getActivity().findViewById(R.id.scan_badge);
        scanBadgeFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                BadgeScannerFragment badgeScannerFragment = new BadgeScannerFragment();
                badgeScannerFragment.show(fragmentManager, "scan");
            }
        });
    }

    public void updateContactList(){
        List<Contact> contacts = Contact.listAll(Contact.class);
        mContactListAdapter.updateContactList(contacts);
    }
}
