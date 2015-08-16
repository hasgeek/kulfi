package com.hasgeek.zalebi.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.hasgeek.zalebi.R;
import com.hasgeek.zalebi.model.Contact;

/**
 * Created by heisenberg on 16/08/15.
 */
public class ContactDetailDialogFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Contact contact = getArguments().getParcelable("contact");
                Intent intent = new Intent(Intent.ACTION_INSERT);
                intent.setType(ContactsContract.Contacts.CONTENT_TYPE);

                intent.putExtra(ContactsContract.Intents.Insert.NAME, contact.getFullname());
                intent.putExtra(ContactsContract.Intents.Insert.PHONE, contact.getPhone());
                intent.putExtra(ContactsContract.Intents.Insert.COMPANY, contact.getCompany());
                intent.putExtra(ContactsContract.Intents.Insert.EMAIL, contact.getEmail());
                intent.putExtra(ContactsContract.Intents.Insert.JOB_TITLE, contact.getJobTitle());
                startActivity(intent);
            }
        }).setNegativeButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setCancelable(true).setView(buildView());
        return builder.create();
    }

    private View buildView() {
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.contact_detail, null);
        Bundle bundle = getArguments();
        Contact contact = bundle.getParcelable("contact");
        ((TextView)view.findViewById(R.id.contact_name)).setText(contact.getFullname());
        ((TextView)view.findViewById(R.id.contact_phone)).setText(contact.getPhone());
        ((TextView)view.findViewById(R.id.contact_email)).setText(contact.getEmail());
        ((TextView)view.findViewById(R.id.contact_organization)).setText(contact.getCompany());
        return view;
    }
}
