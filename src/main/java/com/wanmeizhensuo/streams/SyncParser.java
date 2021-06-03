package com.wanmeizhensuo.streams;

import com.wanmeizhensuo.streams.flow.Sink;
import com.wanmeizhensuo.streams.flow.Select;
import com.wanmeizhensuo.streams.parser.*;
import com.wanmeizhensuo.streams.parser.common.SaveToAny;
import io.vertx.mutiny.sqlclient.Pool;
import jaskell.parsec.common.Parsec;
import jaskell.parsec.common.State;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * TODO
 *
 * @author mars
 * @version 1.0.0
 * @since 2021/03/15 19:25
 */
public class SyncParser implements Parsec<Token, Sink> {

    private Pool pool;

    public SyncParser(Pool pool) {
        this.pool = pool;
    }

    Parsec<Token, String> flowParser = new FlowParser();
    Parsec<Token, String> fromParser = new FromParser();
    Parsec<Token, List<String>> selectParser = new SelectParser();
    Parsec<Token, String> save2PGParser = new Save2PGParser();
    Parsec<Token, LinkedHashMap<String, String>> save2EsParser = new Save2EsParser();

    @Override
    public Sink parse(State<Token> s) throws Throwable {
        var verticle = SyncVerticle.flow(flowParser.parse(s))
                .bootstrapServers("127.0.0.1:9093")
                .groupId("doctor_sync_test.api_doctor-001")
                .consumers(1)
                .topic(fromParser.parse(s));
        var selectResult = selectParser.parse(s);
        var saveToResult = save2EsParser.parse(s);
        var select = Select.select();
        for (var field : selectResult) {
            verticle.select(select.identity(field));
        }

        return verticle.select(Select.select()).saveTo().schema("").table(saveToResult.get("name"));
    }


}
