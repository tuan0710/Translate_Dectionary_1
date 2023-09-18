package com.example.translate_dectionary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

public class anhviet extends AppCompatActivity {

    ArrayList<String> arrayName;

    Button home;
    Spinner spin;
        String anh[] = {"Please choose from.", "Pest", "Pond", "Barley", "Potatoes", "Ants", "Apple", "Apiculture", "Quail", "Rice", "Rainfall", "Beans", "Basils", "Seed", "Shurbs", "Snowfall", "Spinach", "Storm", " Cereals", "Tree", "Cabbage"};
    String viet[] = {"", "Côn trùng", "Cái ao", "Lúa mạch", "Khoai tây", "Kiến", "Táo", "Nuôi ong", "Chim cút", "Gạo", "Lượng mưa", "Đậu", "Rau húng quế", "Hạt giống", "Cây bụi", "Tuyết rơi", "Rau dền", "Bão", "Ngũ cốc", "Cây cối", "Cải bắp"};
    EditText  tiengviet;
    ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anhviet);

        home = (Button) findViewById(R.id.home);
        tiengviet = (EditText) findViewById(R.id.txtViet);
        img = (ImageView) findViewById(R.id.imageView);

        spin = (Spinner) findViewById(R.id.spinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,anh);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);

        spin.setAdapter(adapter);
        spin.setOnItemSelectedListener(new anhviet.MyProcessEvent());

        String[] mangten = getResources().getStringArray(R.array.list_name);
        arrayName = new ArrayList<>(Arrays.asList(mangten));

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(anhviet.this , MainActivity.class);
                startActivity(i);
            }
        });
    }

    private class MyProcessEvent implements AdapterView.OnItemSelectedListener{
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            tiengviet.setText(viet[i]);
            int idHinh = getResources().getIdentifier(arrayName.get(i), "drawable", getPackageName());
            img.setImageResource(idHinh);
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
            tiengviet.setText("");
        }
    }
}