<template>
  <div>
    <div ref="menuWrapper" class="wrapper">
      <ul ref="menuUl" class="ui-menu" :style="{width: mWidth}">
        <li :class="['m-item', {'m-item-active': currentMenu === -1}]" @click="selectMenu($event, {id: -1, name: '重点项目'})">
          重点项目
        </li>
        <li v-for="item in $store.state.centers" :key="item.id" :class="['m-item', {'m-item-active': currentMenu === item.id}]" @click="selectMenu($event, item)">
          {{item.name}}
        </li>
      </ul>
    </div>
    <br>
    <div v-if="loading" class="loading">
      加载中…
    </div>
    <div v-else-if="projectList.length === 0" class="loading">
      暂无项目推进记录
    </div>
    <Collapse v-else v-model="panelId" style="margin: 0 5px;" accordion>
      <h3 class="mt10 mb10" style="text-align: center">{{title}}</h3>
      <Panel v-for="(item, index) in projectList" :key="item.contentId" :name="String(item.contentId)">
        <!-- {{item.projectName}} - {{item.workProject}} -->
        <p class="ellipsis" style="width: 94%; float: right;">{{item.projectName}} - {{item.workProject}}</p>
        <Icon v-if="item.isImportant === 1" type="ios-star" class="icon" style="color: gold; position: absolute; left:10px; top: 13px;"></Icon>
        <Icon v-if="item.isRisk === 1" type="alert-circled" class="icon" style="color: red; position: absolute; left:10px; top: 13px;"></Icon>
        <p slot="content" class="content">
          <b class="label">工作项目：</b>{{item.workProject}}
          <b class="label">工作内容：</b>
          {{item.workContent}}
          <b class="label">完成情况：</b>
          {{item.overState}}
          <b class="label">责任人：</b>{{item.responsible}}
          <b class="label">配合人：</b>{{item.cooperator}}
          <b class="label">完成时限：</b>{{item.timeLimit}}
          <b class="label">开票回款风险：</b><span :class="{redText: item.isRisk === 1}">{{item.riskSituation}}</span><br>
          <b class="label">备注：</b>{{item.remark}}
        </p>
      </Panel>
    </Collapse>
  </div>
</template>
<script>
import BScroll from 'better-scroll'
import api from '@/service'

export default {
  name: 'mobileProjectView',
  title: '本周项目推进',
  data() {
    return {
      loading: false,
      title: '',
      mWidth: '100%',
      currentMenu: -1,
      panelId: '',
      projectList: []
    }
  },
  created() {
    this.getProjectList()
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
      this.getProjectList()
    },
    async getProjectList() {
      let params = {}
      let id = this.currentMenu
      if (id === -1) {
        params.isImportant = 1
      } else {
        params.centerId = id
      }
      this.loading = true
      this.projectList = []
      try {
        let res = await api.project.getPublishedProject(params)
        this.projectList = res.data.list
        this.title = res.title
        if (this.projectList.length > 0) {
          this.$nextTick(function() {
            this.panelId = String(this.projectList[0].contentId)
          })
        }
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
.loading {
  padding-top: 30px;
  text-align: center;
}
.content {
  line-height: 24px;
  white-space: pre-line;
  word-break: break-word;
}
.label {
  font-weight: bold;
}
.icon {
  transform: none !important;
}
.redText {
  color: red;
}
</style>
