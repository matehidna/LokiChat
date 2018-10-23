package com.example.rtkr.lokichat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ShowChannelActivity extends AppCompatActivity {
    private Button btnCreateChannel;
    private String userId;
    private DatabaseReference mDatabase;
    private ArrayList<Channel> messageList;
    DatabaseReference mMessageReference;
    private TextView ownerTextView;
    private EditText newListNameEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_channels_list);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        userId =  intent.getStringExtra(MainActivity.USERID_KEY);
        btnCreateChannel = (Button) findViewById(R.id.createNewChannelButton);
        ownerTextView = (TextView) findViewById(R.id.ownerTextView);
        newListNameEditText = (EditText) findViewById(R.id.newChannelNameEV);
        readUserData(userId);
        messageList = new ArrayList<>();
        mMessageReference = FirebaseDatabase.getInstance().getReference("Channels");
        btnCreateChannel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewList();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
    }
    private void readUserData(String userId){
        mDatabase.child("Users").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User currentUser = dataSnapshot.getValue(User.class);
                if (currentUser != null) {
                    String test = currentUser.UserName;
                    ownerTextView.setText(test);
                }
                else {
                    ownerTextView.setText("");
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                ownerTextView.setText(""); ;
            }
        });
        mDatabase.child("Channels").push().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User currentUser = dataSnapshot.getValue(User.class);
                if (currentUser != null) {
                    String test = currentUser.UserName;
                    ownerTextView.setText(test);
                }
                else {
                    ownerTextView.setText("");
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                ownerTextView.setText(""); ;
            }
        });
    }
    private void createNewList(){
        String newListName = newListNameEditText.getText().toString();
        Channel newChannel = new Channel(newListName, userId, 1);

        Map<String, Object> newChannelValues = newChannel.toMap();
        Map<String, Object> channelUpdates = new HashMap<>();

        String key = mDatabase.child("Channels").push().getKey();

        channelUpdates.put("/Channels/" + key, newChannelValues);

        mDatabase.updateChildren(channelUpdates);
        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.channelsLayout);
        TextView newChannelView = new TextView(ShowChannelActivity.this);
        newChannelView.setText(newListName);
        newChannelView.setId(messageList.size());
        newChannelView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.FILL_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        linearLayout.addView(newChannelView);
    }
}
