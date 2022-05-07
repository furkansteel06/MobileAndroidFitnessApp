package com.example.wm_fitness;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
import com.google.gson.Gson;

import org.json.JSONObject;

public class FragmentExerciseActivity extends Fragment {

    private TextView vkiInfoTV;
    private final String getProfileURL = GlobalVariables.API_URL + "profile/getExercise";
    private Bundle extras;
    private ProfileInfoModel profileInfoModel;
    Button btnExercises, btnCalendar;
    ImageView btnTraining;

    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_exercise,container,false);

        bindViews(view);
        vkiInfoTV.setVisibility(View.INVISIBLE);

        this.extras = getActivity().getIntent().getExtras();
        getProfileHTTPRequest(new UsernameModel(extras.getString("username"),extras.getString("token")));

        btnCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),CalendarActivity.class);
                startActivity(intent);
            }
        });

        btnTraining.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),DailyTrainingActivity.class);
                intent.putExtra("vki",profileInfoModel.getVki());
                startActivity(intent);
            }
        });

        btnExercises.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),ListExercises.class);
                intent.putExtra("username",extras.getString("username"));
                intent.putExtra("token",extras.getString("token"));
                intent.putExtra("vki",profileInfoModel.getVki());
                startActivity(intent);
            }
        });

        return view;

    }

    private void bindViews(View v){
        vkiInfoTV = (TextView) view.findViewById(R.id.vkiInfoTV);
        btnExercises = (Button)view.findViewById(R.id.btnExercises);
        btnCalendar = (Button)view.findViewById(R.id.btnCalendar);
        btnTraining = (ImageView)view.findViewById(R.id.btnTraining);
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
                                vkiInfoTV.setText(profileInfoModel.getVki());
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
