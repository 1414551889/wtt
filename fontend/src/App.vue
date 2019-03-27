<template>
  <div id="app">
    <!-- <img src="./assets/logo.png"> -->
    <router-view/>
  </div>
</template>

<script>
import api from '@/service'
import utils from '@/utils'
import * as types from '@/store/mutation-types'
export default {
  name: 'app',
  created () {
    this.$Message.config({
      duration: 3
    })
//  this.initPage()
    this.fetchUserInfo()
  },
  methods: {
    /**
     * 启动时取回用户信息
     */
    async fetchUserInfo () {
      let isAuthenticated = this.$store.state.token
      let token = utils.localStorage.getToken()
      console.log('---------->'+token)
      if (token && !isAuthenticated) {
        console.log('页面启动,取回用户信息')
        try {
          let res = await api.user.getUserByToken(token)
          this.$store.commit(types.SET_USER, res)
        } catch (e) {
          this.$Message.error(e.message)
        }
      }
    },
    initPage () {
      // 禁止右键菜单
      document.oncontextmenu = function() { return false }
      // IE
      document.onselectstart = function() {
        if (event.srcElement.type === "text" && event.srcElement.type === "textarea" && event.srcElement.type === "password") {
          return true
        }
        return false
      }
      // FIREFOX
      if (window.sidebar) {
        document.onmousedown = function(e) {
          let obj = e.target
          if (obj.tagName.toUpperCase() === "INPUT" || obj.tagName.toUpperCase() === "TEXTAREA" || obj.tagName.toUpperCase() === "PASSWORD") {
            return true
          }
          return false
        }
      }
    }
  }
}
</script>
<style scoped>
  #app {
    height: 100%;
  }
</style>
