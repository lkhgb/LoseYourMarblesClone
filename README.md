LoseYourMarblesClone
====================

This project is a partial remake of SegaSoft's 1997 puzzle game, Lose Your Marbles. It is written in Java using the game development framework libGDX, v0.9.9 (http://libgdx.badlogicgames.com/releases/).

LibGDX's project generator can create project files for popular IDEs such as Eclipse (https://github.com/libgdx/libgdx/wiki/Project-Setup-Gradle). Note, I started this project with the outdated process (https://github.com/libgdx/libgdx/wiki/Project-setup%2C-running-%26-debugging), and libGDX has since migrated to a new pipeline.

After generating the projects, I used the Eclipse ADT bundle (http://developer.android.com/sdk/index.html) to import them. Build v22.2.1-833290 of the Eclipse ADT bundle was used for development. The version of the Android SDK used was 4.3 (API 18).

The Eclipse projects I generated were as follows:
lose-your-marbles-clone
lose-your-marbles-clone-desktop
lose-your-marbles-clone-android

The files included in the repository should be linked to the project directories as follows:
lose-your-marbles-clone/src
lose-your-marbles-clone-desktop/src/com/j5backup/loseyourmarblesclone/Main.java
lose-your-marbles-clone-android/src/com/j5backup/loseyourmarblesclone/MainActivity.java
lose-your-marbles-clone-android/assets
lose-your-marbles-clone-android/AndroidManifest.xml
lose-your-marbles-clone-android/res/drawable
lose-your-marbles-clone-android/res/values/strings.xml

NOTE: The code has not been tested with the latest versions of libGDX, Android, or Eclipse; only with the versions listed above.

The source code was written for learning purposes and is released under the BSD-3 license, as specified in the included LICENSE file.

The art, sound, and music assets were created specifically for the game and are released under the Creative Commons Attribution By license (CC Attribution BY), as specified in the included LICENSE file.

Have fun losing your marbles with this project!

-j5backup