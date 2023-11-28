package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    MyContentProvider provider = new MyContentProvider();
    EditText e1;
    EditText e2;
    EditText e3;
    TextView t1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button b1 = (Button) findViewById(R.id.b1);
        Button b2 = (Button) findViewById(R.id.b2);
        Button b3 = (Button) findViewById(R.id.b3);
        b1.setOnClickListener(this);
        b2.setOnClickListener(this);
        b3.setOnClickListener(this);
        e1 = (EditText) findViewById(R.id.e1);
        e2 = (EditText) findViewById(R.id.e2);
        e3 = (EditText) findViewById(R.id.e3);
        t1 = (TextView) findViewById(R.id.t1);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.b1) {
            if (e1.getText().toString().length() == 0 || e2.getText().toString().length() == 0 || e3.getText().toString().length() == 0) {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            } else {   
                int age = Integer.parseInt(e2.getText().toString());

                if (age < 18 || age > 65) {
                    Toast.makeText(this, "Age must be between 18 and 65", Toast.LENGTH_SHORT).show();
                    return; // Stop execution if age is invalid
                }
                ContentValues values = new ContentValues();
                values.put("Nom", e1.getText().toString()); // Replace "column_name_1" with your column name
                values.put("Age", Integer.parseInt(e2.getText().toString())); // Replace "column_name_2" with your column name
                values.put("Dep", e3.getText().toString());
                Uri uri = getContentResolver().insert(MyContentProvider.URL, values);
                if (uri != null) {
                    // Insert successful
                    Toast.makeText(this, "Data inserted successfully", Toast.LENGTH_SHORT).show();
                } else {
                    // Insert failed
                    Toast.makeText(this, "Failed to insert data", Toast.LENGTH_SHORT).show();
                }

            }

        } else if (v.getId() == R.id.b2) {
            e1.setText("");
            e2.setText("");
            e3.setText("");
        } else if (v.getId() == R.id.b3) {
            StringBuilder res = new StringBuilder();
            Uri allEmployeesUri = MyContentProvider.URL; // Use the appropriate URI for retrieving all employees
            Cursor cursor = getContentResolver().query(allEmployeesUri, null, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                // Iterate through the cursor to get all employee data
                do {
                    int id = cursor.getInt(0);
                    String name = cursor.getString(1);
                    int age = cursor.getInt(2); // Corrected line to retrieve age
                    String department = cursor.getString(3); // Replace "Dep" with your column name for department

                    res.append("ID: ").append(id).append("\n")
                            .append("Name: ").append(name).append("\n")
                            .append("Age: ").append(age).append("\n")
                            .append("Department: ").append(department).append("\n\n");

                } while (cursor.moveToNext());

                t1.setText(res.toString());
                Toast.makeText(this, res.toString(), Toast.LENGTH_SHORT).show();
                cursor.close();
            } else {
                // No employees found or an error occurred
                Toast.makeText(this, "No employees found or an error occurred", Toast.LENGTH_SHORT).show();
            }
        }
    }
}