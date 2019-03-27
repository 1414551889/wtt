<template lang="html">
  <div>
    <div class="ui-toolbar">
      <Button v-if="$store.state.user && $store.state.user.role === 1" type="primary" icon="android-add" @click="add">新增</Button>
    </div>
    <Table stripe :columns="columns" :data="tableData" :loading="loading"></Table>
    <appPage :total="total" :pageIndex="pageIndex" :pageSize="pageSize" @pageChange="getDayReportList"></appPage>
  </div>
</template>
<script>
  import utils from '@/utils'
  import api from '@/service'
  import axios from 'axios'
  export default {
    data () {
      return {
        loading: false,
        columns: [
          {
            title: '日报',
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
            width: 200,
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
                    this.$router.push({name: 'summaryDailyView', query: {id: params.row.id}})
                  }
                }
              }, '查看')
              let btnEdit = h('Button', {
                props: {
                  type: 'warning',
                  size: 'small'
                },
                style: {
                  marginRight: '5px'
                },
                on: {
                  click: () => {
                    this.$router.push({name: 'summaryDailyModify', query: {id: params.row.id}})
                  }
                }
              }, '编辑')
              let btnDel = h('Button', {
                props: {
                  type: 'error',
                  size: 'small'
                },
                style: {
                  marginRight: '5px'
                },
                on: {
                  click: () => {
                    let config = {
                      title: '删除',
                      content: `确定要删除「 ${params.row.title} 」吗?`,
                      width: 350,
                      onOk: async () => {
                        try {
                          let res = await api.summary.deleteReportInfo([params.row.id])
                          this.$Message.success("删除成功！")
                          this.tableData = this.tableData.filter(item => item.id !== params.row.id)
                        } catch (e) {
                          this.$Message.error(e.message)
                          console.log("删除请求异常", e)
                        }
                      }
                    }
                    this.$Modal.confirm(config)
                  }
                }
              }, '删除')
              let btnPublish = h('Button', {
                props: {
                  type: 'success',
                  size: 'small'
                },
                style: {
                  marginRight: '5px'
                },
                on: {
                  click: () => {
                    let config = {
                      title: '发布',
                      content: `确定要发布「 ${params.row.title} 」吗?`,
                      width: 350,
                      onOk: async function() {
                        try {
                          let res = await api.summary.publishDayReport([params.row.id])
                          this.$Message.success("发布成功！")
                          params.row.isCheck = 2
                        } catch (e) {
                          this.$Message.error("操作失败！")
                          console.log("删除请求异常", e)
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
                  return h('div', [btnView, btnEdit,btnDel])
                } else if (user.role === 2) {
                  // 发布人
                  return h('div', [btnView, btnPublish,btnDel])
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
      this.getDayReportList()
    },
    computed: {
      user: function() {
        return this.$store.state.user
      }
    },
    methods: {
      add() {
        this.$router.push({name: 'summaryDailyAdd', query: this.$route.query})
      },
      // 查询"工作总结"中的日报清单
      async getDayReportList(pageIndex) {
        if (pageIndex) {
          this.pageIndex = pageIndex
        }
        try {
          let params = {
            pageIndex: this.pageIndex,
            pageSize: this.pageSize
          }
          this.loading = true
          let response = await api.summary.getDayReportList(params)
          console.log("查询工作总结中的日报清单返回结果", response)
          this.tableData = response.list
          this.total = response.reultAllCount
        } catch (e) {
          this.$Message.error(e.message)
        } finally {
          this.loading = false
        }
      }
    }
  }
</script>
