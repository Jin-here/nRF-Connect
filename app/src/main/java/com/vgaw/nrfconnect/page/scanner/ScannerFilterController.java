package com.vgaw.nrfconnect.page.scanner;

import android.app.Activity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CompoundButton;

import com.github.florent37.expansionpanel.ExpansionLayout;
import com.vgaw.nrfconnect.R;
import com.vgaw.nrfconnect.bean.ScannerFilter;
import com.vgaw.nrfconnect.data.PreferenceManager;
import com.vgaw.nrfconnect.databinding.FragmentDeviceScannerBinding;
import com.vgaw.nrfconnect.util.Utils;
import com.vgaw.nrfconnect.view.HexInputFilter;
import com.vgaw.nrfconnect.view.SBWithTV;

/**
 * Created by caojin on 2018/3/2.
 */

public class ScannerFilterController implements View.OnClickListener, CompoundButton.OnCheckedChangeListener, TextWatcher, SBWithTV.ShowValueListener, ExpansionLayout.IndicatorListener {
    private static final int RSSI_MIN = -100;
    private static final int RSSI_MAX = -40;

    private Activity activity;
    private FragmentDeviceScannerBinding binding;

    public void setBinding(Activity activity, FragmentDeviceScannerBinding binding) {
        this.activity = activity;
        this.binding = binding;
    }

    public void onActivityCreated() {
        binding.vScannerContainer.setOnClickListener(this);
        binding.includeScanner.expansionLayout.addIndicatorListener(this);
        binding.includeScanner.ivClearAll.setOnClickListener(this);
        binding.includeScanner.ivNameAddressMore.setOnClickListener(this);
        binding.includeScanner.ivNameAddressClear.setOnClickListener(this);
        binding.includeScanner.ivDataMore.setOnClickListener(this);
        binding.includeScanner.ivDataClear.setOnClickListener(this);
        binding.includeScanner.sbRSSI.setShowValueListener(this);
        binding.includeScanner.sbRSSI.init(activity.getString(R.string.scanner_filter_rssi), RSSI_MIN, RSSI_MAX, 1);
        binding.includeScanner.cbFavorite.setOnCheckedChangeListener(this);
        binding.includeScanner.etData.setFilters(new InputFilter[]{new HexInputFilter()});
        binding.includeScanner.etNameAddress.addTextChangedListener(this);
        binding.includeScanner.etData.addTextChangedListener(this);

        getFilterFromPreference();
        updateDescription();
    }

    public void onStop() {
        setFilterToPreference();
    }

    public void onDestroy() {
        binding.includeScanner.expansionLayout.removeIndicatorListener(this);
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.vScannerContainer:
                if (binding.includeScanner.expansionLayout.isExpanded()) {
                    binding.includeScanner.expansionLayout.collapse(true);
                }
                break;
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
        String favorite = binding.includeScanner.cbFavorite.isChecked() ? activity.getString(R.string.scanner_filter_only_favorite) : null;
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

    @Override
    public void onStartedExpand(ExpansionLayout expansionLayout, boolean willExpand) {
        if (willExpand) {
            binding.vScannerContainer.setBackgroundResource(android.R.drawable.screen_background_dark_transparent);
        } else {
            binding.vScannerContainer.setBackgroundColor(activity.getResources().getColor(android.R.color.transparent));
        }
    }
}
