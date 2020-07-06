package com.example.upload_retrive_videofirebase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class MainActivity extends AppCompatActivity {

    Button recordbtn,uploadbtn,showdata;

    StorageReference storageRefrence;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    ProgressBar progressBar;

    Member member;

    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();
        recordbtn = findViewById(R.id.record);
        uploadbtn = findViewById(R.id.upload);
        progressBar = (ProgressBar) findViewById(R.id.bar);
        showdata = findViewById(R.id.showData);
        member = new Member();
        storageRefrence = FirebaseStorage.getInstance().getReference("Video");
        databaseReference = FirebaseDatabase.getInstance().getReference("video");
        //String uid = firebaseAuth.getCurrentUser().getUid();

     /*   if (firebaseAuth.getCurrentUser()!=null){
            StorageReference storageRef =
                    FirebaseStorage.getInstance().getReference();
            storageRefrence = storageRef.child("/videos/" + firebaseAuth.getCurrentUser().getUid() + "/userIntro.3gp");
        }*/

        recordbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT,10);
                startActivityForResult(intent,1);

            }
        });

        uploadbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UploadVideo();
                /*progressBar.setVisibility(View.VISIBLE);
                //progressBar.setProgress((int) progress);
                if (uri != null) {
                    UploadTask uploadTask = storageRefrence.putFile(uri);

                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MainActivity.this,
                                    "Upload failed: " + e.getLocalizedMessage(),
                                    Toast.LENGTH_LONG).show();

                        }
                    }).addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    Toast.makeText(MainActivity.this, "Upload complete",
                                            Toast.LENGTH_LONG).show();
                                    Log.e("Upload complete",taskSnapshot.toString());
                                    progressBar.setVisibility(View.INVISIBLE);
                                }
                            });*//*.addOnProgressListener(
                            new OnProgressListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                                    updateProgress(taskSnapshot);
                                }
                            });*//*
                }
                else {
                    Toast.makeText(MainActivity.this, "Nothing to upload",
                            Toast.LENGTH_LONG).show();
                }*/
            }
        });
        showdata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,ShowData.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode==RESULT_OK && requestCode==1 && data!=null){

            uri = data.getData();
            Toast.makeText(this, ""+uri, Toast.LENGTH_SHORT).show();

            StorageReference storageRef =
                    FirebaseStorage.getInstance().getReference();
            storageRefrence = storageRef.child("/videos/" + firebaseAuth.getCurrentUser().getUid() + "/userIntro.3gp");

        }
    }
    public void updateProgress(UploadTask.TaskSnapshot taskSnapshot) {

        @SuppressWarnings("VisibleForTests") long fileSize =
                taskSnapshot.getTotalByteCount();

        @SuppressWarnings("VisibleForTests")
        long uploadBytes = taskSnapshot.getBytesTransferred();

        long progress = (100 * uploadBytes) / fileSize;

        //progressBar = (ProgressBar) findViewById(R.id.bar);
        //progressBar.setProgress((int) progress);
    }

    //Better way to Upload video as we are pushing keys in database as well
    public void UploadVideo(){
        final String vidname = "t2";
        if (uri != null&&vidname!=null){
            progressBar.setVisibility(View.VISIBLE);
            StorageReference reference = storageRefrence.child(System.currentTimeMillis()+vidname);
            UploadTask uploadTask2 = reference.putFile(uri);

            uploadTask2.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MainActivity.this,
                            "Upload failed: " + e.getLocalizedMessage(),
                            Toast.LENGTH_LONG).show();

                }
            }).addOnSuccessListener(
                    new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(MainActivity.this, "Upload complete",
                                    Toast.LENGTH_LONG).show();
                            Log.e("Upload complete",taskSnapshot.toString());
                            progressBar.setVisibility(View.INVISIBLE);

                            member.setName(vidname);
                            member.setVideoUri(uri.toString());
                            String i = databaseReference.push().getKey();
                            databaseReference.child(i).setValue(member);
                        }
                    });
        }
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
            Intent intent = new Intent(MainActivity.this,PhoneAuthentication.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}