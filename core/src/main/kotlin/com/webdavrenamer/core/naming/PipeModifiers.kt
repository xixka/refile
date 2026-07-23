package com.webdavrenamer.core.naming

/**
 * 管道修饰符（计划 §5.5 管道修饰符表，全部自实现，可链式 `{n|upper|space(_)}`）。
 *
 * 大小写 / 补零取整 / 字符替换 / 截取匹配 / 命名变换 / 清洗转写 / 列表。
 */
object PipeModifiers {

    /** 应用单个修饰符到值。 */
    @OptIn(kotlin.ExperimentalStdlibApi::class)
    fun apply(value: Any?, modifier: String): Any? {
        if (value == null) return null
        val name = modifier.substringBefore('(').trim()
        val argsRaw = if ('(' in modifier) modifier.substringAfter('(').removeSuffix(")") else ""
        val args = argsRaw.split(',').map { it.trim() }
        return when (name) {
            // 大小写
            "upper" -> value.toStr().uppercase()
            "lower" -> value.toStr().lowercase()
            "upperInitial" -> upperInitial(value.toStr())
            "lowerTrail" -> lowerTrail(value.toStr())
            "title" -> value.toStr().split(' ').joinToString(" ") { w ->
                w.replaceFirstChar { it.titlecase() }
            }
            // 补零与取整
            "pad" -> {
                val n = args.getOrNull(0)?.toIntOrNull() ?: 2
                value.toStr().padStart(n, '0')
            }
            "round" -> {
                val n = args.getOrNull(0)?.toIntOrNull() ?: 0
                val d = value.toStr().toDoubleOrNull() ?: return value
                val f = Math.pow(10.0, n.toDouble())
                (kotlin.math.round(d * f) / f).toString()
            }
            // 字符替换
            "space" -> value.toStr().replace(' ', args.getOrNull(0)?.firstOrNull() ?: ' ')
            "dot" -> value.toStr().replace(' ', '.')
            "colon" -> value.toStr().replace(':', args.getOrNull(0)?.firstOrNull() ?: '-')
            "slash" -> value.toStr().replace('/', args.getOrNull(0)?.firstOrNull() ?: '-')
            "replace" -> {
                val a = args.getOrNull(0) ?: return value
                val b = args.getOrNull(1) ?: ""
                value.toStr().replace(a, b)
            }
            "replaceAll" -> {
                val a = args.getOrNull(0) ?: return value
                val b = args.getOrNull(1) ?: ""
                value.toStr().replace(Regex(a), b)
            }
            "removeAll" -> {
                val p = args.getOrNull(0) ?: return value
                value.toStr().replace(Regex(p), "")
            }
            // 截取与匹配
            "before" -> {
                val p = args.getOrNull(0) ?: return value
                value.toStr().substringBefore(p)
            }
            "after" -> {
                val p = args.getOrNull(0) ?: return value
                value.toStr().substringAfter(p)
            }
            "match" -> {
                val p = args.getOrNull(0) ?: return value
                Regex(p).find(value.toStr())?.value
            }
            "matchAll" -> {
                val p = args.getOrNull(0) ?: return value
                Regex(p).findAll(value.toStr()).map { it.value }.toList()
            }
            // 命名变换
            "sortName" -> sortName(value.toStr()) // 去冠词排序名
            "initialName" -> value.toStr().split(' ').joinToString(" ") { w ->
                w.firstOrNull()?.toString()?.plus(".") ?: ""
            }
            "acronym" -> value.toStr().split(Regex("[^A-Za-z0-9]"))
                .filter { it.isNotEmpty() }
                .mapNotNull { it.firstOrNull()?.uppercaseChar()?.toString() }
                .joinToString("")
            "roman" -> toRoman(value.toStr().toIntOrNull() ?: 0)
            // 清洗与转写
            "clean" -> value.toStr().replace(Regex("[\\\\/:*?\"<>|]"), "").trim()
            "ascii" -> value.toStr().map { if (it.code < 128) it else '?' }.joinToString("")
            "transliterate" -> value.toStr().map { if (it.code < 128) it else '?' }.joinToString("")
            "validateFileName" -> value.toStr().replace(Regex("[\\\\/:*?\"<>|]"), "-").trim()
            // 列表
            "joining" -> {
                val sep = args.getOrNull(0) ?: ","
                val prefix = args.getOrNull(1) ?: ""
                val suffix = args.getOrNull(2) ?: ""
                when (value) {
                    is List<*> -> "$prefix${value.joinToString(sep)}$suffix"
                    else -> value.toStr()
                }
            }
            else -> value // 未知修饰符：原样返回（容错）
        }
    }

    private fun Any.toStr(): String = when (this) {
        is List<*> -> joinToString(",")
        is Boolean -> if (this) "true" else "false"
        else -> toString()
    }

    private fun upperInitial(s: String): String {
        val trimmed = s.trimStart()
        val leading = s.length - trimmed.length
        return s.substring(0, leading) + trimmed.replaceFirstChar { it.titlecase() }
    }

    private fun lowerTrail(s: String): String {
        if (s.isEmpty()) return s
        return s.substring(0, 1) + s.substring(1).lowercase()
    }

    private fun sortName(s: String): String {
        for (a in listOf("The ", "A ", "An ")) {
            if (s.startsWith(a, ignoreCase = true)) {
                return s.substring(a.length) + ", " + s.substring(0, a.length - 1)
            }
        }
        return s
    }

    private val romanMap = linkedMapOf(
        1000 to "M", 900 to "CM", 500 to "D", 400 to "CD",
        100 to "C", 90 to "XC", 50 to "L", 40 to "XL",
        10 to "X", 9 to "IX", 5 to "V", 4 to "IV", 1 to "I",
    )

    private fun toRoman(n: Int): String {
        if (n <= 0) return n.toString()
        val sb = StringBuilder()
        var x = n
        for ((v, sym) in romanMap) {
            while (x >= v) { sb.append(sym); x -= v }
        }
        return sb.toString()
    }
}
