# STORY.md · Tlias 智能学习辅助系统 项目答辩 PPT

## ① 用户意图对齐

**目标受众**：课程项目答辩现场（教师/评审），兼顾求职场景下的 Java 开发岗位面试展示。观众为具备技术判断力但不熟悉本项目细节的人。
**核心目标**：让观众相信——我已具备独立把"需求文档"翻译为"可运行、可演示、可维护的全栈系统"的工程能力；记住 Tlias 是一个覆盖 7 大业务域、30 个 REST 接口、含真实事务与鉴权设计的完整系统，而非练手 Demo。
**PPT 长度**：15 页（封面 1 + 导览 1 + 正文 12 + 结束页 1），对应现场约 10 分钟讲述，Hero 页配额 3 页（20%）。
**视觉调性**：墨绿灰 + 砖红、白底克制、证据驱动、结论条贯穿、界面即证据（对齐用户既有 Java 课程 PPT 的家底风格）。
**内容边界**：
- 必讲：模块划分、技术栈、四大关键技术点、真实界面、亮点、成长收获、升级规划。
- 不讲：逐个接口的字段清单（放附录/源码）、数据库建表语句细节、课程理论概念科普。
- 禁碰：不夸大——所有数字（30 接口 / 7 表 / 1671 行 / 1264 行 / 105+ 条日志）均来自本地实测，亮点只讲已实现的能力，升级规划明确标注"规划中"。

## ② 页面布局骨架

**分页与角色**（无 `type: catalog` 页：第 02 页为 `type: content` 的"内容导览"，不声明章节扉页契约，全篇不设 section 扉页，以最大化 10 分钟现场讲述的信息密度）：

| # | 主题 | role | rhythm | 版式 |
| :- | :-- | :- | :- | :-- |
| 01 | 封面 | hero | peak | 全幅色块 + 骑线文字 |
| 02 | 内容导览 | supporting | valley | 左窄色块标题栏 + 右侧条目 |
| 03 | 项目背景与目标 | supporting | valley | 非对称双栏（55:45） |
| 04 | 系统架构与技术栈 | supporting | peak | 上方架构图 + 下方标签条 |
| 05 | 功能模块全景 | supporting | valley | 非对称双栏（左图右数字） |
| 06 | 核心技术实现 | supporting | valley | 左标题 + 右内容（4 行技术点） |
| 07 | 核心业务时序 | supporting | peak | 横向流程图 + 下方三段说明 |
| 08 | 员工模块3·改删异常统计 | supporting | peak | 左侧深色栏 + 右侧 2×2 卡片 |
| 09 | 界面呈现·员工管理 | **hero** | peak | 全宽截图 + 底部结论条 |
| 10 | 界面呈现·数据统计 | supporting | valley | 左大图 + 右洞察 |
| 11 | 界面呈现·学员与日志 | supporting | valley | 双图并排（左宽右窄）+ 下方说明带 |
| 12 | 项目特色亮点 | supporting | peak | 纵向编号条目（5 条，巨型序号） |
| 13 | 个人成长与收获 | supporting | valley | 左窄色块 + 右侧前后对比 |
| 14 | 后期升级改造规划 | supporting | peak | 阶梯式三阶段路线图 |
| 15 | 结束页 | **hero** | peak | 居中金句 + 落款 |

**Hero 页定位**：01（封面）、09（员工管理界面）、15（结束页）。占比 3/15 = 20% ✓，Hero 间均隔 ≥ 4 页 ✓。
**rhythm 曲线**：peak(01) → valley(02) → valley(03) → peak(04) → valley(05) → valley(06) → peak(07) → peak(08) → peak(09) → valley(10) → valley(11) → peak(12) → valley(13) → peak(14) → peak(15)。无连续 ≥3 页 valley ✓；07/08 连续 peak 由 08 的界面大图承担高潮，13/14 由"规划→收束"形成递进而非重复。
**非对称版式预算**：15 页中 14 页为非对称（93%）≥ 40% ✓；对称版式仅第 15 页（居中金句）1 页 ≤ 2 ✓；`左大图+右侧文字` + `非对称双栏` 合计 3 页（03/05/10）= 20% ≤ 40% ✓；`N卡片横排` 0 次 ✓；无相邻两页版式重复 ✓。

## ③ 页面大纲

