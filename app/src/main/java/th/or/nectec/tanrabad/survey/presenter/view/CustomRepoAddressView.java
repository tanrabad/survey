/*
 * Copyright (c) 2016 NECTEC
 *   National Electronics and Computer Technology Center, Thailand
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package th.or.nectec.tanrabad.survey.presenter.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.TintManager;
import android.support.v7.widget.TintTypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import th.or.nectec.android.widget.thai.OnAddressChangedListener;
import th.or.nectec.android.widget.thai.address.AddressPickerDialog;
import th.or.nectec.android.widget.thai.address.AddressView;
import th.or.nectec.domain.thai.ThaiAddressPrinter;
import th.or.nectec.entity.thai.Address;
import th.or.nectec.tanrabad.survey.repository.adapter.ThaiWidgetProvinceRepository;
import th.or.nectec.tanrabad.survey.repository.persistence.DbProvinceRepository;


public class CustomRepoAddressView extends AppCompatTextView implements AddressView {

    public static final int[] TINT_ATTRS = {android.R.attr.background};
    String addressCode;
    private Context context;
    private OnAddressChangedListener onAddressChangedListener;
    private Address selectedAddress;
    private OnAddressChangedListener onDialgoAddressChangedListener = new OnAddressChangedListener() {

        @Override
        public void onAddressChanged(Address address) {
            setAddress(address);
            if (onAddressChangedListener != null)
                onAddressChangedListener.onAddressChanged(address);
        }

        @Override
        public void onAddressCanceled() {
            if (onAddressChangedListener != null)
                onAddressChangedListener.onAddressCanceled();
        }
    };

    public CustomRepoAddressView(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.spinnerStyle);
    }

    public CustomRepoAddressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initTintManager(attrs, defStyleAttr);
        this.context = context;
    }

    private void initTintManager(AttributeSet attrs, int defStyleAttr) {
        if (TintManager.SHOULD_BE_USED) {
            TintTypedArray a = TintTypedArray.obtainStyledAttributes(getContext(), attrs,
                    TINT_ATTRS, defStyleAttr, 0);
            if (a.hasValue(0)) {
                ColorStateList tint = a.getTintManager().getTintList(a.getResourceId(0, -1));
                if (tint != null) {
                    setSupportBackgroundTintList(tint);
                }
            }
            a.recycle();
        }
    }

    @Override
    public void setAddressCode(String addressCode) {
        this.addressCode = addressCode;
    }

    @Override
    public void setAddress(String s, String s1, String s2) {
        setText(ThaiAddressPrinter.buildShortAddress(s, s1, s2));
    }

    @Override
    public void setOnAddressChangedListener(OnAddressChangedListener onAddressChangedListener) {
        this.onAddressChangedListener = onAddressChangedListener;
    }

    @Override
    public Address getAddress() {
        return selectedAddress;
    }

    private void setAddress(Address address) {
        selectedAddress = address;
        addressCode = address.getSubdistrictCode();
        setAddress(address.getSubdistrict().getName(), address.getDistrict().getName(), address.getProvince().getName());
    }

    @Override
    public boolean performClick() {
        AddressPickerDialog dialog = new AddressPickerDialog(context, onDialgoAddressChangedListener);
        dialog.setRepository(new ThaiWidgetProvinceRepository(new DbProvinceRepository(context)));
        if (TextUtils.isEmpty(addressCode))
            dialog.show();
        else
            dialog.show(addressCode);
        return true;
    }
}
