'use strict'
import Vue from 'vue'
import Vuex from 'vuex'
import mutations from './mutations'
import actions from './actions'

Vue.use(Vuex)

const state = {
  user: null,
  centers: [{
    id: 1,
    name: '医疗行业中心'
  }, {
    id: 2,
    name: '教育行业中心'
  }, {
    id: 3,
    name: '交通行业中心'
  }, {
    id: 4,
    name: '管理信息化中心'
  }, {
    id: 5,
    name: '云化OA项目组'
  }, {
    id: 6,
    name: '安全项目中心'
  }, {
    id: 7,
    name: '质量管理中心'
  }],
  menus: [
    {
      id: 'A',
      title: '工作总结',
      path: '/summary',
      icon: 'ios-navigate',
      children: [{
        id: 'A1',
        icon: 'ios-list-outline',
        title: '日报',
        path: '/summary/'
      }, {
        id: 'A2',
        icon: 'ios-pricetag-outline',
        title: '半周报',
        path: '/summary/halfweek'
      }, /*{
        id: 'A3',
        icon: 'ios-pricetags-outline',
        title: '周报',
        path: '/summary/week'
      }, {
        id: 'A4',
        icon: 'clipboard',
        title: '月报',
        path: '/summary/month'
      }*/
      ]
    }, {
      id: 'B',
      title: '项目推进',
      icon: 'paper-airplane',
      path: '/project',
      children: [{
        id: 'B1',
        title: '推进表',
        icon: 'calendar',
        path: '/project/'
      }]
    }, {
      id: 'C',
      title: '上线计划',
      icon: 'clipboard',
      path: '/plan',
      children: [{
        id: 'C1',
        title: '本周',
        path: '/plan/'
      }]
    }, {
      id: 'D',
      title: '上线报告',
      icon: 'ios-keypad',
      path: '/report',
      children: [{
        id: 'D1',
        title: '本周',
        path: '/report/'
      }]
    }, {
      id: 'E',
      title: '上线追踪',
      icon: 'ios-eye',
      path: '/trace',
      children: [{
        id: 'E1',
        title: '本周',
        path: '/trace/'
      }]
    }
  ],
  _currentTopMenuPath: null,
  _currentSideMenuPath: null
}
const getters = {
  currentTopMenuPath: state => {
    if (!state._currentTopMenuPath) {
      // 指定默认值
      return state.menus[0].path
    }
    return state._currentTopMenuPath
  },
  currentTopMenu: (state, getters) => {
    return state.menus.filter(item => item.path === getters.currentTopMenuPath)[0]
  },
  currentSideMenuPath: (state, getters) => {
    if (!state._currentSideMenuPath) {
      // 指定默认值
      let topMenu = getters.currentTopMenu
      return topMenu.children[0].path
    }
    return state._currentSideMenuPath
  },
  currentSideMenu: (state, getters) => {
    let topMenu = getters.currentTopMenu
    return topMenu.children.filter(item => item.path === getters.currentSideMenuPath)[0]
  }
}
export default new Vuex.Store({
  strict: process.env.NODE_ENV !== 'production',
  state,
  getters,
  mutations,
  actions
})
