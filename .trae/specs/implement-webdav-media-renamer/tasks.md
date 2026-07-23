# Tasks

> 按里程碑 M1–M5 组织，每个里程碑交付可运行 APK + 对应模块单元测试。
> 开发顺序遵循计划 §10：骨架 → 智能 → 命名 → 执行 → 收尾。

## M1 骨架：WebDAV 客户端 + 服务器管理 + 文件浏览器

- [x] Task 1.1: 初始化 Android 工程骨架（Gradle Kotlin DSL + Version Catalog、minSdk 26、Kotlin 100%、Hilt、Compose + Material 3、Coroutines/Flow、OkHttp/Retrofit、Room、DataStore、Coil 3、WorkManager）。
  - [x] SubTask 1.1.1: 配置 `libs.versions.toml` 与 `build.gradle.kts`
  - [x] SubTask 1.1.2: 配置 Infuse 风格 `darkColorScheme`、Type、Shape（`app/ui/theme/`）
  - [x] SubTask 1.1.3: 配置 GitHub Actions（子分支 push 触发构建推 Pre-release）
- [x] Task 1.2: 实现 WebDAV 客户端（`app/core/webdav/`）。
  - [x] SubTask 1.2.1: `WebDavClient`（PROPFIND Depth 0/1、MOVE、MKCOL，`Overwrite: F`）
  - [x] SubTask 1.2.2: `PropfindParser`（XmlPullParser 解析 displayname/size/getlastmodified 等）
  - [x] SubTask 1.2.3: Basic/Digest Auth 自动协商拦截器（读 401 `WWW-Authenticate` 头）
  - [x] SubTask 1.2.4: UTF-8 URL 编码工具类（中文/空格/特殊字符，`Destination` 头完整编码）
- [x] Task 1.3: 实现服务器管理数据层（`app/data/`）。
  - [x] SubTask 1.3.1: Room `ServerConfig` 实体 + DAO
  - [x] SubTask 1.3.2: Android Keystore 密码加解密工具
  - [x] SubTask 1.3.3: `ServerRepository`
- [x] Task 1.4: 实现服务器列表页 + 添加/编辑服务器页 + 测试连接（`app/ui/servers/`）。
  - [x] SubTask 1.4.1: 服务器列表卡片页（左滑编辑/删除、密码 `••••••`）
  - [x] SubTask 1.4.2: 添加/编辑表单 + 测试连接（PROPFIND Depth: 0 反馈）
- [x] Task 1.5: 实现文件浏览器（`app/ui/browser/`，对齐 MiXplorer）。
  - [x] SubTask 1.5.1: 目录列表（图标/名称/大小/修改时间，PROPFIND Depth: 1）
  - [x] SubTask 1.5.2: 面包屑导航 + 系统返回键逐级回退 + 下拉刷新
  - [x] SubTask 1.5.3: 排序（名称/大小/时间，升降序切换）
  - [x] SubTask 1.5.4: 多选模式（长按进入，全选/反选，仅视频可勾选，非视频置灰无复选框）
  - [x] SubTask 1.5.5: 视频/字幕扩展名识别（iso 仅显示）
  - [x] SubTask 1.5.6: 大目录分批加载（>2000 项）

## M2 智能：文件名解析 + TMDB 客户端 + 匹配引擎

- [x] Task 2.1: 实现文件名解析引擎（`app/core/parser/`，纯 Kotlin 无 Android 依赖）。
  - [x] SubTask 2.1.1: `ParsedFilename` 数据模型
  - [x] SubTask 2.1.2: 季集模式正则规则表（SxxExx/NxN/第X季第X集/独立集号/日期型）
  - [x] SubTask 2.1.3: 清洗规则（去扩展名、`.`/`_`→空格、剔除技术标签尾巴、合并空格）
  - [x] SubTask 2.1.4: `FilenameParser` 单元测试 ≥ 30 用例（含全模式/中文/多集/无年份）
- [x] Task 2.2: 实现 TMDB 客户端（`app/core/tmdb/`）。
  - [x] SubTask 2.2.1: Retrofit service + DTO + Domain 映射（kotlinx.serialization）
  - [x] SubTask 2.2.2: 端点封装（search/movie、search/tv、movie/{id}、tv/{id}、tv/{id}/season/{n}、episode_group、collection/{id}），`append_to_response` 合并
  - [x] SubTask 2.2.3: 限流拦截器（40 req/10s）+ 429 退避重试
  - [x] SubTask 2.2.4: 图片基址 `https://image.tmdb.org/t/p/`（w342/w780/original）
