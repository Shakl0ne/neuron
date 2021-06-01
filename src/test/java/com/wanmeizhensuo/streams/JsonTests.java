package com.wanmeizhensuo.streams;

import com.wanmeizhensuo.streams.parser.StreamState;
import io.vertx.core.json.Json;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;

public class JsonTests {
    @Test
    public void testSample0 () throws Throwable {
        var obj = new JSONParser().parse(new FileReader("src/test/resources/basic/simple-0.json"));
        var data = Json.decodeValue(obj.toString());
        var state = new StreamState(data);
        System.out.println(state.getBuffer());
        var res = ("[OPEN_SQUARE_BRACKET<[>, NAME<flow>, NAME<flow name>, OPEN_SQUARE_BRACKET<[>, NAME<from>, NAME<simple.database.table.topic>, " +
                "CLOSE_SQUARE_BRACKET<]>, OPEN_SQUARE_BRACKET<[>, NAME<select>, OPEN_SQUARE_BRACKET<[>, NAME<field1>, NAME<field2>, NAME<field3>, " +
                "OPEN_SQUARE_BRACKET<[>, NAME<named>, NAME<field4>, NAME<caption-0>, CLOSE_SQUARE_BRACKET<]>, OPEN_SQUARE_BRACKET<[>, NAME<named>, " +
                "OPEN_SQUARE_BRACKET<[>, NAME<replace>, NAME<field5>, STRING<'sub'>, STRING<'content'>, CLOSE_SQUARE_BRACKET<]>, NAME<caption-1>, " +
                "CLOSE_SQUARE_BRACKET<]>, CLOSE_SQUARE_BRACKET<]>, CLOSE_SQUARE_BRACKET<]>, OPEN_SQUARE_BRACKET<[>, NAME<saveTo(PG)>, NAME<source name>, " +
                "CLOSE_SQUARE_BRACKET<]>, CLOSE_SQUARE_BRACKET<]>]");
    }
    @Test
    public void testSample1 () throws Throwable {
        var obj = new JSONParser().parse(new FileReader("src/test/resources/basic/sample-elastic-0.json"));
        var data = Json.decodeValue(obj.toString());
        var state = new StreamState(data);
        System.out.println(state.getBuffer());
        var res = ("[OPEN_SQUARE_BRACKET<[>, NAME<flow>, NAME<flow name>, OPEN_SQUARE_BRACKET<[>, NAME<from>, NAME<simple.database.table.topic>, " +
                "OPEN_SQUARE_BRACKET<[>, NAME<join>, OPEN_CURLY_BRACKET<{>, NAME<source>, NAME<source0>, NAME<table>, NAME<table name>, NAME<on>, " +
                "OPEN_SQUARE_BRACKET<[>, NAME<field.name.or.path>, NAME<table.field.name>, CLOSE_SQUARE_BRACKET<]>, CLOSE_CURLY_BRACKET<}>, " +
                "OPEN_CURLY_BRACKET<{>, NAME<as>, NAME<source1>, NAME<source>, NAME<source>, NAME<table>, NAME<table name>, NAME<on>, " +
                "OPEN_SQUARE_BRACKET<[>, NAME<field.name.or.path>, NAME<table.field.name>, CLOSE_SQUARE_BRACKET<]>, CLOSE_CURLY_BRACKET<}>, " +
                "CLOSE_SQUARE_BRACKET<]>, CLOSE_SQUARE_BRACKET<]>, OPEN_SQUARE_BRACKET<[>, NAME<select>, OPEN_CURLY_BRACKET<{>, NAME<field1>, " +
                "OPEN_SQUARE_BRACKET<[>, NAME<source0>, NAME<field1>, CLOSE_SQUARE_BRACKET<]>, NAME<field0>, NAME<field0>, NAME<field3>, " +
                "OPEN_SQUARE_BRACKET<[>, NAME<object>, OPEN_CURLY_BRACKET<{>, NAME<field3.1>, NAME<field4>, NAME<field3.0>, OPEN_SQUARE_BRACKET<[>, " +
                "NAME<from>, NAME<source0>, NAME<field2.or.path>, CLOSE_SQUARE_BRACKET<]>, CLOSE_CURLY_BRACKET<}>, CLOSE_SQUARE_BRACKET<]>, " +
                "NAME<field2>, OPEN_SQUARE_BRACKET<[>, NAME<from>, NAME<source1>, NAME<field2.or.path>, CLOSE_SQUARE_BRACKET<]>, NAME<field4>, " +
                "OPEN_SQUARE_BRACKET<[>, NAME<list>, NAME<field7>, OPEN_SQUARE_BRACKET<[>, NAME<from>, NAME<source>, NAME<path.or.field>, " +
                "CLOSE_SQUARE_BRACKET<]>, CLOSE_SQUARE_BRACKET<]>, CLOSE_CURLY_BRACKET<}>, CLOSE_SQUARE_BRACKET<]>, OPEN_SQUARE_BRACKET<[>, " +
                "NAME<saveToEs>, OPEN_CURLY_BRACKET<{>, NAME<path>, NAME<index path>, NAME<name>, NAME<es name>, CLOSE_CURLY_BRACKET<}>, " +
                "CLOSE_SQUARE_BRACKET<]>, CLOSE_SQUARE_BRACKET<]>]");
    }

}
