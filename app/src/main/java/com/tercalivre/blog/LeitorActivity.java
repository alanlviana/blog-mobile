package com.tercalivre.blog;

import android.content.res.Resources;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.InputStream;

public class LeitorActivity extends AppCompatActivity {

    private String str_content;
    private String str_title;
    private String str_date;
    private String str_thumbnail;

    private ImageView header;
    private WebView wv_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leitor);

        str_title = this.getIntent().getStringExtra("title");
        str_date = this.getIntent().getStringExtra("date");
        str_content = this.getIntent().getStringExtra("content");
        str_thumbnail = this.getIntent().getStringExtra("thumbnail");


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(getString(R.string.toolbar_title));
        wv_content = (WebView) findViewById(R.id.wv_artigo);
        header = (ImageView) findViewById(R.id.header);






        Picasso.with(this).load(str_thumbnail).placeholder(R.drawable.backgroud_drawer).into(header);






        wv_content.setScrollContainer(false);
        wv_content.getSettings().setSupportZoom(false);
        wv_content.getSettings().setJavaScriptEnabled(true);
        wv_content.getSettings().setPluginState(WebSettings.PluginState.ON);
        wv_content.setWebChromeClient(new WebChromeClient());
        wv_content.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        wv_content.loadData(getHTML(), "text/html; charset=utf-8", "utf-8");




    }


    private String getHTML(){
        String html = getHtmlTemplate();

        html = html.replaceAll("@@TITLE@@",str_title);
        html = html.replaceAll("@@CONTENT@@",str_content);
        html = html.replaceAll("@@DATE@@",str_date);

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
        }

        return html;
    }

}
