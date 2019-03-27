<template>
  <div>
    <div ref="menuWrapper" class="wrapper">
      <ul ref="menuUl" class="ui-menu" :style="{width: mWidth}">
        <li v-for="item in $store.state.centers" :key="item.id" :class="['m-item', {'m-item-active': currentMenu === item.id}]" @click="selectMenu($event, item)">
          {{item.name}}
        </li>
      </ul>
    </div>
    <br>
    <h3 class="mt10 mb10" style="text-align: center;">
      {{new Date().toLocaleDateString('zh-CN', {year: 'numeric', month: 'short', day: '2-digit'})}}--日报
    </h3>
    <div v-if="loading" class="loading" style="text-align: center">
      加载中…
    </div>
    <div v-else-if="dailyList.length === 0" class="loading" style="text-align: center">
      暂无日报记录
    </div>
    <Collapse v-else v-model="panelId" style="margin: 0 5px;" accordion>
      <h3 class="mt10 mb10" style="text-align: center;">{{title}}</h3>
      <Panel v-for="(item, index) in dailyList" :key="item.contentId" :name="String(item.contentId)">
        <p class="ellipsis" style="width: 94%; float: right;">{{item.title}} - {{item.content}}</p>
        <Icon v-if="item.isImportant === 1" type="ios-star" class="icon" style="color: gold; position: absolute; left:10px; top: 13px;"></Icon>
        <Icon v-if="item.isRisk === 1" type="alert-circled" class="icon" style="color: red; position: absolute; left:10px; top: 13px;"></Icon>
        <p slot="content" class="content">
          <b class="label">项目名称：</b>{{item.title}}
          <b class="label">工作内容：</b>
          {{item.content}}
        </p>
      </Panel>
    </Collapse>
  </div>
</template>
<script>
import BScroll from 'better-scroll'
import api from '@/service'

export default {
  name: 'mobileDaily',
  title: '日报',
  data() {
    return {
      loading: false,
      title: '',
      mWidth: '100%',
      currentMenu: 1,
      panelId: '',
      dailyList: []
    }
  },
  created() {
    this.getDayReportList()
  },
  mounted() {
    this.$nextTick(function() {
      this.initMenu()
    })
  },
  methods: {
    initMenu() {
      let width = 0
      let menuItems = this.$refs.menuUl.children
      for (let i = 0; i < menuItems.length; i++) {
        width += menuItems[i].getBoundingClientRect().width
      }
      width = Math.max(window.innerWidth, width)
      this.mWidth = `${Math.ceil(width)}px`
      this.scroll = new BScroll(this.$refs.menuWrapper, {
        scrollX: true,
        click: true,
        refreshDelay: 20,
        freeScroll: false
      })
    },
    selectMenu(event, menu) {
      this.currentMenu = menu.id
      let currentMenuWidth = event.target.getBoundingClientRect().width
      let scrollWidth = (window.innerWidth - currentMenuWidth) / 2
      this.$nextTick(function() {
        this.scroll.scrollToElement('.m-item-active', 300, -scrollWidth)
      })
      // 获取数据
      this.getDayReportList()
    },
    async getDayReportList() {
      let params = {}
      let id = this.currentMenu
      if (id === -1) {
        params.isImportant = 1
      } else {
        params.centerId = id
      }
      this.loading = true
      this.dailyList = []
      try {
        let res = await api.summary.getDayReportList(params)
        this.dailyList = res.list
        this.title = res.title
        if (this.dailyList.length > 0) {
          this.$nextTick(function() {
            this.panelId = String(this.dailyList[0].id)
          })
        }
        console.log('测试'+ params)
      } catch (e) {
        console.log(e)
        this.$Message.error("获取数据失败！")
      } finally {
        this.loading = false
      }
    }
  }
}
</script>
<style scoped>
.wrapper {
  width: 100%;
  height: 50px;
  overflow: hidden;
}
.ui-menu {
  height: 50px;
  line-height: 50px;
  background-color: #f8f8f8;
  position: relative;
  z-index: 999
}
.ui-menu::after {
  content: "";
  display: block;
  width: 100%;
  height: 1px;
  background: #dddee1;
  position: absolute;
  bottom: 0;
  left: 0;
}
.m-item {
  position: relative;
  float: left;
  height: inherit;
  line-height: inherit;
  padding: 0 15px;
  border-bottom: 2px solid transparent;
  color: #495060;
  font-size: 14px;
  z-index: 3;
}
.m-item-active {
  color: #2d8cf0;
  border-bottom: 2px solid #2d8cf0;
}
.content {
  padding: 0 12px;
  line-height: 24px;
  white-space: pre-line;
  word-break: break-word;
  text-align:justify;
  text-justify:inter-word;
}
</style>
