package com.example.roomdatabase.ui.main.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.roomdatabase.data.model.MyNote
import com.example.roomdatabase.databinding.ItemNotesBinding
class NotesAdapter:RecyclerView.Adapter<NotesAdapter.NoteViewHolder>() {

    private var onClickNotesListener: ((MyNote) -> Unit)? = null

    fun onClickNoteListener(block: (MyNote) -> Unit) {
        onClickNotesListener = block
    }

    inner class NoteViewHolder(private val binding: ItemNotesBinding): RecyclerView.ViewHolder(binding.root){
        fun bind() {
            val d = models[adapterPosition]

            binding.tvName.text = d.name
            binding.tvSurname.text = d.surname

            binding.root.setOnClickListener {
                onClickNotesListener?.invoke(d)
            }

        }
    }

    var models = mutableListOf<MyNote>()
    set(value) {
        field = value
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder(
            ItemNotesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount() = models.size

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bind()
    }

}