package com.example.wm_fitness;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.gson.Gson;
import com.example.wm_fitness.Configurations.GlobalVariables;
import com.example.wm_fitness.Models.EditProfileModel;
import com.example.wm_fitness.Models.ProfileInfoModel;
import com.example.wm_fitness.Models.ResponseModel;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class EditProfileActivity extends AppCompatActivity {

    // GPSTracker class

    private Bundle extras;
    private ProfileInfoModel profileInfoModel;
    private EditText firstnameET, lastnameET, jobET, biographyET, heightET, weightET;
    private RadioButton maleRB,femaleRB;
    private ImageView profilePhotoIV;
    private TextView locateInfo;
    private Spinner heightSP, weightSP;


    private Button changeProfilePhotoBTN;
    private Bitmap bitmap = null;
    private final String editProfile = GlobalVariables.API_URL + "profile/updateProfile";
    /*PHOTO UP*/

    public static final int GALLERY_REQUEST_CODE = 105;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Uri contentUri = data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),contentUri);
                    profilePhotoIV.setBackgroundResource(0);
                    profilePhotoIV.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Resim yüklenemedi!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private String getFileExt(Uri contentUri) {
        ContentResolver c = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(c.getType(contentUri));
    }

    /*PHOTO UP*/




    public void bindViews(){
        firstnameET = findViewById(R.id.firstnameET);
        lastnameET = findViewById(R.id.lastnameET);
        jobET = findViewById(R.id.jobET);
        profilePhotoIV = findViewById(R.id.profilePhotoIV);
        maleRB = findViewById(R.id.maleRB);
        femaleRB = findViewById(R.id.femaleRB);
        biographyET = findViewById(R.id.biographyET);
        //heightET = findViewById(R.id.heightET);
        //weightET = findViewById(R.id.weightET);
        changeProfilePhotoBTN = findViewById(R.id.changeProfilePhotoBTN);
        locateInfo = findViewById(R.id.locateInfo);
        heightSP = (Spinner)findViewById(R.id.heightSP);
        weightSP = (Spinner)findViewById(R.id.weightSP);

        changeProfilePhotoBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(gallery,GALLERY_REQUEST_CODE);
            }
        });
    }

    public void bindInformations(){
        firstnameET.setText(profileInfoModel.getFirstname());
        lastnameET.setText(profileInfoModel.getLastname());
        jobET.setText(profileInfoModel.getJob());
        biographyET.setText(profileInfoModel.getBiography());
        locateInfo.setText(profileInfoModel.getLocate());
        if(profileInfoModel.getGender().equals("Erkek")){
            maleRB.setChecked(true);
        }else if(profileInfoModel.getGender().equals("Kadın")){
            femaleRB.setChecked(true);
        }
        if(profileInfoModel.getPhoto().equals("none")){
            profilePhotoIV.setBackgroundResource(R.drawable.baseline_account_box_24);
        }else{
            Picasso.with(EditProfileActivity.this)
                    .load(GlobalVariables.API_URL + profileInfoModel.getPhoto())
                    .fit()
                    .centerInside()
                    .into(profilePhotoIV);
        }
    }

    private void weightSP() {
        List weight = new ArrayList<Integer>();
        for (int i = 0; i <= 250; i++) {
            weight.add(Integer.toString(i));
        }
        int compareValue = Integer.parseInt(profileInfoModel.getWeight());
        ArrayAdapter<Integer> spinnerArrayAdapter = new ArrayAdapter<Integer>(this, R.layout.color_spinner, weight);
        spinnerArrayAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
        weightSP.setAdapter(spinnerArrayAdapter);
        weightSP.setSelection(Integer.parseInt(profileInfoModel.getWeight()));
    }

    public void heightSP(){
        List height = new ArrayList<Integer>();
        for (int i = 0; i <= 250; i++) {
            height.add(Integer.toString(i));
        }
        ArrayAdapter<Integer> spinnerArrayAdapter = new ArrayAdapter<Integer>(this, R.layout.color_spinner, height);
        spinnerArrayAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
        heightSP.setAdapter(spinnerArrayAdapter);
        heightSP.setSelection(Integer.parseInt(profileInfoModel.getHeight()));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        extras = getIntent().getExtras();
        profileInfoModel = (ProfileInfoModel) extras.getSerializable("profileInfo");
        bindViews();
        bindInformations();
        heightSP();
        weightSP();
    }






    public void saveEditedProfile(View v){
        if(
                firstnameET.getText().toString().trim().equals("") ||
                        lastnameET.getText().toString().trim().equals("") ||
                        jobET.getText().toString().trim().equals("") ||
                        biographyET.getText().toString().trim().equals("") ||
                        locateInfo.getText().toString().trim().equals("") ||
                        !maleRB.isChecked() && !femaleRB.isChecked()
        ){
            Toast.makeText(this, "Boş alan bırakmayınız..", Toast.LENGTH_SHORT).show();
        }else{
            String gender = maleRB.isChecked() ? maleRB.getText().toString() : femaleRB.getText().toString();
            String photo  = "";
            int w = Integer.parseInt(weightSP.getSelectedItem().toString());
            int h = Integer.parseInt(heightSP.getSelectedItem().toString());
            float bmi = (float) w / ((float) h * h / 10000);
            String vkia = String.format(Locale.getDefault(), "%.1f", bmi);
            String vki = vkia;
            if(bitmap == null){
                photo = "none";
            }else{
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG,75,byteArrayOutputStream);
                byte[] imageInByte = byteArrayOutputStream.toByteArray();
                photo = Base64.encodeToString(imageInByte, Base64.DEFAULT);
            }
            EditProfileModel editProfileModel = new EditProfileModel(
                    jobET.getText().toString(),
                    gender,
                    biographyET.getText().toString(),
                    firstnameET.getText().toString(),
                    lastnameET.getText().toString(),
                    photo,
                    heightSP.getSelectedItem().toString(),
                    weightSP.getSelectedItem().toString(),
                    vki,
                    locateInfo.getText().toString(),
                    extras.getString("username"),
                    extras.getString("token")
            );
            editProfileHTTPRequest(editProfileModel);
        }
    }


    private void editProfileHTTPRequest(EditProfileModel editProfileModel){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        Gson gson = new Gson();
        String jsonString = gson.toJson(editProfileModel);
        try{
            JSONObject editProfileSON = new JSONObject(jsonString);
            JsonObjectRequest registerPageRequest = new JsonObjectRequest(
                    Request.Method.POST,
                    editProfile,
                    editProfileSON,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            ResponseModel responseModel = gson.fromJson(String.valueOf(response),ResponseModel.class);
                            if(responseModel.getStatus() == 200){
                                Intent intent = new Intent(EditProfileActivity.this,MainDrawerActivity.class);
                                intent.putExtra("token",extras.getString("token"));
                                intent.putExtra("username",extras.getString("username"));
                                EditProfileActivity.this.startActivity(intent);
                                EditProfileActivity.this.finish();
                            }else if(responseModel.getStatus() == 401){
                                Intent intent = new Intent(EditProfileActivity.this,LoginActivity.class);
                                EditProfileActivity.this.startActivity(intent);
                                EditProfileActivity.this.finish();
                            }
                            else
                                Toast.makeText(EditProfileActivity.this, responseModel.getBody(), Toast.LENGTH_SHORT).show();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("Rest Response : ", error.toString());
                            Toast.makeText(EditProfileActivity.this, "Servis bağlantısı başarısız!", Toast.LENGTH_SHORT).show();
                        }
                    }
            );
            requestQueue.add(registerPageRequest);
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

}