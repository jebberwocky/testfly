package com.mine.testfly

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.mine.testfly.ui.theme.TestflyTheme
import io.branch.referral.Branch
import io.branch.referral.Branch.BranchReferralInitListener
import org.json.JSONException


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TestflyTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    Greeting("Android")
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Branch.getInstance().setIdentity("UUID123456")
        //Branch.getInstance().setPreinstallCampaign("agency_654143947735585363_My_PreInstall_test3_Ad_Campaign");
        //Branch.getInstance().setPreinstallPartner("jeff_fake_OEM");
        //Branch.getInstance().setPreinstallCampaign("agency_654143947735585363_My_PreInstall_test3_Ad_Campaign");
        //Branch.getInstance().setPreinstallPartner("jeff_fake_OEM");
        Branch.getInstance().setRequestMetadata("app_store", "JEFF_STORE")
        Branch.getInstance()
            .setRequestMetadata("\$braze_install_id", "b87551c4-857a-4186-9117-9a34f93cc19a")
        // Branch init
        // Branch init
        Branch.sessionBuilder(this).withCallback { referringParams, error ->
            if (error == null) {
                if (referringParams != null) {
                    Log.i("BRANCH SDK INIT", referringParams.toString())
                    try {
                        //Log.i("BRANCH params", referringParams.get("params").toString());
                        Log.i(
                            "BRANCH \$canonical_url",
                            referringParams!!["\$canonical_url"].toString()
                        )
                    } catch (exception: JSONException) {
                        Log.e("Casting error", exception.toString())
                    }
                }
            } else {
                Log.e("BRANCH SDK ERROR", error.message)
            }
        }.withData(this.intent.data).init()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        this.intent = intent;
        if (intent != null &&
            intent.hasExtra("branch_force_new_session") &&
            intent.getBooleanExtra("branch_force_new_session",false)) {
            Branch.sessionBuilder(this).withCallback { referringParams, error ->
                if (error != null) {
                    Log.e("BranchSDK_Tester", error.message)
                } else if (referringParams != null) {
                    Log.i("BranchSDK_Tester", referringParams.toString())
                }
            }.reInit()
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
            text = "Hello $name!",
            modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TestflyTheme {
        Greeting("Android")
    }
}