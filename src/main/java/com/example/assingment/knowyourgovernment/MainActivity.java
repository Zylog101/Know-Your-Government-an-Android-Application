package com.example.assingment.knowyourgovernment;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.text.InputType;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG ="save" ;
    List<Official> myOfficialList = new ArrayList<>();
    RecyclerView myRecyclerView;
    Adapter myAdapter;
    TextView locationView;
    Locator locator;

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 5) {
            for (int i = 0; i < permissions.length; i++) {
                if (permissions[i].equals(Manifest.permission.ACCESS_FINE_LOCATION)) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        locator.setUpLocationManager();
                    } else {
                        locationView.setText("Set location manually");
                    }
                }
            }
        }
    }

    public void setLocation(double latitude, double longitude)
    {
        List<Address> addresses = null;
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            Address requiredAddress=addresses.get(0);
            String location=requiredAddress.getAddressLine(1);
            InformationDownloader informationDownloader=new InformationDownloader(MainActivity.this);
            informationDownloader.execute(location.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setLocationByAddress(String address)
    {
        List<Address> addresses = null;
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocationName(address, 1);
            if(addresses.size() == 0)
            {
                locationView.setText("No Data For location");
                return;
            }

           /* StringBuilder location = new StringBuilder();
            Address requiredAddress=addresses.get(0);
            int count = requiredAddress.getMaxAddressLineIndex();

            if(count > 2) {
                location.append(requiredAddress.getAddressLine(2));
            }
            else {

                for (int startIndex = 0; startIndex <= count; startIndex++) {
                    location.append(requiredAddress.getAddressLine(startIndex));
                    if (startIndex != count) {
                        location.append(", ");
                    }
                }
            }*/
            InformationDownloader informationDownloader=new InformationDownloader(MainActivity.this);
            informationDownloader.execute(address);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myRecyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        myAdapter = new Adapter(myOfficialList, this);

        myRecyclerView.setAdapter(myAdapter);
        myRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        locationView = (TextView)findViewById(R.id.location_view);

        if(!IsConnectionAvailable())
        {
            locationView.setText("No Data For location");
            myOfficialList.clear();
            myAdapter.notifyDataSetChanged();
            ShowNoNetworkAvailableDialogue();
            return;
        }
        locator=new Locator(this);

    }

    boolean IsConnectionAvailable()
    {
        ConnectivityManager connectivityManager =(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo currentNetwork=connectivityManager.getActiveNetworkInfo();
        return currentNetwork!=null&&currentNetwork.isConnectedOrConnecting();
    }

    private void ShowNoNetworkAvailableDialogue() {
        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        builder.setTitle("No Network Connection");
        builder.setMessage("Data cannot be accessed/loaded\nwithout and internet connection.");
        builder.create().show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.search:
                if(!IsConnectionAvailable())
                {
                    locationView.setText("No Data For location");
                    myOfficialList.clear();
                    myAdapter.notifyDataSetChanged();
                    ShowNoNetworkAvailableDialogue();
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                final EditText searchEditTextView = new EditText(this);
                searchEditTextView.setInputType(InputType.TYPE_CLASS_TEXT);
                searchEditTextView.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
                searchEditTextView.setGravity(Gravity.CENTER_HORIZONTAL);
                builder.setView(searchEditTextView);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setLocationByAddress(searchEditTextView.getText().toString());
                     //   InformationDownloader informationDownloader=new InformationDownloader(MainActivity.this);
                       // informationDownloader.execute(searchEditTextView.getText().toString());
                    }
                });
                builder.setNegativeButton("CANCEL",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.setTitle("Enter a City, State, or a Zip Code:");
                AlertDialog dialog=builder.create();
                dialog.show();

                break;
            case R.id.about:
                Intent intent;
                intent = new Intent(MainActivity.this, About.class);
                startActivity(intent);
                break;
            default: break;
        }
        return super.onOptionsItemSelected(item);
    }

    void OnOfficialInformationReady(List<Official> officialList, String location)
    {
        myOfficialList.clear();
        for(Official official:officialList)
        {
            myOfficialList.add(official);
        }
        myAdapter.notifyDataSetChanged();
        if(location.isEmpty())
        {
            locationView.setText("No Data found");
        }
        else {
            locationView.setText(location);
        }
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(TAG, locationView.getText().toString());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        locationView = (TextView)findViewById(R.id.location_view);


        locationView.setText(savedInstanceState.getString(TAG));
        InformationDownloader informationDownloader=new InformationDownloader(MainActivity.this);
        informationDownloader.execute(savedInstanceState.getString(TAG));

    }
    @Override
    public void onClick(View view) {
        Official official=myOfficialList.get(myRecyclerView.getChildLayoutPosition(view));
        Intent intent =new Intent(MainActivity.this, OfficialActivity.class);
        intent.putExtra("officialObj",official);
        intent.putExtra("location",locationView.getText());
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {

        if(locator!=null) {
            locator.destroy();
        }
        super.onDestroy();
    }
}
