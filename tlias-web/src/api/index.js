import request from '../utils/request'

// 登录
export const loginApi = (data) => request.post('/login', data).then((r) => r.data)

// 部门
export const deptListApi = () => request.get('/depts').then((r) => r.data)
export const deptAddApi = (data) => request.post('/depts', data)
export const deptUpdateApi = (data) => request.put('/depts', data)
export const deptDeleteApi = (id) => request.delete('/depts', { params: { id } })
export const deptGetApi = (id) => request.get(`/depts/${id}`).then((r) => r.data)

// 员工
export const empPageApi = (params) => request.get('/emps', { params }).then((r) => r.data)
export const empListApi = () => request.get('/emps/list').then((r) => r.data)
export const empAddApi = (data) => request.post('/emps', data)
export const empUpdateApi = (data) => request.put('/emps', data)
export const empGetApi = (id) => request.get(`/emps/${id}`).then((r) => r.data)
export const empDeleteApi = (ids) => request.delete('/emps', { params: { ids: ids.join(',') } })

// 班级
export const clazzPageApi = (params) => request.get('/clazzs', { params }).then((r) => r.data)
export const clazzListApi = () => request.get('/clazzs/list').then((r) => r.data)
export const clazzAddApi = (data) => request.post('/clazzs', data)
export const clazzUpdateApi = (data) => request.put('/clazzs', data)
export const clazzGetApi = (id) => request.get(`/clazzs/${id}`).then((r) => r.data)
export const clazzDeleteApi = (id) => request.delete(`/clazzs/${id}`)

// 学员
export const studentPageApi = (params) => request.get('/students', { params }).then((r) => r.data)
export const studentAddApi = (data) => request.post('/students', data)
export const studentUpdateApi = (data) => request.put('/students', data)
export const studentGetApi = (id) => request.get(`/students/${id}`).then((r) => r.data)
export const studentDeleteApi = (ids) => request.delete(`/students/${ids.join(',')}`)
export const studentViolationApi = (id, score) => request.put(`/students/violation/${id}/${score}`)

// 数据统计
export const reportEmpGenderApi = () => request.get('/report/empGenderData').then((r) => r.data)
export const reportEmpJobApi = () => request.get('/report/empJobData').then((r) => r.data)
export const reportStudentDegreeApi = () => request.get('/report/studentDegreeData').then((r) => r.data)
export const reportStudentCountApi = () => request.get('/report/studentCountData').then((r) => r.data)

// 文件上传
export const uploadApi = (file) => {
  const formData = new FormData()
  formData.append('file', file)
  return request
    .post('/upload', formData, { headers: { 'Content-Type': 'multipart/form-data' } })
    .then((r) => r.data)
}

// 日志
export const logPageApi = (params) => request.get('/log/page', { params }).then((r) => r.data)
