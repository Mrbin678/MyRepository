package future;

/**
 * run方法中无法抛出checked Exception
 */
public class RunnableCantCheckException {

    public void add() throws Exception { // 普通方法抛出异常

    }

    public static void main(String[] args) {
        Runnable runnable = () -> {
            try {
                throw new Exception(); // 只能进行try/catch,无法向外抛出
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
    }

}
