package com.grs21.friendscavern.activity;

import android.Manifest;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.grs21.friendscavern.R;
import com.grs21.friendscavern.dao.AbstractAppDataBase;
import com.grs21.friendscavern.model.User;

public class FriendsProfileActivity extends AppCompatActivity {

    private ImageView imageViewProfile;
    private TextView textViewFirstName,textViewLastName,textViewPhone,textViewAlbum,textViewTwitter;
    private Button buttonProfileEdit,buttonSearchFriends,buttonLogout;
    private Intent intent;
    private User user=User.getInstance();
    public static final String INTENT_KEY_USER_ID="userId";
    private static int userId;
    private static final String TAG = "FriendsProfileActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_profile);

        initializeComponent();
        listener();
        intent=getIntent();
        userId= intent.getIntExtra(INTENT_KEY_USER_ID,0);
        user=AbstractAppDataBase.getInstance(getApplicationContext()).userDao().findById(userId);
        initializeSetValueComponent();

    }

    public void initializeSetValueComponent(){
        textViewFirstName.setText(user.getFirstName());
        textViewLastName.setText(user.getLastName());
        textViewAlbum.setText(user.getAlbumURL());
        textViewTwitter.setPaintFlags(textViewTwitter.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
        textViewTwitter.setText(user.getTwitterURL());
        textViewAlbum.setPaintFlags(textViewAlbum.getPaintFlags()|Paint.UNDERLINE_TEXT_FLAG);
        textViewPhone.setText(user.getPhone());

        if (user.getImage()!=null) {
            byte[] bytes=user.getImage();
            Bitmap bitmap= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
            imageViewProfile.setImageBitmap(bitmap);
        }else imageViewProfile.setImageResource(R.drawable.aang);

        Log.d(TAG, "onCreate:came:"+user.getUserName());

    }

    public void listener(){

        buttonSearchFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                intent=new Intent(getApplicationContext(), FriendsListActivity.class);
                startActivity(intent);
            }
        });

        textViewPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO:Check permission for phone
                if (ContextCompat.checkSelfPermission(FriendsProfileActivity.this
                        , Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(FriendsProfileActivity.this
                            ,new String[]{Manifest.permission.CALL_PHONE},1);
                }else{
                    Intent intentToPhone=new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+user.getPhone()));
                    startActivityForResult(intentToPhone,2);
                }
            }
        });
        buttonProfileEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent=new Intent(getApplicationContext(),UserProfileEditActivity.class);
                intent.putExtra(INTENT_KEY_USER_ID,userId);
                Log.d(TAG, "onClick: "+userId);
                startActivity(intent);
            }
        });

        textViewAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intentToAlbum=new Intent(Intent.ACTION_WEB_SEARCH);
                intentToAlbum.putExtra(SearchManager.QUERY,textViewAlbum.getText().toString());

                if (intentToAlbum.resolveActivity(getPackageManager())!=null){
                    startActivity(intentToAlbum);
                }

            }
        });
        textViewTwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentToTwitter=new Intent(Intent.ACTION_WEB_SEARCH);
                intentToTwitter.putExtra(SearchManager.QUERY,textViewTwitter.getText().toString());
                if (intentToTwitter.resolveActivity(getPackageManager())!=null){
                    startActivity(intentToTwitter);
                }
            }
        });
        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences sharedPreferences=getSharedPreferences(LoginActivity.CHECKBOX_SP_KEY
                        , Context.MODE_PRIVATE);

                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putBoolean("checkBox",false);
                editor.putBoolean("isClickLoginButton",false);
                editor.apply();
                Intent intent=new Intent(FriendsProfileActivity.this,LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode==1){
            Intent intentToPhone=new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+user.getPhone()));
            startActivityForResult(intentToPhone,2);}
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void initializeComponent(){

        imageViewProfile=findViewById(R.id.imageViewProfile);

        textViewFirstName=findViewById(R.id.textViewFirstName);
        textViewLastName=findViewById(R.id.textViewLastName);
        textViewPhone=findViewById(R.id.textViewPhone);
        textViewAlbum=findViewById(R.id.textViewAlbum);
        textViewTwitter=findViewById(R.id.textViewTwitter);

        buttonSearchFriends=findViewById(R.id.buttonSearchFriends);
        buttonProfileEdit=findViewById(R.id.buttonEditProfile);

        buttonLogout=findViewById(R.id.buttonProfilelogoutttt);

    }



}