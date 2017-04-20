package com.tercalivre.blog.model;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ObjetoAPI {

    public class WordpressJson {

        @SerializedName("posts")
        public List<Post> posts;

        public WordpressJson(){
            posts = new ArrayList<>();
        }

        public class Post  implements Comparable{
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
                if (o instanceof Post){
                    Post comparado = (Post)o;
                    return id.compareTo(comparado.id);
                }

                return 0;
            }
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
