package com.example.assignment_2.friendlist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.assignment_2.Login.user;
import com.example.assignment_2.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.assignment_2.Util.Base64Util.base64ToBitmap;


public class ChatActivity extends AppCompatActivity {



    private TextView userText;
    private FirebaseUser firebaseUser;
    private DatabaseReference reference;

    private String username;
    private String friend_username;
    private String friend_avatar;

    ImageButton btn_send;
    EditText text_send;

    MessageAdapter messageAdapter;
    List<Chat> mChat;

    RecyclerView recyclerView;


    private TextView friendUsername;
    private ImageView friendAvatar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        recyclerView =findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager =new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        userText = findViewById(R.id.username_text);
        btn_send = findViewById(R.id.btn_send);
        text_send = findViewById(R.id.text_send);
        friendUsername = findViewById(R.id.chat_tv);
        friendAvatar = findViewById(R.id.chat_iv);

        username = getIntent().getStringExtra("username");
        friend_username = getIntent().getStringExtra("friend_username");
        friend_avatar = getIntent().getStringExtra("friend_avatar");

        friendUsername.setText(friend_username);
        if(friend_avatar !=null){
            friendAvatar.setImageBitmap(base64ToBitmap(friend_avatar));
        }

        //firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = text_send.getText().toString();
                if(!msg.equals("")){
                    sendMessage(username,friend_username,msg);
                    text_send.setText("");
                }else
                {
                    Toast.makeText(ChatActivity.this,"Cannot Send empty Message",Toast.LENGTH_SHORT).show();
                }
            }
        });

        //firebaseUser.getUid(),friend_username
        readMessage();

//        reference = FirebaseDatabase.getInstance().getReference("Chats");
//
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                readMessage();
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//
//        });



    }

    private void sendMessage(String sender, String receiver, String message){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        HashMap<String,Object> HashMap = new HashMap<>();
        HashMap.put("sender",sender);
        HashMap.put("receiver",receiver);
        HashMap.put("message", message);

        reference.child("Chats").push().setValue(HashMap);
        //readMessage();

    }

    //String myid, String userid
    private void readMessage(){
        //mChat = new ArrayList<>();
        mChat=new ArrayList();

        reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mChat.clear();
                for(DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    Map<String,String> chatMap = (Map<String, String>) snapshot.getValue();
                    String receiver = chatMap.get("receiver");
                    String sender = chatMap.get("sender");
                    String message = chatMap.get("message");
                    if((receiver.equals(username) && sender.equals(friend_username))||(receiver.equals(friend_username)&&sender.equals(username))){
                        Chat chat = new Chat();
                        chat.setReceiver(receiver);
                        chat.setSender(sender);
                        chat.setMsg(message);
                        chat.setUsername(username);
                        mChat.add(chat);
                    }
                }
                messageAdapter  = new MessageAdapter(ChatActivity.this,mChat);
                recyclerView.setAdapter(messageAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}