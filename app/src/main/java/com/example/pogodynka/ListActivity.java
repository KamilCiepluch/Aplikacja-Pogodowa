package com.example.pogodynka;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


public class ListActivity extends AppCompatActivity {

    private final ArrayList<String> items = new ArrayList<>();
    CheckBox checkBoxKel;
    CheckBox checkBoxCel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);

        ListView list = findViewById(R.id.citieslist);
        checkBoxKel = findViewById(R.id.checkKel);
        checkBoxCel = findViewById(R.id.checkCel);

        SharedPreferences sharedPreferences = getSharedPreferences("List", Context.MODE_PRIVATE);
        String tempUnit = sharedPreferences.getString("TempUnit", null);
        if(tempUnit==null)
        {
            checkBoxCel.setChecked(true);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("TempUnit", "Cel");
            editor.apply();
        }
        else {
            if(tempUnit.equals("Kel") )
                 checkBoxKel.setChecked(true);
            else
                checkBoxCel.setChecked(true);
        }

        Set<String> set = sharedPreferences.getStringSet("cities", null);
        if (set != null) {
            items.addAll(set);
            MyListAdapter adapter = new MyListAdapter(this,32,items);
            list.setAdapter(adapter);
        }

        TextView selectedCity = findViewById(R.id.selectedCity);
        String str = getString(R.string.selectedCity) + " " + sharedPreferences.getString("SelectedCity",null);
        selectedCity.setText(str);

        TextView input = findViewById(R.id.cityInput);
        Button add = findViewById(R.id.buttonAdd);
        add.setOnClickListener(view -> {
            String city = input.getText().toString();
            if(!items.contains(city))
            {
                SharedPreferences newCity = getSharedPreferences(city, Context.MODE_PRIVATE);
                int check = JsonThread.getData(city,newCity);
                if(check==0)
                {
                    items.add(city);
                    MyListAdapter adapter = new MyListAdapter(this,32,items);
                    list.setAdapter(adapter);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    Set<String> set1 = new HashSet<>(items);
                    editor.putStringSet("cities", set1);
                    editor.putString("SelectedCity", city);
                    editor.apply();

                    String str1 = getString(R.string.selectedCity) + " " + sharedPreferences.getString("SelectedCity",null);
                    selectedCity.setText(str1);
                }
            }
            else {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("SelectedCity", city);
                editor.apply();
            }
        });


        Button remove = findViewById(R.id.buttonRemove);
        remove.setOnClickListener(view -> {
            String city = input.getText().toString();
            if(items.contains(city))
            {
                items.remove(city);
                MyListAdapter adapter = new MyListAdapter(this,32,items);

                list.setAdapter(adapter);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                Set<String> set12 = new HashSet<>(items);
                editor.putStringSet("cities", set12);
                if(!items.isEmpty())
                    editor.putString("SelectedCity", items.get(0));
                else
                    editor.putString("SelectedCity", null);
                editor.apply();


                SharedPreferences cityData = getSharedPreferences(city, Context.MODE_PRIVATE);
                if(cityData!=null)
                {
                    SharedPreferences.Editor editorTmp = cityData.edit();
                    editorTmp.clear();
                    editorTmp.apply();
                }
                String str1 = getString(R.string.selectedCity) + " " + sharedPreferences.getString("SelectedCity",null);
                selectedCity.setText(str1);
            }
        });


        Button select = findViewById(R.id.buttonSelect);
        select.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {

                TextView selectedCity = findViewById(R.id.selectedCity);
                if(items.contains(input.getText().toString()))
                {

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("SelectedCity", input.getText().toString());
                    editor.apply();


                    String str = getString(R.string.selectedCity) + " " + sharedPreferences.getString("SelectedCity",null);
                    selectedCity.setText(str);
                }
               else
                   selectedCity.setText("There's not such city on the list");
            }
        });

        checkBoxKel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkBoxCel.isChecked())
                {
                    checkBoxCel.setChecked(false);
                    checkBoxKel.setChecked(true);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("TempUnit", "Kel");
                    editor.apply();
                }
            }
        });



        checkBoxCel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(checkBoxKel.isChecked())
                {
                    checkBoxKel.setChecked(false);
                    checkBoxCel.setChecked(true);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("TempUnit", "Cel");
                    editor.apply();
                }
            }
        });
    }

}
