package com.example.upload_retrive_videofirebase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.firebase.auth.FirebaseAuth;

public class PlayVideo extends AppCompatActivity {

    VideoView videoView;
    MediaController mediaController;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video);

        Intent intent = getIntent();
        Uri uri = Uri.parse(intent.getStringExtra("url"));
        mediaController= new MediaController(this);

        Toast.makeText(this, ""+uri, Toast.LENGTH_SHORT).show();

        firebaseAuth=FirebaseAuth.getInstance();

        videoView = findViewById(R.id.videoView);
        videoView.setVideoURI(uri);
        videoView.setMediaController(mediaController);
        mediaController.setAnchorView(videoView);
        videoView.start();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.logout){
            // do something
            firebaseAuth.signOut();
            Intent intent = new Intent(PlayVideo.this,PhoneAuthentication.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}