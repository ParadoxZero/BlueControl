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

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
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
    private Boolean ledState = false;
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
        connect.start();
        //ConnectBT connect = new ConnectBT();
        //connect.execute();
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
    @Override
    public void onStop(){
        super.onStop();
        btTransmit.isRunning=false;
        showMsg("Stopping!");
        btTransmit.isRunning=false;
        if(btSocket != null){
            try {
                btSocket.close();
            } catch (IOException e){}
            btSocket = null;
        }

    }

    private void showMsg(String s){
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show();

    }
    private void goBack(){
        try {
            btSocket.close();
        } catch (IOException e){}
        Intent intent = new Intent(Control.this,BTdeviceSelect.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
    //============================ On Click Listeners ===================================
    public void led_onclick(View v){
        if(btSocket.isConnected() && btSocket!=null) {
            if (ledState) {
                ledState = false;
                btTransmit.write("q".getBytes());
            } else {
                ledState = true;

                btTransmit.write("w".getBytes());
            }
        }
        else {
            showMsg("Not connected, please try again!");
        }
    }
    public void me(View v){
        DialogFragment d = new Me();
        d.show(getFragmentManager(),"me");
    }
    public void disconnect(View v){
        goBack();
    }
    //================================================================================

    //========================Threads=================================================
    private class BtConnectThread extends Thread {
        public Boolean isConnected = false;
        @Override
        public void run(){


            try {
                btSocket = btDevice.createInsecureRfcommSocketToServiceRecord(id);
                btTransmit = new BtTransmission();
                btTransmit.inStream = btSocket.getInputStream();
                btTransmit.outStream = btSocket.getOutputStream();
                btTransmit.start();

                adapter.cancelDiscovery();
                btSocket.connect();
                isConnected = true;
            }
            catch (IOException e) {
                goBack();
            }
            if(isConnected=true) return;
        }

    }
    private class BtTransmission extends Thread{

        private InputStream inStream;
        private OutputStream outStream;;
        public Boolean isRunning = true ;
        private byte[] Write ;
        public void run() {
            if (Write != null) {
                try {
                    outStream.write(Write);
                    sendSuccess = true;
                    Write = null;
                } catch (IOException e) {
                    sendSuccess = false;
                }
            }
            if(isRunning=false) return;

        }


        public void write(byte[] w){
                Write = w;
        }
        public void cancel() {
                try {
                    btSocket.close();
                } catch (IOException e) { }
        }

    }

    //==========================================================================

    //=================================Dialogs==================================

    public class Me extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(R.string.switchOnBT)
                    .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                        }
                    });
            // Create the AlertDialog object and return it
            return builder.create();
        }
    }
    //==========================================================================
}
