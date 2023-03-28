package com.example.roomdatabase.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.roomdatabase.data.Student
import com.example.roomdatabase.databinding.ItemStudentsBinding

class StudentsAdapter:RecyclerView.Adapter<StudentsAdapter.StudentViewHolder>() {

    private var onClickStudentsListener: ((Student) -> Unit)? = null

    fun onClickStudentsListener(block: (Student) -> Unit) {
        onClickStudentsListener = block
    }

    inner class StudentViewHolder(private val binding: ItemStudentsBinding): RecyclerView.ViewHolder(binding.root){
        fun bind() {
            val d = models[adapterPosition]

            binding.tvName.text = d.name
            binding.tvSurname.text = d.surname

            binding.root.setOnClickListener {
                onClickStudentsListener?.invoke(d)
            }

        }
    }

    var models = mutableListOf<Student>()
    set(value) {
        field = value
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        return StudentViewHolder(
            ItemStudentsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount() = models.size

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        holder.bind()
    }

}