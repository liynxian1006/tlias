<template>
  <div>
    <el-row :gutter="16">
      <el-col :span="12">
        <el-card><div ref="genderRef" class="chart"></div></el-card>
      </el-col>
      <el-col :span="12">
        <el-card><div ref="jobRef" class="chart"></div></el-card>
      </el-col>
    </el-row>
    <el-row :gutter="16" style="margin-top: 16px">
      <el-col :span="12">
        <el-card><div ref="degreeRef" class="chart"></div></el-card>
      </el-col>
      <el-col :span="12">
        <el-card><div ref="classRef" class="chart"></div></el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount } from 'vue'
import * as echarts from 'echarts'
import {
  reportEmpGenderApi, reportEmpJobApi, reportStudentDegreeApi, reportStudentCountApi
} from '../api'

const genderRef = ref()
const jobRef = ref()
const degreeRef = ref()
const classRef = ref()
const charts = []

const initChart = (el, option) => {
  const c = echarts.init(el)
  c.setOption(option)
  charts.push(c)
  return c
}

const resize = () => charts.forEach((c) => c.resize())

onMounted(async () => {
  const gender = await reportEmpGenderApi()
  initChart(genderRef.value, {
    title: { text: '员工性别统计' },
    tooltip: { trigger: 'item' },
    legend: { bottom: 0 },
    series: [{ type: 'pie', radius: '55%', data: gender, label: { formatter: '{b}: {c}' } }]
  })

  const job = await reportEmpJobApi()
  initChart(jobRef.value, {
    title: { text: '员工职位人数统计' },
    tooltip: {},
    xAxis: { type: 'category', data: job.jobList },
    yAxis: { type: 'value' },
    series: [{ type: 'bar', data: job.dataList, itemStyle: { color: '#409EFF' } }]
  })

  const degree = await reportStudentDegreeApi()
  initChart(degreeRef.value, {
    title: { text: '学员学历统计' },
    tooltip: { trigger: 'item' },
    legend: { bottom: 0 },
    series: [{ type: 'pie', radius: '55%', data: degree, label: { formatter: '{b}: {c}' } }]
  })

  const cls = await reportStudentCountApi()
  initChart(classRef.value, {
    title: { text: '班级人数统计' },
    tooltip: {},
    xAxis: { type: 'category', data: cls.clazzList, axisLabel: { interval: 0, rotate: 30 } },
    yAxis: { type: 'value' },
    series: [{ type: 'bar', data: cls.dataList, itemStyle: { color: '#67C23A' } }]
  })

  window.addEventListener('resize', resize)
})

onBeforeUnmount(() => {
  window.removeEventListener('resize', resize)
  charts.forEach((c) => c.dispose())
})
</script>

<style scoped>
.chart {
  height: 320px;
}
</style>
