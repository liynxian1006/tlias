<template>
  <div class="login-container">
    <el-card class="login-card">
      <h2 class="title">Tlias 智能学习辅助系统</h2>
      <el-form :model="form" :rules="rules" ref="formRef" label-width="0">
        <el-form-item prop="username">
          <el-input v-model="form.username" placeholder="用户名" :prefix-icon="User" />
        </el-form-item>
        <el-form-item prop="password">
          <el-input v-model="form.password" type="password" placeholder="密码" :prefix-icon="Lock" show-password />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" style="width: 100%" :loading="loading" @click="onSubmit">登录</el-button>
        </el-form-item>
      </el-form>
      <div class="tip">测试账号：songjiang / 123456</div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { User, Lock } from '@element-plus/icons-vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { loginApi } from '../api'

const router = useRouter()
const formRef = ref()
const loading = ref(false)
const form = reactive({ username: 'songjiang', password: '123456' })
const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

const onSubmit = () => {
  formRef.value.validate(async (valid) => {
    if (!valid) return
    loading.value = true
    try {
      const data = await loginApi(form)
      localStorage.setItem('tlias_token', data.token)
      localStorage.setItem(
        'tlias_user',
        JSON.stringify({ id: data.id, name: data.name, username: data.username })
      )
      ElMessage.success('登录成功')
      router.push('/')
    } catch (e) {
      // 错误由拦截器统一提示
    } finally {
      loading.value = false
    }
  })
}
</script>

<style scoped>
.login-container {
  height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #1f2d3d, #3a506b);
}
.login-card {
  width: 360px;
}
.title {
  text-align: center;
  margin-bottom: 22px;
  color: #303133;
}
.tip {
  text-align: center;
  color: #999;
  font-size: 12px;
}
</style>
