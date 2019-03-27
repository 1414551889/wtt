<template lang="html">
  <div>
    <div class="ui-toolbar">
        <Button type="default" icon="ios-arrow-back" @click="$router.go(-1)">返回</Button>
        <Button v-if="$store.state.user && $store.state.user.role === 1" type="primary" icon="android-add" @click="add">新增</Button>
        <Button v-if="$store.state.user && $store.state.user.role === 2" type="success" icon="android-cloud-done" @click="publishBill">发布</Button>
        <div class="pull-right">
           <Button type="info" icon="android-download" @click="downloadModel">下载</Button>
           <Upload  action="/report/api/imporOnlineBatchRecharge" :headers="{jwtToken:token}" :data="{billId:billId}" name="file" accept="application/vnd.ms-excel" @on-succcess="uploadSuccess">
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
    <div v-else-if="planList.length === 0">
      暂无数据
    </div>
    <Collapse v-else v-model="panelId" accordion>
      <Panel v-for="(item, index) in planList" :key="item.contentId" :name="String(item.contentId)">
        <p class="ellipsis" style="width: 100%; float: right;margin-left:-5px; padding-left:8px; padding-right: 130px">
          {{item.centerName}}: {{item.project}} - {{item.type}}
        </p>
        <ButtonGroup v-if="$store.state.user && $store.state.user.role === 1" class="modify" size="small">
          <Button type="warning" icon="edit" @click.stop="modify(item.id)">修改</Button>
          <Button type="error" icon="ios-trash" @click.stop="del(item.id)">删除</Button>
        </ButtonGroup>
        <p slot="content" class="content">
          <b class="label">项目编号：</b>{{item.projectNumber}}
          <b class="label">类型：</b>{{item.type}}
          <b class="label">任务内容：</b>
          {{item.content}}
          <b class="label">所属项目：</b>{{item.project}}
          <b class="label">测试时间：</b>{{item.testTime}}
          <b class="label">产品人员：</b>{{item.productStaff}}
          <b class="label">开发人员：</b>{{item.developStaff}}
          <b class="label">测试人员：</b>{{item.testStaff}}
          <b class="label">上线时间：</b>{{item.onlineTime}}
          <b class="label">备注(上线计划)：</b>{{item.remark}}
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
      planList: [],
      loading: false,
      token: null,
      billId: null
    }
  },
  created() {
    this.getOnlineContentDetailData(),
    this.token = utils.localStorage.getToken(),
    this.billId = this.$route.query.id
  },
  watch: {
    'planList': 'openDefaultPanel'
  },
  methods: {
    async getOnlineContentDetailData() {
      let billId = this.$route.query.id
      try {
        this.loading = true
        let res = await api.plan.getOnlineContentByBill({billId})
        this.planList = res.data.list
      } catch (e) {
        this.$Message.error("获取数据失败！")
      } finally {
        this.loading = false
      }
    },
    //新增
    add() {
      let billId = this.$route.query.id
      this.$router.push({name: 'planAdd', query: {billId:billId}})
    },
    modify(id) {
      this.$router.push({name: 'planModify', query: {id:id}})
    },
    async publishOnlineBill() {
      let billId = this.$route.query.id
      try {
        let res = await api.plan.publishOnlineBill({billId})
        console.log(res)
        this.$Message.success("发布成功！")
        this.$router.go(-1)
      } catch (e) {
        console.log(e)
        this.$Message.error("操作失败!")
      }
    },
    // 删除一条数据
    async del(id) {
      var a = new Array();
      a.push(id)
      let json=JSON.stringify(a)
      let token = utils.localStorage.getToken();
      console.log(json)
      axios.post(
        "/report/api/deleteOnlineContent",
        json,
        {headers: {'Content-Type': 'application/json;charset=UTF-8', "jwtToken": token}}
      ).then(response => {
        this.$Message.success("删除成功！")
        console.log(id)
        this.planList = this.planList.filter(item => item.id !== id)
      })
    },
    openDefaultPanel() {
      if (this.planList.length === 0) {
        return
      }
      this.$nextTick(function() {
        this.panelId = String(this.planList[0].contentId)
      })
    },
    downloadModel() {
      window.location.href = '/report/api/exportOnlineBatchResult';

    },
    uploadSuccess (res,file){
      if(res.data.code===0) {
        this.$Message.success("导入成功");
      }else{
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
