import agents.Agent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        AgentFactory af = new AgentFactory();
        Agent agent = af.getAgentObject("history", null);

        try {
            String response  = processWithTimeout(agent, "text message", 2, TimeUnit.SECONDS);
            if(response == null){
                agent = af.getAgentObject("history", agent);
                response  = processWithTimeout(agent, "text message", 2, TimeUnit.SECONDS);
            }
            if(response == null){
                agent = af.getAgentObject("history", agent);
                response  = processWithTimeout(agent, "text message", 2, TimeUnit.SECONDS);
            }

            if(response == null) {
                System.out.println("NOT ABLE TO PROCESS");
            }else System.out.println("processed successfully");
        }catch (Exception e){
            System.out.println("exception");
        }

    }

    private static String processWithTimeout(Agent agent, String message, long timeout, TimeUnit unit) throws Exception {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<String> future = executor.submit(() -> agent.process(message));

        try {
            return future.get(timeout, unit);
        } catch (TimeoutException e) {
            future.cancel(true);
            System.out.println("agent processing timed out");
            return null;
        } finally {
            executor.shutdownNow();
        }
    }
}
