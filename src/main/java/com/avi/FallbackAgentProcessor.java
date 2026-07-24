package com.avi;

import agents.Agent;

import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.*;

@Component // registers this processor as a Spring Bean
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

    @PreDestroy // Tells Spring to run this method when shutting down the web server
    public void shutdown() {
        executor.shutdownNow();
    }

    private String processWithTimeout(Agent agent, String message) {
        Future<String> future = executor.submit(() -> agent.process(message));

        try {
            return future.get(timeout, unit);
        } catch (TimeoutException e) {
            future.cancel(true);
            System.out.printf("agent processing timed out: %s \n", agent.getClass().getSimpleName());
            return null;
        }catch (ExecutionException e) {
            System.out.printf(
                    "agent processing failed: %s, reason: %s%n",
                    agent.getClass().getSimpleName(),
                    e.getCause().getMessage()
            );
            return null;
        }catch (InterruptedException e) {
            future.cancel(true);
            Thread.currentThread().interrupt();
            System.out.printf("agent processing interrupted: %s \n", agent.getClass().getSimpleName());
            return null;
        }
    }
}
