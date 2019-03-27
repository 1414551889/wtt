<template lang="html">
  <div>
    <Table stripe :columns="columns" :data="tableData" :loading="loading"></Table>
    <appPage :total="total" :pageIndex="pageIndex" :pageSize="pageSize" @pageChange="getHalfWeekReportBill"></appPage>
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
        options: {
          disabledDate (date) {
            return date && date.getTime() > Date.now();
          },
          date: initTimeDate()
        },
        columns: [
          {
            title: '半周报',
            key: 'title'
          }, {
            title: '提交时间',
            key: 'subTime',
            width: 300,
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
            // align: 'center',
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
                    this.$router.push({name: 'summaryView', query: {id: params.row.id}})
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
                    this.$router.push({name: 'summaryEdit', query: {id: params.row.id}})
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
                          await api.summary.publishHalfBill([params.row.id])
                          this.$Message.success("发布成功！")
                          params.row.isCheck = 2
                        } catch (e) {
                          console.log(e)
                          this.$Message.error(e.message)
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
      this.getHalfWeekReportBill()
    },
    computed: {
      user: function() {
        return this.$store.state.user
      }
    },
    methods: {
      search() {
        this.getHalfWeekReportBill(1)
      },
      async getHalfWeekReportBill(pageIndex) {
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
          let response = await api.summary.getHalfWeekReportBill(params)
          console.log(response)
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
