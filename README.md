<!-- Improved compatibility of back to top link: See: https://github.com/othneildrew/Best-README-Template/pull/73 -->
<a id="readme-top"></a>



<!-- PROJECT SHIELDS -->
[![Contributors][contributors-shield]][contributors-url]
[![Forks][forks-shield]][forks-url]
[![Stargazers][stars-shield]][stars-url]
[![Issues][issues-shield]][issues-url]
[![MIT License][license-shield]][license-url]



<!-- PROJECT LOGO -->
<br />
<div align="center">
  <a href="https://github.com/Rudra-P9/Infinite-Stress-Loop-Escape-Room">
    <img src="https://img.shields.io/badge/%F0%9F%94%93-Escape%20Room-purple?style=for-the-badge" alt="Logo" height="40">
  </a>

  <h3 align="center">Infinite Stress Loop — Escape Room</h3>

  <p align="center">
    A feature-rich, text-driven Java escape room game where every second counts.
    <br />
    <a href="https://github.com/Rudra-P9/Infinite-Stress-Loop-Escape-Room/tree/main/docs"><strong>Explore the docs »</strong></a>
    <br />
    <br />
    <a href="https://rudra-p9.github.io/Infinite-Stress-Loop-Escape-Room/">View Prototype</a>
    &middot;
    <a href="https://www.youtube.com/watch?v=8z9kc37HaFE">Watch Presentation</a>
    &middot;
    <a href="https://github.com/Rudra-P9/Infinite-Stress-Loop-Escape-Room/issues/new?labels=bug">Report Bug</a>
    &middot;
    <a href="https://github.com/Rudra-P9/Infinite-Stress-Loop-Escape-Room/issues/new?labels=enhancement">Request Feature</a>
  </p>
</div>



<!-- TABLE OF CONTENTS -->
<details>
  <summary>Table of Contents</summary>
  <ol>
    <li>
      <a href="#about-the-project">About The Project</a>
      <ul>
        <li><a href="#built-with">Built With</a></li>
      </ul>
    </li>
    <li>
      <a href="#getting-started">Getting Started</a>
      <ul>
        <li><a href="#prerequisites">Prerequisites</a></li>
        <li><a href="#installation">Installation</a></li>
      </ul>
    </li>
    <li><a href="#gameplay">Gameplay</a></li>
    <li><a href="#features">Features</a></li>
    <li><a href="#project-structure">Project Structure</a></li>
    <li><a href="#roadmap">Roadmap</a></li>
    <li><a href="#contributing">Contributing</a></li>
    <li><a href="#license">License</a></li>
    <li><a href="#contact">Contact</a></li>
    <li><a href="#acknowledgments">Acknowledgments</a></li>
  </ol>
</details>



<!-- ABOUT THE PROJECT -->
## About The Project

**Infinite Stress Loop** is a fully-featured escape room game built in Java with a JavaFX GUI. Players are dropped into a tense, ticking-clock scenario where they must navigate multiple interconnected rooms, solve a variety of puzzles (riddles, number ciphers, word games, audio clues), manage their inventory, and unlock the story before time runs out.

Every hint you request chips away at your score. Think carefully, explore thoroughly, and escape the loop before it's too late.

> **Can you escape the loop before time runs out?**

