package com.example.rtkr.lokichat;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CreateNewUserAktivity extends AppCompatActivity {

    private Button createButton;
    private EditText userNickNameET;
    private EditText userMottoET;
    private EditText userEmailET;
    private EditText passwort1ET;
    private EditText passwort2ET;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_user_aktivity);
        createButton = (Button) findViewById(R.id.createButton);
        userNickNameET = (EditText) findViewById(R.id.userNickNameET);
        userMottoET = (EditText) findViewById(R.id.userMottoET);
        userEmailET = (EditText) findViewById(R.id.userEmailET);
        passwort1ET = (EditText) findViewById(R.id.passwort1ET);
        passwort2ET = (EditText) findViewById(R.id.passwort2ET);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateAndSubmit();
            }
        });
    }
    private void validateAndSubmit(){
        String pass1 = passwort1ET.getText().toString();
        String pass2 = passwort2ET.getText().toString();
        String email = userEmailET.getText().toString();
        String errorText = null;
        if (pass1 == null || pass2 == null || pass1.isEmpty() || pass2.isEmpty()){
            errorText = "Password shouldn't be empty";
        }
        else if (email == null || email.isEmpty() ){
            errorText = "Email shouldn't be empty";
        }
        else if (!pass1.equals(pass2)){
            errorText = "Passwords are not equal";
        }
        if (errorText == null) {
            createNewUser(email, pass1);
        }
        else {
            createErrorToast(errorText);
        }
    }

    private void createNewUser(String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            finish();
                            saveUserInfo(mAuth.getCurrentUser().getUid());
                        }
                        else {
                            Toast.makeText(CreateNewUserAktivity.this, "Can not create User", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
    private void createErrorToast(String errorText){
        Toast.makeText(CreateNewUserAktivity.this, errorText, Toast.LENGTH_LONG).show();
    }

    private void saveUserInfo(String userId){
        String userNick = userNickNameET.getText().toString();
        String userMotto = userMottoET.getText().toString();
        String errorText = null;
        if (userNick == null || userNick.isEmpty()){
            userNick = "Loki";
        }
        else if (userMotto == null || userMotto.isEmpty() ){
            userMotto = "I'll be back";
        }
        User newUser = new User(userNick, userMotto);
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("Users/" + userId, newUser);
        mDatabase.updateChildren(childUpdates);
    }
}
