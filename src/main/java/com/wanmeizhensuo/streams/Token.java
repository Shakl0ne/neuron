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
    private String token;

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

    public static class OpenSqu extends Token {
        public OpenSqu() {
            super("[");
        }
    }

    public static class CloseSqu extends Token {
        public CloseSqu() {
            super("]");
        }
    }

    public static class OpenCur extends Token {
        public OpenCur() {
            super("{");
        }
    }

    public static class CloseCur extends Token {
        public  CloseCur() {
            super("}");
        }
    }


    public static OpenSqu openSqu() {
        return new OpenSqu();
    }

    public static CloseSqu closeSqu() {
        return new CloseSqu();
    }

    public static OpenCur openCur() { return new OpenCur(); }

    public static CloseCur closeCur() { return new CloseCur(); }

    public static Token token(String token) {
        return new Token(token);
    }
}
