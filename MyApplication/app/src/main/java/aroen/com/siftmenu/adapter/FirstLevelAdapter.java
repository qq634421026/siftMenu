package aroen.com.siftmenu.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import aroen.com.siftmenu.R;

public class FirstLevelAdapter extends BaseAdapter {
    private List<String> list;
    // 用来控制CheckBox的选中状况
    private HashMap<Integer, Boolean> isSelected;
    // 上下文
    private Context context;
    // 用来导入布局
    private LayoutInflater inflater = null;

    // 构造器
    public FirstLevelAdapter(List<String> list, Context context,
                             HashMap<Integer, Boolean> isSelected) {
        this.context = context;
        this.list = list;
        this.isSelected = isSelected;
        inflater = LayoutInflater.from(context);
        isSelected = new HashMap<Integer, Boolean>();
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
            convertView = inflater.inflate(R.layout.first_level, parent, false);

            holder.tv = (TextView) convertView.findViewById(R.id.first_textview);
            holder.cb = (View) convertView.findViewById(R.id.first_level_leftview);
            holder.first_root = (RelativeLayout) convertView.findViewById(R.id.first_root);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv.setText(list.get(position));

        if (isSelected.get(position)) {
            holder.first_root.setBackgroundColor(Color.parseColor("#ffffff"));
            holder.cb.setVisibility(View.VISIBLE);
        } else {
            holder.first_root.setBackgroundColor(Color.parseColor("#ededed"));
            holder.cb.setVisibility(View.INVISIBLE);
        }
        return convertView;
    }

    public static class ViewHolder {
        TextView tv;
        View cb;
        RelativeLayout first_root;
    }

    public void reloadListView(List<String> data,
                               HashMap<Integer, Boolean> selected) {
        list.clear();
        isSelected.clear();
        list.addAll(data);
        isSelected.putAll(selected);
        notifyDataSetChanged();
    }
}