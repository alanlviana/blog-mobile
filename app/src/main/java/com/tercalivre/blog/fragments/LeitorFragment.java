package com.tercalivre.blog.fragments;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.tercalivre.blog.LeitorActivity;
import com.tercalivre.blog.R;
import com.tercalivre.blog.model.Post;

import java.io.InputStream;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LeitorFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LeitorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LeitorFragment extends Fragment {
    private static final String ARG_POST = "com.tercalivre.blog.fragments.LeitorFragment.post";

    private Post mPost;
    private LeitorFragmentHolder mHolder;

    private OnFragmentInteractionListener mListener;

    public LeitorFragment() {
        // Required empty public constructor
    }

    public static LeitorFragment newInstance(Post post) {
        LeitorFragment fragment = new LeitorFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_POST, post);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPost = (Post)getArguments().getSerializable(ARG_POST);
            Log.d(LeitorFragment.class.getSimpleName(),mPost.title);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_leitor, container, false);
        mHolder = new LeitorFragmentHolder(view);
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
        public ImageView img_header;
        public Toolbar toolbar;
        public WebView wv_artigo;

        public LeitorFragmentHolder(View view){

            appbar = (AppBarLayout)view.findViewById(R.id.appbar);
            collapsing_toolbar = (CollapsingToolbarLayout)view.findViewById(R.id.collapsing_toolbar);
            img_header = (ImageView)view.findViewById(R.id.header);
            toolbar = (Toolbar)view.findViewById(R.id.toolbar);
            wv_artigo = (WebView)view.findViewById(R.id.wv_artigo);

            setupToolbar();
            setupWebView();
            setPostUI(mPost);
        }
        private void setupToolbar(){
            ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
            ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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

        public void setPostUI(Post post){
            collapsing_toolbar.setTitle(post.title);
            Picasso picasso = Picasso.with(getContext());
            picasso.setIndicatorsEnabled(true);
            picasso.setLoggingEnabled(true);
            picasso.load(post.thumbnail).placeholder(R.drawable.backgroud_drawer).into(img_header);
            wv_artigo.loadData(getHTML(post), "text/html; charset=utf-8", "utf-8");
        }

    }

    private String getHTML(Post post){
        String html = getHtmlTemplate();

        html = html.replaceAll("@@TITLE@@",post.title);
        html = html.replaceAll("@@CONTENT@@",post.content);
        html = html.replaceAll("@@AUTHOR@@",post.excerpt);
        html = html.replaceAll("@@DATE@@",post.date);

        html = html.replaceAll("<iframe","<div class=\"videoWrapper\">\n" +
                "  <iframe");

        html = html.replaceAll("</iframe>","</iframe></div>");

        return html;
    }

    private String getHtmlTemplate(){
        String html = "";

        try {
            Resources res = getResources();
            InputStream in_s = res.openRawResource(R.raw.html_template);

            byte[] b = new byte[in_s.available()];
            in_s.read(b);
            html = new String(b);
        } catch (Exception e) {
            html = null;
            e.fillInStackTrace();
        }

        return html;
    }



    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
