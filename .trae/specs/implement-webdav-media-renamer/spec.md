# Android WebDAV 媒体文件重命名工具 Spec

> 本文档基于 `WebDAV_Media_Renamer_plan.md`，定义一款 Android 原生应用（手机版 FileBot）的完整规格。该应用面向 WebDAV 远程存储，使用 TMDB 元数据，对标 FileBot 的核心重命名能力、MiXplorer 的 WebDAV 浏览体验与 Infuse 的影院级深色 UI。

## Why

网盘里的影视文件命名杂乱（如 `The.Last.of.Us.S01E02.1080p.WEB-DL.x264-GROUP.mkv`），不符合 Plex/Kodi/Emby/Jellyfin 媒体库扫描规范，导致刮削失败、海报缺失、剧集错位。桌面端 FileBot 解决此问题但无法在手机上直接操作 WebDAV 远程文件。需要一个移动端工具：在手机上把 WebDAV 网盘里杂乱的影视文件按媒体库标准一键整理重命名，全程不下载文件内容、不引入脚本引擎、不依赖代理也能在中国大陆直连 TMDB。

## What Changes

本项目为全新开发（greenfield），从空仓库构建：

- **Android 工程**：Kotlin 100%、Jetpack Compose + Material 3（仅深色主题）、minSdk 26、targetSdk 最新。
- **WebDAV 客户端**：基于 OkHttp 自实现轻量客户端（PROPFIND/MOVE/MKCOL + Basic/Digest Auth 自动协商 + XmlPullParser 解析），不引入第三方 WebDAV 库。
- **TMDB 客户端**：Retrofit + kotlinx.serialization，限流（默认 40 req/10s）+ 429 退避重试；端点合并请求（`append_to_response`）。
- **文件名解析引擎**：纯 Kotlin，正则规则表驱动，输出 `ParsedFilename`。
- **匹配引擎**：标题相似度评分 + 年份加成 + 流行度微调；自动/手动决策阈值化。
- **命名规则引擎（重点：非 Groovy）**：自实现模板语法（变量 `{n}` + 管道修饰符 `{n|upper}` + 链式 + 路径分隔 `/`），实现 FileBot Binding Reference 全量变量（排除媒体流信息类/字幕/音乐/照片/外部数据源/CLI 类），内置 Plex/Kodi/Emby/Jellyfin 预设。
- **批量执行**：WebDAV MOVE 批量重命名 + MKCOL 建目录 + 伴随文件跟随 + 冲突检测 + WorkManager 后台队列 + 前台通知。
- **历史与撤销**：Room 持久化 `RenameBatch`/`RenameEntry`，整批反向 MOVE 撤销。
- **手动匹配工具**：Edit Match（单条修正 + 多集文件多选集）+ Episodes 面板（批量预制多集条目）+ 线性对齐模式。
- **设置备份**：JSON 导出/导入（含 AES-GCM 口令加密密码选项），先全量校验再落库。
- **自定义 Hosts**：OkHttp `Dns` 接口实现 TMDB 直连，候选 IP 自动测速，TLS SNI 与证书校验仍基于原域名。
- **Infuse 风格 UI**：深色影院级界面，海报墙网格，fanart 渐变遮罩详情页。
- **CI**：GitHub Actions 子分支 push 触发构建并推送 Pre-release。

### 硬性约束（红线，不可违背）

1. 不实现 Groovy 或任何动态脚本求值；模板语法仅限本规格 §5.5 定义范围。
2. 不接入 TMDB 以外的元数据 API。
3. 不对 WebDAV 文件做读取/下载内容的操作（除浏览必需的 PROPFIND 外）——重命名只通过 MOVE/MKCOL 完成。
4. 服务器密码必须经 Android Keystore 加密后存储，禁止明文落盘、禁止进入日志。
5. 所有破坏性操作（批量重命名、撤销）执行前必须有明确的预览/确认界面。

## Impact

- Affected specs: 无（全新项目）。
- Affected code: 全新代码库，主要模块：
  - `app/core/webdav/` — WebDAV 客户端
  - `app/core/tmdb/` — TMDB 客户端
  - `app/core/parser/` — 文件名解析
  - `app/core/naming/` — 模板引擎 + 预设
  - `app/core/matcher/` — 匹配引擎
  - `app/core/backup/` — 设置备份 + HostsDns
  - `app/data/db/` — Room（ServerConfig、RenameBatch、RenameEntry、TmdbCache）
  - `app/data/repository/`、`app/data/prefs/`
  - `app/worker/` — RenameWorker
  - `app/ui/` — servers/browser/match/preview/progress/history/settings/theme
