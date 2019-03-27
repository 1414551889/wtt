import Vue from 'vue'
import Router from 'vue-router'
import utils from '@/utils'
import login from '@/pages/login'
import main from '@/pages/main'
// import index from '@/pages/index'
import error from '@/pages/error'
// 工作总结
import summary from './summary'
// 项目推进
import project from './project'
// 上线计划
import plan from './plan'
// 上线报告
import report from './report'
// 上线追踪
import trace from './trace'
// 移动端，领导界面
import mobile from './mobile'

Vue.use(Router)

const router = new Router({
  mode: 'hash',
  base: process.env.ROUTER_BASE,
  routes: [
    {
      path: '/login',
      name: 'login',
      component: login,
      meta: {role: [1, 2, 3]}
    }, {
      path: '/',
      component: main,
      meta: {requiresAuth: true, role: [1, 2, 3]},
      children: [{
        path: '',
        name: 'index',
        beforeEnter: (to, from, next) => {
          let user = utils.localStorage.getUser()
          if (user.role !== 3) {
            next({path: '/project', replace: true})
          } else if (user.role === 3) {
            next({path: '/mobile', replace: true})
          }
        }
      }]
    }, summary, project, plan, report, mobile, trace,
    {
      path: '*',
      component: error,
      meta: {role: [1, 2, 3]}
    }
  ]
})

router.beforeEach((to, from, next) => {
  let token = utils.localStorage.getToken()
  let user = utils.localStorage.getUser()
  // 判断是否需要登录
  if (to.matched.some(record => record.meta.requiresAuth)) {
    if (!token) {
      console.log("router全局拦截--未登录--跳转到登录页")
      next({
        path: '/login'// ,
        // query: { redirect: to.fullPath }
      })
    } else {
      // TODO: 验证当前用户是否有此路由的访问权限
      if (to.matched[0].meta.role.indexOf(user.role) !== -1) {
        next()
      } else {
        // 无权限
        if (from.fullPath === '/') {
          // 如果是直接新打开地址过来，导航到首页
          next('/')
        } else {
          // 如果是从其他页面过来，则停留再原页面
          next(false)
        }
      }
    }
  } else {
    next() // 确保一定要调用 next()
  }
})

router.afterEach(route => {
})
export default router
