package com.example.bookcrudwithapi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

public class AddBookActivity extends AppCompatActivity {
    // Declare at top
    private static final int PICK_IMAGE_REQUEST = 1;
    private EditText editName, editAuthorName, decimalPrice, editIsbn, editStock, decimalRating, editGenre, editLocation, created_at, updated_at;
    private Button button;
    private ApiService apiService;
    private ImageView imageView;
    private boolean isEditMode = false;
    private int bookId = -1;
    private Uri imageUri;
    private Bitmap selectedBitmap;

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
        editIsbn = findViewById(R.id.editIsbn);
        editStock = findViewById(R.id.editStock);
        decimalRating = findViewById(R.id.decimalRating);
        editGenre = findViewById(R.id.editGenre);
        editLocation = findViewById(R.id.editLocation);
        created_at = findViewById(R.id.created_at);
        updated_at = findViewById(R.id.updated_at);
        button = findViewById(R.id.button);
        Button buttonSelectImage = findViewById(R.id.buttonSelectImage);
        imageView = findViewById(R.id.imageView);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8081/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);
        button.setOnClickListener(v -> saveOrUpdateBook());

        buttonSelectImage.setOnClickListener(v -> openImagePicker());

        Intent intent = getIntent();
        if (getIntent().hasExtra("book")) {
            Book book = new Gson()
                    .fromJson(intent.getStringExtra("book"), Book.class);
            bookId = book.getId();
            editName.setText(book.getName());
            editAuthorName.setText(book.getAuthor_name());
            decimalPrice.setText(String.valueOf(book.getPrice()));
            editIsbn.setText(book.getIsbn());
            editStock.setText(book.getStock());
            decimalRating.setText(String.valueOf(book.getRating()));
            editGenre.setText(book.getGenre());
            editLocation.setText(book.getWarehouseLocation());
            created_at.setText(book.getCreatedAt());
            updated_at.setText(book.getUpdatedAt());

            imageView.setImageResource(book.getImage());

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
        String genre = editGenre.getText().toString().trim();
        String location = editLocation.getText().toString().trim();
        double rating = Double.parseDouble(decimalRating.getText().toString().trim());
        Integer isbn = Integer.parseInt(editIsbn.getText().toString().trim());
        Integer stock = Integer.parseInt(editStock.getText().toString().trim());

        Integer image = Integer.parseInt(imageView.ImageView().toString().trim());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String currentTime = LocalDateTime.now().format(formatter);


        Book book = new Book();
        if (isEditMode) {
            book.setId(bookId);
        }
        book.setName(name);
        book.setAuthor_name(authors_name);
        book.setPrice(price);
        book.setIsbn(isbn);
        book.setGenre(genre);
        book.setRating(rating);
        book.setStock(stock);
        book.setWarehouseLocation(location);
        book.setCreatedAt(currentTime);

        book.setUpdatedAt(currentTime);

        book.setImage(image);

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

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            try {
                selectedBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                imageView.setImageBitmap(selectedBitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void clearForm() {
        editName.setText("");
        editAuthorName.setText("");
        decimalPrice.setText("");
        editIsbn.setText("");
        imageView.setImageResource(Integer.parseInt(""));
        editStock.setText("");
        decimalRating.setText("");
        editGenre.setText("");
        editLocation.setText("");
        created_at.setText("");
        updated_at.setText("");
    }
}