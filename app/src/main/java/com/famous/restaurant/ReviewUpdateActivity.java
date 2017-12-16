package com.famous.restaurant;

import android.app.ProgressDialog;
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

import com.bumptech.glide.Glide;
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
import java.util.Iterator;

public class ReviewUpdateActivity extends AppCompatActivity {
    StorageReference storageReference;
    DatabaseReference mDatabase;

    RatingBar ratingBar;

    EditText reviewText;
    TextView textCntView;

    ImageView btn_update_photo;
    ImageView update_imageView[];
    Bitmap imageBitmap[];
    String imageFB[];
    Uri filePathUri[];

    Button btn_update_review;
    Button btn_delete_review;

    int FB_imageCnt = 0;
    int new_imageCnt = 0;
    int Image_Request_Code = 7;

    ReviewVO insertVO;

    String review_key;
    ProgressDialog progressDialog ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_update);
//        insertVO = new ReviewVO();
        progressDialog = new ProgressDialog(this);
        mDatabase = FirebaseDatabase.getInstance().getReference("reviews");
        storageReference = FirebaseStorage.getInstance().getReference();

        /* 별점 */
        ratingBar = (RatingBar) findViewById(R.id.ratingBar1);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                insertVO.setRating_star((int) rating);
            }
        });


        /* 방문후기 텍스트 */
        reviewText = (EditText) findViewById(R.id.review_update_text);
        textCntView = (TextView) findViewById(R.id.textCntView);

        reviewText.addTextChangedListener(new MyTextChangeListener());

        /* 사진첨부 */
        btn_update_photo = (ImageView) findViewById(R.id.btn_update_photo);

        update_imageView = new ImageView[4];
        imageBitmap = new Bitmap[4];
        filePathUri = new Uri[4];
        imageFB = new String[4];
