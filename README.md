# ap

Advanced Programming course repository.

## Final Project — University Library Management System

Console-based library management system with guest / student / librarian / manager roles
(book search, borrow requests, approvals, returns, overdue tracking, account management).

Data is persisted in a lightweight **SQLite** database (`library.db`).

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