- 关键设计要求：`FilenameParser`、`TemplateEngine`、`MatchEngine` 为纯 Kotlin 无 Android 依赖，便于 JVM 单元测试。

## ADDED Requirements

### Requirement: WebDAV 服务器管理

系统 SHALL 支持添加多个 WebDAV 服务器（别名、Base URL（http/https）、端口、根路径、用户名、密码），认证采用 Basic Auth 与 Digest Auth 自动协商（先无凭据发请求，按 401 的 `WWW-Authenticate` 头响应）。系统 SHALL 提供「测试连接」按钮（对根路径发 `PROPFIND Depth: 0`，反馈成功/失败原因）。服务器列表页以卡片展示，左滑编辑/删除，密码不回显（仅 `••••••`）。系统 SHALL 兼容 Nextcloud/ownCloud、Alist、坚果云、群晖 WebDAV Server、nginx/apache WebDAV。

#### Scenario: 测试连接成功
- **WHEN** 用户填写服务器信息并点击「测试连接」
- **THEN** 系统对根路径发 `PROPFIND Depth: 0` 并返回成功状态

#### Scenario: 认证自动协商
- **WHEN** 服务器要求 Digest Auth 且客户端未携带凭据
- **THEN** 系统读取 401 响应的 `WWW-Authenticate` 头并切换为 Digest 认证重试

#### Scenario: 密码不回显
- **WHEN** 用户查看服务器列表
- **THEN** 密码字段仅显示 `••••••` 占位，不展示明文

### Requirement: WebDAV 文件浏览（对齐 MiXplorer）

系统 SHALL 通过 `PROPFIND Depth: 1` 浏览目录，列表项显示图标（文件夹/视频/字幕/其他）、名称、大小、修改时间。系统 SHALL 提供面包屑导航（点击回跳任意层级）、系统返回键逐级回退、下拉刷新、按名称/大小/时间排序（升降序切换）、长按多选（全选/反选）。

#### Scenario: 选择规则——仅视频可选
- **WHEN** 用户浏览任意目录
- **THEN** 目录下所有类型文件（文件夹、视频、字幕、nfo、图片、杂项）均显示，但只有视频文件出现复选框且可勾选；非视频文件置灰、无复选框、仅可查看详情

#### Scenario: 伴随文件不可手动选中
- **WHEN** 用户尝试选中字幕/nfo/图片
- **THEN** 系统禁止选中；这些文件通过伴随文件机制跟随主视频自动处理

#### Scenario: 视频文件识别
- **WHEN** 系统识别文件类型
- **THEN** 视频扩展名（不区分大小写）含 `mkv, mp4, m4v, avi, mov, wmv, flv, ts, m2ts, webm, mpg, mpeg, rmvb, iso`；字幕扩展名含 `srt, ass, ssa, sub, idx`；`iso` 仅显示不参与重命名

### Requirement: 文件名解析引擎

系统 SHALL 输入任意文件名输出结构化 `ParsedFilename`（title、year、season、episodes、resolution、source、videoCodec、audioCodec、group）。系统 SHALL 支持季集模式：SxxExx、NxN、第X季第X集、独立集号、日期型剧集。系统 SHALL 按清洗规则处理（去扩展名、`.`/`_` 替换为空格、剔除技术标签尾巴、合并连续空格）。该引擎 MUST 为纯 Kotlin 无 Android 依赖。

#### Scenario: 多集文件解析
- **WHEN** 输入 `The.Last.of.Us.S01E01E02.1080p.WEB-DL.x264-GROUP.mkv`
- **THEN** title=`The Last of Us`、season=1、episodes=[1,2]、resolution=`1080p`、source=`WEB-DL`、videoCodec=`x264`、group=`GROUP`

#### Scenario: 中文季集模式
- **WHEN** 输入 `某剧.第1季第2集.mkv`
- **THEN** title=`某剧`、season=1、episodes=[2]

### Requirement: TMDB 匹配

