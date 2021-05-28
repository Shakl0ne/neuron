package com.wanmeizhensuo.streams.parser;

import jaskell.parsec.common.Parsec;
import jaskell.parsec.common.State;

import java.util.List;
import java.util.stream.Collectors;

import static com.wanmeizhensuo.streams.parser.Combinator.*;
import static jaskell.parsec.common.Combinator.*;

public class SelectParser implements  Parsec<Token, List<String>>{
    final Parsec<Token, List<Token>> parser = between(openSquareParser(), closeSquareParser(),
            Combinator.nameT("select").then(many1(Combinator.nameT())));

    @Override
    public List<String> parse(State<Token> s) throws Throwable {
        return parser.parse(s).stream().map(token -> token.content.toString()).collect(Collectors.toList());
    }


}
