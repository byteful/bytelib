package me.byteful.bytelib.core.util;

import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

@FunctionalInterface
public interface TriFunction<A, B, C, R> {
    R apply(A a, B b, C c);

    default <V> TriFunction<A, B, C, V> andThen(@NotNull Function<? super R, ? extends V> after) {
        return (A a, B b, C c) -> after.apply(apply(a, b, c));
    }
}
