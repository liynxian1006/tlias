<template>
  <el-container class="layout">
    <el-aside width="210px" class="aside">
      <div class="logo">Tlias</div>
      <el-menu
        :default-active="activeMenu"
        router
        background-color="#304156"
        text-color="#bfcbd9"
        active-text-color="#409EFF"
      >
        <el-menu-item index="/dept"><el-icon><OfficeBuilding /></el-icon>部门管理</el-menu-item>
        <el-menu-item index="/emp"><el-icon><User /></el-icon>员工管理</el-menu-item>
        <el-menu-item index="/clazz"><el-icon><School /></el-icon>班级管理</el-menu-item>
        <el-menu-item index="/student"><el-icon><Reading /></el-icon>学员管理</el-menu-item>
        <el-menu-item index="/report"><el-icon><PieChart /></el-icon>数据统计</el-menu-item>
        <el-menu-item index="/log"><el-icon><Document /></el-icon>日志管理</el-menu-item>
      </el-menu>
    </el-aside>
    <el-container>
      <el-header class="header">
        <span class="sys-title">Tlias 智能学习辅助系统</span>
        <div class="user">
          <span>欢迎，{{ user.name }}</span>
          <el-button text type="primary" @click="logout">退出登录</el-button>
        </div>
      </el-header>
      <el-main>
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessageBox } from 'element-plus'

const route = useRoute()
const router = useRouter()
const user = JSON.parse(localStorage.getItem('tlias_user') || '{}')
const activeMenu = computed(() => '/' + (route.path.split('/')[1] || 'dept'))

const logout = () => {
  ElMessageBox.confirm('确认退出登录？', '提示', { type: 'warning' })
    .then(() => {
      localStorage.removeItem('tlias_token')
      localStorage.removeItem('tlias_user')
      router.push('/login')
    })
    .catch(() => {})
}
</script>

<style scoped>
.layout {
  height: 100vh;
}
.aside {
  background: #304156;
}
.logo {
  height: 60px;
  line-height: 60px;
  text-align: center;
  color: #fff;
  font-size: 22px;
  font-weight: bold;
  background: #2b3a4d;
}
.el-menu {
  border-right: none;
}
.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: #fff;
  border-bottom: 1px solid #eee;
}
.sys-title {
  font-size: 18px;
  font-weight: bold;
  color: #303133;
}
.user {
  display: flex;
  align-items: center;
  gap: 12px;
}
</style>
