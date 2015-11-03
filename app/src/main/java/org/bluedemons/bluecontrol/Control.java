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
import android.os.ParcelUuid;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;


public class Control extends ActionBarActivity {

    private Button led ;
    private String btAddress;

    private UUID id ;

    private BluetoothAdapter adapter = null;
    private BluetoothDevice btDevice = null;
    private BluetoothSocket btSocket = null;

    private BtTransmission btTransmit;

    private Boolean sendSuccess ;
    private Boolean readSuccess ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);
        led = (Button) findViewById(R.id.led);
        Intent Control = getIntent();
        btAddress = Control.getStringExtra(BTdeviceSelect.EXTRA_DEVICE_ADDRESS);
        adapter = BluetoothAdapter.getDefaultAdapter();
        btDevice =  adapter.getRemoteDevice(btAddress);
        ParcelUuid[] uuid = btDevice.getUuids();
        id = UUID.fromString(uuid[0].toString());
        BtConnectThread connect = new BtConnectThread();
        connect.run();
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
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void led_onclick(View v){
        btTransmit.write("Hello\n".getBytes());
    }
    private void showMsg(String s){
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();

    }
    private void goBack(){
        Intent intent = new Intent(Control.this,BTdeviceSelect.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
    private class BtConnectThread extends Thread {
        public BtConnectThread(){
            try{
                btSocket = btDevice.createRfcommSocketToServiceRecord(id);
                btTransmit = new BtTransmission();
                btTransmit.inStream = btSocket.getInputStream();
                btTransmit.outStream = btSocket.getOutputStream();
                showMsg("Socket Created");

            }catch (IOException e){
                showMsg("Failed to communicate with the device please try again!");
                goBack();
            }
        }
        public void run(){
            adapter.cancelDiscovery();
            try {
                btSocket.connect();
                showMsg("Connected!");
            }
            catch (IOException e){
                showMsg("Connection failed");
                goBack();
            }
        }
    }
    private class BtTransmission extends Thread{

        private InputStream inStream;
        private OutputStream outStream;;


        public void write(byte[] write){
            try {
                outStream.write(write);
                sendSuccess = true;
            }catch (IOException e){
                showMsg("Sending failed");
                sendSuccess = false;
            }
        }
        public int read(){
            try{
                return inStream.read();
            }catch (IOException e){
                showMsg("Read fail");
                readSuccess = false;
                return -989;
            }
        }
    }

}
