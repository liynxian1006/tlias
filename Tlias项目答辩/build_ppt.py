# -*- coding: utf-8 -*-
"""Tlias 项目答辩 PPT 构建器 —— 对齐用户 Java 课程 PPT 的家底风格：
白底 / 左上角小标题 / 墨绿灰+蓝+砖红 / 米黄结论条 / 深色代码块；封面极简。"""
import re, os
from pptx import Presentation
from pptx.util import Emu, Pt
from pptx.dml.color import RGBColor
from pptx.enum.text import PP_ALIGN, MSO_ANCHOR
from pptx.enum.shapes import MSO_SHAPE
from pptx.oxml.ns import qn

BASE = os.path.dirname(os.path.abspath(__file__))
ASSETS = os.path.join(BASE, "assets")
OUT = os.path.join(BASE, "Tlias项目答辩.pptx")

# ---------- 设计令牌（取自用户课程 PPT 主题） ----------
INK   = RGBColor(0x36, 0x3C, 0x3B)   # 主色 墨绿灰
BLUE  = RGBColor(0x4F, 0x81, 0xBD)   # 辅色 蓝
RED   = RGBColor(0xAD, 0x2B, 0x26)   # 强调 砖红
CREAM = RGBColor(0xFF, 0xFF, 0xE4)   # 米黄（代码块前景 / 结论条底）
BG    = RGBColor(0xFC, 0xFA, 0xEE)   # 结论条底（淡米黄）
GRAY  = RGBColor(0x6E, 0x73, 0x72)   # 次要文字
SILVER= RGBColor(0xA8, 0xAD, 0xAC)   # 弱文字 / 页码
LINE  = RGBColor(0xE3, 0xE0, 0xCE)   # 分隔线
WHITE = RGBColor(0xFF, 0xFF, 0xFF)
CODES = RGBColor(0xF0, 0xEC, 0xD6)   # 代码块正文
CODEK = RGBColor(0xE8, 0x9A, 0x95)   # 代码块关键词

FONT = "Microsoft YaHei"
MONO = "Consolas"
W, H = 1280, 720
TOTAL = 15
ML, MR = 64, 64
CTOP, CBOT = 124, 646

def px(v): return Emu(int(round(v * 9525)))

prs = Presentation()
prs.slide_width, prs.slide_height = px(W), px(H)
BLANK = prs.slide_layouts[6]

def new_slide():
    s = prs.slides.add_slide(BLANK)
    bg = s.shapes.add_shape(MSO_SHAPE.RECTANGLE, 0, 0, px(W), px(H))
    bg.fill.solid(); bg.fill.fore_color.rgb = WHITE
    bg.line.fill.background(); bg.shadow.inherit = False
    return s

def set_ea(run, name):
    rPr = run._r.get_or_add_rPr()
    for tag in ('a:latin', 'a:ea', 'a:cs'):
        el = rPr.find(qn(tag))
        if el is None:
            el = rPr.makeelement(qn(tag), {}); rPr.append(el)
        el.set('typeface', name)

def rect(s, x, y, w, h, fill=None, line=None, lw=1, shape=MSO_SHAPE.RECTANGLE, radius=None):
    sh = s.shapes.add_shape(shape, px(x), px(y), px(w), px(h))
    if fill is None: sh.fill.background()
    else:
        sh.fill.solid(); sh.fill.fore_color.rgb = fill
    if line is None: sh.line.fill.background()
    else:
        sh.line.color.rgb = line; sh.line.width = Pt(lw)
    sh.shadow.inherit = False
    if radius is not None and shape == MSO_SHAPE.ROUNDED_RECTANGLE:
        try: sh.adjustments[0] = radius
        except Exception: pass
    tf = sh.text_frame
    tf.margin_left = tf.margin_right = tf.margin_top = tf.margin_bottom = 0
    return sh

def parse(row):
    out, pos = [], 0
    for mt in re.finditer(r"\*\*(.+?)\*\*|`(.+?)`", row):
        if mt.start() > pos: out.append((row[pos:mt.start()], False, False))
        if mt.group(1) is not None: out.append((mt.group(1), True, False))
        else: out.append((mt.group(2), False, True))
        pos = mt.end()
    if pos < len(row): out.append((row[pos:], False, False))
    return out or [("", False, False)]

def txt(s, x, y, w, h, content, size=15, color=INK, bold=False, align=PP_ALIGN.LEFT,
        anchor=MSO_ANCHOR.TOP, line=1.5, after=0, font=FONT):
    tb = s.shapes.add_textbox(px(x), px(y), px(w), px(h))
    tf = tb.text_frame
    tf.word_wrap = True
    tf.margin_left = tf.margin_right = tf.margin_top = tf.margin_bottom = 0
    tf.vertical_anchor = anchor
    rows = content.split("\n") if isinstance(content, str) else list(content)
    for i, row in enumerate(rows):
        p = tf.paragraphs[0] if i == 0 else tf.add_paragraph()
        p.alignment = align; p.line_spacing = line
        if after: p.space_after = Pt(after)
        for seg, b, m in parse(row):
            r = p.add_run(); r.text = seg
            f = r.font
            f.size = Pt(size); f.bold = bold or b; f.color.rgb = color
            f.name = MONO if m else font
            set_ea(r, MONO if m else font)
    return tb

