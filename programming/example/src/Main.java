public class Main {

    public static void main(String[] args){
        ClassicSingleton a = ClassicSingleton.getInstance();
        ClassicSingleton b = ClassicSingleton.getInstance();
        System.out.println(a.equals(b));
    }
}
