package org.ivanprokhorevich;

import java.lang.Math;

public class Main2 {
    static int[] w = {1, 3, 5, 7, 9, 11, 13, 15, 17, 19};
    static float[] x = new float[17];
    static double[][] s = new double[10][17];

    static void printMatrix(double[][] arr){
        for(double[] sy:arr){
            for(double sx: sy){
                System.out.printf(" %9.2e ", sx);
            }
            System.out.println();
        }
    }
    static double calculateNewMatrixElement(float xj, int w){
        if (w==3) {
            return Math.cos(
                    Math.log(
                            Math.acos((xj + 1.5) / 11)
                    )
            );
        }
        else if(w==1 || w==5 || w == 13 || w==15 || w==17){
            return Math.atan(Math.sin(Math.sin(Math.sin(xj))));
        }else{
            return
                    Math.pow(
                            (
                                    Math.pow(Math.pow((2*Math.atan((xj+1.5)/11)), 2),
                            (0.25/(Math.pow((0.25-Math.pow(xj, 4*xj)), Math.pow(xj, xj)))))
                            )/2,
                            2);
        }
    }


    public static void main(String [] args){
        for(int i=0; i<17; i++) {
            x[i] = -4.0f + (float) Math.random()*(7.0f+4.0f);
        }

        for(int i=0; i<10; i++){
            for(int j=0; j<17; j++){
                s[i][j] = calculateNewMatrixElement(x[j], w[i]);
            }
        }

        printMatrix(s);
    }
}