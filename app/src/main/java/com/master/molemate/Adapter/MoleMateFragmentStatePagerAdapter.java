package com.master.molemate.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class MoleMateFragmentStatePagerAdapter extends FragmentStatePagerAdapter {

    private final List<Fragment> fragmentList = new ArrayList<Fragment>();
    private final List<String> fragmentTitleList = new ArrayList<String>();

    public MoleMateFragmentStatePagerAdapter(FragmentManager fm, int behavior) {
        super(fm, behavior);
        //behavior should be FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
    }

    public boolean addFragment(Fragment fragment, String title){
        return fragmentList.add(fragment) && fragmentTitleList.add(title);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);

    }

    public int getItemWithTitle(String title){
        return fragmentTitleList.indexOf(title);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    public String getFragmentTitle(int fragmentPosition){
        return fragmentTitleList.get(fragmentPosition);
    }
}
