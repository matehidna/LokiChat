package com.example.rtkr.lokichat;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements
        View.OnClickListener {
    public static final String USERNAME_KEY = "com.example.rtkr.lokichat.usernameKey";
    public static final String USEREMAIL_KEY = "com.example.rtkr.lokichat.useremailKey";
    public static final String USERID_KEY = "com.example.rtkr.lokichat.useridKey";
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.authentificateButton).setOnClickListener(this);
        findViewById(R.id.newUserButton).setOnClickListener(this);
        findViewById(R.id.bettaButton).setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
    }

    private void startChat(String s1, String s2, String s3){
        Intent intent = new Intent(MainActivity.this, ChatAktivity.class);
        intent.putExtra(USEREMAIL_KEY, (s2 == null || s2.isEmpty())? "Email not defined":s2 );
        intent.putExtra(USERID_KEY, (s3 == null || s3.isEmpty())? "fehlerId" : s3 );
        startActivity(intent);
    }
    public void authentificateUser() {
        EditText userNameTextField = (EditText) findViewById(R.id.userNameTextView);
        EditText passwordTextField = (EditText) findViewById(R.id.userPasswordTextView);
        String userNameTextNullable = userNameTextField.getText().toString();
        String  passwordTextNullable = passwordTextField.getText().toString();
        final String userNameText = (userNameTextNullable == null || userNameTextNullable.isEmpty()) ? "anonymos" : userNameTextNullable;
        final String userPasswordText = (passwordTextNullable == null || passwordTextNullable.isEmpty()) ? "123" : passwordTextNullable;
        mAuth.signInWithEmailAndPassword(userNameText,
                userPasswordText)
                .addOnCompleteListener(this, new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if(task.isSuccessful()){
                    finish();
                    startChat(mAuth.getCurrentUser().getDisplayName(), mAuth.getCurrentUser().getEmail(), mAuth.getCurrentUser().getUid());
                }
                else {
                    startChat("Falsches Passwort für", userNameText, userPasswordText);
                }
            }
        });
    }
    public void ShowChannelList(String userId){
        Intent intent = new Intent(this, ShowChannelActivity.class);
        intent.putExtra(USERID_KEY, userId);
        startActivity(intent);
    }
    public void createNewUser() {
        Intent intent = new Intent(this, CreateNewUserAktivity.class);
        startActivity(intent);
    }
    @Override
    public void onClick(View view){
        int i = view.getId();
        if (i == R.id.newUserButton) {
            createNewUser();
         } else if (i == R.id.authentificateButton) {
            authentificateUser();
        }
        else if (i == R.id.bettaButton) {
            ShowChannelList("nnurLYgZDPUQuBo63E3xeYjbAo83");
        }
    }
}