系统 SHALL 使用 TMDB API v3（用户首次启动在设置页填入 API Key），所有请求带 `Accept: application/json`，尊重速率限制（默认 40 req/10s，客户端限流与 429 退避重试），语言偏好可配置（默认 `zh-CN`，备选 `en-US` 回退）。系统 SHALL 使用合并端点（`append_to_response`）减少调用。系统 SHALL 按匹配流程：解析→判型→搜索→评分→自动匹配（得分≥0.85 且次名分差≥0.1）或手动确认。匹配结果缓存到 Room（以 `tmdbId + language` 为键）。

#### Scenario: 自动匹配高置信度
- **WHEN** 视频文件解析后标题与 TMDB 搜索结果得分 ≥ 0.85 且与次名分差 ≥ 0.1
- **THEN** 直接采用并标记「自动」

#### Scenario: 低置信度进入待确认
- **WHEN** 得分低于阈值
- **THEN** 条目进入「待确认」状态，UI 展示候选列表（海报缩略图 + 名称 + 年份 + 简介首行）

#### Scenario: 多集文件标题合并
- **WHEN** 多集文件匹配后拉取对应季数据
- **THEN** 集号填充 episodeTitle/airDate，多集标题按 `A & B` 合并

### Requirement: 手动匹配与 Episodes 工具

系统 SHALL 提供 Edit Match（单条修正：切换电影/剧集搜索、季选择器 + 集列表、find-as-you-type 过滤）、多集文件多选集（连续或手动勾选，组合为多集条目，集号渲染 `S01E01-E02`，标题 `A & B` 合并）、Episodes 面板（浏览全季集列表、连续多选批量生成多集组合条目）、线性对齐模式（左侧文件列表与右侧集列表按顺序一一对齐，支持拖拽调整、单条解绑、确认后批量应用）。

#### Scenario: 多集文件多选集匹配
- **WHEN** 用户在 Edit Match 中对 `S01E01E02` 文件勾选集 1 与集 2
- **THEN** 组合为多集条目，预览渲染 `S01E01-E02`，标题合并

#### Scenario: 线性对齐批量匹配
- **WHEN** 用户进入线性对齐模式并调整文件与集的对应位置后确认
- **THEN** 批量应用为匹配结果

### Requirement: 命名规则引擎（非 Groovy）

系统 SHALL 不实现 Groovy。命名方案由 预设 + 模板字符串 + 可视化选项 三层组成。模板语法支持变量 `{n}`、管道修饰符 `{n|upper}`、链式 `{n|lower|space(_)}`、路径分隔 `/`。条件块（P2）可用「变量缺失时自动省略其所在的相邻括号组」简化规则替代。系统 SHALL 在最终输出前按用户设置替换或剔除非法文件名字符（`\/:*?"<>|`）。系统 SHALL 实现 FileBot Binding Reference 全量变量（A-G 组，排除媒体流信息类/字幕/音乐/照片/外部数据源/CLI 类）。系统 SHALL 实现管道修饰符（大小写、补零取整、字符替换、截取匹配、命名变换、清洗转写、列表）。系统 SHALL 提供可视化选项（词语分隔符、大小写、非法字符处理、补零位数）。系统 SHALL 内置 Plex/Kodi/Emby/Jellyfin 预设。系统 SHALL 对缺失变量容错（渲染失败/缺失时该段留空并清理多余分隔符，不允许输出 `{undefined}` 字面量）。该引擎 MUST 为纯 Kotlin 无 Android 依赖。

#### Scenario: 缺失变量容错
- **WHEN** 模板含未提供值的变量
- **THEN** 该段留空且清理多余分隔符，不输出 `{undefined}` 字面量

#### Scenario: 路径分隔建目录
- **WHEN** 模板含 `/`
- **THEN** 重命名时通过 MKCOL 建目录 + MOVE 实现目录层级

#### Scenario: 排除绑定渲染为空且警告
- **WHEN** 模板使用排除清单中的变量（如 `mediaTitle`）
- **THEN** 渲染为空并给出警告，不崩溃

#### Scenario: 实时预览
- **WHEN** 用户在模板编辑器编辑模板字符串
- **THEN** 用当前选中文件的解析结果即时渲染预览

### Requirement: 重命名预览与执行

