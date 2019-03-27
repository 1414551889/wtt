<template>
  <Menu ref="menu" :active-name="currentSideMenuPath" theme="light" width="auto" @on-select="changeMenu">
    <MenuItem v-for="item in menus" :key="item.path" :name="item.path">
      <Icon :type="item.icon"></Icon>
      <span>{{item.title}}</span>
    </MenuItem>
  </Menu>
</template>
<script>
import { mapGetters } from 'vuex'
import * as types from '@/store/mutation-types'

export default {
  name: 'side-menu',
  desc: '左侧菜单组件',
  computed: {
    ...mapGetters(['currentTopMenu', 'currentSideMenuPath']),
    menus() {
      return this.currentTopMenu.children
    }
  },
  watch: {
    'currentSideMenuPath': 'updateMenuStatus'
  },
  methods: {
    // 更新侧边菜单选中状态
    updateMenuStatus() {
      this.$nextTick(function() {
        this.$refs.menu.updateActiveName()
      })
    },
    // 点击菜单时,更新store状态
    changeMenu(name) {
      this.$store.commit(types.SET_SIDE_MENU_PATH, {path: name})
      this.$router.push({path: name})
    }
  }
}
</script>
<style scoped>
.ivu-menu-item-active {
  background-color: #eee;
}
</style>
