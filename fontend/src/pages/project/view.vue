<template lang="html">
  <div>
    <div class="ui-toolbar">
      <Button type="default" icon="ios-arrow-back" @click="$router.go(-1)">返回</Button>
    </div>
    <h3 class="mb10">{{title}}</h3>
    <div v-if="loading" class="loading">
      <Spin fix>
        <Icon type="load-c" size=18 class="spin-icon-load"></Icon>
        <div>Loading</div>
      </Spin>
    </div>
    <div v-else-if="projectList.length === 0">
      暂无数据
    </div>
    <Collapse v-else v-model="panelId" accordion>
      <Panel v-for="(item, index) in projectList" :key="item.contentId" :name="String(item.contentId)">
        <p class="ellipsis" style="width: 100%; float: right;margin-left:-5px; padding-left:8px;">
          <Icon v-if="item.isImportant === 1" type="ios-star" class="icon" style="color: gold;"></Icon>
          <Icon v-if="item.isRisk === 1" type="alert-circled" class="icon" style="color: red;"></Icon>
          {{item.centerName}}: {{item.projectName}} - {{item.workProject}}
        </p>
        <!-- {{item.centerName}}: {{item.projectName}} - {{item.workProject}} -->
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
import api from '@/service'

export default {
  data () {
    return {
      title: '',
      panelId: '',
      projectList: [],
      loading: true
    }
  },
  created() {
    this.getProjectContent()
  },
  watch: {
    'projectList': 'openDefaultPanel'
  },
  methods: {
    async getProjectContent() {
      let billId = this.$route.query.id
      try {
        this.loading = true
        let res = await api.project.getProjectContent({billId})
        this.title = res.title
        this.projectList = res.data.list
      } catch (e) {
        console.log(e)
        this.$Message.error("获取数据失败！")
      } finally {
        this.loading = false
      }
    },
    openDefaultPanel() {
      if (this.projectList.length === 0) {
        return
      }
      this.$nextTick(function() {
        this.panelId = String(this.projectList[0].contentId)
      })
    }
  }
}
</script>

<style lang="css" scoped>
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
.modify {
  display: inline-block;
  margin: 7px 10px 0 0;
}
.spin-icon-load{
  animation: ani-spin 1s linear infinite;
}
@keyframes ani-spin {
  from { transform: rotate(0deg);}
  50%  { transform: rotate(180deg);}
  to   { transform: rotate(360deg);}
}
.loading{
  height: 200px;
  position: relative;
}
</style>
