### Documentation for Contributing to the SCHOOL-SYSTEM-JDBC-MYSQL Project

#### Project Overview

The **SCHOOL-SYSTEM-JDBC-MYSQL** project aims to build a multi-school management system where each school operates as an independent subsystem with its own database. However, all schools share a uniform database schema, enabling data sharing and seamless connectivity between them. The system is designed with modularity in mind, employing Java and MySQL for database operations and core functionality.

---

### How to Contribute

#### 1. Clone the Repository
To begin, clone the project repository:
```bash
git clone https://github.com/Prince07746/SCHOOL-SYSTEM-JDBC-MYSQL.git
cd SCHOOL-SYSTEM-JDBC-MYSQL
```

#### 2. Project Structure
Here’s an overview of the key files and directories:
- **`src/DBOperations/`**: Contains classes for database management.
  - `DBManager.java`: Core class for managing database operations.
  - `DbOperationInterface.java`: Interface defining database operations.
- **`src/java.SchoolCore/`**: Contains the core functionality of the school system.
  - `Courses/`: Handles course management.
  - `SchoolSystem/`: Defines and manages school entities.
  - `UserSystem/`: Manages user entities, including `Student` and `Teacher`.
- **`src/java.SchoolCore/Main.java`**: Entry point for running and testing the system.

#### 3. Contribution Guidelines
- Follow consistent coding styles as per the existing codebase.
- Ensure all classes adhere to modular and object-oriented principles.
- Use meaningful commit messages to describe your changes clearly.

---

### Quick Start for Testing Contributions

The primary entry point for contributions is the **`connectDBTechnician` method** in `DBManager.java`. This method ensures the integrity of the database, creating it if necessary, and setting up essential tables. Here’s how to use it:

1. **Set Up Database Credentials:**
   Edit the `Main.java` file to configure your MySQL credentials:
   ```java
   DBManager db = new DBManager("your_username", "your_password", "", "your_database_name");
   ```

2. **Invoke `connectDBTechnician`:**
   Uncomment the following in `Main.java`:
   ```java
   db.connectDBTechnician();
   ```

3. **Run the Application:**
   Execute the application from the command line or an IDE to test the database setup:
   ```bash
   java -cp .:mysql-connector-java.jar SchoolCore.Main
   ```

---

### Key Focus Areas for Contributions
1. **Database Management Enhancements:**
   - Improve error handling in `DBManager.java`.
   - Extend functionality for managing multiple schools.
2. **Core System Functionality:**
   - Expand `School`, `Courses`, and `User` modules.
   - Integrate features for user and course assignment.
3. **Testing and Documentation:**
   - Add unit tests for critical methods, especially in `DBManager`.
   - Improve in-code comments and overall documentation.

---








The structure of the database and the relationships between its tables in the **SCHOOL-SYSTEM-JDBC-MYSQL** project can be deduced from the `DBManager.java` file. Here is a detailed breakdown of the database schema:

---

### Tables and Their Structure

#### 1. **Teacher Table**
Represents teachers in the school system.
```sql
CREATE TABLE Teacher (
    id VARCHAR(50) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    lastName VARCHAR(100) NOT NULL,
    gender VARCHAR(10) NOT NULL,
    age INT NOT NULL,
    salary DOUBLE NOT NULL
);
```

#### 2. **Student Table**
Represents students in the school system.
```sql
CREATE TABLE Student (
    id VARCHAR(50) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    lastName VARCHAR(100) NOT NULL,
    gender VARCHAR(10) NOT NULL,
    age INT NOT NULL
);
```

#### 3. **Course Table**
Stores information about courses offered in the system.
```sql
CREATE TABLE Course (
    id VARCHAR(50) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    maxPoints DOUBLE NOT NULL
);
```

#### 4. **EnrollmentST Table (Student Enrollment)**
Links students to courses and teachers, along with their performance (marks).
```sql
CREATE TABLE EnrollmentST (
    id VARCHAR(50) PRIMARY KEY,
    studentId VARCHAR(50),
    teacherId VARCHAR(50),
    courseId VARCHAR(50),
    marks DOUBLE,
    FOREIGN KEY (studentId) REFERENCES Student(id),
    FOREIGN KEY (teacherId) REFERENCES Teacher(id),
    FOREIGN KEY (courseId) REFERENCES Course(id)
);
```

#### 5. **EnrollmentTC Table (Teacher Course Assignment)**
Links teachers to courses they are responsible for teaching.
```sql
CREATE TABLE EnrollmentTC (
    id VARCHAR(50) PRIMARY KEY,
    courseId VARCHAR(50),
    teacherId VARCHAR(50),
    FOREIGN KEY (courseId) REFERENCES Course(id),
    FOREIGN KEY (teacherId) REFERENCES Teacher(id)
);
```

---

### Relationships Between Tables

1. **`Student` ↔ `EnrollmentST`:**
   - A **Student** can be enrolled in multiple courses via the **EnrollmentST** table.
   - The relationship is **one-to-many** from `Student` to `EnrollmentST`.

2. **`Teacher` ↔ `EnrollmentST`:**
   - A **Teacher** can be assigned to multiple students for specific courses via the **EnrollmentST** table.
   - The relationship is **one-to-many** from `Teacher` to `EnrollmentST`.

3. **`Course` ↔ `EnrollmentST`:**
   - A **Course** can have multiple students enrolled, with grades stored in the **EnrollmentST** table.
   - The relationship is **one-to-many** from `Course` to `EnrollmentST`.

4. **`Teacher` ↔ `EnrollmentTC`:**
   - A **Teacher** can teach multiple courses, recorded in the **EnrollmentTC** table.
   - The relationship is **one-to-many** from `Teacher` to `EnrollmentTC`.

5. **`Course` ↔ `EnrollmentTC`:**
   - A **Course** can have multiple teachers assigned (e.g., for collaborative teaching), recorded in **EnrollmentTC**.
   - The relationship is **one-to-many** from `Course` to `EnrollmentTC`.
![image](https://github.com/user-attachments/assets/8b785ed3-f63f-4563-846e-61fe54339c34)

---

### Entity-Relationship (ER) Diagram

#### Summary of Relationships:
- **Primary Keys:**
  - `id` fields are unique identifiers for each table.
- **Foreign Keys:**
  - `studentId` in `EnrollmentST` → `id` in `Student`.
  - `teacherId` in both `EnrollmentST` and `EnrollmentTC` → `id` in `Teacher`.
  - `courseId` in both `EnrollmentST` and `EnrollmentTC` → `id` in `Course`.

This schema ensures data integrity and enforces relationships between entities like students, teachers, and courses. Each table is modular, allowing flexibility in system design while supporting the overall school management functionality.
