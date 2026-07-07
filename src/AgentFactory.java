import agents.Agent;
import agents.Claude;
import agents.Gemini;
import agents.Gpt;


public class AgentFactory {
    Agent getAgentObject(String type, Agent curAgent) {
        if (type.equals("history")) {
            if(curAgent == null){
                return new Gpt();
            }else{
                if(curAgent instanceof Gpt){
                    return new Claude();
                }else if(curAgent instanceof Claude){
                    return new Gemini();
                }else{
                    // todo: throw exception
                    return null;
                }
            }
        } else if (type.equals("calculate")) {
            // todo: same for calculate
        } else {
            // todo: same for logical type
        }

        // todo: throw exception
        return null;
    }
}
