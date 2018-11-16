package com.example.mico.ubcardtracker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private JSONArray jsonArray;
    private EditText IDNumber,Password;
    private Button Login;
    private LinearLayout LoginLayout,LoadingLayout;
    private String id,name,role,company,email;
    private TextView ShowPassword;
    private boolean loggedIn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        LoginLayout = findViewById(R.id.login_layout);
        LoadingLayout = findViewById(R.id.loading_layout);
        IDNumber = findViewById(R.id.et_id_number);
        Password = findViewById(R.id.et_password);
        Login = findViewById(R.id.btn_login);
        ShowPassword = findViewById(R.id.tv_show);

        Login.setOnClickListener(LoginActivity.this);

        Password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(Password.getText().length() > 0)
                {
                    ShowPassword.setVisibility(View.VISIBLE);
                }
                else{
                    ShowPassword.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        ShowPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ShowPassword.getText() == "Show")
                {
                    ShowPassword.setText("Hide");
                    Password.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    Password.setSelection(Password.length());
                }
                else
                {
                    ShowPassword.setText("Show");
                    Password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    Password.setSelection(Password.length());
                }
            }
        });
    }
    @Override
    public void onClick(View v) {
        if(v == Login)
        {
            if(IDNumber.getText().length() == 0)
            {
                IDNumber.setError("Please Enter your ID Number");
            }
            else if(Password.getText().length() < 5)
            {
                Password.setError("Password must atleast 5 characters");
            }
            else
            {
                login();
                HideLogin();
            }

        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME,Context.MODE_PRIVATE);
        loggedIn = sharedPreferences.getBoolean(Config.LOGGEDIN_SHARED_PREF, false);

        if(loggedIn){
            String role = sharedPreferences.getString(Config.ROLE_SHARED_PREF, "null");

            if(role.contentEquals("Courier"))
            {
                Intent intent = new Intent(LoginActivity.this, CourierMainActivity.class);
                startActivity(intent);
                finish();
            }
            else
            {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }

        }
    }
    private void login(){
        final String id_number = IDNumber.getText().toString();
        final String password = Password.getText().toString();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.LOGIN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equalsIgnoreCase("Login_Successfully")){
                            sharedPreferences = LoginActivity.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                            editor = sharedPreferences.edit();
                            editor.putBoolean(Config.LOGGEDIN_SHARED_PREF, true);
                            editor.putString(Config.ID_SHARED_PREF,id_number);
                            editor.commit();
                            getInfo();
                        }
                        else{
                            ShowLogin();
                            Toast.makeText(LoginActivity.this, "Incorrect ID or Password", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ShowLogin();
                        Toast.makeText(LoginActivity.this,"No Internet Connection",Toast.LENGTH_SHORT).show();
                        Log.wtf("Error",error);
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("id_number",id_number);
                params.put("password", password);

                return params;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        stringRequest.setShouldCache(false);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    private void getInfo()
    {
        SharedPreferences sharedPreferences = this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        id = sharedPreferences.getString(Config.ID_SHARED_PREF,"null");
        final String url = Config.SELECT_INFO_URL;
        final RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.wtf("id",id);
                        insertInfo(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }

                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("id_number",id);
                return params;
            }};
        requestQueue.add(stringRequest);
    }
    private void insertInfo(String response)
    {
        try {
            Log.wtf("insert", "Response: " + response);
            JSONObject jsonObject = new JSONObject(response);
            jsonArray = jsonObject.getJSONArray("about");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject JsonObject = jsonArray.getJSONObject(i);
                name = JsonObject.getString("UserName");
                role = JsonObject.getString("UserRole");
                company = JsonObject.getString("UserCompany");
                email = JsonObject.getString("UserEmail");

                sharedPreferences = LoginActivity.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                editor = sharedPreferences.edit();
                editor.putString(Config.NAME_SHARED_PREF, name);
                editor.putString(Config.ROLE_SHARED_PREF, role);
                editor.putString(Config.COMPANY_SHARED_PREF, company);
                editor.putString(Config.EMAIL_SHARED_PREF, email);
                editor.commit();

                if(role.contentEquals("Courier"))
                {
                    Intent intent = new Intent(LoginActivity.this, CourierMainActivity.class);
                    startActivity(intent);
                    finish();
                }
                else{
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                }
            }
        }
        catch (Exception ee){
            Toast.makeText(this,ee.getMessage(),Toast.LENGTH_SHORT).show();
            Log.wtf("insert (catch)","Exception : "+ee.getMessage());
        }
    }
    private void HideLogin()
    {
        LoadingLayout.setVisibility(View.VISIBLE);
        LoginLayout.setVisibility(View.GONE);
    }
    private void ShowLogin()
    {
        LoadingLayout.setVisibility(View.GONE);
        LoginLayout.setVisibility(View.VISIBLE);
    }

}
