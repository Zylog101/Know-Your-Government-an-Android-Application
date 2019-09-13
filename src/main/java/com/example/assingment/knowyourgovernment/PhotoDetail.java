package com.example.assingment.knowyourgovernment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class PhotoDetail extends AppCompatActivity {
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_detail);
        Intent intent=getIntent();
        ((TextView)findViewById(R.id.location_view)).setText( intent.getStringExtra("location"));
        ((TextView)findViewById(R.id.name_view)).setText(intent.getStringExtra("name"));
        ((TextView)findViewById(R.id.office_view)).setText( intent.getStringExtra("officeTitle"));
        setBackground(intent.getStringExtra("party"));
        uploadPhoto( intent.getStringExtra("photoUrl"));
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void setBackground(String partyName) {
        int colorId;
        if (partyName.equals("Democratic")) {
            colorId = R.color.colorDemocratic;
        } else if (partyName.equals("Republican")) {
            colorId = R.color.colorRepublican;
        }
        else
        {
            colorId=R.color.colorOther;
        }

        findViewById(R.id.photo_detail_layout).setBackgroundColor(getColor(colorId));
    }

    private void uploadPhoto(final String photoUrl) {
        Picasso picasso = new Picasso.Builder(this)
                .listener(new Picasso.Listener() {
                    @Override
                    public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                        // Here we try https if the http image attempt failed

                        final String changedUrl = photoUrl.replace("http:", "https:");
                        picasso.load(changedUrl)
                                .error(R.drawable.brokenimage)
                                .placeholder(R.drawable.placeholder)
                                .into((ImageView)findViewById(R.id.imageView2));
                    }
                })
                .build();

        picasso.load(photoUrl)
                .error(R.drawable.brokenimage)
                .placeholder(R.drawable.placeholder)
                .into((ImageView)findViewById(R.id.imageView2));
    }
}