### 01 封面
- type: cover / role: hero / rhythm: peak
- layout: 全幅色块 + 骑线文字
- visual: 右侧深蓝竖带 + 橙色数据标；L1 视觉以大字号排版块承担
- visual_role: anchor
- density: 字数约 60 / 图片 0 张 / 留白约 30%
- anti_pattern: 禁止居中通用标题模板；禁止把姓名放成页脚小字；禁止用装饰小图充当主视觉
- description: 主标"Tlias 智能学习辅助系统"锁定项目名，副标点明"Spring Boot 3 + Vue 3 前后端分离全栈实战"，右侧深蓝竖带承载"7 大业务域 / 30 个 REST 接口 / 真实可运行"三项数据，底部落款姓名、学校、专业与日期。

### 02 内容导览
- type: content / role: supporting / rhythm: valley
- layout: 左窄色块标题栏 + 右侧条目
- visual: L3 角标（深蓝方块序号）；右侧纵向 5 条路线条目
- visual_role: evidence
- density: 字数约 150 / 图片 0 张 / 留白约 30%
- anti_pattern: 禁止把 5 个板块做成等宽卡片横排；禁止只写板块名不写"这一段我会讲什么"
- description: 把 10 分钟拆成 5 段可预期的路线：项目全景 → 技术实现 → 界面呈现 → 特色与成长 → 未来规划，每条标注对应页码区间，让评委知道节奏、也知道重点落在哪一段。

### 03 项目背景与目标
- type: content / role: supporting / rhythm: valley
- layout: 非对称双栏（55:45）
- visual: 左栏三条痛点编号条目；右栏目标与范围说明块
- visual_role: evidence
- density: 字数约 260 / 图片 0 张 / 留白约 25%
- anti_pattern: 禁止 50:50 等分；禁止把痛点写成空泛口号；禁止无判断的罗列
- description: 痛点——课程信息散落、员工与班级数据无关联、变更无留痕；目标——把"部门/员工/班级/学员/统计/日志"整合为一套可管理的数据后台。判断：正因为原业务存在"多表关联 + 增删改留痕"的真实复杂度，才值得用全栈方案落地，也决定了本项目的技术重点落在关联查询与事务一致性上。

### 04 系统架构与技术栈
- type: content / role: supporting / rhythm: peak
- layout: 上方架构图 + 下方标签条
- visual: L1 架构图（SVG 三层结构：Vue3 前端 → Spring Boot 后端 → MySQL 数据层，横切标注 JWT 鉴权与 AOP 日志）
- visual_role: anchor
- density: 字数约 200 / 图 1 张（SVG 架构图，占 B 区约 45%）
- anti_pattern: 禁止把架构画成三段等宽方块；禁止堆砌技术名词不做分层归属；禁止在标题栏塞装饰小图
- description: 三层架构：Vue3 + Element Plus 表现层经 Vite 代理调用 REST 接口；Spring Boot 3 业务层以 Controller / Service / Mapper 三层承接，MyBatis 拼装动态 SQL；MySQL 8 持久层承载 7 张业务表。JWT 拦截与 AOP 日志作为横切能力贯穿三层。判断：分层清晰使得"改一个业务只动一个目录"，这是项目可维护性的结构基础。

### 05 功能模块全景
- type: content / role: supporting / rhythm: valley
- layout: 非对称双栏（左图右数字）
- visual: 左侧 7 模块 SVG 地图（部门/员工/班级/学员/统计/日志/登录上传）
- visual_role: evidence
- density: 字数约 200 / 图 1 张 / 留白约 28%
- anti_pattern: 禁止把 7 个模块做成 7 张等宽卡片；禁止巨型数字只报数不给判断
- description: 7 个业务域全部落地并可操作；右侧三个锚点数字：30 个 REST 接口、7 张业务表、8 个控制器。判断：接口数与模块数对得上，说明需求文档里的每一条规范都找到了实现落点，而不是只做"看起来像"的界面。

### 06 核心技术实现
- type: content / role: supporting / rhythm: valley
- layout: 左标题 + 右内容（4 行技术点，每行含代码片段）
- visual: 四行技术点，右侧代码片段用 Consolas 呈现
- visual_role: evidence
- density: 字数约 320 / 代码片段 4 段 / 留白约 18%
- anti_pattern: 禁止 4 张等宽卡片横排；禁止只写技术名不贴关键代码；禁止用伪代码冒充真实实现
- description: 四个技术点——① MyBatis XML 动态 SQL 实现姓名/性别/入职日期三条件组合分页；② JWT + 拦截器从自定义 token 头取令牌，未登录返回 401；③ AOP 环绕通知自动落库操作日志（含耗时与返回体）；④ `@Transactional(rollbackFor = Exception.class)` 配合 `REQUIRES_NEW` 让操作日志独立提交。判断：这四点分别对应"查得准、控得住、留得全、不脏数据"，是本项目区别于纯 CRUD Demo 的实质。

