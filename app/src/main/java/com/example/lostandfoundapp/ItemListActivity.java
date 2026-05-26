package com.example.lostandfoundapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ItemListActivity extends AppCompatActivity {

    ListView listViewAdverts;
    Spinner spinnerFilter;

    DatabaseHelper databaseHelper;
    ArrayList<Advert> advertList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        listViewAdverts = findViewById(R.id.listViewAdverts);
        spinnerFilter = findViewById(R.id.spinnerFilter);

        databaseHelper = new DatabaseHelper(this);

        String[] categories = {"All", "Electronics", "Pets", "Wallets", "Bags", "Keys", "Other"};

        spinnerFilter.setAdapter(
                new ArrayAdapter<>(this,
                        android.R.layout.simple_spinner_dropdown_item,
                        categories)
        );

        loadAdverts();

        spinnerFilter.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {

                String selected = spinnerFilter.getSelectedItem().toString();

                if (selected.equals("All")) {
                    advertList = databaseHelper.getAllAdverts();
                } else {
                    advertList = databaseHelper.getAdvertsByCategory(selected);
                }

                showAdverts();
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {

            }
        });

        listViewAdverts.setOnItemClickListener((parent, view, position, id) -> {

            Intent intent = new Intent(ItemListActivity.this, ItemDetailActivity.class);

            intent.putExtra("advertId", advertList.get(position).getId());

            startActivity(intent);
        });
    }

    private void loadAdverts() {
        advertList = databaseHelper.getAllAdverts();
        showAdverts();
    }

    private void showAdverts() {

        ArrayAdapter<Advert> adapter = new ArrayAdapter<Advert>(
                this,
                R.layout.list_item_advert,
                advertList
        ) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                if (convertView == null) {
                    convertView = getLayoutInflater().inflate(
                            R.layout.list_item_advert,
                            parent,
                            false
                    );
                }

                Advert advert = advertList.get(position);

                ImageView itemImage = convertView.findViewById(R.id.itemImage);
                TextView textType = convertView.findViewById(R.id.textType);
                TextView textDescription = convertView.findViewById(R.id.textDescription);
                TextView textCategory = convertView.findViewById(R.id.textCategory);
                TextView textTimestamp = convertView.findViewById(R.id.textTimestamp);

                itemImage.setImageURI(Uri.parse(advert.getImage()));

                textType.setText(advert.getType());
                textDescription.setText(advert.getDescription());
                textCategory.setText("Category: " + advert.getCategory());
                textTimestamp.setText(advert.getTimestamp());

                return convertView;
            }
        };

        listViewAdverts.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadAdverts();
    }
}
