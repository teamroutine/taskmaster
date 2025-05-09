import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';
import { viteStaticCopy } from 'vite-plugin-static-copy';
export default defineConfig({
  plugins: [react(),
  viteStaticCopy({
    targets: [
      {
        src: 'static.json',
        dest: '.'
      }
    ]
  })

  ],
  test: {
    globals: true,
    environment: "jsdom",
    include: [
      'src/tests/**/*.{test,spec}.{js,ts,jsx,tsx}',
      'src/tests/*.{test,spec}.{js,ts,jsx,tsx}'
    ],
    coverage: {
      provider: 'v8',
      reporter: ['text', 'html'],
      include: ['src/**/*.{js,ts,jsx,tsx}'],
      dir: 'coverage',
    },
  },
});