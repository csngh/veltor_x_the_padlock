package co.veltor.veltorx;

import android.content.Context;
import android.content.SharedPreferences;

public class OfflineLogs {
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private Context mCtx;

    private String userLogs;

    public OfflineLogs(Context context){
        this.mCtx = context;
    }

    public void removeAllLogs() {
        preferences = (mCtx).getSharedPreferences("userLogs", Context.MODE_PRIVATE);
        preferences.edit().clear().apply();
    }

    public String getUserLogs() {
        preferences = (mCtx).getSharedPreferences("userLogs", Context.MODE_PRIVATE);
        userLogs = preferences.getString("log", "");
        return userLogs;
    }

    public void setUserLogs(String userLogs) {
        preferences = (mCtx).getSharedPreferences("userLogs", Context.MODE_PRIVATE);
        editor = preferences.edit();
        String previousLogs = preferences.getString("log", "");
        editor.putString("log", previousLogs + "%" + userLogs);
        editor.apply();
    }
}