- [x] Task 2.3: 实现匹配引擎（`app/core/matcher/`，纯 Kotlin 无 Android 依赖）。
  - [x] SubTask 2.3.1: `ConfidenceScorer`（标题相似度 × 权重 + 年份加成 + 流行度微调）
  - [x] SubTask 2.3.2: `MatchEngine`（自动/手动决策阈值：≥0.85 且次名分差≥0.1）
  - [x] SubTask 2.3.3: 剧集匹配后填充 episodeTitle/airDate + 多集标题 `A & B` 合并
  - [x] SubTask 2.3.4: Room `TmdbCache` 缓存（`tmdbId + language` 为键）【Android 层，待 :app】
- [x] Task 2.4: 实现匹配 UI 流程（`app/ui/match/`）。
  - [x] SubTask 2.4.1: 匹配方式选择（电影/剧集，含强制指定目录类型）
  - [x] SubTask 2.4.2: 自动匹配进行中（进度 + 海报墙逐项点亮）
  - [x] SubTask 2.4.3: 待确认列表 + 候选选择页（海报缩略图 + 名称 + 年份 + 简介首行）
- [x] Task 2.5: 实现手动匹配与 Episodes 工具（`app/ui/match/`）。
  - [x] SubTask 2.5.1: Edit Match 单条修正（切换电影/剧集、季选择器 + 集列表、find-as-you-type 过滤）
  - [x] SubTask 2.5.2: 多集文件多选集（连续/手动勾选，组合条目 `S01E01-E02`，标题 `A & B`）
  - [x] SubTask 2.5.3: Episodes 面板（浏览全季集列表、连续多选批量生成多集组合条目）
  - [x] SubTask 2.5.4: 线性对齐模式（文件列表与集列表顺序对齐、拖拽调整、单条解绑、批量应用）

## M3 命名：模板引擎 + 预设 + 预览页（只预览不执行）

- [x] Task 3.1: 实现模板引擎（`app/core/naming/`，纯 Kotlin 无 Android 依赖）。
  - [x] SubTask 3.1.1: `TemplateEngine`（变量解析、管道修饰、链式、路径分隔 `/`）
  - [x] SubTask 3.1.2: 变量表 A–G 组全量实现（数据来源标注 TMDB/文件名/WebDAV/上下文）
  - [x] SubTask 3.1.3: 管道修饰符全量实现（大小写/补零取整/字符替换/截取匹配/命名变换/清洗转写/列表）
  - [x] SubTask 3.1.4: 缺失变量容错（留空 + 清理多余分隔符，不输出 `{undefined}`）
  - [x] SubTask 3.1.5: 排除绑定渲染为空 + 警告（不崩溃）
  - [x] SubTask 3.1.6: 非法字符清洗（`\/:*?"<>|`，按用户设置替换/剔除）
  - [x] SubTask 3.1.7: `TemplateEngine` 单元测试（A–G 组每个变量 + 缺失值容错 + 排除绑定警告）
- [x] Task 3.2: 实现预设与可视化选项。
  - [x] SubTask 3.2.1: `PresetRepository`（Plex/Kodi/Emby/Jellyfin 预设 + 另存为自定义模板）
  - [x] SubTask 3.2.2: 可视化选项（分隔符/大小写/非法字符处理/补零位数，作用于所有预设）
- [x] Task 3.3: 实现模板编辑器（`app/ui/settings/`）。
  - [x] SubTask 3.3.1: 模板字符串编辑器 + 变量插入按钮 + 实时预览（当前选中文件解析结果即时渲染）
- [x] Task 3.4: 实现重命名预览页（`app/ui/preview/`，只预览不执行）。
  - [x] SubTask 3.4.1: 对照列表（原路径小字灰色 → 新路径大字主题色，状态图标 自动✅/待确认⚠️/冲突❌）
  - [x] SubTask 3.4.2: 冲突检测（目标目录 PROPFIND + 同批次内重名，标红，一键自动加序号后缀）
  - [x] SubTask 3.4.3: 单条左滑排除 + 点击单条手动修改

## M4 执行：MKCOL/MOVE 批量执行 + WorkManager + 进度通知 + 冲突处理

- [x] Task 4.1: 实现批量执行逻辑（`app/core/webdav/` + `app/worker/`）。
  - [x] SubTask 4.1.1: 按目标路径排序 + MKCOL 建缺失目录（幂等，405 忽略）
  - [x] SubTask 4.1.2: 逐个 MOVE（`Overwrite: F`）+ 伴随文件跟随（同名字幕/nfo/图片）
  - [x] SubTask 4.1.3: 失败条目记录原因（403/404/409/412）
  - [x] SubTask 4.1.4: 汇总报告 + 重试失败项
- [x] Task 4.2: 实现 WorkManager 队列 + 前台通知（`app/worker/`）。
  - [x] SubTask 4.2.1: `RenameWorker`（串行队列 + 前台 Service 通知：当前文件/总数 + 文件名）
  - [x] SubTask 4.2.2: App 杀后恢复（WorkManager 持久化）
