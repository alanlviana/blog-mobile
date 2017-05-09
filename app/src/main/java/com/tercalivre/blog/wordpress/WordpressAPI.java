package com.tercalivre.blog.wordpress;

import com.google.gson.annotations.SerializedName;
import com.tercalivre.blog.model.Category;
import com.tercalivre.blog.model.Post;

import java.util.List;

public class WordpressAPI {

    public class PostResponse{
        @SerializedName("count")
        public int count;

        @SerializedName("count_total")
        public int count_total;

        @SerializedName("pages")
        public int pages;

        @SerializedName("posts")
        public List<Post> posts;
    }

    public class CategoryResponse{
        @SerializedName("categories")
        public List<Category> categories;
    }

}
