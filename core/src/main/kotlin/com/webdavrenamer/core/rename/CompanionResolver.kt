package com.webdavrenamer.core.rename

import com.webdavrenamer.core.webdav.MediaFileTypes
import com.webdavrenamer.core.webdav.WebDavClient

/**
 * 伴随文件发现器（计划 §5.2 伴随文件规则）。
 *
 * 对主文件所在目录 PROPFIND Depth 1，找出与主文件同名（去扩展名）且为伴随文件
 * （字幕/nfo/图片，由 [MediaFileTypes.isCompanion] 判定）的文件，按 [targetPath]
 * 同目录、主文件目标名去扩展名 + 伴随文件原扩展名 生成伴随重命名目标。
 *
 * 例：主文件 `/Movies/a.mkv` → 目标 `/target/a.mkv`，发现同目录 `a.srt`/`a.nfo`
 * → 伴随重命名 `/Movies/a.srt`→`/target/a.srt`、`/Movies/a.nfo`→`/target/a.nfo`。
 *
 * 调用方（预览页）可调用本类自动发现伴随文件；若已自行解析，可直接在 [RenameOperation]
 * 中传入 [CompanionRename] 列表，无需经过本类。
 */
class CompanionResolver(private val client: WebDavClient) {

    /**
     * 解析 [sourcePath] 主文件所在目录下的伴随文件并生成重命名目标。
     *
     * @param sourcePath 主文件源路径（如 `/Movies/a.mkv`）。
     * @param targetPath 主文件目标路径（如 `/target/a.mkv`）。
     * @return 伴随重命名列表（不含主文件自身、不含非同名文件、不含非伴随文件）。
     */
    suspend fun resolve(sourcePath: String, targetPath: String): List<CompanionRename> {
        val mainFileName = fileNameOf(sourcePath)
        val mainBase = baseNameWithoutExt(mainFileName) ?: return emptyList()
        val parentDir = parentDirOf(sourcePath)

        val entries = try {
            client.propfind(parentDir, 1)
        } catch (e: Exception) {
            return emptyList()
        }

        val targetDir = parentDirOf(targetPath)
        val targetMainBase = baseNameWithoutExt(fileNameOf(targetPath)) ?: mainBase

        val companions = mutableListOf<CompanionRename>()
        for (entry in entries) {
            if (entry.isCollection) continue
            val name = entry.displayName?.takeIf { it.isNotEmpty() }
                ?: entry.href.substringAfterLast('/').trimEnd('/').takeIf { it.isNotEmpty() }
                ?: continue
            // 跳过主文件自身。
            if (name == mainFileName) continue
            // 仅保留伴随文件（字幕/nfo/图片）。
            if (!MediaFileTypes.isCompanion(name)) continue
            val base = baseNameWithoutExt(name) ?: continue
            // 仅保留与主文件同名（去扩展名）的伴随文件。
            if (base != mainBase) continue
            val ext = rawExtension(name) ?: continue
            val compTarget = joinPath(targetDir, "$targetMainBase.$ext")
            companions.add(CompanionRename(sourcePath = joinPath(parentDir, name), targetPath = compTarget))
        }
        return companions
    }

    /** 取路径的文件名部分（最后一段）。 */
    private fun fileNameOf(path: String): String =
        path.substringAfterLast('/')

    /** 取路径的父目录。无 `/` 或仅根 `/` 时返回 `/`。 */
    private fun parentDirOf(path: String): String {
        val idx = path.lastIndexOf('/')
        return if (idx <= 0) "/" else path.substring(0, idx)
    }

    /** 拼接目录与文件名，保证恰好一个 `/` 分隔。 */
    private fun joinPath(dir: String, name: String): String =
        if (dir.endsWith("/")) "$dir$name" else "$dir/$name"

    /** 取文件名去扩展名部分（保留原大小写）。无扩展名返回 null。 */
    private fun baseNameWithoutExt(fileName: String): String? {
        if (fileName.isEmpty()) return null
        val name = fileName.substringAfterLast('/')
        val dot = name.lastIndexOf('.')
        if (dot <= 0) return null
        return name.substring(0, dot)
    }

    /** 取扩展名（保留原大小写，与 [MediaFileTypes.extension] 的区别是不小写化）。 */
    private fun rawExtension(fileName: String): String? {
        val name = fileName.substringAfterLast('/')
        val dot = name.lastIndexOf('.')
        if (dot < 0 || dot == name.length - 1) return null
        return name.substring(dot + 1)
    }
}
