package com.wanmeizhensuo.streams.parser;

import jaskell.parsec.common.Parsec;
import jaskell.parsec.common.State;

import java.util.List;
import java.util.stream.Collectors;

import static com.wanmeizhensuo.streams.parser.Parsers.*;
import static jaskell.parsec.common.Combinator.*;

public class SelectParser implements  Parsec<Token, List<Object>>{
    final Parsec<Token, List<Object>> fieldParser = new FieldParser();
    final Parsec<Token, List<Token>> parser = between(openSquare(),closeSquare(),
            Parsers.nameT("select").then(many1(Parsers.nameT())));

    @Override
    public List<Object> parse(State<Token> s) throws Throwable {
        return parser.parse(s).stream().map(token -> token.content).collect(Collectors.toList());
    }


}
