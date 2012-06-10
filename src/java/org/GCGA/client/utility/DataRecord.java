/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.GCGA.client.utility;

import java.util.ArrayList;

/**
 *
 * @author Eurialo
 */
public class DataRecord {
    private String name = null;
    private final ArrayList<Double> data = new ArrayList<Double>();
    private double min, max, avg=0;
    private boolean show = true;

    private double median, first_quartile, third_quartile;
    private boolean validMedian = false;


    public DataRecord(String dataField) {
        name = dataField;
    }

    public boolean isNull(){
        return name==null;
    }

    public String getDataField(){
        return name;
    }

    public double getMin() throws NullPointerException{
        return min;
    }

    public double getMax() throws NullPointerException{
        return max;
    }

    public double getAvg() throws NullPointerException{
        return avg;
    }

    public ArrayList<Double> getData() throws NullPointerException{
        return data;
    }

    public void addData(Double d ){
        validMedian = false;

        data.add(d);
        int n = data.size();
        if (n == 1){
            max = min = avg = d;
        }else{
            if (d < min)
                min = d;
            else if (d > max )
                max = d;
            avg = (avg *(n-1)+d) / n;
        }
    }

    public boolean equals(DataRecord other){
        return this.name.equals(other.name);
    }

    public boolean equals(String otherName){
        return this.name.equals(otherName);
    }

    public void hideDataRecord(){
        show = false;
    }

    public void showDataRecord(){
        show = true;
    }

    public boolean isVisible(){
        return show;
    }

    public double getMedian(){
        if (!validMedian)
            computeQuartiles();

        return median;
    }

    public double getFirstQuartile(){
        if (!validMedian)
            computeQuartiles();

        return first_quartile;
    }

    public double getThirdQuartile(){
        if (!validMedian)
            computeQuartiles();

        return third_quartile;
    }

    /**
     * Computes median, first quartile and third quartile;
     */
    private void computeQuartiles(){
        int n = data.size();
        Double[] tmp = new Double[n];
        data.toArray(tmp);

        quicksort(tmp, 0, n-1);

         if (n%4==0){
            first_quartile = tmp[n/4-1];
            third_quartile = tmp[n/4*3-1];
            median = tmp[n/2-1];
        }
        else if (n%2==0){
            median = tmp[n/2-1];
            first_quartile = (tmp[(n/2-1)/2-1] + tmp[(n/2+1)/2-1])/2;
            third_quartile = (tmp[((n/2-1)/2)*3-1] + tmp[((n/2+1)/2)*3-1])/2;
        }
        else{
            median = (tmp[(n-1)/2-1]+tmp[(n+1)/2-1])/2;
            first_quartile = tmp[(int)Math.round(n/4.0)-1];
            third_quartile = tmp[(int)Math.round(n/4.0*3)-1];
        }
        validMedian = true;
    }

    private void quicksort(Double A[], int l, int r){

            int p;

            if (A==null || l<0 || r<0 || l>=r)
                    return ;

            if ( l < r-1 ){
                    p = partition(A, l, r );
                    quicksort(A,l, p-1);
                    quicksort(A, p+1, r);
            }
            else if (l==r-1){
                    if (A[l]>A[r]){
                            Double tmp = A[r];
                            A[r] = A[l];
                            A[l] = tmp;
                    }
            }
    }


    private int partition(Double A[], int l, int r){

            Double tmp;

            int pivot_pos = l + (int)Math.floor(Math.random() * (r-l+1));

            Double pivot;
            pivot = A[l];
            A[l] = A[pivot_pos];
            A[pivot_pos] = pivot;
            pivot_pos = l++;

            while (l<r){
                    while ( A[r] > pivot )
                            r--;

                    while ( l<r && A[l] <= pivot )
                            l++;

                    if ( l < r){
                            tmp = A[r];
                            A[r--] = A[l];
                            A[l++] = tmp;
                    }
            }
            if (A[r]>pivot )
                    r--;

            A[pivot_pos] = A[r];
            A[r] = pivot;

            return r;
    }

    public double getLastValue() throws IllegalArgumentException{
        if ( data.size() <= 0 )
            throw  new IllegalArgumentException();
        return data.get(data.size()-1);
    }
}