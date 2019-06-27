package co.veltor.veltorx;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class ACSelectActivity extends AppCompatActivity {

    private ScanQRCode scanQRCode;
    private VeltorEncryptDecrypt ved;
    private UserPreferences userPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Select Priority");

        userPreferences = new UserPreferences(this);

        //Check for login
        final UserPreferences userPreferences = new UserPreferences(this);
        if(userPreferences.isLoggedin()){
            //Get login type
            //For admin
            if(userPreferences.getLoginType().equalsIgnoreCase("ADMIN")){
                Intent adminHomePage = new Intent(ACSelectActivity.this, AdminHome.class);
                startActivity(adminHomePage);
                endActivity();
            }//For client
            else if(userPreferences.getLoginType().equalsIgnoreCase("CLIENT")) {
                Intent clientHomePage = new Intent(ACSelectActivity.this, ClientHome.class);
                startActivity(clientHomePage);
                endActivity();
            }
        }

        //Initiate all objects
        scanQRCode = new ScanQRCode(this);
        ved = new VeltorEncryptDecrypt();

        //Event call for admin
        findViewById(R.id.bAdmin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userPreferences.setLoginType("ADMIN");
                scanQRCode.initiateQRCodeScan("Please scan the QRCode from product's back!");
            }
        });
        //Event call for client
        findViewById(R.id.bClient).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userPreferences.setLoginType("CLIENT");
                scanQRCode.initiateQRCodeScan("Please scan the QRCode from admin's phone!");
            }
        });
    }

    //Validate QRCode
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            //If QRScan in cancelled
            if (result.getContents() == null) {
                Toast.makeText(getApplicationContext(), "Cancelled", Toast.LENGTH_SHORT).show();
            }//Scanned QRCode
            else {
                try {
                    String finalVal = result.getContents();
                    Log.i("MYTAGS", result.getContents());
                    //Checking for qenuine QRCode
                    if(userPreferences.getLoginType().equalsIgnoreCase("ADMIN")) {
                        if (!finalVal.contains("P:") || !finalVal.contains("D:") || !finalVal.contains("VELTORX01")) {
                            Toast.makeText(getApplicationContext(), "Invalid QRCode!", Toast.LENGTH_SHORT).show();
                        } else {
                            Intent registerPage = new Intent(ACSelectActivity.this, RegisterAdminDetails.class);
                            startActivity(registerPage);
                            endActivity();
                        }
                    }else if(userPreferences.getLoginType().equalsIgnoreCase("CLIENT")){
                        if(!finalVal.contains("D:") || !finalVal.contains("E:") || !finalVal.contains("P:")|| !finalVal.contains("W:")||
                                !finalVal.contains("L:")|| !finalVal.contains("C:") || !finalVal.contains("VELTORX01")){
                            Toast.makeText(getApplicationContext(), "Invalid QRCode!", Toast.LENGTH_SHORT).show();
                        }else {
                            userPreferences.setemailID(finalVal.substring(finalVal.indexOf("E:") + 2, finalVal.indexOf("P:")));
                            userPreferences.setPassword(finalVal.substring(finalVal.indexOf("P:") + 2, finalVal.indexOf("W:")));
                            userPreferences.setNickName(finalVal.substring(finalVal.indexOf("C:") + 2, finalVal.indexOf("A:")));
                            userPreferences.setLockName(finalVal.substring(finalVal.indexOf("L:") + 2, finalVal.indexOf("C:")));
                            userPreferences.setLatLoc(finalVal.substring(finalVal.indexOf("A:") + 2, finalVal.indexOf("O:")));
                            userPreferences.setLongLoc(finalVal.substring(finalVal.indexOf("O:") + 2));
                            userPreferences.setLoggedin(true);
                            Intent clientHome = new Intent(ACSelectActivity.this, ClientHome.class);
                            startActivity(clientHome);
                            endActivity();
                            Log.i("MYTAGS", userPreferences.getemailID() + ", " + userPreferences.getPassword() +
                            ", " + userPreferences.getNickName() + ", " + userPreferences.getLockName());
                        }
                    }
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(), "Invalid QRCode!", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void endActivity(){
        this.finish();
    }

}

