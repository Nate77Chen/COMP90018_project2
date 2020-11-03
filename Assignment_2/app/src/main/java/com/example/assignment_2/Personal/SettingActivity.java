package com.example.assignment_2.Personal;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.assignment_2.R;

public class SettingActivity extends AppCompatActivity {

    private TextView tv_d_self;
    private TextView tv_d_friend;
    private TextView tv_about;

    private ImageButton ib_back;

    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        tv_d_self = (TextView) findViewById(R.id.set_tv_delete_self);
        tv_d_friend = (TextView) findViewById(R.id.set_tv_delete_friends);
        tv_about = (TextView) findViewById(R.id.set_tv_about);
        ib_back = (ImageButton) findViewById(R.id.set_btn_back);

        username = getIntent().getStringExtra("username");

        ib_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tv_d_self.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
                builder.setCancelable(true);
                builder.setTitle("Account deletion");
                builder.setMessage("Do you wish to continue deleting you account?");
                builder.setPositiveButton("Confirm",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(SettingActivity.this, DeleteAccount.class);
                                startActivity(intent);
                            }
                        });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        tv_d_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_s = new Intent(SettingActivity.this, DeleteFriendActivity.class);
                intent_s.putExtra("username", username);
                startActivity(intent_s);
            }
        });
        tv_about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}