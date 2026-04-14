package org.ivanprokhorevich;

public class Main {
    static long[] w = new long[17];
    static float[] x = new float[15];
    static double[][] w1 = new double[17][15];

    public static boolean checkSecondStatement(long wl){
        int[] check_nums = {4, 6, 7, 9, 15, 16, 17, 18};
        for(int i=0; i<check_nums.length; i++){
            if(wl==check_nums[i]){
                return true;
            }
        }
        return false;
    }


    public static double getNewW1Elem(float xl, long wl){
        if(wl==19){
            return Math.pow(0.5*Math.atan((xl+0.5)/5.0), 3);
        }
        else if(checkSecondStatement(wl)){
            return Math.pow(
                    Math.exp(Math.pow(xl, (1/3.0)))*(0.5+Math.tan(Math.pow(xl, (1/3.0)))),
                    Math.pow(
                            Math.exp(xl),
                            Math.exp(xl)-1
                    )
            );
        }
        else{
            return Math.atan(Math.cos(Math.pow(((1/2.0))*Math.sin(Math.pow(3*xl, 2)), 2) ));
        }
    }

    public static void outputResult(double[][] wl){
        for(int i=0; i<wl.length; i++) {
            for (int j = 0; j < wl[i].length; j++) {
                if(j!=wl[i].length-1) System.out.printf("%9.2e ", wl[i][j]);
                else System.out.printf("%9.2e", wl[i][j]);
            }
            if(wl.length-1!=i) System.out.println();
        }
    }

    public static void main(String[] args) {
        for(int i=4; i<=20; i++) {
            w[i-4] = i;
        }
        for(int i=0; i<15; i++) {
            x[i] = (-2.0f) + (float)(Math.random()*(5.0f));
        }
        for(int i=0; i<17; i++){
            for (int j=0; j<15; j++){
                w1[i][j] = getNewW1Elem(x[j], w[i]);
            }
        }
        outputResult(w1);
    }
}