import http from '@/service/instance'

export default {
  async getKaptchaImage() {
    let response = await http.get(`getKaptchaImage?r=${Math.random()}`)
    return response
  },
  async login(user, password, validateCode) {
    let response = await http.post('login', {
      account: user,
      password,
      validateCode
    })
    return {
      token: response.token,
      user: response.data
    }
  },
  async getUserByToken(token) {
    let response = await http.post('getUserByToken')
    return {
      user: response.data
    }
  },
  async getUserByTokenNew(jwtToken) {
    let response = await http.get('getUserByTokenNew')
    return {
      user: response.data
    }
  },
  async changePassWord({oldPass, newPass}) {
    let response = await http.post('updatePassWord', {oldPass, newPass})
    return response
  },
  async abb({oldPass, newPass}) {
    let response = await http.post('updatePassWord', {oldPass, newPass})
    return response
  }

}
