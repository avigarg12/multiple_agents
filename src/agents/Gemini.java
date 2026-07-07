package agents;

public class Gemini implements Agent{
    @Override
    public String process(String message) throws InterruptedException {
//        Thread.sleep(3000);

        System.out.println("processing via agents.Gemini");
        return "";
    }
}
