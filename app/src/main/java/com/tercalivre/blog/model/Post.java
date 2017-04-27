package com.tercalivre.blog.model;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by alanl on 25/04/2017.
 */

public class Post implements Comparable, Serializable {
    @SerializedName("categories")
    public List<Category> categories;

    @SerializedName("url")
    public String url;

    @SerializedName("id")
    public Integer id;

    @SerializedName("title")
    public String title;

    @SerializedName("excerpt")
    public String excerpt;

    @SerializedName("thumbnail")
    public String thumbnail;

    @SerializedName("content")
    public String content;

    @SerializedName("date")
    public String date;

    @Override
    public int compareTo(@NonNull Object o) {
        if (o instanceof Post) {
            Post comparado = (Post) o;
            return id.compareTo(comparado.id);
        }

        return 0;
    }
}


