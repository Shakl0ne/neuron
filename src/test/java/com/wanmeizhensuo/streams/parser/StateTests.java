package com.wanmeizhensuo.streams.parser;

import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import jaskell.parsec.common.Parsec;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * TODO
 *
 * @author mars
 * @version 1.0.0
 * @since 2021/05/18 15:44
 */
public class StateTests {
    @Test
    public void testSample0() throws Throwable {
        var data = Json.decodeValue("[1, 2, 3]");
        var elements = List.of(Token.openSqu(),
                Token.token(1),
                Token.token(2),
                Token.token(3),
                Token.closeSqu());
        var state = new StreamState(data);
        for(var ele: elements) {
            var parser = parsec(ele);
            var result = parser.parse(state);
            Assert.assertEquals(ele, result);
        }
    }

    Parsec<Token, Token> parsec(Token token) {
        return state -> {
            var item= state.next();
            if(item.equals(token)){
                return item;
            }
            throw state.trap(String.format("expect token %s but get %s",
                    token.toString(), item));
        };
    }
}