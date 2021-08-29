package com.example.newsretro;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CategoryRVAdapter extends RecyclerView.Adapter<CategoryRVAdapter.ViewHolder> {

    //variables for arraylist
    private ArrayList<CategoryRVModel> categoryRVModels;
    private Context context;
    private CategoryClickInterface categoryClickInterface;

    public CategoryRVAdapter(ArrayList<CategoryRVModel> categoryRVModels, Context context, CategoryClickInterface categoryClickInterface) {
        this.categoryRVModels = categoryRVModels;
        this.context = context;
        this.categoryClickInterface = categoryClickInterface;
    }

    @NonNull
    @Override
    public CategoryRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.categories_rv_item,parent,false);
        return  new CategoryRVAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryRVAdapter.ViewHolder holder, int position) {

        //set date t o on bind view for animate

        CategoryRVModel categoryRVModel = categoryRVModels.get(position);
        holder.categoryTV.setText(categoryRVModel.getCategory());
        Picasso.get().load(categoryRVModel.getCategoryImageUrl()).into(holder.categoryIV);

        //add on click listener for category

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //call interface
                categoryClickInterface.onCategoryClick(position);//passing positon
            }
        });

    }

    @Override
    public int getItemCount() {
        return categoryRVModels.size();
    }

    public interface CategoryClickInterface{
        void onCategoryClick(int position); //get data from array list
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        //inflate layout and initialise views
        private TextView categoryTV;
        private ImageView categoryIV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryTV = itemView.findViewById(R.id.tvCategory);
        }
    }
}
