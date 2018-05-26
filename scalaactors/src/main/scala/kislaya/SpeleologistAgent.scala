package kislaya

import akka.actor.Actor

class SpeleologistAgent extends Actor {

    var navigatorAgent: AID

    var currentRoom: Room

    def setup() {
        System.out.println("SpeleologistAgent started")
        def catacombs = new Catacombs()
        currentRoom = catacombs.first
        addBehaviour(new ListenToNavigatorBehaviour())
    }

    class ListenToNavigatorBehaviour extends CyclicBehaviour {
        var int step = 0
        var MessageTemplate mt
        
        def action() {
            switch (step) {
                case 0: {
                    def dfd = new DFAgentDescription()
                    def sd = new ServiceDescription()
                    sd.setType("navigator")
                    dfd.addServices(sd)
                    try {
                        DFAgentDescription[] result = DFService.search(myAgent, dfd)
                        if (result != null && result.length > 0) {
                            navigatorAgent = result[0].getName()
                            ++step
                        } else {
                            Thread.sleep(5000)
                        }
                    } catch (Exception e) {
                        e.printStackTrace()
                    }
                    break
                }
                case 1: {
                    ACLMessage message = new ACLMessage(ACLMessage.INFORM)
                    message.addReceiver(navigatorAgent)
                    String content = null
                    if (currentRoom.to.isEmpty()) {
                        content = Words.CAN_GO + Words.NOWHERE
                    }
                    if (currentRoom.to.size() == 1) {
                        content = Words.CAN_GO + Words.LEFT
                    }
                    if (currentRoom.to.size() == 2) {
                        content = Words.CAN_GO + Words.LEFT + ", " + Words.RIGHT
                    }
                    message.setContent(content)
                    message.setConversationId("ns")
                    message.setReplyWith("order" + System.currentTimeMillis())
                    System.out.println(message.getContent())
                    myAgent.send(message)
                    mt = MessageTemplate.and(MessageTemplate.MatchConversationId("ns"),
                            MessageTemplate.MatchInReplyTo(message.getReplyWith()))
                    ++step
                    break
                }
                case 2: {
                    ACLMessage reply = myAgent.receive(mt)
                    if (reply != null) {
                        if (reply.getContent().startsWith(Words.GO)) {
                            String action = reply.getContent().replace(Words.GO, "")
                            if (Words.BACK.equals(action)) {
                                currentRoom = currentRoom.from
                            } else {
                                Integer pathNumber = action.equalsIgnoreCase(Words.LEFT) ? 1 : 2
                                currentRoom = currentRoom.to.get(pathNumber - 1)
                            }
                            --step
                        }
                        if (Words.STOP.equals(reply.getContent())) {
                            ++step
                        }
                    } else {
                        block()
                    }
                    break
                }
                case 3:
                    block()
            }
        }

    }
}