系统 SHALL 提供预览页（每行：原路径小字灰色 → 新路径大字主题色，右侧状态图标 自动✅/待确认⚠️/冲突❌）。系统 SHALL 检测冲突（目标路径已存在或同批次内目标重名，标红，提供「自动加序号后缀」一键解决）。系统 SHALL 支持单条左滑排除、点击单条手动修改。系统 SHALL 按执行流程：按目标路径排序→MKCOL 建缺失目录（幂等，405 忽略）→逐个 MOVE（`Overwrite: F`）→伴随文件跟随→失败条目记录原因（403/404/409/412）→汇总报告→支持「重试失败项」。系统 SHALL 在 WorkManager 队列串行执行，前台通知显示进度（当前文件/总数 + 文件名），App 杀死后可恢复。系统 SHALL 每批次生成历史记录。

#### Scenario: 冲突标红且不可直接执行
- **WHEN** 目标路径已存在或同批次内目标重名
- **THEN** 预览阶段标红且不可直接执行，提供「自动加序号后缀」解决

#### Scenario: 伴随文件跟随改名
- **WHEN** 主视频文件 MOVE
- **THEN** 同名（去扩展名）的字幕、`.nfo`、海报图跟随主文件一并 MOVE

#### Scenario: 后台执行不中断
- **WHEN** App 退后台或被杀后重启
- **THEN** 任务在 WorkManager 队列中恢复，前台通知继续显示进度

### Requirement: 历史记录与撤销

系统 SHALL 用 Room 持久化 `RenameBatch`（时间、服务器、条目数、状态）+ `RenameEntry`（batchId、原路径、新路径、状态）。系统 SHALL 历史页按批次倒序展示并支持查看详情。系统 SHALL 对成功条目按相反顺序执行反向 MOVE（新路径→原路径），逐条确认远程状态，任一失败则中止并提示已回滚 N/M 条。系统 SHALL 将撤销操作本身记入历史（标记 revert 类型）。

#### Scenario: 整批撤销成功
- **WHEN** 用户对某批次点击撤销
- **THEN** 按相反顺序反向 MOVE，全部文件恢复原路径

#### Scenario: 撤销中途失败
- **WHEN** 撤销某条目时远程状态异常
- **THEN** 中止并提示「已回滚 N/M 条」

### Requirement: 设置备份与恢复（JSON 导出/导入）

系统 SHALL 一键导出全部配置为单个 JSON 文件（通过 SAF 选择保存位置），内容含备份格式版本号、导出时间、命名选项、当前预设与全部自定义模板、服务器列表（别名/URL/根路径/用户名）、TMDB API Key 与语言偏好、Hosts 配置；历史记录与 TMDB 缓存不纳入备份。系统 SHALL 处理密码：默认导出 `password` 为空串（导入后需重新输入），另提供「加密包含密码」选项（用户输入导出口令，AES-GCM + PBKDF2 派生密钥加密密码字段，导入时同口令解密）。系统 SHALL 导入时解析校验 schema 与版本兼容性（不兼容则明确报错）→展示变更预览（新增 N 项/覆盖 M 项）→用户确认后一次性写入，必须先全量校验通过再落库，导入失败不得破坏现有配置。JSON 顶层结构：`{ "formatVersion": 1, "exportedAt": "...", "app": "...", "settings": {...}, "servers": [...], "templates": [...], "hosts": [...] }`。

#### Scenario: 导出后清除数据再导入恢复
- **WHEN** 用户导出 JSON、清除应用数据后导入该 JSON
- **THEN** 完整恢复服务器列表（密码按选项需重新输入或口令解密）、命名选项、自定义模板与 Hosts 配置

#### Scenario: 导入失败不破坏现有配置
- **WHEN** 导入 JSON 校验失败
- **THEN** 现有配置保持不变

### Requirement: 自定义 Hosts（TMDB 直连）

系统 SHALL 在设置页「网络与 Hosts」为指定域名配置一条或多条静态 IP，内置针对 `api.themoviedb.org` 与 `image.tmdb.org` 的候选 IP 预设列表。系统 SHALL 支持自动测速（逐个 HTTPS 请求测延迟与可用性，自动选用最优并标注延迟）。系统 SHALL 实现 OkHttp `Dns` 接口（命中 hosts 表返回配置 IP，多条时轮询 + 失败切换，未命中走系统 DNS）。系统 SHALL 确保 TLS SNI 与证书校验仍基于原域名，严禁 trustAll 或关闭主机名校验。系统 SHALL 提供连接测试按钮（分别测试 API 与图片域名的连通性、HTTP 状态、延迟，可视化反馈）。系统 SHALL 提供 Hosts 总开关，配置随备份一并导出导入。

