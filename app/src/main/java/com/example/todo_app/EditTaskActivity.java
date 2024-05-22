package com.example.todo_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.todo_app.database.QueryHelper;
import com.example.todo_app.todo.AppManager;
import com.example.todo_app.todo.Category;
import com.example.todo_app.todo.Dialogs.AddReminderDialog;
import com.example.todo_app.todo.Remainder;
import com.example.todo_app.todo.Todo;
import com.example.todo_app.todo.adapter.CategoriesSpinnerAdapter;
import com.example.todo_app.todo.adapter.RecyclerViewReminderAdapter;

import java.util.ArrayList;

public class EditTaskActivity extends AppCompatActivity {


    // current todo_item
    public Todo todo ;
    public EditText et_title ;
    public EditText et_date ;
    public EditText et_time ;
    public Spinner sp_category ;
    public Button btn_save ;
    public Button btn_addReminder ;
    public RecyclerView review_reminders ;
    public RecyclerViewReminderAdapter recyclerViewReminderAdapter ;
    public long selected_category ;

    public QueryHelper queryHelper;
    ArrayList<Category> categories ;

    // remainders list for current todo_item
    public ArrayList<Remainder> remainders ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);


        //get elements
        et_title = findViewById(R.id.editTask_et_title );
        et_date = findViewById(R.id.editTask_et_date);
        et_time = findViewById(R.id.editTask_et_time);
        sp_category = findViewById(R.id.editTask_sp_category);
        btn_save = findViewById(R.id.editTask_btn_save);
        btn_addReminder = findViewById(R.id.editTask_btn_addReminder);
        review_reminders = findViewById(R.id.editTask_review_reminders);
        queryHelper = new QueryHelper(this);
        // define intent to can read current todo_item

      try {
          long todo_id = getIntent().getLongExtra("Todo" , 0);

          todo =  queryHelper.getTodoById(todo_id);



      }catch (Exception e){
          Log.e("MYERROR" , e.getMessage());

      }



        // init spinner category  data
        // get all categories from database
        categories = new ArrayList<>();
        categories.add(new Category( "None"  , 0 ));
        categories.addAll(queryHelper.getAllCategories());
        // define categories spinner adapter
        CategoriesSpinnerAdapter categoriesSpinnerAdapter = new CategoriesSpinnerAdapter(this, R.layout.spinner_category_item, categories);
        // set adapter to spinner
        sp_category.setAdapter(categoriesSpinnerAdapter);

        init_todo_values_in_view();


        remainders = new ArrayList<>();
        try {
            remainders.addAll(queryHelper.getRemaindersByTodoId(todo.getId()));
            // init reminders recycler view
            review_reminders.setLayoutManager(new LinearLayoutManager(this));
            recyclerViewReminderAdapter = new RecyclerViewReminderAdapter(this ,remainders);
            review_reminders.setAdapter(recyclerViewReminderAdapter);
        }catch (Exception e){
            Log.e("MYERROR" , e.getMessage());
        }

        btn_addReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddReminderDialog add_reminder_dialog = new AddReminderDialog(EditTaskActivity.this , todo.getId());
                add_reminder_dialog.show();
//
                add_reminder_dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        // update reminders list


                ArrayList<Remainder> newRe = new ArrayList<>();
                newRe.addAll(queryHelper.getRemaindersByTodoId(todo.getId()));
                try {
                    Remainder new_reminder_added = newRe.get(newRe.size() -1 );


                    remainders.add(new_reminder_added);
                    //  reset remainder list to adapter
                    recyclerViewReminderAdapter.remainders = remainders ;
                    recyclerViewReminderAdapter.notifyItemInserted(remainders.size() - 1 );
                }catch (Exception e){
                    Log.e("MYERROR" , e.getMessage());
                }
                    }
                });
//

            }
        });
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get data from view
                String title = et_title.getText().toString();
                String date = et_date.getText().toString();
                String time = et_time.getText().toString();
                long category_id = selected_category ;

                // check if value empty
                if (AppManager.validateEmptyEditText( EditTaskActivity.this , et_time ,null)  == null
                        || AppManager.validateEmptyEditText(EditTaskActivity.this ,et_date ,null)   == null
                        || AppManager.validateEmptyEditText(EditTaskActivity.this ,et_title ,null ) == null
                ){
                    Toast.makeText( EditTaskActivity.this ,
                            getResources().getString(R.string.msg_empty_data) ,
                            Toast.LENGTH_LONG).show();

                }else{

                    Todo newTodo = new Todo(title ,date,time,category_id,todo.isDone());
                    if(queryHelper.update(todo , newTodo)) {



                        Toast.makeText(EditTaskActivity.this,
                                getResources().getString(R.string.msg_task_insert_done) ,
                                Toast.LENGTH_LONG
                        ).show();
                    }else{
                        Toast.makeText(EditTaskActivity.this ,
                                getResources().getString(R.string.error_insert),
                                Toast.LENGTH_LONG
                        ).show();
                    }
                }
            }
        });


        sp_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Category category_selected = (Category) sp_category.getSelectedItem();
                selected_category  =category_selected.getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    public  void init_todo_values_in_view(){
        et_title.setText(todo.getTitle());
        et_date.setText(todo.getDate());
        et_time.setText(todo.getTime());
        for (int i = 0; i < categories.size(); i++) {
            if (categories.get(i).getId() == todo.getCategory_id()) {
                // Set the selection if a matching ID is found
                selected_category = i;
                sp_category.setSelection(i);
                break;
            }
        }


    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
        // return MainActivity
        startActivity( new Intent(getApplicationContext() , MainActivity.class));
        finish();
    }
}