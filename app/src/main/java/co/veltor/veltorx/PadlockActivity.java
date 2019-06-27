package co.veltor.veltorx;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

public class PadlockActivity extends AppCompatActivity {

    private UserPreferences userPreferences;
    private OfflineLogs offlineLogs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_padlock);
        setTitle("Access Padlock");

        offlineLogs = new OfflineLogs(this);
        userPreferences = new UserPreferences(this);

        findViewById(R.id.bLock).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String url = "http://192.168.0.5/lock";
//                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
//                    }
//                }, new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(getApplicationContext(), "Failed!", Toast.LENGTH_SHORT).show();
//                    }
//                });
//                MySingleton.getmInstance(getApplicationContext()).addToRequestQueue(stringRequest);
                Toast.makeText(getApplicationContext(), "Locking...", Toast.LENGTH_SHORT).show();
                saveLogs("LOCKED");
            }
        });

        findViewById(R.id.bUnlock).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String url = "http://192.168.0.5/unlock";
//                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
//                    }
//                }, new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(getApplicationContext(), "Failed!", Toast.LENGTH_SHORT).show();
//                    }
//                });
//                MySingleton.getmInstance(getApplicationContext()).addToRequestQueue(stringRequest);
                Toast.makeText(getApplicationContext(), "Unlocking...", Toast.LENGTH_SHORT).show();
                saveLogs("UNLOCKED");
            }
        });
    }

    private void saveLogs(String lockStatus){
        String lockName = userPreferences.getLockName();
        String userName = userPreferences.getNickName();
        String email = userPreferences.getemailID();
        String password = userPreferences.getPassword();
        String dateTime = "no time";

        String log = email + "," + password + "," + userName + "," + lockStatus + "," + lockName + "," + dateTime;
        offlineLogs.setUserLogs(log);
    }
}
