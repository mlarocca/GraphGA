/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.GCGA.client.algorithm;

import java.util.ArrayList;
import org.GCGA.client.Graphs.CustomGraph;
import org.GCGA.client.GA_Element;
import org.GCGA.client.GUI.AnalysisPanel;
import org.GCGA.client.utility.DataRecord;
import org.GCGA.client.utility.GAElementTern;
import org.GCGA.client.utility.StringMap;
import org.GCGA.client.utility.Utility;

/**
 *
 * @author Marcello La Rocca
 */
public class AIS extends GeneticAlgorithm{


    protected static int CLONAL_SELECTION_SET_SIZE;

    private static AIS AIS_algorithm = null;

    private static double suppressiveEffectTreshold = 0.25;
    private static double stimulationRatio = 0.1;

    public static final double DEFAULT_MUTATION_2_RATIO = 0.025;
    public static final double DEFAULT_MUTATION_3_RATIO = 0.01;

    private static final String DATA_FOLDER_AIS = "AIS";
        public static final String DATA_LABEL_AIS_AVERAGE_FITNESS = "AIS Fitness Average";
        public static final String DATA_LABEL_AIS_BEST_FITNESS = "AIS Best Element Fitness";
        //public static final String VARIANCE_FITNESS_LABEL = "Fitness Variance";
        public static final String DATA_LABEL_AIS_STDDEV_FITNESS = "AIS Fitness Standard Deviation";

    private static final String DATA_FOLDER_AIS_GA = "AISGA";
        public static final String DATA_LABEL_AIS_GA_AVERAGE_FITNESS = "Fitness Average: AIS % gain";
        public static final String DATA_LABEL_AIS_GA_BEST_FITNESS = "Best Element Fitness: : AIS % gain";
        //public static final String VARIANCE_FITNESS_LABEL = "Fitness Variance";
        public static final String DATA_LABEL_AIS_GA_STDDEV_FITNESS = "Fitness Standard Deviation: : AIS % gain";

    private static final String DATA_FOLDER_NEGATIVE_SELECTION  = "NegativeSelection";
        private static final String DATA_LABEL_NEGATIVE_SELECTION_MEDIAN_DIST =  "Elements Distance Median";
        private static final String DATA_LABEL_NEGATIVE_SELECTION_AVG_DIST =  "Elements Average Distance";
        private static final String DATA_LABEL_NEGATIVE_SELECTION_FSTQ_DIST =  "Elements Distance 1st quartile";
        private static final String DATA_LABEL_NEGATIVE_SELECTION_TRDQ_DIST =  "Elements Distance 3rd quartile";
        private static final String DATA_LABEL_NEGATIVE_SELECTION_DEL_ELEM =  "Replaced elements";
        private static final String DATA_LABEL_NEGATIVE_SELECTION_HYPERMUTATED_ELEM = "Hypermuted elements";

    private static final String DATA_FOLDER_NEGATIVE_SELECTION_COEFF  = "NegativeSelectionCoeff";
        private static final String DATA_LABEL_NEGATIVE_SELECTION_DEL_DIST = "Affected elements AVG distance";

    private static final String DATA_FOLDER_CLONAL_SELECTION = "ClonalSelection";
        public static final String DATA_LABEL_CLONAL_SELECTION_GAIN = "Best Element Fitness % comparison";

    private AIS(){
        super();
        AIS_algorithm = this;
    }

    public static AIS get(){
        return AIS_algorithm==null? new AIS() : AIS_algorithm;
    }

    public static void init(int iterNbr, int popSize, int vNbr, short graphType, float gridSideMultiplier){
        
        if (AIS_algorithm == null){
            AIS_algorithm = new AIS();
        }
        GeneticAlgorithm.setInstance(AIS_algorithm);
        GeneticAlgorithm.init(iterNbr, popSize, vNbr, graphType, gridSideMultiplier);
    }

    public static void init(int iterNbr, int popSize, CustomGraph graph, float gridSideMultiplier){

        if (AIS_algorithm == null){
            AIS_algorithm = new AIS();     
        }
        GeneticAlgorithm.setInstance(AIS_algorithm);
        GeneticAlgorithm.init(iterNbr, popSize, graph, gridSideMultiplier);
    }

