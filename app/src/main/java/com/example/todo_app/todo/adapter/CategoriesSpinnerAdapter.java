package com.example.todo_app.todo.adapter;

import com.example.todo_app.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.todo_app.todo.Category;

import java.util.ArrayList;

/**
 * adapter for categories spinner
 */
public class CategoriesSpinnerAdapter extends ArrayAdapter<Category> {

    private Context context ;
    private ArrayList<Category> categories ;

    /**
     *
     * @param context
     * @param textViewResourceId
     * @param categories
     */
    public CategoriesSpinnerAdapter(@NonNull Context context ,int textViewResourceId, @NonNull ArrayList<Category> categories) {
        super(context, textViewResourceId, categories);
        this.categories = categories ;
        this.context = context ;
    }
    @Override
    public int getCount(){
        return categories.size();
    }

    @Override
    public Category getItem(int position){
        return categories.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.spinner_category_item, parent, false);
        }

        Category category = getItem(position);

        TextView textView = (TextView) convertView.findViewById(R.id.spinner_item_category_name);
        textView.setText(category.getName());

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.spinner_category_item, parent, false);
        }

        Category category = getItem(position);

        TextView textView = (TextView) convertView.findViewById(R.id.spinner_item_category_name);
        textView.setText(category.getName());

        return convertView;
    }



}
