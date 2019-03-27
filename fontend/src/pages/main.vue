<template>
  <div class="layout">
    <Layout>
      <Header>
        <topMenu></topMenu>
      </Header>
      <Layout>
        <Sider class="sider" hide-trigger>
          <sideMenu></sideMenu>
        </Sider>
        <Layout :style="{padding: '0 24px 24px'}">
          <Breadcrumb :style="{margin: '24px 0', height: '20px'}">
            <BreadcrumbItem>{{$store.getters.currentTopMenu.title}}</BreadcrumbItem>
            <BreadcrumbItem>{{$store.getters.currentSideMenu.title}}</BreadcrumbItem>
          </Breadcrumb>
          <Content :style="{padding: '24px', minHeight: contentHeight, background: '#fff'}">
            <router-view></router-view>
          </Content>
        </Layout>
      </Layout>
    </Layout>
  </div>
</template>
<script>
  import * as types from '@/store/mutation-types'
  import topMenu from '@/components/top-menu'
  import sideMenu from '@/components/side-menu'

  export default {
    name: 'main',
    desc: '主框架',
    components: {
      topMenu,
      sideMenu
    },
    data() {
      return {
        contentHeight: '0px'
      }
    },
    created() {
      this.initMenuStatus()
    },
    mounted() {
      this.initContentHeight()
    },
    methods: {
      /**
       * 主框架创建时,根据路由设置菜单选中状态
       */
      initMenuStatus() {
        let route = this.$route.matched
        let topMenuPath = route[0].path
        let sideMenuPath = route[1].path
        if (route[1].meta && route[1].meta.refPath) {
          sideMenuPath = route[1].meta.refPath
        }
        this.$store.commit(types.SET_TOP_MENU_PATH, {path: topMenuPath})
        this.$store.commit(types.SET_SIDE_MENU_PATH, {path: sideMenuPath})
      },
      /**
       * 计算内容区域最小高度
       */
      initContentHeight() {
        // 设置左侧菜单高度
        this.contentHeight = '0px'
        this.$nextTick(function() {
          let windowHeight = Math.max(window.innerHeight, document.body.scrollHeight)
          this.contentHeight = `${windowHeight - 64 - 68 - 24 - 5}px`
          console.log('更新侧边栏高度', this.contentHeight)
        })
      }
    }
  }
</script>
<style scoped>
.layout{
  min-width: 1200px;
  border-top: 4px solid #d7dde4;
  border-bottom: 1px solid #d7dde4;
  background: #f5f7f9;
  position: relative;
}
.ivu-layout-header {
  background: #2d8cf0;
  padding: 0 20px;
}
.sider {
  background-color: #fff;
  padding-top: 20px;
}
.sider:after {
    content: '';
    display: block;
    width: 1px;
    height: 100%;
    background: #dddee1;
    position: absolute;
    top: 0;
    bottom: 0;
    right: 0;
    z-index: 1;
}
</style>
