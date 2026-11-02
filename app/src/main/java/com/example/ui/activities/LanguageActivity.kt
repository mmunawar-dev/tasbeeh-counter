package com.example.ui.activities

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.example.databinding.FragmentLanguageSelectBinding
import com.example.model.AppLanguage
import com.example.service.SakinahPreferences
import com.example.ui.adapters.LanguageAdapter

class LanguageActivity : AppCompatActivity() {

  private lateinit var binding: FragmentLanguageSelectBinding
  private var selectedLanguage: AppLanguage = AppLanguage.ENGLISH
  private var languageAdapter: LanguageAdapter? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = FragmentLanguageSelectBinding.inflate(layoutInflater)
    setContentView(binding.root)

    binding.btnLanguageBack.visibility = View.VISIBLE
    binding.btnLanguageBack.setOnClickListener {
      finish()
    }

    selectedLanguage = SakinahPreferences.getSavedLanguage(this)

    languageAdapter = LanguageAdapter(selectedLanguage) { lang ->
      selectedLanguage = lang
      languageAdapter?.updateSelectedLanguage(lang)
    }

    binding.rvLanguages.layoutManager = GridLayoutManager(this, 2)
    binding.rvLanguages.adapter = languageAdapter

    binding.btnLanguageApply.setOnClickListener {
      SakinahPreferences.saveLanguage(this, selectedLanguage)
      val appLocale = LocaleListCompat.forLanguageTags(selectedLanguage.code)
      AppCompatDelegate.setApplicationLocales(appLocale)
      finish()
    }
  }
}