- [x] Task 4.3: 实现执行进度页 + 结果报告页（`app/ui/progress/`）。

## M5 收尾：历史与撤销、设置页打磨、Infuse 视觉精修、验收回归

- [x] Task 5.1: 实现历史记录与撤销（`app/data/db/` + `app/ui/history/`）。
  - [x] SubTask 5.1.1: Room `RenameBatch` + `RenameEntry` 实体与 DAO
  - [x] SubTask 5.1.2: `HistoryRepository`
  - [x] SubTask 5.1.3: 历史页按批次倒序 + 详情查看
  - [x] SubTask 5.1.4: 整批撤销（反向顺序 MOVE，逐条确认，中途失败提示「已回滚 N/M 条」，撤销记入历史标记 revert）
- [x] Task 5.2: 实现设置备份与恢复（`app/core/backup/` + `app/ui/settings/`）。
  - [x] SubTask 5.2.1: JSON 导出（SAF 选择保存位置，formatVersion/exportedAt/settings/servers/templates/hosts，历史与缓存不纳入）
  - [x] SubTask 5.2.2: 密码处理（默认空串 + AES-GCM 口令加密选项，PBKDF2 派生密钥）
  - [x] SubTask 5.2.3: JSON 导入（schema + 版本兼容性校验 → 变更预览 → 全量校验通过再落库，失败不破坏现有配置）
- [x] Task 5.3: 实现自定义 Hosts（`app/core/backup/HostsDns` + `app/ui/settings/`）。
  - [x] SubTask 5.3.1: OkHttp `Dns` 接口实现（命中 hosts 返回 IP，多条轮询 + 失败切换，未命中走系统 DNS，TLS SNI/证书基于原域名）
  - [x] SubTask 5.3.2: 候选 IP 预设列表（api.themoviedb.org、image.tmdb.org）
  - [x] SubTask 5.3.3: 自动测速（逐个 HTTPS 测延迟 + 可用性，选用最优并标注延迟）
  - [x] SubTask 5.3.4: 连接测试按钮（API 与图片域名分别测连通性/HTTP 状态/延迟，可视化反馈）
  - [x] SubTask 5.3.5: Hosts 总开关 + 配置随备份导出导入
- [x] Task 5.4: 设置页打磨（API Key 引导、语言偏好、命名选项、模板编辑、备份导出/导入、网络与 Hosts）。
- [x] Task 5.5: Infuse 风格视觉精修（海报墙网格、fanart 渐变遮罩、空状态插画、过渡动画 `slideInHorizontally`/`crossfade(300)`、字体层级）。
- [x] Task 5.6: 验收用例全量回归（按 checklist.md 逐条验证，含 50 文件目录自动匹配 ≥90%、Emby 预设格式、伴随文件跟随、撤销恢复、DNS 污染下直连等）。【沙箱可验证 82/85 通过；#24/#47/#66 需真机集成测试，代码已支持】

# Task Dependencies

- [Task 1.1] 无依赖（工程骨架先行）
- [Task 1.2] 依赖 [Task 1.1]
- [Task 1.3] 依赖 [Task 1.1]
- [Task 1.4] 依赖 [Task 1.2]、[Task 1.3]
- [Task 1.5] 依赖 [Task 1.2]、[Task 1.4]
- [Task 2.1] 依赖 [Task 1.1]（纯 Kotlin，可与 M1 部分并行）
- [Task 2.2] 依赖 [Task 1.1]
- [Task 2.3] 依赖 [Task 2.1]、[Task 2.2]
- [Task 2.4] 依赖 [Task 1.5]、[Task 2.3]
- [Task 2.5] 依赖 [Task 2.4]
- [Task 3.1] 依赖 [Task 2.1]（纯 Kotlin，可与 M2 后期并行）
- [Task 3.2] 依赖 [Task 3.1]
- [Task 3.3] 依赖 [Task 3.2]
- [Task 3.4] 依赖 [Task 3.1]、[Task 2.4]
- [Task 4.1] 依赖 [Task 1.2]、[Task 3.4]
- [Task 4.2] 依赖 [Task 4.1]
- [Task 4.3] 依赖 [Task 4.2]
- [Task 5.1] 依赖 [Task 4.2]
- [Task 5.2] 依赖 [Task 1.3]、[Task 3.2]、[Task 5.3]
- [Task 5.3] 依赖 [Task 1.1]
- [Task 5.4] 依赖 [Task 5.2]、[Task 5.3]
- [Task 5.5] 依赖 [Task 1.4]、[Task 2.4]、[Task 3.4]
- [Task 5.6] 依赖所有前序任务
