package com.edufun.roomdatabase;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.Transliterator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder>{

    Context context;
    ArrayList<Expense> arrayList;
    DatabaseHelper databaseHelper;
    Activity activity;

    public DataAdapter(Context context, ArrayList<Expense> arrayList , DatabaseHelper databaseHelper, Activity activity) {
        this.context = context;
        this.arrayList = arrayList;
        this.databaseHelper = databaseHelper;
        this.activity = activity;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.show_data,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Expense list =arrayList.get(position);
        holder.tvAmount.setEnabled(false);
        holder.tvTitle.setEnabled(false);
        holder.tvTitle.setText(list.getTitle());
        holder.tvAmount.setText(list.getAmount());

        holder.itemView.setOnLongClickListener(v -> {
            deleteItem(position);
            return true;
        });

        holder.itemView.setOnClickListener(v -> {
            Intent in = new Intent(context,NotesActivity.class);
            in.putExtra("title",list.getTitle());
            in.putExtra("Note",list.getAmount());
            in.putExtra("position",arrayList.get(position).getId());
            context.startActivity(in);
            activity.finish();
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
    private void saveItem(int position,String title,String amount) {
        databaseHelper.expenseDao().updateTx(new Expense(arrayList.get(position).getId(),title,amount));
        ((MainActivity)context).showAdapter();

    }
    private void deleteItem(int position){
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle("Delete")
                .setMessage("Are you sure want to delete")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        databaseHelper.expenseDao().deleteTx(new Expense(arrayList.get(position).getId(),arrayList.get(position).getTitle(),arrayList.get(position).getAmount()));
                        ((MainActivity)context).showAdapter();
                    }
                }).setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle,tvAmount;
        ImageView imgSave;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle=itemView.findViewById(R.id.tvTitle);
            tvAmount=itemView.findViewById(R.id.tvAmount);
            imgSave=itemView.findViewById(R.id.imgSave);
        }
    }
}
