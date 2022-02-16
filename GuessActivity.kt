package com.example.geolyrical

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_guess.*

class GuessActivity : AppCompatActivity() {
    var attempts : Int = 3
   override fun onCreate(savedInstanceState: Bundle?) {
       super.onCreate(savedInstanceState)
       setContentView(R.layout.activity_guess)

       val songLyrics: TextView = findViewById(R.id.guessSongText)
       val allLyrics = mutableListOf<String?>()
       for (i in 0 until Lyrics.lyricsCollected.size -1) {
           allLyrics.add(Lyrics.lyricsCollected[i])
       }
       var lyrics = allLyrics.toString()
       lyrics = lyrics.replace("null,", "")
       lyrics = lyrics.replace("null", "")
       songLyrics.text = lyrics

       val giveUpButton: Button = this.findViewById(R.id.giveUpBtn) as Button
       giveUpButton.setOnClickListener {
           startActivity(Intent(this, MapsActivity::class.java))
       }

       val guessSong = findViewById<EditText>(R.id.guessSongTxt)
       guessSong.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
           if (actionId == EditorInfo.IME_ACTION_DONE) {
               verifyGuess()
           }
           false
       })

   }

    private fun verifyGuess() {
        val guessArtist = findViewById<EditText>(R.id.guessArtistTxt).text
        val guessSong = findViewById<EditText>(R.id.guessSongTxt).text

        if (attempts == 0) {
            Toast.makeText(this, "No more guesses remaining", Toast.LENGTH_SHORT).show()
        } else {
        if (guessArtist.toString() == Lyrics.artistOfSong) {
            findViewById<EditText>(R.id.guessArtistTxt).isFocusable = false
            if (guessSong.toString() == Lyrics.songName) {
                findViewById<EditText>(R.id.guessArtistTxt).isFocusable = false
                Toast.makeText(this, "Correct guess", Toast.LENGTH_SHORT).show()
                Lyrics.songsGuessed.add(Lyrics.songName)
                Lyrics.artistsGuessed.add(Lyrics.artistOfSong)
                startActivity(Intent(this, MapsActivity::class.java))
            }
        } else {
            Toast.makeText(this, "Artist and Song incorrect", Toast.LENGTH_SHORT).show()

            try {
                val guessCount = findViewById<TextView>(R.id.guessesRemaining).text
                val count: Int = guessCount.toString().toInt()
                guessesRemaining.text = (count - 1).toString()
                attempts--
            } catch (e: NumberFormatException) {
                // not a valid int
            }
        }
        }
    }

    private fun readLyrics() {
        for (i in 0 until Lyrics.lyricsCollected.size -1) {
            print(Lyrics.lyricsCollected[i])
        }
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
