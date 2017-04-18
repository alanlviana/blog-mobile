package com.tercalivre.blog.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ObjetoAPI {

    public class WordpressJson {

        @SerializedName("posts")
        public List<Post> posts;

        public class Post {
            @SerializedName("categories")
            public List<Category> categories;

            @SerializedName("url")
            public String url;

            @SerializedName("id")
            public String id;

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
        }

        public class Category {
            @SerializedName("title")
            public String title;

            @SerializedName("post_count")
            public String post_count;
        }
    }

    public class CategoryPost {
        @SerializedName("categories")
        public List<ResultCategory> categories;

        public class ResultCategory {
            @SerializedName("slug")
            public String slug;

            @SerializedName("title")
            public String title;

            @SerializedName("post_count")
            public String post_count;
        }
    }

    public class CommentPost {

        @SerializedName("comments")
        public List<Comment> comments;

        public class Comment {
            @SerializedName("name")
            public String name;

            @SerializedName("content")
            public String content;
        }
    }



}
