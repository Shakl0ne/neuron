package com.wanmeizhensuo.streams.parser;

import io.vertx.core.json.Json;
import org.json.simple.parser.JSONParser;
import org.junit.Assert;
import org.junit.Test;

import java.io.FileReader;
import java.util.LinkedHashMap;

public class SaveToParserTests {
    @Test
    public void testSample0() throws Throwable {
        var obj = new JSONParser().parse(new FileReader("src/test/resources/basic/simple-0.json"));
        var data = Json.decodeValue(obj.toString());
        var state = new StreamState(data);
        String res = "source name";
        var parser = new Save2PGParser();
        Assert.assertEquals(res, parser.parse(state));
    }
    @Test
    public void testSample1() throws Throwable {
        var obj = new JSONParser().parse(new FileReader("src/test/resources/basic/sample-elastic-0.json"));
        var data = Json.decodeValue(obj.toString());
        var state = new StreamState(data);
        LinkedHashMap<String, String> resDict = new LinkedHashMap<>();
        resDict.put("name","es name");
        resDict.put("path","index path");
        var parser = new Save2EsParser();
        Assert.assertEquals(resDict, parser.parse(state));
    }
}
