package com.wanmeizhensuo.streams;

import com.wanmeizhensuo.streams.parser.StreamState;
import com.wanmeizhensuo.streams.parser.Token;
import io.vertx.core.json.Json;
import jaskell.parsec.common.Parsec;
import org.json.simple.parser.JSONParser;
import org.junit.Assert;
import org.junit.Test;

import java.io.FileReader;
import java.util.List;
import java.util.Date;
/**
 * TODO
 *
 * @author mars
 * @version 1.0.0
 * @since 2021/05/18 15:44
 */
public class StateTests {
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



    @Test
    public void testSample5() throws Throwable {
        var obj = new JSONParser().parse(new FileReader("src/test/resources/basic/simple-0.json"));
        var data = Json.decodeValue(obj.toString());

        var state = new StreamState(data);
        var elements = List.of(
                Token.openSqu(), Token.token("flow"), Token.token("flow name"), Token.openSqu(),
                Token.token("from"), Token.token("simple.database.table.topic"), Token.closeSqu(),
                Token.openSqu(), Token.token("select"), Token.openSqu(), Token.token("field1"),
                Token.token("field2"), Token.token("field3"), Token.openSqu(), Token.token("named"),
                Token.token("field4"), Token.token("caption-0"), Token.closeSqu(), Token.openSqu(),
                Token.token("named"), Token.openSqu(), Token.token("replace"), Token.token("field5"),
                Token.token("'sub'"), Token.token("'content'"), Token.closeSqu(), Token.token("caption-1"),
                Token.closeSqu(), Token.closeSqu(), Token.closeSqu(), Token.openSqu(), Token.token("saveTo(PG)"),
                Token.token("source name"), Token.closeSqu(), Token.closeSqu()
        );
        for (var ele : elements) {
            var parser = parsec(ele);
            var result = parser.parse(state);
            Assert.assertEquals(ele, result);
        }
/*
        [OPEN_SQUARE_BRACKET<[>, NAME<flow>, NAME<flow name>, OPEN_SQUARE_BRACKET<[>, NAME<from>,
        NAME<simple.database.table.topic>, CLOSE_SQUARE_BRACKET<]>, OPEN_SQUARE_BRACKET<[>, NAME<select>,
        OPEN_SQUARE_BRACKET<[>, NAME<field1>, NAME<field2>, NAME<field3>, OPEN_SQUARE_BRACKET<[>, NAME<named>,
        NAME<field4>, NAME<caption-0>, CLOSE_SQUARE_BRACKET<]>, OPEN_SQUARE_BRACKET<[>, NAME<named>,
        OPEN_SQUARE_BRACKET<[>, NAME<replace>, NAME<field5>, STRING<'sub'>, STRING<'content'>, CLOSE_SQUARE_BRACKET<]>,
        NAME<caption-1>, CLOSE_SQUARE_BRACKET<]>, CLOSE_SQUARE_BRACKET<]>, CLOSE_SQUARE_BRACKET<]>, OPEN_SQUARE_BRACKET<[>,
        NAME<saveTo(PG)>, NAME<source name>, CLOSE_SQUARE_BRACKET<]>, CLOSE_SQUARE_BRACKET<]>]
*/
    }

