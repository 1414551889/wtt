<template lang="html">
  <div>
    <div class="ui-toolbar">
      <Button type="default" icon="ios-arrow-back" @click="$router.go(-1)">返回</Button>
      <Button type="primary" icon="folder" @click="submit">更新</Button>
    </div>
    <Row>
      <Col span="12" offset="6">
        <Form ref="form" :model="formData" :rules="formRule" :label-width="100">
          <FormItem label="所在中心：" prop="centerId">
            <Select v-model="formData.centerId" placeholder="请选择中心" disabled>
              <Option v-for="item in $store.state.centers" :key="item.id" :value="item.id">{{item.name}}</Option>
            </Select>
          </FormItem>
          <!--<FormItem label="重点项目：" prop="isImportant">
            <RadioGroup v-model="formData.isImportant">
              <Radio :label="1">是</Radio>
              <Radio :label="0">否</Radio>
            </RadioGroup>
          </FormItem>-->
          <FormItem label="半周报名称：" prop="projectName">
            <Input v-model="formData.projectName" placeholder="请输入项目名称"></Input>
          </FormItem>
          <FormItem label="半周报内容：" prop="workContent">
            <Input v-model="formData.workContent" placeholder="请输入工作项目"></Input>
          </FormItem>
          <FormItem label="子项目内容：" prop="subProject">
            <Input v-model="formData.subProject" type="textarea" placeholder="请输入工作内容" :autosize="{minRows: 2}"></Input>
          </FormItem>
   </Form>
      </Col>
    </Row>
  </div>
</template>

<script>
import api from '@/service'
import utils from '@/utils'
import axios from 'axios'
export default {
  name: 'summary_modify',
  data() {
    return {
      formData: {
        id: '1',
        projectName: '',
        workContent: '',
        subProject: ''
      },
      formRule: {
        centerId: {required: true},
        isImportant: {required: true},
        projectName: {required: true, message: '请填写项目名称！', trigger: 'blur'},
        workProject: {required: true, message: '请填写工作项目！', trigger: 'blur'},
        workContent: {required: true, message: '请填写工作内容！', trigger: 'blur'},
        overState: {required: true, message: '请填写完成情况！', trigger: 'blur'}
       /*  responsible: {required: true, message: '请填写责任人！', trigger: 'blur'},
        cooperator: {required: true, message: '请填写配合人！', trigger: 'blur'},
        timeLimit: {required: true, message: '请填写完成时限！', trigger: 'blur'},
        isRisk: {required: true},
        riskSituation: {required: true, message: '请填写项目风险！', trigger: 'change'} */
      }
    }
  },
  watch: {
    'formData.isRisk': 'setDefaultRisk'
  },
  created() {
    this.initData()
  },
  methods: {
    async initData() {
      let contentId = this.$route.query.id
      try {
        console.log(contentId)
        let res = await api.summary.getHalfWeekContentById({contentId})

        this.formData = res
      } catch (e) {
        console.log(e)
        this.$Message.error("获取数据失败！")
      }
    },
    submit() {
      this.$refs.form.validate(async valid =>{
        if(valid){
          let params = this.formData
          let json= JSON.stringify(params);
          let token = utils.localStorage.getToken();
          axios.post(
            "/report/api/saveHalfWeekContent",
            json,
            {headers: {'Content-Type': 'application/json;charset=UTF-8', "jwtToken":token}}
          ).then(function (response) {
            console.log(response)
          })
          this.$Message.success("更新成功！")
          this.$router.go(-1)
        }else{
          this.$Message.success("更新失败！")
        }
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