🎬 **Demo Video:** [Escape The Varen Project](https://youtu.be/DhgokPhCjug)  
📽️ **Full Presentation:** [Infinite Stress Loop Presentation](https://www.youtube.com/watch?v=8z9kc37HaFE)  
🌐 **Live Prototype:** [GitHub Pages](https://rudra-p9.github.io/Infinite-Stress-Loop-Escape-Room/)

<p align="right">(<a href="#readme-top">back to top</a>)</p>



### Built With

* [![Java][Java-badge]][Java-url]
* [![JavaFX][JavaFX-badge]][JavaFX-url]
* [![Maven][Maven-badge]][Maven-url]
* [![JSON Simple][JSON-badge]][JSON-url]
* [![FreeTTS][FreeTTS-badge]][FreeTTS-url]
* [![JUnit][JUnit-badge]][JUnit-url]

<p align="right">(<a href="#readme-top">back to top</a>)</p>



<!-- GETTING STARTED -->
## Getting Started

Follow these steps to get a local copy up and running.

### Prerequisites

* **Java 21** (JDK)
  ```sh
  java -version   # should report 21.x.x
  ```
* **Apache Maven 3.8+**
  ```sh
  mvn -version
  ```
* **JavaFX 21** — handled automatically via Maven dependencies (no separate install needed).

### Installation

1. Clone the repository
   ```sh
   git clone https://github.com/Rudra-P9/Infinite-Stress-Loop-Escape-Room.git
   ```
2. Navigate into the project directory
   ```sh
   cd Infinite-Stress-Loop-Escape-Room/escaperoom
   ```
3. Build the project with Maven
   ```sh
   mvn clean install
   ```
4. Run the application
   ```sh
   mvn clean javafx:run
   ```

<p align="right">(<a href="#readme-top">back to top</a>)</p>



<!-- GAMEPLAY -->
## Gameplay

1. **Create an Account** — Register a username and password. Your progress and scores are saved per profile.
2. **Choose a Difficulty** — Easy, Medium, or Hard. Higher difficulty means less time and fewer hints.
3. **Explore the Rooms** — Navigate through multiple themed rooms (Fragment Corridor, Chamber Hall, and more) using on-screen controls.
4. **Solve Puzzles** — Encounter a variety of puzzle types:
   - 🔢 Number Puzzles
   - 📖 Riddle Puzzles
   - 🔤 Word Puzzles
   - 🎵 Audio Puzzles (with text-to-speech narration via FreeTTS)
5. **Manage Your Inventory** — Pick up and use items found throughout the rooms to unlock doors and progress the story.
6. **Use Hints Wisely** — Each hint deducts points from your final score.
7. **Beat the Timer** — Escape before the countdown hits zero to claim your spot on the leaderboard!

<p align="right">(<a href="#readme-top">back to top</a>)</p>



<!-- FEATURES -->
## Features

- 🔐 **Account System** — Create, log in, and save progress per user account.
- ⏱️ **Countdown Timer** — Real-time timer that ramps up the tension.
- 🗺️ **Multi-Room Navigation** — Multiple distinct rooms with unique puzzles and story elements.
- 🧩 **Diverse Puzzle Types** — Number, riddle, word, and audio-based challenges.
- 🎒 **Inventory Management** — Pick up items; use them at the right moments to progress.
- 💡 **Hint System** — Request hints at the cost of your score.
- 🏆 **Leaderboard** — Compare scores across all players.
- 🎙️ **Text-to-Speech Narration** — Story and clues are narrated via FreeTTS for immersion.
- 💾 **Persistent Save Data** — Game state is serialized and restored via JSON.
- 🎨 **JavaFX GUI** — Fully interactive graphical interface built with FXML and CSS styling.

<p align="right">(<a href="#readme-top">back to top</a>)</p>



<!-- PROJECT STRUCTURE -->
## Project Structure

```
Infinite-Stress-Loop-Escape-Room/
├── docs/                        # UML diagrams, sequence diagrams, requirements PDF
├── escaperoom/
│   ├── pom.xml                  # Maven build configuration
│   └── src/
│       ├── main/
│       │   ├── java/
│       │   │   ├── com/escape/  # Core application & model layer
│       │   │   │   ├── App.java
│       │   │   │   ├── model/   # Game logic (Rooms, Puzzles, Inventory, Timer, etc.)
│       │   │   │   └── util/
│       │   │   └── controllers/ # JavaFX controllers for each screen/room
│       │   └── resources/       # FXML layouts, CSS, images, audio
│       └── test/                # JUnit test cases
└── README.md
```

<p align="right">(<a href="#readme-top">back to top</a>)</p>



<!-- ROADMAP -->
## Roadmap

- [x] Core game engine with multiple room types
- [x] Account creation, login, and save/load system
- [x] Leaderboard and scoring system
- [x] Audio narration via FreeTTS
- [x] Difficulty selection (Easy / Medium / Hard)
- [x] JavaFX GUI with FXML layouts
- [ ] Additional puzzle types (image-based, drag-and-drop)
- [ ] Online/networked leaderboard
- [ ] Expanded story with more rooms and branching narrative
- [ ] Packaging as an executable JAR / installer for easy distribution

See the [open issues](https://github.com/Rudra-P9/Infinite-Stress-Loop-Escape-Room/issues) for a full list of proposed features and known issues.

<p align="right">(<a href="#readme-top">back to top</a>)</p>



<!-- CONTRIBUTING -->
## Contributing

Contributions are what make the open source community such an amazing place to learn, inspire, and create. Any contributions you make are **greatly appreciated**.

If you have a suggestion that would make this better, please fork the repo and create a pull request. You can also open an issue with the tag `enhancement`. Don't forget to give the project a star! ⭐

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

<p align="right">(<a href="#readme-top">back to top</a>)</p>



<!-- LICENSE -->
## License

Distributed under the MIT License. See `LICENSE` for more information.

<p align="right">(<a href="#readme-top">back to top</a>)</p>



<!-- CONTACT -->
## Contact

**Rudra Patel** — [GitHub @Rudra-P9](https://github.com/Rudra-P9)

Project Link: [https://github.com/Rudra-P9/Infinite-Stress-Loop-Escape-Room](https://github.com/Rudra-P9/Infinite-Stress-Loop-Escape-Room)

<p align="right">(<a href="#readme-top">back to top</a>)</p>



<!-- ACKNOWLEDGMENTS -->
## Acknowledgments

* [JavaFX Documentation](https://openjfx.io/)
* [Apache Maven](https://maven.apache.org/)
* [FreeTTS — Free Text-to-Speech](https://freetts.sourceforge.net/)
* [JSON Simple](https://code.google.com/archive/p/json-simple/)
* [JUnit 4](https://junit.org/junit4/)
* [Shields.io](https://shields.io)
* [Best-README-Template](https://github.com/othneildrew/Best-README-Template)
* [Choose an Open Source License](https://choosealicense.com)

<p align="right">(<a href="#readme-top">back to top</a>)</p>



<!-- MARKDOWN LINKS & IMAGES -->
[contributors-shield]: https://img.shields.io/github/contributors/Rudra-P9/Infinite-Stress-Loop-Escape-Room.svg?style=for-the-badge
[contributors-url]: https://github.com/Rudra-P9/Infinite-Stress-Loop-Escape-Room/graphs/contributors
[forks-shield]: https://img.shields.io/github/forks/Rudra-P9/Infinite-Stress-Loop-Escape-Room.svg?style=for-the-badge
[forks-url]: https://github.com/Rudra-P9/Infinite-Stress-Loop-Escape-Room/network/members
[stars-shield]: https://img.shields.io/github/stars/Rudra-P9/Infinite-Stress-Loop-Escape-Room.svg?style=for-the-badge
[stars-url]: https://github.com/Rudra-P9/Infinite-Stress-Loop-Escape-Room/stargazers
[issues-shield]: https://img.shields.io/github/issues/Rudra-P9/Infinite-Stress-Loop-Escape-Room.svg?style=for-the-badge
[issues-url]: https://github.com/Rudra-P9/Infinite-Stress-Loop-Escape-Room/issues
[license-shield]: https://img.shields.io/github/license/Rudra-P9/Infinite-Stress-Loop-Escape-Room.svg?style=for-the-badge
[license-url]: https://github.com/Rudra-P9/Infinite-Stress-Loop-Escape-Room/blob/main/LICENSE

[Java-badge]: https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white
[Java-url]: https://www.oracle.com/java/
[JavaFX-badge]: https://img.shields.io/badge/JavaFX-21-5382A1?style=for-the-badge&logo=java&logoColor=white
[JavaFX-url]: https://openjfx.io/
[Maven-badge]: https://img.shields.io/badge/Maven-3.8+-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white
[Maven-url]: https://maven.apache.org/
[JSON-badge]: https://img.shields.io/badge/JSON--Simple-1.1.1-grey?style=for-the-badge&logo=json&logoColor=white
[JSON-url]: https://code.google.com/archive/p/json-simple/
[FreeTTS-badge]: https://img.shields.io/badge/FreeTTS-1.0-blueviolet?style=for-the-badge
[FreeTTS-url]: https://freetts.sourceforge.net/
[JUnit-badge]: https://img.shields.io/badge/JUnit-4.13.2-25A162?style=for-the-badge&logo=junit5&logoColor=white
[JUnit-url]: https://junit.org/junit4/
