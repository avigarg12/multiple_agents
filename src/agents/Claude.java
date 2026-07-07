package agents;

public class Claude implements Agent{
    @Override
    public String process(String message) {
        System.out.println("processing via agents.Claude");
        return "";
    }
}
