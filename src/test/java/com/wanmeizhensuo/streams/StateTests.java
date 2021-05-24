package com.wanmeizhensuo.streams;

import com.wanmeizhensuo.streams.parser.StreamState;
import com.wanmeizhensuo.streams.parser.Token;
import io.vertx.core.json.Json;
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

    @Test
    public void testSample1() throws Throwable {
        var data = Json.decodeValue("{\"key\":75.37,\"value\":1,\"47\":49}");
        var elements = List.of(
                Token.openCur(),
                Token.token("key"),
                Token.token(75.37),
                Token.token("value"),
                Token.token(1),
                Token.token(String.valueOf(47).toString()),
                Token.token(49),
                Token.closeCur()
        );
        var state = new StreamState(data);
        for(var ele:elements) {
            var parser = parsec(ele);
            var result = parser.parse(state);
            Assert.assertEquals(ele, result);
        }
    }

    @Test
    public void testSample2() throws Throwable {
        var data = Json.decodeValue("[{\"key\":75.37,\"value\":1},null]");
        var elements = List.of(
                Token.openSqu(),
                Token.openCur(),
                Token.token("key"),
                Token.token(75.37),
                Token.token("value"),
                Token.token(1),
                Token.closeCur(),
                Token.token(null),
                Token.closeSqu()
        );
        var state = new StreamState(data);
        for(var ele:elements) {
            var parser = parsec(ele);
            var result = parser.parse(state);
            Assert.assertEquals(ele, result);
        }
    }
    @Test
    public void testSample3() throws Throwable {
        var data = Json.decodeValue("[\"flow\"," +
                "[\"from\",\"simple.database.table.topic\"]," +
                "[\"select\",[758,\"arcade\"]]]");
        var elements = List.of(
                Token.openSqu(),
                Token.token("flow"),
                Token.openSqu(),
                Token.token("from"),
                Token.token("simple.database.table.topic"),
                Token.closeSqu(),
                Token.openSqu(),
                Token.token("select"),
                Token.openSqu(),
                Token.token(758),
                Token.token("arcade"),
                Token.closeSqu(),
                Token.closeSqu(),
                Token.closeSqu()
        );
        var state = new StreamState(data);
        for (var ele : elements) {
            var parser = parsec(ele);
            var result = parser.parse(state);
            Assert.assertEquals(ele, result);
        }
    }
    @Test
    public void testSample4() throws Throwable {
        var data = Json.decodeValue("[{\"flow\": true,\"from\": false,\"subjects\": {" +
                "\"s2\": \"software engineering\" }, \"ST R\": -1073741824 },1073741824]");
        var elements = List.of(
                Token.openSqu(),
                Token.openCur(),
                Token.token("flow"),
                Token.token(true),
                Token.token("from"),
                Token.token(false),
                Token.token("subjects"),
                Token.openCur(),
                Token.token("s2"),
                Token.token("software engineering"),
                Token.closeCur(),
                Token.token("ST R"),
                Token.token(-1073741824),
                Token.closeCur(),
                Token.token(1073741824),
                Token.closeSqu()
        );
        var state = new StreamState(data);
        for (var ele : elements) {
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
