package com.mgu.functional.property.example.fizzbuzz;

import java.util.stream.IntStream;

public class Fizzbuzz {

    public String fizzbuzz(int upToInclusive) {
        return IntStream
                .range(0, upToInclusive+1)
                .boxed()
                .map(n -> map(n))
                .reduce("", (a, b) -> a + " " + b);
    }

    public String map(int n) {
        if (n % 15 == 0) {
            return "FizzBuzz";
        } else if (n % 3 == 0) {
            return "Fizz";
        } else if (n % 5 == 0) {
            return "Buzz";
        } else {
            return String.valueOf(n);
        }
    }
}