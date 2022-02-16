Android app written in Kotlin which uses Google Maps to walk around Swansea University campus 
and collect lyrics.

Run the emulator with the normal Gradle sync files to begin using the application.

MainActivity - 
    Used EditText properties to allow the user to enter a username and password to which the app then
    checks to see if it is valid, it does this by using findViewById. If the credentials are correct
    the MapActivity is loaded, otherwise a clear message indicating the attempt was unsuccessful is 
    displayed.

MapsActivity -
    I used Tom's code sample provided to get the functionality of the google Maps Api working. 
    I used a discovery distance to only get markers within a certain distance of the devices location.

    I used boolean a boolean value to differ between "classic" and "current" modes, this boolean value
    is parsed into a selectSong() method which chooses a random number between 0 and the size of the 
    asset list containing the songs. The chosen song value is then parsed into a placeMarkers() method,
    this again uses the current mode to determine which arrays to add the data too, the markers are
    assigned data by using the 'tag' property which is the chosen song. This method also calls one of 
    the read methods, The read method goes through the chosen song and initially reads item, Random 
    value's are then generated to determine which lines of the song to use, this is then used to add to
    the array for later use, the song name and artist of the song are also pinpointed in this method 
    using a 'split' and 'replace' function, the artist and song name are added to an object to be used 
    by the GuessActivity class.

    When a marker is selected once the mode is determined and an add to collected method is
    called with the marker as parameter, this method checks all lyrics to find the lyric that matches
    the markers 'tag'. The markers tag is then added to a new collected lyric array in the position 
    it was found in the lyric array, a value also increments by 1 with each found lyric.
    This process repeats for all lyrics.

    When the user wants to make a guess it is first checked that the incremented value is not 0, if so
    the user has collected no lyrics and may not proceed to the GuessActivity. If the value is not 0 
    then there is an array in the object class called Lyrics which becomes the collected lyrics array,
    this is to be used in the GuessActivity class.

GuessActivity - 
    The lyrics text view on this activity is initially populated with lyrics from the object "Lyrics",
    the null values are removed from the string and the strings are always in correct order due to the
    fixed positioning I have used in MapsActivity.

    A value is used to decrement when the user makes incorrect guesses, aslong as this value is not 0  
    then the user can continue to keep guessing, if the value is 0 then a message is prompted to the 
    user that they have run out of attempts. If the user guesses both song and artist correctly then 
    this is added to their collections screen which is achievable with an ArrayList in the object 
    "Lyrics".

CollectionActivity - 
    This class takes in the object ArrayList and concatenates the strings found to produce a single 
    string containing just the artist and song name, this then populates the textview immediately.

menu -
   Used for direct access to activities.
   The mode is selected in the menu with the default being "classic", this works by checking the
   current item title and changing it to the opposite while changing the object "Mode"'s property.

Object Lyrics - 
    Stores the lyrics collected, songs guessed, artists guessed, artist of current song instance,
    the name of the current song instance. Used as a reference between classes for instances.
    

Object Mode - 
    Used to determine mode switch in the app with a boolean variable.







 





















