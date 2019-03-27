'use strict'
const merge = require('webpack-merge')
const prodEnv = require('./prod.env')

module.exports = merge(prodEnv, {
  assetsPublicPath: '/',
  NODE_ENV: '"development"',
  ROUTER_BASE: '"/"',
  AXIOS_BASE_URL: '"/report/api/"'
})
