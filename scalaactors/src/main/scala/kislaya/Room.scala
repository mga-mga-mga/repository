package kislaya

class Room {

    var from: Room

    var to: List[Room]

    def Room(from: Room) {
        this.from = from
        this.to = List[Room]
    }

}