    @Override
    public void run(){
        if ( progressBar != null){
            progressBar.startLoader(0, iterations);

        }
        counter = 0;
        initAnalysisPanel();

        bestElementIndex = super.createPopulation();

        for (int i=0; i<iterations; i++){
             this.iteration();
        }

        population_sort();

        AnalysisPanel.get().selectInitialFolder(DATA_FOLDER_AIS);
/*
        AnalysisPanel.get().paintAxes(Utility.AVERAGE_FITNESS_LABEL);
        AnalysisPanel.get().paint();
*/
        //progressBar.stopLoader();
        //GeneticAlgorithm.setInstance(algorithm);

    }


    /**
     * Adds all the categories of monitored data to the Analysis Panel, after having cleared previously added data, categories included
     */
    @Override
    protected void initAnalysisPanel(){
        super.initAnalysisPanel();
        AnalysisPanel.get().addFolder(DATA_FOLDER_AIS, "Fitness after AIS");
        AnalysisPanel.get().addFolder(DATA_FOLDER_AIS_GA, "AIS vs GA comparison");
        AnalysisPanel.get().addFolder(DATA_FOLDER_CLONAL_SELECTION, "Clonal selection");
        AnalysisPanel.get().addFolder(DATA_FOLDER_NEGATIVE_SELECTION, "Negative selection");
        AnalysisPanel.get().addFolder(DATA_FOLDER_NEGATIVE_SELECTION_COEFF, "Negative selection Coefficients");
    }

    @Override
    protected void iteration(){

        super.iteration();

        clonalSelection();

        //idiotypic network
        negativeSelection();

        computeAISStatistics();

    }

    private void clonalSelection(){

        //int bestElementIndex = findBestElement();
        GA_Element best = population[bestElementIndex];
 CLONAL_SELECTION_SET_SIZE = strlen;

        long bestFitness = best.fitness();

        if (bestFitness == 0){
            //Can't improve, fitness is already minimal!
            AnalysisPanel.get().addValue(DATA_FOLDER_CLONAL_SELECTION, DATA_LABEL_CLONAL_SELECTION_GAIN, 0 );
            return;
        }


        double tmpFitness = bestFitness;

        GA_Element[] clonalSelectionSet = new GA_Element[CLONAL_SELECTION_SET_SIZE];
        for (int i = 0; i<CLONAL_SELECTION_SET_SIZE; i++)
            clonalSelectionSet[i] = (GA_Element)best.clone();

        for ( GA_Element elem : clonalSelectionSet){
            hypermutation(elem);

            if ( elem.fitness() < bestFitness ){
                best = elem;
                bestFitness = elem.fitness();
            }
        }

        population[bestElementIndex] = best;

        AnalysisPanel.get().addValue(DATA_FOLDER_CLONAL_SELECTION, DATA_LABEL_CLONAL_SELECTION_GAIN, bestFitness/tmpFitness * 100 );

        //DELETE ROUTINE
        for (int i = 0; i<CLONAL_SELECTION_SET_SIZE; i++)
            clonalSelectionSet[i] = null;
        clonalSelectionSet = null;
    }

    private void hypermutation(GA_Element elem){
        mutation1(elem);
        mutation2(elem);
        mutation3(elem);
    }

