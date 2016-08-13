package com.example.android.hospice;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ChatRoomActivity extends AppCompatActivity {

    private Button sendMessage;
    private EditText inputMessage;
    private TextView chat;

    private String userName, roomName;
    private DatabaseReference root;
    private String tmpKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        sendMessage = (Button) findViewById(R.id.send_button);
        inputMessage = (EditText) findViewById(R.id.message);
        chat = (TextView) findViewById(R.id.textView);

//        userName = UserProfile.this.personName;
        userName = "";
        roomName = getIntent().getExtras().get("room_name").toString();

        setTitle("Room - " + roomName);
        root = FirebaseDatabase.getInstance().getReference().child("chat-rooms").child(roomName);
        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, Object> map = new HashMap<String, Object>();
                tmpKey = root.push().getKey();
                root.updateChildren(map);

                DatabaseReference msgRoot = root.child(tmpKey);
                Map<String, Object> map2 = new HashMap<String, Object>();
                map2.put("name", userName);
                map2.put("msg", inputMessage.getText().toString());
                msgRoot.updateChildren(map2);
                inputMessage.setText("");
            }
        });

        root.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                appendMessage(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                appendMessage(dataSnapshot);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private String chatMsg, chatUserName;

    private void appendMessage(DataSnapshot dataSnapshot) {
        Iterator i = dataSnapshot.getChildren().iterator();
        while (i.hasNext()) {
            chatMsg = (String) ((DataSnapshot) i.next()).getValue();
            chatUserName = (String) ((DataSnapshot) i.next()).getValue();
            chat.append(chatUserName + " : " + chatMsg + "\n");
        }
    }
}
