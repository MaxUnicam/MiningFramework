import { QualityMeasure } from "./qualityMeasure";

export interface AnalysisResult {

    algorithm: string;

    qualityMeasure: QualityMeasure;

    pnmlContent: string;

    xesContent: string;

}