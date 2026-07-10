Demo Project for learning DataStructures in Java

## Build & test

Requires JDK 26+. Build with the bundled Gradle wrapper:

```
./gradlew test
```

This runs the JUnit 5 test suite and enforces 100% line/branch coverage
(via JaCoCo) on every class under `datastructure/`. A human-readable
coverage report is written to `build/reports/jacoco/test/html/index.html`.

## Interactive Tutorial App

We have built a dynamic, interactive React application that serves as an educational tutorial for all the data structures coded in this repository.

### Live Website
The application is automatically deployed to GitHub Pages and is live at:
[https://prosenjit-nandi.github.io/DataStructures/](https://prosenjit-nandi.github.io/DataStructures/)

*(Note: Ensure your GitHub repository settings under Settings > Pages are configured to use GitHub Actions as the build and deployment source.)*

### Running Locally
To run the React app locally:
```bash
cd tutorial-app
npm install
npm run dev
```
The site will be available at `http://localhost:5173/` by default.

### Adding New Data Structures to the Tutorial
The tutorial app dynamically extracts data directly from the Java source files. When you add a new data structure under `src/main/java/datastructure/`, simply add a Javadoc block comment at the top of the class with the following tags:

```java
/**
 * @description A brief description of what the data structure is.
 * @usage Where and when this data structure should be used.
 * @summary A quick summary of how it is implemented under the hood.
 */
```

The build script will automatically detect the new file, parse these tags, extract the source code, and add it to the interactive tutorial!
