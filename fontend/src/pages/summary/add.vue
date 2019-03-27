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
          <FormItem label="半周报名称：" prop="projectName">
            <Input v-model="formData.projectName" placeholder="请输入半周报名称"></Input>
          </FormItem>
          <FormItem label="半周报内容：" prop="workContent">
            <Input v-model="formData.workContent" type="textarea" placeholder="请输入半周报内容" :autosize="{minRows: 2}"></Input>
          </FormItem>
           <FormItem label="子项目名称：" prop="subProject">
            <Input v-model="formData.subProject" type="textarea" placeholder="请输入子项目" :autosize="{minRows: 2}"></Input>
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
    name: 'summary_add',
    data() {
      return {
        formData: {
          centerId: '',
          projectName: '',
          workContent: '',
          subProject: ''
        },
        formRule: {
          centerId: {required: true},
          projectName: {required: true, message: '请填写半周报名称！', trigger: 'blur'},
          workContent: {required: true, message: '请填写工作内容！', trigger: 'blur'},
          subProject: {required: true, message: '请填写子项目！', trigger: 'blur'},
          /* responsible: {required: true, message: '请填写责任人！', trigger: 'blur'},
          cooperator: {required: true, message: '请填写配合人！', trigger: 'blur'},
          timeLimit: {required: true, message: '请填写完成时限！', trigger: 'blur'},
          isRisk: {required: true},
          riskSituation: {required: true, message: '请填写项目风险！', trigger: 'change'} */
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
        let id = this.$route.query.id
        let params = this.formData
        let centerName = this.$store.state.centers.filter(item => item.id === this.formData.centerId)[0].name
        params.centerName = centerName
        params.billId= id
        let json = JSON.stringify(params);
        let token = utils.localStorage.getToken();
        axios.post(
          "/report/api/saveHalfWeekContent",
          json,
          {headers: {'Content-Type': 'application/json;charset=UTF-8', "jwtToken": token}}
        ).then(({data}) => {
            // 解构
          if (data.code===0) {
            this.$Message.success("保存成功")
            this.$router.go(-1)
          } else {
            this.$Message.error("保存失败")
          }
          // 需要提示保存成功是否
        })
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
