package co.veltor.veltorx;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterAdminDetails extends AppCompatActivity {

    private EditText etName, etEmail, etPassword;
    private UserPreferences userPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_admin_details);
        setTitle("Register");

        etName = findViewById(R.id.editNickName);
        etEmail = findViewById(R.id.editEmailID);
        etPassword = findViewById(R.id.editPassword);
        userPreferences = new UserPreferences(this);

        findViewById(R.id.bRegister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String adminName = etName.getText().toString();
                final String adminEmail = etEmail.getText().toString();
                final String adminPassword = etPassword.getText().toString();

                if(adminName.equalsIgnoreCase("") || adminEmail.equalsIgnoreCase("") ||
                        adminPassword.equalsIgnoreCase("")){
                    Toast.makeText(getApplicationContext(), "Details can't be empty!", Toast.LENGTH_SHORT).show();
                }else if(adminName.length() < 3){
                    etName.setError("Can't be smaller than 3 characters!");
                }else if(!adminEmail.contains("@") || !(adminEmail.contains(".com") || adminEmail.contains(".in"))){
                    etEmail.setError("Incorrect Email!");
                }else if(adminPassword.length() < 8){
                    etPassword.setError("Password should be minimum 8 chars!");
                }else {
                    String registerUrl = "http://192.168.0.5/veltorx/register_admin.php";
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, registerUrl, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                JSONObject jsonObject = jsonArray.getJSONObject(0);
                                String code = jsonObject.getString("code");
                                String message = jsonObject.getString("message");
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                                if(code.equalsIgnoreCase("reg_success")){
                                    userPreferences.setNickName(adminName);
                                    userPreferences.setemailID(adminEmail);
                                    userPreferences.setPassword(adminPassword);
                                    userPreferences.setLoginType("ADMIN");
                                    userPreferences.setLoggedin(true);
                                    Intent adminHome = new Intent(RegisterAdminDetails.this, AdminHome.class);
                                    startActivity(adminHome);
                                    endActivity();
                                }

                            }catch (Exception e){
                                e.printStackTrace();
                                Toast.makeText(getApplicationContext(), "Error registering !!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), "No server response !!", Toast.LENGTH_SHORT).show();
                        }
                    }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            params.put("admin_email", adminEmail);
                            params.put("admin_password", adminPassword);
                            return params;
                        }
                    };
                    MySingleton.getmInstance(getApplicationContext()).addToRequestQueue(stringRequest);
                }
            }
        });
    }

    private void endActivity(){
        this.finish();
    }
}
