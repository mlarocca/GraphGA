package org.GCGA.client.algorithm;

import java.util.Stack;
import org.GCGA.client.Chromosome;
import org.GCGA.client.Graphs.Complete_Bipartite_Graph;
import org.GCGA.client.Graphs.Complete_Graph;
import org.GCGA.client.Graphs.CustomGraph;
import org.GCGA.client.GA_Element;
import org.GCGA.client.GUI.AnalysisPanel;
import org.GCGA.client.Graphs.Graph;
import org.GCGA.client.GUI.GraphPanel;
import org.GCGA.client.GraphType;
import org.GCGA.client.Grid;
import org.GCGA.client.GridCoordinates;
import org.GCGA.client.GUI.LoadingBar;
import org.GCGA.client.Graphs.Square_Grid_Graph;
import org.GCGA.client.Graphs.Triangular_Mesh_Graph;
import org.GCGA.client.utility.Utility;
/**
 *
 * @author Owner
 */
public class GeneticAlgorithm extends Basic_GeneticAlgorithm{

    private static GeneticAlgorithm algorithm = null;
    private static GeneticAlgorithm thisAlgorithm = null;

    protected int strlen;

    public static final float DEFAULT_GRIDSIZE_FACTOR = 2;
    public static final double DEFAULT_CROSSOVER_RATIO = 0.55;

    public static final double DEFAULT_MUTATION_1_RATIO = 0.01;

    public static final double DEFAULT_MUTATION_2_RATIO = 0.075;
    public static final double DEFAULT_MUTATION_3_RATIO = 0.05;

    public static final double MAX_MUTATION_RATIO = 0.25;

    private static final double SELECT_BEST_RATIO = 0.7;    //k% times the best fit element is chosen in the round robin procedure
    protected static double crossoverRatio = DEFAULT_CROSSOVER_RATIO;
    
    protected static double mutation_1_Ratio = DEFAULT_MUTATION_1_RATIO;
    protected static double mutation_2_Ratio = DEFAULT_MUTATION_2_RATIO;
    protected static double mutation_3_Ratio = DEFAULT_MUTATION_3_RATIO;

//    protected static GA_Element population[];

    protected static int counter;

    private static Graph G;

    private static Grid GRID;

    protected static LoadingBar progressBar = null;


    protected GeneticAlgorithm() {
        super();
    }

    public static void init(int iterNbr, int popSize, int vNbr, short graphType, float gridSideMultiplier){

       if (algorithm == null)
            thisAlgorithm = algorithm = new GeneticAlgorithm();
       
       switch( graphType ) {
           case GraphType.COMPLETE_GRAPH:
               G = new Complete_Graph(vNbr < 2 ? 2 : vNbr );
               break;
           case GraphType.COMPLETE_BIPARTITE_GRAPH:
               G = new Complete_Bipartite_Graph( vNbr < 2 ? 2 : vNbr );
               break;
           case GraphType.SQUARE_GRID_GRAPH:
               G = new Square_Grid_Graph( vNbr < 2 ? 2 : vNbr );
               break;
           case GraphType.TRIANGULAR_MESH_GRAPH:
               G = new Triangular_Mesh_Graph( vNbr < 2 ? 2 : vNbr );
               break;

          // case GraphType.ERROR:
           default:
               throw new IllegalArgumentException("GRAPH TYPE: " + graphType );
       }
       vNbr = G.verticesNumber();

       algorithm.setIterations(iterNbr < 10 ? 10 : iterNbr);
       algorithm.setPopulationSize(popSize < 1 ? 1 : popSize);

        //Viene usata una griglia di dimensione proporzionale al quadrato del numero dei vertici

        GRID = new Grid(vNbr, gridSideMultiplier);

        algorithm.strlen = vNbr;

    }
    public static void init(int iterNbr, int popSize, CustomGraph graph, float gridSideMultiplier){

       if (algorithm == null)
            thisAlgorithm = algorithm = new GeneticAlgorithm();

       G = graph;

       algorithm.setIterations(iterNbr < 10 ? 10 : iterNbr);
       algorithm.setPopulationSize(popSize < 1 ? 1 : popSize);

//       alert(G.verticesNumber() + " ");
        //Viene usata una griglia di dimensione proporzionale al quadrato del numero dei vertici
        GRID = new Grid(G.verticesNumber(), gridSideMultiplier);

        algorithm.strlen = G.verticesNumber();

    }

