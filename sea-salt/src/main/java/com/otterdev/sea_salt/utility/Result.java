package com.otterdev.sea_salt.utility;

public abstract class Result<T, E> {
    public abstract boolean isOk();
    public abstract boolean isErr();
    public abstract T unwrap() throws IllegalStateException;
    public abstract E unwrapErr() throws IllegalStateException;

    public static <T, E> Result<T, E> ok(T value) {
        return new Ok<>(value);
    }

    public static <T, E> Result<T, E> err(E error) {
        return new Err<>(error);
    }

    private static class Ok<T, E> extends Result<T, E> {
        private final T value;

        Ok(T value) {
            this.value = value;
        }

        @Override
        public boolean isOk() {
            return true;
        }

        @Override
        public boolean isErr() {
            return false;
        }

        @Override
        public T unwrap() {
            return value;
        }

        @Override
        public E unwrapErr() {
            throw new IllegalStateException("Called unwrapErr() on Ok");
        }

        @Override
        public String toString() {
            return "Ok(" + value + ")";
        }
    }

    private static class Err<T, E> extends Result<T, E> {
        private final E error;

        Err(E error) {
            this.error = error;
        }

        @Override
        public boolean isOk() {
            return false;
        }

        @Override
        public boolean isErr() {
            return true;
        }

        @Override
        public T unwrap() {
            throw new IllegalStateException("Called unwrap() on Err");
        }

        @Override
        public E unwrapErr() {
            return error;
        }

        @Override
        public String toString() {
            return "Err(" + error + ")";
        }
    }
}
