package com.grs21.friendscavern.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.grs21.friendscavern.R;
import com.grs21.friendscavern.dao.AbstractAppDataBase;
import com.grs21.friendscavern.model.User;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class UserProfileEditActivity extends AppCompatActivity {
    private EditText editTextFirstName,editTextLastName,editTextPhone,editTextTwitter,editTextAlbum;
    private Button buttonDeleteProfile,buttonChangeImage,buttonSave,buttonToProfile;
    private ImageView imageViewImage;
    private static int userId;
    private  User user=User.getInstance();
    private  String userFirstName;
    private  String userLastName;
    private  String phone;
    private  String album;
    private  String twitter;
    private static final String TAG = "UserProfileEditActivity";
    private Bitmap selectedImage;
    private byte[] bitmapImageTOByteArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_proile_edit);

        Intent intent = getIntent();
        userId= intent.getIntExtra(FriendsProfileActivity.INTENT_KEY_USER_ID,0);
        user=AbstractAppDataBase.getInstance(getApplicationContext()).userDao().findById(userId);
        initializeComponent();
        initializeSetEditText();
        listener();

        Log.d(TAG, "onCreate:userIDUserEdit:"+userId);
    }

    public void initializeSetEditText(){
        editTextFirstName.setText(user.getFirstName());
        editTextLastName.setText(user.getLastName());
        editTextPhone.setText(user.getPhone());
        editTextAlbum.setText(user.getAlbumURL());
        editTextTwitter.setText(user.getTwitterURL());

       if (user.getImage()!=null){
        byte [] bytImage=user.getImage();
        Bitmap bitmap= BitmapFactory.decodeByteArray(bytImage,0,bytImage.length);
        imageViewImage.setImageBitmap(bitmap);
       }else imageViewImage.setImageResource(R.drawable.aang);
    }

    public void currentValueInEditText(){
        userFirstName=editTextFirstName.getText().toString().trim();
        userLastName=editTextLastName.getText().toString().trim();
        phone=editTextPhone.getText().toString().trim();
        album=editTextAlbum.getText().toString().trim();
        twitter=editTextTwitter.getText().toString().trim();
    }

    public Boolean controller(String column,String getDbColumn){

        return (!column.trim().equals(getDbColumn) && !column.isEmpty());
    }

    public void listener(){

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentValueInEditText();
                if(selectedImage!=null){
                    imageConvertType();

                    //TODO:imageResource save DB
                    AbstractAppDataBase.getInstance(getApplicationContext()).userDao().updateImage(userId, bitmapImageTOByteArray);
                }

             if (controller(userFirstName,user.getFirstName()))
             {
                 AbstractAppDataBase.getInstance(getApplicationContext()).userDao()
                         .updateFirstName(userId,userFirstName);

             }

             if (controller(userLastName,user.getLastName())){
                    AbstractAppDataBase.getInstance(getApplicationContext()).userDao()
                            .updateLastName(userId,userLastName);
             }

             if (controller(phone,user.getPhone())){
                    AbstractAppDataBase.getInstance(getApplicationContext()).userDao()
                            .updatePhone(userId,phone);
             }

             if (controller(album,user.getAlbumURL())){
                    AbstractAppDataBase.getInstance(getApplicationContext()).userDao()
                            .updateAlbum(userId,album);
             }

             if (controller(twitter,user.getTwitterURL())){
                    AbstractAppDataBase.getInstance(getApplicationContext()).userDao()
                            .updateTwitter(userId,twitter);
             }

                Toast.makeText(UserProfileEditActivity.this,"SAVED",Toast.LENGTH_SHORT).show();



            }
        });


        buttonChangeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //TODO: Check permission
                if (ContextCompat.checkSelfPermission(UserProfileEditActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(UserProfileEditActivity.this
                            ,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
                }else {
                    Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent,2);
                }
            }
        });

        buttonDeleteProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AbstractAppDataBase.getInstance(getApplicationContext()).userDao().deleteById(userId);
                Intent intent=new Intent(UserProfileEditActivity.this,FriendsListActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK );
                startActivity(intent);
                Log.d(TAG, "onClick: "+AbstractAppDataBase.getInstance(getApplicationContext()).userDao().getAllFistNameLastName());
               // finish();
            }
        });

        buttonToProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent= new Intent(getApplicationContext(),FriendsProfileActivity.class);
               intent.putExtra(FriendsProfileActivity.INTENT_KEY_USER_ID,userId);

               startActivity(intent);
            }
        });
    }

    //TODO:Transactions in the gallery
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //TODO:number one permission
        if (requestCode==1 )
        {
            if (grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Intent intentToGallery=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intentToGallery,2);
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode==2 && resultCode==RESULT_OK && data!=null){

            Uri imageDataURI=data.getData();

            try {
                if(Build.VERSION.SDK_INT>= 28){
                    ImageDecoder.Source source=ImageDecoder.createSource(this.getContentResolver(),imageDataURI);
                    selectedImage=ImageDecoder.decodeBitmap(source);
                    //imageViewImage.setImageBitmap(selectedImage);

                }else {
                    selectedImage=MediaStore.Images.Media.getBitmap(UserProfileEditActivity.this.getContentResolver(),imageDataURI);

                }
                imageViewImage.setImageBitmap(selectedImage);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public void imageConvertType(){

              Bitmap bitmapSmallImage = makeSmaller(selectedImage, 371);

              ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
              bitmapSmallImage.compress(Bitmap.CompressFormat.JPEG, 50, outputStream);
              bitmapImageTOByteArray = outputStream.toByteArray();
    }

    //ToDo:Make Small image size
     public Bitmap makeSmaller(Bitmap image,int maxSize){

               int with=image.getWidth();
               int height=image.getHeight();
               float bitmapImageReelRate=(float)with/height;

               if (bitmapImageReelRate>1){
                   with=maxSize;
                   height= (int)(bitmapImageReelRate/with);
               }else{
                   height=maxSize;
                   with=(int)(height*bitmapImageReelRate);
               }

               return Bitmap.createScaledBitmap(image,with,height,true);
    }

    public void initializeComponent()
    {
        editTextFirstName=findViewById(R.id.editTextEditUserName);
        editTextLastName=findViewById(R.id.editTextEditLastName);
        editTextPhone=findViewById(R.id.editTextEditPhone);
        editTextTwitter=findViewById(R.id.editTextEditTwitter);
        editTextAlbum=findViewById(R.id.editTextEditAlbum);

        buttonChangeImage=findViewById(R.id.buttonEditChanceImage);
        buttonDeleteProfile=findViewById(R.id.buttonEditDeleteProfile);
        buttonSave=findViewById(R.id.buttonEditSave);
        buttonToProfile=findViewById(R.id.buttonEditToFriendsProfile);

        imageViewImage=findViewById(R.id.imageViewEditProfileName);
    }
}