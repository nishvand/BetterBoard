package nishvand.Misc;

public abstract class InfiniteLoopThread extends Thread{
    abstract public void task() throws Exception;

    @Override
    public void run() {
        while (true)
            try {
                task();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
    }
}
