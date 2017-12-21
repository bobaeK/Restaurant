package com.famous.restaurant;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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

        if(SaveSharedPreference.getUserName(this)==null){
            Intent intent =new Intent(getApplicationContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        }

        // 회원가입 페이지로 이동 액션
        TextView link_signup = (TextView)findViewById(R.id.link_signup);
        link_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),RegisterActivity.class);
                startActivity(intent);
            }
        });

        TextView link_findId = (TextView)findViewById(R.id.link_findId);
        link_findId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),FindIDActivity.class);
                startActivity(intent);
            }
        });

        TextView link_findPwd = (TextView)findViewById(R.id.link_findPwd);
        link_findPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),FindPwdActivity.class);
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

                                SaveSharedPreference.setUserName( LoginActivity.this , checkMember.getId());
                                //Toast.makeText(getApplicationContext(),"로그인 되었습니다.",Toast.LENGTH_SHORT).show();
                                Intent intent =new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                                finish();
                                return;
                            }
                        }
                    }
                    // alert 창으로 수정
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    builder.setTitle("오류");
                    builder.setMessage("존재하지 않는 아이디이거나 비밀번호가 일치하지 않습니다.");
                    builder.setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    builder.show();
                    //Toast.makeText(getApplicationContext(),"존재하지 않는 정보입니다.",Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

}
