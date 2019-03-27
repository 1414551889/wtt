import * as types from '@/store/mutation-types'
import api from '@/service'

export default {
  async login ({commit}, {user, password, validateCode}) {
    try {
      let response = await api.user.login(user, password, validateCode)
      commit(types.SET_USER, {user: response.user, token: response.token})
      return response
    } catch (e) {
      return -1
    }
  }
}
