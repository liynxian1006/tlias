export const genderMap = { 1: '男', 2: '女' }

export const jobMap = {
  1: '班主任',
  2: '讲师',
  3: '学工主管',
  4: '教研主管',
  5: '咨询师'
}

export const degreeMap = {
  1: '初中',
  2: '高中',
  3: '大专',
  4: '本科',
  5: '硕士',
  6: '博士'
}

export const subjectMap = {
  1: 'Java',
  2: '前端',
  3: '大数据',
  4: 'Python',
  5: 'Go',
  6: '嵌入式'
}

export const clazzStatusMap = {
  '未开班': 'info',
  '已开班': 'success',
  '已结课': 'warning'
}

// 图片地址处理：相对路径（/images/...）需要拼接 /api 前缀，远程地址直接使用
export function resolveImg(url) {
  if (!url) return ''
  if (url.startsWith('http')) return url
  return '/api' + url
}
