package com.tercalivre.blog;

import android.app.ProgressDialog;
import android.os.Bundle;
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
import com.tercalivre.blog.adapters.EndlessRecyclerOnScrollListener;
import com.tercalivre.blog.model.ObjetoAPI;
import com.tercalivre.blog.utils.NetworkCache;
import com.tercalivre.blog.utils.Settings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ListaPostagensActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView mRecyclerView;
    private List<ObjetoAPI.WordpressJson.Post> wordpressJson;
    private EndlessRecyclerOnScrollListener scrollListener;
    private RecyclerView.Adapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private ProgressDialog pDialog;
    private SwipeRefreshLayout swipeRefresh;
    private final int quantidadePorPagina = 10;


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

        mRecyclerView = (RecyclerView) findViewById(R.id.postagemReciclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        wordpressJson = new ArrayList<>();
        mAdapter = new CardPostagemAdapter(ListaPostagensActivity.this,wordpressJson);
        mRecyclerView.setAdapter(mAdapter);

        scrollListener =new EndlessRecyclerOnScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                getPostJSON(current_page);
            }
        };

        mRecyclerView.setOnScrollListener(scrollListener);

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


    private void getPostJSON(int pagina){
        showDialog();
        final String url = Settings.URL_MAIN+"&page="+pagina;

        try {
            RequestQueue queue = Volley.newRequestQueue(ListaPostagensActivity.this);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {;
                @Override
                public void onResponse(String response) {
                    GsonBuilder builder = new GsonBuilder();
                    Gson mGson = builder.create();
                    ObjetoAPI.WordpressJson wordpressResponse = mGson.fromJson(response, ObjetoAPI.WordpressJson.class);
                    swipeRefresh.setRefreshing(false);


                    if (wordpressResponse.posts.size() > 0){
                        Log.d("URL:",url);
                        Log.d("RESPONSE:",String.valueOf(wordpressResponse.posts.size()));

                        wordpressJson.addAll(wordpressResponse.posts);
                        //Collections.sort(wordpressJson);
                        //Collections.reverse(wordpressJson);
                        if (mAdapter.getItemCount()>0){
                            mAdapter.notifyDataSetChanged();
                        }



                    }


                    hideDialog();
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
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }


    public void refresh(){
        wordpressJson.clear();
        getPostJSON(1);
        scrollListener.resetState();

    }

}
