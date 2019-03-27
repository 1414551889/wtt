/**
 * Created by JackChen on 2018/4/26.
 */
import main from '@/pages/main'

const trace = () => import(/* webpackChunkName: "group-trace" */ '@/pages/trace/trace')
const edit = () => import(/* webpackChunkName: "group-plan" */ '@/pages/trace/edit')
// const list = () => import(/* webpackChunkName: "group-plan" */ '@/pages/trace/list')
const add = () => import(/* webpackChunkName: "group-plan" */ '@/pages/trace/add')
const modify = () => import(/* webpackChunkName: "group-plan" */ '@/pages/trace/modify')
const view = () => import(/* webpackChunkName: "group-plan" */ '@/pages/trace/view')

export default {
  path: '/trace',
  component: main,
  meta: {requiresAuth: true, role: [1, 2]},
  children: [{
    path: '',
    name: 'trace',
    component: trace
  }, {
    path: 'edit',
    name: 'traceEdit',
    component: edit,
    meta: {refPath: '/trace/'}
  }, {
    path: 'add',
    name: 'traceAdd',
    component: add,
    meta: {refPath: '/trace/'}
  }, {
    path: 'modify',
    name: 'traceModify',
    component: modify,
    meta: {refPath: '/trace/'}
  }, {
    path: 'view',
    name: 'traceView',
    component: view,
    meta: {refPath: '/trace/'}
  }]
}
