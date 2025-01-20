package com.edufun.roomdatabase;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class NotesActivity extends AppCompatActivity {

    ConstraintLayout mainLy;
    EditText etTitle,etNote;
    ImageView imgBack,imgSave;
    TextView tvChar;

    DatabaseHelper databaseHelper;
    @SuppressLint({"MissingInflatedId", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_notes);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        mainLy = findViewById(R.id.ly);
        etNote=findViewById(R.id.etNote);
        etTitle=findViewById(R.id.etTitle);
        imgBack=findViewById(R.id.imgBack);
        imgSave=findViewById(R.id.imgSave);
        tvChar=findViewById(R.id.tvChar);

        databaseHelper = DatabaseHelper.getDB(this);

        Intent in = getIntent();
        String title = in.getStringExtra("title");
        String note = in.getStringExtra("Note");
        int position = in.getIntExtra("position",-1);

        etNote.setText(note);
        etTitle.setText(title);


        imgBack.setOnClickListener(v -> {
            imgSave.performClick();
        });
        imgSave.setOnClickListener(v -> {
            if (position== -1) {
                if (!etTitle.getText().toString().replace(" ","").equalsIgnoreCase("") || !etNote.getText().toString().replace(" ","").equalsIgnoreCase("")) {
                    databaseHelper.expenseDao().addTx(new Expense(etTitle.getText().toString(), etNote.getText().toString()));
                    //Toast.makeText(this, "add", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }else {
                int noteCharacter = etNote.getText().toString().replace(" ","").length();
                int titleCharacter = etNote.getText().toString().replace(" ","").length();

                if (!(noteCharacter ==0) || !(titleCharacter ==0)) {
                    databaseHelper.expenseDao().updateTx(new Expense(position, etTitle.getText().toString(), etNote.getText().toString()));
                    //Toast.makeText(this, "update", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    databaseHelper.expenseDao().deleteTx(new Expense(position,etTitle.getText().toString(), etNote.getText().toString()));
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

      //  etNote.setFocusable(false);

        etNote.setInputType(InputType.TYPE_TEXT_VARIATION_NORMAL);

        mainLy.setOnClickListener(v -> {
            etNote.setInputType(InputType.TYPE_CLASS_TEXT);
           etNote.requestFocus();
           etNote.setFocusable(true);
           etNote.setCursorVisible(true);
           etNote.setFocusableInTouchMode(true);
           etNote.setSelection(etNote.getText().length());
           openKeyboard();
            //Toast.makeText(this, "clicked", Toast.LENGTH_SHORT).show();
        });
        int character = etNote.getText().toString().replace(" ","").length();
        tvChar.setText(character+" characters");
        etNote.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int character = s.toString().replace(" ","").length();
                tvChar.setText(character+" characters");
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }

    private void openKeyboard(){
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null){
            imm.showSoftInput(etNote, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        imgSave.performClick();
    }
}