package com.example.mario

class Level(
    val name: String,
    val worldWidth: Float,
    val worldHeight: Float,
    val startX: Float,
    val startY: Float,
    val platforms: List<Platform>,
    val goalX: Float,
    val goalY: Float
) {
    companion object {
        // Экран: 1920 x 1080 (условно)
        // Земля: 100 пикселей высотой внизу
        private const val G = 1080f   // высота "мира"
        private const val GROUND_Y = 980f

        fun getLevel(index: Int): Level {
            return when (index) {
                0 -> level1()
                1 -> level2()
                2 -> level3()
                else -> level1()
            }
        }

        // Уровень 1: простые платформы
        private fun level1(): Level {
            val platforms = mutableListOf<Platform>()

            // Земля (несколько блоков с ямами)
            platforms.add(Platform(0f, GROUND_Y, 600f, 100f, Platform.TYPE_GROUND))
            platforms.add(Platform(700f, GROUND_Y, 500f, 100f, Platform.TYPE_GROUND))
            platforms.add(Platform(1400f, GROUND_Y, 700f, 100f, Platform.TYPE_GROUND))

            // Платформы в воздухе
            platforms.add(Platform(300f, 800f, 150f, 30f, Platform.TYPE_BRICK))
            platforms.add(Platform(550f, 650f, 150f, 30f, Platform.TYPE_BRICK))
            platforms.add(Platform(850f, 750f, 200f, 30f, Platform.TYPE_BLOCK))
            platforms.add(Platform(1200f, 600f, 150f, 30f, Platform.TYPE_BRICK))
            platforms.add(Platform(1500f, 750f, 200f, 30f, Platform.TYPE_BLOCK))

            // Финиш
            platforms.add(Platform(1950f, 850f, 40f, 130f, Platform.TYPE_GOAL))

            return Level(
                name = "Уровень 1",
                worldWidth = 2100f,
                worldHeight = G,
                startX = 100f,
                startY = 800f,
                platforms = platforms,
                goalX = 1950f,
                goalY = 850f
            )
        }

        // Уровень 2: больше платформ
        private fun level2(): Level {
            val platforms = mutableListOf<Platform>()

            platforms.add(Platform(0f, GROUND_Y, 400f, 100f, Platform.TYPE_GROUND))
            platforms.add(Platform(550f, GROUND_Y, 300f, 100f, Platform.TYPE_GROUND))
            platforms.add(Platform(1000f, GROUND_Y, 400f, 100f, Platform.TYPE_GROUND))
            platforms.add(Platform(1550f, GROUND_Y, 800f, 100f, Platform.TYPE_GROUND))

            platforms.add(Platform(200f, 800f, 120f, 30f, Platform.TYPE_BRICK))
            platforms.add(Platform(420f, 700f, 120f, 30f, Platform.TYPE_BRICK))
            platforms.add(Platform(650f, 600f, 120f, 30f, Platform.TYPE_BRICK))
            platforms.add(Platform(900f, 700f, 150f, 30f, Platform.TYPE_BLOCK))
            platforms.add(Platform(1150f, 800f, 150f, 30f, Platform.TYPE_BRICK))
            platforms.add(Platform(1400f, 700f, 150f, 30f, Platform.TYPE_BLOCK))
            platforms.add(Platform(1700f, 600f, 150f, 30f, Platform.TYPE_BRICK))

            platforms.add(Platform(2200f, 850f, 40f, 130f, Platform.TYPE_GOAL))

            return Level(
                name = "Уровень 2",
                worldWidth = 2400f,
                worldHeight = G,
                startX = 100f,
                startY = 800f,
                platforms = platforms,
                goalX = 2200f,
                goalY = 850f
            )
        }

        // Уровень 3: сложнее
        private fun level3(): Level {
            val platforms = mutableListOf<Platform>()

            platforms.add(Platform(0f, GROUND_Y, 300f, 100f, Platform.TYPE_GROUND))
            platforms.add(Platform(450f, GROUND_Y, 200f, 100f, Platform.TYPE_GROUND))
            platforms.add(Platform(800f, GROUND_Y, 200f, 100f, Platform.TYPE_GROUND))
            platforms.add(Platform(1150f, GROUND_Y, 200f, 100f, Platform.TYPE_GROUND))
            platforms.add(Platform(1500f, GROUND_Y, 200f, 100f, Platform.TYPE_GROUND))
            platforms.add(Platform(1850f, GROUND_Y, 600f, 100f, Platform.TYPE_GROUND))

            platforms.add(Platform(200f, 800f, 100f, 30f, Platform.TYPE_BRICK))
            platforms.add(Platform(500f, 700f, 100f, 30f, Platform.TYPE_BRICK))
            platforms.add(Platform(750f, 600f, 100f, 30f, Platform.TYPE_BRICK))
            platforms.add(Platform(1000f, 700f, 100f, 30f, Platform.TYPE_BRICK))
            platforms.add(Platform(1250f, 600f, 100f, 30f, Platform.TYPE_BRICK))
            platforms.add(Platform(1500f, 700f, 100f, 30f, Platform.TYPE_BRICK))
            platforms.add(Platform(1750f, 600f, 100f, 30f, Platform.TYPE_BRICK))

            platforms.add(Platform(2300f, 850f, 40f, 130f, Platform.TYPE_GOAL))

            return Level(
                name = "Уровень 3",
                worldWidth = 2500f,
                worldHeight = G,
                startX = 50f,
                startY = 800f,
                platforms = platforms,
                goalX = 2300f,
                goalY = 850f
            )
        }
    }
}