    protected static void setInstance(GeneticAlgorithm instance){
        if (instance!=null)
            algorithm = instance;
    }

    public static void resetInstance(){
        algorithm = thisAlgorithm;
    }

    public static GeneticAlgorithm get(){
        if (algorithm==null)
            throw new NullPointerException("The object hasn't been init yet");
        else
            return algorithm;
    }

    @Override
    public void setPopulationSize(int ps){
        super.setPopulationSize(ps);
        population = new GA_Element[populationSize];
    }

    protected int createPopulation(){
        int bestElIndex = 0;
        long bestFitness = Long.MAX_VALUE;
        for (int i=0; i<populationSize; i++ ){
            population[i] = new GA_Element(strlen);
            if (population[i].fitness() < bestFitness){
                bestFitness = population[i].fitness();
                bestElIndex = i;
            }
        }
        return bestElIndex;
    }

    protected GA_Element crossover(GA_Element father1, GA_Element father2) throws IllegalArgumentException{

        if (father1==null || father2==null ){
                throw new IllegalArgumentException();
        }

        int i, l = father1.getChromosome().genesNumber();
        int crossing_point = (int) Math.floor( Math.random() * l );

        //Nuovo cromosoma da riempire (lo crea con geni "vuoti";
        Chromosome tmp = new Chromosome(l, GRID.gridSize(), false);

        //Per comodità memorizza i riferimenti ai cromosomi paterni
        Chromosome f1c , f2c;
                //Con probabilità 1/2 scambia i genitori di posizione, in modo che la prima parte del corredo genetico venga dal secondo.
        if (Math.random() < 0.5){
             f1c = father1.getChromosome();
             f2c = father2.getChromosome();
        }else{
             f2c = father1.getChromosome();
             f1c = father2.getChromosome();
        }

        for (i=0; i<crossing_point; i++){
            tmp.setGene(i, f1c.getGeneValue(i) );
        }
        //Invariante: i mantiene il suo valore: i==crossing_point!

        Stack<Integer> stack = new Stack();
        for (; i<l; i++){
            if (!tmp.conflict(f2c.getGeneValue(i)) )
                tmp.setGene(i, f2c.getGeneValue(i) );
            else
                stack.push(i);
        }
        while (!stack.empty()){
            tmp.setGeneRandom(stack.pop(), GRID.gridSize());
        }

        return new GA_Element(tmp); //Automaticamente marcato come cambiato
/*
 * Effettuare due passi
 * 1) Assegnare le posizioni che non vanno in conflitto con la prima parte, e tenere traccia dei conflitti, senza intervenire
 * 2) Per i conflitti, generare nuove posizioni compatibili con il resto del cromosoma
        for (; i<l; i++){
            tmp.setGene(i, f2c.getGene(i));
        }
        
 * 
 */
    }

    /**
     * Exchanges two positions in the element;
     * @param e - The element to be mutated
     */
    protected void mutation1(GA_Element e){
        int l = e.getChromosome().genesNumber();

        int p2;
        for(int p1=0; p1 < l; p1++){
            //For every gene, with probability eq. to mutation_1_Ratio swaps it with another one randomly chosen
            if (Math.random() >= mutation_1_Ratio )
                continue;
            do {
                p2 = (int) Math.floor( Math.random() * l );
            }while(p2==p1);

//            GeneticAlgorithm.alert(counter + ": " + p1 + " -> " + p2);
            e.markChanged();

            e.getChromosome().exchangeGenes(p1, p2);
        }
    }

    /**
     * Try to move one vertex in the surroundings
     * @param e - The element to be mutated
     */
    protected void mutation2(GA_Element e){
        int l = e.getChromosome().genesNumber();
        short row_shift;
        short col_shift;
        for (int i=0; i<l; i++){
            if ( Math.random() < mutation_2_Ratio ){
                GridCoordinates gc = GRID.gridCoordinates( e.getChromosome().getGeneValue(i) );
                if ( Math.random() < 0.5 ){
                    //col_shift = 0;
                    if (gc.row() == 0){
                        row_shift = 1;
                    }else if (gc.row() == (GRID.sideSize()-1)){
                        row_shift = -1;
                    }else{
                        row_shift = (short)( Math.random() < 0.5 ? -1 : 1 );
                    }
                }else{
                    row_shift = 0;
                }
                if ( row_shift==0 || Math.random() < 0.5 ){
                    //row_shift = 0;
                    if (gc.column() == 0){
                        col_shift = 1;
                    }else if (gc.column() == (GRID.sideSize()-1)){
                        col_shift = -1;
                    }else{
                        col_shift = (short)( Math.random() < 0.5 ? -1 : 1 );
                    }
                }else{
                    col_shift = 0;
                }

                int new_pos = GRID.gridPosition( new GridCoordinates(gc.row() + row_shift, gc.column() + col_shift ) );
                //Controlla che la posizione non sia già occupata;
                if (  !e.getChromosome().conflict( new_pos )  ){
                    e.markChanged();
                    e.getChromosome().setGene(i, new_pos );
                }
            }
        }
    }

