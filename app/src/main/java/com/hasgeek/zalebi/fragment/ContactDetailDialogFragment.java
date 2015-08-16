package com.hasgeek.zalebi.fragment;

import android.app.Activity;
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

    private onContactDeleteListener mContactDeleteListener;
    private Contact mContact;

    public interface onContactDeleteListener{
        public void onContactDelete();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mContact = getArguments().getParcelable("contact");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Intent.ACTION_INSERT);
                intent.setType(ContactsContract.Contacts.CONTENT_TYPE);

                intent.putExtra(ContactsContract.Intents.Insert.NAME, mContact.getFullname());
                intent.putExtra(ContactsContract.Intents.Insert.PHONE, mContact.getPhone());
                intent.putExtra(ContactsContract.Intents.Insert.COMPANY, mContact.getCompany());
                intent.putExtra(ContactsContract.Intents.Insert.EMAIL, mContact.getEmail());
                intent.putExtra(ContactsContract.Intents.Insert.JOB_TITLE, mContact.getJobTitle());
                startActivity(intent);
            }
        }).setNegativeButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("Confirm")
                        .setMessage("Are you sure you want to delete this contact?")
                        .setPositiveButton("Yes, Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Contact contact = getArguments().getParcelable("contact");
                                contact.delete();
                                mContactDeleteListener.onContactDelete();
                            }
                        })
                        .setNegativeButton("No", null)
                        .create()
                        .show();

            }
        }).setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setCancelable(true).setView(buildView());
        return builder.create();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mContactDeleteListener = (onContactDeleteListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement onContactFetchListener");
        }

    }

    private View buildView() {
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.contact_detail, null);
        Bundle bundle = getArguments();
        ((TextView)view.findViewById(R.id.contact_name)).setText(mContact.getFullname());
        ((TextView)view.findViewById(R.id.contact_phone)).setText(mContact.getPhone());
        ((TextView)view.findViewById(R.id.contact_email)).setText(mContact.getEmail());
        ((TextView)view.findViewById(R.id.contact_organization)).setText(mContact.getCompany());
        return view;
    }
}
