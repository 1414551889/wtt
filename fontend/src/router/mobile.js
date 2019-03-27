import mobile from '@/pages/mobile'
const project = () => import(/* webpackChunkName: "group-mobile" */ '@/pages/mobile/project')
const historyList = () => import(/* webpackChunkName: "group-mobile" */ '@/pages/mobile/history-list')
const historyView = () => import(/* webpackChunkName: "group-mobile" */ '@/pages/mobile/history-view')
const changePassWord = () => import(/* webpackChunkName: "group-mobile" */ '@/pages/mobile/change-password')
const daily = () => import(/* webpackChunkName: "group-mobile" */ '@/pages/mobile/daily')
const halfweekly = () => import(/* webpackChunkName: "group-mobile" */ '@/pages/mobile/half-weekly')
/* const weekly = () => import(/!* webpackChunkName: "group-mobile" *!/ '@/pages/mobile/weekly') */
const sxjh = () => import(/* webpackChunkName: "group-mobile" */ '@/pages/mobile/sxjh')
const sxbg = () => import(/* webpackChunkName: "group-mobile" */ '@/pages/mobile/sxbg')

export default {
  path: '/mobile',
  component: mobile,
  meta: {requiresAuth: true, role: [3]},
  children: [{
    path: '',
    name: 'benzhoutuijin',
    component: project
  }, {
    path: 'history',
    name: 'tuijinjilu',
    component: historyList
  }, {
    path: 'history/view',
    name: 'historyView',
    component: historyView,
    meta: {refName: 'tuijinjilu'}
  }, {
    path: 'changepassword',
    name: 'changePassWord',
    component: changePassWord
  }, {
    path: 'daily',
    name: 'mobiledaily',
    component: daily
  }, {
    path: 'halfweekly',
    name: 'halfweekly',
    component: halfweekly
  }, /* {
    path: 'weekly',
    name: 'weekly',
    component: weekly
  } */, {
    path: 'sxjh',
    name: 'sxjh',
    component: sxjh
  }, {
    path: 'sxbg',
    name: 'sxbg',
    component: sxbg
  }]
}