def chip(s, x, y, w, h, text, fill=WHITE, color=INK, size=12.5, bold=False, line=LINE):
    sh = rect(s, x, y, w, h, fill=fill, line=line, lw=1, shape=MSO_SHAPE.ROUNDED_RECTANGLE, radius=0.3)
    tf = sh.text_frame; tf.word_wrap = False; tf.vertical_anchor = MSO_ANCHOR.MIDDLE
    p = tf.paragraphs[0]; p.alignment = PP_ALIGN.CENTER; p.line_spacing = 1.0
    r = p.add_run(); r.text = text
    r.font.size = Pt(size); r.font.bold = bold; r.font.color.rgb = color; r.font.name = FONT
    set_ea(r, FONT)
    return sh

def pic(s, name, x, y, w, h=None, crop_top=0.0, crop_bottom=0.0):
    p = s.shapes.add_picture(os.path.join(ASSETS, name), px(x), px(y), width=px(w))
    if crop_top or crop_bottom:
        p.crop_top = crop_top; p.crop_bottom = crop_bottom
        if h: p.height = px(h)
    return p

def head(s, title, sub=None, page=None):
    """A 标题块：左上角小标题（对齐课程 PPT 风格）+ 极简页脚"""
    txt(s, ML, 44, 900, 36, title, size=27, bold=True, color=INK, line=1.2)
    if sub:
        txt(s, ML, 86, 1000, 22, sub, size=13.5, color=GRAY, line=1.3)
    txt(s, ML, 674, 600, 18, "Tlias 智能学习辅助系统 · 项目答辩", size=10.5, color=SILVER, line=1.2)
    if page:
        txt(s, W - MR - 160, 674, 160, 18, f"{page:02d} / {TOTAL}", size=10.5, color=SILVER,
            align=PP_ALIGN.RIGHT, line=1.2)

def conclusion(s, x, y, w, h, text, size=15):
    """米黄结论条 —— 用户课程 PPT 的标志性元素"""
    rect(s, x, y, w, h, fill=BG)
    rect(s, x, y, 3, h, fill=RED)
    txt(s, x + 22, y + 16, w - 44, h - 28, text, size=size, color=INK, line=1.55,
        anchor=MSO_ANCHOR.MIDDLE)

def arrow_down(s, x, y, h, color=BLUE):
    t = s.shapes.add_shape(MSO_SHAPE.ISOSCELES_TRIANGLE, px(x - 7), px(y), px(14), px(h))
    t.rotation = 180
    t.fill.solid(); t.fill.fore_color.rgb = color
    t.line.fill.background(); t.shadow.inherit = False

def arrow_right(s, x, y, w, color=BLUE):
    t = s.shapes.add_shape(MSO_SHAPE.ISOSCELES_TRIANGLE, px(x), px(y - 7), px(w), px(14))
    t.rotation = 90
    t.fill.solid(); t.fill.fore_color.rgb = color
    t.line.fill.background(); t.shadow.inherit = False

# ============================================================
# 01 封面 —— 极简：白底 + 一条砖红竖线 + 标题 + 落款
# ============================================================
s = new_slide()
rect(s, 0, 0, 8, 720, fill=RED)
txt(s, 96, 92, 500, 24, "课程项目答辩", size=15, color=GRAY, line=1.3)
txt(s, 96, 292, 900, 68, "Tlias 智能学习辅助系统", size=48, bold=True, color=INK, line=1.15)
txt(s, 96, 372, 900, 30, "Spring Boot 3 + Vue 3 前后端分离全栈实战", size=19, color=GRAY, line=1.35)
rect(s, 96, 424, 56, 3, fill=RED)
txt(s, 96, 458, 700, 24, "李胤贤 · 山东政法学院 · 信息管理与信息系统", size=14.5, color=INK, line=1.35)
txt(s, 96, 486, 700, 22, "2026.10", size=13, color=SILVER, line=1.3)
txt(s, 96, 604, 700, 22, "Java 开发工程师方向 · 前后端分离全栈项目", size=12.5, color=SILVER, line=1.3)

