import agents.Agent;
import com.avi.AgentFactory;
import com.avi.FallbackAgentProcessor;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AgentFallbackIntegrationTest {
    @Test
    void shouldFallbackToGeminiWhenGptAndClaudeTimeout(){
        AgentFactory factory = new AgentFactory();
        List<Agent> agents = factory.getAgents(AgentTaskType.HISTORY);

        FallbackAgentProcessor processor = new FallbackAgentProcessor(1, TimeUnit.SECONDS);
        try{
            String response = processor.process(agents, "hello");
            assertEquals("",response);
        }finally {
            processor.shutdown();
        }
    }
}