### 07 核心业务时序
- type: content / role: supporting / rhythm: peak
- layout: 横向流程图 + 下方三段说明
- visual: L1 流程图（SVG，5 步横向链路 + 双事务分叉）
- visual_role: anchor
- density: 字数约 230 / 图 1 张（占 B 区约 40%）
- anti_pattern: 禁止把流程画成竖向单列；禁止只画成功路径不画异常路径
- description: 新增员工一次提交要写两张表：emp 主表与 emp_expr 工作经历。Service 方法开启主事务，两表写入同生共死；finally 块调用 REQUIRES_NEW 的日志服务另起事务。判断：把"业务数据"与"操作日志"放在两个事务里，正是为了保证业务回滚时日志仍然留痕——这是我在课程示例基础上真正理解并落地的部分。

### 08 员工模块3 · 修改 / 删除 / 异常 / 统计
- type: content / role: supporting / rhythm: peak
- layout: 左侧深色栏 + 右侧 2×2 卡片
- visual: 左侧墨绿灰深色栏（04 个功能点清单）+ 右侧四张白底卡片
- visual_role: evidence
- density: 字数约 340 / 图片 0 张 / 留白约 16%
- anti_pattern: 禁止四张等宽卡片顶天立地；禁止把接口路径写成中文说明；禁止文字溢出卡片
- description: 对应课程《04-后端Web项目（员工模块3）》的全部四个要求——修改员工（一条 left join + resultMap 的 collection 一次查出基本信息与全部工作经历，保存时先删旧经历再批量插新经历，同事务提交）、批量删除（一个接口兼容单删与批量，emp 与 emp_expr 联动删除，事务保证不残留孤儿数据）、全局异常处理（emp.phone 唯一约束冲突由 @RestControllerAdvice + @ExceptionHandler 翻译成「该手机号已被其他员工占用」这类可读提示）、员工信息统计（case when 翻译职位名后 group by 聚合，用 JobOption 封装 jobList / dataList 供前端 ECharts 直接消费）。判断：这四个功能合起来，才是「员工管理」的完整闭环——增删改查齐了，异常兜住了，统计也出来了。

### 09 界面呈现 · 员工管理
- type: content / role: hero / rhythm: peak
- layout: 全幅截图 + 骑线文字
- visual: L1 `assets/03_employee_list.png`（真实运行界面，占 B 区约 62%）
- visual_role: anchor
- density: 字数约 120 / 图片 1 张 / 留白约 22%
- anti_pattern: 禁止把截图缩成小图塞进卡片；禁止只用"界面很美观"这类无信息量评价；禁止用代码块代替真实界面
- description: 真实运行中的员工管理页：左侧六模块导航、条件查询区（姓名/性别/入职日期）、分页表格（头像、职位、部门）、批量删除。判断：条件分页与批量删除同时可用，说明后端动态 SQL 与批量 SQL 都经过了真实数据验证，而非仅在开发环境跑通。

### 10 界面呈现 · 数据统计
- type: content / role: supporting / rhythm: valley
- layout: 左大图 + 右洞察
- visual: L1 `assets/05_report.png`（ECharts 四图，占 B 区约 58%）
- visual_role: evidence
- density: 字数约 150 / 图片 1 张 / 留白约 25%
- anti_pattern: 禁止 50:50 等分；禁止只贴图表不解读；禁止把图表缩成缩略图
- description: 四组统计图表：员工性别、职位人数、学员学历、班级人数，均由后端聚合查询实时返回。判断：统计接口不落库、全部实时聚合，数据源唯一，避免了"统计结果与明细对不上"这一常见问题。

### 11 界面呈现 · 学员与操作日志
- type: content / role: supporting / rhythm: valley
- layout: 双图并排（左宽右窄）+ 下方说明带
- visual: L2 `assets/06_student.png` + L2 `assets/08_log.png`
- visual_role: evidence
- density: 字数约 140 / 图片 2 张 / 留白约 26%
- anti_pattern: 禁止两张图做成完全等宽对称；禁止只贴图不写这张图证明了什么
- description: 左侧学员管理（违纪次数/扣分/班级关联字段），右侧操作日志（记录类名、方法名、参数、返回值与耗时）。判断：日志表里已沉淀 100+ 条真实操作记录，说明 AOP 埋点在日常使用中持续生效，这是"能自证运行"的运维底座。

