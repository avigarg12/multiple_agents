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

    public String process(List<Agent> agents, String message) {
        for(Agent agent: agents) {
            String response = processWithTimeout(agent,message);
            if(response != null) return response;
        }
        return null;
    }

    public void shutdown() {
        executor.shutdownNow();
    }

    private String processWithTimeout(Agent agent, String message) {
        Future<String> future = executor.submit(() -> agent.process(message));

        try {
            return future.get(timeout, unit);
        } catch (TimeoutException e) {
            future.cancel(true);
            System.out.printf("agent processing timed out: %s \n", agent.getClass());
            return null;
        }catch (ExecutionException e) {
            System.out.printf("agent processing failed: %s \n", agent.getClass().getSimpleName());
            return null;
        }catch (InterruptedException e) {
            future.cancel(true);
            Thread.currentThread().interrupt();
            System.out.printf("agent processing interrupted: %s \n", agent.getClass().getSimpleName());
            return null;
        }
    }
}
