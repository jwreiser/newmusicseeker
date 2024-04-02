package com.goodforallcode.newmusicseeker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.security.NetworkSecurityPolicy;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.goodforallcode.newmusicseeker.util.RESTCallingTask;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";


    TextView artist,album,track;
    ImageView cover;
    ImageButton likeArtist,dislikeArtist,likeAlbum,likeSong,dislikeSong;
    ImageButton loadArtist,loadAlbum,moreSongs,loadPlaylist,comedy;
    ImageButton basisArtists,basisSongs,basisAudioFeatures;
    ImageButton previous,pause_play,next;

    private void handleClick(View view,String url){
        new RESTCallingTask(artist, album, track, cover).execute(url);
    }

    private void handlePausePlay(View view){
        Log.i(TAG, "Inside pause/play");
        ImageButton button=(ImageButton)view;
        if(button.getTooltipText().equals("Pause")){
            button.setTooltipText("Play");
            button.setImageResource(R.drawable.play);
            new RESTCallingTask(artist, album, track, cover).execute("player/pause");
        }else{
            button.setTooltipText("Pause");
            button.setImageResource(R.drawable.mute);
            new RESTCallingTask(artist, album, track, cover).execute("player/play");
        }


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.logo);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setElevation(0);

        setContentView(R.layout.activity_main);

        likeArtist = findViewById(R.id.likeArtist);
        dislikeArtist = findViewById(R.id.dislikeArtist);
        likeAlbum = findViewById(R.id.likeAlbum);
        likeSong = findViewById(R.id.likeSong);
        dislikeSong = findViewById(R.id.dislikeSong);
        moreSongs = findViewById(R.id.moreSongs);

        loadAlbum = findViewById(R.id.loadAlbum);
        loadArtist = findViewById(R.id.loadArtist);
        loadPlaylist = findViewById(R.id.loadPlaylist);

        comedy = findViewById(R.id.comedy);
        artist = findViewById(R.id.artist);
        album = findViewById(R.id.album);
        track = findViewById(R.id.track);
        cover = findViewById(R.id.cover);
        basisArtists = findViewById(R.id.basisArtists);
        basisSongs = findViewById(R.id.basisSongs);
        basisAudioFeatures = findViewById(R.id.basisAudioFeatures);

        previous = findViewById(R.id.previous);
        pause_play = findViewById(R.id.pause_play);
        next = findViewById(R.id.next);

        //row 1
        likeArtist.setOnClickListener(v->handleClick(v,"artist/like"));
        dislikeArtist.setOnClickListener(v->handleClick(v,"artist/dislike"));
        likeAlbum.setOnClickListener(v->handleClick(v,"album/like"));
        loadAlbum.setOnClickListener(v->handleClick(v,"load/album"));

        //row 2
        likeSong.setOnClickListener(v->handleClick(v,"song/like"));
        dislikeSong.setOnClickListener(v->handleClick(v,"song/dislike"));
        loadArtist.setOnClickListener(v->handleClick(v,"load/artist"));
        moreSongs.setOnClickListener(v->handleClick(v,"load/songs"));

        //row 3
        basisArtists.setOnClickListener(v->handleClick(v,"basis/artists"));
        basisSongs.setOnClickListener(v->handleClick(v,"basis/songs"));
        basisAudioFeatures.setOnClickListener(v->handleClick(v,"basis/features"));
        loadPlaylist.setOnClickListener(v->handleClick(v,"load/playlist"));

        //row 4
        comedy.setOnClickListener(v->handleClick(v,"load/comedy"));
        previous.setOnClickListener(v->handleClick(v,"player/previous"));
        pause_play.setOnClickListener(v->handlePausePlay(v));
        next.setOnClickListener(v->handleClick(v,"player/next"));
    }
}