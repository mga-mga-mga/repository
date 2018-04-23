package kislaya;

import java.util.ArrayList;
import java.util.List;

public class Room {

    public Room from;

    public List<Room> to;

    public Room(Room from) {
        this.from = from;
        this.to = new ArrayList<>();
    }

}
