package me.robbin.wallet

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import me.robbin.wallet.ui.theme.WalletTheme
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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