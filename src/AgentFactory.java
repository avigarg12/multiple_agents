import agents.Agent;
import agents.Claude;
import agents.Gemini;
import agents.Gpt;

import java.util.List;


public class AgentFactory {
    List<Agent> getAgents(String type) {
        if (type.equals("history")) {
            return List.of(new Gpt(), new Claude(), new Gemini());
        } else if (type.equals("calculate")) {
            return List.of(new Gemini(), new Claude(),new Gpt());
        } else if (type.equals("logical")) {
            return List.of(new Claude(),new Gemini(),new Gpt());
        }

        return List.of();
    }
}
