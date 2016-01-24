package com.mgu.functional.property;

import java.util.Optional;

public class Result<T> {

    private final int numberOfPassedTests;

    private final boolean passed;

    private final T failedOn;

    private Result(final int numberOfPassedTests, final boolean passed, final T failedOn) {
        this.numberOfPassedTests = numberOfPassedTests;
        this.failedOn = failedOn;
        this.passed = passed;
    }

    public void assertPassed() {
        if (!this.passed) {
            throw new AssertionError("! Falsified after " + this.numberOfPassedTests + " tests on sample " + failedOn);
        }
    }

    public int numberOfPassedTests() {
        return this.numberOfPassedTests;
    }

    public boolean passed() {
        return this.passed;
    }

    public Optional<T> failedOn() {
        return Optional.ofNullable(this.failedOn);
    }

    public static <T> Result<T> passed(final int numbersOfPassedTests, final T failedOn) {
        return new Result<>(numbersOfPassedTests, true, null);
    }

    public static <T> Result<T> falsified(final int numberOfPassedTests, final T failedOn) {
        return new Result<>(numberOfPassedTests, false, failedOn);
    }
}