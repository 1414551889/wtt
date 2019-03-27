import main from '@/pages/main'

const index = () => import(/* webpackChunkName: "group-plan" */ '@/pages/plan/index')
const edit = () => import(/* webpackChunkName: "group-plan" */ '@/pages/plan/edit')
const list = () => import(/* webpackChunkName: "group-plan" */ '@/pages/plan/list')
const add = () => import(/* webpackChunkName: "group-plan" */ '@/pages/plan/add')
const modify = () => import(/* webpackChunkName: "group-plan" */ '@/pages/plan/modify')
const view = () => import(/* webpackChunkName: "group-plan" */ '@/pages/plan/view')

export default {
  path: '/plan',
  component: main,
  meta: {requiresAuth: true, role: [1, 2]},
  children: [{
    path: '',
    name: 'plan',
    component: index
  }, {
    path: 'edit',
    name: 'planEdit',
    component: edit,
    meta: {refPath: '/plan/'}
  }, {
    path: 'list',
    name: 'planList',
    component: list,
    meta: {refPath: '/plan/'}
  }, {
    path: 'add',
    name: 'planAdd',
    component: add,
    meta: {refPath: '/plan/'}
  }, {
    path: 'modify',
    name: 'planModify',
    component: modify,
    meta: {refPath: '/plan/'}
  }, {
    path: 'view',
    name: 'planView',
    component: view,
    meta: {refPath: '/plan/'}
  }]
}
