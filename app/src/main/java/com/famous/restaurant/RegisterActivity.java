package com.famous.restaurant;

import android.graphics.Color;
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

public class RegisterActivity extends AppCompatActivity {

    DatabaseReference mDatabase;
    EditText reg_name;
    EditText reg_id;
    EditText reg_pwd;
    EditText reg_pwd2;
    EditText reg_email;
    EditText reg_phone;
    Button reg_button;

    TextView id_check_textview;
    TextView password_check_textview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mDatabase = FirebaseDatabase.getInstance().getReference("users");

        reg_name = (EditText)findViewById(R.id.reg_name);
        reg_id = (EditText)findViewById(R.id.reg_id);
        reg_pwd = (EditText)findViewById(R.id.reg_pwd);
        reg_pwd2 = (EditText)findViewById(R.id.reg_pwd2);
        reg_email =(EditText)findViewById(R.id.reg_email);
        reg_phone = (EditText)findViewById(R.id.reg_phone);

        reg_button = (Button)findViewById(R.id.reg_button);
        reg_button.setOnClickListener(new registerClickListener());

        id_check_textview = (TextView)findViewById(R.id.id_check_textview);
        password_check_textview = (TextView)findViewById(R.id.password_check_textview);

        Id_check_listener listener = new Id_check_listener();

        reg_id.setOnFocusChangeListener(listener);

        reg_pwd2.setOnFocusChangeListener( new View.OnFocusChangeListener(){

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus == false){
                    String pwd = reg_pwd.getText().toString();
                    String pwd2 = reg_pwd2.getText().toString();
                    if(!pwd.equals(pwd2)){
                        password_check_textview.setText("비밀번호가 일치하지 않습니다.");
                        password_check_textview.setTextColor(Color.RED);
                        password_check_textview.setVisibility(TextView.VISIBLE);
                        //Toast.makeText(getApplicationContext(),,Toast.LENGTH_SHORT).show();
                        reg_pwd2.setText("");
                    } else{
                        password_check_textview.setText("비밀번호가 일치합니다.");
                        password_check_textview.setTextColor(Color.GREEN);
                    }
                }
            }
        });
    }

    public class Id_check_listener implements View.OnFocusChangeListener {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if(hasFocus == false){
                mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Iterator<DataSnapshot> child = dataSnapshot.getChildren().iterator();
                        while (child.hasNext()) {
                            String new_id = reg_id.getText().toString();
                            if (new_id.equals(child.next().getKey())) {
                                // Toast.makeText(getApplicationContext(), "아이디가 존재합니다.", Toast.LENGTH_LONG).show();
                                id_check_textview.setText("이미 사용중인 아이디입니다.");
                                id_check_textview.setTextColor(Color.RED);
                                id_check_textview.setVisibility(TextView.VISIBLE);

                                break;
                            }else {
                                id_check_textview.setText("아이디 사용가능합니다.");
                                id_check_textview.setTextColor(Color.GREEN);
                            }

                        }
                        //mDatabase.removeEventListener(this);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        }
    }

    private class registerClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            String pwd = reg_pwd.getText().toString();
            String pwd2 = reg_pwd2.getText().toString();
            if( pwd.equals("") || !pwd.equals(pwd2)){
                Toast.makeText(getApplicationContext(),"비밀번호를 확인해주세요",Toast.LENGTH_SHORT).show();
                return;
            }

            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    Iterator<DataSnapshot> child = dataSnapshot.getChildren().iterator();
                    while (child.hasNext()) {
                        String new_id = reg_id.getText().toString();
                        if (new_id.equals(child.next().getKey())) {
                            Toast.makeText(getApplicationContext(), "아이디를 확인해주세요.", Toast.LENGTH_LONG).show();
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
                    finish();

                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }



}
