package com.example.mvvmarchitecture

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_add_note.*

class AddEditNoteActivity : AppCompatActivity() {

    companion object {
        val EXTRA_TITLE = "com.example.mvvmarchitecture.EXTRA_TITLE"
        val EXTRA_DESCRIPTION = "com.example.mvvmarchitecture.EXTRA_DESCRIPTION"
        val EXTRA_PRIORITY = "com.example.mvvmarchitecture.EXTRA_PRIORITY"
        val EXTRA_ID = "com.example.mvvmarchitecture.EXTRA_ID"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_note)

        numberPickerPriority.minValue = 1
        numberPickerPriority.maxValue = 10

        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close)

        val intent = intent
        if (intent.hasExtra(EXTRA_ID)) {
            title = "Edit Note"
            etTitle.setText(intent.getStringExtra(EXTRA_TITLE))
            etDescription.setText(intent.getStringExtra(EXTRA_DESCRIPTION))
            numberPickerPriority.value = intent.getIntExtra(EXTRA_PRIORITY, 1)
        } else {
            title = "Add Note"
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.add_note_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.save_note -> {
                saveNote()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun saveNote() {
        val title = etTitle.text.toString()
        val description = etDescription.text.toString()
        val priority = numberPickerPriority.value

        if (title.trim().isEmpty() || description.trim().isEmpty()) {
            Toast.makeText(this, "Please insert a title and description", Toast.LENGTH_SHORT).show()
            return
        }

        val data: Intent = Intent().apply {
            putExtra(EXTRA_TITLE, title)
            putExtra(EXTRA_DESCRIPTION, description)
            putExtra(EXTRA_PRIORITY, priority)

            val id = intent.getIntExtra(EXTRA_ID, -1)
            if (id != -1) {
                putExtra(EXTRA_ID, id)
            }
        }
        setResult(RESULT_OK, data)
        finish()
    }
}