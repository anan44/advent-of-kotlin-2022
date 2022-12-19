package advent.of.kotlin.days

import advent.of.kotlin.utils.readFileAsString

class Day7 {

    open class Command
    class LSCommand(private val result: List<FSElem>) : Command() {
        override fun toString() = "ls $result"

        fun getFullPathFiles(pwd: String): List<File> {
            return result
                .filterIsInstance<File>()
                .map { File("$pwd${it.name}", it.size) }
        }
    }

    class CDCommand(private val target: String) : Command() {
        override fun toString() = "cd $target"

        fun nextDirPath(pwd: String): String {

            return when (target) {
                ".." -> {
                    val re = """(.*/)\w+/""".toRegex()
                    val partial = re.find(pwd)!!
                    partial.groups[1]!!.value
                }

                "/" -> "/"
                else -> {
                    "$pwd$target/"
                }
            }
        }
    }

    open class FSElem
    class Dir(val name: String) : FSElem()
    class File(val name: String, val size: Int) : FSElem() {
        override fun toString(): String {
            return "$name($size)"
        }
    }


    private fun stringToFSElem(s: String): FSElem {
        val split = s.split(" ")
        return if (split[0] == "dir") {
            Dir(split[1])
        } else {
            File(split[1], split[0].toInt())
        }
    }

    private fun parseLsResult(s: String): List<FSElem> {
        val split = s.trim().split("\n")
        return split
            .map {
                stringToFSElem(it)
            }
    }

    private fun parseCommands(raw: String): List<Command> {
        val re = """\$\s(\w+)\s([\w\s./]+)""".toRegex(RegexOption.MULTILINE)
        val matches = re.findAll(raw)
        return matches
            .map {
                val g1 = it.groups[1]!!.value
                val g2 = it.groups[2]!!.value.trim()
                if (g1 == "ls")
                    LSCommand(parseLsResult(g2)) else
                    CDCommand(g2)
            }
            .toList()
    }


    private fun buildFileTree(commands: List<Command>): Map<String, Int> {
        var pwd = "/"
        val files = mutableListOf<File>()
        for (c in commands) {
            when (c) {
                is CDCommand -> {
                    pwd = c.nextDirPath(pwd)
                }

                is LSCommand -> {
                    files.addAll(c.getFullPathFiles(pwd))
                }
            }
        }
        return files.associate { Pair(it.name, it.size) }
    }

    private fun fileToFolders(path: String): List<String> {
        var pathSoFar = ""
        val folders = mutableListOf<String>()
        val split = path.split("/")
        for (s in split.subList(0, split.size - 1)) {
            if (s == "") {
                folders.add("/")
                continue
            }
            pathSoFar = "$pathSoFar/$s"
            folders.add(pathSoFar)
        }
        return folders
    }

    private fun getDirectories(fileTree: Map<String, Int>): List<String> {
        return fileTree
            .keys
            .flatMap { fileToFolders(it) }
            .toSet()
            .toList()
    }

    private fun calculateFolderSizes(fileTree: Map<String, Int>): List<Pair<String, Int>> {
        val dirs = getDirectories(fileTree)
        val files = fileTree.toList()

        val helper = { dir: String ->
            Pair(
                dir,
                files
                    .filter { it.first.startsWith(dir) }
                    .sumOf { it.second })
        }

        return dirs.map(helper)
    }

    fun part1() {
        val raw = readFileAsString("day7.txt")
        val commands = parseCommands(raw)
        val fileTree = buildFileTree(commands)
        val folderSizes = calculateFolderSizes(fileTree)
        val result = folderSizes
            .filter { it.second <= 100000 }
            .sumOf { it.second }
        println(result)
    }
}