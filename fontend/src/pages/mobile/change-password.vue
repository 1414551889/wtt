<template lang="html">
  <div class="window">
    <Form ref="changePassWordForm" :model="changePassWordForm" :label-width="80" :rules="rules">
      <FormItem prop="oldPass" label="原始密码:" style="width:98%">
          <Input v-model="changePassWordForm.oldPass" type="password"></Input>
      </FormItem>
      <FormItem prop="newPass" label="新密码:" style="width:98%">
          <Input v-model="changePassWordForm.newPass" type="password"></Input>
      </FormItem>
      <FormItem prop="confirmPass" label="确认密码:" style="width:98%">
          <Input v-model="changePassWordForm.confirmPass" type="password"></Input>
      </FormItem>
      <Button type="ghost" @click="handleReset" class="btn pull-left">重置</Button>
      <Button type="primary" @click="handleSubmit" class="btn pull-right">确定</Button>
    </Form>
  </div>
</template>

<script>
import api from '@/service'
export default {
  data() {
    return {
      changePassWordForm: {
        oldPass: '',
        newPass: '',
        confirmPass: ''
      },
      rules: {
        oldPass: [{required: true, message: '请输入原始密码', trigger: 'blur'}],
        newPass: [{required: true, min: 6, message: '请输入6位以上新密码', trigger: 'blur'}],
        confirmPass: [{required: true, message: '请再次输入新密码', trigger: 'blur'}]
      }
    }
  },
  methods: {
    handleSubmit() {
      this.$refs.changePassWordForm.validate(async (valid) => {
        if (!valid) {
          return
        }
        if (this.changePassWordForm.oldPass === this.changePassWordForm.newPass) {
          this.$Message.error("新密码不能与原始密码相同!")
          return
        } else if (this.changePassWordForm.newPass !== this.changePassWordForm.confirmPass) {
          this.$Message.error("两次输入的密码不一致!")
          return
        }
        try {
          let res = await api.user.changePassWord(this.changePassWordForm)
          console.log(res)
          this.$Message.success("修改成功,请重新登录")
          // 重新登录
          setTimeout(function() {
            window.localStorage.clear()
            window.location = process.env.ROUTER_BASE
          }, 1000)
          // 重置表单
          // this.$refs.changePassWordForm.resetFields()
        } catch (e) {
          this.$Message.error(e.message)
        }
      })
    },
    handleReset() {
      this.$refs.changePassWordForm.resetFields()
    }
  }
}
</script>

<style lang="css" scoped>
.window {
  padding: 30px 20px;
}
.btn {
  width: 49%;
  display: inline-block;
}
</style>
