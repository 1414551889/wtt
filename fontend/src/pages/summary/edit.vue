<template lang="html">
  <div>
    <div class="ui-toolbar">
        <Button type="default" icon="ios-arrow-back" @click="$router.go(-1)">返回</Button>
        <Button v-if="$store.state.user && $store.state.user.role === 1" type="primary" icon="android-add" @click="add">新增</Button>
        <Button v-if="$store.state.user && $store.state.user.role === 2" type="success" icon="android-cloud-done" @click="publishBill">发布</Button>
        <div class="pull-right">
           <Button type="info" icon="android-download" @click="downloadModel">下载</Button>
           <Upload action="/report/api/imporHalfBatchRecharge" :headers="{jwtToken:token}" :data="{billId:billId}" name="file" accept="application/vnd.ms-excel" @on-succcess="uploadSuccess">
              <Button type="info" class="upload" icon="ios-cloud-upload-outline" @on-success="uploadSuccess">导出</Button>
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
    <div v-else-if="halfWeekList.length===0">
      暂无数据
    </div>
    <Collapse v-else v-model="panelId" accordion>
      <Panel v-for="(item, index) in halfWeekList" :key="item.id" :name="String(item.id)">
        <p class="ellipsis" style="width: 100%; float: right;margin-left:-5px; padding-left:8px; padding-right: 130px">
          {{item.centerName}}: {{item.projectName}}
        </p>
        <ButtonGroup v-if="$store.state.user && $store.state.user.role === 1" class="modify" size="small">
          <Button type="warning" icon="edit" @click.stop="modify(item.id)">修改</Button>
          <Button type="error" icon="ios-trash" @click.stop="del(item.id)">删除</Button>
        </ButtonGroup>
        <p slot="content" class="content">
          <b class="label">半周报名称：</b>{{item.projectName}}
          <b class="label">半周报内容：</b>{{item.workContent}}
          <b class="label">子项目名称：</b>{{item.subProject}}
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
      panelId: '',
      halfWeekList: [],
      loading: false,
      token: null,
      billId: null
    }
  },
  created() {
    this.getContentByHalfBill()
    this.token = utils.localStorage.getToken()
    this.billId = this.$route.query.id
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
        console.log(res)
        this.halfWeekList = res.data.list
      } catch (e) {
        this.$Message.error("获取数据失败！")
      } finally {
        this.loading = false
      }
    },
    add() {
      this.$router.push({name: 'summaryAdd', query: this.$route.query})
    },
    modify(id) {
      this.$router.push({name: 'summaryModify', query: {id}})
    },
    async publishHalfBill() {
      let id = this.$route.query.id
      try {
        console.log(id)
        let res = await api.summary.publishHalfBill({id})
        this.$Message.success("发布成功！")
        this.$router.go(-1)
      } catch (e) {
        console.log(e)
        this.$Message.error("操作失败!")
      }
    },

 // 删除按钮
    async del(id) {
      var a = new Array();
      a.push(id)
      let json = JSON.stringify(a)
      let token = utils.localStorage.getToken();
      console.log(json)
      axios.post(
        "/report/api/deleteHalfWeekContent",
        json,
        {headers: {'Content-Type': 'application/json;charset=UTF-8', "jwtToken": token}}
      ).then((response) => {
        this.$Message.success("删除成功！")
        console.log(id)
        this.halfWeekList = this.halfWeekList.filter(item => item.id !== id)
      })
    },
    openDefaultPanel() {
      if (this.halfWeekList.length === 0) {
        return
      }
      this.$nextTick(function() {
        this.panelId = String(this.halfWeekList[0].id)
      })
    },
    downloadModel () {
      console.log("下载文件")
      window.location.href = '/report/api/exportHalfBatchResult';
    },
    uploadSuccess (res, file) {
      if (res.data.code === 0) {
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
.pull-right span{
  display: inline-block;
  position:absolute;
}
.pull-right.file_prev{
  width:30px;
  height:32px;
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
.pull-right >input{
  position: absolute;
  font-size: 100px;
  right: 0;
  top: 0;
  opacity: 0;
  filter: alpha(opacity=0);
  cursor: pointer;
}
</style>
