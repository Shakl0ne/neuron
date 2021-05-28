package com.wanmeizhensuo.streams.parser;

import jaskell.parsec.common.Parsec;
import jaskell.parsec.common.State;

import java.util.List;
import java.util.stream.Collectors;

import static jaskell.parsec.common.Combinator.*;
import static com.wanmeizhensuo.streams.parser.Combinator.*;

public class FieldsParser implements Parsec<Token, List<Object>> {
    final Parsec<Token, Token> oneToken = choice(attempt(nameT()),attempt(integerT()),attempt(longT()),
                    attempt(doubleT()),attempt(floatT()),attempt(booleanT()),attempt(nullT()));


    final Parsec<Token, List<Token>> parser = between(openSquareParser(), closeSquareParser(),
            many1(choice(oneToken)));

    @Override
    public List<Object> parse(State<Token> s) throws Throwable {
        var result = parser.parse(s);
        return result.stream().map(token -> token.content).collect(Collectors.toList());
    }


}
