package co.veltor.veltorx;

import android.content.Context;
import android.content.SharedPreferences;

public class UserPreferences {

    //Functions
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private Context mCtx;
    //Data types
    private boolean isLoggedin;
    private String emailID, nickName, loginType, password, lockName, latLoc, longLoc;
    private int product_id;

    public UserPreferences(Context context) {
        this.mCtx = context;
    }

    public UserPreferences() {
    }

    public void removeAllWithLogout() {
        preferences = (mCtx).getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        preferences.edit().clear().apply();
        preferences = (mCtx).getSharedPreferences("logininfo", Context.MODE_PRIVATE);
        preferences.edit().clear().apply();
    }

    public int getProduct_id() {
        preferences = (mCtx).getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        product_id = preferences.getInt("product_id", 0);
        return product_id;
    }

    public void setProduct_id(int product_id) {
        preferences = (mCtx).getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        editor = preferences.edit();
        editor.putInt("product_id", product_id);
        editor.apply();
    }

    public boolean isLoggedin() {
        preferences = (mCtx).getSharedPreferences("logininfo", Context.MODE_PRIVATE);
        isLoggedin = preferences.getBoolean("islogin", false);
        return isLoggedin;
    }

    public void setLoggedin(boolean loggedin) {
        preferences = (mCtx).getSharedPreferences("logininfo", Context.MODE_PRIVATE);
        editor = preferences.edit();
        editor.putBoolean("islogin", loggedin);
        editor.apply();
    }

    public String getemailID() {
        preferences = (mCtx).getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        emailID = preferences.getString("emailid", "");
        return emailID;
    }

    public void setemailID(String emailID) {
        preferences = (mCtx).getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        editor = preferences.edit();
        editor.putString("emailid", emailID);
        editor.apply();
    }

    public String getLockName() {
        preferences = (mCtx).getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        lockName = preferences.getString("lockName", "");
        return lockName;
    }

    public void setLockName(String lockName) {
        preferences = (mCtx).getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        editor = preferences.edit();
        editor.putString("lockName", lockName);
        editor.apply();
    }

    public String getPassword() {
        preferences = (mCtx).getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        password = preferences.getString("password", "");
        return password;
    }

    public void setPassword(String pass) {
        preferences = (mCtx).getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        editor = preferences.edit();
        editor.putString("password", pass);
        editor.apply();
    }

    public String getNickName() {
        preferences = (mCtx).getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        nickName = preferences.getString("nickName", "");
        return nickName;
    }

    public void setNickName(String nickName) {
        preferences = (mCtx).getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        editor = preferences.edit();
        editor.putString("nickName", nickName);
        editor.apply();
    }

    public String getLoginType() {
        preferences = (mCtx).getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        loginType = preferences.getString("logintype", "");
        return loginType;
    }

    public void setLoginType(String loginType) {
        preferences = (mCtx).getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        editor = preferences.edit();
        editor.putString("logintype", loginType);
        editor.apply();
    }

    public String getLatLoc() {
        preferences = (mCtx).getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        latLoc = preferences.getString("latloc", "");
        return latLoc;
    }

    public void setLatLoc(String latLoc) {
        preferences = (mCtx).getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        editor = preferences.edit();
        editor.putString("latloc", latLoc);
        editor.apply();
    }

    public String getLongLoc() {
        preferences = (mCtx).getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        longLoc = preferences.getString("longloc", "");
        return longLoc;
    }

    public void setLongLoc(String longLoc) {
        preferences = (mCtx).getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        editor = preferences.edit();
        editor.putString("longloc", longLoc);
        editor.apply();
    }
}
