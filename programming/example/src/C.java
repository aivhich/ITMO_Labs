public interface C {
    private void f(){

    }
    default void func() {
        System.out.println("hello from c");
    }

    static void func2(){
        System.out.println("hello");
    }

    default void openMethod(){
        f();
    }
}
