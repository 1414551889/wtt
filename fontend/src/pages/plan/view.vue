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
    <div v-else-if="planList.length === 0">
      暂无数据
    </div>
    <Collapse v-else v-model="panelId" accordion>
      <Panel v-for="(item, index) in planList" :key="item.contentId" :name="String(item.contentId)">
        <p class="ellipsis" style="width: 100%; float: right;margin-left:-5px; padding-left:8px;">
          {{item.centerName}}: {{item.project}} - {{item.type}}
        </p>
        <!-- {{item.centerName}}: {{item.projectName}} - {{item.workProject}} -->
        <p slot="content" class="content">
          <b class="label">分类：</b>{{item.type}}
          <b class="label">具体内容：</b>{{item.content}}
          <b class="label">提测时间：</b>{{item.testTime}}
          <b class="label">产品人员：</b>{{item.productStaff}}
          <b class="label">开发人员：</b>{{item.developStaff}}
          <b class="label">测试人员：</b>{{item.testStaff}}
          <b class="label">备注&风险：</b>{{item.remark}}
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
      planList: [],
      loading: true
    }
  },
  created() {
    this.getOnlineContentByBill()
  },
  watch: {
    'planList': 'openDefaultPanel'
  },
  methods: {
    async getOnlineContentByBill() {
      let billId = this.$route.query.id
      console.log(billId)
      try {
        this.loading = true
        let res = await api.plan.getOnlineContentByBill({billId})
        console.log(res)
        this.title = res.title
        this.planList = res.data.list
      } catch (e) {
        console.log(e)
        this.$Message.error("获取数据失败！")
      } finally {
        this.loading = false
      }
    },
    openDefaultPanel() {
      if (this.planList.length === 0) {
        return
      }
      this.$nextTick(function() {
        this.panelId = String(this.planList[0].contentId)
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
