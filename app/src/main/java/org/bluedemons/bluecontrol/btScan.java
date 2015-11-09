package org.bluedemons.bluecontrol;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.ToggleButton;

import java.util.ArrayList;


public class btScan extends ActionBarActivity {
    private BluetoothAdapter adapter ;
    private ArrayAdapter btArrayList ;
    private ArrayList btList;
    private BroadcastReceiver btBroadcastReciever = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String result = intent.getAction();
            if(BluetoothDevice.ACTION_FOUND.equals(result)){
                BluetoothDevice b = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if(b!=null) {
                    btArrayList.add(b.getName() + " : " + b.getAddress());
                    btArrayList.notifyDataSetChanged();
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bt_scan);
        adapter = BluetoothAdapter.getDefaultAdapter();
        btList = new ArrayList();
        btArrayList = new ArrayAdapter(this, android.R.layout.simple_list_item_1, btList);
        ListView scanList = (ListView) findViewById(R.id.scanList);
        scanList.setAdapter(btArrayList);
        ToggleButton scan = (ToggleButton) findViewById(R.id.toggleButton);
        scan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
                if (isChecked) {
                    btList.clear();
                    registerReceiver(btBroadcastReciever, filter);
                    adapter.startDiscovery();
                } else {
                    unregisterReceiver(btBroadcastReciever);
                    adapter.cancelDiscovery();
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_bt_scan, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