# ============================================================
# 02 内容导览
# ============================================================
s = new_slide()
head(s, "内容导览", "十分钟路线：从项目全貌到未来规划", 2)
rect(s, ML, CTOP, 232, 150, fill=BG)
txt(s, ML + 22, CTOP + 24, 190, 26, "总时长", size=13, color=GRAY, line=1.3)
txt(s, ML + 22, CTOP + 52, 190, 34, "约 10 分钟", size=26, bold=True, color=RED, line=1.2)
txt(s, ML + 22, CTOP + 96, 190, 40, "五个板块，逐段推进", size=12.5, color=GRAY, line=1.4)
ROUTE = [
    ("01", "项目全景", "为什么做这个系统、做成什么样子、由哪些模块构成", "P03 – P05"),
    ("02", "技术实现", "三层架构、四个关键技术点，以及员工模块3 的完整闭环", "P06 – P08"),
    ("03", "界面呈现", "真实运行中的员工管理、数据统计、学员与日志界面", "P09 – P11"),
    ("04", "特色与成长", "五条项目亮点，以及我在这条链路上获得的能力", "P12 – P13"),
    ("05", "未来规划", "三阶段升级改造路线：先可维护，再可扩展，最后可规模化", "P14"),
]
for i, (no, t, d, pg) in enumerate(ROUTE):
    y = CTOP + i * 106
    txt(s, ML, y + 4, 60, 34, no, size=26, bold=True, color=RED, line=1.1, font=MONO)
    txt(s, ML + 76, y + 2, 480, 28, t, size=19, bold=True, color=INK, line=1.3)
    txt(s, ML + 76, y + 36, 700, 44, d, size=14, color=GRAY, line=1.5)
    txt(s, W - MR - 160, y + 6, 160, 22, pg, size=12, color=SILVER, align=PP_ALIGN.RIGHT,
        line=1.3, font=MONO)
    if i < 4: rect(s, ML, y + 92, W - ML - MR, 1, fill=LINE)

# ============================================================
# 03 项目背景与目标
# ============================================================
s = new_slide()
head(s, "项目背景与目标", "从课程信息的分散管理，到一套可运营的数据后台", 3)
txt(s, ML, CTOP, 600, 26, "为什么做", size=16, bold=True, color=INK, line=1.3)
PAIN = [
    ("数据散落", "部门、员工、班级、学员分别维护，班级与班主任、学员与班级之间的关联靠人工记忆。"),
    ("变更无痕", "谁在什么时候改了哪条记录没有留痕，出了问题无法回溯，缺少可追责的操作日志。"),
    ("统计靠人", "人数、学历、岗位分布靠人工统计 Excel，数据一改就得重新汇总，统计与明细经常对不上。"),
]
for i, (t, d) in enumerate(PAIN):
    y = CTOP + 42 + i * 118
    txt(s, ML, y, 60, 30, f"0{i+1}", size=22, bold=True, color=RED, line=1.1, font=MONO)
    txt(s, ML + 62, y + 2, 500, 26, t, size=17, bold=True, color=INK, line=1.3)
    txt(s, ML + 62, y + 34, 556, 62, d, size=14, color=GRAY, line=1.6)
rect(s, 716, CTOP, 500, 372, fill=WHITE, line=LINE)
txt(s, 744, CTOP + 24, 440, 26, "项目目标与范围", size=16, bold=True, color=INK, line=1.3)
for i, t in enumerate([
    "打通「部门—员工—班级—学员」四级主数据，员工可追溯工作经历，学员可定位班级。",
    "所有写操作统一事务保护，统计口径全部实时聚合，不落冗余统计表。",
    "完整实现登录鉴权、文件上传与操作日志三条基础能力线。",
]):
    y = CTOP + 66 + i * 92
    rect(s, 744, y + 8, 7, 7, fill=BLUE, shape=MSO_SHAPE.OVAL)
    txt(s, 766, y, 424, 80, t, size=14.5, color=INK, line=1.65)
conclusion(s, 716, CTOP + 392, 500, 128,
           "正因为原业务天然存在「多表关联 + 增删改留痕」的复杂度，才值得用一套完整系统落地；"
           "也正因此，本项目的技术重点落在**关联查询**与**事务一致性**上，而不是页面数量。", 14.5)

# ============================================================
# 04 系统架构与技术栈
# ============================================================
s = new_slide()
head(s, "系统架构与技术栈", "三层分层 + 两项横切能力，改一个业务只动一个目录", 4)
LAYERS = [
    (CTOP, "前端展现层", ["Vue 3", "Element Plus", "Vite 5", "Axios"]),
    (CTOP + 84, "后端业务层", ["Controller", "Service", "Mapper", "MyBatis XML"]),
    (CTOP + 168, "数据持久层", ["MySQL 8.0", "7 张业务表", "utf8mb4", "连接池"]),
]
for ly, name, tags in LAYERS:
    rect(s, ML, ly, 748, 62, fill=WHITE, line=LINE)
    rect(s, ML, ly, 96, 62, fill=BLUE)
    txt(s, ML + 10, ly + 22, 80, 24, name, size=13.5, bold=True, color=WHITE, line=1.2)
    cx = ML + 116
    for t in tags:
        w = 24 + len(t) * 13
        chip(s, cx, ly + 16, w, 30, t, fill=WHITE, color=INK, size=12.5, line=LINE)
        cx += w + 10