### 12 项目特色亮点
- type: content / role: supporting / rhythm: peak
- layout: 纵向编号条目（5 条，巨型序号）
- visual: 每条左侧巨型序号（48–64px）作为锚点
- visual_role: anchor
- density: 字数约 320 / 图片 0 张 / 留白约 20%
- anti_pattern: 禁止等宽卡片横排；禁止把 5 条亮点写成没有差异的空话；禁止序号与正文同字号
- description: 五条亮点——① 按接口文档 100% 交付 30 条规范；② 端到端真实可运行（真实 MySQL、真实前后端联调）；③ 跨表事务一致性保障；④ 事务传播行为落地验证；⑤ 工程化闭环（Git 版本管理、GitHub 公开仓库、一键启动脚本、初始化 SQL 随码交付）。判断：前四条证明技术深度，第五条证明工程习惯——两者合起来才是"可被团队接手"的系统。

### 13 个人成长与收获
- type: content / role: supporting / rhythm: valley
- layout: 左窄色块 + 右侧前后对比
- visual: 左栏深蓝块（标题+一句话自评），右侧"起点 → 现在"对照与三条收获
- visual_role: evidence
- density: 字数约 280 / 图片 0 张 / 留白约 26%
- anti_pattern: 禁止自评写成形容词堆砌；禁止只讲技术成长不讲工程习惯养成
- description: 起点：会用 Java 语法、能用 JDBC 写单表增删改查；现在：能独立完成 30 接口全栈系统，并落地鉴权、事务、缓存式查询与日志埋点。三条收获——① 把"需求文档"当作唯一事实来源的习惯；② 排错能力（端口占用、MyBatis 映射未加载、代理证书问题均独立定位）；③ 版本管理与交付意识。判断：最大的成长不是某个 API 会用，而是"从需求到上线闭环"的完整链路走通了一遍。

### 14 后期升级改造规划
- type: content / role: supporting / rhythm: peak
- layout: 阶梯式三阶段路线图
- visual: SVG 阶梯（阶段一/二/三，逐级抬升）+ 每阶段任务标签
- visual_role: anchor
- density: 字数约 280 / 图 1 张 / 留白约 22%
- anti_pattern: 禁止把三阶段做成等宽卡片；禁止把"规划"写成已完成；禁止阶段之间无递进关系
- description: 阶段一工程加固（OpenAPI 文档、单元测试与接口文档同步、异常日志分级）；阶段二能力扩展（Redis 缓存与登录态续期、对象存储替换本地磁盘、RBAC 角色权限）；阶段三架构演进（Docker 容器化、CI/CD 自动构建部署、Vue3 + TypeScript 重构、监控告警）。判断：改造顺序遵循"先可维护、再可扩展、最后可规模化"，与项目当前规模匹配，不做过度设计。

### 15 结束页
- type: ending / role: hero / rhythm: peak
- layout: 居中金句 + 落款
- visual: 居中大号金句 + 底部深蓝信息条（姓名/仓库地址）
- visual_role: atmosphere
- density: 字数约 50 / 图片 0 张 / 留白约 45%
- anti_pattern: 禁止四卡片预览；禁止"谢谢观看"空洞收尾；禁止堆砌正文段落
- description: 收束句——"从一份接口文档，到一套能跑起来、能被接手、能继续长大的系统"。落款给出姓名、方向与 GitHub 仓库地址，把现场演示延伸到可自行验证。

## Checklist 自检
- Hero 页 3/15 = 20%（≥2 且 ≤35%）✓
- 无连续 ≥3 页 supporting+valley（02/03 与 05/06 间均被 peak 页打断）✓
- `N卡片横排` 0 次 ✓
- 非对称版式 14/15 = 93%（≥40%）✓
- 相邻页版式无重复 ✓
- `左大图+右侧文字`+`非对称双栏` = 3/15 = 20%（≤40%）✓
- 每页均含 role / rhythm / visual_role / anti_pattern ✓
- 关键数据页均给出"所以呢"判断句（05/06/07/08/09/10/11/12/13/14）✓
- 无 `type: catalog` 页 → 无章节扉页契约冲突 ✓
