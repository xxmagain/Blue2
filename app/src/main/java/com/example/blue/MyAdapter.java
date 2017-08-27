package com.example.blue;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by magin on 2017/7/24.
 */

public class MyAdapter extends BaseAdapter {
    private ArrayList<BluetoothDevice>btdevices;
    private LayoutInflater mInflator;
    private Activity mContext;

    public MyAdapter(Activity c){
        super();
        mContext = c;
        btdevices = new ArrayList<BluetoothDevice>();
        mInflator = mContext.getLayoutInflater();
    }
    public void clear(){
        btdevices.clear();
    }
    public void addDevice(BluetoothDevice device){
        if(device == null)
            return;
        for(int i = 0;i<btdevices.size();i++){
            String btAddress = btdevices.get(i).getAddress();
            if(btAddress.equals(device.getAddress())){
                btdevices.add(i+1,device);
                btdevices.remove(i);
                return;
            }
        }
        btdevices.add(device);
    }
    public BluetoothDevice getDevice(int position){
        return btdevices.get(position);
    }
    @Override
    public int getCount(){
        return btdevices.size();
    }
    @Override
    public Object getItem(int i){
        return btdevices.get(i);
    }
    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if(view==null){
            view = mInflator.inflate(R.layout.item,null);
            holder = new ViewHolder();
            holder.deviceAdress = (TextView)view.findViewById(R.id.adress);
            holder.deviceName = (TextView)view.findViewById(R.id.name);
            view.setTag(holder);
        }else {
            holder = (ViewHolder)view.getTag();
        }
        BluetoothDevice device = btdevices.get(i);
        final String deviceName = device.getName();
        if(deviceName!=null&&deviceName.length()>0)
            holder.deviceName.setText("名称:"+deviceName);
        else
            holder.deviceName.setText("未知名称");
        holder.deviceAdress.setText("地址："+device.getAddress());
        if(i%2==0)
        {
            view.setBackgroundColor(Color.argb(25,255,0,0));
        }else {
            view.setBackgroundColor(Color.argb(25,0,255,0));
        }
        return view;
    }
    class ViewHolder{
        TextView deviceName;
        TextView deviceAdress;
    }
}