arrow_down(s, ML + 374, CTOP + 64, 16)
arrow_down(s, ML + 374, CTOP + 148, 16)
for cy, t, d in [
    (CTOP, "JWT 登录鉴权", "拦截器从自定义 token 头取令牌并校验，未登录一律 401。"),
    (CTOP + 126, "AOP 操作日志", "环绕通知自动记录类名、方法、参数、返回值与耗时。"),
]:
    rect(s, 852, cy, 364, 104, fill=INK)
    txt(s, 880, cy + 20, 320, 24, t, size=16, bold=True, color=WHITE, line=1.2)
    txt(s, 880, cy + 52, 318, 44, d, size=12.5, color=CREAM, line=1.5)
txt(s, ML, 392, 300, 24, "技术栈", size=15, bold=True, color=INK, line=1.3)
STACK = [
    ("后端", ["Spring Boot 3.2.5", "MyBatis 3.0.3", "MySQL Connector/J", "JJWT 0.12.5", "Spring AOP", "Lombok"]),
    ("前端", ["Vue 3.4", "Element Plus 2.7", "Vue Router 4.4", "Vite 5.3", "ECharts 5.5", "Axios 1.6"]),
    ("工程", ["Maven", "Git", "GitHub", "Navicat", "一键启动脚本", "初始化 SQL 随码交付"]),
]
for r, (label, tags) in enumerate(STACK):
    y = 426 + r * 68
    txt(s, ML, y + 8, 60, 24, label, size=14, bold=True, color=RED, line=1.2)
    cx = ML + 66
    for t in tags:
        w = 24 + len(t) * 12.6
        chip(s, cx, y, w, 34, t, fill=WHITE, color=INK, size=12.5, line=LINE)
        cx += w + 10
    if r < 2: rect(s, ML, y + 52, W - ML - MR, 1, fill=LINE)

# ============================================================
# 05 功能模块全景
# ============================================================
s = new_slide()
head(s, "功能模块全景", "7 大业务域全部落地并可操作，接口规范逐条对应", 5)
MODS = [
    ("部门管理", "增删改查 · 唯一性约束", BLUE),
    ("员工管理", "条件分页 · 增删改查 · 工作经历", RED),
    ("班级管理", "开课信息 · 班主任关联", BLUE),
    ("学员管理", "违纪记录 · 班级归属", BLUE),
    ("数据统计", "性别 / 职位 / 学历 / 人数", BLUE),
    ("操作日志", "AOP 自动埋点 · 耗时统计", BLUE),
    ("登录与上传", "JWT 签发 · 头像本地存储", RED),
    ("统一约定", "Result 响应体 · 401 拦截", BLUE),
]
for i, (t, d, c) in enumerate(MODS):
    col, row = i % 2, i // 2
    x = ML + col * 350
    y = CTOP + 12 + row * 120
    rect(s, x, y, 310, 100, fill=WHITE, line=LINE)
    rect(s, x, y, 3, 100, fill=c)
    txt(s, x + 22, y + 22, 270, 26, t, size=16, bold=True, color=INK, line=1.25)
    txt(s, x + 22, y + 54, 272, 38, d, size=12.5, color=GRAY, line=1.45)
STATS = [("30", "个 REST 接口全部实现并实测通过", INK, CTOP + 6),
         ("7", "张业务表：dept / emp / emp_expr / clazz / student / log / emp_log", BLUE, CTOP + 126),
         ("8", "个控制器承载全部业务域，职责边界清晰", RED, CTOP + 246)]
for num, label, c, y in STATS:
    txt(s, 764, y, 200, 78, num, size=72, bold=True, color=c, line=1.0, font=MONO)
    txt(s, 764, y + 80, 452, 42, label, size=14, color=GRAY, line=1.5)
conclusion(s, 764, CTOP + 350, 452, 142,
           "接口数与模块数一一对得上：接口文档里的每一条规范都找到了实现落点，"
           "而不是只做出「看起来像」的界面。", 14.5)

# ============================================================
# 06 核心技术实现
# ============================================================
s = new_slide()
head(s, "核心技术实现", "四个技术点，分别对应：查得准、控得住、留得全、不脏数据", 6)
txt(s, ML, CTOP + 2, 260, 26, "四个关键技术点", size=17, bold=True, color=INK, line=1.3)
for i, k in enumerate(["查得准", "控得住", "留得全", "不脏数据"]):
    txt(s, ML, CTOP + 48 + i * 40, 260, 28, f"0{i+1}   {k}", size=15, bold=True, color=RED, line=1.2)
txt(s, ML, CTOP + 216, 264, 104,
    "不是课程例子的复刻，\n而是我在真实联调中\n遇到过问题、逐个定位解决。",
    size=13.5, color=GRAY, line=1.7)
conclusion(s, ML, CTOP + 376, 264, 116,
           "**04 · 事务与传播**\n两表同生共死，\n日志独立落库", 13.5)
