package agents;

public class Claude implements Agent{
    @Override
    public String process(String message) throws InterruptedException {
        Thread.sleep(3000);

        System.out.println("processing via main.java.agents.Claude");
        return "";
    }
}
