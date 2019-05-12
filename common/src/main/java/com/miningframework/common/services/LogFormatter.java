package com.miningframework.common.services;

import com.miningframework.common.models.MethodABI;
import com.miningframework.common.models.Task;
import com.miningframework.common.models.Trace;
import com.miningframework.common.models.Transaction;
import com.miningframework.common.utils.StringUtils;

import java.text.SimpleDateFormat;
import java.util.*;


public class LogFormatter {

    public String format(Map<String, List<Transaction>> data, List<MethodABI> contractABI) {
        List<String> validAbis = new ArrayList<>();
        validAbis.add("function");
        validAbis.add("constructor");
        validAbis.add("event");
        validAbis.add("modifier");

        List<Trace> traces = new ArrayList<>();
        for (String key : data.keySet()) {
            Trace trace = new Trace();
            trace.id = key;
            trace.tasks = new ArrayList<>();

            List<Transaction> transactions = data.get(key);
            for (Transaction item : transactions) {
                if (StringUtils.isNullOrEmpty(item.input) || item.input.length() <= 10)
                    continue;

                Task task = new Task();
                task.description = item.input.substring(0, 10);
                task.timestamp = new Date(item.timestamp);

                if (contractABI != null) {
                    contractABI.removeIf(p -> !validAbis.contains(p.type.toLowerCase()));
                    for (MethodABI method : contractABI) {
                        String hashedName = method.hashedName();
                        if (hashedName.length() >= 10 && hashedName.substring(0, 10).toLowerCase().equals(task.description.toLowerCase())) {
                            task.description = method.name;
                        }
                    }
                }

                trace.tasks.add(task);
            }

            traces.add(trace);
        }

        StringBuilder builder = new StringBuilder();
        builder.append(String.format("traceid,event,timestamp%s", System.lineSeparator()));
        for (Trace trace : traces) {
            for (Task task : trace.tasks) {
                SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/YYYY hh:mm:ss");
                String dateString = dateFormatter.format(task.timestamp);
//                String row = String.format("%s,%s,%s%s", trace.id, task.description, task.timestamp.getTime(), System.lineSeparator());
                String row = String.format("%s,%s,%s%s", trace.id, task.description, dateString, System.lineSeparator());
                builder.append(row);
            }
        }

        return builder.toString();
    }

}