package com.wanmeizhensuo.streams.parser.common;


import com.wanmeizhensuo.streams.parser.Token;
import jaskell.parsec.common.Parsec;

public class Collector {
    public static NName nName(Token name) { return new NName(name); }

    public static OneToken oneToken() { return new OneToken(); }

    public static OneName oneName() { return new OneName(); }

    public static OneString oneString() { return new OneString(); }

    public static <E, T> Pair1<E, T> pair1(Parsec<E, T> p1, Parsec<E, T> p2) { return new Pair1<>(p1, p2); }

    public static <E,T> Dictionary<E,T> dictionary(Parsec<E,T> p1, Parsec<E,T> p2) {return new Dictionary<>(p1,p2); }

}
