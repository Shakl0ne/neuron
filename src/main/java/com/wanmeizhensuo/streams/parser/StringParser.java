package com.wanmeizhensuo.streams.parser;

import com.google.errorprone.annotations.Var;
import jaskell.parsec.common.Parsec;

import jaskell.parsec.common.State;
import jaskell.parsec.common.TxtState;

import static jaskell.parsec.common.Combinator.*;
import static jaskell.parsec.common.Txt.*;

public class StringParser implements Parsec<Character, String> {
    private final Parsec<Character, Character> character = attempt(chNone("\"\\"));
    private final Parsec<Character, Character> escapeCharacter = ch('\\').then(s -> {
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
            default:
                throw s.trap(String.format("invalid char  \\%c", c));
        }
    });
    private String content;

    {
        try {
            TxtState s = new TxtState(content, "\n");
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    private Object State;
    private final Parsec<Character, String> stringParser = between(ch('\''), ch('\''),
            many(choice(character, escapeCharacter))).bind(joining());


    @Override
    public String parse(String content) throws Throwable {
        return Parsec.super.parse(content);
    }
}
