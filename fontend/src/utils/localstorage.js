import AES from 'crypto-js/aes'
import utf8 from 'crypto-js/enc-utf8'
import * as CODE from '@/appcode'

const serializer = {
  /**
   * 序列化对象
   * @param {Object} item
   */
  serialize (item) {
    return item instanceof Object ? JSON.stringify(item) : item + ''
  },
  /**
   * 反序列化
   * @param {String} data
   */
  deserialize (data) {
    return data && JSON.parse(data)
  }
}

function _setItem (key, value) {
  let str = serializer.serialize(value)
  str = AES.encrypt(str, CODE.AES_KEY).toString()
  window.localStorage.setItem(key, str)
}

function _getItem (key) {
  let str = window.localStorage.getItem(key)
  if (str) {
    str = AES.decrypt(str, CODE.AES_KEY).toString(utf8)
  }
  return str
}

function getJson (dddd) {
  return serializer.serialize(dddd);
}

/**
 * 保存jwt到localStorage
 * @param {String} token
 */
export function saveToken (token) {
  _setItem(CODE.TOKEN_TIME, Date.now())
  return _setItem(CODE.JWT_TOKEN, token)
}

/**
 * 取回jwt
 * @return {String} token
 */
export function getToken () {
  let token = _getItem(CODE.JWT_TOKEN)
  // let time = _getItem(CODE.TOKEN_TIME) - 0
  // if (Date.now() - time > 24 * 60 * 60 * 1000) {
  //   return null
  // }
  return token
}

export function saveUser (user) {
  return _setItem(CODE.USER, user)
}

export function getUser () {
  let user = _getItem(CODE.USER)
  if (user) {
    user = serializer.deserialize(user)
  }
  return user
}

export function setRedirectUrl(url) {
  return _setItem(CODE.REDIRECT_URL, url)
}

export function getRedirectUrl(url) {
  return _getItem(CODE.REDIRECT_URL)
}

// 清空本地缓存
export function clear() {
  window.localStorage.clear()
}
