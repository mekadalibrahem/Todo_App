package com.example.todo_app.todo.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todo_app.R;
import com.example.todo_app.database.QueryHelper;
import com.example.todo_app.todo.AppManager;
import com.example.todo_app.todo.Category;

import java.util.ArrayList;

/**
 *
 * يعمل recyclerView مع  view holder   طريقة لربط العناصر من مجموعة من البيانات و ظريقة عرضها
 */
public class RecyclerViewCategoryAdapter  extends RecyclerView.Adapter<RecyclerViewCategoryAdapter.CategoryViewHolder> {
    private ArrayList<Category> categories ;

    public RecyclerViewCategoryAdapter(Context context, ArrayList<Category> allCategories) {
        categories = allCategories ;
    }

    /**
     *
     * يصف هذا الصف طريقة عرض العنصر و البيانات الخاصة به و اي عمليات للازرار  لعتصر واحد ضمن  recycler view
     */



    @NonNull
    @Override
    public RecyclerViewCategoryAdapter.CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_category_item, parent, false);
        CategoryViewHolder vh = new CategoryViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewCategoryAdapter.CategoryViewHolder holder, int position) {
            holder.category = categories.get(position);

        holder.et_name.setText( holder.category.getName());



    }

    @Override
    public int getItemCount() {
        return  categories.size();
    }
    public class CategoryViewHolder extends RecyclerView.ViewHolder {

        private QueryHelper queryHelper ;
        public TextView et_name ;
        public AppCompatImageButton btn_delete ;
        public AppCompatImageButton btn_update ;
        public  Category category ;

        public CategoryViewHolder(View v) {
            super(v);

            queryHelper = new QueryHelper(v.getContext());
            et_name = v.findViewById(R.id.review_item_tv_name);
            btn_delete = v.findViewById(R.id.review_item_btn_delete);
            btn_update = v.findViewById(R.id.review_item_btn_update);
            btn_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AlertDialog.Builder confirmDialog = new AlertDialog.Builder(v.getContext());
                    confirmDialog.setTitle(v.getResources().getString(R.string.confirm_title));
                    confirmDialog.setMessage(v.getResources().getString(R.string.msg_confirm_delete_item));
                    confirmDialog.setPositiveButton(v.getResources().getText(R.string.delete), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(queryHelper.delete(category)){
                                int deletedPosition = categories.indexOf(category);
                                categories.remove(category); // Remove from the data source

                                // Notify the adapter about the item removal
                                notifyItemRemoved(deletedPosition);
                                Toast.makeText(v.getContext(), v.getResources().getString(R.string.msg_delete_item), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    confirmDialog.show();
                }
            });
            btn_update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //get index for category will updated
                    int position = categories.indexOf(category);
                    // show dialog for write new name and then update name
                    AlertDialog.Builder AlertDialogUpdateName = new AlertDialog.Builder(v.getContext());
                    AlertDialogUpdateName.setTitle(v.getContext().getString(R.string.update_category_title));
                    // define edit text and set it to alert dialog ( in this edit item user will write new name )
                    EditText et_new_name = new EditText(v.getContext());
                    // set default value ( category old name ) for edit item
                    et_new_name.setText(category.getName());
                    // add edit item for dialog
                    AlertDialogUpdateName.setView(et_new_name);
                    // button when user click yes
                    AlertDialogUpdateName.setPositiveButton(v.getContext().getString(R.string.edit), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String newName = et_new_name.getText().toString().trim();
                            if(AppManager.validateEmptyEditText(v.getContext() , et_new_name , null) != null){
                                if ( !newName.equals(category.getName())) {



                                    if (queryHelper.updateCategoryName(category ,  newName)) {
                                        category.setName(newName);
                                        categories.set(position, category); // Update data source
                                        notifyItemChanged(position); // Notify adapter about change
                                        Toast.makeText(v.getContext(), v.getContext().getString(R.string.msg_update_item), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }
                    });
                    // button when user click no
                    AlertDialogUpdateName.setNegativeButton(v.getContext().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    // show dialog
                    AlertDialogUpdateName.show();
                }



            });

        }



    }
}


