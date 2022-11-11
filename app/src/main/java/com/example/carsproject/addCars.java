package com.example.carsproject;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.InputStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
public class addCars extends AppCompatActivity {

    Bundle arg;
    Cars cars;
    ImageView imageCars;
    Bitmap bitmap=null, b;
    EditText Brand, Model, Price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

        getSupportActionBar().setCustomView(R.layout.toolbar_title_layout);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.teal_200)));
        setContentView(R.layout.activity_add_cars);
        imageCars = findViewById(R.id.imageView);

        b = BitmapFactory.decodeResource(addCars.this.getResources(), R.drawable.images);
        Brand = findViewById(R.id.txtBrand);
        Model = findViewById(R.id.txtModel);
        Price = findViewById(R.id.txtPrice);
        imageCars.setImageBitmap(b);
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


    public void AddCars(View v){


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder
                .setTitle("Добавление данных")
                .setMessage("Вы уверены что хотите добавить?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("Да", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {

                        EncodeImage encodeImage = new EncodeImage();

                        String ret = checkData(Brand.getText().toString(), Model.getText().toString(),Price.getText().toString());
                        if(ret == "")

                            postData(Brand.getText().toString(), Model.getText().toString(), Integer.parseInt(Price.getText().toString()), encodeImage.Image(bitmap), v);
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

    private void postData(String model, String brand, int price, String image, View v) {


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://ngknn.ru:5001/ngknn/КарзиновВА/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitApi retrofitAPI = retrofit.create(RetrofitApi.class);
        Cars modal = new Cars(null, model, brand, price, image);
        Call<Cars> call = retrofitAPI.createPost(modal);
        call.enqueue(new Callback<Cars>() {
            @Override
            public void onResponse(Call<Cars> call, Response<Cars> response) {
                Toast.makeText(addCars.this, "Данные добавлены", Toast.LENGTH_SHORT).show();
                BackMenu(v);
            }

            @Override
            public void onFailure(Call<Cars> call, Throwable t) {
                Toast.makeText(addCars.this, "Ошибка", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private final ActivityResultLauncher<Intent> pickImg = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == RESULT_OK) {
            if (result.getData() != null) {
                Uri uri = result.getData().getData();
                try {
                    InputStream is = getContentResolver().openInputStream(uri);
                    bitmap = BitmapFactory.decodeStream(is);
                    imageCars.setImageBitmap(bitmap);
                } catch (Exception e) {
                    Log.e(e.toString(), e.getMessage());
                }
            }
        }
    });
    public void AddPhoto(View v){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        pickImg.launch(intent);
    }



    public void BackMenu(View v){
        startActivity(new Intent(this, MainActivity.class));
    }
}