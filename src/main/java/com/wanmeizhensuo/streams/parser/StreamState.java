package com.wanmeizhensuo.streams.parser;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import jaskell.parsec.ParsecException;
import jaskell.parsec.common.State;

import java.io.EOFException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * TODO
 *
 * @author mars
 * @version 1.0.0
 * @since 2021/03/15 18:12
 */
public class StreamState implements State<Token> {
    private final List<Token> buffer = new ArrayList<>();
    private int current = 0;
    private int tran = -1;


    @Override
    public Token next() throws EOFException {
        if (this.current >= this.buffer.size()) {
            throw new EOFException();
        }
        Token cur_token = this.buffer.get(this.current);
        this.current++;
        return cur_token;
    }

    @Override
    public Integer status() {
        return this.current;
    }

    @Override
    public Integer begin() {
        if (this.tran == -1) {
            this.tran = this.current;
        }
        return this.current;
    }

    @Override
    public Integer begin(Integer tran) {
        if (this.tran > tran) {
            this.tran = tran;
        }
        return this.tran;
    }

    @Override
    public void rollback(Integer tran) {
        if (this.tran == tran) {
            this.tran = -1;
        }
        this.current = tran;
    }

    @Override
    public void commit(Integer tran) {
        if (this.tran == tran) {
            this.tran = -1;
        }
    }

    @Override
    public ParsecException trap(String message) {
        return new ParsecException(this.current, message);
    }


    public StreamState(Object content) throws Throwable {
        this.loadItem(content);
    }

    void loadItem(Object data) {
        if(data instanceof JsonArray) {
            buffer.add(Token.openSqu());
            ((JsonArray)data).forEach(this::loadItem);
            buffer.add(Token.closeSqu());
            return;
        }

        if(data instanceof JsonObject) {
            buffer.add(Token.openCur());
            ((JsonObject)data).forEach(this::loadItem);
            buffer.add(Token.closeCur());
            return;
        }

        if(data instanceof Map.Entry){
            var entry = (Map.Entry)data;
            buffer.add(Token.token(entry.getKey()));
            this.loadItem(entry.getValue());
            return;
        }

        buffer.add(Token.token(data));
    }

    public List<Token> getBuffer(){
        return this.buffer;
    }

    public static StreamState streamState(Object data) throws Throwable {
        return new StreamState(data);
    }
}
