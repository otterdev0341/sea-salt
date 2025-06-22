package com.otterdev.sea_salt.utility;

public abstract class Option<T> {
    public abstract boolean isSome();
    public abstract boolean isNone();
    public abstract T unwrap();  // panics if None

    public static <T> Option<T> some(T value) {
        return new Some<>(value);
    }

    public static <T> Option<T> none() {
        return new None<>();
    }

    private static class Some<T> extends Option<T> {
        private final T value;

        Some(T value) {
            this.value = value;
        }

        @Override
        public boolean isSome() {
            return true;
        }

        @Override
        public boolean isNone() {
            return false;
        }

        @Override
        public T unwrap() {
            return value;
        }

        public String toString() {
            return "Some(" + value + ")";
        }
    }

    private static class None<T> extends Option<T> {
        @Override
        public boolean isSome() {
            return false;
        }

        @Override
        public boolean isNone() {
            return true;
        }

        @Override
        public T unwrap() {
            throw new IllegalStateException("Called unwrap on None");
        }

        public String toString() {
            return "None";
        }
    }
}