TECH = [
    ("MyBatis 动态 SQL", "姓名、性别、入职日期三条件任意组合分页，条件为空自动跳过对应 where。",
     ['<if test="name != null and', '  name != \'\'">', '  and name like concat(', "    '%', #{name}, '%')", '</if>']),
    ("JWT + 登录拦截器", "登录签发 JWT；拦截器从自定义 token 请求头取令牌，缺失或失效一律 401。",
     ['String token = request', '.getHeader("token");', 'if (token == null) {', '  throw new', '  NotLoginException(); }']),
    ("AOP 操作日志", "环绕通知自动记录类名、方法名、请求参数、返回值与执行耗时，统一落库。",
     ['@Around("execution(* com', '.itheima.controller.*', '.*(..))")', 'public Object around(', '  ProceedingJoinPoint pjp)', '  throws Throwable {']),
    ("事务与传播行为", "save 加 rollbackFor 覆盖全部异常；finally 中以 REQUIRES_NEW 另起事务写日志。",
     ['@Transactional(', '  rollbackFor = Exception.class)', 'public void save(Emp emp) {', '  try { ... } finally {', '    empLogService.insertLog(info); }']),
]
for i, (t, d, code) in enumerate(TECH):
    y = CTOP + i * 134
    txt(s, 360, y + 2, 60, 30, f"0{i+1}", size=20, bold=True, color=RED, line=1.1, font=MONO)
    txt(s, 418, y, 470, 26, t, size=17, bold=True, color=INK, line=1.25)
    txt(s, 418, y + 32, 486, 58, d, size=13.5, color=GRAY, line=1.55)
    rect(s, 934, y, 282, 112, fill=INK)
    rect(s, 934, y, 3, 112, fill=RED)
    txt(s, 950, y + 12, 258, 92, "\n".join(code), size=10.5, color=CODES, line=1.42, font=MONO)
    if i < 3: rect(s, 360, y + 124, 856, 1, fill=LINE)

# ============================================================
# 07 核心业务时序
# ============================================================
s = new_slide()
head(s, "核心业务时序：新增员工", "一次提交写两张表，业务数据与操作日志分属两个事务", 7)
FLOW = [
    ("前端表单提交", "基本信息 + 动态工作经历行", BLUE),
    ("Controller 校验", "JSON 反序列化与必填规则校验", BLUE),
    ("Service 开启主事务", "@Transactional(rollbackFor)", INK),
    ("emp + emp_expr", "两张表在同一事务内写入", INK),
]
bw = (1152 - 3 * 28) / 4
for i, (t, d, c) in enumerate(FLOW):
    x = ML + i * (bw + 28)
    rect(s, x, 148, bw, 88, fill=WHITE, line=LINE)
    rect(s, x, 148, bw, 3, fill=c)
    txt(s, x + 20, 172, bw - 40, 26, t, size=15, bold=True, color=INK, line=1.25)
    txt(s, x + 20, 202, bw - 38, 30, d, size=12.5, color=GRAY, line=1.4)
    if i < 3: arrow_right(s, x + bw + 7, 192, 14, BLUE)
arrow_down(s, ML + 3 * (bw + 28) + bw / 2, 242, 24, RED)
rect(s, ML, 282, 556, 118, fill=WHITE, line=LINE)
rect(s, ML, 282, 3, 118, fill=RED)
txt(s, ML + 24, 300, 500, 26, "业务异常 → 主事务回滚", size=15.5, bold=True, color=INK, line=1.25)
txt(s, ML + 24, 332, 500, 54,
    "emp 与 emp_expr 一并回滚，员工与其工作经历不会留下「只写了一半」的脏数据。",
    size=13, color=GRAY, line=1.55)
arrow_right(s, ML + 572, 336, 18, INK)
rect(s, ML + 606, 282, 546, 118, fill=INK)
txt(s, ML + 632, 300, 490, 26, "finally → emp_log 独立事务提交", size=15.5, bold=True, color=WHITE, line=1.25)
txt(s, ML + 632, 332, 490, 54,
    "REQUIRES_NEW 挂起外层事务另起新事务：即便业务回滚，这次「谁在何时新增了谁」依然留痕。",
    size=13, color=CREAM, line=1.55)
CARDS = [
    ("一致性", "emp 与 emp_expr 共享一个事务边界，任何一步失败整体回滚。"),
    ("传播行为", "REQUIRES_NEW 让操作日志在业务回滚时依然能落库，这正是它的用途。"),
    ("可观测性", "日志表已沉淀 100+ 条真实记录，AOP 埋点在日常使用中持续生效。"),
]
cw = (1152 - 2 * 24) / 3
for i, (t, d) in enumerate(CARDS):
    x = ML + i * (cw + 24)
    rect(s, x, 428, cw, 96, fill=WHITE, line=LINE)
    rect(s, x, 428, 3, 96, fill=BLUE)
    txt(s, x + 22, 448, cw - 44, 26, t, size=15, bold=True, color=INK, line=1.25)
    txt(s, x + 22, 478, cw - 44, 40, d, size=12.5, color=GRAY, line=1.5)
conclusion(s, ML, 544, 1152, 74,
           "这是我在本项目里理解最深的一处：**业务数据与操作日志刻意分属两个事务**——"
           "业务回滚不能抹掉「谁做了什么」的痕迹。", 15)

