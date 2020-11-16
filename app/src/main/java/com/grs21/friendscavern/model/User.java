package com.grs21.friendscavern.model;

    import androidx.annotation.NonNull;
    import androidx.room.ColumnInfo;
    import androidx.room.Entity;
    import androidx.room.Index;
    import androidx.room.PrimaryKey;

    import java.sql.Blob;
    import java.util.Arrays;


/**
     * Data class that captures user information for logged in users retrieved from LoginRepository
     */
    @Entity(tableName = "t_user",indices = {
            @Index(value ={"user_id"},unique = true),
            @Index(value = {"user_name"},unique =true)
    })
    public class User {

        private static User user;

        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "user_id")
        private int userId;
        @NonNull
        @ColumnInfo(name = "user_name")
        private String userName;
        @NonNull
        @ColumnInfo(name = "password")
        private String password;
        @ColumnInfo(name = "first_name")
        private String firstName;
        @ColumnInfo(name = "last_name")
        private String lastName;
        @ColumnInfo(name ="album_URL")
        private String albumURL;
        @ColumnInfo(name="twitter_URL")
        private String twitterURL;
        @ColumnInfo(name = "phone")
        private String phone;
        @ColumnInfo(name = "image",typeAffinity =ColumnInfo.BLOB)
        public byte [] image;



        public byte[] getImage() {
        return image;
    }

        public void setImage(byte[] image) {
        this.image = image;
    }

        public String getPhone() {
          return phone;
       }

        public void setPhone(String phone) {
        this.phone = phone;
    }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getAlbumURL() {
            return albumURL;
        }

        public void setAlbumURL(String albumURL) {
            this.albumURL = albumURL;
        }

        public String getTwitterURL() {
            return twitterURL;
        }

        public void setTwitterURL(String twitterURL) {
            this.twitterURL = twitterURL;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getFirstName() {
            return firstName;
        }

        public String getPassword() {
            return password;
        }

        public String getUserName() {
            return userName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", albumURL='" + albumURL + '\'' +
                ", twitterURL='" + twitterURL + '\'' +
                ", phone='" + phone + '\'' +
                ", image=" + Arrays.toString(image) +
                '}';
    }

    public static User getInstance(){
        if (user==null){

            user=new User();
        }

        return user;
    }
}