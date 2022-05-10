package com.bignerdranch.android.sharedpreferences

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import com.bignerdranch.android.sharedpreferences.databinding.ActivityMainBinding

// Имя преференцов
const val APP_PREFERENCES = "APP_PREFERENCES"
// Префренцы хранят данные так же как и бандлы,в парах ключ и значение
const val PREF_SOME_TEXT_VALUE = "PREF_SOME_TEXT_VALUE"

class MainActivity : AppCompatActivity() {

    private lateinit var binding:ActivityMainBinding

    private lateinit var preferences: SharedPreferences

    // Лисенер срабатывать когда применяется метод apply и что то меняется
    // в реалтайме обновляем интерфейс
    private val preferencesListener = SharedPreferences.OnSharedPreferenceChangeListener { preferences, key ->
        if(key == PREF_SOME_TEXT_VALUE){
            binding.currentTextValueTextView.text = preferences.getString(key,"")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preferences = getSharedPreferences(APP_PREFERENCES,Context.MODE_PRIVATE)
        val currentValue = preferences.getString(PREF_SOME_TEXT_VALUE,"")
        binding.currentTextValueTextView.text = currentValue

        // Вытягиваем данные из преференсов
        binding.valueEditText.setText(preferences.getString(PREF_SOME_TEXT_VALUE,""))

        // Скрываю клавиатуру
        binding.valueEditText.setOnEditorActionListener { _, id, _ ->
            if(id == EditorInfo.IME_ACTION_DONE){
                return@setOnEditorActionListener false
            }
            return@setOnEditorActionListener true
            //false скроет клавиатуру а true нет
        }

        // Сохраняем данные в преференцы
        // метод edit открывает транзакцию для редактирования,можно ещё что то положить
        // но в конце нужно закрыть транзакцию методом apply
        binding.saveButton.setOnClickListener {
            val value = binding.valueEditText.text.toString()
            preferences.edit()
                .putString(PREF_SOME_TEXT_VALUE,value)
                .apply()
        }

        // Подписываемся на лисенер
        preferences.registerOnSharedPreferenceChangeListener(preferencesListener)
    }

    // Отписываемся от лисенера
    override fun onDestroy() {
        super.onDestroy()
        preferences.unregisterOnSharedPreferenceChangeListener(preferencesListener)
    }
}

// Pref можно посмотреть через Device File Explorer , /Data/Data/com.bignerdranch.android.sharedpreferences/shred_prefs