#### Scenario: DNS 污染下直连成功
- **WHEN** 模拟 DNS 污染（TMDB 域名指向无效解析）并配置 hosts 静态 IP
- **THEN** 连接测试通过，搜索与图片加载恢复正常

### Requirement: Infuse 风格 UI

系统 SHALL 仅深色主题（背景近黑 `#0D0D0F`、卡片面 `#1A1A1E`、主强调色暖琥珀/金色 `#E8A33D` 区间、文字三级灰阶 `#F5F5F7`/`#9A9AA3`/`#5C5C66`，Material 3 `darkColorScheme` 定制）。系统 SHALL 卡片圆角 12–16dp、海报圆角 8dp、阴影极轻、用亮度差分层。系统 SHALL 首页/匹配确认页用海报墙网格（2–3 列，`w342` 海报，懒加载 + 淡入过渡）。系统 SHALL 详情/确认页顶部 fanart（`w780`）全宽 + 渐变遮罩融入背景，标题/年份/评分/简介依次排布，填充胶囊主按钮 + 描边次按钮。系统 SHALL 文件浏览器保持高效工具感（列表行高密度、扁平单色图标、多选时情境操作栏）。系统 SHALL 过渡动画用 Compose 默认弹簧动画、页面切换 `slideInHorizontally`、海报加载 `crossfade(300)`。系统 SHALL 空状态居中插画占位 + 一句话引导。系统 SHALL 字体用系统默认，标题 `titleLarge`、正文 `bodyMedium`、路径 `Monospace` 小号。主要页面：服务器列表→添加/编辑服务器→文件浏览器（含多选）→匹配方式选择→自动匹配进行中→待确认列表 + 候选选择→Edit Match→Episodes 面板→重命名预览→执行进度→结果报告→历史记录→设置。

#### Scenario: 海报墙淡入过渡
- **WHEN** 用户进入首页/匹配确认页
- **THEN** 海报墙网格（2–3 列，`w342`）懒加载并淡入过渡

#### Scenario: fanart 渐变遮罩详情页
- **WHEN** 用户进入详情/确认页
- **THEN** 顶部 fanart（`w780`）全宽 + 从上到下渐变遮罩融入背景

### Requirement: 架构与模块约束

系统 SHALL 采用 MVVM + Repository + UseCase（ViewModel + StateFlow，单向数据流）。系统 SHALL 异步用 Kotlin Coroutines + Flow（不用 RxJava/Callback）。系统 SHALL 本地存储用 Room（历史记录、服务器配置加密存储）+ DataStore（偏好设置），密码用 Android Keystore 加密。系统 SHALL 图片加载用 Coil 3（Compose 集成，磁盘缓存）。系统 SHALL 后台任务用 WorkManager + 前台 Service 通知。系统 SHALL 依赖注入用 Hilt。系统 SHALL 构建用 Gradle Kotlin DSL + Version Catalog。系统 SHALL WebDAV 路径处理统一用 UTF-8 URL 编码工具类（`Destination` 头必须完整编码）。系统 SHALL 长列表用 Compose `LazyColumn`，PROPFIND 结果 >2000 项时分批加载。

### Requirement: CI（GitHub Actions）

系统 SHALL 配置 GitHub Actions：任意子分支 push 时触发构建并推送到 Pre-release。

#### Scenario: 子分支 push 触发构建
- **WHEN** 任意子分支 push
- **THEN** GitHub Actions 触发构建并推送 Pre-release

### Requirement: 单元测试覆盖

系统 SHALL 为 `FilenameParser` 与 `TemplateEngine` 提供单元测试 ≥ 30 个用例（含 §5.3 表格全部模式、中文文件名、多集文件、无年份电影）。系统 SHALL 为 §5.5 变量表 A–G 组每个变量提供对应单元测试用例（含缺失值容错渲染）。系统 SHALL 验证排除清单变量在模板中使用时渲染为空并给出警告、不崩溃。

## MODIFIED Requirements

无（全新项目）。

## REMOVED Requirements

无（全新项目）。
