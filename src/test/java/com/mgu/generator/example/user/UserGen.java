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

    private static Gen<String> validEmailGen(final Gen<String> firstNameGen, final Gen<String> lastNameGen) {
        return firstNameGen
                .flatMap(firstName -> oneOf("-", ".", "_")
                .flatMap(nameDelimiter -> lastNameGen
                .flatMap(lastName -> constant("@")
                .flatMap(at -> domainNameGen()
                .flatMap(domainName -> constant(".")
                .flatMap(domainDelimiter -> topLevelDomainNameGen()
                .map(topLevelDomain -> firstName + nameDelimiter + lastName + at + domainName + domainDelimiter + topLevelDomain)))))));
    }

    public static Gen<User> userGen() {
        return alphaNumStringGen(8)
                .flatMap(firstName -> alphaNumStringGen(8)
                .flatMap(lastName -> validEmailGen(constant(firstName), constant(lastName))
                .flatMap(email -> alphaNumStringGen(14)
                .map(hashedPassword -> new User(firstName + " " + lastName, email, hashedPassword)))));
    }
}