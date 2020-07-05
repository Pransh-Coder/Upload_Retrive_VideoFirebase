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
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class MainActivity extends AppCompatActivity {

    Button recordbtn,uploadbtn;
    StorageReference storageRefrence;
    Uri uri;
    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();
        recordbtn = findViewById(R.id.record);
        uploadbtn = findViewById(R.id.upload);
        progressDialog =new ProgressDialog(this);
        progressDialog.setMessage("Please Wait....");
        progressDialog.setCancelable(false);
        progressBar = (ProgressBar) findViewById(R.id.bar);
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
                progressBar.setVisibility(View.VISIBLE);
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
                            });/*.addOnProgressListener(
                            new OnProgressListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                                    updateProgress(taskSnapshot);
                                }
                            });*/
                }
                else {
                    Toast.makeText(MainActivity.this, "Nothing to upload",
                            Toast.LENGTH_LONG).show();
                }
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
            storageRefrence = storageRef.child("/videos/" + firebaseAuth.getCurrentUser() + "/userIntro2.3gp");

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
}