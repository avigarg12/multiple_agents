import agents.Agent;

import java.util.List;
import java.util.concurrent.*;

public class FallbackAgentProcessor {
    private final long timeout;
    private final TimeUnit unit;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public FallbackAgentProcessor(long timeout, TimeUnit unit) {
        this.timeout = timeout;
        this.unit = unit;
    }

    public String process(List<Agent> agents, String message) throws Exception {
        for(Agent agent: agents) {
            String response = processWithTimeout(agent,message);
            if(response != null) return response;
        }
        return null;
    }

    public void shudown() {
        executor.shutdownNow();
    }

    private String processWithTimeout(Agent agent, String message) throws Exception {
        Future<String> future = executor.submit(() -> agent.process(message));

        try {
            return future.get(timeout, unit);
        } catch (TimeoutException e) {
            future.cancel(true);
            System.out.printf("agent processing timed out: %s \n", agent.getClass());
            return null;
        }
    }
}
