package com.example.rtkr.lokichat;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;
@IgnoreExtraProperties
public class ChatMessages {
        public String userId;
        public String userName;
        public String body;
        public String time;

    ChatMessages(){
        // Default constructor required for calls to DataSnapshot.getValue(T.class)
    }

        ChatMessages(String userId, String userName, String body, String time){
            this.body = body;
            this.time = time;
            this.userId = userId;
            this.userName = userName;
        }

        @Exclude
        public Map<String, Object> toMap() {
            HashMap<String, Object> result = new HashMap<>();
            result.put("userId", userId);
            result.put("userName", userName);
            result.put("body", body);
            result.put("time", time);
            return result;
        }
    }

