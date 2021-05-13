package com.wanmeizhensuo.streams;

import jaskell.sql.Left;

/**
 * TODO
 *
 * @author mars
 * @version 1.0.0
 * @since 2021/03/15 18:13
 */
public class Token {
    String token;

    public Token(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    @Override
    public String  toString() {
        return token;
    }

    public static class Left extends Token {
        public Left() {
            super("[");
        }
    }

    public static class Right extends Token {
        public Right() {
            super("]");
        }
    }


    public static Left left() {
        return new Left();
    }

    public static Right right() {
        return new Right();
    }

    public static Token token(String token) {
        return new Token(token);
    }
}
