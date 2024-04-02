package com.goodforallcode.newmusicseeker.util;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.goodforallcode.newmusicseeker.model.RESTCallResult;

public class RESTCallingTask extends AsyncTask<String, Void,RESTCallResult> {
    private static final String TAG = "RestCallingTask";
    private static int MAX_STRING=23;
    private static int NUM_RETRIES=1;
    RESTCallResult callResult=null;
    TextView artist;
    TextView album;
    TextView track;
    ImageView cover;

    public RESTCallingTask(TextView artist, TextView album, TextView track, ImageView cover) {
        this.artist = artist;
        this.album = album;
        this.track = track;
        this.cover = cover;
    }

    public RESTCallResult doInBackground(String... urls){
        return callUrl("http://goodforallcode.pythonanywhere.com/"+urls[0],0);
    }
    public RESTCallResult callUrl(String urlString, int trialNumber){
        Log.i(TAG, "Inside callUrl");
        RESTCallResult callResult=null;
        URL url =null;
        try {
             url = new URL(urlString);
        }catch (MalformedURLException mfe){

        }
        HttpURLConnection connection = null;
        if(url!=null) {
            try  {
                connection = (HttpURLConnection) url.openConnection();
                System.err.println("Calling " + urlString);

                connection.setRequestProperty("Accept", "application/json");
                connection.setRequestMethod("GET");

                // Collect the response code
                Log.i(TAG, "Calling");
                int responseCode = connection.getResponseCode();
                Log.i(TAG, "RETURNED FROM CALL " + urlString);
                String coverUrl = null;

                if (responseCode == connection.HTTP_OK) {
                    // Create a reader with the input stream reader.
                    try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));) {

                        String inputLine;

                        // Create a string buffer
                        StringBuffer response = new StringBuffer();

                        // Write each of the input line
                        while ((inputLine = in.readLine()) != null) {
                            response.append(inputLine);
                        }

                        String json = response.toString();
                        Log.i(TAG, "JSON:" + json);
                        String[] parts = json.split("\",");

                        String artist = parts[0].split(":")[1].replaceAll("\"", "");
                        String albumName = "";
                        if (parts.length > 1) {
                            String[] albumParts = parts[1].split("\":");
                            if (albumParts.length > 1) {
                                albumName = albumParts[1].replaceAll("\"", "");
                            }
                        }

                        String track = null;
                        if (parts.length > 2) {
                            String[] trackParts = parts[2].split(":");
                            if (trackParts.length > 1) {
                                track = trackParts[1].replaceAll("\"", "");
                            } else {
                                Log.i(TAG, "No track value " + parts[2]);
                            }

                            if (parts.length > 3) {
                                String coverAttrVal = parts[3];
                                int start = coverAttrVal.indexOf("http");
                                if (start > 0) {
                                    coverUrl = coverAttrVal.substring(start, coverAttrVal.length() - 2);
                                }
                            }
                        } else {
                            Log.i(TAG, "No track " + json);
                        }

                        callResult = new RESTCallResult(artist, albumName, track, coverUrl);
                    }
                } else if (trialNumber < NUM_RETRIES) {
                    try {
                        Thread.sleep(3_000);
                    } catch (InterruptedException e) {

                    }
                    return callUrl(urlString, ++trialNumber);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }finally {
                if(connection!=null) {
                    connection.disconnect();
                }
            }
        }
        return callResult;
    }
    protected void onPostExecute (RESTCallResult result){
        if(result!=null) {
            artist.setText("Artist: " + cleanupUnicodeEtc(result.getArtist()));
            String trackString = result.getTrack();
            if (trackString!=null) {
                trackString = cleanupUnicodeEtc(trackString);
                if(trackString.length() > MAX_STRING){
                    trackString = trackString.substring(0, MAX_STRING - 3)+ "...";
                }
                track.setText("Track: " + trackString);
            }else{
                track.setText("Track: ");
            }

            String albumString = cleanupUnicodeEtc(result.getAlbum());
            if (albumString.length() > MAX_STRING) {
                albumString = albumString.substring(0, MAX_STRING - 3)+ "...";
            }
            album.setText("Album: " + albumString);

            if (result.getCover() != null) {
                new CoverLoadingTask(cover).execute(result.getCover());
            } else {
                cover.setImageResource(android.R.color.transparent);
            }
        }else{
            Log.i(TAG, "Result Next");
        }
    }
    private String cleanupUnicodeEtc(String value){
        return value.replaceAll("\u2019","").replaceAll("\\\\","\"");
    }

}
