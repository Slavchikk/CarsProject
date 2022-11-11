package com.example.carsproject;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;


public class MainActivity extends AppCompatActivity {
    private AdapterCars pAdapter;
    TextView txtPrice;
    TextView txtBrand;
    TextView txtModel;
    TextView txtID;
    TextView image;
    EditText edFind;
    ListView lstCars;
    Spinner spinner;
    private List<Cars> listCars = new ArrayList<>();
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lstCars = findViewById(R.id.lvData);

        ListView ivProducts = findViewById(R.id.lvData);//Находим лист в который будем класть наши объекты
        pAdapter = new AdapterCars(MainActivity.this, listCars); //Создаем объект нашего адаптера
        ivProducts.setAdapter(pAdapter);  //Cвязывает подготовленный список с адаптером

        //new GetProducts().execute(); //Подключение к нашей API в отдельном потоке
        String[]items = {"<по умолчанию>","По марке","По модели", "По цене"};
        spinner = findViewById(R.id.spinner);
        edFind = findViewById(R.id.edfind);
        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if(edFind.getText().toString().isEmpty()){
                    Sort(listCars);
                }
                else{
                    Search();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
        edFind.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { // реальное время dude...
                Search();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void Search(){
        List<Cars> lstFilter = listCars.stream().filter(x-> (x.carsModel.toLowerCase(Locale.ROOT).contains(edFind.getText().toString().toLowerCase(Locale.ROOT)))).collect(Collectors.toList());
        Sort(lstFilter);
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public void Sort(List<Cars> list){
        lstCars.setAdapter(null);

        switch(spinner.getSelectedItemPosition()){
            case 0:
                if(edFind.getText().toString().isEmpty()){
                    new GetProducts().execute();
                }
                break;
            case 1:
                Collections.sort(list, Comparator.comparing(Cars::getCarsBrand));
                break;
            case 2:
                Collections.sort(list, Comparator.comparing(Cars::getCarsModel));

                break;
            case 3:
                Collections.sort(list, Comparator.comparing(Cars::getPrice));

                break;
            default:
                break;
        }
       SetAdapter(list);
    }


    public void SetAdapter(List<Cars> list){
        pAdapter = new AdapterCars(MainActivity.this,list);
        lstCars.setAdapter(pAdapter);
        pAdapter.notifyDataSetInvalidated();

    }

    public void AddCars(View v){
        startActivity(new Intent(this, addCars.class));
    }


    private class GetProducts extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL("https://ngknn.ru:5001/NGKNN/КарзиновВА/api/Cars");//Строка подключения к нашей API
                HttpURLConnection connection = (HttpURLConnection) url.openConnection(); //вызываем нашу API
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder result = new StringBuilder();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    result.append(line);//кладет строковое значение в потоке
                }
                return result.toString();

            } catch (Exception exception) {
                return null;
            }
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try
            {
                JSONArray tempArray = new JSONArray(s);//преоброзование строки в json массив
                for (int i = 0;i<tempArray.length();i++)
                {

                    JSONObject productJson = tempArray.getJSONObject(i);//Преобразование json объекта в нашу структуру
                    Cars tempProduct = new Cars(
                            productJson.getInt("id_cars"),
                            productJson.getString("carsBrand"),
                            productJson.getString("carsModel"),
                            productJson.getInt("price"),
                            productJson.getString("image")
                    );
                    listCars.add(tempProduct);
                    pAdapter.notifyDataSetInvalidated();
                }
            } catch (Exception ignored) {


            }
        }


    }
}









