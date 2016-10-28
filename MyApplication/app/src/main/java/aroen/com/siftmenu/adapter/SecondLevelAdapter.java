package aroen.com.siftmenu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import aroen.com.siftmenu.R;


public class SecondLevelAdapter extends BaseAdapter {
    private List<String> list;
    // 上下文
    private Context context;
    // 用来导入布局
    private LayoutInflater inflater = null;

    // 构造器
    public SecondLevelAdapter(List<String> list, Context context) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.second_level_big, parent, false);
            holder.tv = (TextView) convertView.findViewById(R.id.second_textview);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv.setText(list.get(position));
        return convertView;
    }

    public static class ViewHolder {
        TextView tv;
    }

    public void reloadListView(List<String> data) {
        list.clear();
        list.addAll(data);
        notifyDataSetChanged();
    }
}