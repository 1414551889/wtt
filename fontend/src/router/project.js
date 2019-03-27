import main from '@/pages/main'
const edit = () => import(/* webpackChunkName: "group-project" */ '@/pages/project/edit')
const list = () => import(/* webpackChunkName: "group-project" */ '@/pages/project/list')
const add = () => import(/* webpackChunkName: "group-project" */ '@/pages/project/add')
const modify = () => import(/* webpackChunkName: "group-project" */ '@/pages/project/modify')
const template = () => import(/* webpackChunkName: "group-project" */ '@/pages/project/template')
const view = () => import(/* webpackChunkName: "group-project" */ '@/pages/project/view')

export default {
  path: '/project',
  component: main,
  meta: {requiresAuth: true, role: [1, 2]},
  children: [{
    path: '',
    component: list
  }, {
    path: 'edit',
    name: 'projectEdit',
    component: edit,
    meta: {refPath: '/project/'}
  }, {
    path: 'add',
    name: 'projectAdd',
    component: add,
    meta: {refPath: '/project/'}
  }, {
    path: 'modify',
    name: 'projectModify',
    component: modify,
    meta: {refPath: '/project/'}
  }, {
  	path: 'template',
  	name: 'projectTemplate',
  	component: template,
  	meta: {refPath:'/project/'}
  }, {
    path: 'view',
    name: 'projectView',
    component: view,
    meta: {refPath: '/project/'}
  }]
}
