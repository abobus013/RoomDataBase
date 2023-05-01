package com.example.roomdatabase.ui.main

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.roomdatabase.ui.main.adapters.NotesAdapter
import com.example.roomdatabase.data.model.MyNote
import com.example.roomdatabase.databinding.ActivityMainBinding
import com.example.roomdatabase.repositories.MainViewModel
import com.example.roomdatabase.ui.add.AddStudentActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding
    private val adapter = NotesAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        setAppLocale(languageFromPreference = String())
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        viewModel = ViewModelProvider(
            this, ViewModelProvider.AndroidViewModelFactory(application)
        )[MainViewModel::class.java]

        initVariables()
        initListeners()
        initObservers()
    }


    private fun initObservers() {
        viewModel.getAllNotesLifeData.observe(this){

            if (it.isNotEmpty()) {
                binding.tvEmpty.visibility = View.GONE
                adapter.models= it.toMutableList()
            } else {
                binding.tvEmpty.visibility = View.VISIBLE
            }

        }
    }

    private fun initListeners() {

        binding.fbAdd.setOnClickListener {
            val intent = Intent(this, AddStudentActivity::class.java)
            intent.putExtra("isEdit", false)
            startActivity(intent)
        }

        adapter.onClickNoteListener {
            val intent = Intent(this, AddStudentActivity::class.java)
            intent.putExtra("isEdit", true)
            intent.putExtra("id", it.id)
            intent.putExtra("name", it.name)
            intent.putExtra("surname", it.surname)

            startActivity(intent)
        }

        binding.etSearch.addTextChangedListener {
                lifecycleScope.launch {
                    viewModel.searchNoteByName(it.toString())
                }
        }
    }

    private fun initVariables() {
        val swapHelper = getSwapManager()
        swapHelper.attachToRecyclerView(binding.rvNotes)
        binding.rvNotes.adapter = adapter
    }

    private fun getSwapManager(): ItemTouchHelper {
        return ItemTouchHelper( object: ItemTouchHelper
            .SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ) = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val myNote: MyNote = adapter.models[position]

                lifecycleScope.launchWhenResumed {
                    viewModel.deleteNote(myNote)
                }
                adapter.models.removeAt(position)
                adapter.notifyItemRemoved(position)
//
                Snackbar.make(
                    viewHolder.itemView,
                    "Удалено", Snackbar.LENGTH_LONG
                ).apply {
                    setAction("Отмена") {

                        lifecycleScope.launchWhenResumed {
                            viewModel.addNote(myNote)
                        }
                        adapter.models.add(position, myNote)
                        adapter.notifyItemInserted(position)
                        binding.rvNotes.scrollToPosition(position)
                    }
                    setActionTextColor(Color.GREEN)
                }.show()
            }
        })
    }

    override fun onResume() {
        super.onResume()

        lifecycleScope.launch {
            viewModel.getAllNotes()
        }

    }

    private fun setAppLocale(languageFromPreference: String?) {
        if (languageFromPreference != null) {
            val resources: Resources = resources
            val dm: DisplayMetrics = resources.displayMetrics
            val config: Configuration = resources.configuration
            config.setLocale(Locale(languageFromPreference.lowercase(Locale.ROOT)))
            val pref = getSharedPreferences("pref", Context.MODE_PRIVATE)
            pref.edit().putString("language", languageFromPreference.toString().lowercase()).apply()
            resources.updateConfiguration(config, dm)
        }
    }


}