//        image_url = new String[4];

        update_imageView[0] = (ImageView) findViewById(R.id.update_imageView1);
        update_imageView[1] = (ImageView) findViewById(R.id.update_imageView2);
        update_imageView[2] = (ImageView) findViewById(R.id.update_imageView3);
        update_imageView[3] = (ImageView) findViewById(R.id.update_imageView4);

        btn_update_photo.setOnClickListener(new Add_photo_onClickListener());

        for (int i = 0; i < 4; i++) {
            update_imageView[i].setOnClickListener(new delete_photo_onClickListener(i));
        }

        /* 저장하기 */
        btn_update_review = (Button) findViewById(R.id.btn_update_review);
        btn_update_review.setOnClickListener(new update_review_onClickListener());
        /* 삭제하기 */
        btn_delete_review = (Button) findViewById(R.id.btn_delete_review);
        btn_delete_review.setOnClickListener(new delete_review_onClickListener());

        review_key=getIntent().getStringExtra("SELECTED_ITEM");

        /* 데이터 setting */
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> child = dataSnapshot.getChildren().iterator();
                while (child.hasNext()) {
                    DataSnapshot reviewData = child.next();

                    // 키값 가져오는 것 수정
                    if (reviewData.getKey().equals(review_key)) {
                        insertVO = reviewData.getValue(ReviewVO.class);
                        reviewText.setText(insertVO.getReview_text());
                        ratingBar.setRating(insertVO.getRating_star());

                        //   Toast.makeText(getApplicationContext()," 확인 "+updateVO.getImageUri().get(0),Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        /* 사진있으면 FB에서 불러오기 */
        DatabaseReference dataR1 = mDatabase.child(review_key).child("imageUrl");
        dataR1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // Toast.makeText(getApplicationContext(),"Gg",Toast.LENGTH_SHORT).show();
//                List<String> list = new ArrayList<String>();
                Iterator<DataSnapshot> list_it = dataSnapshot.getChildren().iterator();

                while (list_it.hasNext() && FB_imageCnt < 4) {
                    DataSnapshot dss = list_it.next();
                    imageFB[FB_imageCnt] = dss.getValue(String.class);
                   // Toast.makeText(getApplicationContext()," "+imageFB[FB_imageCnt],Toast.LENGTH_SHORT).show();

                    Glide.with(getApplicationContext()).load(imageFB[FB_imageCnt]).into(update_imageView[FB_imageCnt]);
                    update_imageView[FB_imageCnt].setVisibility(ImageView.VISIBLE);
                    FB_imageCnt++;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        // 사진 4개이상 될 경우 add button 없앤다.
        if (FB_imageCnt == 4) {
            btn_update_photo.setVisibility(ImageView.GONE);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        int totNum = FB_imageCnt + new_imageCnt;
        Toast.makeText(getApplicationContext()," "+totNum,Toast.LENGTH_SHORT).show();
        if (requestCode == Image_Request_Code && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePathUri[totNum] = data.getData();
            try {

                // Getting selected image into Bitmap.
                imageBitmap[totNum] = MediaStore.Images.Media.getBitmap(getContentResolver(), filePathUri[totNum]);
                // Setting up bitmap selected image into ImageView.
                update_imageView[totNum].setImageBitmap(imageBitmap[totNum]);
                update_imageView[totNum].setVisibility(ImageView.VISIBLE);

                new_imageCnt++;
                totNum++;
                if (totNum == 4) {
                    btn_update_photo.setVisibility(ImageView.GONE);
                }

            } catch (IOException e) {

                e.printStackTrace();
            }
        }
    }

    /* 글자수 check */
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
            if (len > 1000) {
                Toast.makeText(getApplicationContext(), "1000자가 넘었습니다.", Toast.LENGTH_SHORT).show();
            }
            textCntView.setText(len + "");
        }
    }

    /* 사진첨부 */
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

    /* 사진삭제 */
    private class delete_photo_onClickListener implements View.OnClickListener {
        int num;

        public delete_photo_onClickListener(int i) {
            num = i;
        }

        @Override
        public void onClick(View v) {
            boolean chk_fb = false;
            int tot = FB_imageCnt + new_imageCnt;
            if(num<tot){
                int temp_num = num;
                if(num<=FB_imageCnt-1){
                    for(int i=num;i<FB_imageCnt-1;i++){
                        imageFB[i]=imageFB[i+1];
                        Glide.with(getApplicationContext()).load(imageFB[i]).into(update_imageView[i]);
                        temp_num++;
                    }

                    chk_fb=true;
                }
                if(temp_num <= tot-1){
                    for(int i=temp_num; i<tot-1;i++){
                        filePathUri[i]=filePathUri[i+1];
                        imageBitmap[i]=imageBitmap[i+1];
                        update_imageView[i].setImageBitmap(imageBitmap[i]);
                    }
                }
            }
            if(num==FB_imageCnt){
                chk_fb=true;
            }

            if(chk_fb){
                FB_imageCnt--;
            }else{
                new_imageCnt--;
            }

            update_imageView[tot-1].setVisibility(ImageView.GONE);

            if(tot == 4){
                btn_update_photo.setVisibility(ImageView.VISIBLE);
            }
        }
    }

    /* 수정하기 */
    private class update_review_onClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            // Setting progressDialog Title.
            progressDialog.setTitle("Image is Uploading...");

            // Showing progressDialog.
            progressDialog.show();

            /* 입력 필수 체크 */
            String review_text = reviewText.getText().toString();
            if(review_text.length()<=0 || insertVO.getRating_star()==0){
                Toast.makeText(getApplicationContext(),"필수 항목을 입력해주세요.",Toast.LENGTH_SHORT);
                return;
            }


            int tot = new_imageCnt+FB_imageCnt;
            insertVO.setImageCnt(tot);
            insertVO.setReview_text(reviewText.getText().toString());
            //insertVO.setRating_star(ratingBar.getNumStars());

            // 데이터 저장
            mDatabase.child(review_key).setValue(insertVO);

            if(tot>0){
                // storage 에 저장
                UploadImageFileToStorage();
            }
        }
    }

    private void UploadImageFileToStorage(){
        // FB에 저장되어있던 이미지 DB에 넣기
        for(int j=0;j<FB_imageCnt;j++)
            mDatabase.child(review_key).child("imageUrl").push().setValue(imageFB[j]);

        // 새로운 이미지 Storage에 추가 및 DB에 넣기
        for (int i = 0; i < new_imageCnt; i++) {
            StorageReference storageReference2nd = storageReference.child("review_images/" + System.currentTimeMillis() + "." + GetFileExtension(filePathUri[i+FB_imageCnt]));
            storageReference2nd.putFile(filePathUri[i+FB_imageCnt])
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @SuppressWarnings("VisibleForTests")
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            String image_url = taskSnapshot.getDownloadUrl().toString();
                            //image_url_cnt++;
                            // Log.e("superdroid",image_url[image_url_cnt-1]);


                            mDatabase.child(review_key).child("imageUrl").push().setValue(image_url);


                        }
                    })
                    // If something goes wrong .
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {


                            // Showing exception erro message.
                            Toast.makeText(ReviewUpdateActivity.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });

        }
        progressDialog.dismiss();
    }

    private class delete_review_onClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {

        }
    }

    // 파일 타입 수정
    public String GetFileExtension(Uri uri) {

        ContentResolver contentResolver = getContentResolver();

        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        // Returning the file Extension.
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));

    }
}
