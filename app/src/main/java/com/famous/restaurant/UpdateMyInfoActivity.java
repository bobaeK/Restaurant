package com.famous.restaurant;

import android.content.Intent;
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

public class UpdateMyInfoActivity extends AppCompatActivity {

    DatabaseReference mDatabase;
    EditText update_name;
    EditText update_id;
    EditText update_pwd;
    EditText update_pwd2;
    EditText update_email;
    EditText update_phone;
    Button update_button;

    String user_id;
    MemVO member;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_my_info);

        user_id = SaveSharedPreference.getUserName(this);
        member = new MemVO();

        mDatabase = FirebaseDatabase.getInstance().getReference("users");
        update_name = (EditText)findViewById(R.id.update_name);
        update_id = (EditText)findViewById(R.id.update_id);
        update_pwd = (EditText)findViewById(R.id.update_pwd);
        update_pwd2 = (EditText)findViewById(R.id.update_pwd2);
        update_email =(EditText)findViewById(R.id.update_email);
        update_phone = (EditText)findViewById(R.id.update_phone);

        update_pwd2.setOnFocusChangeListener( new View.OnFocusChangeListener(){

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus == false){
                    String pwd = update_pwd.getText().toString();
                    String pwd2 = update_pwd2.getText().toString();
                    if(!pwd.equals(pwd2)){
                        Toast.makeText(getApplicationContext(),"비밀번호가 일치하지 않습니다.",Toast.LENGTH_SHORT).show();
                        update_pwd2.setText("");
                    }
                }
            }
        });

        findUserInfo();

        update_button = (Button)findViewById(R.id.btn_update_info);
        update_button.setOnClickListener(new UpdateClickListener());

        Button btn_cancel = (Button)findViewById(R.id.btn_update_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void findUserInfo(){

        mDatabase.orderByKey().equalTo(user_id).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> child = dataSnapshot.getChildren().iterator();
                while(child.hasNext())
                {
                    DataSnapshot memSnapshot = child.next();
                    if(memSnapshot.getKey().equals(user_id)){
                        member = memSnapshot.getValue(MemVO.class);

                        update_name.setText(member.getName());
                        update_id.setText(member.getId());
                        update_id.setClickable(false);
                        update_id.setFocusable(false);
                        update_email.setText(member.getEmail());
                        update_phone.setText(member.getPhone());

                        break;
                    }
                }


            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private class UpdateClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            String pwd = update_pwd.getText().toString();
            String pwd2 = update_pwd2.getText().toString();
            if(pwd.equals("") || !pwd.equals(pwd2)){
                Toast.makeText(getApplicationContext(),"비밀번호를 확인해주세요",Toast.LENGTH_SHORT).show();
                return;
            }

            member.setPassword(pwd);
            member.setPhone(update_phone.getText().toString());
            member.setEmail(update_email.getText().toString());
            member.setName(update_name.getText().toString());

            mDatabase.child(user_id).setValue(member);
            Toast.makeText(getApplicationContext(),"수정되었습니다.",Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(getApplicationContext(), MypageActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
    }
}
