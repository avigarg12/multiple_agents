package agents;

public class Gpt implements Agent{
    @Override
    public String process(String message) throws InterruptedException {
        Thread.sleep(3000);

        System.out.println("processing via GPT");
        return "";
    }
}
