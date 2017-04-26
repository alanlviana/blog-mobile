package com.tercalivre.blog.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by alanl on 25/04/2017.
 */

public class Category {
    @SerializedName("slug")
    public String slug;

    @SerializedName("title")
    public String title;

    @SerializedName("post_count")
    public String post_count;
}
