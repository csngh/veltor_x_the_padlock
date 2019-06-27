package co.veltor.veltorx;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LogsActivity extends AppCompatActivity {

    private ListView listView;
    private LogListAdapter listAdapter;
    private ArrayList<LogListHandler> arrayList = new ArrayList<>();
    private UserPreferences userPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logs);
        setTitle("User Logs");

        listAdapter = new LogListAdapter(getApplicationContext(), R.layout.list_logs);
        listView = findViewById(R.id.listViewLogs);
        userPreferences = new UserPreferences(this);

        String url = "http://192.168.0.5/veltorx/fetch_logs.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String nickName = jsonObject.getString("operator_name");
                        String logTime = jsonObject.getString("time_of_update");
                        String lockName = jsonObject.getString("lock_name");
                        String lockStat = jsonObject.getString("lock_status");
                        LogListHandler logListHandler = new LogListHandler(nickName, logTime, lockStat, lockName);
                        arrayList.add(logListHandler);
                    }
                    setupList();
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(), "Fetching error!", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Response error!", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("admin_email", userPreferences.getemailID());
                return params;
            }
        };
        MySingleton.getmInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    private void setupList(){
        for(LogListHandler logListHandler : arrayList){
            listAdapter.add(logListHandler);
        }
        listView.setAdapter(listAdapter);
    }
}
