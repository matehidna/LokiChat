package com.example.rtkr.lokichat;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

// [START blog_user_class]
@IgnoreExtraProperties
public class User {

    public String UserName;
    public String UserMotto;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String UserName, String motto) {
        this.UserName = UserName;
        this.UserMotto = motto;
    }
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("UserName", UserName);
        result.put("UserMotto", UserMotto);
        return result;
    }
}
