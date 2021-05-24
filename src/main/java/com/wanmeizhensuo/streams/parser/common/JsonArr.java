package com.wanmeizhensuo.streams.parser.common;

import com.wanmeizhensuo.streams.parser.Token;
import jaskell.parsec.common.Parsec;
import jaskell.parsec.common.State;

import java.util.List;
import java.util.stream.Collectors;

import static com.wanmeizhensuo.streams.parser.Parsers.*;
import static jaskell.parsec.common.Atom.eof;
import static jaskell.parsec.common.Combinator.*;

public class JsonArr implements Parsec<Token, Object> {
    final Parsec<Token, Token> oneToken = choice(attempt(nameT()),attempt(integerT()),attempt(longT()),
                    attempt(doubleT()),attempt(floatT()),attempt(booleanT()),attempt(nullT()));

    final Parsec<Token, ?> jsonArr = openSquare().then(oneToken).then(closeSquare());

    @Override
    public Object parse(State<Token> s) throws Throwable {
        return jsonArr.parse(s);
    }


}
