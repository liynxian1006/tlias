import axios from 'axios'
import { ElMessage } from 'element-plus'
import router from '../router'

const service = axios.create({
  baseURL: '/api',
  timeout: 15000
})

// 请求拦截：自动携带 token
service.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('tlias_token')
    if (token) {
      config.headers['token'] = token
    }
    return config
  },
  (error) => Promise.reject(error)
)

// 响应拦截：统一处理 code 与 401
service.interceptors.response.use(
  (response) => {
    const res = response.data
    if (res.code === 1) {
      return res
    }
    ElMessage.error(res.msg || '请求失败')
    return Promise.reject(res)
  },
  (error) => {
    if (error.response && error.response.status === 401) {
      ElMessage.error('登录已过期，请重新登录')
      localStorage.removeItem('tlias_token')
      localStorage.removeItem('tlias_user')
      router.push('/login')
    } else {
      ElMessage.error((error.response && error.response.data && error.response.data.msg) || error.message || '网络错误')
    }
    return Promise.reject(error)
  }
)

export default service
