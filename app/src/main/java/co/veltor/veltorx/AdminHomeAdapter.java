package co.veltor.veltorx;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class AdminHomeAdapter extends ArrayAdapter {

    private List<Object> list = new ArrayList<>();

    AdminHomeAdapter(@NonNull Context context, @LayoutRes int resource) {
        super(context, resource);
    }

    @Override
    public void add(@Nullable Object object) {
        super.add(object);
        list.add(object);
    }

    @Override
    public int getCount() {
        return this.list.size();
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return this.list.get(position);
    }

    private static class DataHandler {
        private TextView tvOptionName;
        private ImageView imgOptionImage;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        final DataHandler handler = new DataHandler();

        LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        convertView = inflater.inflate(R.layout.list_admin_home, parent, false);
        handler.tvOptionName = convertView.findViewById(R.id.tvOptionName);
        handler.imgOptionImage = convertView.findViewById(R.id.imgOptionID);
        convertView.setTag(handler);

        final AdminHomeHandler adminHomeHandler;
        adminHomeHandler = (AdminHomeHandler) this.getItem(position);

        assert adminHomeHandler != null;
        handler.tvOptionName.setText(adminHomeHandler.getOptionName());

        if(adminHomeHandler.getOptionName().contains("WiFi")){
            handler.imgOptionImage.setImageResource(R.drawable.wifipass);
        }else if(adminHomeHandler.getOptionName().contains("Logs")){
            handler.imgOptionImage.setImageResource(R.drawable.logs);
        }else if(adminHomeHandler.getOptionName().contains("Padlock")){
            handler.imgOptionImage.setImageResource(R.drawable.padlock);
        }else if(adminHomeHandler.getOptionName().contains("Share")){
            handler.imgOptionImage.setImageResource(R.drawable.share);
        }

        return convertView;
    }
}
