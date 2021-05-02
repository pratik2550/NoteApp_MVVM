package com.example.mvvmarchitecture

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.note_item.view.*

class NoteAdapter(private val listener: onItemClickListener) : ListAdapter <Note, NoteAdapter.Holder>(TaskDiffCallBack()) {

//    var notes = ArrayList<Note>()

    class TaskDiffCallBack : DiffUtil.ItemCallback<Note>() {
        override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem.id == newItem.id;
        }

        override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem.title.equals(newItem.title) &&
                    oldItem.description.equals(newItem.description) &&
                    oldItem.priority == newItem.priority
        }
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val viewHolder =
            Holder(LayoutInflater.from(parent.context).inflate(R.layout.note_item, parent, false))
        viewHolder.itemView.setOnClickListener {
            if (viewHolder.adapterPosition != RecyclerView.NO_POSITION) {
                listener.onItemClick(getItem(viewHolder.adapterPosition))
            }
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val currentNote: Note = getItem(position)
        holder.itemView.tvTitle.text = currentNote.title
        holder.itemView.tvDescription.text = currentNote.description
        holder.itemView.tvPriority.text = currentNote.priority.toString()
    }

    fun getNoteAt(position: Int): Note {
        return getItem(position)
    }

}

interface onItemClickListener {
    fun onItemClick(note: Note)
}