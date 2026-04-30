# TypingRaceSimulator

A competitive typing speed simulator where multiple typists race to complete passages of text.

---

## 📋 Project Overview

TypingRaceSimulator is a Java-based application that simulates competitive typing races between multiple competitors. The project is split into two implementations:

- **Part 1:** Console-based textual simulation with command-line interface
- **Part 2:** Full graphical user interface (GUI) with advanced features and metrics

### Key Features

✅ **Realistic Typing Mechanics**
- Accuracy-based mistype probability
- Character-by-character progression
- Sliding backwards on mistakes
- Burnout events that temporarily block typing

✅ **Customizable Typists**
- Individual accuracy ratings (0.0 - 1.0)
- Customizable typing speed
- Configurable typing styles and keyboard types
- Energy drink boosts for performance enhancement

✅ **Global Gameplay Modifiers**
- Autocorrect Mode: Reduces penalty for mistypes
- Caffeine Mode: Increases typing speed over time
- Night Mode: Reduces accuracy for all typists

✅ **Performance Metrics**
- Words per minute (WPM)
- Accuracy tracking
- Burnout statistics
- Complete race history

---

## 📁 Project Structure

```
TypingRaceSimulator/
├── Part1/                          # Console-based simulation
│   ├── TypingRace.java            # Main race engine
│   ├── Typist.java                # Individual typist model
│   ├── Testing.java               # Test cases
│   └── Utilities.java             # Helper functions
│
└── Part2/                          # GUI-based simulation
    ├── TypingRaceSimulation.java   # Advanced race simulation engine
    ├── TypistSimulation.java       # Enhanced typist model
    ├── TypingRaceGUI.java          # Main GUI application
    ├── Menu.java                   # Base menu class
    ├── GameInfo.java               # Game state management
    ├── RacingHistory.java          # Historical race data
    ├── PerformanceMetric.java      # Performance statistics
    ├── HelperUtilities.java        # GUI utility functions
    └── TypeSimulation.java         # Simulation utilities
```

---

## 🚀 Getting Started

### Part 1 — Console Application

#### Compile

```bash
cd Part1
javac *.java
```

#### Run

```bash
java TypingRace
```

The program will prompt you to:
1. Choose a passage length
2. Configure typists (name, symbol, accuracy)
3. Start the race and watch typists compete

You can run multiple races in one session.

### Part 2 — GUI Application

#### Compile

```bash
cd Part2
javac *.java
```

#### Run

```bash
java TypingRaceGUI
```

A graphical window will open allowing you to:
- Configure typists with advanced options
- Enable/disable global game modes
- Visualize races in real-time
- View detailed performance metrics
- Access race history

You can run multiple races in one session
---

## 🎮 Game Mechanics

### Basic Race Flow

1. **Setup Phase:**
   - Define passage length (number of characters)
   - Configure each typist's stats
   - Set global game modes

2. **Racing Phase:**
   - Each typist types character-by-character per turn
   - Success depends on their accuracy rating
   - On failure: character progress slides backward

3. **Completion:**
   - First typist to reach passage length wins
   - Race records finishing order for all participants
   - Performance metrics are calculated

### Key Mechanics

| Event | Effect | Trigger |
|-------|--------|---------|
| **Successful Type** | Progress +1 character | Accuracy roll succeeds |
| **Mistype** | Progress -2 or -1 characters | Accuracy roll fails |
| **Burnout** | Typist frozen for 3+ turns | Random low-accuracy event |
| **Autocorrect** | Mistype penalty reduced | Global mode enabled |
| **Caffeine** | Typing speed increases | Global mode enabled |
| **Night Mode** | Accuracy reduced by 5% | Global mode enabled |

---

## 🏗️ Class Overview

### Part 1 Core Classes

**TypingRace**
- Manages the main race simulation loop
- Handles typist progression and win conditions
- Processes mistype and burnout events
- Provides race statistics and results

**Typist**
- Represents an individual competitor
- Tracks progress, accuracy, and burnout state
- Calculates performance metrics
- Manages state between races

**Utilities** & **Testing**
- Input validation and user interaction
- Test cases for race mechanics

### Part 2 Core Classes

**TypingRaceSimulation**
- Extended race engine with advanced mechanics
- Global game mode application
- Performance metric calculation
- Finishing order determination

**TypistSimulation**
- Enhanced typist model with customization options
- Tracking typing style and keyboard type
- Energy drink effects
- Detailed statistics collection

**TypingRaceGUI**
- Main Swing application window
- Race visualization and control
- Menu navigation

**GameInfo**
- Centralized game state management
- Configuration storage
- Shared data between menu screens

**RacingHistory** & **PerformanceMetric**
- Historical data persistence
- Performance statistics calculation
- WPM and accuracy analysis

**Menu** & **HelperUtilities**
- Reusable UI components
- GUI helper functions

---

## 💡 Usage Example (Part 1)

```java
// Create a race with 500-character passage
TypingRace race = new TypingRace(500);

// Add typists
race.configureTypists(scanner); // Interactive setup

// Run the race
race.startRace();

// Results are displayed automatically
```

---

## 📊 Performance Metrics

The application calculates:
- **Words Per Minute (WPM):** Typed characters divided by time, scaled to words
- **Accuracy:** Correct characters / total characters attempted
- **Burnout Count:** Number of times a typist experienced burnout
- **Finishing Position:** Final race rank

---

## 🛠️ Development Notes

- **Primary Developer:** Kishal Chhetri
- **Language:** Java 25
- **GUI Framework:** Swing 

---
