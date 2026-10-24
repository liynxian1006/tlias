<template>
  <div>
    <el-card class="search-card">
      <el-form :model="search" inline>
        <el-form-item label="姓名">
          <el-input v-model="search.name" placeholder="请输入" clearable />
        </el-form-item>
        <el-form-item label="学历">
          <el-select v-model="search.degree" placeholder="不限" clearable style="width: 120px">
            <el-option v-for="(v, k) in degreeMap" :key="k" :label="v" :value="Number(k)" />
          </el-select>
        </el-form-item>
        <el-form-item label="班级">
          <el-select v-model="search.clazzId" placeholder="不限" clearable style="width: 160px">
            <el-option v-for="c in clazzs" :key="c.id" :label="c.name" :value="c.id" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :icon="Search" @click="load">查询</el-button>
          <el-button :icon="Refresh" @click="reset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card>
      <div class="toolbar">
        <el-button type="primary" :icon="Plus" @click="openAdd">新增学员</el-button>
      </div>
      <el-table :data="list" border stripe v-loading="loading">
        <el-table-column type="index" label="序号" width="70" />
        <el-table-column prop="name" label="姓名" width="90" />
        <el-table-column prop="no" label="学号" width="120" />
        <el-table-column label="性别" width="70">
          <template #default="{ row }">{{ genderMap[row.gender] }}</template>
        </el-table-column>
        <el-table-column prop="phone" label="手机号" width="130" />
        <el-table-column label="学历" width="80">
          <template #default="{ row }">{{ degreeMap[row.degree] }}</template>
        </el-table-column>
        <el-table-column prop="clazzName" label="班级" width="120" />
        <el-table-column label="违纪" width="110">
          <template #default="{ row }">{{ row.violationCount }} 次 / {{ row.violationScore }} 分</template>
        </el-table-column>
        <el-table-column label="操作" width="220">
          <template #default="{ row }">
            <el-button size="small" @click="openEdit(row)">编辑</el-button>
            <el-button size="small" type="warning" @click="violation(row)">违纪</el-button>
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

    <el-dialog v-model="dialogVisible" :title="form.id ? '编辑学员' : '新增学员'" width="520px">
      <el-form :model="form" label-width="90px">
        <el-form-item label="姓名" required><el-input v-model="form.name" /></el-form-item>
        <el-form-item label="学号" required><el-input v-model="form.no" /></el-form-item>
        <el-form-item label="性别" required>
          <el-radio-group v-model="form.gender">
            <el-radio :value="1">男</el-radio>
            <el-radio :value="2">女</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="手机号"><el-input v-model="form.phone" /></el-form-item>
        <el-form-item label="学历">
          <el-select v-model="form.degree" placeholder="请选择" style="width: 200px">
            <el-option v-for="(v, k) in degreeMap" :key="k" :label="v" :value="Number(k)" />
          </el-select>
        </el-form-item>
        <el-form-item label="身份证号"><el-input v-model="form.idCard" /></el-form-item>
        <el-form-item label="是否院校生">
          <el-radio-group v-model="form.isCollege">
            <el-radio :value="1">是</el-radio>
            <el-radio :value="0">否</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="联系地址"><el-input v-model="form.address" /></el-form-item>
        <el-form-item label="毕业时间">
          <el-date-picker v-model="form.graduationDate" type="date" value-format="YYYY-MM-DD" />
        </el-form-item>
        <el-form-item label="班级">
          <el-select v-model="form.clazzId" placeholder="请选择" style="width: 200px">
            <el-option v-for="c in clazzs" :key="c.id" :label="c.name" :value="c.id" />
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
  studentPageApi, studentAddApi, studentUpdateApi, studentGetApi, studentDeleteApi,
  studentViolationApi, clazzListApi
} from '../api'
import { genderMap, degreeMap } from '../utils'

const list = ref([])
const total = ref(0)
const loading = ref(false)
const clazzs = ref([])
const dialogVisible = ref(false)
const search = reactive({ name: '', degree: '', clazzId: '', page: 1, pageSize: 10 })

const emptyForm = () => ({
  id: null, name: '', no: '', gender: 1, phone: '', degree: '', idCard: '',
  isCollege: 0, address: '', graduationDate: '', clazzId: ''
})
const form = reactive(emptyForm())

const load = async () => {
  loading.value = true
  try {
    const params = {
      page: search.page,
      pageSize: search.pageSize,
      name: search.name || undefined,
      degree: search.degree || undefined,
      clazzId: search.clazzId || undefined
    }
    const data = await studentPageApi(params)
    list.value = data.rows
    total.value = data.total
  } finally {
    loading.value = false
  }
}

const reset = () => {
  search.name = ''
  search.degree = ''
  search.clazzId = ''
  search.page = 1
  load()
}

const openAdd = () => {
  Object.assign(form, emptyForm())
  dialogVisible.value = true
}

const openEdit = async (row) => {
  const data = await studentGetApi(row.id)
  Object.assign(form, data)
  dialogVisible.value = true
}

const remove = (row) => {
  ElMessageBox.confirm(`确认删除学员【${row.name}】？`, '提示', { type: 'warning' })
    .then(async () => {
      await studentDeleteApi([row.id])
      ElMessage.success('删除成功')
      load()
    })
    .catch(() => {})
}

const violation = (row) => {
  ElMessageBox.prompt('请输入扣分数', '违纪处理', { inputPattern: /^\d+$/, inputErrorMessage: '请输入正整数' })
    .then(async ({ value }) => {
      await studentViolationApi(row.id, Number(value))
      ElMessage.success('处理成功')
      load()
    })
    .catch(() => {})
}

const submit = async () => {
  if (!form.name || !form.no) {
    ElMessage.warning('请填写姓名和学号')
    return
  }
  const payload = { ...form }
  if (form.id) await studentUpdateApi(payload)
  else await studentAddApi(payload)
  ElMessage.success('保存成功')
  dialogVisible.value = false
  load()
}

onMounted(async () => {
  clazzs.value = await clazzListApi()
  load()
})
</script>

<style scoped>
.search-card { margin-bottom: 14px; }
.toolbar { margin-bottom: 14px; }
.pager { margin-top: 16px; justify-content: flex-end; }
</style>
