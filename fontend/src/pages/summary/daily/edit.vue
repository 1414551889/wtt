<template lang="html">
  <div>
    <div class="ui-toolbar">
        <Button type="default" icon="ios-arrow-back" @click="$router.go(-1)">返回</Button>
        <Button v-if="$store.state.user && $store.state.user.role === 2" type="success" icon="android-cloud-done" @click="publishBill">发布</Button>

    </div>
    <!--<h3 class="mb10">{{title}}</h3>-->
    <h3 class="mb10">{{title}}</h3>
    <div v-if="loading" class="loading">
      <Spin fix>
        <Icon type="load-c" size=18 class="spin-icon-load"></Icon>
        <div>Loading</div>
      </Spin>
    </div>
    <div v-else-if="!dailyList">
      暂无数据
    </div>
    <Collapse v-else v-model="panelId" accordion>
      <Panel>
        <p class="ellipsis" style="width: 100%; float: right;margin-left:-5px; padding-left:8px; padding-right: 130px">
          <!--<Icon v-if="item.isImportant === 1" type="ios-star" class="icon" style="color: gold;"></Icon>
          <Icon v-if="item.isRisk === 1" type="alert-circled" class="icon" style="color: red;"></Icon>-->
          <!--{{item.centerName}}: {{item.content}} - {{item.subTime}}-->
          {{dailyList.title}}
        </p>
        <ButtonGroup v-if="$store.state.user && $store.state.user.role === 1" class="modify" size="small">
          <Button type="warning" icon="edit" @click.stop="modify(dailyList.id)">修改</Button>
          <Button type="error" icon="ios-trash" @click.stop="del(dailyList.id)">删除</Button>
        </ButtonGroup>
        <p slot="content" class="content">
          <b class="label">中心名称：</b>{{dailyList.centerName}}
          <b class="label">工作内容：</b>{{dailyList.content}}
          <b class="label">提交日期：</b>{{dailyList.subTime}}

        </p>
      </Panel>
    </Collapse>
  </div>
</template>
<script>
import api from '@/service'
import axios from 'axios'
import utils from '@/utils'
export default {
  data () {
    return {
      title: '',
      panelId: '',
      dailyList: [],
      loading: false
    }
  },
  created() {
    this.getDayReportById()
  },

  methods: {
    async getDayReportById() {
      let id = this.$route.query.id
      try {
        this.loading = true
        console.log(id);
        let res = await api.summary.getDayReportById({id})
        console.log(res)
        // this.title = res.title
        this.dailyList = res;
      } catch (e) {
        this.$Message.error("获取数据失败！")
      } finally {
        this.loading = false
      }
    },
    add() {
      this.$router.push({name: 'summaryDailyAdd', query: this.$route.query})
    },
    modify(id) {
      this.$router.push({name: 'summaryDailyModify', query: {id}})
    },
    async publishDayReport() {
      let billId = this.$route.query.dailyList
      try {
        let res = await api.summary.publishDayReport({billId})
        console.log(res)
        this.$Message.success("发布成功！")
        this.$router.go(-1)
      } catch (e) {
        console.log(e)
        this.$Message.error("操作失败!")
      }
    },
    async del(id) {
      let config = {
        title: '删除',
        content: `确定要删除「 'dd' 」吗?`,
        width: 350,
        onOk: async () => {
          var a = new Array();
          a.push(id)
          let json=JSON.stringify(a)
          let token = utils.localStorage.getToken();
          console.log(json)
          axios.post(
            "/report/api/deleteOnlineContent",
            json,
            {headers: {'Content-Type': 'application/json;charset=UTF-8', "jwtToken": token}}
          ).then((response) => {
            this.$Message.success("删除成功！")
            console.log(id)
            this.dailyList = this.dailyList.filter(item => item.id !== params.row.id)
            //   this.deleteObjectBy(params.row.id)
          })
        }
      }
      this.$Modal.confirm(config)
      try {
        await api.summary.deleteReportInfo({id: id})
        this.dailyList = this.dailyList.filter(item => item.id !== id)
      } catch (e) {
        console.log(e)
        this.$Message.error("操作失败！")
      }
    },
    openDefaultPanel() {
      if (this.dailyList.length === 0) {
        return
      }
      this.$nextTick(function() {
        this.panelId = String(this.dailyList[0].contentId)
      })
    },
    downloadModel() {
      console.log("下载文件")
      window.location.href = 'url'
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
  position: absolute;
  top: 0;
  right: 0;
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
.pull-right{
  position:relative;
  }
.pull-right>Button{
  position:absolute;
  right:80px;
}
</style>
