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
          <FormItem label="项目工作：" prop="projectWork">
            <Input v-model="formData.projectWork" placeholder="请输入项目工作"></Input>
          </FormItem>
          <FormItem label="上线内容：" prop="content">
            <Input v-model="formData.content" placeholder="请输入上线内容"></Input>
          </FormItem>
          <FormItem label="所属转态：" prop="status">
            <Input v-model="formData.status" placeholder="请输入所属状态"></Input>
          </FormItem>
            <FormItem label="负责人" prop="projectManager">
            <Input v-model="formData.projectManager" placeholder="请输入项目负责人"></Input>
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
import api from '@/service'
import utils from '@/utils'
import axios from 'axios'
export default {
  name: 'trace_modify',
  data() {
    return {
      formData: {
        projectWork :'',
        content: '',
        centerId: '',
        centerName: '',
        status: '',
        projectManager: '',
        remark: '',
        onlinePlanBillId: ''
      },
      formRule: {
        projectWork: {required: true, message: '请输入项目工作！', trigger: 'blur'},
        content: {required: true, message: '请输入上线内容！', trigger: 'blur'},
        status: {required: true, message: '请输入所属状态！', trigger: 'blur'},
        projectManager: {required: true, message: '请输入项目负责人！', trigger: 'blur'},
        remark: {required: true, message: '请输入备注！', trigger: 'blur'},
      }
    }
  },
  created() {
    this.initData()
  },
  methods: {
    async initData() {
      let contentId = this.$route.query.id
      try {
        let res = await api.trace.getOnlineTraceContentByContentId({contentId})
        console.log(res)
        this.formData = res
      } catch (e) {
        console.log(e)
        this.$Message.error("获取数据失败！")
      }
    },
    submit() {
      this.$refs.form.validate(async valid => {
        if (valid) {
          let params = this.formData
          let json= JSON.stringify(params);
          let token = utils.localStorage.getToken();
          axios.post(
            "/report/api/saveOnlineTraceContent",
            json,
            {headers: {'Content-Type': 'application/json;charset=UTF-8', "jwtToken": token}}
          ).then(function (response) {
            console.log(response)
          })
          this.$Message.success("更新成功！")
          this.$router.go(-1)
        } else {
          this.$Message.error("验证失败！")
        }
      })
    }
  }
}
</script>
