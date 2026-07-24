package xa.refile.core.naming

/**
 * 模板引擎（计划 §5.5，非 Groovy）。
 *
 * 模板语法：
 * - 变量 `{n}` `{y}` `{s00e00}` `{t}`
 * - 管道修饰符 `{n|upper}`、可链式 `{n|lower|space(_)}`
 * - 路径分隔 `/` 表示目录层级（重命名 = MKCOL 建目录 + MOVE）
 * - 非法文件名字符 `\/:*?"<>|` 在最终输出前按 [NamingOptions] 处理
 *
 * 容错规则（简化条件块）：变量缺失时自动省略其所在的相邻括号组；
 * 渲染失败/缺失时该段留空并清理多余分隔符，不输出 `{undefined}` 字面量。
 *
 * 纯 Kotlin 无 Android 依赖。
 *
 * @property resolver 变量绑定解析器
 * @property options 命名可视化选项
 */
class TemplateEngine(
    private val resolver: BindingResolver,
    private val options: NamingOptions = NamingOptions(),
) {
    /**
     * 渲染模板为最终相对路径（相对库根）。多段以 `/` 分隔。
     * 返回 [RenderResult]，含渲染路径与警告。
     */
    fun render(template: String): RenderResult {
        val segmentWarnings = mutableListOf<String>()
        // 按目录层级切分
        val segments = template.split('/')
        val renderedSegments = segments.map { seg -> renderSegment(seg, segmentWarnings) }
        // 过滤空段（缺失变量导致的整段空）；清理每段首尾的多余分隔符
        val nonEmpty = renderedSegments
            .map { it.trim(' ', '.', '_', '-') }
            .filter { it.isNotBlank() }
        // 应用全局可视化选项（分隔符、大小写、非法字符）
        val processed = nonEmpty.map { applyGlobalOptions(it) }
        val path = processed.joinToString("/")
        // 警告在渲染过程中由 resolver 写入，结束后收集
        return RenderResult(path = path, warnings = (resolver.warnings + segmentWarnings).distinct())
    }

    /** 渲染单段（一个目录层级或文件名）。处理变量、管道、括号组容错。 */
    private fun renderSegment(segment: String, warnings: MutableList<String>): String {
        val sb = StringBuilder()
        var i = 0
        while (i < segment.length) {
            val c = segment[i]
            when {
                c == '{' -> {
                    val end = findClosingBrace(segment, i)
                    if (end == -1) {
                        sb.append(c); i++; continue
                    }
                    val expr = segment.substring(i + 1, end)
                    // 条件块 {?year?}({y}){/?} 简化：跳过 ?year? / /? 标记
                    if (expr.startsWith("?") || expr == "/?") {
                        // 条件块简化处理：直接忽略标记，内部按普通括号组容错
                        i = end + 1; continue
                    }
                    val value = evalExpression(expr)
                    if (value != null) {
                        sb.append(formatValue(value))
                    }
                    i = end + 1
                }
                c == '(' || c == '[' -> {
                    // 括号组容错：括号内含变量时，若整体渲染为空则省略整个括号组
                    val close = if (c == '(') ')' else ']'
                    val end = segment.indexOf(close, i + 1)
                    if (end == -1) { sb.append(c); i++; continue }
                    val inner = segment.substring(i + 1, end)
                    val renderedInner = renderSegment(inner, warnings)
                    if (renderedInner.isNotBlank()) {
                        sb.append(c).append(renderedInner).append(close)
                    }
                    i = end + 1
                }
                else -> { sb.append(c); i++ }
            }
        }
        return sb.toString()
    }

    /** 求值表达式：`变量名` 或 `变量名|修饰符|修饰符`。 */
    private fun evalExpression(expr: String): Any? {
        val parts = expr.split('|')
        val varPath = parts[0].trim()
        var value: Any? = resolver.resolve(varPath)
        for (mIdx in 1 until parts.size) {
            val mod = parts[mIdx].trim()
            value = PipeModifiers.apply(value, mod)
            if (value == null) break // 链中遇 null 中断
        }
        return value
    }

    /** 格式化值为字符串（列表用默认逗号，布尔 true/false）。 */
    private fun formatValue(value: Any?): String? {
        if (value == null) return null
        return when (value) {
            is List<*> -> value.joinToString(", ")
            is Boolean -> if (value) "true" else "false"
            else -> value.toString()
        }
    }

    /** 应用全局可视化选项：非法字符处理 + 词语分隔符 + 大小写。 */
    private fun applyGlobalOptions(s: String): String {
        // 非法文件名字符（路径分隔 / 已用于分段，此处不再替换 /）
        val cleaned = when (options.illegalCharHandling) {
            NamingOptions.IllegalCharHandling.REPLACE_DASH ->
                s.replace(Regex("[\\\\:*?\"<>|]"), "-")
            NamingOptions.IllegalCharHandling.REPLACE_UNDERSCORE ->
                s.replace(Regex("[\\\\:*?\"<>|]"), "_")
            NamingOptions.IllegalCharHandling.REMOVE ->
                s.replace(Regex("[\\\\:*?\"<>|]"), "")
        }
        // 词语分隔符：将空格替换为指定分隔符
        val separated = if (options.wordSeparator != ' ') {
            cleaned.replace(' ', options.wordSeparator)
        } else cleaned
        // 大小写
        return when (options.casing) {
            NamingOptions.Casing.AS_IS -> separated
            NamingOptions.Casing.LOWER -> separated.lowercase()
            NamingOptions.Casing.UPPER -> separated.uppercase()
            NamingOptions.Casing.TITLE -> separated.split(' ').joinToString(" ") {
                it.replaceFirstChar { ch -> ch.titlecase() }
            }.let {
                if (options.wordSeparator != ' ') it.replace(' ', options.wordSeparator) else it
            }
        }
    }

    private fun findClosingBrace(s: String, start: Int): Int {
        var depth = 0
        for (j in start until s.length) {
            when (s[j]) {
                '{' -> depth++
                '}' -> { depth--; if (depth == 0) return j }
            }
        }
        return -1
    }
}

/** 渲染结果。 */
data class RenderResult(
    val path: String,
    val warnings: List<String>,
)
