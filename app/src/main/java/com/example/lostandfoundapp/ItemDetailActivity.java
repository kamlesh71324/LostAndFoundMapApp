package com.example.lostandfoundapp;

import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ItemDetailActivity extends AppCompatActivity {

    ImageView detailImage;
    TextView textType, textName, textPhone, textDescription,
            textLocation, textCategory, textTimestamp;

    Button btnDelete;

    DatabaseHelper databaseHelper;
    Advert advert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        detailImage = findViewById(R.id.detailImage);
        textType = findViewById(R.id.textType);
        textName = findViewById(R.id.textName);
        textPhone = findViewById(R.id.textPhone);
        textDescription = findViewById(R.id.textDescription);
        textLocation = findViewById(R.id.textLocation);
        textCategory = findViewById(R.id.textCategory);
        textTimestamp = findViewById(R.id.textTimestamp);
        btnDelete = findViewById(R.id.btnDelete);

        databaseHelper = new DatabaseHelper(this);

        int advertId = getIntent().getIntExtra("advertId", -1);

        advert = databaseHelper.getAdvertById(advertId);

        if (advert != null) {

            detailImage.setImageURI(Uri.parse(advert.getImage()));

            textType.setText(advert.getType());
            textName.setText("Name: " + advert.getName());
            textPhone.setText("Phone: " + advert.getPhone());
            textDescription.setText("Description: " + advert.getDescription());
            textLocation.setText("Location: " + advert.getLocation());
            textCategory.setText("Category: " + advert.getCategory());
            textTimestamp.setText("Posted: " + advert.getTimestamp());
        }

        btnDelete.setOnClickListener(v -> {

            boolean deleted = databaseHelper.deleteAdvert(advert.getId());

            if (deleted) {
                Toast.makeText(this, "Advert removed", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Delete failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
