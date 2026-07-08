import agents.Agent;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        AgentFactory af = new AgentFactory();
        List<Agent> agents = af.getAgents("history");

        FallbackAgentProcessor processor = new FallbackAgentProcessor(2, TimeUnit.SECONDS);

        try {
            String response = processor.process(agents, "text message");
            if(response == null) {
                System.out.println("NOT ABLE TO PROCESS");
            }else System.out.println("processed successfully");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }finally {
            processor.shudown();
        }
    }
}
