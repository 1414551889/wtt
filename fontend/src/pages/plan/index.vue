<template lang="html">
  <div>
    <div class="ui-toolbar">
      <span>时间段：</span>
      <DatePicker :options="options" @on-change="dateChange" type="daterange" placement="bottom-start" placeholder="选择日期" style="width: 200px"></DatePicker>
      <Button type="primary" icon="ios-search" @click="search">搜索</Button>
    </div>
    <Table stripe :columns="columns" :data="tableData" :loading="loading"></Table>
    <appPage :total="total" :pageIndex="pageIndex" :pageSize="pageSize" @pageChange="getOnlineBills"></appPage>
  </div>
</template>

<script>
import utils from '@/utils'
import api from '@/service'
import axios from 'axios'
const initTimeDate = function() {
  const date = new Date()
  date.setMonth(date.getMonth() - 1)
  return date
}
export default {
  data () {
    return {
      loading: false,
      dateRange: [],
      options: {
        disabledDate (date) {
          return date && date.getTime() > Date.now();
        },
        date: initTimeDate()
      },
      columns: [
        {
          title: '标题',
          key: 'title'
        }, {
          title: '截止时间',
          key: 'subTime',
          width: 200,
          align: 'center',
          render: (h, params) => {
            let time = params.row.subTime
            if (time) {
              return time.substring(0, 10)
            }
          }
        }, {
          title: '状态',
          key: 'isCheck',
          width: 200,
          align: 'center',
          render: (h, params) => {
            let isCheck = params.row.isCheck
            let text = ''
            switch (isCheck) {
              case 0:
                text = '草稿'
                break
              case 1:
                text = '提交'
                break
              case 2:
                text = '发布'
                break
              case 3:
                text = '已阅'
                break
              default:
                text = ''
            }
            return text
          }
        }, {
          title: '操作',
          width: 140,
          render: (h, params) => {
            let user = utils.localStorage.getUser()
            let btnView = h('Button', {
              props: {
                type: 'info',
                size: 'small'
              },
              style: {
                marginRight: '5px'
              },
              on: {
                click: () => {
                  //  此处查看--------------name = planView------------
                  this.$router.push({name: 'planView', query: {id: params.row.id}})
                }
              }
            }, '查看')
            let btnEdit = h('Button', {
              props: {
                type: 'warning',
                size: 'small'
              },
              on: {
                click: () => {
                  //  查看name为planEdit的页面----------------- name = planEdit
                  console.log(params.row.id);
                  this.$router.push({name: 'planEdit', query: {id: params.row.id}})
                }
              }
            }, '编辑')
            let btnPublish = h('Button', {
              props: {
                type: 'success',
                size: 'small'
              },
              on: {
                click: () => {
                  let config = {
                    title: '发布',
                    content: `确定要发布「 ${params.row.title} 」吗?`,
                    width: 350,
                    onOk: async function() {
                       try {
                        await api.plan.publishOnlineBill({billId: params.row.onlineBillId})
                        this.$Message.success("发布成功！")
                        params.row.isCheck = 2
                      } catch (e) {
                        this.$Message.error("操作失败!")
                      }
                    }
                  }
                  this.$Modal.confirm(config)
                }
              }
            }, '发布')

            if (params.row.isCheck === 0) {
              if (user.role === 1) {
                // 录入人
                return h('div', [btnView, btnEdit])
              } else if (user.role === 2) {
                // 发布人
                return h('div', [btnView, btnPublish])
              }
            } else {
              return h('div', [btnView])
            }
          }
        }
      ],
      tableData: [],
      total: 0,
      pageIndex: 1,
      pageSize: 10
    }
  },
  created() {
    this.getOnlineBills()
  },
  computed: {
    user: function() {
      return this.$store.state.user
    }
  },
  methods: {
    search() {
      this.getOnlineBills(1)
    },
    async getOnlineBills(pageIndex) {
      if (pageIndex) {
        this.pageIndex = pageIndex
      }
      try {
        let params = {
          daterange: this.daterange,
          pageIndex: this.pageIndex,
          pageSize: this.pageSize
        }
        this.loading = true
        // 此处有错误000---------------
        let response = await api.plan.getOnlineBills(params)
        this.tableData = response.list
        this.total = response.reultAllCount
      } catch (e) {
        this.$Message.error(e.message)
      } finally {
        this.loading = false
      }
    },
    dateChange(v) {
      this.daterange = v
    }
  }
}
</script>

<style lang="css" scoped>
</style>
