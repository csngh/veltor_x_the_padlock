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

public class LogListAdapter extends ArrayAdapter {

    private List<Object> list = new ArrayList<>();

    LogListAdapter(@NonNull Context context, @LayoutRes int resource) {
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
        private TextView tvLogNickName;
        private TextView tvLogTime;
        private ImageView imgLockStat;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        final DataHandler handler = new DataHandler();

        LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        convertView = inflater.inflate(R.layout.list_logs, parent, false);
        handler.tvLogNickName = convertView.findViewById(R.id.tvLogNickName);
        handler.tvLogTime = convertView.findViewById(R.id.tvLogTime);
        handler.imgLockStat = convertView.findViewById(R.id.imgLockStat);
        convertView.setTag(handler);

        final LogListHandler logListHandler;
        logListHandler = (LogListHandler) this.getItem(position);

        assert logListHandler != null;
        handler.tvLogNickName.setText(logListHandler.getNickName() + ", " + logListHandler.getLockName());
        handler.tvLogTime.setText(logListHandler.getLockStat() + ", " + logListHandler.getLogTime());

        if(logListHandler.getLockStat().equalsIgnoreCase("LOCKED")){
            handler.imgLockStat.setImageResource(R.drawable.ic_lock);
        }else if(logListHandler.getLockStat().equalsIgnoreCase("UNLOCKED")){
            handler.imgLockStat.setImageResource(R.drawable.ic_unlock);
        }

        return convertView;
    }

}
