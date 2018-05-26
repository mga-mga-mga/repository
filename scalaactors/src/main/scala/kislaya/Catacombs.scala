package kislaya

class Catacombs {

    var first: Room

    def Catacombs() {
        first = new Room(null)
        var leftRoom = new Room(first)
        var rightRoom = new Room(first)
        first.to.add(leftRoom)
        first.to.add(rightRoom)
        var leftLeftRoom = new Room(leftRoom)
        var leftRightRoom = new Room(leftRoom)
        leftRoom.to.add(leftLeftRoom)
        leftRoom.to.add(leftRightRoom)
        var rightLeftRoom = new Room(rightRoom)
        var rightRightRoom = new Room(rightRoom)
        rightRoom.to.add(rightLeftRoom)
        rightRoom.to.add(rightRightRoom)
    }
}
