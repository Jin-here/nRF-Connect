package com.vgaw.nrfconnect.page.main;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.vgaw.nrfconnect.R;
import com.vgaw.nrfconnect.common.BLEManager;
import com.vgaw.nrfconnect.databinding.ActivityMainBinding;
import com.vgaw.nrfconnect.util.ToastUtil;

/**
 * @author caojin
 * @date 2018/2/26
 */

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    private static final String TAG = "MainActivity";

    private static final int REQUEST_ENABLE_BT = 0x01;
    private ActivityMainBinding binding;

    private MainTabController mMainTabController;

    private BroadcastReceiver mBluetoothReceiver;

    public Menu getMenu() {
        return binding.toolbar.getMenu();
    }

    public MainTabController getMainTabController() {
        return this.mMainTabController;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        initView();
        registerBluetoothReceiver();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterBluetoothRecevier();
        mMainTabController.release();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_device:
                break;
            case R.id.nav_manage_favorite:
                break;
            case R.id.nav_definition:
                break;
            case R.id.nav_device_information:
                break;
            case R.id.nav_setting:
                break;
        }
        binding.drawerMain.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (binding.drawerMain.isDrawerOpen(GravityCompat.START)) {
            binding.drawerMain.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ENABLE_BT && resultCode == Activity.RESULT_OK) {
            proBluetoothHint();
        }
    }

    private void initView() {
        setSupportActionBar(binding.toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                binding.drawerMain, binding.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        binding.drawerMain.addDrawerListener(toggle);
        toggle.syncState();

        setTitle(R.string.nav_device);
        binding.navMain.setNavigationItemSelectedListener(this);

        mMainTabController = new MainTabController(this, binding);
        mMainTabController.init();

        binding.btnEnableBluetooth.setOnClickListener(this);
        proBluetoothHint();
    }

    private void proBluetoothHint() {
        binding.vMainBluetoothDisabled.setVisibility(BLEManager.enabled(this) ? View.GONE : View.VISIBLE);
    }

    public void openBLE(Activity activity) {
        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        activity.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
    }

    @Override
    public void onClick(View v) {
        if (BLEManager.supported(this)) {
            openBLE(this);
        } else {
            ToastUtil.show(R.string.main_hint_bluetooth_not_supported);
        }
    }

    private void registerBluetoothReceiver() {
        mBluetoothReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch(intent.getAction()) {
                    case BluetoothAdapter.ACTION_STATE_CHANGED:
                        int blueState = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 0);
                        switch(blueState) {
                            case BluetoothAdapter.STATE_ON:
                            case BluetoothAdapter.STATE_OFF:
                                proBluetoothHint();
                                break;
                        }
                        break;
                }
            }
        };
        IntentFilter statusFilter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(mBluetoothReceiver, statusFilter);
    }

    private void unregisterBluetoothRecevier() {
        unregisterReceiver(mBluetoothReceiver);
    }
}
