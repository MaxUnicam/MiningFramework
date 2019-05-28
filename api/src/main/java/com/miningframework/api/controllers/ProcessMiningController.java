package com.miningframework.api.controllers;

import com.google.gson.Gson;
import com.miningframework.api.models.AnalysisResult;
import com.miningframework.common.models.PetriNet;
import com.miningframework.common.services.LogBuilder;
import com.miningframework.common.services.ProMWrapper;
import com.miningframework.common.settings.ApplicationSettings;
import com.miningframework.common.utils.DiscoveryAlgorithm;
import com.miningframework.common.utils.FakeProMContext;
import com.miningframework.common.utils.StringUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.out.XesXmlSerializer;
import org.processmining.plugins.pnml.exporting.PnmlExportNetToPNML;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

@RestController
public class ProcessMiningController {

    private static final String INDUCTIVE_MINER = "inductive";
    private static final String HEURISTIC_MINER = "heuristic";

    private ProMWrapper promWrapper = new ProMWrapper();

    private LogBuilder logBuilder = new LogBuilder();

    private Gson gson = new Gson();


    @CrossOrigin
    @RequestMapping("api/v1/ping")
    public ResponseEntity ping() throws Exception {
        return ResponseEntity.ok("pong");
    }


    @CrossOrigin
    @RequestMapping("api/v1/processmining/analyze")
    public ResponseEntity mine(
            @RequestParam(value="contract") String contract,
            @RequestParam(value="algorithm", defaultValue=INDUCTIVE_MINER) String algorithm,
            @RequestParam(value="returnXes", defaultValue="false") boolean returnXes,
            @RequestParam(value="returnPnml", defaultValue="false") boolean returnPnml) throws Exception {

        if (StringUtils.isNullOrEmpty(contract)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Contract is required");
        }

        AnalysisResult result = new AnalysisResult();
        result.algorithm = parseAlgorithm(algorithm);

        String path = logBuilder.build(contract);
        if (StringUtils.isNullOrEmpty(path)) {
            String message = "Can't create a log from specified contract";
            System.out.println(message);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(message);
        }

        XLog log = promWrapper.convertCsvToXes(path);
        if (returnXes) {
            ByteArrayOutputStream xesFile = new ByteArrayOutputStream();
//            new XesXmlGZIPSerializer().serialize(log, xesFile);
            new XesXmlSerializer().serialize(log, xesFile);
            byte[] encoded = Base64.encodeBase64(xesFile.toByteArray());
            result.xesContent = new String(encoded, StandardCharsets.US_ASCII);
        }

        PetriNet petrinet = promWrapper.mine(log, result.algorithm);
        result.qualityMeasure = promWrapper.getQualityMeasure(log, petrinet);

        if (returnPnml) {
            String pnmlPath = ApplicationSettings.instance().outputLocation + "tmp.pnml";
            File tmp = new File(pnmlPath);
            PnmlExportNetToPNML exporterPNML = new PnmlExportNetToPNML();
            exporterPNML.exportPetriNetToPNMLFile(new FakeProMContext(), petrinet.net, tmp);
            byte[] pnmlContent = Files.readAllBytes(tmp.toPath());
            byte[] pnmlEncoded = Base64.encodeBase64(pnmlContent);
            result.pnmlContent = new String(pnmlEncoded, StandardCharsets.US_ASCII);
            tmp.delete();
        }

        return ResponseEntity.ok(gson.toJson(result));
    }


    private DiscoveryAlgorithm parseAlgorithm(String algorithm) {
        String normalizedAlgorithm = algorithm.toLowerCase().trim();
        if (normalizedAlgorithm.equals(HEURISTIC_MINER)) {
            return DiscoveryAlgorithm.HeuristicMiner;
        } else {
            return DiscoveryAlgorithm.InductiveMiner;
        }
    }

}