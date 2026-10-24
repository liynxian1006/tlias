import { createRouter, createWebHistory } from 'vue-router'
import Login from '../views/Login.vue'

const routes = [
  { path: '/login', name: 'Login', component: Login },
  {
    path: '/',
    component: () => import('../views/Layout.vue'),
    redirect: '/dept',
    children: [
      { path: 'dept', name: 'Dept', component: () => import('../views/Dept.vue') },
      { path: 'emp', name: 'Emp', component: () => import('../views/Emp.vue') },
      { path: 'clazz', name: 'Clazz', component: () => import('../views/Clazz.vue') },
      { path: 'student', name: 'Student', component: () => import('../views/Student.vue') },
      { path: 'report', name: 'Report', component: () => import('../views/Report.vue') },
      { path: 'log', name: 'Log', component: () => import('../views/Log.vue') }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 登录拦截：未携带 token 跳转到登录页
router.beforeEach((to) => {
  const token = localStorage.getItem('tlias_token')
  if (to.path !== '/login' && !token) {
    return '/login'
  }
  if (to.path === '/login' && token) {
    return '/'
  }
})

export default router
