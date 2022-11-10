

package com.example.carsproject;
import android.content.Context;
import android.content.Intent;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class AdapterCars  extends BaseAdapter {
    private Context mContext;
    List<Cars> carsList;

    public AdapterCars(Context mContext, List<Cars> listProduct) {
        this.mContext = mContext;
        this.carsList = listProduct;
    }

    @Override
    public int getCount() {
        return carsList.size();
    }

    @Override
    public Object getItem(int i) {
        return carsList.get(i);
    }

    @Override
    public long getItemId(int i)
    {
        return carsList.get(i).getId_cars();
    }



    @Override
    public View getView(int i, View view, ViewGroup viewGroup)
    {
        View v = View.inflate(mContext,R.layout.activity_cars,null);
        TextView Model = v.findViewById(R.id.txtModel);
        TextView Brand = v.findViewById(R.id.txtBrand);
        TextView Price = v.findViewById(R.id.txtPrice);
        ImageView imageView = v.findViewById(R.id.imageView2);
        Cars cars = carsList.get(i);
        Model.setText(cars.getCarsModel());
        Brand.setText(cars.getCarsBrand());
        Price.setText(Integer.toString(cars.getPrice()));
        DecodeImage dcd = new DecodeImage(mContext);
        imageView.setImageBitmap(dcd.getUserImage(cars.getImage()));

        //  imageView.setImageBitmap(getUserImage(mask.get()));

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, insertCars.class);
                intent.putExtra(Cars.class.getSimpleName(), cars);
                mContext.startActivity(intent);
            }
        });
        return v;
    }
}





