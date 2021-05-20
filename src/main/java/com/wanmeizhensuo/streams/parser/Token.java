package com.wanmeizhensuo.streams.parser;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

import static com.wanmeizhensuo.streams.parser.TokenType.*;

/**
 * TODO
 *
 * @author mars
 * @version 1.0.0
 * @since 2021/03/15 18:13
 */
@Getter
@Setter
@AllArgsConstructor
@Builder
public class Token {
    Object content;
    TokenType type;

    @Override
    public String toString() {
        if (type == NULL){
            return "NULL<NULL>";
        } else {
            return String.format("%s<%s>", type.toString(), content.toString());
        }
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Token){
            var item = (Token) obj;
            if(this.type == NULL && item.type == NULL){
                return true;
            }
            return this.type == item.type && Objects.equals(this.content, item.content);
        }
        return false;
    }

    public static class OpenSqu extends Token {
        public OpenSqu() {
            super("[", OPEN_SQUARE_BRACKET);
        }
    }

    public static class CloseSqu extends Token {
        public CloseSqu() {
            super("]", CLOSE_SQUARE_BRACKET);
        }
    }

    public static class OpenCur extends Token {
        public OpenCur() {
            super("{", OPEN_CURLY_BRACKET);
        }
    }

    public static class CloseCur extends Token {
        public CloseCur() {
            super("}", CLOSE_CURLY_BRACKET);
        }
    }


    public static OpenSqu openSqu() {
        return new OpenSqu();
    }

    public static CloseSqu closeSqu() {
        return new CloseSqu();
    }

    public static OpenCur openCur() {
        return new OpenCur();
    }

    public static CloseCur closeCur() {
        return new CloseCur();
    }

    public static Token token(Object token) {
        if (token instanceof String) {
            if ( ((String) token).startsWith("'") && ((String) token).endsWith("'")) {
                return new Token(token, STRING);
            }
            return new Token(token, NAME);
        }
        if (token instanceof Integer) {
            return new Token(token, INTEGER);
        }
        if(token instanceof Long){
            return new Token(token, LONG);
        }
        if(token instanceof Float){
            return new Token(token, FLOAT);
        }
        if(token instanceof Double){
            return new Token(token, DOUBLE);
        }
        if(token instanceof Boolean){
            return new Token(token, BOOLEAN);
        }
        if(token == null) {
            return new Token(null, NULL);
        }
        String message = String.format("unsupported item %s type %s", token.toString(), token.getClass());
        throw new IllegalArgumentException(message);
    }
}
