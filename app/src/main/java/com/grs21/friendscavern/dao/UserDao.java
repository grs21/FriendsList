package com.grs21.friendscavern.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.grs21.friendscavern.model.User;
import java.util.ArrayList;
import java.util.List;

@Dao
public interface UserDao {

    String getAll="SELECT * FROM t_user";
    String FIND_BY_ID="SELECT * FROM t_user WHERE user_id LIKE :userId";
    String findByUserName="SELECT * FROM t_user WHERE user_name LIKE :userName";
    String firstName_LastName="SELECT user_id,user_name,password,first_name,last_name FROM t_user ";

    @Query(firstName_LastName)
    List<User> getAllFistNameLastName();

    @Query(findByUserName)
    User findByUserName(String userName);

    @Query(FIND_BY_ID)
    User findById(int userId);

    @Query(getAll)
    List<User> getAll();

    @Query("UPDATE t_user SET first_name =:firstName WHERE user_id=:id ")
    void updateFirstName(int id, String firstName);

    @Query("UPDATE t_user SET last_name =:lastName WHERE user_id=:id ")
    void updateLastName(int id,String lastName);

    @Query("UPDATE t_user SET phone=:phone WHERE user_id=:id ")
    void updatePhone(int id,String phone);

    @Query("UPDATE t_user SET album_URL =:albumURL WHERE user_id=:id ")
    void updateAlbum(int id,String albumURL);

    @Query("UPDATE t_user SET twitter_URL =:twitterURL WHERE user_id=:id ")
    void updateTwitter(int id, String twitterURL);

    @Query("UPDATE t_user SET image=:image WHERE user_id=:id ")
    int updateImage(int id,byte[] image);

    @Insert
    void insertAll (User user);

    @Query("DELETE FROM t_user WHERE user_id=:userId")
    void deleteById(int userId);



 }
