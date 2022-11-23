package com.evelyne.labs.trialapp.fragments;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.evelyne.labs.trialapp.R;
import com.evelyne.labs.trialapp.ShowUploads;
import com.evelyne.labs.trialapp.model.Upload;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class UploadFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;
    private Button  chooseImage, Upload;
    private EditText company,capacity,plate,price;
    private ImageView imageView;
    private ProgressBar progressBar;
    private TextView showUpload;
    private FirebaseAuth auth;
    private FirebaseFirestore mstore;
    private Uri imageUri;
    private StorageReference mStorage;
    private DatabaseReference mReference;
    private StorageTask mUploadTask;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_upload,null);

        showUpload = (TextView) root.findViewById(R.id.showUploads);
        company = (EditText) root.findViewById(R.id.description);
        capacity = (EditText) root.findViewById(R.id.cap);
        plate = (EditText) root.findViewById(R.id.noplate);
        price = (EditText) root.findViewById(R.id.prices);
        Upload = (Button) root.findViewById(R.id.upload);
        chooseImage = (Button) root.findViewById(R.id.chooseFile);
        imageView = (ImageView) root.findViewById(R.id.imageview);
        progressBar = (ProgressBar) root.findViewById(R.id.progressBarUpload);
        mstore = FirebaseFirestore.getInstance();
        mStorage = FirebaseStorage.getInstance().getReference("uploads");
        mReference = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
        chooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mUploadTask != null && mUploadTask.isInProgress()) {
                    Toast.makeText(getContext(),"Upload in Progress", Toast.LENGTH_LONG).show();

                }else {
                    OpenImageChooser();
                }
            }
        });
        Upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uploadfile();
                progressBar.setVisibility(View.VISIBLE);

            }
        });
        showUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //load uploads
                Intent intent = new Intent(getActivity(), ShowUploads.class);
                startActivity(intent);
            }
        });

        return root;
    }
    private void OpenImageChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST || resultCode == RESULT_OK
         || data !=null || data.getData() != null){
            imageUri = data.getData();

            Picasso.with(chooseImage.getContext()).load(imageUri).into(imageView);
        }
    }
    private String getFileExt(Uri uri){

        ContentResolver cR = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }
    private void Uploadfile(){
        String timestamp = ""+System.currentTimeMillis();
//        if(imageUri !=null){
//            StorageReference filereference = mStorage.child(System.currentTimeMillis()
//                    +"."+getFileExt(imageUri));
        if(imageUri ==null){
            String filepathName = "profile_images/" + auth.getUid();

           StorageReference filereference = FirebaseStorage.getInstance().getReference(filepathName);

           filereference.putFile(imageUri)
//            mUploadTask=filereference.putFile(imageUri)
                   .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                       @Override
                       public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                            String Uid = pid.getText().toString().trim();
//                            String uid = ""+ds.getRef().getKey();

                           Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                           while (!uriTask.isSuccessful()) ;
                           Uri downloadImageUri = uriTask.getResult();
                           if (uriTask.isSuccessful()) {
                               HashMap<String, Object> hashMap = new HashMap<>();
                               hashMap.put("uploadId", "" + timestamp);
                               hashMap.put("timestamp", "" + timestamp);
//                               hashMap.put("imageUri", "" + filereference);
                               hashMap.put("uid", "" + auth.getUid());
//                               String Image = imageUri.toString();
                               String CName = company.getText().toString().trim();
                               String tCapacity = capacity.getText().toString().trim();
                               String tPlate = plate.getText().toString().trim();
                               String sPrice = price.getText().toString().trim();

                               HashMap uploadDetails = new HashMap();
                               // uploadDetails.put("uid", Uid);
                              // uploadDetails.put("imageUri", ""+downloadImageUri);
//                               uploadDetails.put("imageUri",Image );
                               uploadDetails.put("CompanyName", CName);
                               uploadDetails.put("TankCapacity", tCapacity);
                               uploadDetails.put("NumberPlate", tPlate);
                               uploadDetails.put("ServicePrice", sPrice);
                               uploadDetails.put("IsProvider", true);


                               //upload to firestore
                               mstore.collection("Services")
                                       .document(auth.getCurrentUser().getUid())
                                       .collection("my_services")
                                       .document().set(uploadDetails);


//                           Upload companyName = new Upload(company.getText().toString().trim()
//                           ,taskSnapshot.getUploadSessionUri().toString());
//                           Upload tankCapacity = new Upload(capacity.getText().toString().trim()
//                                   ,taskSnapshot.getUploadSessionUri().toString());
//                           Upload truckNumberPlate = new Upload(plate.getText().toString().trim()
//                                   ,taskSnapshot.getUploadSessionUri().toString());
//                           Upload servicePrice = new Upload(price.getText().toString().trim()
//                                   ,taskSnapshot.getUploadSessionUri().toString());
//                           String uploadId = mReference.push().getKey();
                               mReference.child("Providers").child(auth.getCurrentUser().getUid()).child("uploads").setValue(uploadDetails);
                               progressBar.setVisibility(View.GONE);
                               Toast.makeText(getContext(), "Upload Successful", Toast.LENGTH_LONG).show();
                           }
                       }

                   })
                   .addOnFailureListener(new OnFailureListener() {
                       @Override
                       public void onFailure(@NonNull Exception e) {
                           Toast.makeText(getContext(),e.getMessage(), Toast.LENGTH_LONG).show();
                       }
                   })
                   .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                       @Override
                       public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                           //gives the progress
                           double progress=(100.0 * snapshot.getBytesTransferred()/snapshot.getTotalByteCount());
                           //progressBar.getProgress((int)progress);
                       }
                   });

        }else{
            Toast.makeText(getContext(),"Upload File", Toast.LENGTH_LONG).show();
        }

    }
}