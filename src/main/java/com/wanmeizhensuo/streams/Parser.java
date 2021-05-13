package com.wanmeizhensuo.streams;

import com.wanmeizhensuo.streams.flow.WorkFlow;
import com.wanmeizhensuo.streams.flow.Select;
import io.vertx.mutiny.sqlclient.Pool;
import jaskell.parsec.ParsecException;
import jaskell.parsec.common.Attempt;
import jaskell.parsec.common.Parsec;
import jaskell.parsec.common.State;
import jaskell.util.Try;

import java.io.EOFException;

import static jaskell.parsec.common.Combinator.between;

/**
 * TODO
 *
 * @author mars
 * @version 1.0.0
 * @since 2021/03/15 19:25
 */
public class Parser implements Parsec<Token, SyncStream> {

    private Pool pool;

    public Parser() {
    }

    public Parser(Pool pool) {
        this.pool = pool;
    }

    Parsec<Token, String> selectParser = between(new Left(), new Right(), new Func("select"));

    @Override
    public SyncStream parse(State<Token> s) throws Throwable {

        var bootstrapServers = getParam("bootstrapServers", s);
        var syncName = getParam("syncName", s);
        var applicationId = getParam("applicationId", s);
        var consumerCount = Integer.valueOf(getParam("consumerCount", s));
        var sinkSchema = getParam("sinkSchema", s);
        var sinkTable = getParam("sinkTable", s);
        var topic = getParam("from", s);

        var syncStream = SyncStream.from(syncName)
                .bootstrapServers(bootstrapServers)
                .groupId(applicationId)
                .consumers(consumerCount)
                .topic(topic);

        var select = Select.select();
        Try<Select> re;
        do {
            re = new Attempt<>(between(new Left(), new Right(), new Field(select))).exec(s);
        } while (re.isOk());

        (new Right()).parse(s);
        return syncStream.select(re.get()).saveTo().schema(sinkSchema).table(sinkTable).sync();
    }

    public String getParam(String functionName, State<Token> s) throws Throwable {
        return between(new Left(), new Right(), new Func(functionName).then(new Text())).parse(s);
    }

    public class Left implements Parsec<Token, Token> {
        @Override
        public Token parse(State<Token> s) throws EOFException, ParsecException {
            var item = s.next();
            if (item instanceof Token.Left) {
                return item;
            } else {
                var message = String.format("expect left token but get %s", item.toString());
                throw s.trap(message);
            }
        }
    }

    public class Right implements Parsec<Token, Token> {
        @Override
        public Token parse(State<Token> s) throws EOFException, ParsecException {
            var item = s.next();
            if (item instanceof Token.Right) {
                return item;
            } else {
                var message = String.format("expect right token but get %s", item.toString());
                throw s.trap(message);
            }
        }
    }

    public class Text implements Parsec<Token, String> {
        @Override
        public String parse(State<Token> s) throws EOFException, ParsecException {
            var item = s.next();
            if (item != null) {
                return item.token;
            } else {
                var message = String.format("expect text token but get %s", item.toString());
                throw s.trap(message);
            }
        }
    }

    public class Field implements Parsec<Token, Select> {
        WorkFlow flow;
        Text parser = new Text();

        @Override
        public Select parse(State<Token> s) throws EOFException, ParsecException {
            var column = parser.parse(s);
            var func = parser.parse(s);
            Select select = Select.select();
            if (func.equals("string")) {
                return select.text(column);
            }
            if (func.equals("integer")) {
                return select.int32(column);
            }
            if (func.equals("long")) {
                return select.int64(column);
            }
            if (func.equals("bool")) {
                return select.bool(column);
            }
            if (func.equals("intToBool")) {
                return select.intToBool(column);
            }
            if (func.equals("millSecond2Date")) {
                return select.millSecond2Date(column);
            }
            if (func.equals("millSecond2Timestamp")) {
                return select.millSecond2Timestamp(column);
            }
            if (func.equals("microSecond2Timestamp")) {
                return select.microSecond2Timestamp(column);
            }
            if (func.equals("microSecond2TimestampWithZone")) {
                return select.microSecond2TimestampWithZone(column);
            }
            if (func.equals("millSecond2TimestampWithZone")) {
                return select.millSecond2TimestampWithZone(column);
            }
            return select;
        }

        public Field(Select select) {
            this.flow.select(select);
        }
    }

    public class Func implements Parsec<Token, String> {
        String name;

        @Override
        public String parse(State<Token> s) throws EOFException, ParsecException {
            var t = s.next();
            if (t.token.equals(name)) {
                return name;
            } else {
                var message = String.format("expect funct %s but get %s", name, t.token);
                throw s.trap(message);
            }
        }

        public Func(String name) {
            this.name = name;
        }
    }
}
