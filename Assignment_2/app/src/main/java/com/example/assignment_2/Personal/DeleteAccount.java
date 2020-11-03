package com.example.assignment_2.Personal;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DeleteAccount extends AppCompatActivity {
    private DatabaseReference dbReference;
    private String username;
    private SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedpreferences = getApplicationContext().getSharedPreferences("Preferences", MODE_PRIVATE);
        username = sharedpreferences.getString("username", null);
        dbReference = FirebaseDatabase.getInstance().getReference();
        deleteSelfFromOthersFriendList();
        deleteSelfFromCoordinate();
        deleteSelfFromFriends();
        deleteSelfFromUsers();
        deleteSelfFromAuth();
    }

    private void deleteSelfFromOthersFriendList(){
        //get this user's friends
        Query queryFriendList = dbReference.child("friends/" + username);
        queryFriendList.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {
                    Map<String, Map<?, ?>> friendsList = (Map<String, Map<?, ?>>) snapshot.getValue();
                    List<String> fNameList = new ArrayList<String>(friendsList.keySet());

                    for(String aFriend:fNameList){
                        Query Query = dbReference.child("friends").orderByChild("username").equalTo(aFriend);
                        Query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot Snapshot: dataSnapshot.getChildren()) {
                                    Snapshot.getRef().removeValue();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // TODO Auto-generated method stub
            }
        });
    }

    private void deleteSelfFromCoordinate(){
        Query queryFriendList = dbReference.child("coordinates/" + username);
        queryFriendList.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot Snapshot: dataSnapshot.getChildren()) {
                    Snapshot.getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void deleteSelfFromFriends(){
        Query queryFriendList = dbReference.child("friends/" + username);
        queryFriendList.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot Snapshot: dataSnapshot.getChildren()) {
                    Snapshot.getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void deleteSelfFromUsers(){
        Query queryFriendList = dbReference.child("users/" + username);
        queryFriendList.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot Snapshot: dataSnapshot.getChildren()) {
                    Snapshot.getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void deleteSelfFromAuth(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        user.delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("TAG", "User account deleted.");
                        }
                    }
                });
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.clear();
        editor.commit();
        FirebaseAuth.getInstance().signOut();

    }
}
