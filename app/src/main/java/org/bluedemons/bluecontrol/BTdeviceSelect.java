/*
*
* Author: SIDHIN S THOMAS
* GitHub UserName: ParadoxZero
* Email: sidhin.thomas@gmail.com
*
* This program is free to use and edit and protected under Open GNU license agreement
*
 */



package org.bluedemons.bluecontrol;

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
            Toast.makeText(getApplicationContext(),"Select the correct bluetooth device!",Toast.LENGTH_LONG).show();
            if (btadapter.isEnabled())
            {
                pairedDevices = btadapter.getBondedDevices();
            }
            else{
                //Ask to the user turn the bluetooth on
                startActivityForResult( new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE), 0);
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
    public void populateList(View v){
        ArrayList list = new ArrayList();
        if(pairedDevices.isEmpty()){
            Toast.makeText(getApplicationContext(),"No paired device available!",Toast.LENGTH_LONG).show();
        }
        else{
            for(BluetoothDevice b : pairedDevices){
                list.add(b.getName()+" : "+b.getAddress());
            }
        }
        ArrayAdapter badapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,list);
        btList.setAdapter(badapter);
        btList.setOnItemClickListener(null);
    }
}
