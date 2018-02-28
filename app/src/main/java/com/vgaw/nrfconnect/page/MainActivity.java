package com.vgaw.nrfconnect.page;

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

import com.vgaw.nrfconnect.R;
import com.vgaw.nrfconnect.databinding.ActivityMainBinding;
import com.vgaw.nrfconnect.page.scanner.ScannerFragment;

/**
 * Created by caojin on 2018/2/26.
 */

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "MainActivity";
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        initView();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (binding.drawerMain.isDrawerOpen(GravityCompat.START)) {
            binding.drawerMain.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void initView() {
        setSupportActionBar(binding.toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                binding.drawerMain, binding.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        binding.drawerMain.addDrawerListener(toggle);
        toggle.syncState();

        binding.navMain.setNavigationItemSelectedListener(this);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.container_main, new ScannerFragment())
                .commit();
    }
}
