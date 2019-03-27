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
          <FormItem label="日报名称：" prop="title">
            <Input v-model="formData.title" placeholder="请输入日报名称"></Input>
          </FormItem>
          <FormItem label="工作内容：" prop="content">
            <Input v-model="formData.content" type="textarea" placeholder="请输入工作内容" :autosize="{minRows: 2}"></Input>
          </FormItem>
        </Form>
      </Col>
    </Row>
  </div>
</template>

<script>
import api from '@/service'
export default {
  data() {
    return {
      formData: {
        centerId: '',
        projectName: '',
        content: ''
      },
      formRule: {
        centerId: {required: true},
        projectName: {required: true, message: '请填写中心名称！', trigger: 'blur'},
        content: {required: true, message: '请填写工作内容！', trigger: 'blur'}
      }
    }
  },
  created() {
    this.initData()
  },
  methods: {
    async initData() {
      let id = this.$route.query.id
      try {
        let res = await api.summary.getDayReportById({id})
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
          try {
            let res = await api.summary.updateReportContent(params)
            this.$Message.success("更新成功！")
            this.$router.go(-1)
          } catch (e) {
            console.log(e)
            this.$Message.error(e.message)
          }
        }
      })
    }
  }
}
</script>
