'use strict'
const merge = require('webpack-merge')
const prodEnv = require('./prod.env')

module.exports = merge(prodEnv, {
  NODE_ENV: '"development"',
  // SERVER_BASE_URL: '"http://localhost:9090"'
  SERVER_BASE_URL: '""'
})
