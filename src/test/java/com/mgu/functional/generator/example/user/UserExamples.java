package com.mgu.functional.generator.example.user;

import org.junit.Test;

import static com.mgu.functional.generator.CoreGen.oneOf;
import static com.mgu.functional.generator.example.user.UserGen.userGen;

public class UserExamples {

    @Test
    public void usingUserGenToGenerateRandomUsersWithValidAttributes() {
        final User user = userGen().sample();
        System.out.println(user);
    }

    @Test
    public void selectRandomUserFromExistingRepositories() {
        final UserRepository userRepository = new UserRepository();
        final User user = oneOf(userRepository.getUsers()).sample();
        System.out.println(user);
    }
}