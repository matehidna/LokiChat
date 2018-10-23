package com.example.rtkr.lokichat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class ChatAktivity extends AppCompatActivity {
        private static String COLLECTION_KEY = "Chat";
        private static String DOCUMENT_KEY = "Message";
        private static String USERID_FIELD = "UserId";
        private static String TEXT_FIELD = "Text";
        private DatabaseReference mDatabase;
        private String userId;
        private String userName;

        private Button btnBack;
        private Button btnSend;
        private EditText edtSentText;
        private TextView tvAuthor;
        private TextView tvBody;
        private TextView tvOwner;

        private ArrayList<ChatMessages> messageList;
        private DatabaseReference mMessageReference;
        private ChildEventListener mMessageListener;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            mDatabase = FirebaseDatabase.getInstance().getReference();
            setContentView(R.layout.activity_message);
            // Get the Intent that started this activity and extract the string
            Intent intent = getIntent();
            userId =  intent.getStringExtra(MainActivity.USERID_KEY);
            btnSend = (Button) findViewById(R.id.btn_send);
            edtSentText = (EditText) findViewById(R.id.edt_sent_text);
            tvAuthor = (TextView) findViewById(R.id.tv_author);
            tvBody = (TextView) findViewById(R.id.tv_body);
            tvOwner = (TextView) findViewById(R.id.tv_owner);
            readUserData(userId);
            messageList = new ArrayList<>();

            mMessageReference = FirebaseDatabase.getInstance().getReference("Messages");

            btnSend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    submitMessage();
                    edtSentText.setText("");
                }
            });


            tvAuthor.setText("");
            tvBody.setText("");
        }
        private void readUserData(String userId){
            mDatabase.child("Users").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    userName = dataSnapshot.getValue().toString();
                    tvOwner.setText(userName);
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    tvOwner.setText("onChange"); ;
                }
            });
        }
    private void submitMessage() {
        final String body = edtSentText.getText().toString();

        if (TextUtils.isEmpty(body)) {
            edtSentText.setError("No Empty Message");
            return;
        }

        // User data change listener
        mDatabase.child("Users").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                if (user == null) {
                    Toast.makeText(ChatAktivity.this, "onDataChange: User data is null!", Toast.LENGTH_SHORT).show();
                    return;
                }

                addNewMessage(body);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e("", "onCancelled: Failed to read user!");
            }
        });
    }
    private void addNewMessage(String body) {
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
        ChatMessages message = new ChatMessages(userId, userName, body, time);

        Map<String, Object> messageValues = message.toMap();
        Map<String, Object> childUpdates = new HashMap<>();

        String key = mDatabase.child("Messages").push().getKey();

        childUpdates.put("/Messages/" + key, messageValues);

        mDatabase.updateChildren(childUpdates);
    }
    @Override
    protected void onStart() {
        super.onStart();

        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                // A new message has been added
                // onChildAdded() will be called for each node at the first time
                ChatMessages message = dataSnapshot.getValue(ChatMessages.class);
                messageList.add(message);
                ChatMessages lastMessage = messageList.get(messageList.size() - 1);

                LinearLayout linearLayout = (LinearLayout)findViewById(R.id.messageLayout);
                TextView tv_newMessage = new TextView(ChatAktivity.this);
                tv_newMessage.setText(lastMessage.userName +" wrote: " + lastMessage.body);
                tv_newMessage.setId(messageList.size());
                tv_newMessage.setLayoutParams(new LayoutParams(
                        LayoutParams.FILL_PARENT,
                        LayoutParams.WRAP_CONTENT));
                linearLayout.addView(tv_newMessage);
                tvAuthor.setText(lastMessage.userName);

                tvBody.setText(lastMessage.body);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                ChatMessages message = dataSnapshot.getValue(ChatMessages.class);
                Toast.makeText(ChatAktivity.this, "onChildChanged: " + message.body, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                ChatMessages message = dataSnapshot.getValue(ChatMessages.class);
                Toast.makeText(ChatAktivity.this, "onChildRemoved: " + message.body, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                ChatMessages message = dataSnapshot.getValue(ChatMessages.class);
                Toast.makeText(ChatAktivity.this, "onChildMoved: " + message.body, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ChatAktivity.this, "Failed to load Message.", Toast.LENGTH_SHORT).show();
            }
        };

        mMessageReference.addChildEventListener(childEventListener);
        mMessageListener = childEventListener;
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mMessageListener != null) {
            mMessageReference.removeEventListener(mMessageListener);
        }
    }
    }


