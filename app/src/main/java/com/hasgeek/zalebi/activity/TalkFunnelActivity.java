package com.hasgeek.zalebi.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.bugsnag.android.Bugsnag;

import com.hasgeek.zalebi.BuildConfig;
import com.hasgeek.zalebi.R;
import com.hasgeek.zalebi.fragment.BadgeScannerFragment;
import com.hasgeek.zalebi.fragment.ChatFragment;
import com.hasgeek.zalebi.fragment.ContactDetailDialogFragment;
import com.hasgeek.zalebi.fragment.ContactFragment;
import com.hasgeek.zalebi.fragment.SessionFragment;
import com.hasgeek.zalebi.model.Space;
import com.hasgeek.zalebi.network.AttendeeListFetcher;
import com.hasgeek.zalebi.network.AuthHelper;
import com.hasgeek.zalebi.sync.SyncProvider;
import com.hasgeek.zalebi.util.Config;

import java.util.ArrayList;
import java.util.List;


public class TalkFunnelActivity extends AppCompatActivity implements BadgeScannerFragment.onContactFetchListener, ContactDetailDialogFragment.onContactDeleteListener {

    // Get rid of these once we have support for multiple conferences
    public static final String SPACE_ID = "55";
    public static final String SPACE_NAME = "JSFoo";
    private ViewPager mViewPager;
    AuthHelper authHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_talk_funnel);

        if(!BuildConfig.DEBUG) {
            Bugsnag.init(this);
            
        }

        authHelper =  new AuthHelper(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(SPACE_NAME);

        final ActionBar actionBar = getSupportActionBar();

        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        if (mViewPager != null) {
            setupViewPager(mViewPager);
        }
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setTabTextColors(getResources().getColor(R.color.tab_normal),
                getResources().getColor(R.color.tab_selected));
        tabLayout.setupWithViewPager(mViewPager);
        if(Space.count(Space.class,"",new String[]{}) == 0){
            Log.d("hasgeek","need to fetch data as no data exists");
            new SyncProvider().sync(this);
        }
        else{
            Log.d("hasgeek","no need to sync, as some data exists");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_talk_funnel, menu);

        if(authHelper.isLoggedIn()){
            MenuItem logoutOption = menu.findItem(R.id.action_logout);
            logoutOption.setVisible(true);
        }else{
            MenuItem loginOption = menu.findItem(R.id.action_login);
            loginOption.setVisible(true);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_login) {

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(Config.AUTH_URL));
            startActivity(intent);
        }

        if(id == R.id.action_logout){
            authHelper.invalidateSession();
            Toast.makeText(this,"Successfully Logged out", Toast.LENGTH_LONG).show();
            invalidateOptionsMenu();
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getSupportFragmentManager());
        adapter.addFragment(new SessionFragment(), getString(R.string.schedule));
        adapter.addFragment(new ContactFragment(), getString(R.string.contacts));
        adapter.addFragment(new ChatFragment(), getString(R.string.chat));
        viewPager.setAdapter(adapter);
        final FloatingActionButton scanBadge = (FloatingActionButton) findViewById(R.id.scan_badge);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                /* Need a better way of doing this
                position=1 => Contact fragment
                */

                if (position == 1) {
                    scanBadge.setVisibility(View.VISIBLE);
                } else {
                    scanBadge.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public Adapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }

        public Fragment getFragment(int position) {
            return mFragments.get(position);
        }
    }


    @Override
    public void onContactFetchComplete() {
        updateContactFragment();
    }

    @Override
    public void onContactDelete() {
        updateContactFragment();
    }

    private void updateContactFragment() {
        try {
            int currentItem = mViewPager.getCurrentItem();
            Adapter adapter = (Adapter) mViewPager.getAdapter();
            ContactFragment contactFragment = (ContactFragment) adapter.getFragment(currentItem);
            contactFragment.updateContactList();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("funnel", e.getMessage());
        }
    }
}
