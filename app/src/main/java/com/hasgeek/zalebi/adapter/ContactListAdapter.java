package com.hasgeek.zalebi.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hasgeek.zalebi.R;
import com.hasgeek.zalebi.model.Contact;

import java.util.List;

/**
 * Created by heisenberg on 24/07/15.
 */
public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.ContactViewHolder> {
    List<Contact> mContacts;

    public ContactListAdapter(List<Contact> mContacts) {
        this.mContacts = mContacts;
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView confName;
        TextView personName;
        TextView personDesc;

        public ContactViewHolder(View view) {
            super(view);
            confName = (TextView) view.findViewById(R.id.conf_name);
            personName = (TextView) view.findViewById(R.id.person_name);
            personDesc = (TextView) view.findViewById(R.id.person_desc);
        }

        @Override
        public void onClick(View v) {

        }
    }

    @Override
    public ContactListAdapter.ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_list_item, parent, false);
        return new ContactViewHolder(view);

    }

    @Override
    public void onBindViewHolder(ContactListAdapter.ContactViewHolder holder, int position) {
        Contact contact = mContacts.get(position);
        holder.personName.setText(contact.getFullname());
        holder.personDesc.setText(constructPersonDescription(contact));
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
