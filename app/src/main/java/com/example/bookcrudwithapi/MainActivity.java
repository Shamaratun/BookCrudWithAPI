package com.example.bookcrudwithapi;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.bookcrudwithapi.activity.AddBookActivity;
import com.example.bookcrudwithapi.activity.BookListActivity;

public class MainActivity extends AppCompatActivity {


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
            Button btnAddBook = findViewById(R.id.btnAddBook);
            Button btnListBook = findViewById(R.id.btnListBook);

            btnAddBook.setOnClickListener(v -> navigateToAddBookPage());
            btnListBook.setOnClickListener(v -> navigateToBookListPage());
        }



    private void navigateToBookListPage() {
        Intent intent = new Intent(MainActivity.this, BookListActivity.class);
        startActivity(intent);


    }

    private void navigateToAddBookPage() {
        Intent intent = new Intent(MainActivity.this, AddBookActivity.class);
        startActivity(intent);
    }
    }
