package com.wanmeizhensuo.streams;

import com.wanmeizhensuo.streams.parser.*;
import io.vertx.core.json.Json;
import org.json.simple.parser.JSONParser;
import org.junit.Assert;
import org.junit.Test;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Stream;

public class FlowTests {
    @Test
    public void test_simple_0() throws Throwable {
        var obj = new JSONParser().parse(new FileReader("src/test/resources/basic/simple-0.json"));
        var data = Json.decodeValue(obj.toString());

        var flowState = new StreamState(data);
        var flowParser = new FlowParser();
        String flowRes = "flow name";
        Assert.assertEquals(flowRes,flowParser.parse(flowState));

        var fromState = new StreamState(data);
        var fromParser = new FromParser();
        var fromRes = "simple.database.table.topic";
        Assert.assertEquals(fromRes, fromParser.parse(fromState));

        var selectState = new StreamState(data);
        var selectParser = new SelectParser();
        String [] resArray = {"[", "field1", "field2", "field3", "[", "named", "field4", "caption-0", "]",
                "[", "named", "[", "replace", "field5", "'sub'", "'content'", "]", "caption-1", "]", "]"};
        Assert.assertEquals(Arrays.asList(resArray),selectParser.parse(selectState));

        var saveToState = new StreamState(data);
        var save2PGParser = new Save2PGParser();
        String saveToRes = "source name";
        Assert.assertEquals(saveToRes, save2PGParser.parse(saveToState));
    }
    @Test
    public void test_sample_elastic_0() throws Throwable {
        var obj = new JSONParser().parse(new FileReader("src/test/resources/basic/sample-elastic-0.json"));
        var data = Json.decodeValue(obj.toString());

        var flowState = new StreamState(data);
        var flowParser = new FlowParser();
        String flowRes = "flow name";
        Assert.assertEquals(flowRes,flowParser.parse(flowState));

        var fromState = new StreamState(data);
        var fromParser = new FromParser();
        var fromRes = "simple.database.table.topic";
        Assert.assertEquals(fromRes, fromParser.parse(fromState));

        var selectState = new StreamState(data);
        var selectParser = new SelectParser();
        String [] resArray = {"{", "field1", "[", "source0", "field1", "]", "field0", "field0", "field3",
                "[", "object", "{", "field3.1", "field4", "field3.0", "[", "from", "source0", "field2.or.path", "]",
                "}", "]", "field2", "[", "from", "source1", "field2.or.path", "]", "field4", "[", "list", "field7",
                "[", "from", "source", "path.or.field", "]", "]", "}"};
        Assert.assertEquals(Arrays.asList(resArray),selectParser.parse(selectState));

        var saveToState = new StreamState(data);
        var save2EsParser = new Save2EsParser();
        LinkedHashMap<String, String> resDict = new LinkedHashMap<>();
        resDict.put("name","es name");
        resDict.put("path","index path");
        Assert.assertEquals(resDict, save2EsParser.parse(saveToState));
    }
}
