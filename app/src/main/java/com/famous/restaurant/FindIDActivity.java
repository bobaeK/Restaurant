package com.famous.restaurant;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

public class FindIDActivity extends AppCompatActivity {

    DatabaseReference mDatabase;
    EditText find_id_name;
    EditText find_id_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_id);

        // 취소하기
        Button btn = (Button)findViewById(R.id.btn_find_id_cancel);
        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mDatabase = FirebaseDatabase.getInstance().getReference("users");

        find_id_name = (EditText)findViewById(R.id.find_id_name);
        find_id_email = (EditText)findViewById(R.id.find_id_email);
        // ID 찾기 눌렸을 경우
        Button searchBtn = (Button)findViewById(R.id.btn_find_id);
        searchBtn.setOnClickListener(new FindIDListener());

    }

    private class FindIDListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {

            String id_name = find_id_name.getText().toString();
            String id_eamil = find_id_email.getText().toString();
            if(id_name.equals("")){
                Toast.makeText(getApplicationContext(),"이름을 입력해주세요.",Toast.LENGTH_SHORT).show();
                return;
            }else{
                if(id_eamil.equals("")){
                    Toast.makeText(getApplicationContext(),"이메일을 입력해주세요.",Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                String found_id = null;
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Iterator<DataSnapshot> child = dataSnapshot.getChildren().iterator();
                    while(child.hasNext()) {
                        DataSnapshot memData = child.next();
                        MemVO checkMember = memData.getValue(MemVO.class);

                        if(checkMember.getName().equals(find_id_name.getText().toString())){
                            Log.e("suprdroid",checkMember.getEmail()+" , "+find_id_email.getText().toString());
                            if(checkMember.getEmail().equals(find_id_email.getText().toString())){


                                found_id = checkMember.getId();
                                break;
                            }
                        }
                    }
                    AlertDialog.Builder builder = new AlertDialog.Builder(FindIDActivity.this);

                    if(found_id != null){
                        builder.setTitle("아이디 확인");
                        // 아이디 일부 메시지로 사용자에게 보여줌
                        int n = found_id.length();
                        String temp = found_id.substring(0,(n+1)/2);
                        for(int i=(n+1)/2;i<n;i++){
                            temp = temp+"*";
                        }
                        builder.setMessage(temp);
                        builder.setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        finish();
                                    }
                                });


                    }else {
                        builder.setTitle("실패");
                        // 아이디 일부 메시지로 사용자에게 보여줌
                        builder.setMessage("입력하신 정보와 일치하는 아이디가 존재하지 않습니다.");
                        builder.setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                    }

                    builder.show();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

}
