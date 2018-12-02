package com.example.user.doggy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class listadapter extends BaseAdapter {
    private ArrayList<route_model> listViewItemList = new ArrayList<>();
    LayoutInflater inflater;
    Context context;
    int layout;
    ArrayList<route_model> list;

    public listadapter(Context context, int resource, ArrayList<route_model> listViewItemList) {
        this.context = context;
        this.layout = resource;
        this.list = listViewItemList;

        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount(){
        return listViewItemList.size();
    }

    @Override
    public long getItemId(int position) {
        return position ;
    }

    // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position) ;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent ){
        final int pos = position;

        if (convertView == null) {

            convertView = inflater.inflate(R.layout.item_list, parent, false);
        }

        TextView name = (TextView) convertView.findViewById(R.id.textView_list_name);
        TextView time = (TextView) convertView.findViewById(R.id.textView_list_time);
        TextView memo = (TextView) convertView.findViewById(R.id.textView_list_memo);
        TextView datetime = (TextView) convertView.findViewById(R.id.textView_list_datetime);

        route_model model = listViewItemList.get(position);

        name.setText(model.getName());
        time.setText(model.getTime());
        memo.setText(model.getMemo());
        datetime.setText(model.getDatetime());

        return convertView;
    }

    public void setList(ArrayList<route_model> list) {
        this.listViewItemList = list;
    }


}

