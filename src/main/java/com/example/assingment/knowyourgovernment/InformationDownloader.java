package com.example.assingment.knowyourgovernment;


import android.net.Uri;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class InformationDownloader extends AsyncTask<String, Void, List<Official>> {

    private String myInfoDownloaderUrl = "https://www.googleapis.com/civicinfo/v2/representatives";
    private String myAPIKey = "AIzaSyA_6u-Y_4BJKPDubkh2VhFH8GHVYDPR7qA";
    private MainActivity myMainActivity;
    private String myZipCode;
    private String locationData = "";


    public InformationDownloader(MainActivity mainActivity) {
        this.myMainActivity = mainActivity;
    }

    @Override
    protected List<Official> doInBackground(String... params) {
        myZipCode = params[0];

        Uri.Builder buildURL=Uri.parse(myInfoDownloaderUrl).buildUpon();

        buildURL.appendQueryParameter("key",myAPIKey);

        buildURL.appendQueryParameter("address",myZipCode);
        String urlString=buildURL.build().toString();

        StringBuilder sb = new StringBuilder();
        try {
            URL url= new URL(urlString);

            HttpURLConnection connection=(HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            InputStream inputStream=connection.getInputStream();
            BufferedReader reader = new BufferedReader((new InputStreamReader(inputStream)));
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return parseJSON(sb.toString());
    }

    @Override
    protected void onPostExecute(List<Official> tempOfficialList) {
        myMainActivity.OnOfficialInformationReady(tempOfficialList, locationData);
    }

    private List<Official> parseJSON(String s) {
        List<Official> officialList = new ArrayList<>();
        try {
            String noDataProvided = "No Data Provided";
            JSONObject jsonObject=new JSONObject(s);
            JSONObject jsonObjectNormalizedInput = jsonObject.optJSONObject("normalizedInput");
            if(jsonObjectNormalizedInput==null)
            {
                return null;
            }
            String city = jsonObjectNormalizedInput.optString("city", "");
            String state = jsonObjectNormalizedInput.optString("state", "");
            String zipCode = jsonObjectNormalizedInput.optString("zip", "");
            locationData += city + (!Objects.equals(state, "") ? ", " + state : state) +
                    (!Objects.equals(zipCode, "") ? ", " + zipCode : zipCode);

            JSONArray jsonArrayOfficials = jsonObject.optJSONArray("officials");
            if(jsonArrayOfficials==null)
            {
                return null;
            }
            JSONArray jsonArrayOffices = jsonObject.optJSONArray("offices");
            if(jsonArrayOffices==null)
            {
                return null;
            }

            int length=jsonArrayOffices.length();
            for (int i=0;i<length;i++)
            {
                JSONObject jObject=jsonArrayOffices.optJSONObject(i);
                if(jObject==null)
                {
                    continue;
                }
                String OfficeTitle = jObject.optString("name", noDataProvided);
                JSONArray officialIndices = jObject.optJSONArray("officialIndices");
                if(officialIndices==null)
                {
                    continue;
                }
                int indicesLength = officialIndices.length();
                for(int j = 0; j < indicesLength; j++)
                {
                    int officialsIndex = officialIndices.optInt(j);
                    JSONObject officialsObject = jsonArrayOfficials.optJSONObject(officialsIndex);
                    if(officialsObject==null)
                    {
                        continue;
                    }

                    String OfficialsName = officialsObject.optString("name", noDataProvided);
                    JSONArray addressArray=officialsObject.optJSONArray("address");
                    JSONObject addressObject=null;
                    String OfficialsAddress=noDataProvided;
                    if(addressArray!=null) {
                        addressObject = addressArray.getJSONObject(0);
                        StringBuilder sb = new StringBuilder();
                        String line1Text=addressObject.optString("line1", "");
                        sb.append(!line1Text.equals("")?line1Text:"");
                        if(!line1Text.equals(""))sb.append(", ");
                        String line2Text = addressObject.optString("line2", "");
                        sb.append(!line2Text.equals("") ? line2Text: "");
                        if(!line2Text.equals(""))sb.append(", ");
                        line2Text = addressObject.optString("city", "");
                        sb.append(!line2Text.equals("") ? line2Text: "");
                        if(!line2Text.equals(""))sb.append(", ");
                        line2Text = addressObject.optString("state", "");
                        sb.append(!line2Text.equals("") ? line2Text: "");
                        if(!line2Text.equals(""))sb.append(", ");
                        line2Text = addressObject.optString("zip", "");
                        sb.append(!line2Text.equals("") ? line2Text: "");

                        OfficialsAddress = sb.toString();
                    }
                    String OfficialsParty=officialsObject.optString("party", noDataProvided);

                    JSONArray officialsPhoneArray=officialsObject.optJSONArray("phones");
                    String OfficialsPhone=noDataProvided;
                    if(officialsPhoneArray!=null)
                    {
                       OfficialsPhone = officialsPhoneArray.length() > 0 ? officialsPhoneArray.optString(0) : noDataProvided;
                    }


                    JSONArray officialsUrlArray = officialsObject.optJSONArray("urls");
                    String OfficialsUrl = null;
                    if(officialsUrlArray!=null)
                    {
                        OfficialsUrl = officialsUrlArray.length() > 0 ? officialsUrlArray.optString(0) : noDataProvided;
                    }


                    JSONArray officialsEmailArray = officialsObject.optJSONArray("emails");
                    String OfficialsEmail = noDataProvided;
                    if(officialsEmailArray!=null) {
                        OfficialsEmail = officialsEmailArray.optString(0,noDataProvided);
                    }

                    String OfficialsPhotoUrl = officialsObject.optString("photoUrl",noDataProvided);

                    String OfficialsGoogleChannel = noDataProvided;
                    String OfficialsFacebookChannel = noDataProvided;
                    String OfficialsTwitterChannel = noDataProvided;
                    String OfficialsYoutubeChannel = noDataProvided;
                    JSONArray officialsChannels=officialsObject.optJSONArray("channels");
                    if(officialsChannels!=null)
                    {
                        for(int l=0;l<officialsChannels.length();l++) {
                             JSONObject tempJObject= officialsChannels.optJSONObject(l);
                            if(tempJObject!=null)
                            {
                                String type= tempJObject.optString("type", noDataProvided);
                                JSONObject tempJObject2= officialsChannels.optJSONObject(l);
                                if(tempJObject2!=null)
                                {
                                    String id = tempJObject2.optString("id", noDataProvided);
                                    switch(type)
                                    {
                                        case "GooglePlus": OfficialsGoogleChannel = id;
                                            break;
                                        case "Facebook": OfficialsFacebookChannel = id;
                                            break;
                                        case "Twitter": OfficialsTwitterChannel = id;
                                            break;
                                        case "YouTube": OfficialsYoutubeChannel = id;
                                            break;
                                        default : break;
                                    }
                                }

                            }

                        }
                    }


                    Official tempOfficial = new Official(OfficialsName, OfficialsAddress, OfficialsParty, OfficialsPhone, OfficialsUrl,
                            OfficialsPhotoUrl, OfficeTitle, OfficialsEmail, OfficialsGoogleChannel,
                            OfficialsYoutubeChannel, OfficialsTwitterChannel, OfficialsFacebookChannel);

                    officialList.add(tempOfficial);

                }
            }
        } catch (JSONException e) {
            //Toast.makeText(myMainActivity,"unable to load data for this location",Toast.LENGTH_SHORT);
            e.printStackTrace();
        }
        return officialList;
    }
}
