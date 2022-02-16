package com.example.geolyrical

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem

class FriendActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friends)
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
            else -> return super.onOptionsItemSelected(item)
        }
    }
}
