package com.wanmeizhensuo.streams.parser;

import jaskell.parsec.common.Parsec;
import jaskell.parsec.common.State;

import static com.wanmeizhensuo.streams.parser.Parsers.*;
import static jaskell.parsec.common.Combinator.*;

public class FlowParser implements Parsec<Token, String> {
    final Parsec<Token, Token> parser = openSquareParser().then(nameT("flow").
            then(nameT()));
    @Override
    public String parse(State<Token> s) throws Throwable {
        var result = parser.parse(s);
        return result.content.toString();
    }
}
