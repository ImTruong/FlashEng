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
  },
  server: {
    proxy: {
      '/user': {
        target: 'http://localhost:8080', // Địa chỉ của backend
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/user/, '/user'), // Nếu cần xóa tiền tố `/user`, bạn có thể điều chỉnh ở đây
      },
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api/, ''),
      },
    },
  }
})