    private void negativeSelection(){

        int i,j,k;
        StringMap concentration = new StringMap();

//        int[] multeplicity = new int[populationSize];
        double avgDist = 0;
        double[][] dist = new double[populationSize][populationSize];
//        double[] dx = new double[populationSize];

        DataRecord fitness = new DataRecord("fitness");

        DataRecord distances = new DataRecord("dist");
        
        GAElementTern exist;
        for ( i=0; i<populationSize; i++ ){
            fitness.addData( (double)population[i].fitness() );

            //Calcola la molteplicità di ogni elemento
            //GeneticAlgorithm.alert(population[i].toString());
            exist = concentration.add(population[i].toString(), i );
            if ( exist != null){
                //Element already in the table - no need to recompute distance, just copy it from the first occurrence;

                k = exist.getFirstOccurrence();
                for (j=i+1; j<populationSize; j++){
                    dist[i][j] = dist[j][i] = dist[k][j];   //Copia i valori dagli elementi già calcolati
                    distances.addData(dist[i][j]);
                    avgDist += dist[i][j];
                }
            }else{
                //Compute distance gene_by_gene, i.e. for each vertex in the graph between two elements
                for (j=i+1; j<populationSize; j++){
                    dist[i][j] = dist[j][i] = population[i].computeDistance(population[j]);
                    distances.addData(dist[i][j]);
                    avgDist += dist[i][j];
                }
            }
        }

        avgDist /= populationSize * (populationSize -1) / 2 ;

//        avgFitness /= populationSize;

        AnalysisPanel.get().addValue(DATA_FOLDER_NEGATIVE_SELECTION,DATA_LABEL_NEGATIVE_SELECTION_MEDIAN_DIST,  distances.getMedian());
        AnalysisPanel.get().addValue(DATA_FOLDER_NEGATIVE_SELECTION,DATA_LABEL_NEGATIVE_SELECTION_FSTQ_DIST,  distances.getFirstQuartile());
        AnalysisPanel.get().addValue(DATA_FOLDER_NEGATIVE_SELECTION,DATA_LABEL_NEGATIVE_SELECTION_TRDQ_DIST,  distances.getThirdQuartile());


            /** Decides whether to suppress the element or not
             *  1) Higher distance from other elements improve survival chances
             *  2) High concentration of the antybody lower survival chances
             *  3) Low fitness improves survival chances (proportionally to stimulationRatio)
             *  4) If the delta (dx) minus a random factor is higher than the suppressiveTreshold, then the element survives
             *      [ This means that if dx[i] > 1 + suppressiveEffectTreshold the element will certainly survive ]
             */
//        int c=0;
        double avgDelDist = 0;
        ArrayList<Integer> indicesToBeDeleted = new ArrayList();
        ArrayList<Integer> indicesToBeHyperMutated = new ArrayList();
        int c_i, c_j, c_k;

        for (i = 0; i< populationSize-1; i++){
            if (!indicesToBeDeleted.contains(i)){

                c_i = concentration.getCounter(population[i].toString());

                for (j=i+1; j < populationSize; j++){
                    if ( !indicesToBeDeleted.contains(j) 
                          && dist[i][j] < suppressiveEffectTreshold * distances.getFirstQuartile()  ){

                        c_j = concentration.getCounter(population[j].toString() );
                        //Selects the element to be deleted (prefers the less represented
                        if ( i==bestElementIndex ){
                            k = j;
                            c_k = c_j;
                            continue;
                        }else if ( j==bestElementIndex ){
                            k = i;
                            c_k = c_i;
                            continue;
                        }
                        if ( population[i].fitness() > population[j].fitness() ){
                            k = i;
                            c_k = c_i;
                        }else if ( population[j].fitness() > population[i].fitness() ){
                            k = j;
                            c_k = c_j;
                        }else{
                            if ( Math.random() < 0.5 ){
                                k = i;
                                c_k = c_i;
                            }
                            else{
                                k = j;
                                c_k = c_j;
                            }
                        }

                        if ( population[k].fitness()/ fitness.getFirstQuartile() <= stimulationRatio )
                            //Saves best elements
                            continue;
                        else{
                            if (c_k > 1){
                                concentration.remove(population[k].toString());
                            }
                            else
                                indicesToBeHyperMutated.add(k);

                            indicesToBeDeleted.add(k);  //Adds the element here anywhere to check later if it's already been "doomed"
                            avgDelDist += dist[i][j];
                           // if (k==i){
                                //IMPORTANT: This way the conditions to get the next iteration of the main FOR are met.
                                        //This means that an element can determine just one deletion
                                j = populationSize;
                                continue;
                           // }
                        }
                    }
                }
            }
        }
        for ( Integer index: indicesToBeHyperMutated ){
            /*
            if (index == bestElementIndex )
                GeneticAlgorithm.alert("HyperM Errore"); */
           ///population[index] = new GA_Element(strlen);
            //HYPERMUTATION
            indicesToBeDeleted.remove(index);
            hypermutation(population[index]);
        }
        for ( Integer index: indicesToBeDeleted ){
           population[index] = new GA_Element(strlen);
        }

    if (indicesToBeDeleted.size()>0)
        avgDelDist/=indicesToBeDeleted.size();

        AnalysisPanel.get().addValue(DATA_FOLDER_NEGATIVE_SELECTION, DATA_LABEL_NEGATIVE_SELECTION_DEL_ELEM, indicesToBeDeleted.size());
        AnalysisPanel.get().addValue(DATA_FOLDER_NEGATIVE_SELECTION, DATA_LABEL_NEGATIVE_SELECTION_HYPERMUTATED_ELEM, indicesToBeHyperMutated.size());
        AnalysisPanel.get().addValue(DATA_FOLDER_NEGATIVE_SELECTION_COEFF, DATA_LABEL_NEGATIVE_SELECTION_DEL_ELEM, indicesToBeDeleted.size());
//        AnalysisPanel.get().addValue(DATA_FOLDER_NEGATIVE_SELECTION_COEFF, "AVG DX", avgDX);
        AnalysisPanel.get().addValue(DATA_FOLDER_NEGATIVE_SELECTION_COEFF,DATA_LABEL_NEGATIVE_SELECTION_AVG_DIST,  avgDist);
        AnalysisPanel.get().addValue(DATA_FOLDER_NEGATIVE_SELECTION_COEFF, DATA_LABEL_NEGATIVE_SELECTION_DEL_DIST, avgDelDist);
        
        //DELETE ROUTINE
        indicesToBeDeleted.clear();
        indicesToBeHyperMutated.clear();
        concentration.clear();
        concentration = null;
//        dx = null;
        dist = null;
    }

