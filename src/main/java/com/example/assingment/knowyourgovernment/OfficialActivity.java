package com.example.assingment.knowyourgovernment;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.util.Linkify;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

import static com.example.assingment.knowyourgovernment.R.id.edit_query;
import static com.example.assingment.knowyourgovernment.R.id.imageView;

public class OfficialActivity extends AppCompatActivity {
    private Official myOfficial;
    private String location;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_official);
        myOfficial = (Official) getIntent().getParcelableExtra("officialObj");
        location=getIntent().getStringExtra("location");
        ((TextView)findViewById(R.id.location_view)).setText(location);
        ((TextView)findViewById(R.id.office_view)).setText(myOfficial.getOfficeTitle());
        ((TextView)findViewById(R.id.name_view)).setText(myOfficial.getName());
        String partyName=myOfficial.getPartyName();
        if(partyName.equals("Democratic")|| partyName.equals("Republican")) {
            ((TextView) findViewById(R.id.party_view)).setText("(" + partyName + ")");
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

            findViewById(R.id.innerLayout).setBackgroundColor(getColor(colorId));

        }
        else
        {
            ((TextView)findViewById(R.id.party_view)).setVisibility(View.GONE);
            //making the view invisible
        }

        uploadPhoto((ImageView)findViewById(R.id.official_image_view));
        ((TextView)findViewById(R.id.address_view)).setText(myOfficial.getAddress());
        ((TextView)findViewById(R.id.phone_view)).setText(myOfficial.getPhone());
        ((TextView)findViewById(R.id.email_view)).setText(myOfficial.getEmail());
        ((TextView)findViewById(R.id.web_view)).setText(myOfficial.getWebsiteUrl());

        Linkify.addLinks(((TextView)findViewById(R.id.address_view)), Linkify.MAP_ADDRESSES);
        ((TextView)findViewById(R.id.address_view)).setLinkTextColor(Color.parseColor("#ffffff"));
        Linkify.addLinks(((TextView)findViewById(R.id.phone_view)), Linkify.PHONE_NUMBERS);
        ((TextView)findViewById(R.id.phone_view)).setLinkTextColor(Color.parseColor("#ffffff"));
        Linkify.addLinks(((TextView)findViewById(R.id.email_view)), Linkify.EMAIL_ADDRESSES);
        ((TextView)findViewById(R.id.email_view)).setLinkTextColor(Color.parseColor("#ffffff"));
        Linkify.addLinks(((TextView)findViewById(R.id.web_view)), Linkify.WEB_URLS);
        ((TextView)findViewById(R.id.web_view)).setLinkTextColor(Color.parseColor("#ffffff"));
        setMediaIcons(myOfficial);


    }

    private void setMediaIcons(Official official) {
        if(official.getFacebookChannel().equals("No Data Provided"))
        {
            ((ImageView)findViewById(R.id.facebook_icon)).setVisibility(View.INVISIBLE);
        }
        if(official.getGoogleChannel().equals("No Data Provided"))
        {
            ((ImageView)findViewById(R.id.google_icon)).setVisibility(View.INVISIBLE);
        }
        if(official.getTwitterChannel().equals("No Data Provided"))
        {
            ((ImageView)findViewById(R.id.twitter_icon)).setVisibility(View.INVISIBLE);
        }
        if(official.getYoutubeChannel().equals("No Data Provided"))
        {
            ((ImageView)findViewById(R.id.youtube_icon)).setVisibility(View.INVISIBLE);
        }

    }

    private void uploadPhoto(final ImageView viewById) {
        String photoUrl=myOfficial.getPhotoUrl();
        if(photoUrl.equals("No Data Provided"))
        {
            viewById.setImageResource(R.drawable.missingimage);
            return;
        }
        Picasso picasso = new Picasso.Builder(this)
                .listener(new Picasso.Listener() {
                    @Override
                    public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                        // Here we try https if the http image attempt failed
                        String photoUrl=myOfficial.getPhotoUrl();
                        final String changedUrl = photoUrl.replace("http:", "https:");
                        picasso.load(changedUrl)
                                .error(R.drawable.brokenimage)
                                .placeholder(R.drawable.placeholder)
                                .into(viewById);
                    }
                })
                .build();

        picasso.load(myOfficial.getPhotoUrl())
                .error(R.drawable.brokenimage)
                .placeholder(R.drawable.placeholder)
                .into(viewById);
    }

    public void onOfficialImageClick(View view) {
        String photoUrl=myOfficial.getPhotoUrl();
        if(!photoUrl.equals("No Data Provided"))
        {
            Intent intent=new Intent(this,PhotoDetail.class);
            intent.putExtra("location",location);
            intent.putExtra("name",myOfficial.getName());
            intent.putExtra("officeTitle",myOfficial.getOfficeTitle());
            intent.putExtra("photoUrl",photoUrl);
            intent.putExtra("party",myOfficial.getPartyName());
            startActivity(intent);

        }
    }

    public void onYoutubeClick(View view) {
        String name = myOfficial.getYoutubeChannel();
        Intent intent = null;
        try
        {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setPackage("com.google.android.youtube");
            intent.setData(Uri.parse("https://www.youtube.com/" + name));
            startActivity(intent); }
        catch (ActivityNotFoundException e)
        {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/" + name)));
        }
    }

    public void onGoogleClick(View view) {
        String name = myOfficial.getGoogleChannel();
        Intent intent = null;
        try
        {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setClassName("com.google.android.apps.plus", "com.google.android.apps.plus.phone.UrlGatewayActivity");
            intent.putExtra("customAppUri", name);
            startActivity(intent);
        }
        catch (ActivityNotFoundException e)
        {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://plus.google.com/" + name)));
        }
    }

    public void onTwitterClick(View view) {
        Intent intent = null;
        String name = myOfficial.getTwitterChannel();
        try
        {
            // get the Twitter app if possible
            getPackageManager().getPackageInfo("com.twitter.android", 0);
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=" + name));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        catch (Exception e)
        {
            // no Twitter app, revert to browser
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/" + name));
        }
        startActivity(intent);
    }

    public void onFacebookClick(View view) {
        String FACEBOOK_URL = "https://www.facebook.com/" + myOfficial.getFacebookChannel();
        String urlToUse;
        PackageManager packageManager= getPackageManager();
        try{
           int versionCode = packageManager.getPackageInfo("com.facebook.katana",0).versionCode;
            if(versionCode >= 3002850)
            {
                urlToUse = "fb://facewebmodal/f?href=" + FACEBOOK_URL;
            }
            else
            {
                urlToUse = "fb://page/" + myOfficial.getFacebookChannel();
            }
        }
        catch(PackageManager.NameNotFoundException e)
        {
            urlToUse = FACEBOOK_URL;
        }
        Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
        facebookIntent.setData(Uri.parse(urlToUse));
        startActivity(facebookIntent);
    }
}
