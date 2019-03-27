const serve = require('koa-static')
const history = require('koa2-connect-history-api-fallback')
const Koa = require('koa')
const app = new Koa()

app.use(history())
app.use(serve('publics'))

app.listen(3000)
