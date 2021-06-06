package com.project.cardgame.data.repository.game

import com.project.cardgame.data.model.internal.game.Level

class LevelRepository {

	fun getLevels() = listOf(
		Level(1, 6, 3, 3),
		Level(2, 8, 4, 5),
		Level(3, 12, 3, 7),
		Level(4, 16, 4, 10)
	)
}