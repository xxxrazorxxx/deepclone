fun main() {
    val child = Man("Harry", 15, mutableListOf("Book", "Test book"), null)
    val man = Man("James", 34, mutableListOf<String>(), child)
    val man2 = CopyUtils.deepClone(man) as Man

    println(man.child.name)
    println(man2.child.name)
}