'use strict'

import Vue from 'vue'
import VueQuillEditor from 'vue-quill-editor'
import router from '@/router'
import store from '@/store'
import App from '@/App'
import iView from 'iview'
import 'iview/dist/styles/iview.css'
import './styles/global.css'
import 'quill/dist/quill.core.css'
import 'quill/dist/quill.snow.css'
import 'quill/dist/quill.bubble.css'


 

// 注册全局公用组件
import appPage from '@/components/app-page'
Vue.component('appPage', appPage)

Vue.use(iView)
Vue.use(VueQuillEditor)
Vue.config.productionTip = false

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  store,
  render: h => h(App)
})
