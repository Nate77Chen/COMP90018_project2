package com.example.assignment_2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Friends extends AppCompatActivity {
    private DatabaseReference databaseRef;
    private FirebaseAuth firebaseAuth;
    private Button SendRequest;
    private EditText username;
    //private String uEmail;

    //暫時讓user自己輸入自己的username，因為不能讀取現在登入的user的username！！
    private EditText selfUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        databaseRef = FirebaseDatabase.getInstance().getReference();
        //
        List<String> data = new ArrayList<>();
        data.add("friend1");
        data.add("friend2");
        data.add("friend3");
        //Map<String, Double> coordinate = new HashMap<String, Double>();
        //coordinate.put("lattitude", 0.0);
        //coordinate.put("landtitude", 0.12);

        //databaseRef.child("friends").child("user1").setValue(data);
        //databaseRef.child("coordinates").child("user1").setValue(coordinate);
        //Toast.makeText(Friends.this, "Save successful!!", Toast.LENGTH_SHORT).show();
        //


        firebaseAuth = FirebaseAuth.getInstance();
        username = findViewById(R.id.SearchedPersonName);
        SendRequest = findViewById(R.id.Send_friend_request_button);
        //FirebaseUser user = firebaseAuth.getCurrentUser();  //可先不要

        selfUsername = findViewById(R.id.selfUserName);

        //可先不要
        //可用來檢測有沒有登入
        /*
        if (user!=null){
            uEmail = user.getEmail();//這邊其實沒有直 = “”
            Query findSelfUserName = databaseRef.child("users").equalTo(uEmail);
            findSelfUserName.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Post post = dataSnapshot.getValue(Post.class);
                    self_username = post.selfUsername;
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        }

         */


        SendRequest.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                final String user_name = username.getText().toString();
                final String self_username = selfUsername.getText().toString();

                if (user_name.isEmpty()){
                    username.setError("Please enter the username you want to search");
                    username.requestFocus();
                }
                else if(!(user_name.isEmpty())) {
                    Query checkUsernameExistence = databaseRef.child("users").orderByChild("username").equalTo(user_name);
                    checkUsernameExistence.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(!(snapshot.exists())){
                                username.setError("Username not exists");
                                username.requestFocus();
                            }
                            else{
                                //self_username沒有直導致這邊不對
                                Query checkFriendExistence = databaseRef.child("friends").child(self_username).orderByChild("username").equalTo(user_name);
                                checkFriendExistence.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if(snapshot.exists()) {
                                            username.setError("The user is your friend already");
                                            username.requestFocus();
                                        }
                                        else{
                                            Map<String, String> request_details = new HashMap<String, String>();
                                            request_details.put("requestFrom", self_username);
                                            request_details.put("requestTo", user_name);
                                            request_details.put("status", "waiting");
                                            //request_details.put("waiting", self_username);//這邊型式不對
                                            databaseRef.child("friend request").child(user_name).setValue(request_details);

                                            Toast.makeText(Friends.this, "Request successfully sent!!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                }
                else {
                    Toast.makeText(Friends.this,"Error occured. T_T", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


}
