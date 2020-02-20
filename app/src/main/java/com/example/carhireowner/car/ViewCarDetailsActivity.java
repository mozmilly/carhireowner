package com.example.carhireowner.car;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import com.example.carhireowner.MainActivity;
import com.example.carhireowner.R;
import com.example.carhireowner.car.interfaces.CarInterface;
import com.example.carhireowner.car.models.Car;
import com.example.carhireowner.ui.home.HomeFragment;
import com.example.carhireowner.utils.ApiUtils;
import com.squareup.picasso.Picasso;

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

public class ViewCarDetailsActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks{
    Car car;
    TextView make, number_plate, seater, owner_phone;
    ImageView car_image, owner_image;
    Button save, upload;
    EditText date, no_of_days, price, color;
    CarInterface carInterface;


    private static final String TAG = ViewCarDetailsActivity.class.getSimpleName();
    private static final int REQUEST_GALLERY_CODE = 200;
    private static final int READ_REQUEST_CODE = 300;
    private Uri uri;
    File file ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_car_details);

        number_plate = findViewById(R.id.car_detail_number_plate);
        color = findViewById(R.id.car_detail_color);
        seater = findViewById(R.id.car_detail_seater);
        price = findViewById(R.id.car_detail_price_per_day);
        owner_phone = findViewById(R.id.car_detail_owner_phone);
        car_image = findViewById(R.id.car_detail_image);
        owner_image = findViewById(R.id.car_detail_owner_image);
        upload = findViewById(R.id.upload);
        save = findViewById(R.id.save);

        car = HomeFragment.getCar();

        number_plate.setText(("Number Plate "+car.getNumber_plate() ));
        color.setText(("Color "+car.getColor() ));
        seater.setText(("Seater "+car.getSeaters() ));
        price.setText(("Price Per Day "+car.getPrice_per_day() ));
        owner_phone.setText(("Owner Phone "+car.getOwner().getUser().getUsername() ));

        if (car.getPhoto()!=null){
            if (URLUtil.isValidUrl("https://carhiremodule.pythonanywhere.com"+car.getPhoto())){
                Picasso.with(ViewCarDetailsActivity.this)
                        .load("https://carhiremodule.pythonanywhere.com"+car.getPhoto())
                        .placeholder(R.drawable.cab)
                        .into(car_image);
            }
        }


        if (car.getOwner().getImage()!=null){
            if (URLUtil.isValidUrl("https://carhiremodule.pythonanywhere.com"+car.getOwner().getImage())){
                Picasso.with(ViewCarDetailsActivity.this)
                        .load("https://carhiremodule.pythonanywhere.com"+car.getOwner().getImage())
                        .placeholder(R.drawable.cab)
                        .into(owner_image);
            }
        }


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            upload.setEnabled(false);
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE }, 0);
        }

        BitmapDrawable drawable = (BitmapDrawable) car_image.getDrawable();
        Bitmap bitmap = drawable.getBitmap();

        uri = getImageUri(this, bitmap);

        String filePath = getRealPathFromURIPath(uri, ViewCarDetailsActivity.this);
        file = new File(filePath);


        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alertDialog1 = new AlertDialog.Builder(ViewCarDetailsActivity.this).create();
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

                                        if (ContextCompat.checkSelfPermission(ViewCarDetailsActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                                            ActivityCompat.requestPermissions(ViewCarDetailsActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},REQUEST_GALLERY_CODE);
                                            Toast.makeText(ViewCarDetailsActivity.this, "Clicked", Toast.LENGTH_SHORT).show();
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
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                carInterface = ApiUtils.getCarService();

                RequestBody mFile = RequestBody.create(MediaType.parse("image/*"), file);
                MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), mFile);
                RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());
                RequestBody color1 = RequestBody.create(MediaType.parse("text/plain"), color.getText().toString().trim());
                RequestBody pricePerDay1 = RequestBody.create(MediaType.parse("text/plain"), price.getText().toString().trim());
                RequestBody car_id = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(car.getId()));

                carInterface.edit_car(car_id, color1, pricePerDay1,fileToUpload, filename).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.code()==200){
                            Intent intent = new Intent(ViewCarDetailsActivity.this, MainActivity.class);
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
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, ViewCarDetailsActivity.this);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_GALLERY_CODE && resultCode == Activity.RESULT_OK) {
            Toast.makeText(this, "Done", Toast.LENGTH_SHORT).show();
            uri = data.getData();
            if (EasyPermissions.hasPermissions(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                String filePath = getRealPathFromURIPath(uri, ViewCarDetailsActivity.this);
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
//            Toast.makeText(this, String.valueOf(uri), Toast.LENGTH_SHORT).show();
            if (EasyPermissions.hasPermissions(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                String filePath = getRealPathFromURIPath(uri, ViewCarDetailsActivity.this);
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
            String filePath = getRealPathFromURIPath(uri, ViewCarDetailsActivity.this);
            file = new File(filePath);


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
