package com.tercalivre.blog.wordpress;

import com.google.gson.annotations.SerializedName;
import com.tercalivre.blog.model.Category;
import com.tercalivre.blog.model.Post;

import java.util.List;

/**
 * Created by alanl on 25/04/2017.
 */

public class WordpressAPI {

    public class PostResponse{
        @SerializedName("posts")
        public List<Post> posts;
    }

    public class CategoryResponse{
        @SerializedName("categories")
        public List<Category> categories;
    }

}
