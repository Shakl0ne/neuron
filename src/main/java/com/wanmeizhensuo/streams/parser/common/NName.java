package com.wanmeizhensuo.streams.parser.common;

import com.wanmeizhensuo.streams.parser.Token;
import com.wanmeizhensuo.streams.parser.TokenType;
import jaskell.parsec.common.Parsec;
import jaskell.parsec.common.State;

import static com.wanmeizhensuo.streams.parser.Parsers.*;
import static com.wanmeizhensuo.streams.parser.Parsers.nullT;
import static jaskell.parsec.common.Combinator.attempt;
import static jaskell.parsec.common.Combinator.choice;

/*
Return a token that is not a specific NAME token
*/

public class NName implements Parsec<Token, Token> {
    private final Token name;
    final Parsec<Token, Token> parser = choice(attempt(nameT()),attempt(stringT()),attempt(integerT()),
            attempt(longT()), attempt(doubleT()),attempt(floatT()),attempt(booleanT()),attempt(nullT()),
            attempt(openCurly()), attempt(openSquare()),attempt(closeCurly()),attempt(closeSquare()));

    public NName(Token name) {
        this.name = name;
    }

    @Override
    public Token parse(State<Token> s) throws Throwable {
        Token t = parser.parse(s);
        if (t.content == name.content && t.type == TokenType.NAME) {
            throw s.trap(String.format("expect any name token is not %s at %s but %s",
                    name.content, s.status().toString(), t.content));
        }
        return t;
    }

}
