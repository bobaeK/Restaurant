package com.famous.restaurant;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.Calendar;
import java.util.Iterator;


public class ReviewAddActivity extends AppCompatActivity {

    StorageReference storageReference;
    DatabaseReference mDatabase;

    RatingBar ratingBar;

    EditText reviewText;
    TextView textCntView;

    ImageView btn_add_photo;
    ImageView imageView[];
    Bitmap imageBitmap[];
    Uri filePathUri[];

    Button btn_save_review;

    int imageCnt=0;
    int Image_Request_Code = 7;

    ReviewVO insertVO;
    String user_id;
    String restaurant_name;

    String auth_key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_add);

        user_id = SaveSharedPreference.getUserName(getApplicationContext());
        restaurant_name = getIntent().getStringExtra("restaurant_name");

        TextView toolbar_name = (TextView)findViewById(R.id.restaurant_name);
        toolbar_name.setText(restaurant_name);

        insertVO = new ReviewVO();

        mDatabase = FirebaseDatabase.getInstance().getReference("reviews");
        storageReference = FirebaseStorage.getInstance().getReference();

        /* 별점 */
        ratingBar = (RatingBar)findViewById(R.id.ratingBar1);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                insertVO.setRating_star((int) rating);
            }
        });


        /* 방문후기 텍스트 */
        reviewText = (EditText)findViewById(R.id.reviewText);
        textCntView = (TextView)findViewById(R.id.textCntView);

        reviewText.addTextChangedListener(new MyTextChangeListener());

        /* 사진첨부 */
        btn_add_photo = (ImageView)findViewById(R.id.btn_add_photo);

        imageView = new ImageView[4];
        imageBitmap = new Bitmap[4];
        filePathUri = new Uri[4];

        imageView[0] = (ImageView)findViewById(R.id.imageView1);
        imageView[1] = (ImageView)findViewById(R.id.imageView2);
        imageView[2] = (ImageView)findViewById(R.id.imageView3);
        imageView[3] = (ImageView)findViewById(R.id.imageView4);

        btn_add_photo.setOnClickListener(new Add_photo_onClickListener());

        for(int i=0;i<4;i++){
            imageView[i].setOnClickListener(new delete_photo_onClickListener(i));
        }

        /* 저장하기 */
        btn_save_review = (Button)findViewById(R.id.btn_save_review);
        btn_save_review.setOnClickListener(new save_review_onClickListener());

        // 리얼후기 인증 여부 확인 - 미리확인
        DatabaseReference authDatabase=FirebaseDatabase.getInstance().getReference("authentication");
        authDatabase.orderByChild("restaurant").equalTo(restaurant_name).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> child = dataSnapshot.getChildren().iterator();
                while (child.hasNext()) {
                    DataSnapshot authData = child.next();
                    AuthenticationVO authVO = authData.getValue(AuthenticationVO.class);
                    // 키값 가져오는 것 수정
                    // 회원 아이디랑 음식점 확인 후 , 리뷰 아이디 없을 때!
                    if (authVO.getMem_id().equals(user_id) && authVO.getReview_id().equals("none")){
                            auth_key = authData.getKey();
                            insertVO.setAuthentication(true); // 리얼후기임.
                            break;

                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Image_Request_Code && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePathUri[imageCnt] = data.getData();
            try {

                // Getting selected image into Bitmap.
                imageBitmap[imageCnt] = MediaStore.Images.Media.getBitmap(getContentResolver(), filePathUri[imageCnt]);
                // Setting up bitmap selected image into ImageView.
                imageView[imageCnt].setImageBitmap(imageBitmap[imageCnt]);
                imageView[imageCnt].setVisibility(ImageView.VISIBLE);

                imageCnt++;

                if(imageCnt == 4){
                    btn_add_photo.setVisibility(ImageView.GONE);
                }

            }
            catch (IOException e) {

                e.printStackTrace();
            }
        }
    }

    /* 글자수 제한 리스너 */
    private class MyTextChangeListener implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            int len = s.length();
            if(len > 1000){
                Toast.makeText(getApplicationContext(),"1000자가 넘었습니다.",Toast.LENGTH_SHORT).show();
            }
            textCntView.setText(len+"");
        }
    }

    // 파일 타입 수정
    public String GetFileExtension(Uri uri) {

        ContentResolver contentResolver = getContentResolver();

        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        // Returning the file Extension.
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri)) ;

    }

    // 사진 삭제 리스너
    private class delete_photo_onClickListener implements View.OnClickListener {
        int num;

        public delete_photo_onClickListener(int num) {
            this.num = num+1;
        }

        @Override
        public void onClick(View v) {

            if (imageCnt > num) {

                for (int i = num; i < imageCnt; i++) {
                    filePathUri[i-1] = filePathUri[i];
                    imageBitmap[i-1] = imageBitmap[i];
                    imageView[i - 1].setImageBitmap(imageBitmap[i-1]);
                }
            }
            imageBitmap[imageCnt-1] = null;
            filePathUri[imageCnt-1] = null;
            imageView[imageCnt - 1].setImageBitmap(null);
            imageView[imageCnt - 1].setVisibility(View.GONE);

            if(imageCnt == 4){
                btn_add_photo.setVisibility(ImageView.VISIBLE);
            }
            imageCnt--;
        }
    }

    // 사진 추가 리스너
    private class Add_photo_onClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {


            // Creating intent.
            Intent intent = new Intent();

            // Setting intent type as image to select image from phone storage.
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Please Select Image"), Image_Request_Code);

        }
    }

    /* 저장하기 */
    private class save_review_onClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {

            /* 입력 필수 체크 */
            String review_text = reviewText.getText().toString();
            if(review_text.length()<=0 || insertVO.getRating_star()==0){
                Toast.makeText(getApplicationContext(),"필수 항목을 입력해주세요.",Toast.LENGTH_SHORT);
                return;
            }

            Calendar now = Calendar.getInstance();
            String nowDate = now.get(Calendar.YEAR) + "/" +
                    (now.get(Calendar.MONTH) + 1) + "/" + now.get(Calendar.DATE);

            // 객체에 저장.
            insertVO.setUser_id(user_id);
            insertVO.setRestaurant(restaurant_name);
            insertVO.setDate(nowDate);
            insertVO.setImageCnt(imageCnt);
            insertVO.setReview_text(review_text);
            //insertVO.setRating_star(ratingBar.getNumStars());

            String key = UploadToFirebase(insertVO);

            // authentication에 review ID 추가하기.
            if(!auth_key.equals("")){
                UpdateAuthDB(key);
            }

            if(imageCnt>0){
                // storage 에 저장
                UploadImageFileToStorage(key);
            } else{
                movePage();
            }
            }
        }

        private void UploadImageFileToStorage(final String key) {

            for (int i = 0; i < imageCnt; i++) {
                StorageReference storageReference2nd = storageReference.child("review_images/" + System.currentTimeMillis() + "." + GetFileExtension(filePathUri[i]));
                storageReference2nd.putFile(filePathUri[i])
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @SuppressWarnings("VisibleForTests")
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                String img_url = taskSnapshot.getDownloadUrl().toString();
                                mDatabase.child(key).child("imageUrl").push().setValue(img_url);
                            }
                        })
                        // If something goes wrong .
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {


                                // Showing exception erro message.
                                Toast.makeText(ReviewAddActivity.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });

            }
            movePage();
        }

        private String UploadToFirebase(ReviewVO insertVO){

            String key = mDatabase.push().getKey();
            mDatabase.child(key).setValue(insertVO);

            int cnt = insertVO.getImageCnt();


            return key;
        }

        private void UpdateAuthDB(String key){
            DatabaseReference authDB = FirebaseDatabase.getInstance().getReference("authentication");
            authDB.child(auth_key).child("review_id").setValue(key);
        }

        private void movePage(){
            finish();
        }

}

