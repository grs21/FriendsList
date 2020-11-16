package com.grs21.friendscavern.activity;



import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.grs21.friendscavern.R;
import com.grs21.friendscavern.dao.AbstractAppDataBase;
import com.grs21.friendscavern.model.User;


public class LoginActivity extends AppCompatActivity {
    private User user=User.getInstance();
    private EditText editTextEmail,editTextPassword;
    private TextView textViewNewAccount;
    private Button buttonLogin;
    private Intent intent;
    private CheckBox checkBox;
    private static final String TAG = "LoginActivity";
    public  static final String CHECKBOX_SP_KEY="userCheckBoxKey";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private boolean isLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initializeComponent();

        sharedPreferences=getSharedPreferences(CHECKBOX_SP_KEY,Context.MODE_PRIVATE);
        checkBox.setChecked(sharedPreferences.getBoolean("checkBox",false));
        ifCheckBoxChecked(checkBox.isChecked());
        listener();
        Log.d(TAG, "onCreate:"+AbstractAppDataBase.getInstance(this).userDao().getAllFistNameLastName());
    }

    public void ifCheckBoxChecked(boolean checkBoxControl){
        if (checkBoxControl){
            editTextEmail.setText(sharedPreferences.getString("userName",""));
            editTextPassword.setText(sharedPreferences.getString("password",""));
            if (checkLogin() && sharedPreferences.getBoolean("isClickLoginButton",false) ){
                intent=new Intent(LoginActivity.this,FriendsProfileActivity.class);
                intent.putExtra(FriendsProfileActivity.INTENT_KEY_USER_ID,user.getUserId());
                startActivity(intent);
                finish();
            }
        }
    }

    public  boolean checkLogin(){

        try {
            String userName = editTextEmail.getText().toString();
            user=AbstractAppDataBase.getInstance(getApplicationContext()).userDao().findByUserName(userName.trim());

            if (!user.getUserName().isEmpty()){

                if (user.getPassword().equals(editTextPassword.getText().toString().trim())){
                    isLogin=true;

                }//PASSWORD CONTROL
                else Toast.makeText(this, "Check Your Password", Toast.LENGTH_SHORT).show();
            }//LOGIN CONTROL
            else {
                Toast.makeText(getApplicationContext(), "Check your User Name"
                        , Toast.LENGTH_SHORT).show();
                isLogin = false;
            }
        }
        catch (NullPointerException e){
            e.getMessage();
            Toast.makeText(LoginActivity.this,"CHECK your USER NAME"
                    ,Toast.LENGTH_LONG).show();
            isLogin=false;
        }
        return  isLogin;
    }

    public void listener(){

            buttonLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkLogin()){
                        if (checkBox.isChecked()){

                          editor=sharedPreferences.edit();
                          editor.putString("userName",editTextEmail.getText().toString().trim());
                          editor.putString("password",editTextPassword.getText().toString().trim());
                          editor.putBoolean("isClickLoginButton",true);
                          editor.apply();
                        }
                        intent=new Intent(LoginActivity.this,FriendsProfileActivity.class);
                        intent.putExtra(FriendsProfileActivity.INTENT_KEY_USER_ID,user.getUserId());
                        startActivity(intent);
                        finish();
                    }
                }
            });

            textViewNewAccount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    intent=new Intent(getApplicationContext(),RegisterActivity.class);
                    startActivity(intent);
                }
            });

            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                        editor = sharedPreferences.edit();

                    if (buttonView.isChecked()) {
                        //TODO:Put Something
                        editor.putString("userName",editTextEmail.getText().toString());
                        editor.putString("password",editTextPassword.getText().toString());
                        editor.putBoolean("checkBox",isChecked);
                        editor.apply();
                    }else if(!buttonView.isChecked()){
                        editor.putBoolean("checkBox",isChecked);
                        editor.apply();
                        Log.d(TAG, "onCheckedChanged: "+buttonView.isChecked());
                    }
                }
            });
    }

    public void initializeComponent(){
        editTextEmail=findViewById(R.id.editTextTextEmailAddress);
        editTextPassword=findViewById(R.id.editTextTextPassword);
        buttonLogin=findViewById(R.id.buttonLogin);
        textViewNewAccount=findViewById(R.id.textViewNewAccount);
        checkBox=findViewById(R.id.checkBox);
    }
}