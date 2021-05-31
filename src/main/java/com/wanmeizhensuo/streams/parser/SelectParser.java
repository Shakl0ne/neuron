package com.wanmeizhensuo.streams.parser;

import com.wanmeizhensuo.streams.parser.combination.JsonArrParser;
import jaskell.parsec.common.Parsec;
import jaskell.parsec.common.State;

import java.util.List;
import java.util.stream.Collectors;

import static com.wanmeizhensuo.streams.parser.Combinator.*;
import static jaskell.parsec.common.Combinator.*;

public class SelectParser implements  Parsec<Token, List<String>>{
    final Parsec<Token, List<String>> parser = openSquareParser().then(Combinator.nameT("select"))
            .then(new FieldsParser());

    @Override
    public List<String> parse(State<Token> s) throws Throwable {
        return parser.parse(s);
    }


}
