package com.wanmeizhensuo.sqlisp.ast;

import jaskell.parsec.ParsecException;
import jaskell.parsec.common.Attempt;
import jaskell.parsec.common.Parsec;
import jaskell.parsec.common.State;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.io.EOFException;

public class Pair1<E, T> implements Parsec<E, ImmutablePair<T,T>> {
    private final Parsec<E, T> parser;

    @Override
    public ImmutablePair<T,T> parse(State<E> s) throws Throwable {
        ImmutablePair<T,T> pair1 = new ImmutablePair<T,T>(this.parser.parse(s),null);
        Parsec<E,T> p = new Attempt<>(parser);
        try {
            pair1.setValue(p.parse(s));
        } catch (Exception e) {
            return pair1;
        }
    }

    public Pair1(Parsec<E, T> parsec) { this.parser = parsec; }
    }
}
