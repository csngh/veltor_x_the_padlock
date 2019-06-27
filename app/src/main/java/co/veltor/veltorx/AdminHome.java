package co.veltor.veltorx;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdminHome extends AppCompatActivity {

    private ListView listView;
    private AdminHomeAdapter listAdapter;
    private ArrayList<String> arrayList = new ArrayList<>();
    private OfflineLogs offlineLogs;
    private UserPreferences userPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        listAdapter = new AdminHomeAdapter(getApplicationContext(), R.layout.list_admin_home);
        listView = findViewById(R.id.listViewHomeItems);
        offlineLogs = new OfflineLogs(this);
        userPreferences = new UserPreferences(this);

        userPreferences.setLockName("Padlock01");

        String[] names = getResources().getStringArray(R.array.admin_options);

        for (String x : names) {
            AdminHomeHandler adminHomeHandler = new AdminHomeHandler(x);
            arrayList.add(adminHomeHandler.getOptionName());
            listAdapter.add(adminHomeHandler);
        }

        listView.setAdapter(listAdapter);

        if (!offlineLogs.getUserLogs().equalsIgnoreCase("")) {
            AlertDialog.Builder alertDialogBuilderex = new AlertDialog.Builder(this);
            alertDialogBuilderex.setTitle("You have pending logs! Please update!");
            alertDialogBuilderex
                    .setCancelable(false)
                    .setPositiveButton("Okay",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int arg1) {
                                    if (!checkInternetConnection()) {
                                        Toast.makeText(getApplicationContext(), "Sorry, you're not connected to internet!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        updateAllLogs(1);
                                    }
                                }
                            });
            AlertDialog alertDialogex = alertDialogBuilderex.create();
            alertDialogex.show();
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if (arrayList.get(i).contains("WiFi")) {
                    Toast.makeText(getApplicationContext(), "Feature in progress!", Toast.LENGTH_SHORT).show();
                } else if (arrayList.get(i).contains("Client Logs")) {
                    Intent intent = new Intent(AdminHome.this, LogsActivity.class);
                    startActivity(intent);
                } else if (arrayList.get(i).contains("Padlock")) {
                    Intent intent = new Intent(AdminHome.this, PadlockActivity.class);
                    startActivity(intent);
                } else if (arrayList.get(i).contains("Share")) {
                    Intent intent = new Intent(AdminHome.this, ShareAccessActivity.class);
                    startActivity(intent);
                } else if (arrayList.get(i).contains("Update")) {
                    if (offlineLogs.getUserLogs().equalsIgnoreCase("")) {
                        Toast.makeText(getApplicationContext(), "No logs found!", Toast.LENGTH_SHORT).show();
                    } else {
                        if (!checkInternetConnection()) {
                            Toast.makeText(getApplicationContext(), "Sorry, you're not connected to internet!", Toast.LENGTH_SHORT).show();
                        } else {
                            updateAllLogs(1);
                        }
                    }
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                AlertDialog.Builder alertDialogBuilderex = new AlertDialog.Builder(this);
                alertDialogBuilderex.setTitle("Sure to logout?");
                alertDialogBuilderex
                        .setCancelable(false)
                        .setPositiveButton("Yes",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int arg1) {
                                        userPreferences.removeAllWithLogout();
                                        offlineLogs.removeAllLogs();
                                        endActivity();
                                    }
                                })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int arg1) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialogex = alertDialogBuilderex.create();
                alertDialogex.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateAllLogs(final int count) {

        final String logs[] = offlineLogs.getUserLogs().split("%");

        String url = "http://192.168.0.5/veltorx/add_logs.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                int newCount = count + 1;
                if (newCount < logs.length) {
                    updateAllLogs(newCount);
                } else {
                    Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                    offlineLogs.removeAllLogs();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Response error!", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("admin_email", logs[count].split(",")[0]);
                params.put("admin_password", logs[count].split(",")[1]);
                params.put("client_name", logs[count].split(",")[2]);
                params.put("lock_status", logs[count].split(",")[3]);
                params.put("lock_name", "Padlock01");
                params.put("date_time", logs[count].split(",")[5]);
                return params;
            }
        };
        MySingleton.getmInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    private boolean checkInternetConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnected();
    }

    @Override
    public void onBackPressed() {
        if (offlineLogs.getUserLogs().equalsIgnoreCase("")) {
            AlertDialog.Builder alertDialogBuilderex = new AlertDialog.Builder(this);
            alertDialogBuilderex.setTitle("Sure to exit?");
            alertDialogBuilderex
                    .setCancelable(false)
                    .setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int arg1) {
                                    endActivity();
                                }
                            })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int arg1) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alertDialogex = alertDialogBuilderex.create();
            alertDialogex.show();
        } else {
            Toast.makeText(getApplicationContext(), "Please update system logs first!", Toast.LENGTH_SHORT).show();
        }
    }

    private void endActivity() {
        this.finish();
    }
}
