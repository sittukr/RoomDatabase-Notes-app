package com.edufun.roomdatabase;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    EditText etTitle,etAmount;
    TextView tvCreate;

    ImageView imgAddNote;
    RecyclerView recyclerView;
    ArrayList<Expense> arrayList;
    DatabaseHelper databaseHelper;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        etTitle=findViewById(R.id.etTitle);
        imgAddNote=findViewById(R.id.imgAddNote);
        recyclerView=findViewById(R.id.recyclerview);
        tvCreate=findViewById(R.id.tvCreate);

        databaseHelper = DatabaseHelper.getDB(this);

        arrayList = (ArrayList<Expense>) databaseHelper.expenseDao().getAllExpense();
        for (int i=0; i<arrayList.size(); i++){
            Log.d("Data","Title"+arrayList.get(i).getTitle() + "Amount" +arrayList.get(i).getAmount());
            //Toast.makeText(this,"Title"+arrayList.get(i).getTitle() + "Amount" +arrayList.get(i).getAmount() , Toast.LENGTH_SHORT).show();
        }

        showAdapter();

        imgAddNote.setOnClickListener(v -> {
            Intent in = new Intent(this,NotesActivity.class);
            startActivity(in);
        });
        tvCreate.setOnClickListener(v -> {
            imgAddNote.performClick();
        });

    }
    public void showAdapter(){
        arrayList = (ArrayList<Expense>) databaseHelper.expenseDao().getAllExpense();
        if (!(arrayList.isEmpty())) {
            tvCreate.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            DataAdapter adapter = new DataAdapter(this, arrayList, databaseHelper);
            recyclerView.setAdapter(adapter);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(linearLayoutManager);
        }else {
            tvCreate.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
    }
}