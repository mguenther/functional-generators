package com.mgu.generator.example.user;

import org.junit.Test;

import static com.mgu.generator.CoreGen.oneOf;
import static com.mgu.generator.example.user.UserGen.flatMapUserGen;
import static com.mgu.generator.example.user.UserGen.userGen;

public class UserExamples {

    @Test
    public void usingUserGenToGenerateRandomUsersWithValidAttributes() {
        final User user = userGen().sample();
        System.out.println(user);
    }

    @Test
    public void usingFlatMapUserGenToGenerateRandomUsersWithValidAttributes() {
        final User user = flatMapUserGen().sample();
        System.out.println(user);
    }

    @Test
    public void selectRandomUserFromExistingRepositories() {
        final UserRepository userRepository = new UserRepository();
        final User user = oneOf(userRepository.getUsers()).sample();
        System.out.println(user);
    }
}