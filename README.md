# Eisenhower-Matrix-4J

**Eisenhower-Matrix-4J** is a Java library for managing tasks using the Eisenhower Matrix. This matrix helps in prioritizing tasks by urgency and importance. The library provides two implementations of the matrix, allowing you to choose between a list-based or a set-based storage approach.


## Features

- **Quadrants**: Tasks are organized into four quadrants based on urgency and importance.
- **Task Management**: Add, remove, and retrieve tasks efficiently.
- **Duplicates Handling**: Choose between allowing duplicates (List implementation) or disallowing duplicates (Set implementation).


## Getting Started

### Prerequisites

Ensure you have Java 19 or later installed.

### Installation

You can include this library in your project by adding the JAR file to your classpath or by using a build tool like Maven or Gradle.

### Usage

#### Create a Matrix

You can choose between two implementations based on your needs:

- **List-Based Matrix**: Allows duplicate tasks.
- **Set-Based Matrix**: Disallows duplicate tasks.

```java
import com.eisenhower.matrix.EisenhowerMatrixList;
import com.eisenhower.matrix.EisenhowerMatrixSet;
import com.eisenhower.util.Quadrant;

public class Main {
    public static void main(String[] args) {
        // Create a list-based matrix
        EisenhowerMatrixList<String> listMatrix = new EisenhowerMatrixList<>();
        
        // Create a set-based matrix
        EisenhowerMatrixSet<String> setMatrix = new EisenhowerMatrixSet<>();
    }
}
```

#### Add Tasks

```java
listMatrix.putTask("Complete project report", Quadrant.DO_IT_NOW);
setMatrix.putTask("Prepare presentation", Quadrant.SCHEDULE_IT);
```

#### Remove Tasks

```java
listMatrix.removeTask("Complete project report", Quadrant.DO_IT_NOW);
setMatrix.removeTask("Prepare presentation", Quadrant.SCHEDULE_IT);
```

#### Retrieve Tasks

```java
String task = listMatrix.getTask(Quadrant.DO_IT_NOW, 0);
boolean isUrgent = setMatrix.isUrgent("Prepare presentation");
boolean isImportant = setMatrix.isImportant("Prepare presentation");
```


## Summary of API Documentation

### `EisenhowerMatrixList`

- **Description**: Provides a list-based implementation of the matrix where each quadrant contains a `List` of tasks.
- **Methods**:
  - `putTask(T task, Quadrant quadrant)`: Adds a task to the specified quadrant. If the task or quadrant is `null`, a `NullPointerException` is thrown.
  - `removeTask(T task, Quadrant quadrant)`: Removes all occurrences of the specified task from the specified quadrant. If the task or quadrant is `null`, a `NullPointerException` is thrown.
  - `getTask(Quadrant quadrant, int index)`: Retrieves the task at the specified index from the specified quadrant. If the quadrant is `null` or the index is out of bounds, a `NullPointerException` or `IndexOutOfBoundsException` is thrown.
  - `sublist(Quadrant quadrant, int fromIndex, int toIndex)`: Returns a sublist of tasks from the specified quadrant, between the specified indices. If the quadrant is `null` or indices are out of bounds, a `NullPointerException` or `IndexOutOfBoundsException` is thrown.
  - `setTask(T task, Quadrant quadrant, int index)`: Replaces the task at the specified index in the specified quadrant. If the task or quadrant is `null`, a `NullPointerException` is thrown.

### `EisenhowerMatrixSet`

- **Description**: Provides a set-based implementation of the matrix where each quadrant contains a `Set` of tasks.
- **Methods**:
  - `putTask(T task, Quadrant quadrant)`: Adds a task to the specified quadrant. Duplicate tasks are not allowed. If the task or quadrant is `null`, a `NullPointerException` is thrown.
  - `removeTask(T task, Quadrant quadrant)`: Removes the specified task from the specified quadrant. If the task or quadrant is `null`, a `NullPointerException` is thrown.
  - `isUrgent(T task)`: Checks if the specified task is urgent. Throws a `NullPointerException` if the task is `null`, and a `NoSuchElementException` if the task is not found in any quadrant.
  - `isImportant(T task)`: Checks if the specified task is important. Throws a `NullPointerException` if the task is `null`, and a `NoSuchElementException` if the task is not found in any quadrant.

### `Quadrant`

- **Description**: Enum representing the four quadrants of the Eisenhower Matrix.
- **Methods**:
  - `Quadrant.getQuadrant(boolean urgent, boolean important)`: Returns the appropriate quadrant based on the urgency and importance of the task.


## Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository.
2. Create a new branch (`git checkout -b feature/your-feature`).
3. Make your changes.
4. Commit your changes (`git commit -am 'Add some feature'`).
5. Push to the branch (`git push origin feature/your-feature`).
6. Create a new Pull Request.


## License

Copyright © 2024, Nicolas Scalese (alias IlNick03 on Github)

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.


## Acknowledgments

- Inspired by the Eisenhower Matrix, a tool for prioritizing tasks.
- Thanks to the Java community for the robust libraries and tools.
