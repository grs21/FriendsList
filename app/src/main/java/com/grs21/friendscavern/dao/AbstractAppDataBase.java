package com.grs21.friendscavern.dao;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.grs21.friendscavern.model.User;

@Database(entities = {User.class},version = 3,exportSchema =false )
public abstract class AbstractAppDataBase extends RoomDatabase  {


        public static  String DATABASE_NAME="sqLite";
        private static  AbstractAppDataBase instance;



        public static synchronized AbstractAppDataBase getInstance(Context context){
                //When dataBase is null
                //initialize dataBase
                if (instance==null) {
                        instance = Room.databaseBuilder(context.getApplicationContext()
                                ,AbstractAppDataBase.class, DATABASE_NAME).allowMainThreadQueries()
                                .build();
                }
                return instance;
        }

        public abstract  UserDao userDao();


     static final Migration MIGRATION_2_3=new Migration(2,3) {
         @Override
         public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE 't_user' ADD COLUMN 'image' BLOB ");
         }
     };


}
