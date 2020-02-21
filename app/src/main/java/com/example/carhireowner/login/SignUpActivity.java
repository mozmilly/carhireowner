package com.example.carhireowner.login;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.carhireowner.MainActivity;
import com.example.carhireowner.R;
import com.example.carhireowner.car.AddCarActivity;
import com.example.carhireowner.login.interfaces.SignUpInterface;
import com.example.carhireowner.login.models.SignUp;
import com.example.carhireowner.utils.ApiUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks{
    SignUpInterface signUpInterface;

    private Spinner spinner;
    private EditText editText;


    private static final String TAG = SignUpActivity.class.getSimpleName();
    private static final int REQUEST_GALLERY_CODE = 200;
    private static final int READ_REQUEST_CODE = 300;
    private Uri uri;
    File file = null;

    Button upload;

    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        EditText first_name = findViewById(R.id.first_name_signup);
        // EditText username = findViewById(R.id.username_signup);
        //username.setVisibility(View.GONE);
        // EditText password = findViewById(R.id.password_signup);
        //EditText confirm_password = findViewById(R.id.confirm_password_signup);
        // password.setVisibility(View.GONE);
        //confirm_password.setVisibility(View.GONE);
        EditText last_name = findViewById(R.id.last_name_signup);
        EditText email = findViewById(R.id.email_signup);
        EditText ref_code = findViewById(R.id.ref_code);

        imageView = findViewById(R.id.sign_up_prof_pic);

        upload = findViewById(R.id.upload_image);

        Button signUp = findViewById(R.id.sign_up);
        TextView login = findViewById(R.id.sign_in);


        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alertDialog1 = new AlertDialog.Builder(SignUpActivity.this).create();
                alertDialog1.setMessage("Select whether to choose file or take a photo.");
                alertDialog1.setButton(AlertDialog.BUTTON_POSITIVE, "Take Photo...",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                startCamera();
                            }
                        });
                alertDialog1.setButton(AlertDialog.BUTTON_NEGATIVE, "Choose photo from gallery...",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {


                                    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                                        if (ContextCompat.checkSelfPermission(SignUpActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                                            ActivityCompat.requestPermissions(SignUpActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},REQUEST_GALLERY_CODE);
                                            Toast.makeText(SignUpActivity.this, "Clicked", Toast.LENGTH_SHORT).show();
                                        }
                                        else
                                        {
                                            Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                                                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                            startActivityForResult(pickPhoto , REQUEST_GALLERY_CODE);//one can be replaced with any action code
                                        }

                                    } else {
                                        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                        startActivityForResult(pickPhoto , REQUEST_GALLERY_CODE);//one can be replaced with any action code
                                    }
                                } else {
                                    Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                    startActivityForResult(pickPhoto , REQUEST_GALLERY_CODE);//one can be replaced with any action code

                                }
                            }
                        });
                alertDialog1.show();
                alertDialog1.setCanceledOnTouchOutside(false);
            }
        });


        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUpInterface = ApiUtils.getSignUpService();
                String firstname = first_name.getText().toString();
                String lastname = last_name.getText().toString();
                String email1 = email.getText().toString();
                String refCode = ref_code.getText().toString();


                RequestBody mFile = RequestBody.create(MediaType.parse("image/*"), file);
                MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), mFile);
                RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());
                RequestBody first_name_body = RequestBody.create(MediaType.parse("text/plain"), firstname);
                RequestBody user_name_body = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(getIntent().getStringExtra("pNumber")));
                RequestBody last_name_body = RequestBody.create(MediaType.parse("text/plain"), lastname);
                RequestBody email_body = RequestBody.create(MediaType.parse("text/plain"), email1);

                if (email1.length()>9){
                    signUpPost(first_name_body, user_name_body, last_name_body, email_body, filename, fileToUpload);
                } else {
                    if (email.length()<4){
                        Toast.makeText(SignUpActivity.this, "Your email seems to be invalid!!", Toast.LENGTH_SHORT).show();
                    }

                }



            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    public void signUpPost(RequestBody first_name, RequestBody username, RequestBody last_name, RequestBody email, RequestBody name, MultipartBody.Part file){
        signUpInterface.sign_up(username, first_name, last_name, email, name, file).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                if (response.code()==200){
                    Toast.makeText(SignUpActivity.this, "Sign up successful", Toast.LENGTH_SHORT).show();
                    startActivity(intent);

                } else {
                    Toast.makeText(SignUpActivity.this, "Phone number is already registered!", Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, SignUpActivity.this);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_GALLERY_CODE && resultCode == Activity.RESULT_OK) {
            Toast.makeText(this, "Done", Toast.LENGTH_SHORT).show();
            uri = data.getData();
            Uri selectedImage = uri;
            imageView.setImageURI(selectedImage);
            if (EasyPermissions.hasPermissions(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                String filePath = getRealPathFromURIPath(uri, SignUpActivity.this);
                file = new File(filePath);
                Log.d(TAG, "Filename " + file.getName());
                //RequestBody mFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);

            } else {
                EasyPermissions.requestPermissions(this, getString(R.string.read_file), READ_REQUEST_CODE, Manifest.permission.READ_EXTERNAL_STORAGE);
            }
        } else if (requestCode==1&&resultCode==RESULT_OK){
            Toast.makeText(this, "Done", Toast.LENGTH_SHORT).show();
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            uri = getImageUri(getApplicationContext(), imageBitmap);
            Uri selectedImage = uri;
            imageView.setImageURI(selectedImage);
//            Toast.makeText(this, String.valueOf(uri), Toast.LENGTH_SHORT).show();
            if (EasyPermissions.hasPermissions(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                String filePath = getRealPathFromURIPath(uri, SignUpActivity.this);
                file = new File(filePath);
                Log.d(TAG, "Filename " + file.getName());
                //RequestBody mFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);

            } else {
                EasyPermissions.requestPermissions(this, getString(R.string.read_file), READ_REQUEST_CODE, Manifest.permission.READ_EXTERNAL_STORAGE);
            }

        }
        else {
            Toast.makeText(this, "Done else", Toast.LENGTH_SHORT).show();
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
    private String getRealPathFromURIPath(Uri contentURI, Activity activity) {
        Cursor cursor = activity.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }
    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        if(uri != null){
            String filePath = getRealPathFromURIPath(uri, SignUpActivity.this);
            file = new File(filePath);
            Uri selectedImage = uri;
            imageView.setImageURI(selectedImage);

        } else {
            Toast.makeText(this, "uri is null", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Log.d(TAG, "Permission has been denied");
    }

    private void startCamera() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, 1);
        }

    }


}
