package com.hasgeek.zalebi.activity;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.hasgeek.zalebi.R;
import com.hasgeek.zalebi.fragment.ChatFragment;
import com.hasgeek.zalebi.fragment.ContactFragment;
import com.hasgeek.zalebi.fragment.SessionFragment;

import java.util.ArrayList;
import java.util.List;


public class TalkFunnelActivity extends AppCompatActivity{

    // The authority for the sync adapter's content provider
    public static final String AUTHORITY = "com.hasgeek.zalebi.sync.provider";
    // An account type, in the form of a domain name
    public static final String ACCOUNT_TYPE = "hasgeek.com";
    // The account name
    public static final String ACCOUNT = "dummyaccount";
    private Account mAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_talk_funnel);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle("MetaRefresh");

        final ActionBar actionBar = getSupportActionBar();
//        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
//        actionBar.setDisplayHomeAsUpEnabled(true);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        if (viewPager != null) {
            setupViewPager(viewPager);
        }
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setTabTextColors(getResources().getColor(R.color.tab_normal),
                getResources().getColor(R.color.tab_selected));
        tabLayout.setupWithViewPager(viewPager);
        mAccount = createSyncAccount(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.menu_talk_funnel, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getSupportFragmentManager());
        adapter.addFragment(new SessionFragment(), getString(R.string.schedule));
        adapter.addFragment(new ContactFragment(), getString(R.string.contacts));
        adapter.addFragment(new ChatFragment(), getString(R.string.chat));
        viewPager.setAdapter(adapter);
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
    }

    /** Create a new dummy account for the sync adapter
    *
            * @param context The application context
    */
    public static Account createSyncAccount(Context context) {
        // Create the account type and default account
        Account newAccount = new Account(
                ACCOUNT, ACCOUNT_TYPE);
        // Get an instance of the Android account manager
        AccountManager accountManager =
                (AccountManager) context.getSystemService(
                        ACCOUNT_SERVICE);
        return newAccount;
    }

}
