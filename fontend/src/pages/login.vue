<template>
  <div class="login">
    <Row>
      <Col :xs="{ span: 20, offset: 2 }" :sm="{ span: 6, offset: 9 }">
        <Form class="loginForm" ref="form" :model="formData" :rules="rule">
          <h2 class="title">全通微头条</h2>
          <FormItem prop="user">
            <Input type="text" v-model="formData.user" placeholder="请输入账号">
                <Icon type="ios-person-outline" slot="prepend"></Icon>
            </Input>
          </FormItem>
          <FormItem prop="password">
            <Input type="password" v-model="formData.password" placeholder="请输入密码">
                <Icon type="ios-locked-outline" slot="prepend"></Icon>
            </Input>
          </FormItem>
          <div class="kaptcha">
            <FormItem prop="validateCode" style="margin-right: 80px;">
              <Input type="text" v-model="formData.validateCode" placeholder="请输入验证码">
                  <Icon type="ios-gear-outline" slot="prepend"></Icon>
              </Input>
            </FormItem>
            <img :src="kaptchaImageUrl" alt="" @click="getKaptchaImage">
          </div>
          <FormItem>
            <Button type="primary" @click="handleSubmit('form')" style="width:100%;">登 录</Button>
          </FormItem>
        </Form>
      </Col>
    </Row>
  </div>
</template>
<script>
import utils from '@/utils'
import api from '@/service'
export default {
  name: 'login',
  data () {
    return {
      kaptchaImageUrl: '',
      formData: {
        user: '',
        password: '',
        validateCode: ''
      },
      rule: {
        user: [
          { required: true, message: '请输入账号', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' },
          { type: 'string', min: 6, message: '密码至少是6位以上', trigger: 'blur' }
        ],
        validateCode: [
          { require: true, message: '请输入验证码', trigger: 'blur' }
        ]
      }
    }
  },
  beforeCreate () {
    let redirect = this.$route.query.redirect
    let token = utils.localStorage.getToken()
    // 已登录用户，不允许进入登录页
    if (token) {
      // 跳转到目标页面
      if (redirect) {
        console.log(`用户已登录--跳转到目标页面：${redirect}`)
        this.$router.replace({path: redirect})
      } else {
        console.log('用户已登录--跳转到首页')
        this.$router.replace({name: 'index'})
      }
    }
  },
  created() {
    // 获取初始验证码
    this.getKaptchaImage()
  },
  methods: {
    handleSubmit(name) {
      this.$refs[name].validate((valid) => {
        if (valid) {
          this.$store.dispatch('login', this.formData).then(data => {
            if (data === -1) {
            	if(!this.formData.validateCode){
            		this.$Message.error('验证码错误！')
            	}else{
              	this.$Message.error('用户名密码错误！')
            	}
              // 更新验证码
              this.getKaptchaImage()
              return
            }
            this.$router.replace({name: 'index'})
            if (window.wttapp && window.wttapp.loginBindUserId) {
              console.log('客户端绑定userId:', data.user.userId)
              window.wttapp.loginBindUserId(data.user.userId)
            }
          })
        } else {
          this.$Message.error('用户名密码错误！');
        }
      })
    },
    async getKaptchaImage() {
      let res = await api.user.getKaptchaImage()
      this.kaptchaImageUrl = `data:image/jpeg;base64,${res.img}`
    }
  }
}
</script>
<style scoped>
.login {
  height: 100%;
  background: url('~@/assets/login-bg.png') no-repeat #f5f7f9;
  background-size: cover;
}
.loginForm {
  margin-top: 100px;
}
.title {
  text-align: center;
  color: #eee;
  margin: 20px 0;
}
.kaptcha {
  position: relative;
}
.kaptcha > img {
  position: absolute;
  top: 2px;
  right: 0;
  height: 30px;
  width: 70px;
  cursor: pointer;
}
</style>
