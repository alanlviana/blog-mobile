package com.tercalivre.blog.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
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
import com.tercalivre.blog.adapters.CardPostagemAdapter;
import com.tercalivre.blog.adapters.OnLoadMoreListener;
import com.tercalivre.blog.model.Post;
import com.tercalivre.blog.utils.NetworkCache;
import com.tercalivre.blog.utils.Settings;
import com.tercalivre.blog.wordpress.WordpressAPI;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alanl on 30/04/2017.
 */

public class RecyclerViewFragment extends Fragment {

    private static final String TAG = "RecyclerViewFragment";
    protected static final String CAT_ID = "id";
    protected static final String QUERY = "query";


    private String mQuery;
    private int mCatId;
    private AppCompatActivity mContext;
    private RecyclerView mRecyclerView;
    private List<Post> postagens;
    private CardPostagemAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private ProgressDialog pDialog;
    private SwipeRefreshLayout swipeRefresh;
    protected Handler handler;

    private int mPagina = 1;

    public static RecyclerViewFragment newInstance(int id) {
        RecyclerViewFragment fragment = new RecyclerViewFragment();
        Bundle args = new Bundle();
        args.putInt(CAT_ID, id);
        fragment.setArguments(args);
        return fragment;
    }
    public static RecyclerViewFragment newInstance(String query) {
        RecyclerViewFragment fragment = new RecyclerViewFragment();
        Bundle args = new Bundle();
        args.putString(QUERY, query);
        fragment.setArguments(args);
        return fragment;
    }
    public RecyclerViewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mCatId = getArguments().getInt(CAT_ID, -1);
            mQuery = getArguments().getString(QUERY, "");
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = (AppCompatActivity)getActivity();

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_lista_postagens, container, false);

        // Toolbar
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        mContext.setSupportActionBar(toolbar);

        // ProgressDialog
        pDialog = new ProgressDialog(mContext);
        pDialog.setCancelable(false);
        pDialog.setMessage("Carregando Posts");
        handler = new Handler();
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.postagemReciclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(mLayoutManager);
        postagens = new ArrayList<>();
        mAdapter = new CardPostagemAdapter(mContext,postagens,mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);


        mAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                //add null , so the adapter will check view_type and show progress bar at bottom
                postagens.add(null);
                mAdapter.notifyItemInserted(postagens.size() - 1);
                getPostJSON(mPagina,2000);
            }
        });

        swipeRefresh = (SwipeRefreshLayout) rootView.findViewById(R.id.postagens_swipe_refresh);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();

            }
        });

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        refresh();
    }

    private void getPostJSON(final int pagina, final int delay){
        final String url = Settings.URL_MAIN+"&page="+pagina;
        Log.i("POST_URL",url);
        try {
            if (mAdapter.isLoading()){
                return;
            }

            mAdapter.setLoading();

            RequestQueue queue = Volley.newRequestQueue(mContext);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {;
                @Override
                public void onResponse(String response) {
                    GsonBuilder builder = new GsonBuilder();
                    Gson mGson = builder.create();
                    final WordpressAPI.PostResponse wordpressResponse = mGson.fromJson(response, WordpressAPI.PostResponse.class);
                    swipeRefresh.setRefreshing(false);

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //   remove progress item
                            int ultimoItem = postagens.size() - 1;
                            if (ultimoItem >= 0 && postagens.get(ultimoItem) == null){
                                postagens.remove(ultimoItem);
                                mAdapter.notifyItemRemoved(postagens.size());
                            }
                            for (int i = 0; i <= wordpressResponse.posts.size()-1; i++) {
                                Post post = wordpressResponse.posts.get(i);
                                if (!postagens.contains(post)){
                                    postagens.add(post);

                                    mAdapter.notifyItemInserted(postagens.size());
                                }
                            }
                            Log.i("POST_QTD",String.valueOf(postagens.size()));
                            mPagina = (int)Math.floor(postagens.size()/10)+1;

                            if (wordpressResponse.pages == pagina){
                                mAdapter.setCompleteLoaded();
                            }

                            mAdapter.setLoaded();
                        }
                    }, 0);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    swipeRefresh.setRefreshing(false);
                    Toast.makeText(mContext,
                            "Check your Internet Connection,\nSwipe or click Refresh button.",
                            Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    String jsonString = new String(response.data);
                    return Response.success(jsonString, NetworkCache.parseIgnoreCacheHeaders(response));
                }
            };
            queue.add(stringRequest);
        } finally {

        }
    }
    public void refresh(){
        postagens.clear();
        postagens.add(null);
        mAdapter.setIncompleteLoaded();
        mAdapter.notifyDataSetChanged();


        mPagina = 1;
        getPostJSON(mPagina,1000);
    }




}