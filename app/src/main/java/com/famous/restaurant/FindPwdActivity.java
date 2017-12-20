package com.famous.restaurant;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
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

public class FindPwdActivity extends AppCompatActivity {

    DatabaseReference mDatabase;
    EditText find_pwd_id;
    EditText find_pwd_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_pwd);
        mDatabase = FirebaseDatabase.getInstance().getReference("users");


        find_pwd_id = (EditText)findViewById(R.id.find_pwd_id);
        find_pwd_email = (EditText)findViewById(R.id.find_pwd_email);
        // ID 찾기 눌렸을 경우
        Button searchBtn = (Button)findViewById(R.id.btn_find_pwd);
        searchBtn.setOnClickListener(new FindPwdListener());

    }

    private class FindPwdListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {

            String pwd_id=find_pwd_id.getText().toString();
            String pwd_email = find_pwd_email.getText().toString();

            if(pwd_id.equals("")){
                Toast.makeText(getApplicationContext(),"아이디를 입력해주세요.",Toast.LENGTH_SHORT).show();
                return;
            }else{
                if(pwd_email.equals("")){
                    Toast.makeText(getApplicationContext(),"이메일을 입력해주세요.",Toast.LENGTH_SHORT).show();
                    return;
                }

            }

            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                boolean found = false;
                DataSnapshot memData = null;
                MemVO checkMember = null;
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Iterator<DataSnapshot> child = dataSnapshot.getChildren().iterator();
                    while(child.hasNext()) {
                        memData = child.next();

                        checkMember = memData.getValue(MemVO.class);
                        if(checkMember.getId().equals(find_pwd_id.getText().toString())){
                            if(checkMember.getEmail().equals(find_pwd_email.getText().toString())){

                                found = true;
                                break;
                            }
                        }
                    }
                    AlertDialog.Builder builder = new AlertDialog.Builder(FindPwdActivity.this);

                    if(found){


                        LayoutInflater inflater = getLayoutInflater();
                        View view = inflater.inflate(R.layout.change_pwd , null);
                        builder.setView(view);

                        final EditText pwd_text1 = (EditText)view.findViewById(R.id.change_pwd);
                        final EditText pwd_text_check = (EditText)view.findViewById(R.id.change_pwd_check);

                        final AlertDialog dialog = builder.create();

                        Button changeBtn = (Button)view.findViewById(R.id.btn_change_pwd);
                        changeBtn.setOnClickListener(new View.OnClickListener(){
                            @Override
                            public void onClick(View v) {
                                String new_pwd = pwd_text1.getText().toString();
                                if(new_pwd.equals(pwd_text_check.getText().toString())){
                                    // 새로운 비밀번호 설정해주는 과정
                                    checkMember.setPassword(new_pwd);
                                    mDatabase.child(memData.getKey()).setValue(checkMember);
                                    Toast.makeText(getApplicationContext(),"비밀번호가 수정되었습니다.",Toast.LENGTH_LONG).show();
                                    dialog.dismiss();
                                    finish();

                                }else {
                                    Toast.makeText(getApplicationContext(),"비밀번호가 일치하지 않습니다.",Toast.LENGTH_LONG).show();
                                }
                            }
                        });

                        Button cancelBtn = (Button)view.findViewById(R.id.btn_change_pwd_cancle);
                        cancelBtn.setOnClickListener(new View.OnClickListener(){
                            @Override
                            public void onClick(View v) {
                                finish();
                            }
                        });
                        dialog.show();

                    }else {
                        builder.setTitle("실패");
                        // 아이디 일부 메시지로 사용자에게 보여줌
                        builder.setMessage("입력하신 아이디와 이메일 정보가 존재하지 않습니다.");
                        builder.setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                        builder.show();
                    }


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    public void onCancel(View v){
        finish();
    }

}