# ============================================================
# 08 员工模块3 · 修改 / 删除 / 异常 / 统计
# ============================================================
s = new_slide()
head(s, "员工模块3 · 修改 / 删除 / 异常 / 统计",
     "从「能新增」到「能改、能删、能兜底、能统计」的完整闭环", 8)
rect(s, ML, CTOP, 264, 522, fill=INK)
txt(s, ML + 24, CTOP + 30, 216, 30, "04 个功能点", size=22, bold=True, color=WHITE, line=1.2)
rect(s, ML + 24, CTOP + 78, 48, 3, fill=RED)
for i, k in enumerate(["修改员工", "批量删除", "异常处理", "员工统计"]):
    txt(s, ML + 24, CTOP + 104 + i * 44, 216, 30, f"0{i+1}   {k}", size=15, bold=True,
        color=CREAM, line=1.2)
txt(s, ML + 24, CTOP + 296, 216, 120,
    "这四个功能是课程《员工模块3》的全部要求，\n也是「员工管理」真正闭环的最后一公里。",
    size=13, color=RGBColor(0xB8, 0xC2, 0xC0), line=1.75)
conclusion(s, ML + 24, CTOP + 420, 216, 80, "4 类接口\n全部实测通过", 13)
M3 = [
    ("修改员工",
     "一条 left join + resultMap 的 collection，一次查出基本信息与全部工作经历；保存时先删旧经历、再批量插新经历，与基本信息同事务提交。",
     "GET /emps/{id}   ·   PUT /emps"),
    ("批量删除",
     "一个接口同时兼容单删与批量：先按员工 ID 清空 emp_expr 的工作经历，再删 emp 主表，事务保证不残留孤儿数据。",
     "DELETE /emps?ids=1,2,3"),
    ("全局异常处理",
     "emp.phone 加唯一约束，重复手机号会抛 DuplicateKeyException；全局异常处理器将其翻译为「该手机号已被其他员工占用」这类可读提示，而非框架默认的 500 堆栈。",
     "DuplicateKeyException  ->  code:0"),
    ("员工信息统计",
     "case when 把 job 翻译成职位名后 group by 聚合，结果用 JobOption 封装为 jobList / dataList，前端 ECharts 直接消费，服务端不画图。",
     "GET /report/empJobData  ·  /empGenderData"),
]
for i, (t, d, code) in enumerate(M3):
    col, row = i % 2, i // 2
    x = 364 + col * 436
    y = CTOP + row * 268
    rect(s, x, y, 416, 250, fill=WHITE, line=LINE)
    rect(s, x, y, 416, 3, fill=BLUE if i % 2 == 0 else RED)
    txt(s, x + 24, y + 24, 60, 26, f"0{i+1}", size=17, bold=True, color=RED, line=1.2, font=MONO)
    txt(s, x + 24, y + 58, 360, 28, t, size=17, bold=True, color=INK, line=1.25)
    txt(s, x + 24, y + 96, 368, 92, d, size=13, color=GRAY, line=1.62)
    rect(s, x + 24, y + 196, 368, 34, fill=BG)
    txt(s, x + 36, y + 205, 348, 22, code, size=11.5, color=RED, line=1.3, font=MONO)

# ============================================================
# 09 界面呈现 · 员工管理（Hero）
# ============================================================
s = new_slide()
head(s, "界面呈现 · 员工管理", "真实运行中的系统界面（非设计稿、非 Mock）", 9)
pic(s, "03_employee_list.png", ML, 118, 1152, 470, crop_bottom=0.27)
conclusion(s, ML, 604, 1152, 56,
           "左侧六模块导航、条件查询（姓名 / 性别 / 入职日期）、分页表格与批量删除**同时可用**——"
           "说明动态 SQL 与批量 SQL 都经过了真实数据验证。", 14.5)

# ============================================================
# 10 界面呈现 · 数据统计
# ============================================================
s = new_slide()
head(s, "界面呈现 · 数据统计", "四组统计图表，全部由后端实时聚合返回", 10)
pic(s, "05_report.png", ML, 132, 764, 430)
txt(s, 856, CTOP + 4, 360, 28, "四组统计，实时聚合", size=17, bold=True, color=INK, line=1.3)
ITEMS = [
    ("员工性别统计", "饼图：男女比例分布"),
    ("员工职位人数", "柱图：五类岗位人数对比"),
    ("学员学历统计", "饼图：本科 / 硕士 / 初中占比"),
    ("班级人数统计", "柱图：三个班级在读人数"),
]
for i, (t, d) in enumerate(ITEMS):
    y = CTOP + 48 + i * 66
    rect(s, 856, y + 9, 7, 7, fill=BLUE, shape=MSO_SHAPE.OVAL)
    txt(s, 876, y, 340, 24, t, size=14.5, bold=True, color=INK, line=1.3)
    txt(s, 876, y + 26, 340, 22, d, size=12.5, color=GRAY, line=1.4)
conclusion(s, 856, CTOP + 336, 360, 116,
           "统计接口**不落库**、全部实时聚合，数据源唯一，避免了「统计结果与明细对不上」"
           "这一常见问题。", 13.5)

