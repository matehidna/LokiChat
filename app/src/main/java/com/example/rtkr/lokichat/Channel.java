package com.example.rtkr.lokichat;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Channel {
    public String channelName;
    public String channelOwner;
    public int channelStatus;
    public Channel(){};
    public Channel(String channelName, String channelOwner, int channelStatus){
        this.channelName = channelName;
        this.channelOwner = channelOwner;
        this.channelStatus = channelStatus;
    }
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("channelName", channelName);
        result.put("channelOwner", channelOwner);
        result.put("channelStatus", channelStatus);
        return result;
    }
}
