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
    <div v-else-if="traceList.length === 0">
      暂无数据
    </div>
    <Collapse v-else v-model="panelId" accordion>
      <Panel v-for="(item, index) in traceList" :key="item.contentId" :name="String(item.contentId)">
        <p class="ellipsis" style="width: 100%; float: right;margin-left:-5px; padding-left:8px;">
          {{item.centerName}}: {{item.projectWork}}
        </p>
        <!-- {{item.centerName}}: {{item.projectName}} - {{item.workProject}} -->
        <p slot="content" class="content">
          <b class="label">项目工作：</b>{{item.projectWork}}
          <b class="label">上线内容：</b>{{item.content}}
          <b class="label">项目状态：</b>{{item.status}}
          <b class="label">负责人：</b>{{item.projectManager}}
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
      traceList: [],
      loading: true
    }
  },
  created() {
    this.getOnlineTraceContentByBill()
  },
  watch: {
    'traceList': 'openDefaultPanel'
  },
  methods: {
    async getOnlineTraceContentByBill() {
      let billId = this.$route.query.id
      console.log(billId)
      try {
        this.loading = true
        let res = await api.trace.getOnlineTraceContentByBill({billId})
        console.log(res)
        this.title = res.title
        this.traceList = res.data.list
      } catch (e) {
        console.log(e)
        this.$Message.error("获取数据失败！")
      } finally {
        this.loading = false
      }
    },
    openDefaultPanel() {
      if (this.traceList.length === 0) {
        return
      }
      this.$nextTick(function() {
        this.panelId = String(this.traceList[0].contentId)
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
