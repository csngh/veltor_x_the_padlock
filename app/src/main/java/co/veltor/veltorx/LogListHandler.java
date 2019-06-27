package co.veltor.veltorx;

public class LogListHandler {

    private String nickName;
    private String logTime;
    private String lockStat;
    private String lockName;

    public LogListHandler() {

    }

    public LogListHandler(String nickName, String logTime, String lockStat, String lockName) {
        this.nickName = nickName;
        this.logTime = logTime;
        this.lockStat = lockStat;
        this.lockName = lockName;
    }

    public String getLockStat() {
        return lockStat;
    }

    public void setLockStat(String lockStat) {
        this.lockStat = lockStat;
    }

    public String getLockName() {
        return lockName;
    }

    public void setLockName(String lockName) {
        this.lockName = lockName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getLogTime() {
        return logTime;
    }

    public void setLogTime(String logTime) {
        this.logTime = logTime;
    }
}
