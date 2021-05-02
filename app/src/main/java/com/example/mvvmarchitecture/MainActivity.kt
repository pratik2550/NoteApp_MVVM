package com.example.mvvmarchitecture

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.SimpleCallback
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity(), onItemClickListener {

    val ADD_NOTE_ACTIVITY: Int = 1
    val EDIT_NOTE_ACTIVITY: Int = 2

    private lateinit var viewModel: NoteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_add_note.setOnClickListener {
            val intent = Intent(this, AddEditNoteActivity::class.java)
            startActivityForResult(intent, ADD_NOTE_ACTIVITY)
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.hasFixedSize()

        val adapter = NoteAdapter(this)
        recyclerView.adapter = adapter

        viewModel = ViewModelProvider(
            this, ViewModelProvider.AndroidViewModelFactory.getInstance(
                application
            )
        )
            .get(NoteViewModel::class.java)
        viewModel.getAllNotes().observe(this, Observer {
            adapter.submitList(it)
        })

        val mIth = ItemTouchHelper(
            object : SimpleCallback(
                0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            ) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: ViewHolder, target: ViewHolder
                ): Boolean {
                    return false // true if moved, false otherwise
                }

                override fun onSwiped(viewHolder: ViewHolder, direction: Int) {
                    // remove from adapter
                    GlobalScope.launch(Dispatchers.Main) {
                        viewModel.delete(adapter.getNoteAt(viewHolder.adapterPosition))
                    }
//                    Toast.makeText(this, "Note deleted", Toast.LENGTH_SHORT)
                    Toast.makeText(this@MainActivity, "Note deleted", Toast.LENGTH_SHORT).show()
                }
            })

        mIth.attachToRecyclerView(recyclerView)

    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == ADD_NOTE_ACTIVITY && resultCode == RESULT_OK) {
            val title = data?.getStringExtra(AddEditNoteActivity.EXTRA_TITLE)
            val description = data?.getStringExtra(AddEditNoteActivity.EXTRA_DESCRIPTION)
            val priority = data?.getIntExtra(AddEditNoteActivity.EXTRA_PRIORITY, 1)

            val note: Note = Note(0, title, description, priority)

            GlobalScope.launch(Dispatchers.Main) {
                viewModel.insert(note)
            }

            Toast.makeText(this, "Note saved", Toast.LENGTH_SHORT).show()
        } else if (requestCode == EDIT_NOTE_ACTIVITY && resultCode == RESULT_OK) {
            val id = data?.getIntExtra(AddEditNoteActivity.EXTRA_ID, -1)

            if (id == -1) {
                Toast.makeText(this, "Note can't be updated", Toast.LENGTH_SHORT).show()
                return
            }

            val title = data?.getStringExtra(AddEditNoteActivity.EXTRA_TITLE)
            val description = data?.getStringExtra(AddEditNoteActivity.EXTRA_DESCRIPTION)
            val priority = data?.getIntExtra(AddEditNoteActivity.EXTRA_PRIORITY, 1)

            val note: Note = Note(id!!, title, description, priority)

            GlobalScope.launch(Dispatchers.Main) {
                viewModel.update(note)
            }

            Toast.makeText(this, "Note Updated", Toast.LENGTH_SHORT).show()

        } else{
            Toast.makeText(this, "Note not saved", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.deleteAllNotes -> {
                GlobalScope.launch(Dispatchers.Main) {
                    viewModel.deleteAllNotes()
                }
                Toast.makeText(this@MainActivity, "All notes deleted", Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onItemClick(note: Note) {
        val intent = Intent(this, AddEditNoteActivity::class.java)
        intent.putExtra(AddEditNoteActivity.EXTRA_TITLE, note.title)
        intent.putExtra(AddEditNoteActivity.EXTRA_DESCRIPTION, note.description)
        intent.putExtra(AddEditNoteActivity.EXTRA_PRIORITY, note.priority)
        intent.putExtra(AddEditNoteActivity.EXTRA_ID, note.id)
        startActivityForResult(intent, EDIT_NOTE_ACTIVITY)
    }
}