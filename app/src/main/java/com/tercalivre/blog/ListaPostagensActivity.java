package com.tercalivre.blog;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
import com.tercalivre.blog.adapters.CardPostagemAdapter;
import com.tercalivre.blog.adapters.OnLoadMoreListener;
import com.tercalivre.blog.model.Post;
import com.tercalivre.blog.utils.NetworkCache;
import com.tercalivre.blog.utils.Settings;
import com.tercalivre.blog.wordpress.WordpressAPI;

import java.util.ArrayList;
import java.util.List;

public class ListaPostagensActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView mRecyclerView;
    private List<Post> postagens;
    private CardPostagemAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private ProgressDialog pDialog;
    private SwipeRefreshLayout swipeRefresh;
    protected Handler handler;

    private int mPagina = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_postagens);

        // Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // ProgressDialog
        pDialog = new ProgressDialog(ListaPostagensActivity.this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Carregando Posts");
        handler = new Handler();
        mRecyclerView = (RecyclerView) findViewById(R.id.postagemReciclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        postagens = new ArrayList<>();
        mAdapter = new CardPostagemAdapter(ListaPostagensActivity.this,postagens,mRecyclerView);
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

        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.postagens_swipe_refresh);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();

            }
        });

        refresh();





        // Drawer Layout
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.lista_postagens, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void getPostJSON(final int pagina, final int delay){
        showDialog();
        final String url = Settings.URL_MAIN+"&page="+pagina;
        Log.i("POST_URL",url);
        try {
            if (mAdapter.isLoading()){
                return;
            }

            mAdapter.setLoading();

            RequestQueue queue = Volley.newRequestQueue(ListaPostagensActivity.this);
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
                    hideDialog();
                    swipeRefresh.setRefreshing(false);
                    Toast.makeText(ListaPostagensActivity.this,
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



    private void showDialog() {
       // if (!pDialog.isShowing())
       //     pDialog.show();
    }

    private void hideDialog() {
        //if (pDialog.isShowing())
        //    pDialog.dismiss();
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
