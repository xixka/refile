# Checklist

> 对应计划 §8 验收标准与硬性约束。每条须在 M5 验收回归时逐条验证。

## WebDAV 与文件浏览

- [x] 1. 添加 Alist / Nextcloud WebDAV 服务器并成功浏览目录（含中文路径）。
- [x] 2. 文件浏览器显示目录下全部类型文件，但仅视频文件出现复选框且可勾选；字幕/nfo/图片置灰不可选。
- [x] 3. 视频扩展名（不区分大小写）识别正确：`mkv, mp4, m4v, avi, mov, wmv, flv, ts, m2ts, webm, mpg, mpeg, rmvb, iso`；`iso` 仅显示不参与重命名。
- [x] 4. 字幕扩展名识别正确：`srt, ass, ssa, sub, idx`，作为视频伴随文件跟随改名。
- [x] 5. 面包屑导航 + 系统返回键逐级回退 + 下拉刷新 + 排序（升降序切换）+ 多选（全选/反选）。
- [x] 6. 大目录（>2000 项）分批加载。

## 服务器管理与认证

- [x] 7. Basic Auth 与 Digest Auth 自动协商（先无凭据发请求，按 401 `WWW-Authenticate` 头响应）。
- [x] 8. 「测试连接」对根路径发 `PROPFIND Depth: 0` 并反馈成功/失败原因。
- [x] 9. 密码不回显（仅 `••••••`），经 Android Keystore 加密存储，禁止明文落盘、禁止进入日志。
- [x] 10. 服务器列表卡片页，左滑编辑/删除。

## 文件名解析引擎

- [x] 11. `FilenameParser` 为纯 Kotlin 无 Android 依赖。
- [x] 12. 支持季集模式：SxxExx、NxN、第X季第X集、独立集号、日期型剧集（全部忽略大小写）。
- [x] 13. `ParsedFilename` 字段完整（title/year/season/episodes/resolution/source/videoCodec/audioCodec/group）。
- [x] 14. 清洗规则正确（去扩展名、`.`/`_`→空格、剔除技术标签尾巴、合并连续空格）。
- [x] 15. `FilenameParser` 单元测试 ≥ 30 用例，覆盖全模式/中文/多集/无年份。

## TMDB 匹配

- [x] 16. 用户首次启动在设置页引导填入 API Key（提供申请指引链接）。
- [x] 17. 所有请求带 `Accept: application/json`，尊重速率限制（40 req/10s）+ 429 退避重试。
- [x] 18. 语言偏好可配置（默认 `zh-CN`，备选 `en-US` 回退）。
- [x] 19. 端点合并请求（`append_to_response`：电影 `credits,external_ids,alternative_titles,translations,release_dates`；剧集 `credits,external_ids,alternative_titles,translations,content_ratings,episode_groups`）。
- [x] 20. 图片基址 `https://image.tmdb.org/t/p/`：w342（海报墙）、w780（背景）、original（详情页按需）。
- [x] 21. 自动匹配阈值：得分 ≥ 0.85 且次名分差 ≥ 0.1；低于则进待确认。
- [x] 22. 多集文件标题合并 `A & B`。
- [x] 23. 匹配结果缓存到 Room（`tmdbId + language` 为键）。
- [ ] 24. 50 个混杂命名剧集文件目录自动匹配正确率 ≥ 90%。

## 手动匹配与 Episodes 工具

- [x] 25. Edit Match 单条修正：切换电影/剧集搜索、季选择器 + 集列表、find-as-you-type 过滤。
- [x] 26. 多集文件多选集（连续/手动勾选），组合条目集号渲染 `S01E01-E02`，标题 `A & B`。
- [x] 27. Episodes 面板：浏览全季集列表、连续多选批量生成多集组合条目。
- [x] 28. 线性对齐模式：文件列表与集列表顺序对齐、拖拽调整、单条解绑、批量应用。

## 命名规则引擎（非 Groovy）

- [x] 29. 整个 App 无 Groovy / 脚本引擎依赖。
- [x] 30. 模板语法支持变量 `{n}`、管道修饰 `{n|upper}`、链式 `{n|lower|space(_)}`、路径分隔 `/`。
- [x] 31. 变量表 A–G 组全量实现（除排除清单外）。
- [x] 32. 管道修饰符全量实现（大小写/补零取整/字符替换/截取匹配/命名变换/清洗转写/列表）。
- [x] 33. 内置预设 Plex（默认）/Kodi/Emby/Jellyfin，电影与剧集模板正确。
- [x] 34. 模板可「另存为自定义模板」，编辑器提供变量插入按钮 + 实时预览。
- [x] 35. 缺失变量容错：渲染失败/缺失时该段留空并清理多余分隔符，不输出 `{undefined}` 字面量。
- [x] 36. 排除绑定在模板中使用时渲染为空并给出警告，不崩溃。
- [x] 37. 非法文件名字符（`\/:*?"<>|`）按用户设置替换或剔除。
- [x] 38. 可视化选项（分隔符/大小写/非法字符处理/补零位数）作用于所有预设。
- [x] 39. `TemplateEngine` 为纯 Kotlin 无 Android 依赖。
- [x] 40. `TemplateEngine` 单元测试覆盖 A–G 组每个变量（含缺失值容错渲染）。

## 重命名预览与执行

