package com.wanmeizhensuo.streams.parser;

import jaskell.parsec.common.Parsec;
import jaskell.parsec.common.State;

import java.util.List;

import static com.wanmeizhensuo.streams.parser.Parsers.*;
import static jaskell.parsec.common.Combinator.*;
import static com.wanmeizhensuo.streams.parser.common.Collector.*;

public class SelectParser implements  Parsec<Token, List<String>>{
    final Parsec<Token, List<String>> parser = new FieldsParser();

    @Override
    public List<String> parse(State<Token> s) throws Throwable {
        var select = new Token("select",TokenType.NAME);
        var n = nName(select);
        manyTill(n,nameT("select")).parse(s);

        return parser.parse(s);
    }


}
