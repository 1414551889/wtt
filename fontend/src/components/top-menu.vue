<template>
  <div>
    <Menu mode="horizontal" theme="primary" :active-name="currentTopMenuPath" @on-select="changeMenu">
      <div class="layout-logo">全通微头条</div>
      <div class="layout-nav">
        <MenuItem v-for="item in menus" :key="item.path" :name="item.path">
          <Icon :type="item.icon"></Icon>
          {{item.title}}
        </MenuItem>
        <Dropdown v-if="user" class="pull-right user" placement="bottom-end" @on-click="dropdownClick">
          {{user.name}}
          <Icon type="arrow-down-b"></Icon>
          <DropdownMenu slot="list">
              <DropdownItem name="changePassWord">修改密码</DropdownItem>
              <DropdownItem name="logout" divided>注销登录</DropdownItem>
          </DropdownMenu>
        </Dropdown>
      </div>
    </Menu>
    <Modal
      v-model="isShowChangePassWordModal"
      title="修改密码"
      width="400"
      @on-ok="ok"
      @on-cancel="cancel">
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
      </Form>
      <div slot="footer">
        <Button type="primary" size="large" long @click="ok">确定</Button>
      </div>
    </Modal>
  </div>
</template>
<script>
import { mapState, mapGetters } from 'vuex'
import * as types from '@/store/mutation-types'
import api from '@/service'

export default {
  name: 'top-menu',
  desc: '顶部导航组件',
  data() {
    return {
      isShowChangePassWordModal: false,
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
  computed: {
    ...mapState(['menus', 'user']),
    ...mapGetters(['currentTopMenuPath'])
  },
  methods: {
    changeMenu(name) {
      if (name === this.currentTopMenuPath) {
        return
      }
      this.$store.commit(types.SET_TOP_MENU_PATH, {path: name})
      this.$router.push({path: name})
    },
    dropdownClick(name) {
      if (name === 'logout') {
        window.localStorage.clear()
        window.location = process.env.ROUTER_BASE
      } else if (name === 'changePassWord') {
        this.isShowChangePassWordModal = true
      }
    },
    ok() {
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
          // 关闭对话框,重置表单
          // this.isShowChangePassWordModal = false
          // this.$refs.changePassWordForm.resetFields()
        } catch (e) {
          this.$Message.error(e.message)
        }
      })
    },
    cancel() {
      // 重置
      this.$refs.changePassWordForm.resetFields()
    }
  }
}
</script>

<style scoped>
.layout-logo{
  width: 200px;
  height: 30px;
  background: url("~@/assets/logo.png") no-repeat left center;
  border-radius: 3px;
  float: left;
  position: relative;
  top: 15px;
  left: 20px;
  color: #fff;
  font-size: 18px;
  line-height: 30px;
  text-align: right;
}
.layout-nav{
  height: 60px;
  margin-left: 290px;
}
.ivu-menu-item-active::before {
  content: "";
  height: 4px;
  width: 100%;
  background-color: #2b85e4;
  position: absolute;
  top: -4px;
  left: 0;
}
.ivu-menu-item-active::after {
  content: "";
  height: 4px;
  width: 100%;
  background-color: #f5f7f9;
  position: absolute;
  bottom: -4px;
  left: 0;
}
.user {
  color: #fff;
  cursor: pointer;
  margin-right: 20px;
}
</style>
