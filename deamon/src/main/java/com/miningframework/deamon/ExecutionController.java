package com.miningframework.deamon;

import com.miningframework.common.models.PetriNet;
import com.miningframework.common.models.QualityMeasure;
import com.miningframework.common.services.ProMWrapper;
import com.miningframework.common.utils.DiscoveryAlgorithm;
import org.deckfour.xes.model.XLog;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;


class ExecutionController {

    private ProMWrapper prom;

    private ExecutorService executor;

    private long timeoutMinutes = 10;


    ExecutionController(ProMWrapper prom) {
        this.prom = prom;
        this.executor = Executors.newFixedThreadPool(1);
    }


    PetriNet mine(XLog log, DiscoveryAlgorithm algorithm) throws Exception {
        if (log == null || algorithm == null)
            throw new Exception("Log or algorithm are null");

        if (this.prom == null)
            throw new Exception();

        Future<PetriNet> future = executor.submit(() -> {
            return this.prom.mine(log, algorithm);
        });

        return future.get(timeoutMinutes, TimeUnit.MINUTES);
    }


    QualityMeasure getQualityMeasure(XLog log, PetriNet petriNet) throws Exception {
        if (log == null || petriNet == null)
            throw new Exception("Log or petri net are null");

        if (this.prom == null)
            throw new Exception();

        Future<QualityMeasure> future = executor.submit(() -> {
            return this.prom.getQualityMeasure(log, petriNet);
        });

        return future.get(timeoutMinutes, TimeUnit.MINUTES);
    }


    void deinitialize() {
        this.executor.shutdown();
    }


    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        if (!this.executor.isShutdown())
            this.executor.shutdownNow();
    }
}
