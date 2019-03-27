import * as types from './mutation-types'
import utils from '@/utils'

export default {
  [types.SET_USER] (state, payload) {
    state.user = payload.user
    if (payload.token) {
      utils.localStorage.saveToken(payload.token)
    }
    utils.localStorage.saveUser(payload.user)
  },
  [types.SET_TOP_MENU_PATH] (state, payload) {
    state._currentTopMenuPath = payload.path
    // 更新topMenu时需要清空此项
    state._currentSideMenuPath = null
  },
  [types.SET_SIDE_MENU_PATH] (state, payload) {
    state._currentSideMenuPath = payload.path
  }
}
