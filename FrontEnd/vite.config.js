import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

// https://vite.dev/config/
export default defineConfig({
  plugins: [
    vue(),
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    }
  }
  ,
  server: {
    proxy: {
      '/user': {
        target: 'http://localhost:8080', // Địa chỉ backend
        changeOrigin: true,
        secure: false, // Thêm secure: false để bỏ qua SSL khi thử nghiệm
      },
      '/set': {
        target: 'http://localhost:8080', // Địa chỉ của backend
        changeOrigin: true,
        secure: false,
      }
    },
  }
})