- [x] 41. 预览页对照列表：原路径小字灰色 → 新路径大字主题色，状态图标（自动✅/待确认⚠️/冲突❌）。
- [x] 42. 冲突检测：目标路径已存在或同批次内重名，标红且不可直接执行，提供「自动加序号后缀」一键解决。
- [x] 43. 单条左滑可从批次排除；点击单条可手动修改新文件名。
- [x] 44. 执行流程：按目标路径排序 → MKCOL 建缺失目录（幂等，405 忽略） → 逐个 MOVE（`Overwrite: F`） → 伴随文件跟随。
- [x] 45. 失败条目记录原因（403/404/409/412），汇总报告，支持「重试失败项」。
- [x] 46. WorkManager 串行队列 + 前台通知（当前文件/总数 + 文件名），App 退后台不中断、杀后可恢复。
- [ ] 47. 50 个文件重命名全程有进度通知，同名字幕文件跟随改名。
- [x] 48. Emby 预设预览：剧集新路径 `TV Shows/{剧名}/Season {ss}/{剧名} - S{s00}E{e00} - {单集标题}.mkv`，电影 `Movies/{电影名} ({年份})/{电影名} ({年份}).mkv`。

## 历史记录与撤销

- [x] 49. Room 持久化 `RenameBatch`（时间/服务器/条目数/状态）+ `RenameEntry`（batchId/原路径/新路径/状态）。
- [x] 50. 历史页按批次倒序展示，可查看详情。
- [x] 51. 整批撤销：对成功条目按相反顺序反向 MOVE，逐条确认远程状态。
- [x] 52. 撤销中途失败中止并提示「已回滚 N/M 条」。
- [x] 53. 撤销操作本身记入历史（标记 revert 类型）。
- [x] 54. 批次完成后历史记录中一键撤销，全部文件恢复原路径。

## 设置备份与恢复

- [x] 55. 一键导出全部配置为单个 JSON（SAF 选择保存位置），含 formatVersion/exportedAt/app/settings/servers/templates/hosts；历史记录与 TMDB 缓存不纳入。
- [x] 56. 密码处理：默认 `password` 为空串（导入后需重新输入）；「加密包含密码」选项用 AES-GCM + PBKDF2 派生密钥，导入同口令解密。
- [x] 57. 导入：解析校验 schema 与版本兼容性（不兼容明确报错）→ 变更预览（新增 N/覆盖 M）→ 全量校验通过再落库，失败不破坏现有配置。
- [x] 58. 导出后清除应用数据再导入，可完整恢复服务器列表（密码按选项重新输入或口令解密）、命名选项、自定义模板与 Hosts 配置。

## 自定义 Hosts（TMDB 直连）

- [x] 59. 设置页「网络与 Hosts」可为指定域名配置一条或多条静态 IP。
- [x] 60. 内置 `api.themoviedb.org` 与 `image.tmdb.org` 候选 IP 预设列表。
- [x] 61. 自动测速：逐个 HTTPS 请求测延迟与可用性，自动选用最优并标注延迟。
- [x] 62. OkHttp `Dns` 接口实现：命中 hosts 返回 IP（多条轮询 + 失败切换），未命中走系统 DNS。
- [x] 63. TLS SNI 与证书校验仍基于原域名，严禁 trustAll 或关闭主机名校验。
- [x] 64. 连接测试按钮：分别测 API 与图片域名连通性/HTTP 状态/延迟，可视化反馈。
- [x] 65. Hosts 总开关，配置随备份导出导入。
- [ ] 66. DNS 污染环境下配置 hosts 静态 IP 后连接测试通过，搜索与图片加载恢复正常。

## UI / UX（Infuse 风格）

- [x] 67. 仅深色主题（背景 `#0D0D0F`、卡片 `#1A1A1E`、强调色暖琥珀 `#E8A33D` 区间、文字三级灰阶 `#F5F5F7`/`#9A9AA3`/`#5C5C66`，Material 3 `darkColorScheme` 定制）。
- [x] 68. 卡片圆角 12–16dp、海报圆角 8dp、阴影极轻、用亮度差分层。
- [x] 69. 首页/匹配确认页海报墙网格（2–3 列，`w342`，懒加载 + 淡入过渡）。
- [x] 70. 详情/确认页顶部 fanart（`w780`）全宽 + 渐变遮罩融入背景，填充胶囊主按钮 + 描边次按钮。
- [x] 71. 文件浏览器高效工具感（列表行高密度、扁平单色图标、多选情境操作栏）。
- [x] 72. 过渡动画：Compose 默认弹簧、页面 `slideInHorizontally`、海报 `crossfade(300)`。
- [x] 73. 空状态居中插画占位 + 一句话引导。
- [x] 74. 字体：标题 `titleLarge`、正文 `bodyMedium`、路径 `Monospace` 小号。

## 架构与技术栈

- [x] 75. Kotlin 100%，minSdk 26，Jetpack Compose + Material 3，MVVM + Repository + UseCase（ViewModel + StateFlow）。
- [x] 76. Coroutines + Flow（不用 RxJava/Callback）。
- [x] 77. Room（历史记录、服务器配置加密存储）+ DataStore（偏好设置）。
- [x] 78. Coil 3 图片加载（Compose 集成，磁盘缓存）。
- [x] 79. WorkManager 后台任务 + 前台 Service 通知。
- [x] 80. Hilt 依赖注入，Gradle Kotlin DSL + Version Catalog。
- [x] 81. WebDAV 路径统一用 UTF-8 URL 编码工具类，`Destination` 头完整编码。
- [x] 82. 元数据请求只发往 `api.themoviedb.org` 与 `image.tmdb.org`。
- [x] 83. 不对 WebDAV 文件做读取/下载内容操作（除浏览必需 PROPFIND 外），重命名只通过 MOVE/MKCOL。
- [x] 84. 所有破坏性操作（批量重命名、撤销）执行前有明确预览/确认界面。

## CI

- [x] 85. GitHub Actions：任意子分支 push 触发构建并推送到 Pre-release。
