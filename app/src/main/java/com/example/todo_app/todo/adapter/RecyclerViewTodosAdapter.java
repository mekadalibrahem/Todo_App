package com.example.todo_app.todo.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todo_app.EditTaskActivity;
import com.example.todo_app.R;
import com.example.todo_app.database.QueryHelper;
import com.example.todo_app.todo.Category;
import com.example.todo_app.todo.Todo;

import java.util.ArrayList;


public class RecyclerViewTodosAdapter extends RecyclerView.Adapter<RecyclerViewTodosAdapter.TodoViewHolder> {

    public ArrayList<Todo> todos ;


    public RecyclerViewTodosAdapter(Context  context, ArrayList<Todo> todos){

        this.todos = todos ;
    }
    @NonNull
    @Override
    public TodoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_todo_list_item ,parent , false);
        return new TodoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TodoViewHolder holder, int position) {
        holder.todo = todos.get(position);
         Category category = holder.queryHelper.getCategoryById(holder.todo.getCategory_id());
//
         holder.tv_name.setText(holder.todo.getTitle());

        holder.tv_category.setText(category.getName());
        holder.tv_date.setText(holder.todo.getDate());
        holder.tv_time.setText(holder.todo.getTime());
        holder.switch_id_done.setChecked(holder.todo.isDone());
    }

    @Override
    public int getItemCount() {
        return  todos.size();
    }

    public  class  TodoViewHolder extends RecyclerView.ViewHolder {
        private QueryHelper queryHelper ;
        public TextView tv_name;
        public TextView tv_date ;
        public  TextView tv_time ;
        public TextView tv_category ;
        public AppCompatImageButton btn_delete ;
        public AppCompatImageButton btn_edit ;
        public Switch switch_id_done;
        public Todo todo ;


        public TodoViewHolder(@NonNull View v) {
            super(v);
            // get elements view
            tv_name = v.findViewById(R.id.review_todo_item_tv_name);
            tv_date = v.findViewById(R.id.review_todo_item_tv_date);
            tv_category = v.findViewById(R.id.review_todo_item_tv_category);
            tv_time = v.findViewById(R.id.review_todo_item_tv_time);
            btn_delete = v.findViewById(R.id.review_todo_item_btn_delete);
            btn_edit = v.findViewById(R.id.review_todo_item_btn_edit);
            switch_id_done = v.findViewById(R.id.review_todo_item_switch_isDone);

            queryHelper = new QueryHelper(v.getContext());

            // delete item
            btn_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder confirmDialog = new AlertDialog.Builder(v.getContext());
                    confirmDialog.setTitle(v.getResources().getString(R.string.confirm_title));
                    confirmDialog.setMessage(v.getResources().getString(R.string.msg_confirm_delete_item));
                    confirmDialog.setPositiveButton(v.getResources().getText(R.string.delete), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(queryHelper.delete(todo)){
                                int deletedPosition = todos.indexOf(todo);
                                todos.remove(todo); // Remove from the data source

                                // Notify the adapter about the item removal
                                notifyItemRemoved(deletedPosition);
                                Toast.makeText(v.getContext(), v.getResources().getString(R.string.msg_delete_item), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    confirmDialog.show();
                }
            });

            // update and set todo_item  is done

            switch_id_done.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int item_index = todos.indexOf(todo);
                    boolean status  = switch_id_done.isChecked();
                    if (todo.isDone() != status){
                        // change is done
                        if(queryHelper.setDone(todo , status)){
                            todo.setDone(status);
                            notifyItemChanged(item_index);
                        }
                    }
                }
            });
            btn_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        // goto EditTaskActivity
                        Intent editTaskIntent = new Intent(v.getContext() , EditTaskActivity.class);

                        editTaskIntent.putExtra("Todo" , todo.getId());
                        v.getContext().startActivity(editTaskIntent);
                    }catch (Exception e){
                        Log.e("MYERROR" , e.getMessage());
                    }

                }
            });
        }
    }

}
