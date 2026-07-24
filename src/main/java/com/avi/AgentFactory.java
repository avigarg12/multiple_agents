package com.avi;

import agents.Agent;
import agents.Claude;
import agents.Gemini;
import agents.Gpt;
import org.springframework.stereotype.Component;

import java.util.List;

@Component // tells spring to manage this class automatically
public class AgentFactory {
    List<Agent> getAgents(AgentTaskType type) {
        if (type == AgentTaskType.HISTORY) {
            return List.of(new Gpt(), new Claude(), new Gemini());
        } else if (type == AgentTaskType.CALCULATE) {
            return List.of(new Gemini(), new Claude(),new Gpt());
        } else if (type == AgentTaskType.LOGICAL) {
            return List.of(new Claude(),new Gemini(),new Gpt());
        }

        return List.of();
    }
}
