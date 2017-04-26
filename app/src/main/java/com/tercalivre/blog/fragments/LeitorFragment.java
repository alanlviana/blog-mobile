package com.tercalivre.blog.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.tercalivre.blog.R;
import com.tercalivre.blog.model.Post;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LeitorFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LeitorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LeitorFragment extends Fragment {
    private static final String ARG_TITLE = "param1";

    private String mParam1;
    private LeitorFragmentHolder holder;

    private OnFragmentInteractionListener mListener;

    public LeitorFragment() {
        // Required empty public constructor
    }

    public static LeitorFragment newInstance(Post post) {
        LeitorFragment fragment = new LeitorFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, "Teste");
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_TITLE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_leitor, container, false);
        holder = new LeitorFragmentHolder(view);
        return view;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    private class LeitorFragmentHolder{

        public AppBarLayout appbar;
        public CollapsingToolbarLayout collapsing_toolbar;
        public ImageView header;
        public Toolbar toolbar;
        public WebView wv_artigo;


        public LeitorFragmentHolder(View view){
            appbar = (AppBarLayout)view.findViewById(R.id.appbar);
            collapsing_toolbar = (CollapsingToolbarLayout)view.findViewById(R.id.collapsing_toolbar);
            header = (ImageView)view.findViewById(R.id.header);
            toolbar = (Toolbar)view.findViewById(R.id.toolbar);
            wv_artigo = (WebView)view.findViewById(R.id.wv_artigo);

            setupImageHeader();
            setupToolbar();
            setupWebView();
        }

        private void setupImageHeader(){
            Picasso.with(LeitorFragment.this.getContext()).load("http://alanlviana.com.br/blog/wp-content/uploads/2017/04/thumbnail_01-300x165.jpg").placeholder(R.drawable.backgroud_drawer).into(header);
        }
        private void setupToolbar(){
            //setSupportActionBar(toolbar);
            //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            collapsing_toolbar.setTitle("Alan dos Santos");
            //toolbar.setSubtitle(str_date);
        }

        private void setupWebView(){
            wv_artigo.setScrollContainer(false);
            wv_artigo.getSettings().setSupportZoom(false);
            wv_artigo.getSettings().setJavaScriptEnabled(true);
            wv_artigo.getSettings().setPluginState(WebSettings.PluginState.ON);
            wv_artigo.setWebChromeClient(new WebChromeClient());
            wv_artigo.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        }

    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
