package com.example.temperatureapp

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private lateinit var editTextCelsius: EditText
    private lateinit var editTextFahrenheit: EditText

    private var isUpdatingCelsius = false
    private var isUpdatingFahrenheit = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editTextCelsius = findViewById(R.id.editTextCelsius)
        editTextFahrenheit = findViewById(R.id.editTextFahrenheit)

        editTextCelsius.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!isUpdatingCelsius) {
                    isUpdatingFahrenheit = true
                    val celsius = s.toString().toDoubleOrNull()
                    updateFahrenheitField(celsius)
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        editTextFahrenheit.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!isUpdatingFahrenheit) {
                    isUpdatingCelsius = true
                    val fahrenheit = s.toString().toDoubleOrNull()
                    updateCelsiusField(fahrenheit)
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun updateFahrenheitField(celsius: Double?) {
        GlobalScope.launch(Dispatchers.Main) {
            val fahrenheit = if (celsius != null) {
                withContext(Dispatchers.Default) {
                    celsiusToFahrenheit(celsius)
                }
            } else {
                null
            }
            editTextFahrenheit.setText(fahrenheit?.toString() ?: "")
            isUpdatingFahrenheit = false
        }
    }

    private fun updateCelsiusField(fahrenheit: Double?) {
        GlobalScope.launch(Dispatchers.Main) {
            val celsius = if (fahrenheit != null) {
                withContext(Dispatchers.Default) {
                    fahrenheitToCelsius(fahrenheit)
                }
            } else {
                null
            }
            editTextCelsius.setText(celsius?.toString() ?: "")
            isUpdatingCelsius = false
        }
    }

    private fun celsiusToFahrenheit(celsius: Double): Double {
        return celsius * 9 / 5 + 32
    }

    private fun fahrenheitToCelsius(fahrenheit: Double): Double {
        return (fahrenheit - 32) * 5 / 9
    }
}
