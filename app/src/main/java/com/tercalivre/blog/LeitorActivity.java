package com.tercalivre.blog;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
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
    private String str_url;
    private String str_nickname;

    private ImageView header;
    private WebView wv_content;
    private ShareActionProvider mShareActionProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leitor);

        str_title = this.getIntent().getStringExtra("title");
        str_date = this.getIntent().getStringExtra("date");
        str_content = this.getIntent().getStringExtra("content");
        str_thumbnail = this.getIntent().getStringExtra("thumbnail");
        str_url = this.getIntent().getStringExtra("url");
        str_nickname = this.getIntent().getStringExtra("nickname");


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);



        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        wv_content = (WebView) findViewById(R.id.wv_artigo);
        header = (ImageView) findViewById(R.id.header);




        Picasso.with(this).load(str_thumbnail).placeholder(R.drawable.backgroud_drawer).into(header);


        wv_content.loadData(getHTML(), "text/html; charset=utf-8", "utf-8");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate menu resource file.
        getMenuInflater().inflate(R.menu.leitor_menu, menu);

        // Locate MenuItem with ShareActionProvider
        MenuItem item = menu.findItem(R.id.leitor_menu_item_share);

        // Fetch and store ShareActionProvider
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, str_url);
        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, str_title);

        setShareIntent(intent);


        // Return true to display menu
        return true;
    }

    // Call to update the share intent
    private void setShareIntent(Intent shareIntent) {
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(shareIntent);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) // Press Back Icon
        {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }


    private String getHTML(){
        String html = getHtmlTemplate();

        html = html.replaceAll("@@TITLE@@",str_title);
        html = html.replaceAll("@@CONTENT@@",str_content);
        html = html.replaceAll("@@AUTHOR@@",str_nickname);
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
