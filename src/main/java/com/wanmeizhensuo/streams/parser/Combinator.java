package com.wanmeizhensuo.streams.parser;

import jaskell.parsec.common.Parsec;

/**
 * TODO
 *
 * @author mars
 * @version 1.0.0
 * @since 2021/05/18 17:56
 */
public class Combinator {
    public static OpenCurlyParser openCurlyParser() {
        return new OpenCurlyParser();
    }

    public static OpenSquareParser openSquareParser() {
        return new OpenSquareParser();
    }

    public static CloseCurlyParser closeCurlyParser() {
        return new CloseCurlyParser();
    }

    public static CloseSquareParser closeSquareParser() {
        return new CloseSquareParser();
    }

    public static  OpenCurly openCurly() {
        return new OpenCurly();
    }

    public static OpenSquare openSquare() { return new OpenSquare(); }

    public static CloseCurly closeCurly() {
        return new CloseCurly();
    }

    public static CloseSquare closeSquare() {
        return new CloseSquare();
    }

    public static Parsec<Token, Token> nameT() {
        return s -> {
            var result = s.next();
            if(result.type == TokenType.NAME){
                return result;
            } else {
                var message = String.format("expect a name token but type of %s is %s",
                        result, result.type);
                throw s.trap(message);
            }
        };
    }


    public static Parsec<Token, Token> integerT() {
        return s -> {
            var result = s.next();
            if(result.type == TokenType.INTEGER){
                return result;
            } else {
                var message = String.format("expect a integer token but type of %s is %s",
                        result, result.type);
                throw s.trap(message);
            }
        };
    }
    public static Parsec<Token, Token> longT() {
        return s -> {
            var result = s.next();
            if(result.type == TokenType.LONG){
                return result;
            } else {
                var message = String.format("expect a long token but type of %s is %s",
                        result, result.type);
                throw s.trap(message);
            }
        };
    }
    public static Parsec<Token, Token> floatT() {
        return s -> {
            var result = s.next();
            if(result.type == TokenType.FLOAT){
                return result;
            } else {
                var message = String.format("expect a float token but type of %s is %s",
                        result, result.type);
                throw s.trap(message);
            }
        };
    }
    public static Parsec<Token, Token> doubleT() {
        return s -> {
            var result = s.next();
            if(result.type == TokenType.DOUBLE){
                return result;
            } else {
                var message = String.format("expect a double token but type of %s is %s",
                        result, result.type);
                throw s.trap(message);
            }
        };
    }
    public static Parsec<Token, Token> booleanT() {
        return s -> {
            var result = s.next();
            if(result.type == TokenType.BOOLEAN){
                return result;
            } else {
                var message = String.format("expect a boolean token but type of %s is %s",
                        result, result.type);
                throw s.trap(message);
            }
        };
    }


    public static Parsec<Token, Token> nameT(String token) {
        return token(new Token(token, TokenType.NAME));
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

    public static Parsec<Token, Token> booleanT(Boolean token){ return token(new Token(token, TokenType.BOOLEAN)); }

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
