package com.avi;

import agents.Agent;
import agents.Claude;
import agents.Gemini;
import agents.Gpt;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

public class AgentFactoryTest {
    @Test
    void shouldReturnHistoryAgentsInCorrectOrder(){
        AgentFactory factory = new AgentFactory();
        List<Agent> agents = factory.getAgents(AgentTaskType.HISTORY);

        assertEquals(3, agents.size());
        assertInstanceOf(Gpt.class, agents.get(0));
        assertInstanceOf(Claude.class, agents.get(1));
        assertInstanceOf(Gemini.class, agents.get(2));
    }

    @Test
    void shouldReturnCalculateAgentsInCorrectOrder(){
        AgentFactory factory = new AgentFactory();
        List<Agent> agents = factory.getAgents(AgentTaskType.CALCULATE);

        assertEquals(3, agents.size());
        assertInstanceOf(Gemini.class, agents.get(0));
        assertInstanceOf(Claude.class, agents.get(1));
        assertInstanceOf(Gpt.class, agents.get(2));
    }

    @Test
    void shouldReturnLogicalAgentsInCorrectOrder(){
        AgentFactory factory = new AgentFactory();
        List<Agent> agents = factory.getAgents(AgentTaskType.LOGICAL);

        assertEquals(3, agents.size());
        assertInstanceOf(Claude.class, agents.get(0));
        assertInstanceOf(Gemini.class, agents.get(1));
        assertInstanceOf(Gpt.class, agents.get(2));
    }

    @Test
    void shouldReturnEmptyListWhenTaskTypeIsNull(){
        AgentFactory factory = new AgentFactory();
        List<Agent> agents = factory.getAgents(null);

        assertEquals(0,agents.size());
    }
}
