<template lang="html">
  <div>
    <div class="ui-toolbar">
        <Button type="default" icon="ios-arrow-back" @click="$router.go(-1)">返回</Button>
        <Button v-if="$store.state.user && $store.state.user.role === 1" type="primary" icon="android-add" @click="add">新增</Button>
        <Button v-if="$store.state.user && $store.state.user.role === 2" type="success" icon="android-cloud-done" @click="publishBill">发布</Button>
        <div class="pull-right">
           <Button type="info" icon="android-download" @click="downloadModel">下载</Button>
           <Upload  action="/report/api/imporBatchRecharge" :headers="{jwtToken:token}" :data="{billId:billId}" name="file" accept="application/vnd.ms-excel" @on-succcess="uploadSuccess">
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
    <div v-else-if="projectList.length === 0">
      暂无数据
    </div>
    <Collapse v-else v-model="panelId" accordion>
      <Panel v-for="(item, index) in projectList" :key="item.contentId" :name="String(item.contentId)">
        <p class="ellipsis" style="width: 100%; float: right;margin-left:-5px; padding-left:8px; padding-right: 130px">
          <Icon v-if="item.isImportant === 1" type="ios-star" class="icon" style="color: gold;"></Icon>
          <Icon v-if="item.isRisk === 1" type="alert-circled" class="icon" style="color: red;"></Icon>
          {{item.centerName}}: {{item.projectName}} - {{item.workProject}}
        </p>
        <ButtonGroup  class="modify" size="small">
          <Button type="warning" icon="edit" @click.stop="modify(item.contentId)">修改</Button>
          <Button type="error" icon="ios-trash" @click.stop="del(item.contentId)">删除</Button>
        </ButtonGroup>
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
import utils from '@/utils'

export default {
  data () {
    return {
      title: '',
      panelId: '',
      projectList: [],
      loading: false,
      token: null,
      billId: null
    }
  },
  created() {
    this.getProjectContent()
    this.token = utils.localStorage.getToken()
    this.billId = this.$route.query.id
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
        this.$Message.error("获取数据失败！")
      } finally {
        this.loading = false
      }
    },
    add() {
      this.$router.push({name: 'projectAdd', query: this.$route.query})
    },
    modify(id) {
      this.$router.push({name: 'projectModify', query: {id}})
    },
    async publishBill() {
      let billId = this.$route.query.id
      try {
        let res = await api.project.publishBill({billId})
        console.log(res)
        this.$Message.success("发布成功！")
        this.$router.go(-1)
      } catch (e) {
        console.log(e)
        this.$Message.error("操作失败!")
      }
    },
    async del(id) {
      try {
        await api.project.deleteContent({contentId: id})
        this.projectList = this.projectList.filter(item => item.contentId !== id)
      } catch (e) {
        console.log(e)
        this.$Message.error("操作失败！")
      }
    },
    openDefaultPanel() {
      if (this.projectList.length === 0) {
        return
      }
      this.$nextTick(function() {
        this.panelId = String(this.projectList[0].contentId)
      })
    },
    downloadModel() {
      console.log("下载文件")
      window.location.href= "/report/api/exportBatchResult";
    },
    uploadSuccess (res, file,) {
        if (res.data.code === 0) {
          this.$Message.success("导入成功");
        }else {
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
</style>
