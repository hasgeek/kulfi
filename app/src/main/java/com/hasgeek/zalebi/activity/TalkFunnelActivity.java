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

import com.bugsnag.android.Bugsnag;

import com.hasgeek.zalebi.R;
import com.hasgeek.zalebi.fragment.BadgeScannerFragment;
import com.hasgeek.zalebi.fragment.ChatFragment;
import com.hasgeek.zalebi.fragment.ContactFragment;
import com.hasgeek.zalebi.fragment.SessionFragment;
import com.hasgeek.zalebi.network.AttendeeListFetcher;

import java.util.ArrayList;
import java.util.List;


public class TalkFunnelActivity extends AppCompatActivity implements BadgeScannerFragment.onContactFetchListener {

    // Get rid of these once we have support for multiple conferences
    public static final String SPACE_ID = "56";
    public static final String SPACE_NAME = "MetaRefresh";
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_talk_funnel);

        Bugsnag.init(this);

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
        tabLayout.setTabTextColors(getResources().getColor(R.color.tab_normal), getResources().getColor(R.color.tab_selected));
        tabLayout.setupWithViewPager(mViewPager);
        new AttendeeListFetcher(this).fetch();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_talk_funnel, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_login) {
            String url = "http://auth.hasgeek.com/auth?client_id=eDnmYKApSSOCXonBXtyoDQ&scope=id+email+phone+organizations+teams+com.talkfunnel:*&response_type=token";
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            startActivity(intent);
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
