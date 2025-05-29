package com.example.bookcrudwithapi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.bookcrudwithapi.R;
import com.example.bookcrudwithapi.model.Book;
import com.example.bookcrudwithapi.service.ApiService;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddBookActivity extends AppCompatActivity {
    private EditText editName, editAuthorName, decimalPrice;
    private Button button;
    private ApiService apiService;

    private boolean isEditMode = false;

    private int bookId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_book);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


        editName = findViewById(R.id.editName);
        editAuthorName = findViewById(R.id.editAuthorName);
        decimalPrice = findViewById(R.id.decimalPrice);

        button = findViewById(R.id.button);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8081/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);
        button.setOnClickListener(v -> saveOrUpdateBook());

        Intent intent = getIntent();
        if (getIntent().hasExtra("book")) {
            Book book = new Gson()
                    .fromJson(intent.getStringExtra("book"), Book.class);
            bookId = book.getId();
            editName.setText(book.getName());
            editAuthorName.setText(book.getAuthor_name());
            decimalPrice.setText(String.valueOf(book.getPrice()));



            button.setText(R.string.update);
            isEditMode = true;

        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

//    private void showDatePicker() {
//        final Calendar calendar = Calendar.getInstance();
//        int year = calendar.get(Calendar.YEAR);
//        int month = calendar.get(Calendar.MONTH);
//        int day = calendar.get(Calendar.DAY_OF_MONTH);
//
//        DatePickerDialog picker = new DatePickerDialog(this,
//                (view, year1, month1, day1) -> {
//                    String dob = String.format(Locale.US, "%04d-%02d-%02d", year1, month1, day1);
//                    editTextDob.setText(dob);
//                },
//                year, month, day);
//        picker.show();
//    }
    private void saveOrUpdateBook() {
        String name = editName.getText().toString().trim();
        String authors_name = editAuthorName.getText().toString().trim();
        double price = Double.parseDouble(decimalPrice.getText().toString().trim());

        Book book = new Book();
        if (isEditMode) {
            book.setId(bookId);
        }
        book.setName(name);
        book.setAuthor_name(authors_name);

        book.setPrice(price);

        Call<Book> call;
        if (isEditMode) {
            call = apiService.updateBook(bookId, book);
        } else {
            call = apiService.saveBook(book);
        }
        String string = call.toString();
        System.out.println(string);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Book> call, @NonNull Response<Book> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(AddBookActivity.this, "Book saved successfully!",
                            Toast.LENGTH_SHORT).show();
                    clearForm();
                    Intent intent = new Intent(AddBookActivity.this, BookListActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(AddBookActivity.this, "Failed to save book "
                            + response.message(), Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onFailure(@NonNull Call<Book> call, @NonNull Throwable t) {
                Toast.makeText(AddBookActivity.this, "Error: " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void clearForm() {
        editName.setText("");
        editAuthorName.setText("");
        decimalPrice.setText("");

    }
}