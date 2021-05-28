package com.wanmeizhensuo.streams;

import com.wanmeizhensuo.streams.flow.Sink;
import com.wanmeizhensuo.streams.flow.WorkFlow;
import com.wanmeizhensuo.streams.flow.Select;
import com.wanmeizhensuo.streams.parser.*;
import io.vertx.mutiny.sqlclient.Pool;
import jaskell.parsec.ParsecException;
import jaskell.parsec.common.Attempt;
import jaskell.parsec.common.Parsec;
import jaskell.parsec.common.State;
import jaskell.util.Try;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.io.EOFException;
import java.util.List;

import static jaskell.parsec.common.Combinator.between;

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
    Parsec<Token, ImmutablePair<String, String>> saveToParser = new SaveToParser();

    @Override
    public Sink parse(State<Token> s) throws Throwable {
        var verticle = SyncVerticle.flow(flowParser.parse(s))
                .bootstrapServers("127.0.0.1:9093")
                .groupId("doctor_sync_test.api_doctor-001")
                .consumers(1)
                .topic(fromParser.parse(s));
        var selectResult = selectParser.parse(s);
        var saveToResult = saveToParser.parse(s);
        var select = Select.select();
        for (var field : selectResult) {
            verticle.select(select.identity(field));
        }

        return verticle.select(Select.select()).saveTo().schema("").table(saveToResult.getRight());
    }


}
