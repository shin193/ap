package Final_project.web;

import static Final_project.web.ApiModels.*;

record Account(Role role, String username, String password, String name, String studentId, boolean active) {
    Profile profile() { return new Profile(role, username, name, studentId, active); }
    @Override public String toString() { return role + ":" + username; }
}
