package com.wanmeizhensuo.sqlisp.ast;

import com.wanmeizhensuo.sqlisp.ParserException;

import java.util.HashMap;
import java.util.Map;

public class Env {
    private Env global = null;

    public Env getGlobal() { return global; }

    public void setGlobal(Env env) { global = env; }

    private final Map<String, Object> local = new HashMap<>();

    public Object put(String name, Object value) { return local.put(name, value); }

    public Object findOut(String name) throws ParserException {
        if (global == null) {
            throw new ParserException(String.format("%s not found", name));
        }
        return global.get(name);
    }

    public Object findIn(String name) throws ParserException {
        if (local.containsKey(name)) {
            return local.get(name);
        }
        else {
            throw new ParserException(String.format("%s is not found in local", name));
        }
    }

    public boolean existsOut(String name) {
        try {
            if (global == null) {
                return false;
            }
            global.get(name);
            return true;
        }
        catch (ParserException e) {
            return false;
        }
    }

    public boolean existsIn(String name) {
        return local.containsKey(name);
    }

    public Object get(String name) throws ParserException {
        try {
            return findIn(name);
        } catch (ParserException notfound) {
            return findOut(name);
        }
    }

    public boolean exists(String name) {
        return existsIn(name) || existsOut(name);
    }

    public Object eval(Object data) throws ParserException {
        if (data instanceof Element) {
            return ((Element) data).eval(this);
        } else {
            return data;
        }
    }

}
