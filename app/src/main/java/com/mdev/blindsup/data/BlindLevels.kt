package com.mdev.blindsup.data

class BlindLevels {
    val blinds = mutableListOf<Level>(
        Level(25, 50, 0),
        Level(50, 100, 0),
        Level(100, 200, 25),
        Level(200, 400, 25),
        Level(400, 800, 50)
    )

}

data class Level(val smallBlind: Int, val bigBlind: Int, val ante: Int)