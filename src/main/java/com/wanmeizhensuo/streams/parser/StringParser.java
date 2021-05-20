package com.wanmeizhensuo.streams.parser;

import com.google.errorprone.annotations.Var;
import jaskell.parsec.common.One;
import jaskell.parsec.common.Parsec;

import jaskell.parsec.common.State;
import jaskell.parsec.common.TxtState;

import static jaskell.parsec.common.Atom.one;
import static jaskell.parsec.common.Combinator.*;
import static jaskell.parsec.common.Txt.*;

public class StringParser implements Parsec<Token, Token> {
    final Parsec<Character, Character> character = one();
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
            default:
                throw s.trap(String.format("invalid char  \\%c", c));
        }
    });
    final Parsec<Character, String> parser = between(ch('\''), ch('\''),
            many(choice(attempt(escapeCharacter), character))).bind(joinChars());

    @Override
    public Token parse(State<Token> s) throws Throwable {
        var token = s.next();
        if (token.type != TokenType.STRING){
            var message = String.format("expect a string token but get %s type %s",
                    token.content, token.type);
            throw s.trap(message);
        }
        var state = new TxtState(token.content.toString());
        var content = parser.parse(state);
        return new Token(content, TokenType.STRING);
    }
}