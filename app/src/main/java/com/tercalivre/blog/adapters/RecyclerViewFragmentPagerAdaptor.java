package com.tercalivre.blog.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.tercalivre.blog.fragments.RecyclerViewFragment;
import com.tercalivre.blog.model.Category;

import java.util.ArrayList;

/**
 * Created by alanl on 30/04/2017.
 */

public class RecyclerViewFragmentPagerAdaptor extends FragmentStatePagerAdapter {
    private ArrayList<Category> categories;

    public RecyclerViewFragmentPagerAdaptor(FragmentManager fm, ArrayList<Category> categories) {
        super(fm);
        this.categories = categories;
    }

    @Override
    public Fragment getItem(int position) {
        return RecyclerViewFragment.newInstance(categories.get(position).id);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return categories.get(position).title;
    }

    @Override
    public int getCount() {
        return categories.size();
    }


}
