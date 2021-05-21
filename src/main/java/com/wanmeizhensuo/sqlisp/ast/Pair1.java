package com.wanmeizhensuo.sqlisp.ast;

import jaskell.parsec.common.Parsec;
import jaskell.parsec.common.State;
import org.apache.commons.lang3.tuple.ImmutablePair;


public class Pair1<E, T> implements Parsec<E, ImmutablePair<T,T>> {
    private final Parsec<E, T> p1;
    private final Parsec<E, T> p2;
    @Override
    public ImmutablePair<T,T> parse(State<E> s) throws Throwable {
        ImmutablePair<T,T> pair1 = new ImmutablePair<T,T>(this.p1.parse(s),this.p2.parse(s));
        return pair1;
    }

    public Pair1(Parsec<E, T> p1, Parsec<E, T> p2) {
        this.p1 = p1;
        this.p2 = p2;
    }


    public static <E, T> Pair1<E, T> pair1(Parsec<E, T> p1, Parsec<E, T> p2) {return new Pair1<>(p1, p2); }
}
