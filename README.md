# ap

Advanced Programming course repository.

## Final Project — University Library Management System

Console-based library management system with guest / student / librarian / manager roles
(book search, borrow requests, approvals, returns, overdue tracking, account management).

Data is persisted in a lightweight **SQLite** database (`library.db`).

### Spring Boot web service

A REST API is available alongside the console application. It supports role-based
authentication, book CRUD/search, student registration and the complete loan workflow
against the same SQLite schema. See [the Persian API guide](Final_project/webservice/README.md)
and [sample HTTP requests](Final_project/webservice/requests.http).

```bash
# from the repository root (JDK 17+ and Maven)
mvn -f Final_project/pom.xml clean verify
java -jar Final_project/webservice/target/library-webservice-1.0.0.jar
```

Health check: `http://127.0.0.1:8080/api/health`. Run the console and API separately;
the console keeps a cached snapshot of the library data.

### Build & Run

The SQLite JDBC driver is bundled in `Final_project/lib/`.

```bash
# from the repository root
javac -d build Final_project/*.java
java -cp "build;Final_project/lib/sqlite-jdbc-3.46.1.3.jar" Final_project.LibrarySystem
```

On Linux/macOS use `:` instead of `;` in the classpath.

### Default accounts

| Role      | Username | Password |
|-----------|----------|----------|
| Manager   | ADMIN    | 1111     |
| Librarian | abc      | 123456   |

### Notes

- On first start the database file `library.db` is created automatically next to the working directory.
- Old `.dat` files from the previous serialization-based storage are imported automatically
  on first start and renamed to `*.dat.migrated`.
- Import failures preserve the original files and stop startup. A failed backup rename
  preserves both files; existing backups are never overwritten.
- Database read/write failures stop the session with an error instead of continuing with
  empty or unsaved data. Resolve the reported problem before restarting.
- Student IDs must be unique and cannot change while a request or loan references them.

### Regression tests

With a JDK and Python 3 installed, run from the repository root:

```bash
python Final_project/tests/run_tests.py
```

The tests compile into a temporary directory and use isolated SQLite files. They cover
restart persistence, transaction rollback, failed manager updates, legacy migration and
backup preservation, startup/read failures, and the console loan workflow.
