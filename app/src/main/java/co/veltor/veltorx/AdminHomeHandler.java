package co.veltor.veltorx;

public class AdminHomeHandler {
    private String optionName;

    public AdminHomeHandler(){
    }

    public AdminHomeHandler(String optionName) {
        this.optionName = optionName;
    }

    public String getOptionName() {
        return optionName;
    }

    public void setOptionName(String optionName) {
        this.optionName = optionName;
    }
}
