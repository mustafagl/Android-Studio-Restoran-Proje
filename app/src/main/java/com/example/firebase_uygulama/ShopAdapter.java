package com.example.firebase_uygulama;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.HashMap;

public class ShopAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private ArrayList<shop_info> shopArrayList;
    Activity this_act;
    FirebaseAuth myAuth;
    FirebaseUser myUser;
    DatabaseReference myReference;
    static HashMap<String,Object> myData= new HashMap<>();

    static int Counter=0;

    public ShopAdapter(Activity activity, ArrayList<shop_info> padisahArrayList) {

        this.mInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.shopArrayList = padisahArrayList;
        this.this_act=activity;
    }

    @Override
    public int getCount() {
        return shopArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return shopArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        myAuth=FirebaseAuth.getInstance();
        myReference= FirebaseDatabase.getInstance().getReference();

        view = mInflater.inflate(R.layout.custom_list, null);
        TextView padisahIsim = (TextView) view.findViewById(R.id.list_row_textview_isim);
        TextView padisahSure = (TextView) view.findViewById(R.id.list_row_textview_sure);
        ImageView padisaImage = (ImageView) view.findViewById(R.id.list_row_imageview_padisah);
        Button sepet_btn = (Button) view.findViewById(R.id.sepet);
        shop_info padisah = shopArrayList.get(i);
        padisahIsim.setText(padisah.getIsim());
        padisahSure.setText(padisah.get_bilgi());
        sepet_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                myUser=myAuth.getCurrentUser();


                myData.put(String.valueOf(Counter),padisah);
                Counter++;
                myReference.child("Kullanicilar").child(myUser.getUid()).child("sepet").setValue(myData).addOnCompleteListener(this_act, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(this_act,"Kayıt tamamlandı",Toast.LENGTH_SHORT).show();
                            Log.d("TAG", "başarılı");
                        }
                        else{

                            Toast.makeText(this_act,task.getException().getMessage(),Toast.LENGTH_SHORT).show();

                        }
                    }
                });


            }
        });

        String urlImage = "https://firebasestorage.googleapis.com/v0/b/fir-71256.appspot.com/o/2610090_810x458.jpeg?alt=media&token=9de3dd5f-afc0-4c07-8c95-c67ddf071c51";

        Glide.with(this.this_act)
                .load(urlImage)
                .into(padisaImage);

        return view;
    }
}