# ============================================================
# 11 界面呈现 · 学员与操作日志
# ============================================================
s = new_slide()
head(s, "界面呈现 · 学员管理与操作日志", "业务数据与系统留痕，两条线同时可查", 11)
pic(s, "06_student.png", ML, 128, 636, 358)
pic(s, "08_log.png", 736, 128, 480, 270)
txt(s, 736, 412, 480, 24, "操作日志（AOP 自动埋点）", size=14, bold=True, color=INK, line=1.3)
txt(s, 736, 440, 480, 40, "记录类名、方法名、请求参数、返回值与耗时", size=12.5, color=GRAY, line=1.45)
conclusion(s, ML, 516, 1152, 102,
           "左侧学员管理含违纪次数、扣分与班级归属字段；右侧日志表已沉淀 **100+ 条真实操作记录**——"
           "AOP 埋点在日常使用中持续生效，这是系统「能自证运行」的运维底座。", 15)

# ============================================================
# 12 项目特色亮点
# ============================================================
s = new_slide()
head(s, "项目特色亮点", "前四条证明技术深度，第五条证明工程习惯", 12)
HILITE = [
    ("按接口文档 100% 交付", "接口文档里写明的 30 条规范全部实现并实测通过，没有「差不多」的近似实现。"),
    ("端到端真实可运行", "真实 MySQL 库、真实前后端联调、真实浏览器操作，不依赖任何 Mock 或假数据。"),
    ("跨表事务一致性", "新增员工时 emp 与 emp_expr 同事务写入，并指定 rollbackFor 覆盖全部异常。"),
    ("事务传播行为落地", "emp_log 以 REQUIRES_NEW 独立提交，业务回滚时操作痕迹依然留痕。"),
    ("工程化闭环", "Git 版本管理、GitHub 公开仓库、一键启动脚本、初始化 SQL 随码交付。"),
]
for i, (t, d) in enumerate(HILITE):
    y = CTOP + 6 + i * 104
    txt(s, ML, y, 76, 44, f"0{i+1}", size=38, bold=True, color=RED, line=1.05, font=MONO)
    txt(s, ML + 88, y + 2, 400, 28, t, size=18, bold=True, color=INK, line=1.3)
    txt(s, ML + 88, y + 36, 420, 56, d, size=13.5, color=GRAY, line=1.55)
    if i < 4: rect(s, ML, y + 92, W - ML - MR, 1, fill=LINE)
txt(s, 660, CTOP + 30, 556, 28, "与课程示例的差异", size=16, bold=True, color=INK, line=1.3)
rect(s, 660, CTOP + 70, 556, 148, fill=WHITE, line=LINE)
for i, t in enumerate([
    "课程示例中「新增员工」只演示成功路径，我补齐了异常回滚与日志留痕两条分支。",
    "示例里操作日志靠手动调用，我改为 AOP 环绕通知自动埋点，零侵入。",
]):
    y = CTOP + 92 + i * 66
    rect(s, 682, y + 8, 7, 7, fill=BLUE, shape=MSO_SHAPE.OVAL)
    txt(s, 702, y, 494, 58, t, size=13.5, color=INK, line=1.6)
conclusion(s, 660, CTOP + 240, 556, 130,
           "前四条是**技术深度**，第五条是**工程习惯**——两者合起来，才是一个「能被团队接手」的系统。", 14.5)

# ============================================================
# 13 个人成长与收获
# ============================================================
s = new_slide()
head(s, "个人成长与收获", "最大的成长不是某个 API 会用，而是完整链路走通了一遍", 13)
rect(s, ML, CTOP, 264, 240, fill=WHITE, line=LINE)
txt(s, ML + 24, CTOP + 24, 216, 26, "起点", size=15, bold=True, color=GRAY, line=1.3)
txt(s, ML + 24, CTOP + 62, 216, 160,
    "会用 Java 语法与面向对象\n能用 JDBC 写单表增删改查\n做过 Python + Flask 的小型后端\n"
    "没有写过完整的前后端分离项目", size=13.5, color=INK, line=1.85)
arrow_right(s, ML + 274, CTOP + 108, 20, RED)
rect(s, ML + 314, CTOP, 300, 240, fill=INK)
txt(s, ML + 338, CTOP + 24, 252, 26, "现在", size=15, bold=True, color=CREAM, line=1.3)
txt(s, ML + 338, CTOP + 62, 252, 160,
    "独立完成 30 个接口的全栈系统\n落地 JWT 鉴权、AOP 日志与事务传播\n"
    "能定位端口占用、MyBatis 映射未加载、代理证书等问题\n养成了版本管理与交付意识",
    size=13.5, color=CREAM, line=1.85)
