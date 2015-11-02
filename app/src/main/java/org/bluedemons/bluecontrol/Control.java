/*
* Copyright 2015-2018 Sidhin S Thomas
*
* Author: SIDHIN S THOMAS
* GitHub UserName: ParadoxZero
* Email: sidhin.thomas@gmail.com
*
* This program is free to use and edit protected under Open GNU license agreement
*
 */


package org.bluedemons.bluecontrol;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.UUID;


public class Control extends ActionBarActivity {

    private Button led ;
    private String btAddress;

    final private UUID id = UUID.fromString("01001101-0000-1010-8000-00805F9B34FB");

    private BluetoothAdapter Adapter = null;
    private BluetoothDevice control = null;
    private BluetoothSocket channel = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);
        led = (Button) findViewById(R.id.led);
        Intent Control = getIntent();
        btAddress = Control.getStringExtra(BTdeviceSelect.EXTRA_DEVICE_ADDRESS);
        Adapter = BluetoothAdapter.getDefaultAdapter();
        control =  Adapter.getRemoteDevice(btAddress);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_control, menu);
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
    public void led_onclick(View v){
        Toast.makeText(getApplicationContext(),btAddress,Toast.LENGTH_LONG).show();

    }
}
