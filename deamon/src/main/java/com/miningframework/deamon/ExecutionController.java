package com.miningframework.deamon;

import com.miningframework.common.models.PetriNet;
import com.miningframework.common.models.QualityMeasure;
import com.miningframework.common.services.ProMWrapper;
import com.miningframework.common.settings.ApplicationSettings;
import com.miningframework.common.utils.DiscoveryAlgorithm;
import org.deckfour.xes.model.XLog;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;


class ExecutionController {

    private ProMWrapper prom;

    private ExecutorService executor;

    private ApplicationSettings settings;


    ExecutionController(ProMWrapper prom, ApplicationSettings settings) {
        this.prom = prom;
        this.settings = settings;
        this.executor = Executors.newFixedThreadPool(1);
        System.out.println("Timeout is " + settings.timeoutMilliseconds + " milliseconds");
    }


    PetriNet mine(XLog log, DiscoveryAlgorithm algorithm) throws Exception {
        if (log == null || algorithm == null)
            throw new Exception("Log or algorithm are null");

        if (this.prom == null)
            throw new Exception();

        Future<PetriNet> future = executor.submit(() -> {
            return this.prom.mine(log, algorithm);
        });

        return future.get(settings.timeoutMilliseconds, TimeUnit.MILLISECONDS);
    }


    QualityMeasure getQualityMeasure(XLog log, PetriNet petriNet) throws Exception {
        if (log == null || petriNet == null)
            throw new Exception("Log or petri net are null");

        if (this.prom == null)
            throw new Exception();

        Future<QualityMeasure> future = executor.submit(() -> {
            return this.prom.getQualityMeasure(log, petriNet);
        });

        return future.get(settings.timeoutMilliseconds, TimeUnit.MILLISECONDS);
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
