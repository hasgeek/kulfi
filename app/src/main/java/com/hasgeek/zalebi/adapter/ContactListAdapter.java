package com.hasgeek.zalebi.adapter;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hasgeek.zalebi.R;
import com.hasgeek.zalebi.fragment.ContactDetailDialogFragment;
import com.hasgeek.zalebi.model.Contact;

import java.util.List;

/**
 * Created by heisenberg on 24/07/15.
 */
public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.ContactViewHolder> {
    List<Contact> mContacts;
    FragmentActivity mParentActivity;

    public ContactListAdapter(List<Contact> mContacts, FragmentActivity activity) {
        this.mContacts = mContacts;
        this.mParentActivity = activity;
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView confName;
        TextView personName;
        TextView personDesc;
        View containerView;

        public ContactViewHolder(View view) {
            super(view);
            containerView = view;
            confName = (TextView) view.findViewById(R.id.conf_name);
            personName = (TextView) view.findViewById(R.id.person_name);
            personDesc = (TextView) view.findViewById(R.id.person_desc);
        }

        @Override
        public void onClick(View v) {
        }

        public View getContainerView() {
            return containerView;
        }
    }

    @Override
    public ContactListAdapter.ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_list_item, parent, false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ContactListAdapter.ContactViewHolder holder, int position) {
        final Contact contact = mContacts.get(position);
        holder.personName.setText(contact.getFullname());
        holder.personDesc.setText(constructPersonDescription(contact));
        holder.getContainerView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContactDetailDialogFragment contactDetailDialogFragment = new ContactDetailDialogFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelable("contact", contact);
                contactDetailDialogFragment.setArguments(bundle);
                contactDetailDialogFragment.show(mParentActivity.getSupportFragmentManager(), "contact_detail");
            }
        });
    }

    @Override
    public int getItemCount() {
        return mContacts.size();
    }

    private String constructPersonDescription(Contact contact) {
        String jobTitle = (contact.getJobTitle() == null) ? "" : contact.getJobTitle();
        String company = (contact.getCompany() == null) ? "" : contact.getCompany();
        return jobTitle + ", " + company;
    }

    public void updateContactList(List<Contact> contacts) {
        mContacts.clear();
        mContacts.addAll(contacts);
        notifyDataSetChanged();
    }
}
