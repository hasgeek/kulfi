package com.hasgeek.zalebi.activity;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hasgeek.zalebi.R;
import com.hasgeek.zalebi.model.Session;
import com.hasgeek.zalebi.util.DateTimeUtils;

public class SessionActivity extends AppCompatActivity {
    Session mSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session);
        mSession = getSessionDetailsFromBundle();

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(mSession.getTitle());
        collapsingToolbar.setExpandedTitleTextAppearance(R.style.expandedTitle);

        loadBackdrop();
    }

    private void loadBackdrop() {
        final ImageView imageView = (ImageView) findViewById(R.id.backdrop);
        Glide.with(this).load(R.drawable.bb).centerCrop().into(imageView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setSessionDetails();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.menu_session, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private Session getSessionDetailsFromBundle() {
        Bundle bundle = getIntent().getExtras();
        return bundle.getParcelable("session");
    }

    private void setSessionDetails() {
        //setSessionTextView(R.id.session_title, mSession.getTitle());
        setSessionTextView(R.id.session_day, DateTimeUtils.displayableDate(mSession.getStart()));
        setSessionTextView(R.id.session_time_interval,
                DateTimeUtils.displayableTimeInterval(mSession.getStart(), mSession.getEnd()));
        setSessionTextView(R.id.session_room, mSession.getRoom().replaceAll("-", " "));
        setSessionTextView(R.id.session_description, Html.fromHtml(mSession.getDescription()));
    }

    private void setSessionTextView(int id, CharSequence value) {
        TextView textView = (TextView) findViewById(id);
        textView.setText(value);
    }
}
