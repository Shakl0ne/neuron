package com.wanmeizhensuo.streams.parser;

import jaskell.parsec.common.Parsec;
import jaskell.parsec.common.State;

import java.util.List;
import java.util.stream.Collectors;

import static jaskell.parsec.common.Combinator.*;
import static com.wanmeizhensuo.streams.parser.Parsers.*;

public class FieldParser implements Parsec<Token, List<Object>> {
    final Parsec<Token, List<Token>> parser = between(openSquare(),closeSquare(),
            many1(choice(attempt(Parsers.nameT()),attempt(integerT()),attempt(longT()),
                    attempt(doubleT()),attempt(floatT()),attempt(booleanT()),attempt(nullT()))));

    @Override
    public List<Object> parse(State<Token> s) throws Throwable {
        var result = parser.parse(s);
        return result.stream().map(token -> token.content).collect(Collectors.toList());
    }



}
