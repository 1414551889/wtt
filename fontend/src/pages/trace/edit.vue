<template lang="html">
  <div>
    <div class="ui-toolbar">
        <Button type="default" icon="ios-arrow-back" @click="$router.go(-1)">返回</Button>
        <Button v-if="$store.state.user && $store.state.user.role === 1" type="primary" icon="android-add" @click="add">新增</Button>
        <Button v-if="$store.state.user && $store.state.user.role === 2" type="success" icon="android-cloud-done" @click="publishBill">发布</Button>
        <div class="pull-right">
           <Button type="info" icon="android-download" @click="downloadModel">下载</Button>
           <Upload  action="/report/api/imporOnlineTraceBatchRecharge" :headers="{jwtToken:token}" :data="{billId:billId}" name="file" accept="application/vnd.ms-excel" @on-succcess="uploadSuccess">
              <Button type="info" class="upload" icon="ios-cloud-upload-outline" @on-success="uploadSuccess">导入</Button>
           </Upload>
        </div>
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
      <Panel v-for="(item, index) in traceList" :key="item.id" :name="String(item.id)">
        <p class="ellipsis" style="width: 100%; float: right;margin-left:-5px; padding-left:8px; padding-right: 130px">
          {{item.centerName}}: {{item.projectWork}}
        </p>
        <ButtonGroup v-if="$store.state.user && $store.state.user.role === 1" class="modify" size="small">
          <Button type="warning" icon="edit" @click.stop="modify(item.id)">修改</Button>
          <Button type="error" icon="ios-trash" @click.stop="del(item.id)">删除</Button>

        </ButtonGroup>
        <p slot="content" class="content">
          <b class="label">项目工作：</b>
          {{item.projectWork}}
          <b class="label">上线内容：</b>
          {{item.content}}
          <b class="label">状态：</b>
          {{item.status}}
          <b class="label">负责人：</b>{{item.projectManager}}
          <b class="label">备注：</b>{{item.remark}}
        </p>
      </Panel>
    </Collapse>

  </div>
</template>
<script>
import api from '@/service'
import utils from '@/utils'
import axios from 'axios'

export default {
  data () {
    return {
      title: '',
      panelId: '1',
      traceList: [],
      loading: false,
      token: null,
      billId: null
    }
  },
  created() {
    this.getOnlineTraceContentByBill(),
    this.token = utils.localStorage.getToken(),
    this.billId = this.$route.query.id
  },
  watch: {
    'traceList': 'openDefaultPanel'
  },
  methods: {
    async getOnlineTraceContentByContentId() {
      let contentId = this.$route.query.id
      console.log(contentId)
      try {
        this.loading = true
        let res = await api.trace.getOnlineTraceContentByContentId({contentId})
        this.traceList = res.data.list
      } catch (e) {
        this.$Message.error("获取数据失败！")
      } finally {
        this.loading = false
      }
    },
    async getOnlineTraceContentByBill() {
      let billId = this.$route.query.id
      console.log(billId)
      try {
        this.loading = true
        let res = await api.trace.getOnlineTraceContentByBill({billId})
        this.traceList = res.data.list
      } catch (e) {
        this.$Message.error("获取数据失败！")
      } finally {
        this.loading = false
      }
    },
    // 新增
    add() {
      let billId = this.$route.query.id
      this.$router.push({name: 'traceAdd', query: {billId: billId}})
    },
    modify(id) {
      this.$router.push({name: 'traceModify', query: {id: id}})
    },
    async publishOnlineTraceBill() {
      let onlineBillId = this.$route.query.id
      try {
        let res = await api.trace.publishOnlineTraceBill({onlineBillId})
        console.log(res)
        this.$Message.success("发布成功！")
        this.$router.go(-1)
      } catch (e) {
        console.log(e)
        this.$Message.error("操作失败!")
      }
    },
    // 删除操作
    async del(id) {
      var a = new Array();
      a.push(id)
      let json = JSON.stringify(a)
      let token = utils.localStorage.getToken();
      console.log(json)
      axios.post(
        "/report/api/deleteOnlineTraceContent",
        json,
        {headers: {'Content-Type': 'application/json;charset=UTF-8', "jwtToken": token}}
      ).then((response) => {
        this.$Message.success("删除成功！")
        console.log(id)
        this.traceList = this.traceList.filter(item => item.id !== id)
      })
    },
    openDefaultPanel() {
      if (this.traceList.length === 0) {
        return
      }
      this.$nextTick(function() {
        this.panelId = String(this.traceList[0].id)
      })
    },
    downloadModel() {
      window.location.href = '/report/api/exportOnlineTraceBatchResult'

    },
    uploadSuccess (res, file) {
      if (res.data.code ===0) {
        this.$Message.success("导入成功");
      } else {
        this.$Message.error("导入失败")
      }
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
.pull-right .upLode{
  width:30px;
  height:32px;
  line-height: 32px;
  border:none;
  border-radius: 5px;
  padding-left:75px;
  background-color: #2DB7F5;
  position:relative;
  cursor: pointer;
  color: #888;
  overflow: hidden;
  display: inline-block;
  *display: inline;
  *zoom: 1
}
.pull-right >.upLode input{
  position: absolute;
  font-size: 100px;
  right: 0;
  top: 0;
  opacity: 0;
  filter: alpha(opacity=0);
  cursor: pointer;
}
</style>
