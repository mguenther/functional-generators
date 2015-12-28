package com.mgu.generator.example.user;

import java.util.ArrayList;
import java.util.List;

public class UserRepository {

    public List<User> getUsers() {
        final List<User> users = new ArrayList<>();
        users.add(new User("Sheldon Cooper", "sheldon.cooper@caltech.edu", "123isnotasafepassword"));
        users.add(new User("Leonard Hofstadter", "leonard.hofstadter@caltech.edu", "iamnotadwarfiamahobbit"));
        users.add(new User("Howard Wolowitz", "howard.wolowitz@caltech.edu", "allnightlong"));
        users.add(new User("Rajesh Koothrappali", "rajesh.koothrappali@caltech.edu", "awkwardsilence"));
        return users;
    }
}
