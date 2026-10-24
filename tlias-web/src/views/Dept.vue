<template>
  <div>
    <el-card>
      <div class="toolbar">
        <el-button type="primary" :icon="Plus" @click="openAdd">新增部门</el-button>
      </div>
      <el-table :data="list" border stripe v-loading="loading">
        <el-table-column type="index" label="序号" width="70" />
        <el-table-column prop="name" label="部门名称" />
        <el-table-column prop="createTime" label="创建时间" />
        <el-table-column prop="updateTime" label="修改时间" />
        <el-table-column label="操作" width="200">
          <template #default="{ row }">
            <el-button size="small" @click="openEdit(row)">编辑</el-button>
            <el-button size="small" type="danger" @click="remove(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="dialogVisible" :title="form.id ? '编辑部门' : '新增部门'" width="400px">
      <el-form :model="form" label-width="80px">
        <el-form-item label="部门名称" required>
          <el-input v-model="form.name" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { Plus } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { deptListApi, deptAddApi, deptUpdateApi, deptDeleteApi } from '../api'

const list = ref([])
const loading = ref(false)
const dialogVisible = ref(false)
const form = ref({ id: null, name: '' })

const load = async () => {
  loading.value = true
  try {
    list.value = await deptListApi()
  } finally {
    loading.value = false
  }
}

const openAdd = () => {
  form.value = { id: null, name: '' }
  dialogVisible.value = true
}

const openEdit = (row) => {
  form.value = { id: row.id, name: row.name }
  dialogVisible.value = true
}

const submit = async () => {
  if (!form.value.name) {
    ElMessage.warning('请输入部门名称')
    return
  }
  if (form.value.id) {
    await deptUpdateApi({ id: form.value.id, name: form.value.name })
  } else {
    await deptAddApi({ name: form.value.name })
  }
  ElMessage.success('保存成功')
  dialogVisible.value = false
  load()
}

const remove = (row) => {
  ElMessageBox.confirm(`确认删除部门【${row.name}】？`, '提示', { type: 'warning' })
    .then(async () => {
      await deptDeleteApi(row.id)
      ElMessage.success('删除成功')
      load()
    })
    .catch(() => {})
}

onMounted(load)
</script>

<style scoped>
.toolbar {
  margin-bottom: 14px;
}
</style>