    /**
     * Moves mutation_3_Ratio% vertices in a diffent, randomly chosen position
     * @param e - The element to be mutated
     */
    protected void mutation3(GA_Element e){
        int l = e.getChromosome().genesNumber();
        int new_pos;
        for (int i=0; i<l; i++){
            if ( Math.random() < mutation_3_Ratio ){
                e.markChanged();
                do{
                    new_pos = (int) Math.floor( Math.random() * getGrid().gridSize() );
 //                   alert(i+" | "+new_pos);
                }while ( e.getChromosome().conflict(new_pos) );
                e.getChromosome().setGene(i, new_pos);
            }
        }
    }
/*
    protected void selection (Integer index1, Integer index2){
        //selezione a torneo
        index1 = round_robin();
        index2 = round_robin();
        System.out.println(index1 + " " + index2);
    }
*/
    protected int round_robin(){
        int x,y;

        //Sceglie a caso due individui
        x = (int)Math.floor(Math.random() * populationSize);
        y = (int)Math.floor(Math.random() * populationSize);

//        alert( (population[x]==null) + " | " + (population[y]==null)  );
        if ( Math.random() < SELECT_BEST_RATIO ){
            //Sceglie quello con la fitness maggiore

            return population[x].fitness() <= population[y].fitness() ? x : y;
        }else{
            return population[x].fitness() > population[y].fitness() ? x : y;
        }

    }

    @Override
    public void run(){

        if ( progressBar != null){
            progressBar.startLoader(0, iterations);
        }

        counter = 0;
        initAnalysisPanel();
        
        super.run();

        population_sort();
        progressBar.stopLoader();

        AnalysisPanel.get().selectInitialFolder(Utility.DATA_FOLDER_GA_FITNESS);

/*
        AnalysisPanel.get().paintAxes(Utility.AVERAGE_FITNESS_LABEL);
        AnalysisPanel.get().paint();
 *
 */

//DEPRECATED        print_population();

        //population[0].drawGraph();
       /* try{
            Thread.sleep(2000);
        }catch(InterruptedException e){
            
        }*/

    }

    public static GraphPanel draw(GraphPanel gp){
      gp.setGraph((GA_Element)population[0].clone()) ;
      gp.paint();
      return gp;
    }

    public static GraphPanel draw(GraphPanel gp, int which){
      if (which <0 || which > populationSize )
          throw new IndexOutOfBoundsException();
      gp.setGraph(population[which]) ;
      gp.paint();
      return gp;
    }

    public static long getFitness(int element){
        if ( population!=null && element>=0 && element< populationSize && population[element]!=null)
            return population[element].fitness();
        else
            return 0;
    }

    public static long getCrossingNumber(int element){
        if ( population!=null && element>=0 && element< populationSize && population[element]!=null)
            return population[element].crossingNumber();
        else
            return 0;
    }

    public static int populationSize(){
        return populationSize;
    }

