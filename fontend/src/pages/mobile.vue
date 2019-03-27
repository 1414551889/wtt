<template lang="html">
  <div :class="{viewport: isLeftMenuOpen}">
    <div :class="['mobile', {'open': isLeftMenuOpen}]">
      <div class="leftMenu">
        <Menu ref="menu" theme="dark" :active-name="currentMenu" width="auto" @on-select="changeMenu">
          <!-- <div class="ui-title">
            当前用户：{{$store.state.user.username}}
          </div> -->
          <MenuGroup title="项目推进">
            <MenuItem name="benzhoutuijin">
                <Icon type="calendar"></Icon>
                最新项目推进
            </MenuItem>
            <MenuItem name="tuijinjilu">
                <Icon type="clock"></Icon>
                项目推进记录
            </MenuItem>
          </MenuGroup>
          <MenuGroup title="工作总结">
            <MenuItem name="mobiledaily">
                <Icon type="calendar"></Icon>
                日报
            </MenuItem>
            <MenuItem name="halfweekly">
                <Icon type="clock"></Icon>
                半周报
            </MenuItem>
            <!--<MenuItem name="weekly">
                <Icon type="clock"></Icon>
                周报
            </MenuItem>-->
          </MenuGroup>
          <MenuGroup title="项目上线">
            <MenuItem name="sxjh">
                <Icon type="ios-keypad"></Icon>
                上线计划
            </MenuItem>
            <MenuItem name="sxbg">
                <Icon type="clipboard"></Icon>
                上线报告
            </MenuItem>
          </MenuGroup>
          <MenuGroup title="系统设置">
            <MenuItem name="changePassWord">
                <Icon type="ios-keypad"></Icon>
                修改密码
            </MenuItem>
            <MenuItem name="logout">
                <Icon type="power"></Icon>
                注销登录
            </MenuItem>
          </MenuGroup>
          <!-- <div class="tools">
            <div class="ui-btn" @click="changePassWord">
              <Icon type="power"></Icon>修改密码
            </div>
            <div class="ui-btn" @click="logout">
              <Icon type="power"></Icon>注销登录
            </div>
          </div> -->
        </Menu>
      </div>
      <div class="main">
        <div class="mask" v-show="isLeftMenuOpen" @click="toggle"></div>
        <header class="header">
          <Button v-if="isTopPath" type="text" icon="navicon-round" class="menu" size="large" @click='toggle'></Button>
          <Button v-else type="text" icon="ios-arrow-back" class="menu" size="large" @click='$router.go(-1)'></Button>
          全通微头条
        </header>
        <div class="content">
          <router-view></router-view>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      isTopPath: true,
      currentMenu: 'benzhoutuijin',
      isLeftMenuOpen: false
    }
  },
  created() {
    this.initMenu()
  },
  watch: {
    '$route': 'initMenu'
  },
  methods: {
    /**
     * 高亮对应的菜单及更改顶部菜单
     */
    initMenu() {
      let route = this.$route
      this.isTopPath = true
      this.currentMenu = route.name
      if (route.meta.refName) {
        this.isTopPath = false
        this.currentMenu = route.meta.refName
      }
      this.$nextTick(function() {
        this.$refs.menu.updateActiveName()
      })
    },
    toggle() {
      this.isLeftMenuOpen = !this.isLeftMenuOpen
    },
    changeMenu(name) {
      if (name === 'logout') {
        if (window.wttapp && window.wttapp.logoutUnbindUserId) {
          let userId = this.$store.state.user.userId
          console.log('客户端解绑userId:', userId)
          window.wttapp.logoutUnbindUserId(userId)
        }
        window.localStorage.clear()
        window.location = process.env.ROUTER_BASE
        return
      }
      this.$router.push({name})
      setTimeout(this.toggle, 100)
    }
  }
}
</script>

<style lang="css" scoped>
.viewport {
  overflow: hidden;
}
.mobile {
  width: 160vw;
  transform: translateX(-60vw);
  transition: all 0.5s ease-out;
}
.open {
  transform: translateX(0);
  transition: all 0.5s ease-out;
}
.leftMenu {
  width: 60vw;
  height: 100vh;
  overflow-y: auto;
  position: absolute;
  left: 0;
  background: #495060;
}
.tools {
  margin-top: 10px;
  padding: 20px 24px 0;
  border-top: 1px solid #666;
}
.ui-btn {
  color: #fff;
}
.ui-btn .ivu-icon {
  margin-right: 8px;
}
.main {
  width: 100vw;
  height: 100vh;
  margin-left: 60vw;
}
.mask {
  width: 100%;
  height: 100%;
  position: absolute;
  z-index: 10000;
  background-color: transparent;
}
.header {
  width: 100%;
  height: 50px;
  background: #2d8cf0;
  color: #fff;
  text-align: center;
  font-size: 16px;
  line-height: 50px;
  position: relative;
}
.menu {
  position: absolute;
  top: 0;
  left: 0;
  padding: 0 20px;
  cursor: pointer;
  height: 100%;
  color: #fff;
}
</style>
