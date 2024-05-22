package com.example.todo_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.todo_app.database.QueryHelper;
import com.example.todo_app.todo.AppManager;
import com.example.todo_app.todo.Category;
import com.example.todo_app.todo.adapter.RecyclerViewCategoryAdapter;

import java.util.ArrayList;

public class CategoryActivity extends AppCompatActivity {

    public Button btn_save ;
    public EditText et_name ;
    public RecyclerView categories_recycler_view ;
    public  long last_category_saved ;
    public QueryHelper queryHelper ;
    public ArrayList<Category> all_categories ;
    public RecyclerViewCategoryAdapter adapter ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        //get elements ;
        btn_save = findViewById(R.id.category_btn_save);
        et_name  = findViewById(R.id.category_et_name) ;
        categories_recycler_view  = findViewById(R.id.category_rev_categories);


        // define query helper for database
        queryHelper = new QueryHelper(this);

        // get all categories form database
        all_categories = new ArrayList<>();
        try {
            all_categories.addAll(queryHelper.getAllCategories());
//
            categories_recycler_view.setLayoutManager(new LinearLayoutManager(this));
             adapter = new RecyclerViewCategoryAdapter(this, all_categories);
            categories_recycler_view.setAdapter(adapter);

        }catch (Exception e){
            Log.e("MYERROR" , e.getMessage());
        }

        // save new category section

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // check is empty or not
                if(AppManager.validateEmptyEditText(CategoryActivity.this , et_name ,null) != null){
                    String name = et_name.getText().toString();
                    Category category = new Category() ;
                    category.setName(name);
                    last_category_saved = queryHelper.insert(category);
                    if(last_category_saved > 0){
                        // category saved
                        Toast.makeText(CategoryActivity.this,
                                getResources().getString(R.string.msg_newCategory_saved)
                                , Toast.LENGTH_SHORT).show();
                        // Add category to data source and update adapter
                        all_categories.add(category); // Add to the ArrayList
                        adapter.notifyItemInserted(all_categories.size() - 1); // Notify adapter about new item

                        // Clear the input field
                        et_name.setText("");
                    }

                }
            }
        });


        //end save new category section







    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
        // return MainActivity
        startActivity( new Intent(getApplicationContext() , MainActivity.class));
        finish();
    }


}