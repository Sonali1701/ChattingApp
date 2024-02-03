package com.example.chattingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import android.content.Intent;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;

public class chatWin extends AppCompatActivity {

    String reciverimg,reciverUid,reciverName,SenderUID;
    CircleImageView profile;
    TextView reciverNName;
    CardView sendbtn;
    EditText textmsg;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase database;
    public static String senderImg;
    public static String reciverIImg;
    String senderRoom,reciverRoom;
    RecyclerView mmsgAdpter;
    ArrayList<msgModel> msgArrayList;
    msgsAdpter msgsAdpter;

    @SuppressLint("MissingInflatedId")

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_win);
        database = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        mmsgAdpter = findViewById(R.id.msgadpter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        mmsgAdpter.setLayoutManager(linearLayoutManager);
        msgsAdpter = new msgsAdpter(chatWin.this,msgArrayList);
        mmsgAdpter.setAdapter(msgsAdpter);

        reciverName=getIntent().getStringExtra("nameeee");
        reciverimg=getIntent().getStringExtra("reciverImg");
        reciverUid=getIntent().getStringExtra("uid");

        msgArrayList = new ArrayList<>();

        sendbtn = findViewById(R.id.sendbtn);
        textmsg = findViewById(R.id.textmsg);

        profile= findViewById(R.id.profileimg);
        reciverNName=findViewById(R.id.recivername);

        Picasso.get().load(reciverimg).into(profile);
        reciverNName.setText(""+reciverName);

        DatabaseReference reference = database.getReference().child("user").child(firebaseAuth.getUid());
        DatabaseReference chatreference = database.getReference().child("user").child(senderRoom).child("messages");

        chatreference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                msgArrayList.clear();
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    msgModel messages= dataSnapshot.getValue(msgModel.class);
                    msgArrayList.add(messages);
                }
                msgsAdpter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               senderImg = snapshot.child("profilepic").getValue().toString();
               reciverIImg = reciverimg;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        SenderUID = firebaseAuth.getUid();

        senderRoom = SenderUID+reciverUid;
        reciverRoom = reciverUid+SenderUID;

        sendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg=textmsg.getText().toString();
                if(msg.isEmpty()){
                    Toast.makeText(chatWin.this,"Please Enter the Message",Toast.LENGTH_SHORT).show();
                    textmsg.setText("");
                    Date date = new Date();
                    msgModel msgs=new msgModel(msg,
                            SenderUID,
                            date.getTime());
                    database = FirebaseDatabase.getInstance();
                    database.getReference().child("chats").child("senderRoom").child("messages").push().setValue(msgs).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            database.getReference().child("chats").child("reciverRoom").
                                    child("messages").push().setValue(msgs).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                        }
                                    });
                        }
                    });

                }
            }
        });
    }
}