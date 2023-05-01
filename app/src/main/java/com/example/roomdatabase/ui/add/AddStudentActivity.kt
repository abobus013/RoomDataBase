package com.example.roomdatabase.ui.add

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.roomdatabase.R
import com.example.roomdatabase.data.model.MyNote
import com.example.roomdatabase.databinding.ActivitySearchBinding
import com.example.roomdatabase.repositories.MainViewModel
import kotlinx.coroutines.launch
import java.util.Locale

class AddStudentActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var binding: ActivitySearchBinding
    private lateinit var mBn: Button

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivitySearchBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        loadLocate()
        setContentView(binding.root)
        supportActionBar?.title = resources.getString(R.string.app_name)

        viewModel = ViewModelProvider(
            this, ViewModelProvider.AndroidViewModelFactory(application)
        )[MainViewModel::class.java]

        mBn = binding.btnChange

        mBn.setOnClickListener {

            showChangeLang()
        }


        val isEdit = intent.getBooleanExtra("isEdit", false)
        val studentId = intent.getIntExtra("id", 0)
        val studentName = intent.getStringExtra("name")
        val studentSurname = intent.getStringExtra("surname")

        if (isEdit) {
            binding.btnAdd.text = "Edit"
            binding.etName.setText(studentName)
            binding.etSurname.setText(studentSurname)
        } else {
            binding.btnAdd.text = "Add"
        }

        binding.ivBack.setOnClickListener {
            finish()
        }

        binding.btnAdd.setOnClickListener {
            val name = binding.etName.text.toString()
            val surname = binding.etSurname.text.toString()

            lifecycleScope.launch {
                if (isEdit) {
                    if (name.isNotEmpty() && surname.isNotEmpty()) {
                        viewModel.updateNotes(MyNote(studentId, name, surname))
                    } else {
                        Toast.makeText(
                            this@AddStudentActivity, "Ввидите данные", Toast.LENGTH_SHORT
                        ).show()

                    }
                    Toast.makeText(this@AddStudentActivity, "успешно обновлено", Toast.LENGTH_SHORT)
                        .show()
                    finish()
                } else {
                    if (name.isNotEmpty() && surname.isNotEmpty()) {
                        viewModel.addNote(MyNote(0, name, surname))
                    } else {
                        Toast.makeText(
                            this@AddStudentActivity, "Ввидите данные", Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                finish()
            }
        }
    }
    private fun showChangeLang() {
        val listItem = arrayOf("Français","日本","Русский","o'zbek")

        val mBuilder = AlertDialog.Builder(this@AddStudentActivity)
        mBuilder.setTitle("Choose Language")
        mBuilder.setSingleChoiceItems(listItem,-1) { dialog, which ->

            if (which == 0) {
                setLocate("fr")
                recreate()
            }

            if (which == 1) {
                setLocate("ja")
                recreate()
            }

            if (which == 2) {
                setLocate("ru")
                recreate()
            }

            if (which == 3) {
                setLocate("uz")
                recreate()
            }

            dialog.dismiss()
        }
        val mDialog = mBuilder.create()
        mDialog.show()
    }

    private fun setLocate(Lang: String) {
        val locale = Locale(Lang)

        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        baseContext.resources.updateConfiguration(config, baseContext.resources.displayMetrics)

        val editor = getSharedPreferences("Setting", Context.MODE_PRIVATE).edit()
        editor.putString("My_Lang", Lang)
        editor.apply()
    }

    private fun loadLocate() {
        val sharedPreferences = getSharedPreferences("Settings", Activity.MODE_PRIVATE)
        val language = sharedPreferences.getString("My_Lang", "")
        if (language != null) {
            setLocate(language)
        }
    }

}

