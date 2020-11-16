package com.grs21.friendscavern.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.grs21.friendscavern.R;
import com.grs21.friendscavern.dao.AbstractAppDataBase;
import com.grs21.friendscavern.model.User;
import java.util.ArrayList;
import java.util.List;

public class FriendsListActivity extends AppCompatActivity {
private ArrayList<String> firstNameArrayList=new ArrayList<>();
private ArrayList<String>  lastNameArrayList=new ArrayList<>();
private ArrayList<String> fullNameArrayList=new ArrayList<>();
private ArrayAdapter userArrayAdapter;
private List<User> userList;
    private static final String TAG = "FriendsActivity";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_list);
        initializeComponent();
    }

    public void initializeComponent(){
         ListView listView=findViewById(R.id.friendsListView);
         userList=AbstractAppDataBase.getInstance(getApplicationContext()).userDao().getAllFistNameLastName();
         for (int i = 0;i<=userList.size()-1;i++){

              firstNameArrayList.add(i,userList.get(i).getFirstName());
              lastNameArrayList.add(i,userList.get(i).getLastName());
              fullNameArrayList.add(i,firstNameArrayList.get(i)+" "+lastNameArrayList.get(i));

              userArrayAdapter=new ArrayAdapter(FriendsListActivity.this,android.R.layout.simple_list_item_1
                     ,fullNameArrayList);
              listView.setAdapter(userArrayAdapter);
         }
         //Todo: Listener
         listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
             @Override
             public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                 int userId=userList.get((int)id).getUserId();

                 Intent intent=new Intent(getApplicationContext(),FriendsProfileActivity.class);
                 intent.putExtra(FriendsProfileActivity.INTENT_KEY_USER_ID,userId);
                 startActivity(intent);
             }
         });

    }
}