    /**
     * Compute: AVG, STD DEVIATION, BEST ELEMENT FITNESS
     * @return best element's index
     */

    protected int computeAISStatistics(){
        int bestElIndex = 0;
        double avgFitness = 0, bestFitness = Double.MAX_VALUE, stdDev = 0;
        for (int i=0; i<populationSize; i++){

            avgFitness += population[i].fitness();
            stdDev += Math.pow( population[i].fitness() , 2);

            if ( population[i].fitness() < bestFitness ){
                bestElIndex = i;
                bestFitness = population[i].fitness();
            }
        }

        stdDev/=(double)populationSize;

        avgFitness /= (double)populationSize;
        stdDev = Math.sqrt(stdDev - Math.pow(avgFitness, 2));

 //       GeneticAlgorithm.alert(avgDist + " | " + avgFitness );
        AnalysisPanel.get().addValue(DATA_FOLDER_AIS,DATA_LABEL_AIS_BEST_FITNESS,  bestFitness);
        AnalysisPanel.get().addValue(DATA_FOLDER_AIS,DATA_LABEL_AIS_AVERAGE_FITNESS,  avgFitness);
        AnalysisPanel.get().addValue(DATA_FOLDER_AIS,DATA_LABEL_AIS_STDDEV_FITNESS,  stdDev);

        double tmp;

        tmp = AnalysisPanel.get().getLastValue(Utility.DATA_FOLDER_GA_FITNESS, Utility.DATA_LABEL_BEST_FITNESS);
 //       GeneticAlgorithm.alert(DATA_FOLDER_AIS+ "  "+Utility.DATA_LABEL_BEST_FITNESS+"  | "+tmp);
        if (tmp>0)
            tmp = (tmp-bestFitness) / tmp * 100;
        AnalysisPanel.get().addValue(DATA_FOLDER_AIS_GA, DATA_LABEL_AIS_GA_BEST_FITNESS,  tmp);

        tmp = AnalysisPanel.get().getLastValue(Utility.DATA_FOLDER_GA_FITNESS, Utility.DATA_LABEL_AVERAGE_FITNESS);
        if (tmp>0)
            tmp = (tmp-avgFitness) / tmp *100;
        AnalysisPanel.get().addValue(DATA_FOLDER_AIS_GA, DATA_LABEL_AIS_GA_AVERAGE_FITNESS,  tmp);

        tmp = AnalysisPanel.get().getLastValue(Utility.DATA_FOLDER_GA_FITNESS, Utility.DATA_LABEL_STDDEV_FITNESS);
        if (tmp>0)
            tmp = (tmp-stdDev) / tmp *100;
        AnalysisPanel.get().addValue(DATA_FOLDER_AIS_GA, DATA_LABEL_AIS_GA_STDDEV_FITNESS,  tmp);

        return bestElIndex;
    }
}
