# COMP2042_CW_hfyfe1
FARAH TAMER FATHY AHMED ELSAID
20406567
-------------------------------------------------------------------------------------------------------------------------------------------------------------------------

This is a README.md file that is a part of the assignment instructions required for the Coursework of the module Software Maintenance.

-------------------------------------------------------------------------------------------------------------------------------------------------------------------------

This Readme.md file will contain:
1. Compilation Instructions
2. Features Implemented and Working Properly
3. Features Implemented but Not Working Properly
4. Features Not Implemented
5. New Java Classes
6. Modified Java Classes
7. Unexpected Problems 

-------------------------------------------------------------------------------------------------------------------------------------------------------------------------

COMPILATION INSTRUCTIONS
------------------------

IDE: IntelliJ IDEA 2023.2.5 (Ultimate Edition)

Java Version: 21.0.1

JUnit Version: 4

JavaFX Version: 21.0.1+6

Runtime version: 17.0.9+7-b1000.46 amd64

VM: OpenJDK 64-Bit Server VM by JetBrains s.r.o.

Kotlin: 232-1.9.0-IJ10227.8

How to import and run:
----------------------

1. Download the zip folder.
2. Unzip/extract the folder into the location of your choice.
3. Open IntelliJ.
4. Select Open if the Welcome screen opens (Or File -> Open from the menu).
5. Go to the directory where the Brick Breaker folder is inside the unzipped folder (make sure the imported folder is the Brick Breaker folder).
6. Click Open.
7. Once you opened the folder, click on File in the top menu of IntelliJ.
8. Click on Project Structure.
9. Click on Libraries.
10. Make sure to add the library where your Java SDK is downloaded.
11. Click Okay.
12. Click on Run in the menu at the top of IntelliJ.
13. Click on Edit Configurations.
14. Click on the Plus "+" sign in the top left corner.
15. Click on Application.
16. Name the application whatever name you'd like. For example: Brick Breaker.
17. Choose the main class. It should appear as "Main of brickGame" when you click on it.
18. Click modify options in the build and run section.
19. Click on Add VM Options.
20. Add this into the VM options if you're on Windows: --module-path "\path\to\javafx-sdk-21.0.1\lib" --add-modules javafx.controls,javafx.fxml
    Make sure to replace "\path\to\javafx-sdk-21.0.1\lib" with the actual path to your Java FX SDK lib folder.
    For example this was mine: --module-path "C:\Java\javafx-sdk-21.0.1\lib" --add-modules javafx.controls,javafx.fxml,javafx.media 
21. Click Okay.
22. Click on Run to run the game.

-------------------------------------------------------------------------------------------------------------------------------------------------------------------------

FEATURES IMPLEMENTED AND WORKING PROPERLY
-----------------------------------------
1. Main Menu Screen

The main menu screen that appears when you run the game has a background and 5 buttons.
The first button allows you to start the game.
The second button allows you to load a saved game.
The third button is a "How to play" button that, when clicked, directs you to a different screen where there are instructions on how to play the game and the points system.
The fourth button is an exit game button that allows you to exit.
The fifth button is in the shape of a music note; it allows you to switch on and switch off the background music.

2. Music
   
I added background music to the game. You can choose to switch off the music at the beginning of the game on the main menu. Additionally, once you complete a level, a sound plays.

3. Colour changes
   
I added different images for most of the blocks and changed the colors of the normal blocks, as well as the background colors of the game while it's running and when you hit the star block. Additionally, when a player gains a score, a "+1" appears on the screen in the color green, and when a player loses a heart, a "-1" appears on the screen in the color red.

4. New Speed Block
   
Added a new block called BLOCK_SPEED that, when hit, increases the speed of the ball for a few seconds.

5. Game Over Sreen
   
I added a game over screen that appears when the user has finished all the hearts. The screen has two buttons: one to restart the game and one to exit the game.

6. Initial Ball Position

I changed the initial position of the ball whenever the game starts or a level loads to always be the center of the screen.

7. Next Level Screen

