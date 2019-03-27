import axios from 'axios'
import qs from 'qs'
import utils from '@/utils'

const instance = axios.create({
  baseURL: process.env.AXIOS_BASE_URL,
  timeout: 35000,
  transformRequest: [data => qs.stringify(data, { arrayFormat: 'repeat' })]
})

// Add a request interceptor
instance.interceptors.request.use(function (config) {
  // Do something before request is sent
  let token = utils.localStorage.getToken()
  config.headers.common["jwtToken"] = token
  return config
}, function (error) {
  // Do something with request error
  return Promise.reject(error)
})

// Add a response interceptor
instance.interceptors.response.use(function (response) {
  // 结束进度条
  // Do something with response data
  if (response.data && response.data.code === 0) {
    return response.data
  } else {
    console.error('axios全局异常拦截，响应code为%d，接口为%s', response.data.code, response.config.url)
    let msg = "网络错误,请稍后再试!"
    if (response.data && response.data.desc) {
      msg = response.data.desc
    }
    return Promise.reject(new Error(msg))
  }
}, function (error) {
  // 结束进度条
  // Do something with response error
  console.error('axios全局异常拦截，接口请求失败', error)
  return Promise.reject(new Error("服务器异常，请稍后再试！"))
})

export default instance
