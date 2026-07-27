package com.avi;

import agents.Agent;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController// Tells Spring this call handles JSON REST requests
@RequestMapping("/api/agents") // Base URL endpoint
public class AgentController {
    private final AgentFactory agentFactory;
    private final FallbackAgentProcessor processor;

    // Spring will automatically inject the managed components here (Constructor Injection)
    public AgentController(AgentFactory agentFactory, FallbackAgentProcessor processor){
        this.agentFactory = agentFactory;
        this.processor = processor;
    }

    // Endpoint: POST http://localhost:8080/api/agents/process
    @PostMapping("/process")
    public ResponseEntity<AgentResponse> processRequest(@RequestBody AgentRequest request){

        // 1. Get ordered agents based on requested task type
        List<Agent> agents = agentFactory.getAgents(request.getTaskType());

        // 2. Process message using our fallback mechanism
        String result = processor.process(agents, request.getMessage());

        if(result == null){
            return ResponseEntity.status(504).body(new AgentResponse(null, false, "All agents failed or timed out."));
        }
        return ResponseEntity.ok(new AgentResponse(result, true, "success"));
    }

    // DTO (Data Transfer Object) for mapping JSON Request Body
    static class AgentRequest{
        private AgentTaskType taskType;
        private  String message;

        public AgentTaskType getTaskType() {
            return taskType;
        }

        public void setTaskType(AgentTaskType taskType) {
            this.taskType = taskType;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    // DTO for Mapping JSON Response Body
    static class AgentResponse{
        private final String output;
        private final boolean success;
        private final String statusMessage;

        public AgentResponse(String output, boolean success, String statusMessage) {
            this.output = output;
            this.success = success;
            this.statusMessage = statusMessage;
        }

        public String getOutput() {
            return output;
        }

        public boolean isSuccess() {
            return success;
        }

        public String getStatusMessage() {
            return statusMessage;
        }
    }

}
