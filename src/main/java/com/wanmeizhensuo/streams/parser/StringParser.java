package com.wanmeizhensuo.streams.parser;

import jaskell.parsec.common.Parsec;

import jaskell.parsec.common.State;
import jaskell.parsec.common.TxtState;

import static com.wanmeizhensuo.streams.parser.common.Collector.*;
import static jaskell.parsec.common.Combinator.*;
import static jaskell.parsec.common.Txt.*;

public class StringParser implements Parsec<Token, String> {
    final Parsec<Character, Character> character = nCh('\'',true);
    final Parsec<Character, Character> escapeCharacter = ch('\\').then(s -> {
        Character c = s.next();
        switch (c) {
            case 'n':
                return '\n';
            case 'r':
                return '\r';
            case 't':
                return '\t';
            case '"':
                return '"';
            case '\'':
                return '\'';
            default:
                throw s.trap(String.format("invalid char  \\%c", c));
        }
    });
    final Parsec<Character, String> parser = between(ch('\''), ch('\''),
            many(choice(attempt(escapeCharacter), character))).bind(joinChars());

    @Override
    public String parse(State<Token> s) throws Throwable {
        var token = oneString().parse(s);

        var state = new TxtState(token.content.toString());
        return parser.parse(state);
    }
}