# Eisenhower-Matrix-4J

**Eisenhower-Matrix-4J** is a Java library for managing tasks using the Eisenhower Matrix. This matrix helps in prioritizing tasks by urgency and importance. The library provides two implementations of the matrix, allowing you to choose between a list-based or a set-based storage approach.


## Features

- **Quadrants**: Tasks are organized into four quadrants based on urgency and importance.
- **Task Management**: Add, remove, and retrieve tasks efficiently.
- **Duplicates Handling**: Choose between allowing duplicates (List implementation) or disallowing duplicates (Set implementation).


## Beta-testing note
Please note this library is still in pre-release. Although much care has been taken in writing the code, it hasn't been tested yet and implementations may contain some little bugs.

Please wait for a stable release, or use it at your own risk.



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



## Essential API documentation

### `EisenhowerMatrix` (interface)
- **Description**: Provides an interface for objects representing Eisenhower Matrices, allowing storing custom object types. It has 2 implementations: a set-based and a list-based.
- **Most useful methods**:
  - `boolean addTask(T task, Quadrant quadrant)`: Adds a task to a quadrant.
  - `Collection<T> getTasks(Quadrant quadrant)`: Retrieves all tasks from a quadrant.
  - `List<T> getAllTasksSorted(EQuadrantsSorting quadrantsOrdering)`: It both sorts tasks in each quadrant, and combines the 4 lists into a single one according the specified `quadrantsOrdering`. Other methods variants allow sorting with a comparator, or using specific comparators for each quadrant.
  - `boolean removeTask(T task, Quadrant quadrant)`: Removes the first occurrence found for this task, from a quadrant.
  - `boolean removeTaskOccurrences(T task, Quadrant quadrant)`: Removes all copies of this task, from a quadrant.
  - `boolean removeTaskOccurrences(T task)`: Removes all copies of this task, from all 4 quadrants.
  - `EisenhowerMatrix<T> clearQuadrant(Quadrant quadrant)`: Creates a copy of this matrix, and deletes all tasks from a quadrant. It is API-fluent and allows to revert modifications.
  - `EisenhowerMatrix<T> clearAllQuadrants()`: Creates a copy of this matrix, and deletes all tasks from the entire matrix. It is API-fluent and allows to revert modifications.

### `EisenhowerMatrixSet`

- **Description**: Provides a set-based implementation of the matrix, where each quadrant contains a `Set` of tasks. It allows duplicated element in the matrix and quadrant itself, and store tasks in the order in which tasks are entered.
- **Specific methods**:
  - `boolean isTaskUrgent(T task)`: Checks if the specified task is urgent in the matrix.
  - `boolean isTaskImportant(T task)`: Checks if the specified task is important in the matrix.
 
### `EisenhowerMatrixList`
- **Description**: Provides a list-based implementation of the matrix, where each quadrant contains a `List` of tasks. It doesn't allow duplicated element in the matrix. The most natural one, for most of the cases.
- **Specific Methods**:
  - `getTask(Quadrant quadrant, int index)`: Retrieves the task at the specified index from the specified quadrant.
  - `setTask(T task, Quadrant quadrant, int index)`: Replaces the task at the specified index in the specified quadrant.
  - `sublist(Quadrant quadrant, int fromIndex, int toIndex)`: Returns a sublist of tasks from the specified quadrant, between the specified indices.

### `Quadrant` (enum)
- **Description**: Enum representing the four quadrants of the Eisenhower Matrix.
- **Useful static methods:**:
  - `static Quadrant getQuadrant(boolean urgent, boolean important)`: Returns the appropriate quadrant based on the urgency and importance of the task.
  - `static Quadrant[] quadrantsSorted(EQuadrantsSorting ordering)`: Returns all 4 `Quadrant` of an Eisenhower Matrix, sorted according different rules: `(Q1, Q2, Q3, Q4)` or `(Q1, Q3, Q2, Q4)`.

### `Task`
- **Description**: Provides a tasks system, to use within the `EisenhowerMatrix`. Most important features are customizable properties and optional subtasks. However, you can represent your tasks using whatever class you like (for instance, a `String`, or create your custom one).
- **Most useful methods:**:
  - `Set<String> getRequiredProperties()`: Returns the set of properties that are required for this task. Concrete subclasses can define their own required properties by overriding this method.
  - `Object putProperty(String key, Object value)`: Adds or updates a property of the task, which is defined by a key and a value.
  - `Object getProperty(String key)`: Retrieves the property using a key.
  - `Object removeProperty(String key)`: Removes a property from this task.
  - `boolean addSubtask(Task subtask)`: Adds a subtask to this task.
  - `boolean removeSubtask(Task subtask)`: Removes a subtask from this task.
  - `Collection<Task> getSubtasks()`: Returns the subtasks of this task. If the task is atomic, the list will be empty and unmodifiable.
  - `Task turnIntoAtomic()`: Returns a copy of this task, but atomic, with the same properties but not allowing subtasks.

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
