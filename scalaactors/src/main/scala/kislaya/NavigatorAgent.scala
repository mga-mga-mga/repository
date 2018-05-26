package kislaya

import akka.actor.Actor


 class NavigatorAgent extends Actor {

    var catacombs: Room
    
    def setup() {
        System.out.println("NavigatorAgent started")
        def dfd = new DFAgentDescription()
        dfd.setName(getAID())
        def sd = new ServiceDescription()
        sd.setType("navigator")
        sd.setName(getClass().getName())
        dfd.addServices(sd)
        try {
            DFService.register(this, dfd)
        }
        catch (FIPAException e) {
            e.printStackTrace()
        }

        addBehaviour(new SpBehaviour())
    }

    class SpBehaviour extends CyclicBehaviour {
        def action() {
            def mt = MessageTemplate.MatchPerformative(ACLMessage.INFORM)
            def msg = myAgent.receive(mt)
            if (msg != null) {
                if (msg.getContent().startsWith(Words.CAN_GO)) {
                    String options = msg.getContent().replace(Words.CAN_GO, "")
                    String[] numbers = options.split(", ")

                    if (catacombs == null){
                        catacombs = new Room(null)
                    }

                    if (catacombs.to.size() == numbers.length || options.equals(Words.NOWHERE)) {
                        def backStep = catacombs.from
                        if (backStep == null) {
                            send(msg, Words.STOP)
                        } else {
                            catacombs = catacombs.from
                            String content = Words.GO + Words.BACK
                            send(msg, content)
                        }
                    } else {
                        def nextStep = new Room(catacombs)
                        catacombs.to.add(nextStep)
                        String content = Words.GO + (catacombs.to.size() == 1 ? Words.LEFT : Words.RIGHT)
                        catacombs = nextStep
                        send(msg, content)
                    }
                }
            }
            else {
                block()
            }
        }
    }

    def send(msg: ACLMessage, content: String) {
        System.out.println(content)
        def reply = msg.createReply()
        reply.setPerformative(ACLMessage.PROPOSE)
        reply.setContent(content)
        send(reply)
    }
}