package com.wanmeizhensuo.streams.parser;

import jaskell.parsec.common.Parsec;
import jaskell.parsec.common.State;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

import static com.wanmeizhensuo.streams.parser.Parsers.*;
import static jaskell.parsec.common.Atom.eof;
import static jaskell.parsec.common.Combinator.*;
import static com.wanmeizhensuo.streams.parser.common.Collector.*;

public class SelectParser implements  Parsec<Token, List<List<String>>> {
    final Parsec<Token, List<String>> fields = openSquareParser().then(many1(name2String())).over(closeSquareParser());
    final Parsec<Token, List<List<String>>> parser = many1(fields).over(closeSquareParser());

    @Override
    public List<List<String>> parse(State<Token> s) throws Throwable {

        var select = new Token("select",TokenType.NAME);
        var n = nName(select);
        manyTill(n,nameT("select")).parse(s);

        return parser.parse(s);
    }


}
