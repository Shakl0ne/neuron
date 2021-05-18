package com.wanmeizhensuo.sqlisp;

import com.wanmeizhensuo.streams.Token;
import jaskell.parsec.common.Parsec;
import jaskell.parsec.ParsecException;
import jaskell.parsec.common.State;

import java.io.EOFException;

public class JsonParser implements Parsec<Token, Token>{
    public JsonParser() {
    }

    @Override
    public class parse(State<Token> s){

    }


    public class OpenSqu implements jaskell.parsec.common.Parsec<Token, Token> {
        @Override
        public Token parse(State<Token> s) throws EOFException, ParsecException {
            var item = s.next();
            if (item instanceof Token.OpenSqu) {
                return item;
            } else {
                var message = String.format("expect open square bracket but get %s", item.toString());
                throw s.trap(message);
            }
        }
    }

    public class CloseSqu implements Parsec<Token, Token> {
        @Override
        public Token parse(State<Token> s) throws EOFException, ParsecException {
            var item = s.next();
            if (item instanceof Token.CloseSqu) {
                return item;
            } else {
                var message = String.format("expect close square bracket but get %s", item.toString());
                throw s.trap(message);
            }
        }
    }

    public class OpenCur implements Parsec<Token, Token> {
        @Override
        public Token parse(State<Token> s) throws EOFException, ParsecException {
            var item = s.next();
            if (item instanceof Token.OpenCur) {
                return item;
            } else {
                var message = String.format("expect open curly bracket but get %s", item.toString());
                throw s.trap(message);
            }
        }
    }

    public class CloseCur implements Parsec<Token, Token> {
        @Override
        public Token parse(State<Token> s) throws EOFException, ParsecException {
            var item = s.next();
            if (item instanceof Token.CloseCur) {
                return item;
            } else {
                var message = String.format("expect close cur bracket but get %s", item.toString());
                throw s.trap(message);
            }
        }
    }

    public class Text implements Parsec<Token, String> {
        @Override
        public String parse(State<Token> s) throws EOFException, ParsecException {
            var item = s.next();
            if (item != null) {
                return item.getToken();
            } else {
                var message = String.format("expect text token but get %s", item.toString());
                throw s.trap(message);
            }
        }
    }

}

