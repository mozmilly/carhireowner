package com.example.carhireowner.car;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.Toast;

import com.example.carhireowner.MainActivity;
import com.example.carhireowner.R;
import com.example.carhireowner.car.interfaces.CarInterface;
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

public class AddCarActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks{
    EditText make, numberPlate, color, seaters, price_per_day, location;
    ImageView imageView;
    Button upload, submit;

    private static final String TAG = AddCarActivity.class.getSimpleName();
    private static final int REQUEST_GALLERY_CODE = 200;
    private static final int READ_REQUEST_CODE = 300;
    private Uri uri;
    File file = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_car);

        make = findViewById(R.id.add_make);
        numberPlate = findViewById(R.id.add_number_plate);
        color = findViewById(R.id.add_color);
        seaters = findViewById(R.id.add_seaters);
        price_per_day = findViewById(R.id.add_price_per_day);
        location = findViewById(R.id.add_location);
        imageView = findViewById(R.id.add_image);
        upload = findViewById(R.id.upload);
        submit = findViewById(R.id.submit_car);


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            upload.setEnabled(false);
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE }, 0);
        }

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alertDialog1 = new AlertDialog.Builder(AddCarActivity.this).create();
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

                                        if (ContextCompat.checkSelfPermission(AddCarActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                                            ActivityCompat.requestPermissions(AddCarActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},REQUEST_GALLERY_CODE);
                                            Toast.makeText(AddCarActivity.this, "Clicked", Toast.LENGTH_SHORT).show();
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

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestBody mFile = RequestBody.create(MediaType.parse("image/*"), file);
                MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), mFile);
                RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());
                RequestBody make1 = RequestBody.create(MediaType.parse("text/plain"), make.getText().toString().trim());
                RequestBody numberPlate1 = RequestBody.create(MediaType.parse("text/plain"), numberPlate.getText().toString().trim());
                RequestBody color1 = RequestBody.create(MediaType.parse("text/plain"), color.getText().toString().trim());
                RequestBody seaters1 = RequestBody.create(MediaType.parse("text/plain"), seaters.getText().toString().trim());
                RequestBody pricePerDay1 = RequestBody.create(MediaType.parse("text/plain"), price_per_day.getText().toString().trim());
                RequestBody location1 = RequestBody.create(MediaType.parse("text/plain"), location.getText().toString().trim());

                SharedPreferences sp = getSharedPreferences("pref",0);
                String username = sp.getString("user", "no user");

                RequestBody username1 = RequestBody.create(MediaType.parse("text/plain"), username);

                CarInterface carInterface = ApiUtils.getCarService();
                carInterface.upload_car(username1, make1, numberPlate1, color1, seaters1, pricePerDay1, location1, fileToUpload, filename).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.code()==200){
                            Intent intent = new Intent(AddCarActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {

                    }
                });
            }
        });



    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, AddCarActivity.this);
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
                String filePath = getRealPathFromURIPath(uri, AddCarActivity.this);
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
                String filePath = getRealPathFromURIPath(uri, AddCarActivity.this);
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
            String filePath = getRealPathFromURIPath(uri, AddCarActivity.this);
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
