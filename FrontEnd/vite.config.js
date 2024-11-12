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
        secure: false,    
      },
      '/set': {
        target: 'http://localhost:8080', // Địa chỉ backend
        changeOrigin: true,
        secure: false
      },
      '/word': {
        target: 'http://localhost:8080', // Địa chỉ backend
        changeOrigin: true,
        secure: false
      },
      '/class': {
        target: 'http://localhost:8080', 
        changeOrigin: true,
        secure: false
      },
      // ,
      // '/eng':{
      //   target: 'https://od-api-sandbox.oxforddictionaries.com/api/v2',
      //   changeOrigin: true,
      //   secure: false,
      //   rewrite: (path) => path.replace(/^\/eng/, '')
      // }
      '/notification': {
        target: 'http://localhost:8080', 
        changeOrigin: true,
        secure: false
      },
      '/study': {
        target: 'http://localhost:8080', 
        changeOrigin: true,
        secure: false
      },
    }
  }
})
