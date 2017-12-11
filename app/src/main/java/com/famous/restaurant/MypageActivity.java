package com.famous.restaurant;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;

import static android.R.layout.simple_list_item_1;

public class MypageActivity extends AppCompatActivity {
    final static int MAX_LIST_NUM=6;
    String[] certifiedStoreList=new String[MAX_LIST_NUM];
    DatabaseReference mDatabase;
    String userName;
    String curPassword;
    TextView tv_userName;
    TextView tv_userId;
    TextView tv_userEmail;
    TextView tv_userPhone;
    EditText et_curPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);

        final RegisteredReviewAdapter ReviewAdapter=new RegisteredReviewAdapter();
        final ImageButton ib_back=(ImageButton)findViewById(R.id.ib_back);
        final ListView lv_reviewList=(ListView)findViewById(R.id.lv_reviewList);
        final ListView lv_certifiedList=(ListView)findViewById(R.id.lv_certifiedList);
        final ScrollView sv_myPage=(ScrollView)findViewById(R.id.sv_myPage);
        final ArrayAdapter<String> certifiedAdapter = new ArrayAdapter(this, simple_list_item_1, certifiedStoreList);
        final Button bt_infoModify=(Button)findViewById(R.id.bt_infoModify);

        userName=SaveSharedPreference.getUserName(MypageActivity.this);
        mDatabase = FirebaseDatabase.getInstance().getReference("users");

        tv_userName=(TextView)findViewById(R.id.tv_userName);
        tv_userId=(TextView)findViewById(R.id.tv_userId);
        tv_userEmail=(TextView)findViewById(R.id.tv_userEmail);
        tv_userPhone=(TextView)findViewById(R.id.tv_userPhone);
        et_curPassword=(EditText)findViewById(R.id.et_curPassword);

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> child = dataSnapshot.getChildren().iterator();
                while(child.hasNext())
                {
                    DataSnapshot memData = child.next();

                    if(memData.getKey().equals(userName))
                    {
                        MemVO checkMember = memData.getValue(MemVO.class);
                        tv_userName.setText(checkMember.getName());
                        tv_userId.setText(checkMember.getId());
                        tv_userEmail.setText(checkMember.getEmail());
                        tv_userPhone.setText(checkMember.getPhone());
                        curPassword=checkMember.getPassword();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        bt_infoModify.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent ev) {
                switch(ev.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        bt_infoModify.setAlpha((float)0.1);
                        break;
                    case MotionEvent.ACTION_UP:
                        bt_infoModify.setAlpha((float)1.0);
                        String inputPassword=et_curPassword.getText().toString();
                        if(inputPassword.equals(curPassword))
                            Toast.makeText(getApplicationContext(), "수정 가능", Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(getApplicationContext(), "수정 불가", Toast.LENGTH_LONG).show();
                        break;
                }
                return true;
            }
        }) ;

        ib_back.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent ev) {
                switch(ev.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        ib_back.setAlpha((float)0.1);
                        break;
                    case MotionEvent.ACTION_UP:
                        ib_back.setAlpha((float)1.0);
                        Intent intent=new Intent(getApplicationContext(), MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        break;
                }
                return true;
            }
        }) ;
        certifiedStoreList[0]="마루스시 "+"2017/11/02";
        certifiedStoreList[1]="마루스시 "+"2017/11/02";
        certifiedStoreList[2]="마루스시 "+"2017/11/02";
        certifiedStoreList[3]="마루스시 "+"2017/11/02";
        certifiedStoreList[4]="마루스시 "+"2017/11/02";
        certifiedStoreList[5]="마루스시 "+"2017/11/02";
        lv_certifiedList.setAdapter(certifiedAdapter);

        ReviewAdapter.addItem(new RegisteredReviewItem("마루스시 "+"2017/11/05 "+"사장님도 친절하시고 가게 분위기가 너무 좋았어요!"));
        ReviewAdapter.addItem(new RegisteredReviewItem("마루스시 "+"2017/11/05 "+"사장님도 친절하시고 가게 분위기가 너무 좋았어요!"));
        ReviewAdapter.addItem(new RegisteredReviewItem("마루스시 "+"2017/11/05 "+"사장님도 친절하시고 가게 분위기가 너무 좋았어요!"));
        ReviewAdapter.addItem(new RegisteredReviewItem("마루스시 "+"2017/11/05 "+"사장님도 친절하시고 가게 분위기가 너무 좋았어요!"));
        ReviewAdapter.addItem(new RegisteredReviewItem("마루스시 "+"2017/11/05 "+"사장님도 친절하시고 가게 분위기가 너무 좋았어요!"));
        ReviewAdapter.addItem(new RegisteredReviewItem("마루스시 "+"2017/11/05 "+"사장님도 친절하시고 가게 분위기가 너무 좋았어요!"));
        lv_reviewList.setAdapter(ReviewAdapter);

        lv_reviewList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                RegisteredReviewItem item=(RegisteredReviewItem)ReviewAdapter.getItem(position);
                Toast.makeText(getApplicationContext(), "선택 : "+item.getRegisteredReview(), Toast.LENGTH_LONG).show();
            }
        });

        lv_reviewList.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                sv_myPage.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        lv_certifiedList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item=certifiedAdapter.getItem(position);
                Toast.makeText(getApplicationContext(), "선택 : "+item, Toast.LENGTH_LONG).show();
            }
        });

        lv_certifiedList.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                sv_myPage.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

    }

    private class RegisteredReviewAdapter extends BaseAdapter {
        private ArrayList<RegisteredReviewItem> items=new ArrayList<RegisteredReviewItem>();

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            RegisteredReviewItemView view =(RegisteredReviewItemView) convertView;

            if(convertView==null)
                view=new RegisteredReviewItemView(getApplicationContext());

            RegisteredReviewItem item=items.get(position);
            view.setRegisteredReview(item.getRegisteredReview());

            return view;
        }

        void addItem(RegisteredReviewItem item) { items.add(item); }
    }
}