    @Override
    protected void iteration(){
        //System.out.println("    ITERATION "+(counter+1));
  //      super.iteration();
        Integer i1=0, i2=0;
        GA_Element[] new_population = new GA_Element[populationSize];
        for (int j=0; j<populationSize; j++){
            if (j==bestElementIndex){
                //ELITISM: best element passes unaltered to next generation
                new_population[j] = (GA_Element)population[j].clone();
                continue;
            }

            if ( Math.random() <  crossoverRatio ){
                //selection(i1, i2);

                i1 = round_robin();
                i2 = round_robin();

                new_population[j] = (GA_Element)crossover(population[i1], population[i2]);

            }else{
                new_population[j] = (GA_Element)population[i1].clone();
            }

//            new_population[j].getChromosome().check();

            mutation1( new_population[j] );
/*
            if ( Math.random() <  mutation_1_Ratio ){
                mutation1( new_population[j] );
            }
*/
//            new_population[j].getChromosome().check();
            mutation2( new_population[j] );

//            new_population[j].getChromosome().check();
            mutation3( new_population[j] );

//            new_population[j].getChromosome().check();
        }

        population_copy(new_population);
        
        //population_sort();

//        trackBestElements(10);

        bestElementIndex = computeStatistics();

        counter++;

        if ( progressBar != null){
            progressBar.advance();
        }

        
//DEPRECATED        print_population();
    }

    protected void population_copy(GA_Element[] np){
        for (int i=0; i<populationSize; i++){
            population[i] = (GA_Element)np[i].clone();
        }
    }


    protected void population_sort(){
        GA_Element tmp;
        for (int i=1; i<populationSize-1; i++){
            for(int j=i; j>=0; j-- ){
                if ( population[j].fitness() > population[j+1].fitness()  ){
                    tmp = population[j];
                    population[j] = population[j+1];
                    population[j+1] = tmp;
                }
            }
        }
    }

    public static void setProgressBar(LoadingBar pB){
        progressBar = pB;
    }

    public GA_Element getBestElement(){
        try{
            return (GA_Element)population[0].clone();
        }catch(Exception e){
            return null;
        }
    }

    public String bestElementToJSON(){
        GA_Element ga_e = this.getBestElement();
        return ga_e.toJSON(getGraph());
    }


    public static void setCrossoverRatio(double cr){
        if (cr>=0 && cr<=1)
             crossoverRatio = cr;
    }


    public static void setMutation_1_Ratio(double m){
        if (m>=0 && m<=MAX_MUTATION_RATIO)
             mutation_1_Ratio = m;
    }

    public static void setMutation_2_Ratio(double m){
        if (m>=0 && m<=MAX_MUTATION_RATIO)
            mutation_2_Ratio = m;
    }

    public static void setMutation_3_Ratio(double m){
        if (m>=0 && m<=MAX_MUTATION_RATIO)
            mutation_3_Ratio = m;
    }

/*
    public boolean check(int element){
        return population[element].getChromosome().check();
    }*/
    
    public static Graph getGraph(){
        return G;
    }

    public static Grid getGrid(){
        return GRID;
    }

    /**
     * Compute: AVG, STD DEVIATION, BEST ELEMENT FITNESS
     * @return best element's index
     */

    protected int computeStatistics(){
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
        AnalysisPanel.get().addValue(Utility.DATA_FOLDER_GA_FITNESS, Utility.DATA_LABEL_BEST_FITNESS,  bestFitness);
        AnalysisPanel.get().addValue(Utility.DATA_FOLDER_GA_FITNESS, Utility.DATA_LABEL_AVERAGE_FITNESS,  avgFitness);
        AnalysisPanel.get().addValue(Utility.DATA_FOLDER_GA_FITNESS, Utility.DATA_LABEL_STDDEV_FITNESS,  stdDev);

        return bestElIndex;
    }

    /**
     * Adds all the categories of monitored data to the Analysis Panel, after having cleared previously added data, categories included
     */
    protected void initAnalysisPanel(){
        AnalysisPanel.get().clearValues();
        AnalysisPanel.get().addFolder(Utility.DATA_FOLDER_GA_FITNESS, "Fitness");
//        AnalysisPanel.get().addFolder(Utility.DATA_FOLDER_GA_ELEMENTS_FITNESS, "Elements Fitness");
    }


    protected void trackBestElements(int howMany){
        howMany = Math.min(howMany, populationSize);
        for (int i=0; i<howMany; i++){
            AnalysisPanel.get().addValue(Utility.DATA_FOLDER_GA_ELEMENTS_FITNESS, Utility.DATA_LABEL_ELEMENT_FITNESS + i,  population[i].fitness());
        }
    }

    protected int findBestElement(){
        int bestElIndex = 0;
        long bestFitness = Long.MAX_VALUE;
        for (int i=0; i<populationSize; i++){
            if ( population[i].fitness() < bestFitness ){
                bestElIndex = i;
                bestFitness = population[i].fitness();
            }
        }
        return bestElIndex;
    }

    
}
