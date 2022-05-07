package com.example.wm_fitness;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.example.wm_fitness.Configurations.GlobalVariables;
import com.example.wm_fitness.Models.ResponseModel;
import com.example.wm_fitness.Models.User;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameET,passwordET;
    private Button loginBTN;
    private final String loginURL = GlobalVariables.API_URL + "auth/login";
    private TextView LoginAndRegisterET;


    private void bindViews(){
        usernameET = findViewById(R.id.usernameET);
        passwordET = findViewById(R.id.passwordET);
        LoginAndRegisterET = findViewById(R.id.loginregisterET);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        bindViews();

        String lgnrgstret = "Bir hesabın yokmu ?   Kayıt Ol";
        SpannableString spns = new SpannableString(lgnrgstret);

        ClickableSpan clickableSpan1 = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        };


        spns.setSpan(clickableSpan1,22,30, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        LoginAndRegisterET.setText(spns);
        LoginAndRegisterET.setMovementMethod(LinkMovementMethod.getInstance());

    }

    private boolean isEmpty(){
        if(usernameET.getText().toString().trim().equals("") || passwordET.getText().toString().trim().equals(""))
            return true;
        return false;
    }
    public void loginBTNOnClickHandler(View view){
        if(isEmpty()){
            Toast.makeText(this, "Kullanıcı adı/parola boş geçilemez!", Toast.LENGTH_SHORT).show();
        }else{
            User user = new User(usernameET.getText().toString(),passwordET.getText().toString());
            loginPageHTTPRequest(user);
        }
    }

    public void registerViewBTNOnClickHandler(View view){
        Intent intent = new Intent(this,RegisterActivity.class);
        startActivity(intent);
        finish();
    }

    private void loginPageHTTPRequest(User user){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        Gson gson = new Gson();
        String jsonString = gson.toJson(user);
        try{
            JSONObject userJSON = new JSONObject(jsonString);
            JsonObjectRequest loginPageRequest = new JsonObjectRequest(
                    Request.Method.POST,
                    loginURL,
                    userJSON,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            ResponseModel responseModel = gson.fromJson(String.valueOf(response),ResponseModel.class);
                            if(responseModel.getStatus() == 200){
                                String token = responseModel.getBody();
                                Intent intent = new Intent(LoginActivity.this,MainDrawerActivity.class);
                                intent.putExtra("token",token);
                                intent.putExtra("username",user.getUsername());
                                LoginActivity.this.startActivity(intent);
                                LoginActivity.this.finish();
                            }
                            else
                                Toast.makeText(LoginActivity.this, responseModel.getBody(), Toast.LENGTH_SHORT).show();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("Rest Response : ", error.toString());
                            Toast.makeText(LoginActivity.this, "Servis bağlantısı başarısız!", Toast.LENGTH_SHORT).show();
                        }
                    }
            );
            requestQueue.add(loginPageRequest);
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
}