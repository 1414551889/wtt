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
    <div v-else-if="halfWeekList.length===0">
      暂无数据
    </div>
    <Collapse v-else v-model="panelId" accordion>
      <Panel v-for="(item, index) in halfWeekList" :key="item.id" :name="String(item.id)">
        <p class="ellipsis" style="width: 100%; float: right;margin-left:-5px; padding-left:8px;">
          {{item.centerName}}: {{item.projectName}}
        </p>
        <!-- {{item.centerName}}: {{item.projectName}} - {{item.workProject}} -->
        <p slot="content" class="content">
          <b class="label">半周报名称：</b>{{item.projectName}}
          <b class="label">半周报内容：</b>
          {{item.workContent}}
          <b class="label">子项目：</b>
          {{item.subProject}}
        </p>
      </Panel>
    </Collapse>
  </div>
</template>
<script>
import api from '@/service'

export default {
  name: "summary_view",
  data () {
    return {
      panelId:'1',
      title: '',
      halfWeekList: [],
      loading: true
    }
  },
  created() {
    this.getContentByHalfBill()
  },
  watch: {
    'halfWeekList': 'openDefaultPanel'
  },
  methods: {
    async getContentByHalfBill() {
      let billId = this.$route.query.id
      try {
        this.loading = true
        let res = await api.summary.getContentByHalfBill({billId})
        this.title = res.title
        this.halfWeekList = res.data.list
      } catch (e) {
        console.log(e)
        this.$Message.error("获取数据失败！")
      } finally {
        this.loading = false
      }
    },
    openDefaultPanel() {
      if (this.halfWeekList.length === 0) {
        return
      }
      this.$nextTick(function() {
        this.panelId = String(this.halfWeekList[0].id)
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
