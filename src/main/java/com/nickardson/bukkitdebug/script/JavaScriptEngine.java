package com.nickardson.bukkitdebug.script;

import org.mozilla.javascript.*;

import java.io.InputStream;
import java.util.Scanner;

public class JavaScriptEngine {
    public JavaScriptEngine() {

    }

    /**
     * Gets the current script context.
     * @return The current script context.
     */
    public Context getContext() {
        return Context.getCurrentContext();
    }

    /**
     * Enters a script context.
     */
    public void enter() {
        Context.enter();
        Context c = getContext();
        c.setOptimizationLevel(optimization);
        c.setLanguageVersion(Context.VERSION_1_8);
    }

    /**
     * Exits the current context.
     */
    public void exit() {
        Context.exit();
    }

    private int optimization = 9;

    /**
     * Gets the current script optimization level.
     * @return The level of optimization, from -1 to 9.
     */
    public int getOptimization() {
        return optimization;
    }

    /**
     * Sets the current script optimization level.
     * @param optimization The level of optimization, from -1 to 9.
     */
    public void setOptimization(int optimization) {
        this.optimization = optimization;
    }

    /**
     * Creates a new scope (global object).
     * @return The created scope.
     */
    public ScriptableObject createScope() {
        enter();
        ScriptableObject scope = new ImporterTopLevel(getContext());

        scope.defineProperty("reflection", new APIReflection(), ScriptableObject.PERMANENT);
        evalStream(scope, getClass().getResourceAsStream("/js/main.js"));

        Context.exit();
        return scope;
    }

    /**
     * Converts the given object into a sandbox-friendly native JavaScript object.
     * @param o The object to convert.
     * @param scope The scope.
     * @return The converted object.
     */
    public Scriptable convert(Object o, Scriptable scope) {
        return ScriptRuntime.toObject(scope, o);
    }

    /**
     * Evaluates the given string
     * @param scope The scope to run in.
     * @param code The code to run.
     */
    public Object eval(Scriptable scope, String code) {
        return eval(scope, code, "eval");
    }

    /**
     * Evaluates the given string
     * @param scope The scope to run in.
     * @param code The code to run.
     * @param source The name of the source.
     */
    public Object eval(Scriptable scope, String code, String source) {
        enter();
        try {
            return getContext().evaluateString(scope, code, source, 1, null);
        } finally {
            exit();
        }
    }

    public Object evalStream(Scriptable scope, InputStream stream) {
        Scanner s = new Scanner(stream);
        String code  = s.useDelimiter("\\Z").next();
        s.close();
        return eval(scope, code);
    }
}
