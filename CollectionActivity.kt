package com.example.geolyrical

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.TextView
import org.w3c.dom.Text

class CollectionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_collections)

        val songsGuessed: TextView = findViewById(R.id.listOfSongsTxt)
        val songs = mutableListOf<String?>()
        val artists = mutableListOf<String?>()
        for (i in 0 until Lyrics.songsGuessed.size) {
            songs.add(Lyrics.songsGuessed[i])
            artists.add(Lyrics.artistsGuessed[i])
        }

        val a = songs.toString()
        val b = " - "
        val c = artists.toString()
        val d = "$a $b $c"

        songsGuessed.text = d

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        getMenuInflater().inflate(R.menu.menu_classic, menu);

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.map -> {
                startActivity(Intent(this, MapsActivity::class.java))
                return true
            }
            R.id.collection -> {
                startActivity(Intent(this, CollectionActivity::class.java))
                return true
            }
            R.id.friends -> {
                startActivity(Intent(this, FriendActivity::class.java))
                return true
            }
            R.id.mainMenu -> {
                startActivity(Intent(this, MainActivity::class.java))
                return true
            }
            R.id.mode -> {
                if (item.title == "Classic") {
                    Mode.mode = false
                    startActivity(Intent(this, MapsActivity::class.java))
                } else if (item.title == "Current") {
                    Mode.mode = true
                    startActivity(Intent(this, MapsActivity::class.java))
                }
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}
