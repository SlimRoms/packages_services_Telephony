/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.android.phone;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceScreen;
import android.preference.PreferenceActivity;
import android.preference.SwitchPreference;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.RILConstants;

import android.os.AsyncResult;
import android.os.Handler;
import android.os.Message;
import android.util.Log;


/**
 * List of Phone-specific settings screens.
 */
public class CellBroadcastSms extends PreferenceActivity
        implements Preference.OnPreferenceChangeListener{
    // debug data
    private static final String LOG_TAG = "CellBroadcastSms";
    private static final boolean DBG = false;

    //String keys for preference lookup
    private static final String BUTTON_ENABLE_DISABLE_BC_SMS_KEY =
        "button_enable_disable_cell_bc_sms";
    private static final String LIST_LANGUAGE_KEY =
        "list_language";
    private static final String BUTTON_EMERGENCY_BROADCAST_KEY =
        "button_emergency_broadcast";
    private static final String BUTTON_ADMINISTRATIVE_KEY =
        "button_administrative";
    private static final String BUTTON_MAINTENANCE_KEY =
        "button_maintenance";
    private static final String BUTTON_LOCAL_WEATHER_KEY =
        "button_local_weather";
    private static final String BUTTON_ATR_KEY =
        "button_atr";
    private static final String BUTTON_LAFS_KEY =
        "button_lafs";
    private static final String BUTTON_RESTAURANTS_KEY =
        "button_restaurants";
    private static final String BUTTON_LODGINGS_KEY =
        "button_lodgings";
    private static final String BUTTON_RETAIL_DIRECTORY_KEY =
        "button_retail_directory";
    private static final String BUTTON_ADVERTISEMENTS_KEY =
        "button_advertisements";
    private static final String BUTTON_STOCK_QUOTES_KEY =
        "button_stock_quotes";
    private static final String BUTTON_EO_KEY =
        "button_eo";
    private static final String BUTTON_MHH_KEY =
        "button_mhh";
    private static final String BUTTON_TECHNOLOGY_NEWS_KEY =
        "button_technology_news";
    private static final String BUTTON_MULTI_CATEGORY_KEY =
        "button_multi_category";

    private static final String BUTTON_LOCAL_GENERAL_NEWS_KEY =
        "button_local_general_news";
    private static final String BUTTON_REGIONAL_GENERAL_NEWS_KEY =
        "button_regional_general_news";
    private static final String BUTTON_NATIONAL_GENERAL_NEWS_KEY =
        "button_national_general_news";
    private static final String BUTTON_INTERNATIONAL_GENERAL_NEWS_KEY =
        "button_international_general_news";

    private static final String BUTTON_LOCAL_BF_NEWS_KEY =
        "button_local_bf_news";
    private static final String BUTTON_REGIONAL_BF_NEWS_KEY =
        "button_regional_bf_news";
    private static final String BUTTON_NATIONAL_BF_NEWS_KEY =
        "button_national_bf_news";
    private static final String BUTTON_INTERNATIONAL_BF_NEWS_KEY =
        "button_international_bf_news";

    private static final String BUTTON_LOCAL_SPORTS_NEWS_KEY =
        "button_local_sports_news";
    private static final String BUTTON_REGIONAL_SPORTS_NEWS_KEY =
        "button_regional_sports_news";
    private static final String BUTTON_NATIONAL_SPORTS_NEWS_KEY =
        "button_national_sports_news";
    private static final String BUTTON_INTERNATIONAL_SPORTS_NEWS_KEY =
        "button_international_sports_news";

    private static final String BUTTON_LOCAL_ENTERTAINMENT_NEWS_KEY =
        "button_local_entertainment_news";
    private static final String BUTTON_REGIONAL_ENTERTAINMENT_NEWS_KEY =
        "button_regional_entertainment_news";
    private static final String BUTTON_NATIONAL_ENTERTAINMENT_NEWS_KEY =
        "button_national_entertainment_news";
    private static final String BUTTON_INTERNATIONAL_ENTERTAINMENT_NEWS_KEY =
        "button_international_entertainment_news";

    //Class constants
    //These values are related to the C structs. See the comments in  method
    //setCbSmsConfig for more information.
    private static final int NO_OF_SERVICE_CATEGORIES = 31;
    private static final int NO_OF_INTS_STRUCT_1 = 3;
    private static final int MAX_LENGTH_RESULT = NO_OF_SERVICE_CATEGORIES * NO_OF_INTS_STRUCT_1 + 1;
    //Handler keys
    private static final int MESSAGE_ACTIVATE_CB_SMS = 1;
    private static final int MESSAGE_GET_CB_SMS_CONFIG = 2;
    private static final int MESSAGE_SET_CB_SMS_CONFIG = 3;

    //UI objects
    private SwitchPreference mButtonBcSms;

    private ListPreference mListLanguage;

    private SwitchPreference mButtonEmergencyBroadcast;
    private SwitchPreference mButtonAdministrative;
    private SwitchPreference mButtonMaintenance;
    private SwitchPreference mButtonLocalWeather;
    private SwitchPreference mButtonAtr;
    private SwitchPreference mButtonLafs;
    private SwitchPreference mButtonRestaurants;
    private SwitchPreference mButtonLodgings;
    private SwitchPreference mButtonRetailDirectory;
    private SwitchPreference mButtonAdvertisements;
    private SwitchPreference mButtonStockQuotes;
    private SwitchPreference mButtonEo;
    private SwitchPreference mButtonMhh;
    private SwitchPreference mButtonTechnologyNews;
    private SwitchPreference mButtonMultiCategory;

    private SwitchPreference mButtonLocal1;
    private SwitchPreference mButtonRegional1;
    private SwitchPreference mButtonNational1;
    private SwitchPreference mButtonInternational1;

    private SwitchPreference mButtonLocal2;
    private SwitchPreference mButtonRegional2;
    private SwitchPreference mButtonNational2;
    private SwitchPreference mButtonInternational2;

    private SwitchPreference mButtonLocal3;
    private SwitchPreference mButtonRegional3;
    private SwitchPreference mButtonNational3;
    private SwitchPreference mButtonInternational3;

    private SwitchPreference mButtonLocal4;
    private SwitchPreference mButtonRegional4;
    private SwitchPreference mButtonNational4;
    private SwitchPreference mButtonInternational4;


    //Member variables
    private Phone mPhone;
    private MyHandler mHandler;

    /**
     * Invoked on each preference click in this hierarchy, overrides
     * PreferenceActivity's implementation.  Used to make sure we track the
     * preference click events.
     */
    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen,
            Preference preference) {
        if (preference == mListLanguage) {
            //Do nothing here, because this click will be handled in onPreferenceChange
        } else {
            preferenceScreen.setEnabled(false);
            return false;
        }

        return true;
    }

    public boolean onPreferenceChange(Preference preference, Object objValue) {
        if (preference == mListLanguage) {
            // set the new language to the array which will be transmitted later
            CellBroadcastSmsConfig.setConfigDataCompleteLanguage(
                    mListLanguage.findIndexOfValue((String) objValue) + 1);
        } else if (preference == mButtonBcSms) {
            if (DBG) Log.d(LOG_TAG, "onPreferenceTreeClick: preference == mButtonBcSms.");
            if ((Boolean) objValue) {
                mPhone.activateCellBroadcastSms(RILConstants.CDMA_CELL_BROADCAST_SMS_ENABLED,
                        Message.obtain(mHandler, MESSAGE_ACTIVATE_CB_SMS));
                android.provider.Settings.Global.putInt(mPhone.getContext().getContentResolver(),
                        android.provider.Settings.Global.CDMA_CELL_BROADCAST_SMS,
                        RILConstants.CDMA_CELL_BROADCAST_SMS_ENABLED);
                enableDisableAllCbConfigButtons(true);
            } else {
                mPhone.activateCellBroadcastSms(RILConstants.CDMA_CELL_BROADCAST_SMS_DISABLED,
                        Message.obtain(mHandler, MESSAGE_ACTIVATE_CB_SMS));
                android.provider.Settings.Global.putInt(mPhone.getContext().getContentResolver(),
                        android.provider.Settings.Global.CDMA_CELL_BROADCAST_SMS,
                        RILConstants.CDMA_CELL_BROADCAST_SMS_DISABLED);
                enableDisableAllCbConfigButtons(false);
            }
        } else if (preference == mButtonEmergencyBroadcast) {
            CellBroadcastSmsConfig.setConfigDataCompleteBSelected(
                    (Boolean) objValue, 1);
            CellBroadcastSmsConfig.setCbSmsBSelectedValue(
                    (Boolean) objValue, 1);
        } else if (preference == mButtonAdministrative) {
            CellBroadcastSmsConfig.setConfigDataCompleteBSelected(
                    (Boolean) objValue, 2);
            CellBroadcastSmsConfig.setCbSmsBSelectedValue((Boolean) objValue, 2);
        } else if (preference == mButtonMaintenance) {
            CellBroadcastSmsConfig.setConfigDataCompleteBSelected(
                    (Boolean) objValue, 3);
            CellBroadcastSmsConfig.setCbSmsBSelectedValue((Boolean) objValue, 3);
        } else if (preference == mButtonLocalWeather) {
            CellBroadcastSmsConfig.setConfigDataCompleteBSelected(
                    (Boolean) objValue, 20);
            CellBroadcastSmsConfig.setCbSmsBSelectedValue((Boolean) objValue, 20);
        } else if (preference == mButtonAtr) {
            CellBroadcastSmsConfig.setConfigDataCompleteBSelected((Boolean) objValue, 21);
            CellBroadcastSmsConfig.setCbSmsBSelectedValue((Boolean) objValue, 21);
        } else if (preference == mButtonLafs) {
            CellBroadcastSmsConfig.setConfigDataCompleteBSelected((Boolean) objValue, 22);
            CellBroadcastSmsConfig.setCbSmsBSelectedValue((Boolean) objValue, 22);
        } else if (preference == mButtonRestaurants) {
            CellBroadcastSmsConfig.setConfigDataCompleteBSelected(
                    (Boolean) objValue, 23);
            CellBroadcastSmsConfig.setCbSmsBSelectedValue((Boolean) objValue, 23);
        } else if (preference == mButtonLodgings) {
            CellBroadcastSmsConfig.setConfigDataCompleteBSelected((Boolean) objValue, 24);
            CellBroadcastSmsConfig.setCbSmsBSelectedValue((Boolean) objValue, 24);
        } else if (preference == mButtonRetailDirectory) {
            CellBroadcastSmsConfig.setConfigDataCompleteBSelected(
                    (Boolean) objValue, 25);
            CellBroadcastSmsConfig.setCbSmsBSelectedValue((Boolean) objValue, 25);
        } else if (preference == mButtonAdvertisements) {
            CellBroadcastSmsConfig.setConfigDataCompleteBSelected(
                    (Boolean) objValue, 26);
            CellBroadcastSmsConfig.setCbSmsBSelectedValue((Boolean) objValue, 26);
        } else if (preference == mButtonStockQuotes) {
            CellBroadcastSmsConfig.setConfigDataCompleteBSelected(
                    (Boolean) objValue, 27);
            CellBroadcastSmsConfig.setCbSmsBSelectedValue((Boolean) objValue, 27);
        } else if (preference == mButtonEo) {
            CellBroadcastSmsConfig.setConfigDataCompleteBSelected((Boolean) objValue, 28);
            CellBroadcastSmsConfig.setCbSmsBSelectedValue((Boolean) objValue, 28);
        } else if (preference == mButtonMhh) {
            CellBroadcastSmsConfig.setConfigDataCompleteBSelected((Boolean) objValue, 29);
            CellBroadcastSmsConfig.setCbSmsBSelectedValue((Boolean) objValue, 29);
        } else if (preference == mButtonTechnologyNews) {
            CellBroadcastSmsConfig.setConfigDataCompleteBSelected(
                    (Boolean) objValue, 30);
            CellBroadcastSmsConfig.setCbSmsBSelectedValue((Boolean) objValue, 30);
        } else if (preference == mButtonMultiCategory) {
            CellBroadcastSmsConfig.setConfigDataCompleteBSelected(
                    (Boolean) objValue, 31);
            CellBroadcastSmsConfig.setCbSmsBSelectedValue((Boolean) objValue, 31);
        } else if (preference == mButtonLocal1) {
            CellBroadcastSmsConfig.setConfigDataCompleteBSelected((Boolean) objValue, 4);
            CellBroadcastSmsConfig.setCbSmsBSelectedValue((Boolean) objValue, 4);
        } else if (preference == mButtonRegional1) {
            CellBroadcastSmsConfig.setConfigDataCompleteBSelected(
                    (Boolean) objValue, 5);
            CellBroadcastSmsConfig.setCbSmsBSelectedValue((Boolean) objValue, 5);
        } else if (preference == mButtonNational1) {
            CellBroadcastSmsConfig.setConfigDataCompleteBSelected(
                    (Boolean) objValue, 6);
            CellBroadcastSmsConfig.setCbSmsBSelectedValue((Boolean) objValue, 6);
        } else if (preference == mButtonInternational1) {
            CellBroadcastSmsConfig.setConfigDataCompleteBSelected(
                    (Boolean) objValue, 7);
            CellBroadcastSmsConfig.setCbSmsBSelectedValue((Boolean) objValue, 7);
        } else if (preference == mButtonLocal2) {
            CellBroadcastSmsConfig.setConfigDataCompleteBSelected((Boolean) objValue, 8);
            CellBroadcastSmsConfig.setCbSmsBSelectedValue((Boolean) objValue, 8);
        } else if (preference == mButtonRegional2) {
            CellBroadcastSmsConfig.setConfigDataCompleteBSelected(
                    (Boolean) objValue, 9);
            CellBroadcastSmsConfig.setCbSmsBSelectedValue((Boolean) objValue, 9);
        } else if (preference == mButtonNational2) {
            CellBroadcastSmsConfig.setConfigDataCompleteBSelected(
                    (Boolean) objValue, 10);
            CellBroadcastSmsConfig.setCbSmsBSelectedValue((Boolean) objValue, 10);
        } else if (preference == mButtonInternational2) {
            CellBroadcastSmsConfig.setConfigDataCompleteBSelected(
                    (Boolean) objValue, 11);
            CellBroadcastSmsConfig.setCbSmsBSelectedValue((Boolean) objValue, 11);
        } else if (preference == mButtonLocal3) {
            CellBroadcastSmsConfig.setConfigDataCompleteBSelected((Boolean) objValue, 12);
            CellBroadcastSmsConfig.setCbSmsBSelectedValue((Boolean) objValue, 12);
        } else if (preference == mButtonRegional3) {
            CellBroadcastSmsConfig.setConfigDataCompleteBSelected(
                    (Boolean) objValue, 13);
            CellBroadcastSmsConfig.setCbSmsBSelectedValue((Boolean) objValue, 13);
        } else if (preference == mButtonNational3) {
            CellBroadcastSmsConfig.setConfigDataCompleteBSelected(
                    (Boolean) objValue, 14);
            CellBroadcastSmsConfig.setCbSmsBSelectedValue((Boolean) objValue, 14);
        } else if (preference == mButtonInternational3) {
            CellBroadcastSmsConfig.setConfigDataCompleteBSelected(
                    (Boolean) objValue, 15);
            CellBroadcastSmsConfig.setCbSmsBSelectedValue((Boolean) objValue, 15);
        } else if (preference == mButtonLocal4) {
            CellBroadcastSmsConfig.setConfigDataCompleteBSelected((Boolean) objValue, 16);
            CellBroadcastSmsConfig.setCbSmsBSelectedValue((Boolean) objValue, 16);
        } else if (preference == mButtonRegional4) {
            CellBroadcastSmsConfig.setConfigDataCompleteBSelected(
                    (Boolean) objValue, 17);
            CellBroadcastSmsConfig.setCbSmsBSelectedValue((Boolean) objValue, 17);
        } else if (preference == mButtonNational4) {
            CellBroadcastSmsConfig.setConfigDataCompleteBSelected(
                    (Boolean) objValue, 18);
            CellBroadcastSmsConfig.setCbSmsBSelectedValue((Boolean) objValue, 18);
        } else if (preference == mButtonInternational4) {
            CellBroadcastSmsConfig.setConfigDataCompleteBSelected(
                    (Boolean) objValue, 19);
            CellBroadcastSmsConfig.setCbSmsBSelectedValue((Boolean) objValue, 19);
        }

        // always let the preference setting proceed.
        return true;
    }

    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        addPreferencesFromResource(R.xml.cell_broadcast_sms);

        mPhone = PhoneGlobals.getPhone();
        mHandler = new MyHandler();

        PreferenceScreen prefSet = getPreferenceScreen();

        mButtonBcSms = (SwitchPreference) prefSet.findPreference(
                BUTTON_ENABLE_DISABLE_BC_SMS_KEY);
        mListLanguage = (ListPreference) prefSet.findPreference(
                LIST_LANGUAGE_KEY);
        // set the listener for the language list preference
        mListLanguage.setOnPreferenceChangeListener(this);
        mButtonEmergencyBroadcast = (SwitchPreference) prefSet.findPreference(
                BUTTON_EMERGENCY_BROADCAST_KEY);
        mButtonEmergencyBroadcast.setOnPreferenceChangeListener(this);
        mButtonAdministrative = (SwitchPreference) prefSet.findPreference(
                BUTTON_ADMINISTRATIVE_KEY);
        mButtonAdministrative.setOnPreferenceChangeListener(this);
        mButtonMaintenance = (SwitchPreference) prefSet.findPreference(
                BUTTON_MAINTENANCE_KEY);
        mButtonMaintenance.setOnPreferenceChangeListener(this);
        mButtonLocalWeather = (SwitchPreference) prefSet.findPreference(
                BUTTON_LOCAL_WEATHER_KEY);
        mButtonLocalWeather.setOnPreferenceChangeListener(this);
        mButtonAtr = (SwitchPreference) prefSet.findPreference(
                BUTTON_ATR_KEY);
        mButtonAtr.setOnPreferenceChangeListener(this);
        mButtonLafs = (SwitchPreference) prefSet.findPreference(
                BUTTON_LAFS_KEY);
        mButtonLafs.setOnPreferenceChangeListener(this);
        mButtonRestaurants = (SwitchPreference) prefSet.findPreference(
                BUTTON_RESTAURANTS_KEY);
        mButtonRestaurants.setOnPreferenceChangeListener(this);
        mButtonLodgings = (SwitchPreference) prefSet.findPreference(
                BUTTON_LODGINGS_KEY);
        mButtonLodgings.setOnPreferenceChangeListener(this);
        mButtonRetailDirectory = (SwitchPreference) prefSet.findPreference(
                BUTTON_RETAIL_DIRECTORY_KEY);
        mButtonRetailDirectory.setOnPreferenceChangeListener(this);
        mButtonAdvertisements = (SwitchPreference) prefSet.findPreference(
                BUTTON_ADVERTISEMENTS_KEY);
        mButtonAdvertisements.setOnPreferenceChangeListener(this);
        mButtonStockQuotes = (SwitchPreference) prefSet.findPreference(
                BUTTON_STOCK_QUOTES_KEY);
        mButtonStockQuotes.setOnPreferenceChangeListener(this);
        mButtonEo = (SwitchPreference) prefSet.findPreference(
                BUTTON_EO_KEY);
        mButtonEo.setOnPreferenceChangeListener(this);
        mButtonMhh = (SwitchPreference) prefSet.findPreference(
                BUTTON_MHH_KEY);
        mButtonMhh.setOnPreferenceChangeListener(this);
        mButtonTechnologyNews = (SwitchPreference) prefSet.findPreference(
                BUTTON_TECHNOLOGY_NEWS_KEY);
        mButtonTechnologyNews.setOnPreferenceChangeListener(this);
        mButtonMultiCategory = (SwitchPreference) prefSet.findPreference(
                BUTTON_MULTI_CATEGORY_KEY);
        mButtonMultiCategory.setOnPreferenceChangeListener(this);

        mButtonLocal1 = (SwitchPreference) prefSet.findPreference(
                BUTTON_LOCAL_GENERAL_NEWS_KEY);
        mButtonLocal1.setOnPreferenceChangeListener(this);
        mButtonRegional1 = (SwitchPreference) prefSet.findPreference(
                BUTTON_REGIONAL_GENERAL_NEWS_KEY);
        mButtonRegional1.setOnPreferenceChangeListener(this);
        mButtonNational1 = (SwitchPreference) prefSet.findPreference(
                BUTTON_NATIONAL_GENERAL_NEWS_KEY);
        mButtonNational1.setOnPreferenceChangeListener(this);
        mButtonInternational1 = (SwitchPreference) prefSet.findPreference(
                BUTTON_INTERNATIONAL_GENERAL_NEWS_KEY);
        mButtonInternational1.setOnPreferenceChangeListener(this);

        mButtonLocal2 = (SwitchPreference) prefSet.findPreference(
                BUTTON_LOCAL_BF_NEWS_KEY);
        mButtonLocal2.setOnPreferenceChangeListener(this);
        mButtonRegional2 = (SwitchPreference) prefSet.findPreference(
                BUTTON_REGIONAL_BF_NEWS_KEY);
        mButtonRegional2.setOnPreferenceChangeListener(this);
        mButtonNational2 = (SwitchPreference) prefSet.findPreference(
                BUTTON_NATIONAL_BF_NEWS_KEY);
        mButtonNational2.setOnPreferenceChangeListener(this);
        mButtonInternational2 = (SwitchPreference) prefSet.findPreference(
                BUTTON_INTERNATIONAL_BF_NEWS_KEY);
        mButtonInternational2.setOnPreferenceChangeListener(this);

        mButtonLocal3 = (SwitchPreference) prefSet.findPreference(
                BUTTON_LOCAL_SPORTS_NEWS_KEY);
        mButtonLocal3.setOnPreferenceChangeListener(this);
        mButtonRegional3 = (SwitchPreference) prefSet.findPreference(
                BUTTON_REGIONAL_SPORTS_NEWS_KEY);
        mButtonRegional3.setOnPreferenceChangeListener(this);
        mButtonNational3 = (SwitchPreference) prefSet.findPreference(
                BUTTON_NATIONAL_SPORTS_NEWS_KEY);
        mButtonNational3.setOnPreferenceChangeListener(this);
        mButtonInternational3 = (SwitchPreference) prefSet.findPreference(
                BUTTON_INTERNATIONAL_SPORTS_NEWS_KEY);
        mButtonInternational3.setOnPreferenceChangeListener(this);

        mButtonLocal4 = (SwitchPreference) prefSet.findPreference(
                BUTTON_LOCAL_ENTERTAINMENT_NEWS_KEY);
        mButtonLocal4.setOnPreferenceChangeListener(this);
        mButtonRegional4 = (SwitchPreference) prefSet.findPreference(
                BUTTON_REGIONAL_ENTERTAINMENT_NEWS_KEY);
        mButtonRegional4.setOnPreferenceChangeListener(this);
        mButtonNational4 = (SwitchPreference) prefSet.findPreference(
                BUTTON_NATIONAL_ENTERTAINMENT_NEWS_KEY);
        mButtonNational4.setOnPreferenceChangeListener(this);
        mButtonInternational4 = (SwitchPreference) prefSet.findPreference(
                BUTTON_INTERNATIONAL_ENTERTAINMENT_NEWS_KEY);
        mButtonInternational4.setOnPreferenceChangeListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        getPreferenceScreen().setEnabled(true);

        int settingCbSms = android.provider.Settings.Global.getInt(
                mPhone.getContext().getContentResolver(),
                android.provider.Settings.Global.CDMA_CELL_BROADCAST_SMS,
                RILConstants.CDMA_CELL_BROADCAST_SMS_DISABLED);
        mButtonBcSms.setChecked(settingCbSms == RILConstants.CDMA_CELL_BROADCAST_SMS_ENABLED);

        if(mButtonBcSms.isChecked()) {
            enableDisableAllCbConfigButtons(true);
        } else {
            enableDisableAllCbConfigButtons(false);
        }

        mPhone.getCellBroadcastSmsConfig(Message.obtain(mHandler, MESSAGE_GET_CB_SMS_CONFIG));
    }

    @Override
    protected void onPause() {
        super.onPause();

            CellBroadcastSmsConfig.setCbSmsNoOfStructs(NO_OF_SERVICE_CATEGORIES);

            mPhone.setCellBroadcastSmsConfig(CellBroadcastSmsConfig.getCbSmsAllValues(),
                    Message.obtain(mHandler, MESSAGE_SET_CB_SMS_CONFIG));
        }

    private void enableDisableAllCbConfigButtons(boolean enable) {
        mButtonEmergencyBroadcast.setEnabled(enable);
        mListLanguage.setEnabled(enable);
        mButtonAdministrative.setEnabled(enable);
        mButtonMaintenance.setEnabled(enable);
        mButtonLocalWeather.setEnabled(enable);
        mButtonAtr.setEnabled(enable);
        mButtonLafs.setEnabled(enable);
        mButtonRestaurants.setEnabled(enable);
        mButtonLodgings.setEnabled(enable);
        mButtonRetailDirectory.setEnabled(enable);
        mButtonAdvertisements.setEnabled(enable);
        mButtonStockQuotes.setEnabled(enable);
        mButtonEo.setEnabled(enable);
        mButtonMhh.setEnabled(enable);
        mButtonTechnologyNews.setEnabled(enable);
        mButtonMultiCategory.setEnabled(enable);

        mButtonLocal1.setEnabled(enable);
        mButtonRegional1.setEnabled(enable);
        mButtonNational1.setEnabled(enable);
        mButtonInternational1.setEnabled(enable);

        mButtonLocal2.setEnabled(enable);
        mButtonRegional2.setEnabled(enable);
        mButtonNational2.setEnabled(enable);
        mButtonInternational2.setEnabled(enable);

        mButtonLocal3.setEnabled(enable);
        mButtonRegional3.setEnabled(enable);
        mButtonNational3.setEnabled(enable);
        mButtonInternational3.setEnabled(enable);

        mButtonLocal4.setEnabled(enable);
        mButtonRegional4.setEnabled(enable);
        mButtonNational4.setEnabled(enable);
        mButtonInternational4.setEnabled(enable);
    }

    private void setAllCbConfigButtons(int[] configArray) {
        //These buttons are in a well defined sequence. If you want to change it,
        //be sure to map the buttons to their corresponding slot in the configArray !
        mButtonEmergencyBroadcast.setChecked(configArray[1] != 0);
        //subtract 1, because the values are handled in an array which starts with 0 and not with 1
        mListLanguage.setValueIndex(CellBroadcastSmsConfig.getConfigDataLanguage() - 1);
        mButtonAdministrative.setChecked(configArray[2] != 0);
        mButtonMaintenance.setChecked(configArray[3] != 0);
        mButtonLocalWeather.setChecked(configArray[20] != 0);
        mButtonAtr.setChecked(configArray[21] != 0);
        mButtonLafs.setChecked(configArray[22] != 0);
        mButtonRestaurants.setChecked(configArray[23] != 0);
        mButtonLodgings.setChecked(configArray[24] != 0);
        mButtonRetailDirectory.setChecked(configArray[25] != 0);
        mButtonAdvertisements.setChecked(configArray[26] != 0);
        mButtonStockQuotes.setChecked(configArray[27] != 0);
        mButtonEo.setChecked(configArray[28] != 0);
        mButtonMhh.setChecked(configArray[29] != 0);
        mButtonTechnologyNews.setChecked(configArray[30] != 0);
        mButtonMultiCategory.setChecked(configArray[31] != 0);

        mButtonLocal1.setChecked(configArray[4] != 0);
        mButtonRegional1.setChecked(configArray[5] != 0);
        mButtonNational1.setChecked(configArray[6] != 0);
        mButtonInternational1.setChecked(configArray[7] != 0);

        mButtonLocal2.setChecked(configArray[8] != 0);
        mButtonRegional2.setChecked(configArray[9] != 0);
        mButtonNational2.setChecked(configArray[10] != 0);
        mButtonInternational2.setChecked(configArray[11] != 0);

        mButtonLocal3.setChecked(configArray[12] != 0);
        mButtonRegional3.setChecked(configArray[13] != 0);
        mButtonNational3.setChecked(configArray[14] != 0);
        mButtonInternational3.setChecked(configArray[15] != 0);

        mButtonLocal4.setChecked(configArray[16] != 0);
        mButtonRegional4.setChecked(configArray[17] != 0);
        mButtonNational4.setChecked(configArray[18] != 0);
        mButtonInternational4.setChecked(configArray[19] != 0);
    }

    private class MyHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case MESSAGE_ACTIVATE_CB_SMS:
                //Only a log message here, because the received response is always null
                if (DBG) Log.d(LOG_TAG, "Cell Broadcast SMS enabled/disabled.");
                break;
            case MESSAGE_GET_CB_SMS_CONFIG:
                int result[] = (int[])((AsyncResult)msg.obj).result;

                // check if the actual service categoties table size on the NV is '0'
                if (result[0] == 0) {
                    result[0] = NO_OF_SERVICE_CATEGORIES;

                    mButtonBcSms.setChecked(false);
                    mPhone.activateCellBroadcastSms(RILConstants.CDMA_CELL_BROADCAST_SMS_DISABLED,
                            Message.obtain(mHandler, MESSAGE_ACTIVATE_CB_SMS));
                    android.provider.Settings.Global.putInt(mPhone.getContext().getContentResolver(),
                            android.provider.Settings.Global.CDMA_CELL_BROADCAST_SMS,
                            RILConstants.CDMA_CELL_BROADCAST_SMS_DISABLED);
                    enableDisableAllCbConfigButtons(false);
                }

                CellBroadcastSmsConfig.setCbSmsConfig(result);
                setAllCbConfigButtons(CellBroadcastSmsConfig.getCbSmsBselectedValues());

                break;
            case MESSAGE_SET_CB_SMS_CONFIG:
                //Only a log message here, because the received response is always null
                if (DBG) Log.d(LOG_TAG, "Set Cell Broadcast SMS values.");
                break;
            default:
                Log.e(LOG_TAG, "Error! Unhandled message in CellBroadcastSms.java. Message: "
                        + msg.what);
            break;
            }
        }
    }

    private static final class CellBroadcastSmsConfig {

        //The values in this array are stored in a particular order. This order
        //is calculated in the setCbSmsConfig method of this class.
        //For more information see comments below...
        //NO_OF_SERVICE_CATEGORIES +1 is used, because we will leave the first array entry 0
        private static int mBSelected[] = new int[NO_OF_SERVICE_CATEGORIES + 1];
        private static int mConfigDataComplete[] = new int[MAX_LENGTH_RESULT];

        private static void setCbSmsConfig(int[] configData) {
            if(configData == null) {
                Log.e(LOG_TAG, "Error! No cell broadcast service categories returned.");
                return;
            }

            if(configData[0] > MAX_LENGTH_RESULT) {
                Log.e(LOG_TAG, "Error! Wrong number of service categories returned from RIL");
                return;
            }

            //The required config values for broadcast SMS are stored in a C struct:
            //
            //  typedef struct {
            //      int size;
            //      RIL_CDMA_BcServiceInfo *entries;
            //  } RIL_CDMA_BcSMSConfig;
            //
            //  typedef struct {
            //      int uServiceCategory;
            //      int uLanguage;
            //      unsigned char bSelected;
            //  } RIL_CDMA_BcServiceInfo;
            //
            // This means, that we have to ignore the first value and check every
            // 3rd value starting with the 2nd of all. This value indicates, where we
            // will store the appropriate bSelected value, which is 2 values behind it.
            for(int i = 1; i < configData.length; i += NO_OF_INTS_STRUCT_1) {
                mBSelected[configData[i]] = configData[i +2];
            }

            //Store all values in an extra array
            mConfigDataComplete = configData;
        }

        private static void setCbSmsBSelectedValue(boolean value, int pos) {
            if(pos < mBSelected.length) {
                mBSelected[pos] = (value == true ? 1 : 0);
            } else {
                Log.e(LOG_TAG,"Error! Invalid value position.");
            }
        }

        private static int[] getCbSmsBselectedValues() {
            return(mBSelected);
        }

        // TODO: Change the return value to a RIL_BroadcastSMSConfig
        private static int[] getCbSmsAllValues() {
            return(mConfigDataComplete);
        }

        private static void setCbSmsNoOfStructs(int value) {
            //Sets the size parameter, which contains the number of structs
            //that will be transmitted
            mConfigDataComplete[0] = value;
        }

        private static void setConfigDataCompleteBSelected(boolean value, int serviceCategory) {
            //Sets the bSelected value for a specific serviceCategory
            for(int i = 1; i < mConfigDataComplete.length; i += NO_OF_INTS_STRUCT_1) {
                if(mConfigDataComplete[i] == serviceCategory) {
                    mConfigDataComplete[i + 2] = value == true ? 1 : 0;
                    break;
                }
            }
        }

        private static void setConfigDataCompleteLanguage(int language) {
            //It is only possible to set the same language for all entries
            for(int i = 2; i < mConfigDataComplete.length; i += NO_OF_INTS_STRUCT_1) {
                mConfigDataComplete[i] = language;
            }
        }

        private static int getConfigDataLanguage() {
            int language = mConfigDataComplete[2];
            //2 is the language value of the first entry
            //It is only possible to set the same language for all entries
            if (language < 1 || language > 7) {
                Log.e(LOG_TAG, "Error! Wrong language returned from RIL...defaulting to 1, english");
                return 1;
            }
            else {
                return language;
            }
        }
    }
}
