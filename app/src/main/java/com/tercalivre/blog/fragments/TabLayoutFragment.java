package com.tercalivre.blog.fragments;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
    private RecyclerViewFragmentPagerAdaptor adaptor;

    // List of all categories
    private ArrayList<Category> categories = null;
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

        Log.d(TAG,"Criado adaptor");


        // Display a search menu
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_tab_layout, container, false);

        mActivity = (ListaPostagensActivity)getActivity();

        mTabLayout = (TabLayout) rootView.findViewById(R.id.tab_layout);
        mViewPager = (ViewPager) rootView.findViewById(R.id.viewpager);
        // Preload 1 page to either side of the current page
        mViewPager.setOffscreenPageLimit(1);
        categories = new ArrayList<>();
        adaptor = new RecyclerViewFragmentPagerAdaptor(getChildFragmentManager(), categories);
        mViewPager.setAdapter(adaptor);
        mTabLayout.setupWithViewPager(mViewPager);

        loadCategories();

        return rootView;
    }



    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

    private void loadCategories() {

        RequestQueue queue = Volley.newRequestQueue(mActivity);
        StringRequest request = new StringRequest(Settings.URL_CATEGORY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                GsonBuilder builder = new GsonBuilder();
                Gson mGson = builder.create();
                final WordpressAPI.CategoryResponse categoryResponse = mGson.fromJson(response,WordpressAPI.CategoryResponse.class);
                categories.add(new Category(-1,"Todos","Todos","1"));
                categories.addAll(categoryResponse.categories);
                adaptor.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                categories.add(new Category(-1,"Todos","Todos","1"));
                adaptor.notifyDataSetChanged();
            }
        });
        queue.add(request);



    }

    public interface TabLayoutListener {
        void onSearchSubmitted(String query);
    }
}
