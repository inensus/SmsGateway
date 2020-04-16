package com.gateway.android.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.telephony.TelephonyManager
import android.text.Html
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.gateway.android.BuildConfig
import com.gateway.android.R
import com.gateway.android.utils.SharedPreferencesWrapper
import com.gateway.android.utils.Util
import kotlinx.android.synthetic.main.activity_dashboard.*

class DashboardActivity : AppCompatActivity(), SharedPreferencesWrapper.Listener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        SharedPreferencesWrapper.getInstance().setListener(this)

        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.SEND_SMS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.SEND_SMS,
                    Manifest.permission.RECEIVE_SMS,
                    Manifest.permission.READ_SMS,
                    Manifest.permission.READ_PHONE_STATE
                ),
                REQUEST_CODE_SMS_PERMISSION
            )
        }

        tvAppInfo.text =
            getString(R.string.app_information, BuildConfig.VERSION_NAME)
        invalidateDynamicViews()

        tvContactDetail.text = Html.fromHtml(getString(R.string.contact_detail))

        cvContact.setOnClickListener {
            Util.sendEmail(this)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) =
        when (item.itemId) {
            R.id.settings -> {
                showEmailBottomSheet()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }

    private fun showEmailBottomSheet() {
        EditTextBottomSheetFragment.newInstance().apply {
            show(supportFragmentManager, BOTTOM_SHEET_FRAGMENT_TAG)
        }
    }

    override fun onSharedPreferencesValueChange() {
        invalidateDynamicViews()
    }

    private fun invalidateDynamicViews() {
        runOnUiThread {
            ivNetworkState.setImageDrawable(
                if (SharedPreferencesWrapper.getInstance().networkState == 1) ContextCompat.getDrawable(
                    this,
                    R.drawable.network_active
                ) else ContextCompat.getDrawable(
                    this,
                    R.drawable.network_inactive
                )
            )

            ivSimState.setImageDrawable(
                if (SharedPreferencesWrapper.getInstance().simState == TelephonyManager.SIM_STATE_READY) ContextCompat.getDrawable(
                    this,
                    R.drawable.sim_active
                ) else ContextCompat.getDrawable(this, R.drawable.sim_inactive)
            )

            tvSentMessageCount.text =
                SharedPreferencesWrapper.getInstance().sentMessageCount.toString()
            tvReceivedMessageCount.text =
                SharedPreferencesWrapper.getInstance().receivedMessageCount.toString()
        }
    }

    companion object {
        private const val REQUEST_CODE_SMS_PERMISSION = 1234
        private const val BOTTOM_SHEET_FRAGMENT_TAG = "BOTTOM_SHEET_FRAGMENT_TAG"
    }
}
