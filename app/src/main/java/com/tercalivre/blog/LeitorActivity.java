package com.tercalivre.blog;

import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import java.io.InputStream;

public class LeitorActivity extends AppCompatActivity {

    private String str_content;
    private String str_title;
    private String str_date;

    private WebView wv_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leitor);

        wv_content = (WebView) findViewById(R.id.wv_artigo);

        str_title = this.getIntent().getStringExtra("title");
        str_date = this.getIntent().getStringExtra("date");
        str_content = this.getIntent().getStringExtra("content");



        wv_content.setScrollContainer(false);
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
