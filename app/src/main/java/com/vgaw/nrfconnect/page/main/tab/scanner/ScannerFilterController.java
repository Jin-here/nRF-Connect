package com.vgaw.nrfconnect.page.main.tab.scanner;

import android.app.Activity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CompoundButton;

import com.vgaw.nrfconnect.R;
import com.vgaw.nrfconnect.bean.ScannerFilter;
import com.vgaw.nrfconnect.data.PreferenceManager;
import com.vgaw.nrfconnect.databinding.FragmentDeviceScannerBinding;
import com.vgaw.nrfconnect.util.Utils;
import com.vgaw.nrfconnect.view.HexInputFilter;
import com.vgaw.nrfconnect.view.SBWithTV;
import com.vgaw.nrfconnect.view.expansion.ExpansionLayout;

/**
 * Created by caojin on 2018/3/2.
 */

public class ScannerFilterController implements View.OnClickListener, CompoundButton.OnCheckedChangeListener, TextWatcher, SBWithTV.ShowValueListener, ExpansionLayout.IndicatorListener {
    private static final String TAG = "ScannerFilterController";
    private static final int RSSI_MIN = -100;
    private static final int RSSI_MAX = -40;

    private Activity activity;
    private FragmentDeviceScannerBinding binding;

    public void setBinding(Activity activity, FragmentDeviceScannerBinding binding) {
        this.activity = activity;
        this.binding = binding;
    }

    public void onCreateView() {
        binding.ivClearAll.setOnClickListener(this);
        binding.ivNameAddressMore.setOnClickListener(this);
        binding.ivNameAddressClear.setOnClickListener(this);
        binding.ivDataMore.setOnClickListener(this);
        binding.ivDataClear.setOnClickListener(this);
        binding.sbRSSI.setShowValueListener(this);
        binding.sbRSSI.init(activity.getString(R.string.scanner_filter_rssi), RSSI_MIN, RSSI_MAX, 1);
        binding.cbFavorite.setOnCheckedChangeListener(this);
        binding.etData.setFilters(new InputFilter[]{new HexInputFilter()});
        binding.etNameAddress.addTextChangedListener(this);
        binding.etData.addTextChangedListener(this);
        binding.expansionLayout.addIndicatorListener(this);
    }

    public void onActivityCreated() {
        getFilterFromPreference();
        updateDescription();
    }

    public void onStop() {
        setFilterToPreference();
    }

    public void onDestroy() {
        binding.expansionLayout.removeIndicatorListener(this);
    }

    private void clearFavorite() {
        binding.cbFavorite.setChecked(false);
    }

    private void clearRSSI() {
        binding.sbRSSI.setProgress(RSSI_MIN);
    }

    private void clearNameAddress() {
        binding.etNameAddress.setText("");
    }

    private void clearData() {
        binding.etData.setText("");
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
        String nameAddress = binding.etNameAddress.getText().toString();
        String data = binding.etData.getText().toString();
        int currentRSSI = (int) binding.sbRSSI.getProgress();
        String rssi = (currentRSSI == RSSI_MIN ? null : String.valueOf(currentRSSI));
        String favorite = binding.cbFavorite.isChecked() ? activity.getString(R.string.scanner_filter_only_favorite) : null;
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
            binding.tvDescription.setText(R.string.scanner_filter_no_filter);
            binding.ivClearAll.setVisibility(View.GONE);
        } else {
            binding.tvDescription.setText(sb.substring(0, sb.length() - 1));
            binding.ivClearAll.setVisibility(View.VISIBLE);
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
            binding.etNameAddress.setText(Utils.nullToEmpty(scannerFilter.getNameAddress()));
            binding.etData.setText(Utils.nullToEmpty(scannerFilter.getData()));
            binding.sbRSSI.setProgress(scannerFilter.getRssi());
            binding.cbFavorite.setChecked(scannerFilter.isFavorite());
        } else {
            clearRSSI();
        }
    }

    private void setFilterToPreference() {
        ScannerFilter scannerFilter = new ScannerFilter();
        scannerFilter.setNameAddress(binding.etNameAddress.getText().toString());
        scannerFilter.setData(binding.etData.getText().toString());
        scannerFilter.setRssi((int) binding.sbRSSI.getProgress());
        scannerFilter.setFavorite(binding.cbFavorite.isChecked());
        PreferenceManager.setScannerFilter(scannerFilter);
    }

    @Override
    public void onStartedExpand(ExpansionLayout expansionLayout, boolean willExpand) {
        binding.horizontalSwipeLayoutScanner.setHorizontalSwipeEnabled(!willExpand);
        binding.swipeRefreshScanner.setEnabled(!willExpand);
    }
}
