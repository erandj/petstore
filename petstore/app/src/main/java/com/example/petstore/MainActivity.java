package com.example.petstore;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    TextView mTextView;
    ImageView mImageView;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView = (TextView) findViewById(R.id.textView);
        mImageView = (ImageView) findViewById(R.id.imageView);
        mContext = getBaseContext();

        PetStoreAPI petStoreAPI = PetStoreAPI.retrofit.create(PetStoreAPI.class);

        final Call<PetStore> call = petStoreAPI.getData(1232);

        call.enqueue(new Callback<PetStore>() {
            @Override
            public void onResponse(Call<PetStore> call, Response<PetStore> response) {
                if (response.isSuccessful()) {
                    PetStore pet = response.body();

                    mTextView.setText(pet.getName());
                    Picasso.with(mContext)
                         .load(pet.getPhotoUrls().get(0))
                         .error(R.drawable.ic_launcher_foreground)
                         .into(mImageView);
                }else {
                    ResponseBody errorBody = response.errorBody();

                    try {
                        mTextView.setText(errorBody.string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<PetStore> call, Throwable throwable) {
                mTextView.setText("Что-то пошло не так: " + throwable.getMessage());
            }
        });
    }
}