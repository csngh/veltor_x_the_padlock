package co.veltor.veltorx;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class ShareAccessActivity extends AppCompatActivity {

    private Bitmap bitmap;
    private ImageView imgQr;
    private UserPreferences userPreferences;
    private String clientName;
    private String latLoc, longLoc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_access);
        setTitle("Share Access");

        imgQr = findViewById(R.id.imgQR);
        userPreferences = new UserPreferences(this);

        showNickNameSetDialog();
    }

    private void generateQR(String qrText) {
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(qrText, BarcodeFormat.QR_CODE, 200, 200);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            bitmap = barcodeEncoder.createBitmap(bitMatrix);
            imgQr.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    private void showNickNameSetDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.custom_dialog_nickname, null);
        dialogBuilder.setView(dialogView);

        final EditText edt = dialogView.findViewById(R.id.editClientName);
        final EditText edtLat = dialogView.findViewById(R.id.editClientLat);
        final EditText edtLong = dialogView.findViewById(R.id.editClientLong);

        dialogBuilder.setTitle("Enter client name");
        dialogBuilder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                endActivity();
            }
        });
        dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                clientName = edt.getText().toString();
                latLoc = edtLat.getText().toString();
                longLoc = edtLong.getText().toString();

                if(clientName.length() < 3){
                    Toast.makeText(getApplicationContext(), "Name should be greater than 3 characters!", Toast.LENGTH_SHORT).show();
                    endActivity();
                }else if(!latLoc.contains(".") || !longLoc.contains(".")){
                    Toast.makeText(getApplicationContext(), "Location should be a number with 2 decimal places!", Toast.LENGTH_SHORT).show();
                    endActivity();
                }
                else {
                    generateQR("D:VELTORX01" +
                            "E:" + userPreferences.getemailID() +
                            "P:" + userPreferences.getPassword() +
                            "W:WIFIPASSWORD" +
                            "L:" + userPreferences.getLockName() +
                            "C:" + clientName +
                            "A:" + latLoc +
                            "O:" + longLoc);
                }
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }

    private void endActivity(){
        this.finish();
    }
}
