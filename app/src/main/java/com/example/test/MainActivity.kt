package com.example.test

import android.app.Activity
import android.content.Intent
import android.icu.text.AlphabeticIndex
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.test.utils.loadNotes
import com.example.test.utils.persistNote
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import org.w3c.dom.Text


class MainActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var notes: MutableList<Note>
    lateinit var  adapter : NoteAdapter

    lateinit var coordinatorLayout: CoordinatorLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<MaterialButton>(R.id.create_note_fab).setOnClickListener(this)


        notes = loadNotes(this)



        adapter = NoteAdapter(notes , this)

        var recyclerView = findViewById<RecyclerView>(R.id.notes_recycler_view)

        recyclerView.layoutManager= LinearLayoutManager(this)

        recyclerView.adapter=adapter

        coordinatorLayout = findViewById<CoordinatorLayout>(R.id.coordinator_layout)


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode!=Activity.RESULT_OK || data == null ){

            return
        }

        when (requestCode) {
            NoteDetailActivity.REQUEST_EDIT_NOTE-> processEditNoteResult(data)
        }
    }



    private fun processEditNoteResult(data: Intent) {
        val noteIndex = data.getIntExtra(NoteDetailActivity.EXTRA_NOTE_INDEX , -1)

        when(data.action){
            NoteDetailActivity.ACTION_SAVE->{

                val note = data.getParcelableExtra<Note>(NoteDetailActivity.EXTRA_NOTE) ?: return
                saveNote(note, noteIndex)
            }

            NoteDetailActivity.ACTION_DELETE->{
                deleteNote(noteIndex)
            }

        }

    }




    override fun onClick(view: View) {

        if(view.tag !=null){
            showNoteDetail(view.tag as Int)
        }
        else {

            when(view.id){
                R.id.create_note_fab->createNewNote()
            }
        }

    }

    fun saveNote(note: Note , noteIndex: Int){
        persistNote(this , note)
        if(noteIndex<0){
            notes.add(0,note)
        }
        else{
            notes[noteIndex]=note
        }
//        notes[noteIndex] = note
        adapter.notifyDataSetChanged()
    }

    private fun deleteNote(noteIndex: Int) {
        if(noteIndex<0){

        }
        val note = notes.removeAt(noteIndex)
        com.example.test.utils.deleteNote(this , note)
        adapter.notifyDataSetChanged()

        Snackbar.make(coordinatorLayout , "${note.title}",Snackbar.LENGTH_SHORT)
            .show()
    }


    fun createNewNote(){
        showNoteDetail(-1)
    }

    fun showNoteDetail(noteIndex: Int){
        val note = if(noteIndex<0) Note()else notes[noteIndex]

        val intent = Intent(this , NoteDetailActivity::class.java)

        intent.putExtra(NoteDetailActivity.EXTRA_NOTE , note as Parcelable )

        intent.putExtra(NoteDetailActivity.EXTRA_NOTE_INDEX , noteIndex )

       //startActivity(intent)

        startActivityForResult(intent , NoteDetailActivity.REQUEST_EDIT_NOTE)




    }



}