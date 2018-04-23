package kislaya;

public class Catacombs {

    public Room first;

    public Catacombs() {
        first = new Room(null);
        Room leftRoom = new Room(first);
        Room rightRoom = new Room(first);
        first.to.add(leftRoom);
        first.to.add(rightRoom);
        Room leftLeftRoom = new Room(leftRoom);
        Room leftRightRoom = new Room(leftRoom);
        leftRoom.to.add(leftLeftRoom);
        leftRoom.to.add(leftRightRoom);
        Room rightLeftRoom = new Room(rightRoom);
        Room rightRightRoom = new Room(rightRoom);
        rightRoom.to.add(rightLeftRoom);
        rightRoom.to.add(rightRightRoom);
    }
}
