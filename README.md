# Worldsmarks & Avatars List Editor

![Java](https://img.shields.io/badge/language-Java-orange.svg)
![License: GPL v3](https://img.shields.io/badge/License-GPLv3-blue.svg)

A desktop tool developed in Java Swing for editing `.worldsmarks` and `.avatars` bookmark files. This application provides a user-friendly graphical interface to manage, add, delete, and reorder entries in these specific binary files, commonly used in virtual world platforms like Worlds.com.

![Screenshot of the application](https://github.com/user-attachments/assets/7f19d18c-ef76-4d3f-ad26-97f5b995384e)

The goal of this editor is to provide a modern and functional alternative to older tools, allowing for more convenient management of your world and avatar lists.

✨ Features
Intuitive GUI: Edit your lists in an easy-to-use table format.

Create New Lists: Generate fresh .worldsmarks or .avatars files from scratch.

Full Editing Capabilities: Add, delete, and reorder entries in your lists with a single click.

Integrated Search: Quickly find any entry by its name or value.

Tab Support: Work with multiple files at the same time.

🚀 Getting Started
To use the editor, you have two options:

Option 1: Use the Pre-compiled Version (Recommended)
The easiest way to get started is to download the executable .jar file from the Releases section of this repository.

Go to the Releases section.

Download the WorldsEditor.jar file.

Make sure you have Java 8 or a newer version installed on your system.

Double-click the WorldsEditor.jar file or run it from a terminal with the command:

Bash

java -jar WorldsEditor.jar
Option 2: Build from Source
If you prefer to compile the program yourself, follow the instructions below.

⚙️ Building from Source
The project is set up to be compiled with a JDK 8 or higher, generating a .jar file that is compatible with a Java 8 JRE. This ensures that the program runs on most computers without compatibility issues.

Prerequisites
Java Development Kit (JDK), version 8 or higher.

The JSON-Java library, whose .jar file is included in the repository (json-20250517.jar).

Compilation Process
Follow these steps from a terminal, located in the project's root folder.

1. Compile the Source Code to .class files
This command compiles all .java files into Java 8 bytecode, ensuring maximum compatibility.

Bash

# On Windows
javac --release 8 -cp ".;json-20250517.jar" *.java

# On Linux / macOS
javac --release 8 -cp ".:json-20250517.jar" *.java
2. Unpack the JSON Library
To create a single, self-contained executable file ("fat JAR"), we first need to extract the classes from the JSON library.

Bash

jar xf json-20250517.jar
This command will create a new folder named org in the current directory.

3. Package the Executable .jar
Finally, this command packages your compiled classes, resources (assets), and the JSON library's classes into a single executable file.

Bash

jar cvfm WorldsEditor.jar MANIFEST.MF *.class assets org
And that's it! The resulting WorldsEditor.jar file is your application, ready to be run and distributed.

Contributing
Contributions are welcome. If you have ideas for improving the tool or find a bug, please open an issue in the repository.






























---
## ## Features

* **File Management:** Create new lists from scratch, or open and save existing `.worldsmarks` and `.avatars` files.
* **Intuitive Editing:** Add, delete, and edit entries directly in a simple table format.
* **Reordering:** Easily move entries up or down in the list to change their order.
* **Search Functionality:** Quickly find specific entries in large lists.
* **Modern Interface:** A clean, dark-themed UI for comfortable use.

---
## ## Getting Started

To compile and run this project from source, you will need a Java Development Kit (JDK) installed on your system (version 11 or higher recommended).

### ### Prerequisites

* **JDK (Java Development Kit)**: You can download it from [Oracle](https://www.oracle.com/java/technologies/downloads/) or use an open-source alternative like [Adoptium Temurin](https://adoptium.net/).
* **JSON Library:** The project depends on the `json-java` library. The required `.jar` (`json-20250517.jar`) is included in this repository for convenience.

### ### Compilation Process

There are two ways to compile this project: from the source `.java` files or using the pre-compiled `.class` files located in the `/build` directory.

#### #### Method 1: Compiling from Source (Recommended)

This method builds the entire project from scratch.

1.  **Clone the repository:**
    ```bash
    git clone [https://github.com/thedevil4k/Worldsmarks-Avatars-List-Editor.git](https://github.com/thedevil4k/Worldsmarks-Avatars-List-Editor.git)
    cd Worldsmarks-Avatars-List-Editor
    ```

2.  **Compile the `.java` files:**
    From the project's root directory, run the `javac` command. This will compile all source files and place the resulting `.class` files into the `build` folder, using the JSON library in the classpath.

    * On Windows:
        ```bash
        javac -d build -cp ".;json-20250517.jar" src/*.java
        ```
    * On Linux/macOS:
        ```bash
        javac -d build -cp ".:json-20250517.jar" src/*.java
        ```

3.  **Package the application into a JAR:**
    This command takes the compiled classes from the `build` folder, includes the `assets`, and uses the `MANIFEST.MF` file to create a final executable JAR.

    ```bash
    jar cvfm WorldsEditor.jar MANIFEST.MF -C build . assets
    ```
    *(Note: The `MANIFEST.MF` file already references the external JSON library in its `Class-Path`)*.

#### #### Method 2: Using Pre-compiled Files

The `/build` directory in this repository contains the pre-compiled `.class` files. If you trust these files, you can skip the `javac` compilation step and create the JAR directly.

1.  **Clone the repository.**

2.  **Package the application directly:**
    Run this command from the project's root directory:
    ```bash
    jar cvfm WorldsEditor.jar MANIFEST.MF -C build . assets
    ```

---
## ## Usage

After completing the compilation, you will have a `WorldsEditor.jar` file. To run the application, ensure that the `json-20250517.jar` file is in the same directory and execute the following command:

```bash
java -jar WorldsEditor.jar
```

