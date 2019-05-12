package com.miningframework.common.utils;

import org.processmining.contexts.uitopia.UIContext;
import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.framework.connections.ConnectionCannotBeObtained;
import org.processmining.framework.connections.ConnectionID;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.PluginExecutionResult;
import org.processmining.framework.plugin.ProMFuture;
import org.processmining.framework.plugin.Progress;

import java.util.Iterator;

public class FakeProMContext extends UIPluginContext {

    public FakeProMContext() {
        this(MAIN_PLUGINCONTEXT, "Fake Plugin Context");
    }

    private FakeProMContext(UIPluginContext context, String label) {
        super(context, label);
    }

    public FakeProMContext(PluginContext context) {
        this(MAIN_PLUGINCONTEXT, "Fake Plugin Context");
        for (ConnectionID cid : context.getConnectionManager().getConnectionIDs()) {
            try {
                org.processmining.framework.connections.Connection connection = context.getConnectionManager().getConnection(cid);
                addConnection(connection);
            } catch (ConnectionCannotBeObtained connectioncannotbeobtained) { connectioncannotbeobtained.printStackTrace(); }
        }
    }

    public Progress getProgress() {
        return new FakeProgress();
    }

    public ProMFuture getFutureResult(int i) {
        return new ProMFuture(String.class, "Fake Future") {

            @Override
            protected Object doInBackground() throws Exception {
                return null;
            }
        };
    }

    public void setFuture(PluginExecutionResult pluginexecutionresult) {
    }

    private static UIPluginContext MAIN_PLUGINCONTEXT;

    static {
        UIContext MAIN_CONTEXT = new UIContext();
        MAIN_PLUGINCONTEXT = MAIN_CONTEXT.getMainPluginContext().createChildContext("");
    }
}