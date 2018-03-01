package com.vgaw.nrfconnect.page.scanner;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.vgaw.nrfconnect.R;
import com.vgaw.nrfconnect.bean.ScannerFilter;
import com.vgaw.nrfconnect.data.PreferenceManager;
import com.vgaw.nrfconnect.databinding.FragmentScannerBinding;
import com.vgaw.nrfconnect.util.HexTransform;
import com.vgaw.nrfconnect.util.Utils;
import com.vgaw.nrfconnect.view.HexInputFilter;
import com.vgaw.nrfconnect.view.SBWithTV;

/**
 * Created by caojin on 2018/2/27.
 */

public class ScannerFragment extends Fragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener, TextWatcher, SBWithTV.ShowValueListener {
    public static final String TAG = "ScannerFragment";
    private static final int RSSI_MIN = -100;
    private static final int RSSI_MAX = -40;
    private FragmentScannerBinding binding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_scanner, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    @Override
    public void onStop() {
        super.onStop();
        setFilterToPreference();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivClearAll:
                clearNameAddress();
                clearData();
                clearRSSI();
                clearFavorite();
                break;
            case R.id.ivNameAddressMore:
                break;
            case R.id.ivNameAddressClear:
                clearNameAddress();
                break;
            case R.id.ivDataMore:
                break;
            case R.id.ivDataClear:
                clearData();
                break;
        }
    }

    private void clearFavorite() {
        binding.includeScanner.cbFavorite.setChecked(false);
    }

    private void clearRSSI() {
        binding.includeScanner.sbRSSI.setProgress(RSSI_MIN);
    }

    private void clearNameAddress() {
        binding.includeScanner.etNameAddress.setText("");
    }

    private void clearData() {
        binding.includeScanner.etData.setText("");
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        updateDescription();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {}

    @Override
    public void afterTextChanged(Editable s) {
        updateDescription();
    }

    private void updateDescription() {
        String nameAddress = binding.includeScanner.etNameAddress.getText().toString();
        String data = binding.includeScanner.etData.getText().toString();
        int currentRSSI = (int) binding.includeScanner.sbRSSI.getProgress();
        String rssi = (currentRSSI == RSSI_MIN ? null : String.valueOf(currentRSSI));
        String favorite = binding.includeScanner.cbFavorite.isChecked() ? getString(R.string.scanner_filter_only_favorite) : null;
        StringBuilder sb = new StringBuilder();
        if (!TextUtils.isEmpty(nameAddress)) {
            sb.append(nameAddress).append(",");
        }
        if (!TextUtils.isEmpty(data)) {
            sb.append(data).append(",");
        }
        if (!TextUtils.isEmpty(rssi)) {
            sb.append(rssi).append(",");
        }
        if (!TextUtils.isEmpty(favorite)) {
            sb.append(favorite).append(",");
        }
        if (TextUtils.isEmpty(sb)) {
            binding.includeScanner.tvDescription.setText(R.string.scanner_filter_no_filter);
            binding.includeScanner.ivClearAll.setVisibility(View.GONE);
        } else {
            binding.includeScanner.tvDescription.setText(sb.substring(0, sb.length() - 1));
            binding.includeScanner.ivClearAll.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public String onShowValue(View view, float raw) {
        return Utils.getDot(raw, 0) + "dBm";
    }

    @Override
    public void onProgressChanged(View view, float currentValue, boolean fromUser) {
        updateDescription();
    }

    private void getFilterFromPreference() {
        ScannerFilter scannerFilter = PreferenceManager.getScannerFilter();
        if (scannerFilter != null) {
            binding.includeScanner.etNameAddress.setText(Utils.nullToEmpty(scannerFilter.getNameAddress()));
            binding.includeScanner.etData.setText(Utils.nullToEmpty(scannerFilter.getData()));
            binding.includeScanner.sbRSSI.setProgress(scannerFilter.getRssi());
            binding.includeScanner.cbFavorite.setChecked(scannerFilter.isFavorite());
        } else {
            clearRSSI();
        }
    }

    private void setFilterToPreference() {
        ScannerFilter scannerFilter = new ScannerFilter();
        scannerFilter.setNameAddress(binding.includeScanner.etNameAddress.getText().toString());
        scannerFilter.setData(binding.includeScanner.etData.getText().toString());
        scannerFilter.setRssi((int) binding.includeScanner.sbRSSI.getProgress());
        scannerFilter.setFavorite(binding.includeScanner.cbFavorite.isChecked());
        PreferenceManager.setScannerFilter(scannerFilter);
    }

    private void initView() {
        binding.includeScanner.ivClearAll.setOnClickListener(this);
        binding.includeScanner.ivNameAddressMore.setOnClickListener(this);
        binding.includeScanner.ivNameAddressClear.setOnClickListener(this);
        binding.includeScanner.ivDataMore.setOnClickListener(this);
        binding.includeScanner.ivDataClear.setOnClickListener(this);
        binding.includeScanner.sbRSSI.setShowValueListener(this);
        binding.includeScanner.sbRSSI.init(getString(R.string.scanner_filter_rssi), RSSI_MIN, RSSI_MAX, 1);
        binding.includeScanner.cbFavorite.setOnCheckedChangeListener(this);
        binding.includeScanner.etData.setFilters(new InputFilter[]{new HexInputFilter()});
        binding.includeScanner.etNameAddress.addTextChangedListener(this);
        binding.includeScanner.etData.addTextChangedListener(this);

        getFilterFromPreference();
    }
}
