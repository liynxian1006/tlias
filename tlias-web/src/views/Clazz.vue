<template>
  <div>
    <el-card class="search-card">
      <el-form :model="search" inline>
        <el-form-item label="班级名称">
          <el-input v-model="search.name" placeholder="请输入" clearable />
        </el-form-item>
        <el-form-item label="结课日期">
          <el-date-picker
            v-model="search.range"
            type="daterange"
            range-separator="至"
            start-placeholder="开始"
            end-placeholder="结束"
            value-format="YYYY-MM-DD"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :icon="Search" @click="load">查询</el-button>
          <el-button :icon="Refresh" @click="reset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card>
      <div class="toolbar">
        <el-button type="primary" :icon="Plus" @click="openAdd">新增班级</el-button>
      </div>
      <el-table :data="list" border stripe v-loading="loading">
        <el-table-column type="index" label="序号" width="70" />
        <el-table-column prop="name" label="班级名称" />
        <el-table-column prop="room" label="教室" width="90" />
        <el-table-column prop="beginDate" label="开课日期" width="120" />
        <el-table-column prop="endDate" label="结课日期" width="120" />
        <el-table-column prop="masterName" label="班主任" width="100" />
        <el-table-column label="学科" width="90">
          <template #default="{ row }">{{ subjectMap[row.subject] }}</template>
        </el-table-column>
        <el-table-column label="状态" width="90">
          <template #default="{ row }">
            <el-tag :type="clazzStatusMap[row.status] || 'info'">{{ row.status }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="160">
          <template #default="{ row }">
            <el-button size="small" @click="openEdit(row)">编辑</el-button>
            <el-button size="small" type="danger" @click="remove(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        class="pager"
        background
        layout="total, prev, pager, next"
        :total="total"
        :page-size="search.pageSize"
        v-model:current-page="search.page"
        @current-change="load"
      />
    </el-card>

    <el-dialog v-model="dialogVisible" :title="form.id ? '编辑班级' : '新增班级'" width="500px">
      <el-form :model="form" label-width="90px">
        <el-form-item label="班级名称" required><el-input v-model="form.name" /></el-form-item>
        <el-form-item label="教室"><el-input v-model="form.room" /></el-form-item>
        <el-form-item label="开课日期">
          <el-date-picker v-model="form.beginDate" type="date" value-format="YYYY-MM-DD" />
        </el-form-item>
        <el-form-item label="结课日期">
          <el-date-picker v-model="form.endDate" type="date" value-format="YYYY-MM-DD" />
        </el-form-item>
        <el-form-item label="班主任">
          <el-select v-model="form.masterId" placeholder="请选择" style="width: 200px">
            <el-option v-for="e in emps" :key="e.id" :label="e.name" :value="e.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="学科">
          <el-select v-model="form.subject" placeholder="请选择" style="width: 200px">
            <el-option v-for="(v, k) in subjectMap" :key="k" :label="v" :value="Number(k)" />
          </el-select>
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
import { Plus, Search, Refresh } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  clazzPageApi, clazzListApi, clazzAddApi, clazzUpdateApi, clazzDeleteApi, empListApi
} from '../api'
import { subjectMap, clazzStatusMap } from '../utils'

const list = ref([])
const total = ref(0)
const loading = ref(false)
const emps = ref([])
const dialogVisible = ref(false)
const search = reactive({ name: '', range: [], page: 1, pageSize: 10 })

const emptyForm = () => ({
  id: null, name: '', room: '', beginDate: '', endDate: '', masterId: '', subject: ''
})
const form = reactive(emptyForm())

const load = async () => {
  loading.value = true
  try {
    const params = {
      page: search.page,
      pageSize: search.pageSize,
      name: search.name || undefined,
      begin: search.range ? search.range[0] : undefined,
      end: search.range ? search.range[1] : undefined
    }
    const data = await clazzPageApi(params)
    list.value = data.rows
    total.value = data.total
  } finally {
    loading.value = false
  }
}

const reset = () => {
  search.name = ''
  search.range = []
  search.page = 1
  load()
}

const openAdd = () => {
  Object.assign(form, emptyForm())
  dialogVisible.value = true
}

const openEdit = async (row) => {
  const data = await clazzListApi()
  const target = data.find((c) => c.id === row.id) || row
  Object.assign(form, target)
  dialogVisible.value = true
}

const remove = (row) => {
  ElMessageBox.confirm(`确认删除班级【${row.name}】？`, '提示', { type: 'warning' })
    .then(async () => {
      await clazzDeleteApi(row.id)
      ElMessage.success('删除成功')
      load()
    })
    .catch(() => {})
}

const submit = async () => {
  if (!form.name) {
    ElMessage.warning('请填写班级名称')
    return
  }
  const payload = { ...form }
  if (form.id) await clazzUpdateApi(payload)
  else await clazzAddApi(payload)
  ElMessage.success('保存成功')
  dialogVisible.value = false
  load()
}

onMounted(async () => {
  emps.value = await empListApi()
  load()
})
</script>

<style scoped>
.search-card { margin-bottom: 14px; }
.toolbar { margin-bottom: 14px; }
.pager { margin-top: 16px; justify-content: flex-end; }
</style>
