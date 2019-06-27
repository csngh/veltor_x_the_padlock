package co.veltor.veltorx;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class ClientHome extends AppCompatActivity implements LocationListener {

    private ListView listView;
    private AdminHomeAdapter listAdapter;
    private ArrayList<String> arrayList = new ArrayList<>();
    private OfflineLogs offlineLogs;
    private UserPreferences userPreferences;
    private LocationManager locationManager;
    private double currentLat, currentLong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_home);

        checkForLocationAccess();

        listAdapter = new AdminHomeAdapter(getApplicationContext(), R.layout.list_admin_home);
        listView = findViewById(R.id.listViewClientItems);
        offlineLogs = new OfflineLogs(this);
        userPreferences = new UserPreferences(this);

        String[] names = getResources().getStringArray(R.array.client_options);

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
                } else if (arrayList.get(i).contains("Padlock")) {
                    Intent intent = new Intent(ClientHome.this, PadlockActivity.class);
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
        Log.i("MYTAGS", String.valueOf(logs));

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

    private void checkForLocationAccess(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull(getSupportActionBar()).hide();
        }

        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
        }

        getLocation();
    }

    void getLocation() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            assert locationManager != null;
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5, this);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        DecimalFormat decimalFormat = new DecimalFormat("#.0");
        currentLat = location.getLatitude();
        currentLong = location.getLongitude();

        String latloc = decimalFormat.format(currentLat);
        String longloc = decimalFormat.format(currentLong);

        if (!longloc.equalsIgnoreCase(userPreferences.getLongLoc()) || !latloc.equalsIgnoreCase(userPreferences.getLatLoc())){
            Toast.makeText(getApplicationContext(), "You're not at the specified location!", Toast.LENGTH_LONG).show();
            endActivity();
        }
        
//        try {
//            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
//            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
//            locationText.setText(locationText.getText() + "\n" + addresses.get(0).getAddressLine(0) + ", " +
//                    addresses.get(0).getAddressLine(1) + ", " + addresses.get(0).getAddressLine(2));
//        } catch (Exception e) {
//
//        }

    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(ClientHome.this, "Please Enable GPS and Internet", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    private void endActivity() {
        this.finish();
    }
}
