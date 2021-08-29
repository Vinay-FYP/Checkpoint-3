package com.example.newsretro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

// d4f1eda454504339b7c74bd920de25a5
public class MainActivity extends AppCompatActivity implements CategoryRVAdapter.CategoryClickInterface{
    //create variables for recycler

    private RecyclerView newsRV, categoryRV;
    private ProgressBar loadingPB;

    //Array list and adapters
    private ArrayList<Articles> articlesArrayList;
    private ArrayList<CategoryRVModel> categoryRVModelArrayList;
    private CategoryRVAdapter categoryRVAdapter;
    private NewsRVAdapter newsRVAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        newsRV = findViewById(R.id.rvnews);
        categoryRV = findViewById(R.id.rvCategories);
        loadingPB = findViewById(R.id.pbLoading);
        articlesArrayList = new ArrayList<>();
        categoryRVModelArrayList = new ArrayList<>();
        newsRVAdapter = new NewsRVAdapter(articlesArrayList, this);
        categoryRVAdapter = new CategoryRVAdapter(categoryRVModelArrayList, this,this::onCategoryClick);
        newsRV.setLayoutManager(new LinearLayoutManager(this));
        newsRV.setAdapter(newsRVAdapter);
        categoryRV.setAdapter(categoryRVAdapter);
        getCategories();

        getnews("All");
        newsRVAdapter.notifyDataSetChanged();

    }

    //Get category Data

    private void  getCategories(){
        categoryRVModelArrayList.add(new CategoryRVModel("All", ""));
        categoryRVModelArrayList.add(new CategoryRVModel("Technology",
                "https://images.unsplash.com/photo-1488590528505-98d2b5aba04b?ixid=MnwxMjA3fDB8MHxzZWFyY2h8Mnx8dGVjaG5vbG9neXxlbnwwfHwwfHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60"));
        categoryRVModelArrayList.add(new CategoryRVModel("Science",
                "https://images.unsplash.com/photo-1507413245164-6160d8298b31?ixid=MnwxMjA3fDB8MHxzZWFyY2h8MXx8c2NpZW5jZXxlbnwwfHwwfHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60"));
        categoryRVModelArrayList.add(new CategoryRVModel("Sports",
                "https://images.unsplash.com/photo-1560089000-7433a4ebbd64?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxzZWFyY2h8Mjl8fHNwb3J0fGVufDB8fDB8fA%3D%3D&auto=format&fit=crop&w=500&q=60"));
        categoryRVModelArrayList.add(new CategoryRVModel("General",
                "https://images.unsplash.com/photo-1516321497487-e288fb19713f?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxzZWFyY2h8MjZ8fGdlbmVyYWx8ZW58MHx8MHx8&auto=format&fit=crop&w=500&q=60"));
        categoryRVModelArrayList.add(new CategoryRVModel("Business",
                "https://images.unsplash.com/photo-1579532537598-459ecdaf39cc?ixid=MnwxMjA3fDB8MHxzZWFyY2h8Mjl8fGJ1c2luZXNzfGVufDB8fDB8fA%3D%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60"));
        categoryRVModelArrayList.add(new CategoryRVModel("Entertainment",
                "https://images.unsplash.com/photo-1470229722913-7c0e2dbbafd3?ixid=MnwxMjA3fDB8MHxzZWFyY2h8NXx8ZW50ZXJ0YWlubWVudHxlbnwwfHwwfHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60"));
        categoryRVModelArrayList.add(new CategoryRVModel("Health",
                "https://images.unsplash.com/photo-1587502536263-5dda37cd33f0?ixid=MnwxMjA3fDB8MHxzZWFyY2h8MXx8aGVhbHRofGVufDB8fDB8fA%3D%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60"));

        //notify the adapter the dat has changed
        categoryRVAdapter.notifyDataSetChanged();
    }

    //get news from the API
    private void getnews(String category){
        loadingPB.setVisibility(View.VISIBLE);
        articlesArrayList.clear();
        String categoryURL = "https://newsapi.org/v2/top-headlines?country=ie&category=" + category + "&apiKey=d4f1eda454504339b7c74bd920de25a5";

        //url for all news
        String url = "https://newsapi.org/v2/top-headlines?country=ie&excludeDomains=stackoverflow.com&sortBy=publishedAt&language=en&apiKey=d4f1eda454504339b7c74bd920de25a5";

        String BASE_URL = "https://newsapi.org/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()) //convert json data
                .build();

        //call interface for retrofit
        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);
        Call<NewsModal> call;

        //chec to see what category it is

        if(category.equals("All")){
            call = retrofitAPI.getAllNews(url);
        }
        else{
            call = retrofitAPI.getNewsByCategory(categoryURL);
        }


        call.enqueue(new Callback<NewsModal>() {
            @Override
            public void onResponse(Call<NewsModal> call, Response<NewsModal> response) {
                NewsModal newsModal = response.body();
                loadingPB.setVisibility(View.GONE);
                ArrayList<Articles> articles = newsModal.getArticles();
                for(int i = 0; i<articles.size(); i++){
                    articlesArrayList.add(new Articles(articles.get(i).getTitle(), articles.get(i).getDescription(), articles.get(i).getUrlToImage(),
                            articles.get(i).getUrl(), articles.get(i).getContent()));
                }
                newsRVAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<NewsModal> call, Throwable t) {

                Toast.makeText(MainActivity.this, "Failed to get news", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onCategoryClick(int position) {
        //get psotion when category is clicked
        String category =categoryRVModelArrayList.get(position).getCategory();
        getnews(category);


    }
}