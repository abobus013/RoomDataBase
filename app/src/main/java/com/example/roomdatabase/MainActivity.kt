package com.example.roomdatabase

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.roomdatabase.adapters.StudentsAdapter
import com.example.roomdatabase.dao.StudentsDao
import com.example.roomdatabase.data.DataBase
import com.example.roomdatabase.data.Student
import com.example.roomdatabase.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import java.util.*
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private lateinit var dao: StudentsDao
    private lateinit var binding: ActivityMainBinding
    private val adapter = StudentsAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        setAppLocale(languageFromPreference = String())
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        dao = DataBase.getInstance(this).getStudentsDao()


        binding.rvStudents.adapter = adapter


        val itemTouchHelperCallback = object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ) = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val student: Student = adapter.models[position]

                lifecycleScope.launchWhenResumed {
                    dao.deleteStudent(student)
                }
                adapter.models.removeAt(position)
                adapter.notifyItemRemoved(position)
//
               Snackbar.make(
                    viewHolder.itemView,
                    "O'shdi", Snackbar.LENGTH_LONG
                ).apply {
                    setAction("UNDO") {

                        lifecycleScope.launchWhenResumed {
                            dao.addStudents(student)
                        }

                        adapter.models.add(position, student)
                        adapter.notifyItemInserted(position)


                        binding.rvStudents.scrollToPosition(position)
                    }
                    setActionTextColor(Color.YELLOW)
                }.show()
            }
        }

        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(binding.rvStudents)
        }


        binding.fbAdd.setOnClickListener {
            val intent = Intent(this, AddStudentActivity::class.java)
            intent.putExtra("isEdit", false)
            startActivity(intent)
        }

        adapter.onClickStudentsListener {
            val intent = Intent(this, AddStudentActivity::class.java)
            intent.putExtra("isEdit", true)
            intent.putExtra("id", it.id)
            intent.putExtra("name", it.name)
            intent.putExtra("surname", it.surname)

            startActivity(intent)
        }

        binding.etSearch.addTextChangedListener {
            if (it.toString().isNotEmpty()) {

                lifecycleScope.launch {
                    adapter.models = dao.searchStudentByName(it.toString()).toMutableList()
                }

            }else {
                lifecycleScope.launch {
                    adapter.models = dao.getListOfStudents().toMutableList()
                }
            }
        }

    }

    override fun onResume() {
        super.onResume()

        lifecycleScope.launch {
            adapter.models = dao.getListOfStudents().toMutableList()
        }
    }
    private fun setAppLocale(languageFromPreference: String?) {
        if (languageFromPreference != null) {
            val resources: Resources = resources
            val dm: DisplayMetrics = resources.displayMetrics
            val config: Configuration = resources.configuration
            config.setLocale(Locale(languageFromPreference.lowercase(Locale.ROOT)))
            val pref = getSharedPreferences("pref", Context.MODE_PRIVATE)
            pref.edit().putString("language",languageFromPreference.toString().lowercase()).apply()
            resources.updateConfiguration(config, dm)
        }
    }


}