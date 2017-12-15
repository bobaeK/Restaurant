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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;


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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_add);

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

            // 객체에 저장.

            insertVO.setUser_id("wang");  // 임시
            insertVO.setRestaurant("temp"); // 임시
            insertVO.setImageCnt(imageCnt);
            insertVO.setReview_text(reviewText.getText().toString());
            insertVO.setRating_star(ratingBar.getNumStars());

            String key = UploadToFirebase(insertVO);

            if(imageCnt>0){
                // storage 에 저장
                UploadImageFileToStorage(key);

                }
            }
        }

        private boolean UploadImageFileToStorage(final String key) {

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
            return true;
        }

        private String UploadToFirebase(ReviewVO insertVO){

            String key = mDatabase.push().getKey();
            mDatabase.child(key).setValue(insertVO);

            int cnt = insertVO.getImageCnt();


            return key;
        }

}

