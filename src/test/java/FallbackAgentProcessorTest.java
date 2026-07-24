import agents.Agent;
import com.avi.FallbackAgentProcessor;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class FallbackAgentProcessorTest {
    @Test
    void shouldReturnResponseWhenFirstAgentSucceeds(){
        FallbackAgentProcessor processor = new FallbackAgentProcessor(1, TimeUnit.SECONDS);
        try{
            Agent successAgent = message -> "success";

            String response = processor.process(List.of(successAgent),"hello");

            assertEquals("success", response);
        }finally {
            processor.shutdown();
        }
    }

    @Test
    void shouldFallbackWhenFirstAgentReturnsNull(){
        FallbackAgentProcessor processor = new FallbackAgentProcessor(1, TimeUnit.SECONDS);

        try {
            Agent nullAgent = message -> null;
            Agent successAgent = message -> "success";

            String response = processor.process(List.of(nullAgent,successAgent), "hello");

            assertEquals("success", response);
        }finally {
            processor.shutdown();
        }
    }

    @Test
    void shouldFallbackWhenFirstAgentTimesOut(){
        FallbackAgentProcessor processor = new FallbackAgentProcessor(100, TimeUnit.MILLISECONDS);
        try {
            Agent slowAgent = message -> {
                Thread.sleep(500);
                return "too late";
            };

            Agent successAgent = message -> "success";

            String response = processor.process(List.of(slowAgent,successAgent), "hello");

            assertEquals("success", response);
        }finally {
            processor.shutdown();
        }
    }

    @Test
    void shouldFallbackWhenFirstAgentThrowsException(){
        FallbackAgentProcessor processor = new FallbackAgentProcessor(1, TimeUnit.SECONDS);
        try {
            Agent failingAgent = message -> {
                throw new RuntimeException("agent failed");
            };

            Agent successAgent = message -> "success";

            String response = processor.process(List.of(failingAgent,successAgent), "hello");

            assertEquals("success", response);
        }finally {
            processor.shutdown();
        }
    }

    @Test
    void shouldReturnNullWhenAllAgentsReturnNull(){
        FallbackAgentProcessor processor = new FallbackAgentProcessor(1, TimeUnit.SECONDS);
        try {
            Agent firstNullAgent = message -> null;
            Agent secondNullAgent = message -> null;

            String response = processor.process(List.of(firstNullAgent,secondNullAgent), "hello");

            assertNull(response);
        }finally {
            processor.shutdown();
        }
    }

    @Test
    void shouldFallbackWhenInterrupted(){
        FallbackAgentProcessor processor = new FallbackAgentProcessor(5,TimeUnit.SECONDS);
        try{
            Agent interuptingAgent = message -> {
              Thread.currentThread().interrupt();
                throw new InterruptedException("Interrupted");
            };

            Agent successAgent = message -> "success";

            String response = processor.process(List.of(interuptingAgent,successAgent),"hello");

            assertEquals("success", response);
        }finally {
            processor.shutdown();
        }
    }
}
