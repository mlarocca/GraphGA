
package org.GCGA.client.algorithm;

import org.GCGA.client.GA_Element;

/**
 *
 * @author Owner
 */
public abstract class Basic_GeneticAlgorithm {
    protected int iterations = 100;

    protected static int populationSize = 100;
    protected static double crossoverRatio;
    protected static double  mutation_1_Ratio;
    protected static GA_Element[] population = null;

    protected int bestElementIndex;

    public static void setCrossoverRatio(double cr){
        if (cr < 0){
            cr = 0;
        }else{
            if (cr > 1){
                cr=1;
            }
        }
        crossoverRatio = cr;
    }

    public static void setMutation_1_Ratio(double mr){
        if (mr < 0){
            mr = 0;
        }else{
            if (mr > 1){
                mr=1;
            }
        }
        mutation_1_Ratio = mr;
    }

    public void setPopulationSize(int ps){

        populationSize = ps < 1 ? 1 : ps;
    }

    public void setIterations(int n){
        if (n<1){
            n = 1;
        }
        iterations = n;
    }

    public void run(){

        bestElementIndex = createPopulation();

        /*
        i=0;
        timer = new Timer() {

            @Override
            public void run() {
                if (++i>iterations)
                    timer.cancel();
                else{
                    iteration();
                }
            }
        };
        timer.scheduleRepeating(100);
         *
         */

        for (int i=0; i<iterations; i++){
            this.iteration();
        }

    }

    protected abstract void iteration();


    protected abstract int createPopulation();


}
