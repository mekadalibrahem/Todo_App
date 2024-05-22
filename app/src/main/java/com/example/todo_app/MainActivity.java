package com.example.todo_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.todo_app.database.QueryHelper;
import com.example.todo_app.todo.AppManager;
import com.example.todo_app.todo.Category;
import com.example.todo_app.todo.Todo;
import com.example.todo_app.todo.adapter.RecyclerViewTodosAdapter;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    public  QueryHelper queryHelper;
    public ArrayList<Todo> todos ;
    public RecyclerView todos_recycler_view ;
    public RecyclerViewTodosAdapter recyclerViewTodosAdapter ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // init application configuration
        AppManager.init(MainActivity.this);


        // init elements
        todos_recycler_view = findViewById(R.id.main_review_todo_list);
        queryHelper = new QueryHelper(this);




        // add toolbar menu
        Toolbar main_toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(main_toolbar);

        // init recycler view for todos items
        todos = new ArrayList<>();
        try {
            todos.addAll(queryHelper.getAllTodos());
            if(todos.isEmpty()){
                Log.e("MYERROR" , "NULL TODOS");
            }
            todos_recycler_view.setLayoutManager(new LinearLayoutManager(this));
            recyclerViewTodosAdapter = new RecyclerViewTodosAdapter(this , todos);
            todos_recycler_view.setAdapter(recyclerViewTodosAdapter);
        }catch (Exception e){
            Log.e("MYERROR" , e.getMessage());
        }


    }


    /**
     * get instantiate menu XML files into Menu objects.
     * @param menu The options menu in which you place your items.
     *
     * @return boolean
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu,menu);
        return true;
    }

    /**
     *
     * set actions when user select item menu
     * @param item The options menu in which you place your items.
     *
     * @return boolean
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.menu_item_new_task) {
           startActivity(new Intent(getApplicationContext() , NewTaskActivity.class) );
            finish();  //  closes the current activity
       } else if (item.getItemId() == R.id.menu_item_categories) {
           startActivity(new Intent(getApplicationContext() , CategoryActivity.class));
            finish();  //  closes the current activity

       }
        return  true ;
    }
}