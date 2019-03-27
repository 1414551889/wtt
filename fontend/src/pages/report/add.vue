<template lang="html">
  <div>
    <div class="ui-toolbar">
      <Button type="default" icon="ios-arrow-back" @click="$router.go(-1)">返回</Button>
      <Button type="primary" icon="folder" @click="submit">保存</Button>
    </div>
    <Row>
      <Col span="12" offset="6">
        <Form ref="form" :model="formData" :rules="formRule" :label-width="100">
          <FormItem label="所在中心：" prop="centerId">
            <Select v-model="formData.centerId" placeholder="请选择中心" disabled>
              <Option v-for="item in $store.state.centers" :key="item.id" :value="item.id">{{item.name}}</Option>
            </Select>
          </FormItem>
          <FormItem label="分类：" prop="type">
            <Input v-model="formData.type" placeholder="请输入分类名称"></Input>
          </FormItem>
          <FormItem label="具体内容：" prop="content">
            <Input v-model="formData.content" placeholder="请输入具体内容"></Input>
          </FormItem>
          <FormItem label="所属项目：" prop="project">
            <Input v-model="formData.project" placeholder="请输入所属项目"></Input>
          </FormItem>
          <FormItem label="提测时间：" prop="testTime">
            <Input v-model="formData.testTime" placeholder="请输入提测时间"></Input>
          </FormItem>
          <FormItem label="产品人员" prop="productStaff">
            <Input v-model="formData.productStaff" placeholder="请输入产品人员"></Input>
          </FormItem>
          <FormItem label="开发人员：" prop="developStaff">
            <Input v-model="formData.developStaff" placeholder="请输入开发人员"></Input>
          </FormItem>
          <FormItem label="测试人员：" prop="testStaff">
            <Input v-model="formData.testStaff" placeholder="请输入测试人员"></Input>
          </FormItem>
          <FormItem label="备注：" prop="remark">
            <Input v-model="formData.remark" placeholder="请输入备注"></Input>
          </FormItem>
        </Form>
      </Col>
    </Row>
  </div>
</template>

<script>
import utils from '@/utils'
import axios from 'axios'
export default {
  name: 'report_add',
  data() {
    return {
      formData: {
        projectNumber :'22',
        type : '',
        content: '',
        project: '',
        centerId: '2',
        centerName: 'eee',
        testTime: '',
        productStaff: '',
        developStaff: '',
        testStaff	: '',
        onlineTime: '2222',
        remark: '',
        onlinePlanBillId: '',
      },
      formRule: {
        type: {required: true, message: '请输入分类名称！', trigger: 'blur'},
        content: {required: true, message: '请输入具体内容！', trigger: 'blur'},
        project: {required: true, message: '请输入所属项目！', trigger: 'blur'},
        testTime: {required: true, message: '请填写提测时间！', trigger: 'blur'},
        productStaff: {required: true, message: '请填写产品人员！', trigger: 'blur'},
        developStaff: {required: true, message: '请填写开发人员！', trigger: 'blur'},
        testStaff: {required: true, message: '请填写测试人员！', trigger: 'blur'},
      }
    }
  },
  computed: {
    user: function() {
      return this.$store.state.user
    }
  },
  watch: {
    'formData.isRisk': 'setDefaultRisk',
    'user': 'initData'
  },
  created() {
    this.initData()
  },
  methods: {
    initData() {
      if (this.user) {
        this.formData.centerId = this.user.centerId
      }
    },
    submit() {
      let params= this.formData;
      params.onlinePlanBillId = this.$route.query.billId;
      let json= JSON.stringify(params);
      let token = utils.localStorage.getToken();
      console.log(params);
      axios.post(
        "/report/api/saveOnlinePlanContent",
        json,
        {headers: {'Content-Type': 'application/json;charset=UTF-8', "jwtToken": token}}
      ).then(function (response) {
      })
      this.$Message.success("保存成功！")
      this.$router.go(-1)
    },
    setDefaultRisk() {
      if (this.formData.isRisk === 0) {
        this.formData.riskSituation = '无'
      } else {
        this.formData.riskSituation = ''
      }
    }
  }
}
</script>

<style lang="css" scoped>
</style>
