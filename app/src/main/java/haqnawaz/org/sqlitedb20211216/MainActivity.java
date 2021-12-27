package haqnawaz.org.sqlitedb20211216;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    Button buttonAdd, buttonViewAll, btnDelAll;
    EditText editName, editAge;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    Switch switchIsActive;
    ArrayList<StudentModel> list;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonAdd = findViewById(R.id.addBtn);
        buttonViewAll = findViewById(R.id.buttonViewAll);
        btnDelAll = findViewById(R.id.delBtn);

        recyclerView = findViewById(R.id.RecyclerView);
        layoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            StudentModel studentModel;

            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                LayoutInflater inflater = MainActivity.this.getLayoutInflater();
                builder.setTitle("Add Student");
                builder.setView(inflater.inflate(R.layout.studentinfo, new LinearLayout(MainActivity.this), false))
                        .setPositiveButton("Add student", (dialog, id) -> {
                            editName = ((AlertDialog) dialog).findViewById(R.id.nameEt);
                            editAge = ((AlertDialog) dialog).findViewById(R.id.ageEt);
                            switchIsActive = ((AlertDialog) dialog).findViewById(R.id.switchStudent);
                            if( TextUtils.isEmpty(editName.getText()) || TextUtils.isEmpty(editAge.getText()))
                            {
                                Toast.makeText(MainActivity.this, "Fill all the details", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                try
                                {
                                    studentModel = new StudentModel(editName.getText().toString(), Integer.parseInt(editAge.getText().toString()), switchIsActive.isChecked());
                                    DbHelper dbHelper = new DbHelper(MainActivity.this);
                                    dbHelper.addStudent(studentModel);
                                    Toast.makeText(MainActivity.this, "Student Added Successfully", Toast.LENGTH_SHORT).show();
                                    Handler handler = new Handler();
                                    handler.postDelayed(() -> ViewStudents(), 1000);

                                }
                                catch (Exception e){
                                    Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_LONG).show();
                                }}
                        })
                        .setNegativeButton("Cancel", (dialog, id) -> dialog.cancel());
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        buttonViewAll.setOnClickListener(v -> ViewStudents());

        btnDelAll.setOnClickListener(view -> {
            if(layoutManager.getItemCount()>0)
            {
                DbHelper dbHelper = new DbHelper(MainActivity.this);
                list = dbHelper.getAllStudents();

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("All Students will be permanent deleted");
                builder.setTitle("Delete Students");
                builder.setCancelable(false);
                builder.setPositiveButton("Delete", (dialog, asdf) -> {
                    RecyclerViewAdapter adapter = new RecyclerViewAdapter(list);
                    recyclerView.setAdapter(adapter);
                    dbHelper.deleteAll();
                    list.clear();
                    Toast.makeText(MainActivity.this, "All Students Deleted Successfully", Toast.LENGTH_SHORT).show();
                });
                builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                dbHelper.close();
            }
            else
            {
                Toast.makeText(MainActivity.this, "There are not any students", Toast.LENGTH_SHORT).show();
            }

        });

    }
}
