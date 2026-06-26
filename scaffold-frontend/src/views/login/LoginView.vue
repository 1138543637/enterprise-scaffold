<template>
  <div class="login-page">
    <div class="login-card">
      <h2 class="title">Enterprise Scaffold</h2>
      <p class="subtitle">企业级后台脚手架</p>

      <el-form
          ref="formRef"
          :model="form"
          :rules="rules"
          label-width="0"
          class="login-form"
      >
        <el-form-item prop="username">
          <el-input
              v-model="form.username"
              size="large"
              placeholder="请输入用户名"
          />
        </el-form-item>

        <el-form-item prop="password">
          <el-input
              v-model="form.password"
              size="large"
              type="password"
              placeholder="请输入密码"
              show-password
              @keyup.enter="handleLogin"
          />
        </el-form-item>

        <el-button
            type="primary"
            size="large"
            class="login-button"
            :loading="loading"
            @click="handleLogin"
        >
          登录
        </el-button>
      </el-form>

      <div class="tips">
        默认账号：admin / admin123
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue'
import type { FormInstance, FormRules } from 'element-plus'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'
import { useAuthStore } from '../../stores/auth'
import type { LoginRequest } from '../../api/system/auth'

const router = useRouter()
const authStore = useAuthStore()

const formRef = ref<FormInstance>()

const loading = ref(false)

const form = reactive<LoginRequest>({
  username: 'admin',
  password: 'admin123'
})

const rules: FormRules<LoginRequest> = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' }
  ]
}

async function handleLogin() {
  await formRef.value?.validate()

  loading.value = true

  try {
    await authStore.login(form)
    ElMessage.success('登录成功')
    await router.push('/dashboard')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-page {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #1f2d3d, #3a8ee6);
}

.login-card {
  width: 420px;
  padding: 40px;
  border-radius: 12px;
  background: #ffffff;
  box-shadow: 0 12px 30px rgba(0, 0, 0, 0.18);
}

.title {
  margin: 0;
  text-align: center;
  font-size: 28px;
  color: #303133;
}

.subtitle {
  margin: 12px 0 32px;
  text-align: center;
  color: #909399;
}

.login-form {
  width: 100%;
}

.login-button {
  width: 100%;
}

.tips {
  margin-top: 18px;
  text-align: center;
  color: #909399;
  font-size: 14px;
}
</style>