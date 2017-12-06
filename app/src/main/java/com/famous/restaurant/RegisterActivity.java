package com.famous.restaurant;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;

public class RegisterActivity extends AppCompatActivity {

    DatabaseReference mDatabase;
    EditText reg_name;
    EditText reg_id;
    EditText reg_pwd;
    EditText reg_email;
    EditText reg_phone;
    Button reg_button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mDatabase = FirebaseDatabase.getInstance().getReference("users");

        reg_name = (EditText)findViewById(R.id.reg_name);
        reg_id = (EditText)findViewById(R.id.reg_id);
        reg_pwd = (EditText)findViewById(R.id.reg_pwd);
        reg_email =(EditText)findViewById(R.id.reg_email);
        reg_phone = (EditText)findViewById(R.id.reg_phone);

        reg_button = (Button)findViewById(R.id.reg_button);

        reg_button.setOnClickListener(new registerClickListener());

    }

    private class registerClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {


            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    Iterator<DataSnapshot> child = dataSnapshot.getChildren().iterator();
                    while (child.hasNext()) {
                        String new_id = reg_id.getText().toString();
                        if (new_id.equals(child.next().getKey())) {
                            Toast.makeText(getApplicationContext(), "아이디가 존재합니다.", Toast.LENGTH_LONG).show();
                            mDatabase.removeEventListener(this);
                            return;
                        }

                    }

                    MemVO member = new MemVO();
                    member.setId(reg_id.getText().toString());
                    member.setName(reg_name.getText().toString());
                    member.setEmail(reg_email.getText().toString());
                    member.setPassword(reg_pwd.getText().toString());
                    member.setPhone(reg_phone.getText().toString());

                    mDatabase.child(member.getId()).setValue(member);

                    Toast.makeText(getApplicationContext(), "추가되었습니다.", Toast.LENGTH_LONG).show();

                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }



}