I added a next-level screen that appears every time you finish a level (i.e., destroy all the bricks) that keeps track of and displays the current level and gives you an option to go to the next level or exit the game.

8. Different Levels

Player has the ability to progress to next levels and the number of blocks increases every time the user progresses to the next level.

-------------------------------------------------------------------------------------------------------------------------------------------------------------------------

FEATURES IMPLEMENTED BUT NOT WORKING PROPERLY
---------------------------------------------

1. Splitter Block

I attempted to add a new block called BLOCK_SPLITTER. This block is designed to duplicate the ball, allowing the player to control two balls for a few seconds. While the ball successfully duplicates upon impact, the duplicated ball merely mirrors the movement of the original ball, and collision detection between the ball and the bricks does not apply to it.
Initially, I observed that the ball was duplicating, but remained hidden behind the original ball.
In an attempt to resolve this issue, I experimented with inducing movement in the opposite direction, which unfortunately caused the game to freeze.
Subsequently, I decided to change the ball's physics entirely by creating a dedicated method for it.
While this approach successfully duplicated the ball, it mirrored the behavior of the original ball.
Unfortunately, I faced challenges when trying to implement collision detection for the duplicated ball.
My attempts to invoke the setPhysicsToBall() method within its own physics function proved unsuccessful.
Additionally, modifying the duplicated ball's physics to handle collision detection posed difficulties and introduced further complications to the game.

-----------------------------------------------------------------------------------

FEATURES NOT IMPLEMENTED
------------------------

1. Different Block Arrangements
   
I aimed to modify the game to feature unique block arrangements for each level, such as a triangle, rectangle, or other distinctive shapes, deviating from the typical block layout. I left this out because the block visibilities proved to be an issue. The ball would collide with invisilble blocks and obey collision detection logic wrongly. It would also sometimes completely ignore collision detection and pass through blocks without causing them to disappear. Additionally, the prospect of creating 18 different level shapes appeared time-consuming.

2. More Music
   
I wanted to enhance the gaming experience by adding different musical tracks for the next level screen and the game-winning moment. I decided to leave this out because I had other priorities such as refining gameplay mechanics, addressing bugs and in game errors, and development of other new features (like the speed block) that seemed more interesting and could enhance gameplay better.

-----------------------------------------------------------------------------------

NEW JAVA CLASSES
----------------

1. Music

The purpose of the Music class is to handle the playing and management of background music in the game. It encapsulates the functionality related to audio playback, providing methods to start, stop, and check the status of the music.
This class exists in the brickGame folder ( src -> main -> java-> brickGame), it is initalized  and used in the start method, nextLevel() method, and the restartGame() method in the Main class. This class uses .mp3 files that are saved in a folder called "music" that is in the same location as the class itself.

2. HelpScreenView

The HelpScreenView class is responsible for creating and managing the help screen in the game. It provides information on how to play, utilizing background images and navigation buttons. The class includes functionality for transitioning between the main help screen and a second page explaining the points system. Overall, it serves as a user guide, enhancing the player's understanding of the game's mechanics. This class exists in the brickGame folder ( src -> main -> java-> brickGame), it is initalized  and used in the start method in the Main class. This class uses new images which are saved in the resources folder in the game ( src -> main -> resources), alongside with the other images.

3. InitiateGame

The InitiateGame class is a refactored class from the original source code's main class and it manages the initialization of game elements in a brick-breaking game. It contains all the Init classes and handles the setup of the ball, bricks (breakable blocks), and the game board. The class initializes the ball with a specified appearance, sets its initial coordinates, and creates bricks with diverse types and colors. Additionally, it populates the game board based on the current level, introducing various block types such as normal, special (e.g., heart, star, speed), and potential splitters. This class exists in the brickGame folder ( src -> main -> java-> brickGame), it is initalized  and used in the start method in the Main class.

-------------------------------------------------------------------------------------------------------------------------------------------------------------------------

MODIFIED JAVA CLASSES
---------------------

1. Block.java

