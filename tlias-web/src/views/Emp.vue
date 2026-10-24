<template>
  <div>
    <el-card class="search-card">
      <el-form :model="search" inline>
        <el-form-item label="姓名">
          <el-input v-model="search.name" placeholder="请输入姓名" clearable />
        </el-form-item>
        <el-form-item label="性别">
          <el-select v-model="search.gender" placeholder="不限" clearable style="width: 120px">
            <el-option label="男" :value="1" />
            <el-option label="女" :value="2" />
          </el-select>
        </el-form-item>
        <el-form-item label="入职日期">
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
        <el-button type="primary" :icon="Plus" @click="openAdd">新增员工</el-button>
        <el-button type="danger" :icon="Delete" :disabled="!selected.length" @click="batchDelete">批量删除</el-button>
      </div>
      <el-table :data="list" border stripe v-loading="loading" @selection-change="onSelect">
        <el-table-column type="selection" width="50" />
        <el-table-column type="index" label="序号" width="70" />
        <el-table-column prop="name" label="姓名" />
        <el-table-column label="性别" width="70">
          <template #default="{ row }">{{ genderMap[row.gender] }}</template>
        </el-table-column>
        <el-table-column label="头像" width="80">
          <template #default="{ row }">
            <el-avatar v-if="row.image" :size="40" :src="resolveImg(row.image)" />
          </template>
        </el-table-column>
        <el-table-column label="职位" width="110">
          <template #default="{ row }">{{ jobMap[row.job] || '—' }}</template>
        </el-table-column>
        <el-table-column prop="deptName" label="部门" width="110" />
        <el-table-column prop="entryDate" label="入职日期" width="120" />
        <el-table-column label="操作" width="180">
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

    <el-dialog v-model="dialogVisible" :title="form.id ? '编辑员工' : '新增员工'" width="640px">
      <el-form :model="form" label-width="90px">
        <el-form-item label="用户名" required>
          <el-input v-model="form.username" :disabled="!!form.id" />
        </el-form-item>
        <el-form-item label="姓名" required>
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="性别" required>
          <el-radio-group v-model="form.gender">
            <el-radio :value="1">男</el-radio>
            <el-radio :value="2">女</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="头像">
          <el-upload
            class="avatar-uploader"
            :show-file-list="false"
            :auto-upload="false"
            :on-change="onImgChange"
          >
            <img v-if="form.image" :src="resolveImg(form.image)" class="avatar" />
            <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
          </el-upload>
        </el-form-item>
        <el-form-item label="职位">
          <el-select v-model="form.job" placeholder="请选择" style="width: 200px">
            <el-option v-for="(v, k) in jobMap" :key="k" :label="v" :value="Number(k)" />
          </el-select>
        </el-form-item>
        <el-form-item label="部门">
          <el-select v-model="form.deptId" placeholder="请选择" style="width: 200px">
            <el-option v-for="d in depts" :key="d.id" :label="d.name" :value="d.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="薪资">
          <el-input-number v-model="form.salary" :min="0" />
        </el-form-item>
        <el-form-item label="入职日期">
          <el-date-picker v-model="form.entryDate" type="date" value-format="YYYY-MM-DD" />
        </el-form-item>
        <el-divider>工作经历</el-divider>
        <div v-for="(exp, idx) in form.exprList" :key="idx" class="expr-row">
          <el-input v-model="exp.company" placeholder="公司" style="width: 150px" />
          <el-input v-model="exp.job" placeholder="职位" style="width: 120px" />
          <el-date-picker v-model="exp.begin" type="date" value-format="YYYY-MM-DD" placeholder="开始" style="width: 140px" />
          <el-date-picker v-model="exp.end" type="date" value-format="YYYY-MM-DD" placeholder="结束" style="width: 140px" />
          <el-button type="danger" :icon="Delete" circle @click="form.exprList.splice(idx, 1)" />
        </div>
        <el-button :icon="Plus" @click="form.exprList.push({ company: '', job: '', begin: '', end: '' })">添加经历</el-button>
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
import { Plus, Search, Refresh, Delete } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  empPageApi, empDeleteApi, empGetApi, empAddApi, empUpdateApi, deptListApi, uploadApi
} from '../api'
import { genderMap, jobMap, resolveImg } from '../utils'

const list = ref([])
const total = ref(0)
const loading = ref(false)
const selected = ref([])
const depts = ref([])
const dialogVisible = ref(false)
const search = reactive({ name: '', gender: '', range: [], page: 1, pageSize: 10 })

const emptyForm = () => ({
  id: null, username: '', name: '', gender: 1, image: '', job: '', deptId: '', salary: 0, entryDate: '', exprList: []
})
const form = reactive(emptyForm())

const load = async () => {
  loading.value = true
  try {
    const params = {
      page: search.page,
      pageSize: search.pageSize,
      name: search.name || undefined,
      gender: search.gender || undefined,
      begin: search.range ? search.range[0] : undefined,
      end: search.range ? search.range[1] : undefined
    }
    const data = await empPageApi(params)
    list.value = data.rows
    total.value = data.total
  } finally {
    loading.value = false
  }
}

const reset = () => {
  search.name = ''
  search.gender = ''
  search.range = []
  search.page = 1
  load()
}

const onSelect = (rows) => {
  selected.value = rows
}

const openAdd = () => {
  Object.assign(form, emptyForm())
  dialogVisible.value = true
}

const openEdit = async (row) => {
  const data = await empGetApi(row.id)
  Object.assign(form, data)
  if (!form.exprList) form.exprList = []
  dialogVisible.value = true
}

const remove = (row) => {
  ElMessageBox.confirm(`确认删除员工【${row.name}】？`, '提示', { type: 'warning' })
    .then(async () => {
      await empDeleteApi([row.id])
      ElMessage.success('删除成功')
      load()
    })
    .catch(() => {})
}

const batchDelete = () => {
  ElMessageBox.confirm(`确认删除选中的 ${selected.value.length} 名员工？`, '提示', { type: 'warning' })
    .then(async () => {
      await empDeleteApi(selected.value.map((s) => s.id))
      ElMessage.success('删除成功')
      load()
    })
    .catch(() => {})
}

const onImgChange = async (file) => {
  const url = await uploadApi(file.raw)
  form.image = url
  ElMessage.success('上传成功')
}

const submit = async () => {
  if (!form.username || !form.name) {
    ElMessage.warning('请填写用户名和姓名')
    return
  }
  const payload = { ...form }
  if (form.id) await empUpdateApi(payload)
  else await empAddApi(payload)
  ElMessage.success('保存成功')
  dialogVisible.value = false
  load()
}

onMounted(async () => {
  depts.value = await deptListApi()
  load()
})
</script>

<style scoped>
.search-card { margin-bottom: 14px; }
.toolbar { margin-bottom: 14px; }
.pager { margin-top: 16px; justify-content: flex-end; }
.expr-row { display: flex; gap: 8px; align-items: center; margin-bottom: 10px; }
.avatar-uploader :deep(.el-upload) {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  width: 80px;
  height: 80px;
  display: flex;
  align-items: center;
  justify-content: center;
}
.avatar { width: 80px; height: 80px; object-fit: cover; border-radius: 6px; }
.avatar-uploader-icon { font-size: 24px; color: #8c939d; }
</style>
