package kislaya;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;


public class NavigatorAgent extends Agent {

    private Room catacombs;

    @Override
    protected void setup() {
        System.out.println("NavigatorAgent started");
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setType("navigator");
        sd.setName(getClass().getName());
        dfd.addServices(sd);
        try {
            DFService.register(this, dfd);
        }
        catch (FIPAException e) {
            e.printStackTrace();
        }

        addBehaviour(new SpBehaviour());
    }

    private class SpBehaviour extends CyclicBehaviour {
        public void action() {
            MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
            ACLMessage msg = myAgent.receive(mt);
            if (msg != null) {
                if (msg.getContent().startsWith(Words.CAN_GO)) {
                    String options = msg.getContent().replace(Words.CAN_GO, "");
                    String[] numbers = options.split(", ");

                    if (catacombs == null){
                        catacombs = new Room(null);
                    }

                    if (catacombs.to.size() == numbers.length || options.equals(Words.NOWHERE)) {
                        Room backStep = catacombs.from;
                        if (backStep == null) {
                            send(msg, Words.STOP);
                        } else {
                            catacombs = catacombs.from;
                            String content = Words.GO + Words.BACK;
                            send(msg, content);
                        }
                    } else {
                        Room nextStep = new Room(catacombs);
                        catacombs.to.add(nextStep);
                        String content = Words.GO + (catacombs.to.size() == 1 ? Words.LEFT : Words.RIGHT);
                        catacombs = nextStep;
                        send(msg, content);
                    }
                }
            }
            else {
                block();
            }
        }
    }

    private void send(ACLMessage msg, String content) {
        System.out.println(content);
        ACLMessage reply = msg.createReply();
        reply.setPerformative(ACLMessage.PROPOSE);
        reply.setContent(content);
        send(reply);
    }
}