package com.example.carsproject;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

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
        return carsList.get(i).getID();
    }

    private Bitmap getUserImage(String encodedImg)
    {

        if(encodedImg!=null&& !encodedImg.equals("null")) {
            byte[] bytes = Base64.decode(encodedImg, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        }
        else
        {
            return null;

        }
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
        Model.setText(cars.getModel());
        Brand.setText(cars.getBrand());
        Price.setText(Integer.toString(cars.getPrice()));

        //  imageView.setImageBitmap(getUserImage(mask.get()));


        return v;
    }
}

