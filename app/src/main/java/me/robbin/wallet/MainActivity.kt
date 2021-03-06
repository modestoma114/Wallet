package me.robbin.wallet

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import me.robbin.utils.BarUtils
import me.robbin.wallet.ui.theme.WalletTheme

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        BarUtils.transparentStatusBar(this)
        setContent {
            WalletTheme {
                Surface(color = MaterialTheme.colors.background) {
                    Greeting("Android") {
                        startActivity(Intent(this, MainActivity::class.java))
                    }
                }
            }
        }
    }

}

@Composable
fun Greeting(
    name: String,
    clickEvent: () -> Unit
) {
    Text(text = "Hello $name!")
    Button(onClick = clickEvent) {
        Text(text = "进入下一页")
    }
}