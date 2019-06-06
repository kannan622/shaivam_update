package org.shaivam.utils;

import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import static org.shaivam.activities.RadioActivity.mPlayerAdapter;

public class MyPhoneStateListener extends PhoneStateListener {
    public void onCallStateChanged(int state, String incomingNumber) {
        switch (state) {
            case TelephonyManager.CALL_STATE_IDLE:
                if (mPlayerAdapter != null && !mPlayerAdapter.isPlaying())
                    mPlayerAdapter.resumeOrPause();
                break;
            case TelephonyManager.CALL_STATE_OFFHOOK:
                if (mPlayerAdapter != null && mPlayerAdapter.isPlaying())
                    mPlayerAdapter.resumeOrPause();
                break;
            case TelephonyManager.CALL_STATE_RINGING:
                if (mPlayerAdapter != null && mPlayerAdapter.isPlaying())
                    mPlayerAdapter.resumeOrPause();
                break;
        }
    }
}