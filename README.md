# Brick Game (JavaFX)

A simple brick-breaker style game built using JavaFX 21. The game includes level progression, bonus blocks, a save and load system, and a custom-built game engine using multi-threaded updates.

---

## Features

- Paddle and ball gameplay with collision detection and basic physics  
- Power-ups and bonus blocks (choco, star, heart)  
- Save and load functionality  
- Multiple levels with incremental difficulty  
- Thread-safe game loop (no deprecated Thread.stop)  
- Resource-based design (images, CSS, and assets)  

---

## Technology Stack

| Component | Version / Tool |
|------------|----------------|
| Programming Language | Java 21 |
| UI Framework | JavaFX 21.0.4 |
| Build System | Apache Maven 3.9+ |
| IDE (Recommended) | Visual Studio Code |

---

## Project Structure

```
brickGame/
├── src/
│   ├── main/
│   │   ├── java/brickGame/        # Main source code
│   │   └── resources/brickGame/   # Images, CSS, and FXML files
│   └── test/                      # Future unit tests
├── pom.xml
└── README.md
```

---

## Running the Game

### Option 1: Using Maven (Recommended)
Open a terminal inside the project folder and run:
```bash
mvn clean javafx:run
```

### Option 2: Using Visual Studio Code
If you have a `.vscode/launch.json` configuration:
1. Click **Run → Start Debugging**
2. Select **Run BrickGame**

---

## Game Controls

| Key | Action |
|-----|---------|
| Left / Right Arrow | Move the paddle |
| S | Save the game |
| Down Arrow | Reserved for testing |

---

## Save and Load System

When you press **S**, your progress is saved automatically to:

- On Windows:  
  `C:\Users\<username>\brickgame\save.mdds`
- On macOS/Linux:  
  `~/brickgame/save.mdds`

You can resume from that save next time you launch the game.

---

## Development Notes

- Updated `GameEngine` to use interrupt-safe loops and thread handling  
- Removed deprecated `Thread.stop()` usage  
- Replaced hard-coded file paths with a platform-independent user home path  
- Moved resource files into `src/main/resources/brickGame`  
- Updated `pom.xml` to JavaFX 21.0.4  
- Added Maven plugin for easy build and run  

---

## Future Improvements

- Add unit tests for collisions, physics, and save/load features  
- Refine UI layout and add animations  
- Improve paddle and ball physics  
- Add background music and sound effects  
- Include screenshots or GIFs in this README  
- Add difficulty settings and power-up logic improvements  

---

## License

This project is created for educational and demonstration purposes.
