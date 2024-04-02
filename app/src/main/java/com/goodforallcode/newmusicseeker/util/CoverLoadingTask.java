package com.goodforallcode.newmusicseeker.util;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.goodforallcode.newmusicseeker.model.RESTCallResult;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class CoverLoadingTask extends AsyncTask<String, Void,Drawable> {
    private static final String TAG = "CoverLoadingTask";

    private static int NUM_RETRIES=1;
    ImageView cover;

    public CoverLoadingTask(ImageView cover) {
        this.cover = cover;
    }

    public Drawable doInBackground(String... urls){
        Drawable drawable=null;
        try ( InputStream is = (InputStream) new URL(urls[0]).getContent()){
            Log.i("Cover url: ",urls[0]);
            drawable= Drawable.createFromStream(is, "src name");
        } catch (Exception e) {
            return null;
        }
        return drawable;
    }
    protected void onPostExecute (Drawable result){
        if(result!=null) {
            cover.setMinimumHeight(500);
            cover.setMinimumWidth(500);
            cover.setMaxHeight(500);
            cover.setMaxWidth(500);
            cover.setAdjustViewBounds(false);
            cover.setImageDrawable(result);

        }else{
            cover.setImageResource(android.R.color.transparent);
        }
    }


}
