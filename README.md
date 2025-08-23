# Worldsmarks & Avatars List Editor

![Java](https://img.shields.io/badge/language-Java-orange.svg)
![License: GPL v3](https://img.shields.io/badge/License-GPLv3-blue.svg)

A desktop tool developed in Java Swing for editing `.worldsmarks` and `.avatars` bookmark files. This application provides a user-friendly graphical interface to manage, add, delete, and reorder entries in these specific binary files, commonly used in virtual world platforms like Worlds.com.
![Screenshot of the application](https://github.com/user-attachments/assets/7f19d18c-ef76-4d3f-ad26-97f5b995384e)

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

