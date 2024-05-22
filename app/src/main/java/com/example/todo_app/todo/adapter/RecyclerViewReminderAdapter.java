package com.example.todo_app.todo.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todo_app.R;
import com.example.todo_app.database.QueryHelper;
import com.example.todo_app.todo.Remainder ;

import java.util.ArrayList;

public class RecyclerViewReminderAdapter  extends RecyclerView.Adapter<RecyclerViewReminderAdapter.ReminderViewHolder>{

    public ArrayList<Remainder> remainders ;


    public  RecyclerViewReminderAdapter(Context context , ArrayList<Remainder> remainders){
        this.remainders = remainders ;
    }



    @NonNull
    @Override
    public RecyclerViewReminderAdapter.ReminderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_reminder_item ,parent , false);
        return new ReminderViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewReminderAdapter.ReminderViewHolder holder, int position) {
        holder.remainder = remainders.get(position);
        holder.tv_dateTime.setText(holder.remainder.getDateTime());
    }

    @Override
    public int getItemCount() {
         return  remainders.size();
    }

    public class ReminderViewHolder extends RecyclerView.ViewHolder {


        public QueryHelper queryHelper ;
        public  Remainder remainder ;

        public TextView tv_dateTime ;
        public AppCompatImageButton btn_delete ;


        public ReminderViewHolder(@NonNull View v) {

            super(v);
            queryHelper = new QueryHelper(v.getContext());
            tv_dateTime =  v.findViewById(R.id.review_reminder_tv_datetime);
            btn_delete =  v.findViewById(R.id.review_reminder_btn_delete);

            btn_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder confirmDialog = new AlertDialog.Builder(v.getContext());
                    confirmDialog.setTitle(v.getResources().getString(R.string.confirm_title));
                    confirmDialog.setMessage(v.getResources().getString(R.string.msg_confirm_delete_item));
                    btn_delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AlertDialog.Builder confirmDialog = new AlertDialog.Builder(v.getContext());
                            confirmDialog.setTitle(v.getResources().getString(R.string.confirm_title));
                            confirmDialog.setMessage(v.getResources().getString(R.string.msg_confirm_delete_item));
                            confirmDialog.setPositiveButton(v.getResources().getText(R.string.delete), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {


                                    if (queryHelper.delete(remainder)) {
                                        int deletedPosition = remainders.indexOf(remainder);
                                        remainders.remove(remainder); // Remove from the data source

                                        // Notify the adapter about the item removal
                                        notifyItemRemoved(deletedPosition);
                                        Toast.makeText(v.getContext(), v.getResources().getString(R.string.msg_delete_item), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                            confirmDialog.show();
                        }
                    });
                }
            });
        }
    }
}
