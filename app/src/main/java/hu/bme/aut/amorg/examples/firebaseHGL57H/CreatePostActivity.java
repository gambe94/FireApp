package hu.bme.aut.amorg.examples.firebaseHGL57H;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.net.URLEncoder;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hu.bme.aut.amorg.examples.firebaseHGL57H.data.Post;

public class CreatePostActivity extends BaseActivity {

    @BindView(R.id.etTitle)
    EditText etTitle;
    @BindView(R.id.etBody)
    EditText etBody;
    @BindView(R.id.imgAttach)
    ImageView imgAttach;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        ButterKnife.bind(this);
    }

    @OnClick(R.id.btnSend)
    void sendClick() {
        if (!isFormValid()) {
            return;
        }

        if (imgAttach.getVisibility() != View.VISIBLE) {
            uploadPost();
        } else {
            try {
                uploadPostWithImage();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadPost(String... imageUrl) {
        String key = FirebaseDatabase.getInstance().getReference().child("posts").push().getKey();
        Post newPost = new Post(getUid(), getUserName(), etTitle.getText().toString(),
                etBody.getText().toString());

        if (imageUrl != null && imageUrl.length>0) {
            newPost.setImageUrl(imageUrl[0]);
        }

        FirebaseDatabase.getInstance().getReference().child("posts").child(key).setValue(newPost).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(CreatePostActivity.this, "Post created", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    @OnClick(R.id.btnAttach)
    void attachClick() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePictureIntent, 101);
    }


    private boolean isFormValid() {
        boolean result = true;
        if (TextUtils.isEmpty(etTitle.getText().toString())) {
            etTitle.setError("Required");
            result = false;
        } else {
            etTitle.setError(null);
        }

        if (TextUtils.isEmpty(etBody.getText().toString())) {
            etBody.setError("Required");
            result = false;
        } else {
            etBody.setError(null);
        }

        return result;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 101 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imgAttach.setImageBitmap(imageBitmap);
            imgAttach.setVisibility(View.VISIBLE);
        }
    }

    public void uploadPostWithImage() throws Exception {
        imgAttach.setDrawingCacheEnabled(true);
        imgAttach.buildDrawingCache();
        Bitmap bitmap = imgAttach.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageInBytes = baos.toByteArray();



        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        String newImage = URLEncoder.encode(UUID.randomUUID().toString(), "UTF-8")+".jpg";
        StorageReference newImageRef = storageRef.child(newImage);
        StorageReference newImageImagesRef = storageRef.child("images/"+newImage);
        newImageRef.getName().equals(newImageImagesRef.getName());
        newImageRef.getPath().equals(newImageImagesRef.getPath());

        UploadTask uploadTask = newImageImagesRef.putBytes(imageInBytes);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Toast.makeText(CreatePostActivity.this, exception.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                uploadPost(taskSnapshot.getDownloadUrl().toString());
            }
        });
    }
}
