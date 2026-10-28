package id.ac.polbeng.depandienda.internalstorageexample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import id.ac.polbeng.depandienda.internalstorageexample.databinding.ActivitySecondBinding
import java.io.FileNotFoundException
import java.io.IOException

class SecondActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySecondBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        loadData()
    }

    private fun loadData() {
        Thread {
            try {
                val input = openFileInput(MainActivity.FILE_NAME)
                input.use {
                    val buffer = StringBuilder()
                    var bytesRead = it.read()
                    while (bytesRead != -1) {
                        buffer.append(bytesRead.toChar())
                        bytesRead = it.read()
                    }
                    runOnUiThread {
                        binding.tvOutputText.text = buffer.toString()
                    }
                }
            } catch (fnf: FileNotFoundException) {
                // File belum ada
            } catch (ioe: IOException) {
                // Error IO
            }
        }.start()
    }
}