package advent.of.kotlin.days

import advent.of.kotlin.utils.readInputFileByLines

class Day8 {
    class Forest(val trees: List<List<Int>>) {

        private fun isTreeVisibleLeft(x: Int, y: Int): Boolean {
            val self = trees[y][x]
            val lineToBorder = trees[y].subList(0, x)
            return lineToBorder
                .all { it < self }
        }

        private fun isTreeVisibleRight(x: Int, y: Int): Boolean {
            val self = trees[y][x]
            val lineToBorder = trees[y].subList(x + 1, trees[y].size)
            return lineToBorder
                .all { it < self }
        }

        private fun isTreeVisibleTop(x: Int, y: Int): Boolean {
            val self = trees[y][x]
            val lineToBorder = trees.map { it[x] }.subList(0, y)
            return lineToBorder
                .all { it < self }
        }

        private fun isTreeVisibleBottom(x: Int, y: Int): Boolean {
            val self = trees[y][x]
            val lineToBorder = trees.map { it[x] }.subList(y + 1, trees.first().size)
            return lineToBorder
                .all { it < self }
        }

        private fun isTreeVisible(x: Int, y: Int): Boolean {
            // is on edge
            if (x == 0) return true
            if (y == 0) return true
            if (y == trees.size.dec()) return true
            if (x == trees[0].size.dec()) return true
            if (isTreeVisibleLeft(x, y)) return true
            if (isTreeVisibleRight(x, y)) return true
            if (isTreeVisibleTop(x, y)) return true
            if (isTreeVisibleBottom(x, y)) return true

            return false
        }

        fun treesVisibleFrom(x: Int, y: Int): Int {
            val self = trees[y][x]
            val lineToLeftBorder = trees[y].subList(0, x).reversed()
            val lineToRightBorder = trees[y].subList(x + 1, trees[y].size)
            val lineToTopBorder = trees.map { it[x] }.subList(0, y).reversed()
            val lineToBottomBorder = trees.map { it[x] }.subList(y + 1, trees.first().size)

            return visibleFromList(self, lineToLeftBorder) * 
                    visibleFromList(self, lineToRightBorder) *
                    visibleFromList(self, lineToTopBorder) * 
                    visibleFromList(self, lineToBottomBorder)
        }
        
        fun highestScenicScore(): Int {
            val yCoordinates = 0..trees.size.dec()
            val xCoordinates = 0..trees.first().size.dec()
            val coordinates = xCoordinates.flatMap { x -> yCoordinates.map { y -> Pair(x, y) } }
            return coordinates
                .map { treesVisibleFrom(it.first, it.second)}
                .sortedDescending()
                .first()
        }

        fun countVisibleTrees(): Int {
            val yCoordinates = 0..trees.size.dec()
            val xCoordinates = 0..trees.first().size.dec()
            val coordinates = xCoordinates.flatMap { x -> yCoordinates.map { y -> Pair(x, y) } }
            val isVisible = coordinates.count { isTreeVisible(it.first, it.second) }
            return isVisible
        }

        fun visibleFromList(self: Int, row: List<Int>): Int {
            var count = 0
            for (x in row) {
                if (x < self) {
                    count += 1
                    continue
                }
                count += 1
                break
            }
            return count
        }
    }

    private fun parseForest(lines: List<String>): Forest {
        val trees = lines
            .map { it.toCharArray().toList() }
            .map { row -> row.map { it.digitToInt() } }
        return Forest(trees)
    }

    fun part1() {
        val lines = readInputFileByLines("day8.txt")
        val forest = parseForest(lines)
        val total = forest.countVisibleTrees()
        println(total)
    }

    fun part2() {
        val lines = readInputFileByLines("day8.txt")
        val forest = parseForest(lines)
        println(forest.highestScenicScore())
    }
}