# Introduction
Grep is Java-based console application that replicates the functionality of the grep bash command using core Java features like StreamAPI, Regex and I/O and Maven as a build automation tool. Additionally, the application has been dockerized to increase accessibility. The application was developed using the TDD approach and functionality covered with unit tests written in Junit 5.

# How to use
Grep has 3 parameters: pattern, input file path and output file path.

# Implementation
The implementation process began by developing the Java core functionality and creating tests. Once the functionality was completed and thoroughly tested, a Maven plugin was used to create a JAR file that included code along with all necessary dependencies, which was then dockerized.

# Improvement List
- Add different input options.
- Add different output options.
- Add multithreading support to speed up execution.
