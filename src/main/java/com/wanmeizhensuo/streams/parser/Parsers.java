package com.wanmeizhensuo.streams.parser;

import jaskell.parsec.common.Parsec;

/**
 * TODO
 *
 * @author mars
 * @version 1.0.0
 * @since 2021/05/18 17:56
 */
public class Parsers {
    public static OpenCurlyParser openCurly() {
        return new OpenCurlyParser();
    }
    public static OpenSquareParser openSquare() {
        return new OpenSquareParser();
    }
    public static CloseCurlyParser closeCurly() {
        return new CloseCurlyParser();
    }

    public static CloseSquareParser closeSquare() {
        return new CloseSquareParser();
    }

    public static Parsec<Token, Token> str(String token) {
        return token(new Token(token, TokenType.STRING));
    }

    public static Parsec<Token, Token> str() {
        return s -> {
            var result = s.next();
            if(result.type == TokenType.STRING){
                return result;
            } else {
                var message = String.format("expect a string token but type of %s is %s",
                        result, result.type);
                throw s.trap(message);
            }
        };
    }

    public static Parsec<Token, Token> integerT(Integer token){
        return token(new Token(token, TokenType.INTEGER));
    }

    public static Parsec<Token, Token> longT(Long token){
        return token(new Token(token, TokenType.LONG));
    }

    public static Parsec<Token, Token> floatT(Float token) {
        return token(new Token(token, TokenType.FLOAT));
    }

    public static Parsec<Token, Token> doubleT(Double token){
        return token(new Token(token, TokenType.DOUBLE));
    }

    public static Parsec<Token, Token> bool(Boolean token){
        return token(new Token(token, TokenType.BOOLEAN));
    }

    public static Parsec<Token, Token> nullT(){
        return token(new Token(null, TokenType.NULL));
    }

    public static Parsec<Token, Token> token(Token token){
        return state -> {
            var element = state.next();
            if(token.equals(element)){
                return element;
            } else {
                var message = String.format("expect token %s but get %s",
                        token, element);
                throw state.trap(message);
            }
        };
    }
}
