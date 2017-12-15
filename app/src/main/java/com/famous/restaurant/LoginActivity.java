package com.famous.restaurant;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;

public class LoginActivity extends AppCompatActivity {

    DatabaseReference mDatabase;
    EditText input_id;
    EditText input_pwd;
    Button btn_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 회원가입 페이지로 이동 액션
        TextView link_signup = (TextView)findViewById(R.id.link_signup);
        link_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),RegisterActivity.class);
                startActivity(intent);
            }
        });
        input_id = (EditText)findViewById(R.id.input_id);
        input_pwd = (EditText)findViewById(R.id.input_password);
        btn_login = (Button)findViewById(R.id.btn_login);

        mDatabase = FirebaseDatabase.getInstance().getReference("users");
        btn_login.setOnClickListener(new loginClickListener());

    }

    // 로그인 처리
    private class loginClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Iterator<DataSnapshot> child = dataSnapshot.getChildren().iterator();
                    while(child.hasNext())
                    {
                        DataSnapshot memData = child.next();

                        if(memData.getKey().equals(input_id.getText().toString()) )
                        {
                            MemVO checkMember = memData.getValue(MemVO.class);
                            if(checkMember.getPassword().equals(input_pwd.getText().toString())){

                                SaveSharedPreference.setUserName( LoginActivity.this , checkMember.getName());
                                Toast.makeText(getApplicationContext(),"로그인 되었습니다.",Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                return;
                            }
                        }
                    }
                    Toast.makeText(getApplicationContext(),"존재하지 않는 아이디입니다.",Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

}
