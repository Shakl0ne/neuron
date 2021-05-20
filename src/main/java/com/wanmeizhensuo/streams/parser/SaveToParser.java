package com.wanmeizhensuo.streams.parser;

import jaskell.parsec.common.Parsec;
import jaskell.parsec.common.State;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

import static com.wanmeizhensuo.streams.parser.Parsers.*;
import static jaskell.parsec.common.Combinator.*;
import static com.wanmeizhensuo.sqlisp.ast.Pair1.pair1;

public class SaveToParser implements Parsec<Token, ImmutablePair<String, String>> {
    final Parsec<Token, ImmutablePair<Token,Token>> parser = between(openSquare(),closeSquare(),pair1(Parsers.nameT("saveTo(PG)"),Parsers.nameT()));
    @Override
    public ImmutablePair<String, String> parse(State<Token> s) throws Throwable {
        var result = parser.parse(s);
        ImmutablePair res = new ImmutablePair(result.left.content.toString(),result.right.content.toString());
        return res;
    }
}
