package com.tercalivre.blog.model;

import android.support.annotation.NonNull;
import android.util.Log;

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

    public String getMainCategory(){
        if (categories == null || categories.size() == 0) {
            return "";
        }

        return categories.get(0).title;
    }

    @Override
    public int compareTo(@NonNull Object o) {
        if (o instanceof Post) {
            Post comparado = (Post) o;
            return id.compareTo(comparado.id);
        }

        return 0;
    }

    @Override
    public boolean equals(Object object)
    {
        boolean sameSame = false;

        if (object != null && object instanceof Post)
        {
            Post target = ((Post) object);
            sameSame = this.id.equals(target.id);
        }

        return sameSame;
    }
}


