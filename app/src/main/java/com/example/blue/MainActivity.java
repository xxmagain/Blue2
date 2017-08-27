package com.example.blue;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    //定义一个蓝牙适配器，以便获取蓝牙设备的信息
    BluetoothAdapter bluetoothAdapter;
    //所有扫描的蓝牙设备
//   bluetoothAdapter List<String>devices = new ArrayList<>();
//    ArrayAdapter<String>adapter;
    MyAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //获取所有界面的组件
        Button open = (Button)findViewById(R.id.open);
        Button scan = (Button)findViewById(R.id.scan);
        Button show = (Button)findViewById(R.id.show);
        ListView Lv = (ListView)findViewById(R.id.lv);
        //获取蓝牙适配器
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        //创建一个适配器
//        adapter = new ArrayAdapter<String>(this,R.layout.item,devices);
        adapter = new MyAdapter(this);
        Lv.setAdapter(adapter);
        //判断设备是否支持低功耗
        if(!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH)){
            Toast.makeText(this,"你的手机不支持BLE",Toast.LENGTH_SHORT).show();
            finish();
        }
        //判断设备是否支持蓝牙
        final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();
        if(bluetoothAdapter == null)
        {
            Toast.makeText(this,"你的手机不支持蓝牙",Toast.LENGTH_SHORT).show();
        }
        //打开蓝牙的点击事件
        open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                open();
            }
        });
        //扫描设备的点击事件
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTitle("正在扫描！");
                adapter.clear();
                scan();
            }
        });
        //显示已配对的蓝牙的点击事件
        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTitle("获取已配对设备！");
                adapter.clear();
                getBound();
            }
        });
    }
    //打开设备蓝牙
    public void open(){
        if(!bluetoothAdapter.isEnabled()) {
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(intent);
        }
        else {
//            Toast.makeText(this,"蓝牙已经打开，可以使用",Toast.LENGTH_SHORT).show();
                showToast("蓝牙已经打开，可以使用",1500);
        }
    }
    //自定义Toast方法
    public void showToast(String msg,int time){
        Toast toast =  Toast.makeText(getApplicationContext(),msg,time);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();
    }
    public void scan() {
        // 扫描蓝牙必须是打开状态，先判断蓝牙的状态
        if (bluetoothAdapter.isEnabled()) {
                // 开始扫描
                showToast("蓝牙已打开，开始扫描",1500);
                startScan();
        } else {
            // 关闭状态
            showToast("蓝牙没有打开，请点击打开蓝牙按钮，打开蓝牙",1000);

        }
    }
    private void startScan(){
        //以广播的形式来发送蓝牙设备，如果蓝牙没在扫描。开始扫描
        if(!bluetoothAdapter.isDiscovering()){
            bluetoothAdapter.startDiscovery();
        }else {
            showToast("正在扫描",1500);
        }
    }
//    private void request(){
//        //蓝牙是关闭状态，
//        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//        startActivity(intent);
//        bluetoothAdapter.startDiscovery();
//    }
    public void getBound(){
        // 获取已配对的蓝牙设备
        Set<BluetoothDevice> bondedDevices = bluetoothAdapter.getBondedDevices();
        // set集合里面去数据用迭代器或者foreach，不能使用for循环，因为没有索引号
        for (BluetoothDevice bondedDevice : bondedDevices) {
            adapter.addDevice(bondedDevice);
            adapter.notifyDataSetChanged();
        }
    }
    // 通过广播接收者来接收蓝牙设备
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // 蓝牙设备被发现的监听
            if (intent.getAction().equals(BluetoothDevice.ACTION_FOUND)) {
                // 获取到其它的蓝牙设备
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // 从蓝牙设备中获取具体的数据，获取蓝牙的名称(如果没有名称就显示匿名)
                String name = device.getName() == null ? "匿名" : device.getName();
                String adress = device.getAddress();
                // 把蓝牙名称和地址添加到集合
                adapter.addDevice(device);
                adapter.notifyDataSetChanged();
            }
        }
    };
    @Override
    protected void onResume(){
        super.onResume();
        registerReceiver(receiver,new IntentFilter(BluetoothDevice.ACTION_FOUND));
    }
    @Override
    protected void onPause(){
        super.onPause();
        unregisterReceiver(receiver);
    }
}

