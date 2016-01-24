package com.mgu.generator;

import com.mgu.functional.Tuple2;
import com.mgu.functional.Tuple3;
import com.mgu.functional.Tuple4;

public class TupleGen {

    public static <T, U> Gen<Tuple2<T, U>> tuple2Gen(Gen<T> genT, Gen<U> genU) {
        return
                genT.flatMap(t ->
                genU.flatMap(u -> () -> new Tuple2<>(t, u)));
    }

    public static <T, U, V> Gen<Tuple3<T, U, V>> tuple3Gen(Gen<T> genT, Gen<U> genU, Gen<V> genV) {
        return
                genT.flatMap(t ->
                genU.flatMap(u ->
                genV.flatMap(v -> () -> new Tuple3<>(t, u, v))));
    }

    public static <T, U, V, W> Gen<Tuple4<T, U, V, W>> tuple4Gen(Gen<T> genT, Gen<U> genU, Gen<V> genV, Gen<W> genW) {
        return
                genT.flatMap(t ->
                genU.flatMap(u ->
                genV.flatMap(v ->
                genW.flatMap(w -> () -> new Tuple4<>(t, u, v, w)))));
    }
}