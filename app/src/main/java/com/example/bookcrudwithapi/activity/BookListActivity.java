package com.example.bookcrudwithapi.activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.bookcrudwithapi.R;
import com.example.bookcrudwithapi.adapter.BookAdapter;

import com.example.bookcrudwithapi.model.Book;
import com.example.bookcrudwithapi.service.ApiService;

import java.util.ArrayList;
import java.util.List;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BookListActivity extends AppCompatActivity {

    private static final String TAG = "BookListActivity";
    private RecyclerView recyclerView;
  private BookAdapter bookAdapter;
    private List<Book> bookList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_book_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        bookAdapter = new BookAdapter(this,bookList);
        recyclerView = findViewById(R.id.bookRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(bookAdapter);

        fetchBooks();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void fetchBooks() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8081/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);

        Call<List<Book>> call =apiService.getAllBook();

        call.enqueue(new Callback<>() {


            @Override
            public void onResponse(@NonNull retrofit2.Call<List<Book>> call, @NonNull Response<List<Book>> response) {
                if (response.isSuccessful()) {
                    List<Book> books = response.body();
//                    if (books != null) {
//                        bookList.clear();
//                        bookList.addAll(books);
//                        bookAdapter.notifyDataSetChanged();
//                        }
                    assert books != null;
                    for (Book item : books) {
                        Log.d(TAG, "ID: " + item.getId() + ", Name: "+item.getName()+",Author Name"
                                + item.getAuthor_name() + ", Price: " + item.getPrice());
                    }
                    bookList.clear();
                    bookList.addAll(books);
                    bookAdapter.notifyDataSetChanged();
                } else {
                    Log.e(TAG, "Received null book list from API" + response.code());
                }

        }
            @Override
            public void onFailure(Call<List<Book>> call, Throwable t) {

                    Log.e(TAG, "API Call Failed: " + t.getMessage());
            }
        });
    }
}
