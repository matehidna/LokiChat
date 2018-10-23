package com.example.rtkr.lokichat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AuthentificateUserActivity extends AppCompatActivity {
        private static String COLLECTION_KEY = "Chat";
        private static String DOCUMENT_KEY = "Message";
        private static String USERID_FIELD = "UserId";
        private static String TEXT_FIELD = "Text";
        private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        setContentView(R.layout.activity_authentificate_user);
        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        String userName = intent.getStringExtra(MainActivity.USERNAME_KEY);
        String userId =  intent.getStringExtra(MainActivity.USERID_KEY);
        String userEmail =  intent.getStringExtra(MainActivity.USEREMAIL_KEY);
        readUserData(userId);
    }
    private void readUserData(String userId){
        final TextView textView = findViewById(R.id.textView4);

        mDatabase.child("Users").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                textView.setText(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError error) {
                textView.setText("onChange"); ;
            }
        });   }
}
