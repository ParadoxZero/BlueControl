/*
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
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import java.util.Set;
import java.util.ArrayList;
import android.widget.Toast;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.content.Intent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;


public class BTdeviceSelect extends ActionBarActivity {

    static final public String EXTRA_DEVICE_ADDRESS = "com.bluedeamons.BTdeviceSelected";

    private Button btselect;
    private ListView btList;
    private BluetoothAdapter btadapter;
    private Set<BluetoothDevice> pairedDevices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_btdevice_select);
        btselect = (Button) findViewById(R.id.btselect);
        btList = (ListView) findViewById(R.id.btlist);
        btadapter = BluetoothAdapter.getDefaultAdapter();

        if(btadapter==null){
            Toast.makeText(getApplicationContext(),"No bluetooth adapter available",Toast.LENGTH_LONG).show();
            finish();
        }
        else{
            if (btadapter.isEnabled())
            {
                pairedDevices = btadapter.getBondedDevices();
            }
            else{
                DialogFragment newFragment = new SwitchBtDialogue();
                newFragment.show(getFragmentManager(), "noBT");
                //startActivityForResult( new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE), 1);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_btdevice_select, menu);
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
    public void populateList(View v) {
        if (btadapter.isEnabled()) {
            ArrayList list = new ArrayList();
            if (pairedDevices.isEmpty()) {
                Toast.makeText(getApplicationContext(), "No paired device available!", Toast.LENGTH_LONG).show();
            } else {
                for (BluetoothDevice b : pairedDevices) {
                    list.add(b.getName() + " : " + b.getAddress());
                }
            }
            ArrayAdapter badapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);
            btList.setAdapter(badapter);
            btList.setOnItemClickListener(new myBtListListener());
        }
        else{
            DialogFragment newFragment = new SwitchBtDialogue();
            newFragment.show(getFragmentManager(), "noBT");
        }
    }


    public void exit_click(View V){
        DialogFragment d = new ExitClick();
        d.show(getFragmentManager(),"exit");
    }


    //============ On Click Liseners================================================

    private class myBtListListener implements AdapterView.OnItemClickListener {
        public void onItemClick(AdapterView p,View v,int i,long id){
            String device = ((TextView)v).getText().toString();
            String deviceAddr = device.substring(device.length()-17);
            //Toast.makeText(getApplicationContext(),deviceAddr,Toast.LENGTH_LONG).show();
            Intent intent = new Intent(BTdeviceSelect.this,Control.class);
            intent.putExtra(EXTRA_DEVICE_ADDRESS,deviceAddr);
            startActivity(intent);
        }
    }
    //==============================================================================



    //========================Dialogue Class===========================================
    public class SwitchBtDialogue extends DialogFragment {
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
    public class ExitClick extends DialogFragment{

        public Dialog onCreateDialog(Bundle savedInstanceState){
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(R.string.confirmationExit)
                    .setPositiveButton(R.string.okay, new DialogInterface.OnClickListener(){
                        public void onClick(DialogInterface dialog, int id){
                            finish();
                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener(){
                        public void onClick(DialogInterface dialogue, int id){}
                    });
            return builder.create();
        }
    }
    //=======================================================================================

}
