package id.ac.polbeng.depandienda.internalstorageexample

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import id.ac.polbeng.depandienda.internalstorageexample.databinding.ActivityMainBinding
import java.io.FileNotFoundException
import java.io.IOException

class MainActivity : AppCompatActivity() {
    private val TAG = javaClass.simpleName
    private lateinit var binding: ActivityMainBinding

    companion object {
        const val FILE_NAME = "rpl.txt"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Tombol untuk pindah ke SecondActivity
        binding.btnSaveText.setOnClickListener {
            startActivity(Intent(this, SecondActivity::class.java))
        }
    }

    private fun saveData() {
        Thread {
            try {
                val out = openFileOutput(FILE_NAME, Context.MODE_PRIVATE)
                out.use {
                    it.write(binding.etInputText.text.toString().toByteArray())
                }
                runOnUiThread {
                    Toast.makeText(this, "Saved", Toast.LENGTH_LONG).show()
                }
            } catch (ioe: IOException) {
                Log.w(TAG, "Error while saving $FILE_NAME: $ioe")
            }
        }.start()
    }

    override fun onPause() {
        super.onPause()
        saveData() // Menyimpan data secara otomatis saat aplikasi di-pause
    }

    override fun onResume() {
        super.onResume()
        loadData()
    }

    private fun loadData() {
        Thread {
            try {
                val input = openFileInput(FILE_NAME)
                input.use {
                    val buffer = StringBuilder()
                    var bytesRead = it.read()
                    while (bytesRead != -1) {
                        buffer.append(bytesRead.toChar())
                        bytesRead = it.read()
                    }
                    runOnUiThread {
                        binding.etInputText.setText(buffer.toString())
                    }
                }
            } catch (fnf: FileNotFoundException) {
                Log.w(TAG, "File not found, occurs only once")
            } catch (ioe: IOException) {
                Log.w(TAG, "IOException: $ioe")
            }
        }.start()
    }
}