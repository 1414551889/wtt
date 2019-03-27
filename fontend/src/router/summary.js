/**
 * 工作总结
 */
import main from '@/pages/main'
// 日报列表
const daily = () => import(/* webpackChunkName: "group-summary" */ '@/pages/summary/daily/daily')
// 日报新增
const dailyAdd = () => import(/* webpackChunkName: "group-summary" */ '@/pages/summary/daily/add')

// const dailyEdit = () => import(/* webpackChunkName: "group-summary" */ '@/pages/summary/daily/edit')
// 日报查看
const dailyView = () => import(/* webpackChunkName: "group-summary" */ '@/pages/summary/daily/view')
// 日报修改
const dailyModify = () => import(/* webpackChunkName: "group-summary" */ '@/pages/summary/daily/modify')
// 半周报列表
const halfweek = () => import(/* webpackChunkName: "group-summary" */ '@/pages/summary/halfweek')
// const week = () => import(/* webpackChunkName: "group-summary" */ '@/pages/summary/week')
// const month = () => import(/* webpackChunkName: "group-summary" */ '@/pages/summary/month')
const add = () => import(/* webpackChunkName: "group-summary" */ '@/pages/summary/add')
// 半周报查看
const view = () => import(/* webpackChunkName: "group-summary" */ '@/pages/summary/view')
const edit = () => import(/* webpackChunkName: "group-summary" */ '@/pages/summary/edit')
const modify = () => import(/* webpackChunkName: "group-summary" */ '@/pages/summary/modify')


export default {
  path: '/summary',
  component: main,
  meta: {requiresAuth: true, role: [1, 2]},
  children: [{
    path: '',
    name: 'daily',
    component: daily,
    meta: {refPath: '/summary/'}
  }, {
    path: 'halfweek',
    name: 'halfweek',
    component: halfweek,
    meta: {refPath: '/summary/halfweek'}
  }, /*{
    path: 'week',
    name: 'week',
    component: week
  }, {
    path: 'month',
    name: 'month',
    component: month
  },*/ {
    path: 'add',
    name: 'summaryAdd',
    component: add,
    meta: {refPath: '/summary/halfweek'}
  }, {
    path: 'view',
    name: 'summaryView',
    component: view,
    meta: {refPath: '/summary/halfweek'}
  }, {
    path: 'edit',
    name: 'summaryEdit',
    component: edit,
    meta: {refPath: '/summary/halfweek'}
  }, {
    path: 'modify',
    name: 'summaryModify',
    component: modify,
    meta: {refPath: '/summary/halfweek'}
  }, {
    path: 'daily/Add',
    name: 'summaryDailyAdd',
    component: dailyAdd,
    meta: {refPath: '/summary/'}
  }, /*{
    path: 'daily/Edit',
    name: 'summaryDailyEdit',
    component: dailyEdit,
    meta: {refPath: '/summary/'}
  }, */{
    path: 'daily/View',
    name: 'summaryDailyView',
    component: dailyView,
    meta: {refPath: '/summary/'}
  },{
    path: 'daily/Modify',
    name: 'summaryDailyModify',
    component: dailyModify,
    meta: {refPath: '/summary/'}
  }]
}
