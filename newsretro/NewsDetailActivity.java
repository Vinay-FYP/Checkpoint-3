package com.example.newsretro;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class NewsDetailActivity extends AppCompatActivity {

    String title, desc, content, imageURl,url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        title = getIntent().getStringExtra("title");
        desc = getIntent().getStringExtra("desc");
        content = getIntent().getStringExtra("content");
        imageURl = getIntent().getStringExtra("image");
        url = getIntent().getStringExtra("url");
    }
}