rect(s, 700, CTOP, 516, 240, fill=BG)
txt(s, 724, CTOP + 24, 468, 26, "三条具体收获", size=15, bold=True, color=INK, line=1.3)
for i, (t, d) in enumerate([
    ("把需求文档当唯一事实来源", "接口文档里没写的，我不臆造；文档里写了的，逐条找到落点。"),
    ("排错能力", "端口被优酷占用、mapper-locations 漏配、代理 TLS 拦截——都能独立定位根因。"),
    ("交付意识", "SQL 随码交付、启动脚本、Git 提交信息与 README，让别人能接手。"),
]):
    y = CTOP + 62 + i * 58
    txt(s, 724, y, 468, 24, f"· {t}", size=13.5, bold=True, color=RED, line=1.3)
    txt(s, 724, y + 24, 468, 32, d, size=12.5, color=INK, line=1.5)
txt(s, ML, CTOP + 272, 1152, 26, "能力矩阵：从单点技术到完整链路", size=16, bold=True, color=INK, line=1.3)
SKILLS = [
    ("Java / 面向对象", 40, "由语法学习到用继承、多态与接口组织可扩展的业务分层"),
    ("MySQL / SQL", 70, "由单表 CRUD 到三条件动态分页、联表统计与七表设计"),
    ("Spring Boot / MyBatis", 75, "由看懂示例到独立完成分层架构、事务与 AOP 埋点"),
    ("前端 / Vue3", 60, "由静态页面到组件化页面、表格分页、ECharts 与文件上传"),
    ("工程 / Git", 65, "由本地写代码到版本管理、远端仓库与一键启动交付"),
]
for i, (t, pct, d) in enumerate(SKILLS):
    y = CTOP + 312 + i * 60
    txt(s, ML, y, 190, 24, t, size=13.5, bold=True, color=INK, line=1.25)
    rect(s, ML + 200, y + 6, 420, 12, fill=LINE)
    rect(s, ML + 200, y + 6, int(420 * pct / 100), 12, fill=BLUE)
    txt(s, ML + 636, y, 300, 24, d, size=12.5, color=GRAY, line=1.4)

# ============================================================
# 14 后期升级改造规划
# ============================================================
s = new_slide()
head(s, "后期升级改造规划", "先可维护、再可扩展、最后可规模化，不做过度设计", 14)
PLAN = [
    ("阶段一 · 工程加固", "补齐工程规范", 276, 244, [
        "接入 OpenAPI / Swagger，接口文档同源",
        "为核心 Service 补单元测试与集成测试",
        "异常日志分级，增加链路追踪 ID",
    ]),
    ("阶段二 · 能力扩展", "扩展系统能力", 200, 320, [
        "引入 Redis 缓存统计结果与登录态续期",
        "头像存储迁移至对象存储（OSS / MinIO）",
        "补齐 RBAC 角色权限，区分管理端与学员端",
        "文件上传增加类型与体积校验",
    ]),
    ("阶段三 · 架构演进", "提升规模化", 124, 396, [
        "Docker 容器化 + CI/CD 自动构建部署",
        "前端升级 Vue3 + TypeScript 抽离公共组件库",
        "接入监控告警与慢查询分析",
        "支撑多实例部署与配置中心治理",
    ]),
]
for i, (t, goal, y, h, items) in enumerate(PLAN):
    x = ML + i * 384
    rect(s, x, y, 352, h, fill=WHITE, line=LINE)
    rect(s, x, y, 352, 3, fill=BLUE if i < 2 else RED)
    txt(s, x + 24, y + 20, 304, 24, t, size=15, bold=True, color=INK, line=1.25)
    txt(s, x + 24, y + 50, 304, 22, goal, size=12.5, bold=True, color=RED, line=1.25)
    for j, it in enumerate(items):
        yy = y + 86 + j * 52
        rect(s, x + 24, yy + 8, 6, 6, fill=BLUE if i < 2 else RED, shape=MSO_SHAPE.OVAL)
        txt(s, x + 44, yy, 284, 46, it, size=12.5, color=GRAY, line=1.5)
conclusion(s, ML, 540, 1152, 78,
           "改造顺序严格遵循**工程加固 → 能力扩展 → 架构演进**，与项目当前规模匹配："
           "在还没有并发压力的阶段引入分布式组件，只会增加维护成本。", 15)

# ============================================================
# 15 结束页
# ============================================================
s = new_slide()
rect(s, 0, 0, 8, 720, fill=RED)
txt(s, 96, 92, 500, 24, "Tlias 智能学习辅助系统", size=14, color=GRAY, line=1.3)
txt(s, 96, 268, 1000, 120,
    "从一份接口文档，\n到一套能跑起来、能被接手、能继续长大的系统。",
    size=34, bold=True, color=INK, line=1.45)
rect(s, 96, 420, 56, 3, fill=RED)
txt(s, 96, 456, 700, 24, "李胤贤 · 山东政法学院 · 信息管理与信息系统", size=14.5, color=INK, line=1.35)
txt(s, 96, 486, 700, 22, "2026.10", size=13, color=SILVER, line=1.3)
txt(s, 96, 604, 900, 22, "代码仓库：github.com/liynxian1006/tlias", size=12.5, color=SILVER, line=1.3, font=MONO)

prs.save(OUT)
print("saved:", OUT, "| 页数:", len(prs.slides._sldIdLst))