    @Test
    public void testSample6() throws Throwable {
        var obj = new JSONParser().parse(new FileReader("src/test/resources/basic/sample-elastic-0.json"));
        var data = Json.decodeValue(obj.toString());
        var state = new StreamState(data);

        var elements = List.of(
                Token.openSqu(), Token.token("flow"), Token.token("flow name"), Token.openSqu(), Token.token("from"),
                Token.token("simple.database.table.topic"), Token.openSqu(), Token.token("join"), Token.openCur(),
                Token.token("source"), Token.token("source0"), Token.token("table"), Token.token("table name"),
                Token.token("on"), Token.openSqu(), Token.token("field.name.or.path"), Token.token("table.field.name"),
                Token.closeSqu(), Token.closeCur(), Token.openCur(), Token.token("as"), Token.token("source1"),
                Token.token("source"), Token.token("source"), Token.token("table"), Token.token("table name"),
                Token.token("on"), Token.openSqu(), Token.token("field.name.or.path"), Token.token("table.field.name"),
                Token.closeSqu(), Token.closeCur(), Token.closeSqu(), Token.closeSqu(), Token.openSqu(),
                Token.token("select"), Token.openCur(), Token.token("field1"), Token.openSqu(), Token.token("source0"),
                Token.token("field1"), Token.closeSqu(), Token.token("field0"), Token.token("field0"),
                Token.token("field3"), Token.openSqu(), Token.token("object"), Token.openCur(), Token.token("field3.1"),
                Token.token("field4"),Token.token("field3.0"), Token.openSqu(), Token.token("from"),
                Token.token("source0"), Token.token("field2.or.path"), Token.closeSqu(),  Token.closeCur(),
                Token.closeSqu(), Token.token("field2"), Token.openSqu(), Token.token("from"), Token.token("source1"),
                Token.token("field2.or.path"), Token.closeSqu(),  Token.token("field4"), Token.openSqu(),
                Token.token("list"), Token.token("field7"), Token.openSqu(), Token.token("from"), Token.token("source"),
                Token.token("path.or.field"), Token.closeSqu(), Token.closeSqu(), Token.closeCur(), Token.closeSqu(),
                Token.openSqu(), Token.token("saveToEs"), Token.openCur(), Token.token("path"), Token.token("index path"),
                Token.token("name"), Token.token("es name"), Token.closeCur(), Token.closeSqu(), Token.closeSqu()
        );
        for (var ele : elements) {
            var parser = parsec(ele);
            var result = parser.parse(state);
            Assert.assertEquals(ele, result);
        }
/*
        [OPEN_SQUARE_BRACKET<[>, NAME<flow>, NAME<flow name>, OPEN_SQUARE_BRACKET<[>, NAME<from>,
        NAME<simple.database.table.topic>, OPEN_SQUARE_BRACKET<[>, NAME<join>, OPEN_CURLY_BRACKET<{>,
        NAME<source>, NAME<source0>, NAME<table>, NAME<table name>, NAME<on>, OPEN_SQUARE_BRACKET<[>,
        NAME<field.name.or.path>, NAME<table.field.name>, CLOSE_SQUARE_BRACKET<]>, CLOSE_CURLY_BRACKET<}>,
        OPEN_CURLY_BRACKET<{>, NAME<as>, NAME<source1>, NAME<source>, NAME<source>, NAME<table>,
        NAME<table name>, NAME<on>, OPEN_SQUARE_BRACKET<[>, NAME<field.name.or.path>, NAME<table.field.name>,
        CLOSE_SQUARE_BRACKET<]>, CLOSE_CURLY_BRACKET<}>, CLOSE_SQUARE_BRACKET<]>, CLOSE_SQUARE_BRACKET<]>,
        OPEN_SQUARE_BRACKET<[>, NAME<select>, OPEN_CURLY_BRACKET<{>, NAME<field1>, OPEN_SQUARE_BRACKET<[>,
        NAME<source0>, NAME<field1>, CLOSE_SQUARE_BRACKET<]>, NAME<field0>, NAME<field0>, NAME<field3>,
        OPEN_SQUARE_BRACKET<[>, NAME<object>, OPEN_CURLY_BRACKET<{>, NAME<field3.1>, NAME<field4>, NAME<field3.0>,
        OPEN_SQUARE_BRACKET<[>, NAME<from>, NAME<source0>, NAME<field2.or.path>, CLOSE_SQUARE_BRACKET<]>,
        CLOSE_CURLY_BRACKET<}>, CLOSE_SQUARE_BRACKET<]>, NAME<field2>, OPEN_SQUARE_BRACKET<[>, NAME<from>,
        NAME<source1>, NAME<field2.or.path>, CLOSE_SQUARE_BRACKET<]>, NAME<field4>, OPEN_SQUARE_BRACKET<[>,
        NAME<list>, NAME<field7>, OPEN_SQUARE_BRACKET<[>, NAME<from>, NAME<source>, NAME<path.or.field>,
        CLOSE_SQUARE_BRACKET<]>, CLOSE_SQUARE_BRACKET<]>, CLOSE_CURLY_BRACKET<}>, CLOSE_SQUARE_BRACKET<]>,
        OPEN_SQUARE_BRACKET<[>, NAME<saveToEs>, OPEN_CURLY_BRACKET<{>, NAME<path>, NAME<index path>, NAME<name>,
        NAME<es name>, CLOSE_CURLY_BRACKET<}>, CLOSE_SQUARE_BRACKET<]>, CLOSE_SQUARE_BRACKET<]>]
*/

    }

}
