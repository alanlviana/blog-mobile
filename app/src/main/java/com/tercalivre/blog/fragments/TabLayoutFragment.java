package com.tercalivre.blog.fragments;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tercalivre.blog.ListaPostagensActivity;
import com.tercalivre.blog.R;
import com.tercalivre.blog.adapters.RecyclerViewFragmentPagerAdaptor;
import com.tercalivre.blog.model.Category;
import com.tercalivre.blog.model.Post;
import com.tercalivre.blog.utils.Settings;
import com.tercalivre.blog.wordpress.WordpressAPI;

import java.util.ArrayList;

/**
 * Created by alanl on 30/04/2017.
 */

public class TabLayoutFragment extends Fragment{

    private static final String TAG = "TAB_LAYOUT_FRAGMENT";

    private ListaPostagensActivity mActivity;

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private SearchView searchView;
    private MenuItem searchMenuItem;

    // List of all categories
    protected static ArrayList<Category> categories = null;
    private TabLayoutListener mListener;

    public static TabLayoutFragment newInstance() {
        TabLayoutFragment fragment = new TabLayoutFragment();;
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Stops onDestroy() and onCreate() being called when the parent
        // activity is destroyed/recreated on configuration change
        setRetainInstance(true);

        // Display a search menu
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_tab_layout, container, false);

        mActivity = (ListaPostagensActivity)getActivity();

        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.tab_toolbar);
        mActivity.setSupportActionBar(toolbar);
        mActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mActivity.getSupportActionBar().setHomeButtonEnabled(true);

        mTabLayout = (TabLayout) rootView.findViewById(R.id.tab_layout);
        mViewPager = (ViewPager) rootView.findViewById(R.id.viewpager);
        // Preload 1 page to either side of the current page
        mViewPager.setOffscreenPageLimit(1);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        loadCategories();
    }

    private void loadCategories() {

        categories = new ArrayList<>();

        categories.add(new Category(3,"deunatelha","#DeuNaTelha","1"));
        categories.add(new Category(5,"de-veritate","De Veritate","1"));

        RecyclerViewFragmentPagerAdaptor adaptor = new
                RecyclerViewFragmentPagerAdaptor(getChildFragmentManager(), categories);
        mViewPager.setAdapter(adaptor);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    public interface TabLayoutListener {
        void onSearchSubmitted(String query);
    }
}