I introduced two new block types: BLOCK_SPEED and BLOCK_SPLITTER
In my draw() method, I included additional conditions for those new block types, to set their fill based on their corresponding images.
These changes were necessary to implement the new features that I added.

I modified the checkHitToBlock() method to use geometric calculations for more precise collision detection, considering the radius of the ball.
This was necessary to improve collision detection accuracy 

I changed the "paddingH" variable value to be 30 instead of 50.  It is the variable used to set the horizontal padding for the blocks in the game grid and  determines the space between the left edge of the game grid and the first column of blocks. I changed it for better visual alignment in the game grid.

2. GameEngine.java

I opted for a more efficient approach using JavaFX's AnimationTimer to manage game updates and physics updates. This provides a dedicated handle method for updates, making the integration with JavaFX smoother. I also chose to interrupt threads using AnimationTimer's stop method instead of directly calling stop() on threads, ensuring a controlled and safer thread termination.

For time management, I utilized AnimationTimer for time updates, offering a straightforward and potentially more accurate way to handle time compared to the separate thread (timeThread) and Thread.sleep(1) used in the source code. Additionally, I employed synchronization through the synchronized keyword and an object (lock) for efficient pausing and resuming of the game loop.

These changes were implemented to enhance readability, maintainability, and overall performance of the game engine, aligning it more closely with JavaFX's features.

3. Main.java

I introduced UI elements such as buttons, box layouts, labels, and more. These additions enable actions like loading/saving the game, initiating new games, and displaying the help screen. In contrast, the source code directly launched the game, but I encapsulate it within UI 

I used JavaFX AnimationTimers and asynchronous threads for tasks like brick movement and game saving, aligning with best practices for JavaFX applications. To address potential concurrency issues, I incorporated more synchronization blocks when accessing shared data structures like the chocos array.

I used try-catch blocks, to wrap risky sections and log errors appropriately. This contrasts with the source code, which had some unhandled exceptions. Ensuring proper error handling is crucial for a more robust application.

To make the game more immersive and engaging, I introduced features such as speed boosts, duplicate blocks, next level screen, background music and sound effects, and game over screen. The ability to save/load game states adds the convenience of resuming progress.

I refactored some functionalities into separate classes like InitiateGame and Score, enhancing separation of concerns and modularity. 

I implemented the essential feature of restarting/resetting the game, which was lacking in the source code. This addition significantly enhances the game's usability and player experience.


4. Score.java

I added CSS styling for labels in the showGameOver method to make the "Game Over" message visually appealing and customizable.

In the showGameOver method, I utilized a VBox to organize the "Game Over" message and buttons, providing better alignment and spacing.

I used Java's logging framework to log exceptions in case of interruption in the animation thread. This ensures better error handling and aids in debugging.

I encapsulated the addition and removal of labels to/from the root in Platform.runLater blocks to ensure these operations are performed on the JavaFX Application Thread. This is essential for thread safety in JavaFX applications.

-------------------------------------------------------------------------------------------------------------------------------------------------------------------------

UNEXPECTED PROBLEMS
-------------------

In the course of the project, I encountered an unexpected challenge manifested in a java.lang.IndexOutOfBoundsException error. This error surfaced during the execution of the program when the ball collided with a block. The root cause of the issue was traced to the method checkDestroyedCount(), specifically when attempting to access elements in the blocks list. 

To address this challenge, I implemented debugging techniques to gain insights into the values of key variables. By introducing print statements to output the size of the blocks list and the destroyedBlockCount variable, I aimed to identify the discrepancy leading to the index out-of-bounds error. Through this iterative process, I intended to pinpoint the specific conditions under which the error occurred and gain a clearer understanding of the root cause. This why I added the removeNodeById() method, to ensure correct synchronization of threads.  

This debugging approach serves as a systematic method to isolate the issue and facilitates a more informed resolution strategy. As part of ongoing efforts to enhance the robustness of the code, this error identification and debugging process contribute to the overall stability and reliability of the application.

Additionally, I wrapped important and risky sections of code with a try/catch method and properly handled exceptions.

------------------------------------------------------------------------------------------------------------------------------------------------------------------------- 
