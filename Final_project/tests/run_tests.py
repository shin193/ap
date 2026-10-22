"""Run real SQLite regression checks without changing the user's library.db."""
import os
from contextlib import closing
from pathlib import Path
import sqlite3
import subprocess
import tempfile
import unittest

ROOT = Path(__file__).resolve().parents[2]


class LibraryTests(unittest.TestCase):
    @classmethod
    def setUpClass(cls):
        cls.build = tempfile.TemporaryDirectory(prefix="library-build-")
        sources = list((ROOT / "Final_project").glob("*.java"))
        sources += list((ROOT / "Final_project/tests").glob("*.java"))
        subprocess.run(["javac", "-encoding", "UTF-8", "-d", cls.build.name,
                        *map(str, sources)], check=True)
        cls.classpath = os.pathsep.join([cls.build.name, str(ROOT / "Final_project/lib/*")])

    @classmethod
    def tearDownClass(cls):
        cls.build.cleanup()

    def setUp(self):
        self.directory = tempfile.TemporaryDirectory(prefix="library-test-")
        self.addCleanup(self.directory.cleanup)

    def java(self, main, *args, input_text="", success=True):
        result = subprocess.run(
            ["java", "-Dfile.encoding=UTF-8", "-Dstdout.encoding=UTF-8",
             "-Dstderr.encoding=UTF-8", "-cp", self.classpath, main, *args],
            cwd=self.directory.name, input=input_text, capture_output=True,
            encoding="utf-8", errors="replace", timeout=120)
        if success:
            self.assertEqual(result.returncode, 0, result.stdout + result.stderr)
        else:
            self.assertNotEqual(result.returncode, 0, result.stdout + result.stderr)
        return result

    def database(self, scenario):
        return self.java("Final_project.tests.DatabaseRegressionTest", scenario)

    def test_persistence_across_processes(self):
        self.database("write")
        self.database("read")

    def test_atomic_manager_update(self):
        self.database("manager-atomic")

    def test_batch_rollback_and_error_reporting(self):
        self.database("rollback")

    def test_load_error_is_not_empty_data(self):
        self.database("load-failure")

    def test_corrupt_date_is_reported(self):
        self.database("bad-date")

    def test_legacy_migration(self):
        self.database("migration")
        # A new JVM must read committed data without importing again.
        self.assertNotIn("Migrated", self.database("read-migration").stdout)

    def test_rename_failure_preserves_both_files(self):
        self.database("rename-collision")
        self.assertNotIn("Migrated", self.database("read-migration").stdout)

    def test_invalid_migration_preserves_original(self):
        self.database("invalid-migration")

    def test_loan_workflow(self):
        self.java("Final_project.tests.LibraryRegressionTest")

    def test_unwritable_database_stops_startup(self):
        Path(self.directory.name, "library.db").mkdir()
        result = self.java("Final_project.LibrarySystem", input_text="6\n", success=False)
        self.assertIn("Storage failure", result.stderr)
        self.assertNotIn("University Library Management System", result.stdout)

    def test_console_workflow_shared_input(self):
        commands = ["2", "Student", "s1", "user", "pw", "3", "user", "pw",
                    "4", "1", "7", "8", "5", "abc", "123456", "7", "1",
                    "11", "10", "1", "13", "3", "user", "pw", "6", "0001",
                    "7", "8", "6"]
        result = self.java("Final_project.LibrarySystem", input_text="\n".join(commands) + "\n")
        for message in ["registration completed", "Book requested successfully",
                        "Borrow request approved", "Book received successfully",
                        "Book returned successfully", "Exiting system"]:
            self.assertIn(message, result.stdout)
        with closing(sqlite3.connect(Path(self.directory.name, "library.db"))) as conn:
            self.assertEqual(conn.execute(
                "SELECT is_available, borrowed_by_student_id, received_date FROM books WHERE id='0001'"
            ).fetchone(), (1, None, None))


if __name__ == "__main__":
    unittest.main(verbosity=2)
