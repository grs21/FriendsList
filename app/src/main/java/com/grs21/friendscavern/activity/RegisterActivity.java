package com.grs21.friendscavern.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.grs21.friendscavern.R;
import com.grs21.friendscavern.dao.AbstractAppDataBase;
import com.grs21.friendscavern.model.User;



public class RegisterActivity extends AppCompatActivity {

    private EditText editTextUserName,editTextPassword;
    private Button buttonRegistration;
    private User user;
    private String userName,password;
    private static final String TAG = "RegisterActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initializeComponent();
        createNewAccountListener();
    }

    public void createNewAccountListener(){

        buttonRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getEditText();
                try {
                    if(!userName.isEmpty()  && !password.isEmpty()) {

                        AbstractAppDataBase.getInstance(getApplicationContext()).userDao().insertAll(createNewUser());
                        user=AbstractAppDataBase.getInstance(getApplicationContext()).userDao().findByUserName(userName);

                        Intent intent = new Intent(getApplicationContext(), UserProfileEditActivity.class);
                        intent.putExtra(FriendsProfileActivity.INTENT_KEY_USER_ID,user.getUserId());
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);

                        Log.d(TAG, "onClick:UserId:"+user.getUserId());

                        Toast.makeText(RegisterActivity.this,"OPERATION SUCCESSFUL",Toast.LENGTH_SHORT).show();
                        finish();
                    }else {
                        Toast.makeText(RegisterActivity.this, "PLEASE ENTER USERNAME PASSWORD!!", Toast.LENGTH_SHORT).show();
                    }
                }
                catch (SQLiteConstraintException e){
                    Toast.makeText(RegisterActivity.this,"ACCOUNT ALREADY EXIST",Toast.LENGTH_SHORT).show();
                }Log.d(TAG, "onClick: "+AbstractAppDataBase.getInstance(getApplicationContext()).userDao().getAll());
            }
        });

    }

    public User createNewUser(){
        user=new User();
        getEditText();
        user.setUserName(userName);
        user.setPassword(password);
        return user;
    }

    public void getEditText(){

        password=editTextPassword.getText().toString().trim();
        userName=editTextUserName.getText().toString().trim();
    }

    public void initializeComponent(){
        editTextPassword=findViewById(R.id.editTextRegisterPassword);
        editTextUserName=findViewById(R.id.editTextRegisterEmail);

        buttonRegistration=findViewById(R.id.buttonCreateNewAccount);
    }


}