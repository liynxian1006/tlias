<template>
  <el-card>
    <el-table :data="list" border stripe v-loading="loading">
      <el-table-column prop="id" label="ID" width="70" />
      <el-table-column prop="operateTime" label="操作时间" width="190" />
      <el-table-column prop="className" label="类名" show-overflow-tooltip />
      <el-table-column prop="methodName" label="方法名" width="150" />
      <el-table-column prop="methodParams" label="请求参数" show-overflow-tooltip />
      <el-table-column prop="costTime" label="耗时(ms)" width="100" />
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
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { logPageApi } from '../api'

const list = ref([])
const total = ref(0)
const loading = ref(false)
const search = reactive({ page: 1, pageSize: 10 })

const load = async () => {
  loading.value = true
  try {
    const data = await logPageApi({ page: search.page, pageSize: search.pageSize })
    list.value = data.rows
    total.value = data.total
  } finally {
    loading.value = false
  }
}

onMounted(load)
</script>

<style scoped>
.pager {
  margin-top: 16px;
  justify-content: flex-end;
}
</style>
