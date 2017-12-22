# musicalChairs
This is a program, that plays wav music files, and will automatically pause them the purpose of this program is so that there is no need for someone to pause the music for the game the computer will do it automatically. Some one is only needed to start the song and choose a song which I figure can be easily automated if some one wanted, those aspects to also be automated.
# Installing
1. Either compile the code, and run it on your machine or download the jar file located in \out\artifacts\musicalChairs_jar
2.Once you start up the program for the first time it will request the location of where your music is stored, select the directory.
3. Once the directory is chosen it will load up the music into the playlist you can then double click a song to begin the game
4. The computer will eventually pause the song the time can be changed in the source code, I will probably add a way for the user to change it
5.Press Play the resume the song, or double click to start another song any time the song is paused.
# Known Bugs
Because of the way the game works as defined in the code, the game will sleep the main thread and freeze the player until the song is paused.

Make sure when you're trying to choose a directory to choose either an actual folder or a song in the target directory. Apparently using the drop down list on the top of the file explorer does not behave correctly.
# Built With
mainly the Java Swing API, File API, various listener API, javasound API, and more standard java APIs.
# Acknowledgements
Thanks to the Learn java reddit community, as this is my first java and OOP project and I had to ask a lot of questions, and try a lot of things they were there to help me understand the way the language works and now I am much better off than when I began learning Java.