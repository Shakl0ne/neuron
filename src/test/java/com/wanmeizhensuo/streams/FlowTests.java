package com.wanmeizhensuo.streams;

import com.wanmeizhensuo.streams.parser.*;
import io.vertx.core.json.Json;
import org.junit.Test;

import java.util.stream.Stream;

public class FlowTests {
    @Test
    public void testSample0() throws Throwable {
        var jsonFile = Json.decodeValue("[\"flow\",\"doctor_sync\", [\"from\",\"topicConfiguration.doctorTopic\"]]");
        var flowState = new StreamState(jsonFile);
        var flowParser = new FlowParser();
        var flowResult = flowParser.parse(flowState);
        System.out.println(flowResult);

        var fromFile = Json.decodeValue(("[\"from\",\"topicConfiguration.doctorTopic\"]"));
        var fromState = new StreamState(fromFile);
        var fromParser = new FromParser();
        var fromResult = fromParser.parse(fromState);
        System.out.println(fromResult);

        var selectFile = Json.decodeValue("[\"select\",\"id\",\"doctor_name\",\"tag_id\"]");
        var selectState = new StreamState(selectFile);
        var selectParser = new SelectParser();
        var selectResult = selectParser.parse(selectState);
        System.out.println(selectResult);

        var saveToFile = Json.decodeValue("[\"saveTo(PG)\", \"public.doctor_sync\"]");
        var saveToState = new StreamState(saveToFile);
        var saveToParser = new SaveToParser();
        var saveToResult = saveToParser.parse(saveToState);
        System.out.println(saveToResult);
    }
}
