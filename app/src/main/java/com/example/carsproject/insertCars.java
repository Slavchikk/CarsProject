package com.example.carsproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.InputStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class insertCars extends AppCompatActivity {

    Bundle arg;
    Cars cars;
    EditText Brand, Model, Price;
    ImageView img;
    Bitmap bitmap=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

        getSupportActionBar().setCustomView(R.layout.toolbar_title_layout);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.teal_200)));
        setContentView(R.layout.activity_insert_cars);
        arg = getIntent().getExtras();
        cars = arg.getParcelable(Cars.class.getSimpleName());
        Brand = findViewById(R.id.txtBrand);
        Model = findViewById(R.id.txtModel);
        Price = findViewById(R.id.txtPrice);
        img = findViewById(R.id.imageInsCars);
        Brand.setText(cars.getCarsBrand());
        Model.setText(cars.getCarsModel());
        Price.setText(Integer.toString(cars.getPrice()));
        DecodeImage decodeImage = new DecodeImage(insertCars.this);
        Bitmap userImage = decodeImage.getUserImage(cars.getImage());
        img.setImageBitmap(userImage);
        if(!cars.getImage().equals("null")){
            bitmap = userImage;
        }



    }

    private final ActivityResultLauncher<Intent> pickImg = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == RESULT_OK) {
            if (result.getData() != null) {
                Uri uri = result.getData().getData();
                try {
                    InputStream is = getContentResolver().openInputStream(uri);
                    bitmap = BitmapFactory.decodeStream(is);
                    img.setImageBitmap(bitmap);
                } catch (Exception e) {
                    Log.e(e.toString(), e.getMessage());
                }
            }
        }
    });



    public void BackMenu(View v){
        startActivity(new Intent(this, MainActivity.class));
    }


    public void ShowError(String ret)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder
                .setTitle("Ошибка при вводе")
                .setMessage(ret)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private String checkData(String model, String brand, String price){
        String ret = "";
        if(model.isEmpty())
        {
            ret +=" Введите модель \n";
        }
        if(brand.isEmpty())
        {
            ret+= " Ведите марку \n";

        }
        if(price.isEmpty())
        {
            ret+=" Введите цену \n";
        }
        try {
            int a = Integer.parseInt(price);
        }
        catch(Exception e){
            ret+= " Цена введена некоректно \n";
        }

        return ret;
    }
    private void DataPut(Cars cars, View v)
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://ngknn.ru:5001/NGKNN/КарзиновВА/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Call<Cars> call = null;
        RetrofitApi retrofitAPIPut = retrofit.create(RetrofitApi.class);
        call = retrofitAPIPut.createPut(cars, cars.getId_cars());
        call.enqueue(new Callback<Cars>() {
            @Override
            public void onResponse(Call<Cars> call, Response<Cars> response) {
                Toast.makeText(insertCars.this, "Данные изменены", Toast.LENGTH_SHORT).show();
                BackMenu(v);
            }

            @Override
            public void onFailure(Call<Cars> call, Throwable t) {
                Toast.makeText(insertCars.this, "Ошибка", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private  void DataDelete(Cars cars, View v)
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://ngknn.ru:5001/NGKNN/КарзиновВА/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Call<Cars> call = null;
        RetrofitApi retrofitAPIsDel = retrofit.create(RetrofitApi.class);
        call = retrofitAPIsDel.createDelete(cars.getId_cars());
        call.enqueue(new Callback<Cars>() {
            @Override
            public void onResponse(Call<Cars> call, Response<Cars> response) {
                Toast.makeText(insertCars.this, "Данные удалены", Toast.LENGTH_SHORT).show();
                BackMenu(v);
            }

            @Override
            public void onFailure(Call<Cars> call, Throwable t) {
                Toast.makeText(insertCars.this, "Ошибка", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void AddPhoto(View v){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        pickImg.launch(intent);
    }



    public void UpdateCars(View v){


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder
                .setTitle("Изменение данных")
                .setMessage("Вы уверены что хотите изменить?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("Да", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {



                        String ret = checkData(Brand.getText().toString(), Model.getText().toString(),Price.getText().toString());
                        if(ret == "") {
                            cars.setCarsBrand(Brand.getText().toString());
                            cars.setCarsModel(Model.getText().toString());
                            cars.setPrice(Integer.parseInt(Price.getText().toString()));
                            EncodeImage encodeImage = new EncodeImage();
                            cars.setImage(encodeImage.Image(bitmap));
                            DataPut(cars, v);
                        }
                        else {
                            ShowError(ret);
                        }
                        dialog.dismiss();
                    }
                });
        builder.setNegativeButton("Нет", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();

    }

    public void DeleteCars(View v){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder
                .setTitle("Удаление данных")
                .setMessage("Вы уверены что хотите удалить?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("Да", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                      DataDelete(cars,  v);
                      dialog.dismiss();
                    }
                });
        builder.setNegativeButton("Нет", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();

    }

}



