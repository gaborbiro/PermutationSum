fun main() {
    printAllRecursive(items.size, items)
    println()
    println("#${bestGroupingIndex + 1} with min diff of $minDiff  => ${bestPermutation?.joinToString()}")
}

val items = arrayOf(
    "Beach and Cold" to 17,
    "Toiletries" to 12,
    "Documents" to 6,
    "Health" to 8,
    "Clothes" to 10,
    "Electronics" to 10,
)

/**
 * For ex for 7 items this gives:
 * [1, 1, 5]
 * [1, 2, 4]
 * [1, 3, 3]
 * [2, 2, 3]
 */
val partitions = findPartitions(items.size, 3).map { Triple(it[0], it[1], it[2]) }

fun printAllRecursive(n: Int, elements: Array<Pair<String, Int>>) {
    if (n == 1) {
        verify(elements)
    } else {
        for (i in 0 until n - 1) {
            printAllRecursive(n - 1, elements)
            if (n % 2 == 0) {
                swap(elements, i, n - 1)
            } else {
                swap(elements, 0, n - 1)
            }
        }
        printAllRecursive(n - 1, elements)
    }
}

private fun <T> swap(input: Array<T>, a: Int, b: Int) {
    val tmp: T = input[a]
    input[a] = input[b]
    input[b] = tmp
}

private var minDiff = Int.MAX_VALUE
private var bestGroupingIndex = -1
private var bestPermutation: List<Pair<String, Int>>? = null

// Function to find partitions of a number `n` into `k` groups
fun findPartitions(n: Int, k: Int): List<List<Int>> {
    val result = mutableListOf<List<Int>>()

    // Helper function to generate partitions recursively
    fun generatePartitions(n: Int, k: Int, currentPartition: MutableList<Int>, start: Int) {
        // Base case: if k == 1, we have one group left and its size must be n
        if (k == 1) {
            currentPartition.add(n)
            result.add(currentPartition.sortedDescending()) // Sort to ensure order doesn't matter
            currentPartition.removeAt(currentPartition.size - 1)
            return
        }

        // Try each possible size for the first group
        for (i in start..n / k) {
            currentPartition.add(i)
            generatePartitions(n - i, k - 1, currentPartition, i) // Recursive step
            currentPartition.removeAt(currentPartition.size - 1) // Backtrack
        }
    }

    generatePartitions(n, k, mutableListOf(), 1)

    // Remove duplicates (because different orders of the same partition would appear)
    return result.distinct()
}

private fun verify(input: Array<Pair<String, Int>>) {
    val list = listOf(*input)
    val diffs = partitions.map { maxDiff(list, it) }
    val min = diffs.minOrNull()!!
    if (min < minDiff) {
        minDiff = min
        bestGroupingIndex = diffs.indexOf(minDiff)
        bestPermutation = list

        for (i in input.indices) {
            print(input[i])
            print(" ")
        }
        println()
    }
}

fun maxDiff(list: List<Pair<String, Int>>, grouping: Triple<Int, Int, Int>): Int {
    return arrayOf(
        list.subList(0, grouping.first).sumOf { it.second },
        list.subList(grouping.first, grouping.first + grouping.second).sumOf { it.second },
        list.subList(grouping.first + grouping.second, grouping.first + grouping.second + grouping.third)
            .sumOf { it.second }
    ).let { it.maxOrNull()!! - it.minOrNull()!! }
}