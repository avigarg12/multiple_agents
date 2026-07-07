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
            // todo: re-order agents gemini->claude->GPT for calculate
            return List.of(new Gemini(), new Claude(),new Gpt());
        } else if (type.equals("logical")) {
            // todo: re-order agents claude->Gemini->GPT for logical type
            return List.of(new Claude(),new Gemini(),new Gpt());
        }

        return List.of();
    }
}
