import { Pipe, PipeTransform } from "@angular/core";
import { DiscoveryAlgorithm } from "../shared/models/discoveryAlgorithm";

@Pipe({ name: 'discoveryAlgorithm' })
export class DisoveryAlgorithm implements PipeTransform {

    transform(algorithm: DiscoveryAlgorithm): string {
        switch(algorithm) {
            case DiscoveryAlgorithm.ETM:
                return "Evolutionary Tree Miner";
            case DiscoveryAlgorithm.HeuristicMiner:
                return "Heuristic Miner";
            case DiscoveryAlgorithm.InductiveMiner:
            default:
                return "Inductive Miner";
        }
    }

}