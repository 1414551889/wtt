import main from '@/pages/main'

const index = () => import(/* webpackChunkName: "group-report" */ '@/pages/report/index')
const edit = () => import(/* webpackChunkName: "group-report" */ '@/pages/report/edit')
// const list = () => import(/* webpackChunkName: "group-report" */ '@/pages/report/list')
const add = () => import(/* webpackChunkName: "group-report" */ '@/pages/report/add')
const modify = () => import(/* webpackChunkName: "group-report" */ '@/pages/report/modify')
const view = () => import(/* webpackChunkName: "group-report" */ '@/pages/report/view')

export default {
  path: '/report',
  component: main,
  meta: {requiresAuth: true, role: [1, 2]},
  children: [{
    path: '',
    name: 'report',
    component: index
  }, {
    path: 'edit',
    name: 'reportEdit',
    component: edit,
    meta: {refPath: '/report/'}
  }, {
    path: 'add',
    name: 'reportAdd',
    component: add,
    meta: {refPath: '/report/'}
  }, {
    path: 'modify',
    name: 'reportModify',
    component: modify,
    meta: {refPath: '/report/'}
  }, {
    path: 'view',
    name: 'reportView',
    component: view,
    meta: {refPath: '/report/'}
  }]
}

