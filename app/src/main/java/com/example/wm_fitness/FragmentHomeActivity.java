package com.example.wm_fitness;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.wm_fitness.Configurations.GlobalVariables;
import com.example.wm_fitness.Models.ProfileInfoModel;
import com.example.wm_fitness.Models.ResponseModel;
import com.example.wm_fitness.Models.UsernameModel;
import com.example.wm_fitness.Utils.GPSTracker;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class FragmentHomeActivity extends Fragment {

    GPSTracker gps;
    private TextView pUsernameTV, firstlastName,jobTV,biographyTV, heightTV, weightTV, vkiTV, textView_result, autoLocationET;
    private ImageView profilePhotoPIC;
    private Button changeBTN;
    private final String getProfileURL = GlobalVariables.API_URL + "profile/getProfile";
    private Bundle extras;
    private ProfileInfoModel profileInfoModel;
    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home,container,false);

        bindViews(view);
        autoLocation(view);
        this.extras = getActivity().getIntent().getExtras();
        getProfileHTTPRequest(new UsernameModel(extras.getString("username"),extras.getString("token")));

        changeBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getActivity(),EditProfileActivity.class);
                in.putExtra("profileInfo",profileInfoModel);
                in.putExtra("username",extras.getString("username"));
                in.putExtra("token",extras.getString("token"));
                startActivity(in);
            }
        });

        return view;
    }

    public void autoLocation(View v){
        // Create class object
        gps = new GPSTracker(getActivity());

        // Check if GPS enabled
        if(gps.canGetLocation()) {

            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();


            Geocoder geoCoder = new Geocoder(getActivity().getBaseContext(), Locale.getDefault());
            try {
                List<Address> addresses = geoCoder.getFromLocation(latitude, longitude, 1);

                if (addresses.size() > 0) {
                    autoLocationET.setText(addresses.get(0).getAdminArea() + "/" + addresses.get(0).getCountryName());
                }

            }
            catch (IOException e1) {
                e1.printStackTrace();
            }
        } else {
            gps.showSettingsAlert();
        }
    }

    private void bindViews(View v){
        changeBTN = (Button) view.findViewById(R.id.changeBtn);
        pUsernameTV = (TextView) view.findViewById(R.id.pUsernameTV);
        firstlastName = (TextView) view.findViewById(R.id.firstlastName);
        jobTV = (TextView) view.findViewById(R.id.jobTV);
        biographyTV = (TextView) view.findViewById(R.id.biographyTV);
        heightTV = (TextView) view.findViewById(R.id.heightTV);
        weightTV = (TextView) view.findViewById(R.id.weightTV);
        vkiTV = (TextView) view.findViewById(R.id.vkiTV);
        textView_result = (TextView) view.findViewById(R.id.textView_result);
        profilePhotoPIC = (ImageView) view.findViewById(R.id.profilePhotoPIC);
        autoLocationET = (TextView) view.findViewById(R.id.autoLocationET);
    }

    private void getProfileHTTPRequest(UsernameModel usernameModel) {
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        Gson gson = new Gson();
        String jsonString = gson.toJson(usernameModel);
        try{
            JSONObject usernameJSON = new JSONObject(jsonString);
            JsonObjectRequest registerPageRequest = new JsonObjectRequest(
                    Request.Method.POST,
                    getProfileURL,
                    usernameJSON,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            ResponseModel responseModel = gson.fromJson(String.valueOf(response),ResponseModel.class);
                            if(responseModel.getStatus() == 200){
                                profileInfoModel = gson.fromJson(responseModel.getBody(),ProfileInfoModel.class);
                                pUsernameTV.setText(usernameModel.getUsername());
                                firstlastName.setText(profileInfoModel.getFirstname() + " " + profileInfoModel.getLastname());
                                jobTV.setText(profileInfoModel.getJob());
                                biographyTV.setText(profileInfoModel.getBiography());
                                heightTV.setText("Boy(cm): " + profileInfoModel.getHeight());
                                weightTV.setText("Kilo(kg): " + profileInfoModel.getWeight());
                                vkiTV.setText("VKİ: " + profileInfoModel.getVki());

                                if (Float.parseFloat(profileInfoModel.getVki()) < 18.5) {
                                    textView_result.setText(R.string.you_are_thin);
                                } else if (Float.parseFloat(profileInfoModel.getVki()) < 25) {
                                    textView_result.setText(R.string.you_are_healthy);
                                } else if (Float.parseFloat(profileInfoModel.getVki()) < 30) {
                                    textView_result.setText(R.string.you_are_overweight);
                                } else {
                                    textView_result.setText(R.string.you_are_obese);
                                }
                                if(profileInfoModel.getPhoto().equals("none")){
                                    profilePhotoPIC.setBackgroundResource(R.drawable.baseline_account_box_24);
                                }else{
                                    Picasso.with(getActivity()).invalidate(GlobalVariables.API_URL + profileInfoModel.getPhoto());
                                    Picasso.with(getActivity())
                                            .load(GlobalVariables.API_URL + profileInfoModel.getPhoto())
                                            .placeholder(R.drawable.baseline_account_box_24)
                                            .fit()
                                            .centerInside()
                                            .into(profilePhotoPIC);
                                }
                            }else if(responseModel.getStatus() == 401){
                                Intent intent = new Intent(getActivity(),LoginActivity.class);
                                getActivity().startActivity(intent);
                                getActivity().finish();
                            }
                            else
                                Toast.makeText(getActivity(), responseModel.getBody(), Toast.LENGTH_SHORT).show();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("Rest Response : ", error.toString());
                            Toast.makeText(getActivity(), "Servis bağlantısı başarısız!", Toast.LENGTH_SHORT).show();
                        }
                    }
            );
            requestQueue.add(registerPageRequest);
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
}
