# 开发提示词：Android WebDAV 媒体文件重命名工具（手机版 FileBot）

> 本文档是给 AI 编码助手（Claude Code / Cursor 等）的完整开发指令。请严格按照本文档的范围、技术栈、功能规格与验收标准实施开发。遇到本文档未覆盖的决策点，优先选择更简单、更符合 Android 平台习惯的方案，并在代码注释中说明取舍理由。

---

## 1. 项目概述

开发一款 **Android 原生应用**：一个运行在手机上、专门面向 **WebDAV 远程存储** 的影视媒体文件批量重命名工具。产品对标桌面软件 [FileBot](https://www.filebot.net/) 的核心重命名能力，但做了明确的移动端裁剪：

- **操作对象只有 WebDAV 远程文件**（不管理本地文件、不支持 SMB/FTP 等其他协议）。
- **元数据源只有 TMDB**（TheMovieDB），不做 TheTVDB / AniDB / OMDb。
- **命名规则实现 FileBot 的 naming scheme 能力**（内置预设 + 模板变量），但 **不引入 Groovy 表达式引擎**，改用自实现的轻量模板系统（占位符 + 管道修饰符）。
- **文件浏览体验对标 MiXplorer** 的 WebDAV 管理：流畅的目录导航、多选、任务队列。
- **UI 风格参考 Infuse**：深色影院级界面、海报墙、沉浸式元数据展示。

一句话定位：**「在手机上把 WebDAV 网盘里杂乱的影视文件，按 Plex/Kodi/Emby 标准一键整理重命名」**。

---

## 2. 产品范围

### 2.1 In Scope（必须实现）

| 模块 | 说明 |
|---|---|
| WebDAV 服务器管理 | 多服务器配置（名称、URL、用户名、密码、HTTPS），连接测试，编辑/删除 |
| WebDAV 文件浏览 | 目录导航、面包屑、排序、多选、刷新，体验对齐 MiXplorer |
| 文件名解析 | 从文件名提取标题、年份、季/集号、分辨率、来源、编码、发布组等 |
| TMDB 匹配 | 自动匹配（高置信度）+ 手动搜索确认（低置信度），支持电影和剧集 |
| 命名规则引擎 | 内置 Plex/Kodi/Emby/Jellyfin 预设 + 可视化模板编辑器（非 Groovy） |
| 重命名预览 | 旧文件名 → 新文件名对照列表，冲突标红，逐条可排除 |
| 批量执行 | WebDAV MOVE 批量重命名（含建目录），后台任务队列 + 进度通知 |
| 历史与撤销 | 每次批量操作落库，支持整批撤销（反向 MOVE） |
| 手动匹配 / Episodes 工具 | Edit Match 手动改选匹配、多集文件多选集、Episodes 面板预制多集条目、线性对齐匹配 |
| 设置备份 | 全部设置导出为 JSON 文件 / 从 JSON 导入恢复 |
| 自定义 Hosts | 为 TMDB 域名配置静态 IP + 自动测速，保证中国大陆网络可直连 |

### 2.2 Out of Scope（明确不做）

- ❌ Groovy / 任意脚本表达式引擎（安全与复杂度原因，用固定模板语法替代）
- ❌ TheTVDB、AniDB、OMDb、Fanart.tv 等 TMDB 以外的元数据源
- ❌ 字幕下载（OpenSubtitles）、文件哈希校验、压缩包处理
- ❌ 音乐（ID3）、照片（EXIF）重命名
- ❌ 本地文件系统、SMB/FTP/SFTP 等非 WebDAV 协议
- ❌ 文件内容读取 / MediaInfo 解析（远程文件无法低成本读取流信息，媒体信息只从文件名解析）
- ❌ 视频播放功能
- ❌ CLI / 自动化脚本接口

---

## 3. 参考资料

开发时可参考以下资源理解逻辑，**不得直接搬运代码**（许可证与语言均不同）：

1. **FileBot 官方**：https://www.filebot.net/ — 理解产品交互流程：拖入文件 → 选择数据源匹配 → 预览 New Names → Rename。
2. **命名规则**：https://www.filebot.net/naming.html — Format Expressions 文档。本项目只吸收其中的「内置预设」与「绑定变量」概念，语法重新设计（见 §6）。
3. **参考源码**：https://github.com/mobeigi/filebot — FileBot 4.8.0 官方源码的存档 fork（Java）。重点参考其思路而非代码：
   - `source/net/filebot/media/` — 媒体文件解析与释放信息（release info）检测思路
   - `source/net/filebot/similarity/` — 标题相似度匹配算法思路
   - `source/net/filebot/web/TheMovieDB*` — TMDB 客户端的数据模型
4. **MiXplorer**（Android 文件管理器）— 交互参考：服务器书签、面包屑导航、多选操作、后台任务队列。
5. **Infuse**（Firecore 出品的视频应用）— UI 参考：深色主题、海报墙网格、沉浸式详情页、渐变遮罩 + 模糊 fanart 背景。
6. **TMDB API v3 文档**：https://developer.themoviedb.org/docs

---

## 4. 技术栈（固定选型，不要偏离）

| 层 | 选型 | 说明 |
|---|---|---|
| 语言 | **Kotlin**（100%） | minSdk 26，targetSdk 最新 |
| UI | **Jetpack Compose + Material 3** | 深色主题定制（见 §7）；不使用 View 体系 |
| 架构 | MVVM + Repository + UseCase | `ViewModel` + `StateFlow`，单向数据流 |
| 异步 | Kotlin Coroutines + Flow | 不用 RxJava / Callback |
| 网络 | OkHttp + Retrofit + kotlinx.serialization | TMDB REST API |
| WebDAV | **基于 OkHttp 自实现轻量 WebDAV 客户端** | WebDAV 只是 HTTP 扩展（PROPFIND/MOVE/MKCOL），自实现可控且无第三方库维护风险；XML 解析用 `XmlPullParser` |
| 本地存储 | **Room**（历史记录、服务器配置加密存储）+ DataStore（偏好设置） | 密码用 Android Keystore 加密 |
| 图片加载 | Coil 3（Compose 集成） | TMDB 海报/背景图，磁盘缓存 |
| 后台任务 | WorkManager（批量重命名队列）+ 前台 Service 通知进度 | 保证 App 退后台后任务不中断 |
| 依赖注入 | Hilt | |
| 构建 | Gradle Kotlin DSL + Version Catalog | |

---

## 5. 功能需求详述

### 5.1 WebDAV 服务器管理

- 支持添加多个服务器：别名、Base URL（http/https）、端口、根路径、用户名、密码。
- 认证方式：Basic Auth 与 Digest Auth 自动协商（先无凭据发请求，按 401 的 `WWW-Authenticate` 头响应）。
- 「测试连接」按钮：对根路径发 `PROPFIND Depth: 0`，反馈成功/失败原因（网络、认证、非 WebDAV 服务）。
- 服务器列表页：卡片展示，左滑编辑/删除；密码不回显，仅显示 `••••••`。
- 兼容常见服务：Nextcloud/ownCloud、Alist、坚果云、群晖 WebDAV Server、nginx/apache WebDAV。

### 5.2 WebDAV 文件浏览（对齐 MiXplorer 体验）

- 目录浏览：`PROPFIND Depth: 1` 拉取当前目录，列表项显示图标（文件夹/视频/字幕/其他）、名称、大小、修改时间。
- 导航：顶部面包屑可点击回跳任意层级；系统返回键逐级回退；支持下拉刷新。
- 视图：列表视图为主，支持按名称/大小/时间排序（升降序切换）。
- 多选模式：长按进入多选，显示选中计数；支持全选、反选。
- **选择规则（重要）**：浏览器显示目录下**所有类型文件**（文件夹、视频、字幕、nfo、图片、其他杂项，不做隐藏过滤），但**只有视频文件可以被勾选选中**；非视频文件显示为置灰状态、不显示复选框，点击仅可查看详情不可选中；字幕/nfo/图片通过伴随文件机制跟随主视频自动处理，无需也不允许手动选中。
- 视频文件识别扩展名（不区分大小写）：
  `mkv, mp4, m4v, avi, mov, wmv, flv, ts, m2ts, webm, mpg, mpeg, rmvb, iso`（`iso` 默认不参与重命名，仅显示）
- 字幕文件识别（作为视频的伴随文件一起改名）：`srt, ass, ssa, sub, idx`
- 伴随文件规则：与视频文件同名（去扩展名）的字幕、`.nfo`、海报图，重命名时跟随主文件一并改名。

### 5.3 文件名解析引擎（Filename Parser）

输入任意文件名，输出结构化解析结果 `ParsedFilename`：

```
title: String?        // 清洗后的标题，如 "The Last of Us"
year: Int?            // 1900-2099 之间的四位年份
season: Int?          // 季号
episodes: List<Int>   // 集号（支持多集 S01E01E02）
resolution: String?   // 480p/720p/1080p/2160p/4K
source: String?       // BluRay/WEB-DL/WEBRip/HDTV/DVDRip/Remux...
videoCodec: String?   // x264/x265/h264/h265/HEVC/AV1...
audioCodec: String?   // AAC/AC3/DTS/Atmos/TrueHD...
group: String?        // 发布组（文件名末尾 -GROUP 或 [GROUP]）
```

必须支持的季集模式（正则，全部忽略大小写）：

| 模式 | 示例 |
|---|---|
| SxxExx | `S01E02`、`s1e2`、`S01E01E02`、`S01E01-E03` |
| NxN | `1x02` |
| 第X季第X集 | `第1季第2集`、`第02集` |
| 独立集号 | `E02`、`EP02`、`[02]`（需结合上下文避免误判年份/分辨率） |
| 日期型剧集 | `2024.01.15`、`2024-01-15`（对应 daily show，TMDB 可按日期匹配） |

清洗规则（对标 FileBot 的 release name 清洗思路）：

1. 去扩展名；将 `.`、`_` 替换为空格（但保留年份与小数点场景的判断）。
2. 剔除方括号/圆括号内的发布信息（分辨率、编码、组名、站点名），但保留含年份的圆括号。
3. 剔除连续技术标签串（`720p BluRay x264 AAC-Group` 这种从首个技术标签到结尾的尾巴），剩余部分作为标题候选。
4. 标题首尾去空格、合并连续空格。

### 5.4 TMDB 匹配

**API 使用**：TMDB API v3，用户首次启动时在设置页引导填入自己的 API Key（提供申请指引链接）；所有请求带 `Accept: application/json`，尊重速率限制（默认 40 req/10s，客户端做限流与 429 退避重试）。语言偏好可配置（默认 `zh-CN`，备选 `en-US` 回退）。

**用到的端点**：

- `GET /search/movie` / `GET /search/tv`（query, year/first_air_date_year, language）
- `GET /movie/{id}`、`GET /tv/{id}`（详情；必须用 `append_to_response=credits,external_ids,alternative_titles,translations,release_dates`（电影）/ `credits,external_ids,alternative_titles,translations,content_ratings,episode_groups`（剧集）合并请求，减少调用次数）
- `GET /tv/{id}/season/{season_number}`（整季集列表：集号、标题、播出日期、单集 runtime）
- `GET /tv/{id}/episode_group/{id}`（仅当模板使用 `{order.*}` 变量时按需拉取）
- `GET /collection/{id}`（仅电影有 `belongs_to_collection` 且模板使用 `ci`/`cy` 时按需拉取）
- 图片基址 `https://image.tmdb.org/t/p/`：`w342`（海报墙）、`w780`（背景）、`original`（仅详情页按需）

**匹配流程**：

1. 对每个视频文件运行 §5.3 解析。
2. 判定类型：有季/集信息 → 剧集搜索；否则 → 电影搜索（也可让用户强制指定当前目录的整体类型）。
3. 用 `title (+year)` 调搜索接口，计算候选得分：标题相似度（归一化后的编辑距离 / token 重合度）× 权重 + 年份一致加成 + 流行度微调。
4. **自动匹配**：得分 ≥ 阈值（如 0.85）且与次名分差 ≥ 0.1 → 直接采用，标记「自动」。
5. **手动确认**：低于阈值 → 条目进入「待确认」状态，UI 展示候选列表（海报缩略图 + 名称 + 年份 + 简介首行），用户点选或手动搜索关键词。
6. 剧集匹配后：拉取对应季数据，按集号填充 `episodeTitle / airDate`；多集文件合并标题（`Title A & Title B`）。
7. 匹配结果缓存到 Room（以 `tmdbId + language` 为键），避免重复请求。

**手动匹配与 Episodes 工具**（对标 FileBot 的 Edit Match / Episodes 面板，参考 https://www.filebot.net/forums/viewtopic.php?t=13391）：

- **Edit Match（单条修正）**：待确认页/预览页点击任一条目 → 进入手动匹配页。可切换电影/剧集搜索；剧集模式展示季选择器 + 集列表（集号、标题、播出日期、剧照），点选正确集完成修正；顶部搜索框支持键入过滤集标题/集号（等价 FileBot 的 find-as-you-type）。
- **多集文件多选**：多集文件（如 `S01E01E02` 解析出多个集号）在 Edit Match 中允许**选择多个集**（连续或手动勾选），组合为一个多集条目，集号渲染形如 `S01E01-E02`，多集标题按 `A & B` 合并（与 §5.4-6 规则一致）。
- **Episodes 工具（批量预制多集条目）**：选定某剧集后，可进入 Episodes 面板浏览该剧全部季/集列表；支持连续多选批量生成「双集/三集/多集组合条目」，用于与多集文件一一对应匹配。
- **线性对齐模式（Linear Matching）**：批量手动匹配场景下，左侧文件列表与右侧集列表按顺序一一对齐；支持拖拽手柄调整对齐位置、单条解绑；确认后批量应用为匹配结果。

### 5.5 命名规则引擎（重点：非 Groovy）

不实现 Groovy。命名方案由 **预设 + 模板字符串 + 可视化选项** 三层组成。

**模板语法**（自实现，递归下降或正则替换均可，但必须支持转义与容错）：

- 变量：`{变量名}`，如 `{n}` `{y}` `{s00e00}` `{t}`
- 管道修饰符：`{n|upper}`、`{e|pad(3)}`，可链式 `{n|lower|space(_)}`
- 条件块（可选，P2 实现）：`{?year?}({y}){/?}` 仅在 year 存在时输出——若实现成本高，可用「变量缺失时自动省略其所在的相邻括号组」的简化规则替代
- 路径分隔：模板中的 `/` 表示目录层级（重命名 = 移动+改名，通过 `MKCOL` 建目录 + `MOVE` 实现）
- 非法文件名字符（`\/:*?"<>|`）在最终输出前自动按用户设置替换或剔除

**变量表（全量实现 FileBot Binding Reference）**

实现原则：**除「媒体流信息类」绑定（需读取文件二进制内容，本项目不做 MediaInfo，见文末排除清单）外，FileBot 官方 Binding Reference 中的变量必须全部实现**。每个变量标注数据来源，共四类：`TMDB`（接口字段）、`文件名`（§5.3 解析结果）、`WebDAV`（PROPFIND 属性）、`上下文`（匹配/批次运行时计算）。

**A. 匹配对象与通用绑定**

| 变量 | 含义 | 示例 | 来源 |
|---|---|---|---|
| `n` | 电影名 / 剧名 | `The Last of Us` | TMDB `title` / `name` |
| `y` | 年份 | `2023` | TMDB `release_date` / `first_air_date` |
| `ny` | 名称（年份）组合 | `The Last of Us (2023)` | 组合绑定 |
| `id` | 条目 ID | `100088` | TMDB `id` |
| `tmdbid` | TMDB ID | `100088` | TMDB `id` |
| `imdbid` | IMDb ID | `tt3581920` | TMDB `external_ids.imdb_id` |
| `tvdbid` | TheTVDB ID（仅剧集可用，电影渲染为空） | `392256` | TMDB TV `external_ids.tvdb_id` |
| `primaryTitle` | 原始语言标题 | `Juuni Kokuki` | TMDB `original_title` / `original_name` |
| `alias` | 别名列表 | `[十二国记, ...]` | TMDB `alternative_titles` |
| `object` | 匹配对象（默认渲染为预格式化串） | `Firefly - 1x01 - Serenity` | 上下文 |
| `type` | 对象类型 | `Movie` / `Episode` | 上下文 |
| `episode` | 剧集对象（渲染为 `n - sxe - t` 格式串） | `Firefly - 1x01 - Serenity` | 上下文 |
| `series` | 系列对象（渲染为 `n (y)` 格式串） | `Firefly (2002)` | 上下文 |
| `movie` | 电影对象（渲染为 `n (y)` 格式串） | `Avatar (2009)` | 上下文 |

**B. 剧集绑定**

| 变量 | 含义 | 示例 | 来源 |
|---|---|---|---|
| `s` | 季号 | `3` | TMDB `season_number` |
| `e` | 集号 | `1` | TMDB `episode_number` |
| `es` | 集号列表（多集文件） | `[1, 2, 3]` | 文件名解析 + TMDB 校验 |
| `sxe` | 季x集 | `1x01` | 组合绑定 |
| `s00e00` | S季E集（补零） | `S01E01` | 组合绑定 |
| `s00` / `e00` | 补零季号 / 集号（便捷绑定，等效 `{s\|pad(2)}`） | `01` | 组合绑定 |
| `t` | 单集标题（多集合并为 `A & B`） | `Labyrinth` | TMDB episode `name` |
| `d` | 单集播出日期（默认 ISO，格式可在设置中改） | `2023-01-29` | TMDB episode `air_date` |
| `airdate` | `d` 的别名，二者必须都支持 | `2023-01-29` | 同上 |
| `startdate` | 剧集开播日期 | `2002-09-20` | TMDB `first_air_date` |
| `absolute` | 绝对集号 | `42` | 上下文：按 TMDB 各季常规集数累加计算（跳过 Season 0 特典；顺序为 S1E1→末集、S2E1→…） |
| `sn` | 季名 | `Wano Country Arc` | TMDB season `name` |
| `sy` | 季年份列表 | `[2002, 2003]` | TMDB 各季 `air_date` |
| `sc` | 总季数 | `5` | TMDB `number_of_seasons` |
| `special` | 特典号（Season 0 内的集号，非常规集时有效） | `1` | TMDB Season 0 |
| `regular` | 是否常规集（季号 > 0） | `true` | 上下文 |
| `anime` | 是否动画剧集 | `false` | 上下文启发式：`origin_country` 含 JP 且 `genres` 含 Animation；可在设置中关闭此判断 |
| `episodelist` | 系列集列表上下文（渲染为当季集数摘要，供调试模板） | `[1x01, 1x02, ...]` | TMDB season 数据 |

**C. 影视元数据绑定（电影全部适用；标注 ▸ 的剧集同样适用）**

| 变量 | 含义 | 示例 | 来源 |
|---|---|---|---|
| `collection` | 电影所属系列 | `Avatar Collection` | TMDB `belongs_to_collection.name` |
| `ci` | 系列内序号（按上映日期排序，从 1 起） | `1` | TMDB `/collection/{id}` |
| `cy` | 系列作品年份列表 | `[2009, 2022]` | TMDB `/collection/{id}` |
| `decade` | 年代（y 向下取整到十年） | `1970` | 上下文计算 |
| `genre` ▸ | 主类型（首个） | `Science Fiction` | TMDB `genres[0]` |
| `genres` ▸ | 全部类型列表 | `[Sci-Fi, Drama]` | TMDB `genres` |
| `language` ▸ | 原始语言 | `eng` | TMDB `original_language` |
| `languages` ▸ | 对白语言列表 | `[eng]` | TMDB `spoken_languages` |
| `country` ▸ | 原产国（首个） | `US` | TMDB `origin_country` / `production_countries` |
| `runtime` ▸ | 时长（分钟；剧集取 `episode_run_time` 或单集 `runtime`） | `162` | TMDB |
| `certification` ▸ | 内容分级（按设置的首选地区，默认 US） | `PG-13` | TMDB `release_dates` / `content_ratings` |
| `rating` ▸ | 评分 | `7.4` | TMDB `vote_average` |
| `votes` ▸ | 评分数 | `17720` | TMDB `vote_count` |
| `director` | 导演（剧集渲染为空或取剧集创作者，取 `created_by`） | `James Cameron` | TMDB `credits` / `created_by` |
| `actors` ▸ | 主演列表（默认前 5，可 `{actors\|joining(,)}` 控制） | `[Zoe Saldana, ...]` | TMDB `credits.cast` |

**D. 批次与序号绑定**

| 变量 | 含义 | 示例 | 来源 |
|---|---|---|---|
| `pi` | 分片序号（CD1/Part1 等多分片文件） | `1` | 文件名解析（`cd1`/`part1`/`disc1` 模式） |
| `pc` | 分片总数 | `2` | 上下文：同组分片计数 |
| `di` | 重名序号（同批次目标路径重复时从 1 编号） | `1` | 上下文：批次内去重 |
| `dc` | 重名总数 | `2` | 上下文 |
| `az` | 排序字母（`n` 去冠词后首字母，用于按字母分目录） | `A` | 上下文计算（`sortName` 规则） |

**E. 文件与路径绑定（远程文件属性，不读文件内容）**

| 变量 | 含义 | 示例 | 来源 |
|---|---|---|---|
| `fn` | 当前文件名（去扩展名） | `Serenity` | WebDAV `displayname` |
| `ext` | 扩展名（引擎自动附加，模板中无需写） | `mkv` | 文件名 |
| `f` | 完整远程路径 | `/library/a.mkv` | WebDAV |
| `folder` | 所在目录路径 | `/library` | WebDAV |
| `drive` | 库根路径（服务器根路径） | `/` | 服务器配置 |
| `files` | 本批次文件组（渲染为数量摘要） | `[3 files]` | 上下文 |
| `relativeFile` | 相对库根的路径 | `shows/a.mkv` | 上下文计算 |
| `mediaFile` | 主媒体文件路径（伴随文件组中的主文件） | `/library/a.mkv` | 上下文 |
| `mediaFileName` | 主媒体文件名（去扩展名） | `Serenity` | 上下文 |
| `original` | 重命名前的原始文件名（去扩展名） | `Serenity` | 上下文 |
| `historic` | 原始路径绑定组（`{historic.f}` 等，供撤销记录） | — | 上下文 |
| `ct` | 文件修改日期 | `2026-07-20` | WebDAV `getlastmodified`（`creationdate` 回退） |
| `age` | 文件年龄（天） | `7` | 上下文：`today - ct` |
| `bytes` | 文件大小（人性化格式） | `356 MB` | WebDAV `getcontentlength` |
| `megabytes` | 文件大小（MB） | `356 MB` | 同上 |
| `gigabytes` | 文件大小（GB） | `0.4 GB` | 同上 |
| `today` | 当前日期 | `2026-07-23` | 系统 |

**F. 技术标签绑定（来源 = 文件名解析，而非文件内容）**

FileBot 中这些值由 MediaInfo 读取；本项目从文件名解析（§5.3），解析不到则为空：

| 变量 | 含义 | 示例 | 来源 |
|---|---|---|---|
| `vf` | 标准分辨率 | `1080p` | 文件名 |
| `vc` | 视频编码 | `x264` | 文件名 |
| `ac` | 音频编码 | `ac3` | 文件名 |
| `cf` | 容器格式（= `ext`） | `mkv` | 文件名 |
| `vs` | 来源类别（归一化） | `BluRay` | 文件名 |
| `source` | 来源原始匹配 | `BD25` | 文件名 |
| `edition` | 版本（导演剪辑/加长版等） | `Extended Edition` | 文件名 |
| `tags` | 版本标签列表 | `[Extended Edition]` | 文件名 |
| `s3d` | 3D 标签 | `3D SBS` | 文件名 |
| `group` | 发布组 | `ALLiANCE` | 文件名 |

**G. 高级上下文绑定**

| 变量 | 含义 | 用法 | 来源 |
|---|---|---|---|
| `info` | 扩展元数据映射 | `{info.tagline}`、`{info.overview}` 等属性访问，透出 TMDB 详情全部字段 | TMDB |
| `localize` | 动态本地化标题 | `{localize.ja.n}` 取日语标题、`{localize.zh.n}` 取中文标题 | TMDB `translations` |
| `order` | 动态剧集顺序 | `{order.ABSOLUTE.e}` 按指定 Episode Group 取集号 | TMDB `/tv/{id}/episode_groups` |
| `self` | 当前条目的全部绑定组（渲染为调试摘要） | 模板调试 | 上下文 |
| `model` | 匹配上下文（同批次绑定列表摘要） | 模板调试 | 上下文 |

**明确排除的绑定（不实现，附理由）**

| 排除项 | 绑定 | 理由 |
|---|---|---|
| 媒体流信息类 | `vcf` `hpi` `vk` `aco` `acf` `af` `channels` `resolution` `width` `height` `bitdepth` `hdr` `dovi` `bitrate` `vbr` `abr` `fps` `khz` `ar` `ws` `hd` `dt` `duration` `seconds` `minutes` `hours` `crc32` `media` `video` `audio` `text` `chapters` `audioLanguages` `textLanguages` `mediaTitle` | 均需读取文件二进制内容（MediaInfo / 哈希），WebDAV 远程读取成本高，超出范围；其中分辨率/编码等以 F 组文件名解析版替代 |
| 字幕类 | `lang` `subt` | 字幕下载功能范围外（同名字幕文件跟随改名仍支持） |
| 音乐类 | `music` `medium` `album` `artist` `albumArtist` | 音乐重命名范围外 |
| 照片类 | `image` `exif` `camera` `location` | 照片重命名范围外 |
| 外部数据源 | `omdb` `db` `AnimeList` `XEM` | 仅 TMDB 单一数据源（`tvdbid` 例外：TMDB `external_ids` 原生返回，已纳入 A 组） |
| CLI/桌面环境 | `home` `output` `defines` `label` | 移动端无 CLI 概念 |

**管道修饰符**（对齐 FileBot 的 String/Number/List 函数，全部自实现，可链式调用如 `{n|upper|space(_)}`）：

- **大小写**：`upper` / `lower` / `upperInitial`（首词首字母大写）/ `lowerTrail`（尾词小写）/ `title`（单词首字母大写）
- **补零与取整**：`pad(n)`（数字补零至 n 位，`{e|pad(3)}` → `001`）/ `round(n)`
- **字符替换**：`space(c)`（空格替换为指定字符）/ `dot`（等价 `space(.)`）/ `colon(c)` / `slash(c)` / `replace(a,b)` / `replaceAll(a,b)` / `removeAll(p)`
- **截取与匹配**：`before(p)` / `after(p)` / `match(p)` / `matchAll(p)`
- **命名变换**：`sortName`（去冠词排序名，`The Walking Dead` → `Walking Dead, The`）/ `initialName`（`James Cameron` → `J. Cameron`）/ `acronym`（`Deep Space 9` → `DS9`）/ `roman`（`4` → `IV`）
- **清洗与转写**：`clean`（剔除无效字符与发布信息）/ `ascii` / `transliterate`（音译为 ASCII）/ `validateFileName`（剔除文件名非法字符）
- **列表**：`joining(d)`（指定分隔符连接，可带前后缀，`{genres|joining(-)}` → `Sci-Fi-Drama`）

**可视化选项**（设置页，作用于所有预设，降低模板编辑需求）：

- 词语分隔符：空格 / `.` / `_`
- 大小写：保持原样 / 全小写 / 全大写 / 单词首字母大写
- 非法字符处理：替换为 `-` / 直接剔除
- 季号/集号补零位数：2 位 / 3 位

**内置预设**（路径相对于用户选择的库根目录）：

| 预设 | 电影模板 | 剧集模板 |
|---|---|---|
| **Plex**（默认） | `Movies/{n} ({y})/{n} ({y})` | `TV Shows/{n} ({y})/Season {s00}/{n} - {s00e00} - {t}` |
| **Kodi** | `Movies/{n} ({y})/{n} ({y})` | `TV Shows/{n}/Season {s00}/{n} {s00e00} {t}` |
| **Emby** | `Movies/{n} ({y})/{n} ({y})` | `TV Shows/{n}/Season {s00}/{n} - S{s00}E{e00} - {t}` |
| **Jellyfin** | 同 Emby | 同 Emby |

- 预设可被「另存为自定义模板」，用户可编辑模板字符串，编辑器提供变量插入按钮与 **实时预览**（用当前选中文件的解析结果即时渲染）。
- 模板引擎必须对缺失变量容错：渲染失败/缺失时该段留空并清理多余分隔符，不允许输出 `{undefined}` 之类的字面量。

### 5.6 重命名预览与执行

- 预览页（对标 FileBot 的 Original Files → New Names 双栏，移动端改为上下分区或卡片式对照列表）：
  - 每行：原路径（小字、灰色）→ 新路径（大字、主题色），右侧状态图标（自动匹配 ✅ / 待确认 ⚠️ / 冲突 ❌）。
  - 冲突检测：目标路径已存在于远程目录（需对目标目录做一次 PROPFIND）或同批次内目标重名 → 标红，提供「自动加序号后缀」一键解决。
  - 单条左滑可从本批次排除；点击单条可手动修改新文件名。
- 执行：
  1. 按目标路径排序，先 `MKCOL` 创建缺失目录（幂等，已存在则忽略 405）。
  2. 逐个 `MOVE /old/path HTTP/1.1 → Destination: /new/path`，`Overwrite: F`。
  3. 伴随文件（同名字幕/nfo/图片）跟随主文件 MOVE。
  4. 失败条目记录原因（403 权限 / 404 源不存在 / 409 父目录缺失 / 412 目标已存在），批次结束时汇总报告，支持「重试失败项」。
- 任务在 WorkManager 队列中串行执行，前台通知显示进度（当前文件/总数、当前文件名），App 杀死后可恢复。
- 每批次生成一条历史记录（见 5.7）。

### 5.7 历史记录与撤销

- Room 持久化：`RenameBatch`（时间、服务器、条目数、状态）+ `RenameEntry`（batchId、原路径、新路径、状态）。
- 历史页按批次倒序展示，可查看详情。
- **撤销**：对成功条目按相反顺序执行反向 MOVE（新路径 → 原路径），逐条确认远程状态；任一失败则中止并提示已回滚 N/M 条。
- 撤销操作本身也记入历史（标记为 revert 类型）。

### 5.8 设置备份与恢复（JSON 导出/导入）

- **导出**：设置页一键导出全部配置为单个 JSON 文件（通过 SAF 选择保存位置），内容包含：备份格式版本号、导出时间、命名选项（分隔符/大小写/补零等）、当前预设与全部自定义模板、服务器列表（别名/URL/根路径/用户名）、TMDB API Key 与语言偏好、Hosts 配置（§5.9）。历史记录与 TMDB 缓存**不**纳入备份。
- **密码处理（安全约束）**：服务器密码经 Android Keystore 加密，无法明文导出。导出时 `password` 字段默认为空串，导入后需重新输入；另提供「加密包含密码」选项——用户输入导出口令，用 AES-GCM（口令经 PBKDF2 派生密钥）加密密码字段，导入时输入同一口令解密。
- **导入**：选择 JSON 文件 → 解析并校验 schema 与版本兼容性（不兼容则明确报错）→ 展示变更预览（新增 N 项 / 覆盖 M 项）→ 用户确认后一次性写入。**必须先全量校验通过再落库**，导入失败不得破坏现有任何配置。
- JSON 顶层结构约定：`{ "formatVersion": 1, "exportedAt": "...", "app": "...", "settings": {...}, "servers": [...], "templates": [...], "hosts": [...] }`。

### 5.9 自定义 Hosts（TMDB 直连）

- **背景**：中国大陆访问 `api.themoviedb.org` 与 `image.tmdb.org` 存在 DNS 污染/解析异常，需提供不依赖代理的直连方案。
- **Hosts 配置**：设置页「网络与 Hosts」中可为指定域名配置一条或多条静态 IP；内置针对上述两个 TMDB 域名的候选 IP 预设列表；支持**自动测速**——对候选 IP 逐个发起 HTTPS 请求测延迟与可用性，自动选用最优结果并标注延迟。
- **实现方式**：实现 OkHttp `Dns` 接口，命中 hosts 表的域名返回配置的 IP（多条时轮询 + 失败切换），未命中走系统 DNS。**TLS SNI 与证书校验必须仍基于原域名**（OkHttp 自定义 Dns 机制天然满足），严禁使用 trustAll 证书或关闭主机名校验。
- **连接测试**：提供按钮分别测试 API 域名与图片域名的连通性、HTTP 状态与延迟，结果可视化反馈。
- **总开关**：Hosts 配置可随时整体启停；配置随 §5.8 备份一并导出导入。

---

## 6. UI / UX 设计规范（Infuse 风格）

**整体气质**：影院级深色界面，内容（海报与元数据）是视觉主角，控件克制、精致。

- **主题**：仅深色主题。背景近黑（如 `#0D0D0F`），卡片面 `#1A1A1E`，主强调色暖琥珀/金色（参考 Infuse 的 `#E8A33D` 区间），文字三级灰阶（`#F5F5F7` / `#9A9AA3` / `#5C5C66`）。Material 3 `darkColorScheme` 定制。
- **圆角与层次**：卡片圆角 12–16dp，海报圆角 8dp，阴影极轻，用亮度差而非投影分层。
- **首页/匹配确认页**：海报墙网格（2–3 列，`w342` 海报，懒加载 + 淡入过渡），选中项显示名称与年份。
- **详情/确认页**：顶部 fanart（`w780`）全宽 + 从上到下渐变遮罩融入背景，可选高斯模糊；标题、年份、评分、简介依次排布；操作按钮为填充胶囊主按钮 + 描边次按钮。
- **文件浏览器**：保持高效工具感（MiXplorer 式），列表行高密度，图标扁平单色；多选时顶部出现情境操作栏。
- **过渡动画**：Compose 默认弹簧动画，页面切换 `slideInHorizontally`；海报加载 `crossfade(300)`。
- **空状态**：居中插画占位 + 一句话引导（如「添加你的第一个 WebDAV 服务器」）。
- **字体**：系统默认（Roboto/系统字体），标题 `titleLarge`，正文 `bodyMedium`，路径用 `Monospace` 小号。

**主要页面清单**：

1. 服务器列表页 → 2. 添加/编辑服务器页 → 3. 文件浏览器（含多选，仅视频可选）→ 4. 匹配方式选择（电影/剧集）→ 5. 自动匹配进行中（进度 + 海报墙逐项点亮）→ 6. 待确认列表 + 候选选择页 → 7. Edit Match 手动匹配页（季/集选择、多集多选、键入过滤）→ 8. Episodes 面板（剧集集列表浏览、批量预制多集条目、线性对齐模式）→ 9. 重命名预览页 → 10. 执行进度页 → 11. 结果报告页 → 12. 历史记录页 → 13. 设置页（API Key、语言、命名选项、模板编辑、**备份导出/导入**、**网络与 Hosts**）

---

## 7. 架构与模块划分

```
app/
├── core/
│   ├── webdav/          # WebDAV 客户端：PropfindParser、WebDavClient（move/mkcol/list）、认证拦截器
│   ├── tmdb/            # Retrofit service、DTO → Domain 映射、限流拦截器、缓存策略
│   ├── parser/          # FilenameParser（正则规则表驱动，规则可单测）
│   ├── naming/          # TemplateEngine（变量解析、管道修饰、非法字符清洗）、PresetRepository
│   ├── matcher/         # MatchEngine（相似度评分、自动/手动决策）、ConfidenceScorer
│   └── backup/          # 设置导出/导入（JSON Schema 校验、AES-GCM 口令加密）、HostsDns（OkHttp Dns 实现）
├── data/
│   ├── db/              # Room：ServerConfig、RenameBatch、RenameEntry、TmdbCache
│   ├── repository/      # ServerRepository、RenameRepository、HistoryRepository、SettingsRepository
│   └── prefs/           # DataStore：API key、语言、命名选项
├── worker/              # RenameWorker（WorkManager + 前台通知）
└── ui/
    ├── servers/  browser/  match/  preview/  progress/  history/  settings/
    └── theme/           # Infuse 风格 darkColorScheme、Type、Shape
```

关键设计要求：

- `FilenameParser`、`TemplateEngine`、`MatchEngine` 为 **纯 Kotlin 无 Android 依赖**，便于 JVM 单元测试。
- WebDAV 路径处理统一用 UTF-8 URL 编码工具类（注意中文、空格、特殊字符的编解码，`Destination` 头必须是完整编码后的 URL）。
- 所有分页/长列表用 Compose `LazyColumn`；PROPFIND 结果不一次性渲染超大目录（>2000 项时分批加载）。

---

## 8. 验收标准

1. 添加 Alist / Nextcloud WebDAV 服务器并成功浏览目录（含中文路径）。
2. 选中一个含 50 个混杂命名剧集文件的目录，自动匹配正确率 ≥ 90%（剩余进入待确认可手动修正）。
3. 使用 Emby 预设预览，剧集新路径格式为 `TV Shows/{剧名}/Season {ss}/{剧名} - S{s00}E{e00} - {单集标题}.mkv`，电影为 `Movies/{电影名} ({年份})/{电影名} ({年份}).mkv`。
4. 执行 50 个文件重命名全程有进度通知，App 退后台不中断；同名字幕文件跟随改名。
5. 批次完成后可在历史记录中一键撤销，全部文件恢复原路径。
6. 目标路径冲突在预览阶段即被标红且不可直接执行。
7. `FilenameParser` 与 `TemplateEngine` 单元测试覆盖 ≥ 30 个用例（含 §5.3 表格全部模式、中文文件名、多集文件、无年份电影）。
8. **变量覆盖验收**：§5.5 变量表 A–G 组的每个变量都有对应单元测试用例（含缺失值容错渲染）；排除清单中的变量在模板中使用时必须渲染为空并给出警告，不得崩溃。
9. 整个 App 无 Groovy/脚本引擎依赖；元数据请求只发往 `api.themoviedb.org` 与 `image.tmdb.org`。
10. 文件浏览器显示目录下全部类型文件，但仅视频文件出现复选框且可勾选；字幕/nfo/图片置灰不可选。
11. 通过 Episodes 工具可将一个多集文件手动匹配为 `S01E01-E02` 组合条目，预览正确渲染合并标题；线性对齐模式可批量调整文件与集的对应关系。
12. 设置可导出为 JSON 文件；清除应用数据后导入该 JSON 可完整恢复服务器列表（密码按导出选项需重新输入或口令解密）、命名选项、自定义模板与 Hosts 配置。
13. 在模拟 DNS 污染的环境（如将 TMDB 域名指向无效解析）下，配置 hosts 静态 IP 后连接测试通过，搜索与图片加载恢复正常。

---

## 9. 硬性约束（红线）

1. 不实现 Groovy 或任何动态脚本求值；模板语法仅限 §5.5 定义的范围。
2. 不接入 TMDB 以外的元数据 API。
3. 不对 WebDAV 文件做读取/下载内容的操作（除浏览必需的 PROPFIND 外）——重命名只通过 MOVE/MKCOL 完成。
4. 服务器密码必须经 Android Keystore 加密后存储，禁止明文落盘、禁止进入日志。
5. 所有破坏性操作（批量重命名、撤销）执行前必须有明确的预览/确认界面。

---

## 10. 建议开发顺序（里程碑）

- **M1 骨架**：WebDAV 客户端 + 服务器管理 + 文件浏览器（只读浏览跑通）。
- **M2 智能**：文件名解析引擎 + TMDB 客户端 + 匹配引擎（先 CLI 式单测验证，再接 UI）。
- **M3 命名**：模板引擎 + 预设 + 预览页（只预览不执行）。
- **M4 执行**：MKCOL/MOVE 批量执行 + WorkManager 队列 + 进度通知 + 冲突处理。
- **M5 收尾**：历史与撤销、设置页打磨、Infuse 风格视觉精修、验收用例全量回归。

每个里程碑交付可运行的 APK 与对应模块的单元测试。
使用GitHub Actions 在任意子分支push 时触发构建推送到Pre-release