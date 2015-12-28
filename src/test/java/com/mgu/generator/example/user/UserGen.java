package com.mgu.generator.example.user;

import com.mgu.generator.Gen;

import static com.mgu.generator.CoreGen.oneOf;
import static com.mgu.generator.CoreGen.constant;
import static com.mgu.generator.StringGen.alphaNumStringGen;

public class UserGen {

    private static Gen<String> topLevelDomainNameGen() {
        return oneOf("com", "de", "at", "ch", "ca", "uk", "gov", "edu");
    }

    private static Gen<String> domainNameGen() {
        return oneOf("habitat47", "google", "spiegel");
    }

    private static Gen<String> validEmailGen(final String firstName, final String lastName) {
        return () -> new StringBuilder()
                .append(firstName)
                .append(oneOf("-", ".", "_").sample())
                .append(lastName)
                .append(constant("@").sample())
                .append(domainNameGen().sample())
                .append(constant(".").sample())
                .append(topLevelDomainNameGen().sample())
                .toString();
    }

    public static Gen<User> userGen() {
        return () -> alphaNumStringGen(8)
                .map(firstName -> alphaNumStringGen(8)
                    .map(lastName -> validEmailGen(firstName, lastName)
                        .map(email -> new User(
                                firstName + " " + lastName,
                                email,
                                alphaNumStringGen(14).sample()))
                            .sample())
                        .sample())
                .sample();
    }

    public static Gen<User> flatMapUserGen() {
        return alphaNumStringGen(8)
                .flatMap(firstName -> alphaNumStringGen(8)
                .flatMap(lastName -> validEmailGen(firstName, lastName)
                .flatMap(email -> () -> new User(firstName + " " + lastName, email, alphaNumStringGen(14).sample()))));
    }
}