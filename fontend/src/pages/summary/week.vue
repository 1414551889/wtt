<template lang="html">
  <div>
    <!--<div class="ui-toolbar">
      <span>时间段：</span>
      <DatePicker :options="options" @on-change="dateChange" type="daterange" placement="bottom-start" placeholder="选择日期" style="width: 200px"></DatePicker>
      <Button type="primary" icon="ios-search" @click="search">搜索</Button>
    </div>-->
    <Button v-if="$store.state.user && $store.state.user.role === 1" type="primary" icon="android-add" @click="add">新增</Button>
    <Table stripe :columns="columns" :data="tableData" :loading="loading"></Table>
    <!--<appPage :total="total" :pageIndex="pageIndex" :pageSize="pageSize" @pageChange="getDataList"></appPage>-->
  </div>
</template>

<script>
  import utils from '@/utils'
  import api from '@/service'
  const initTimeDate = function() {
    const date = new Date()
    date.setMonth(date.getMonth() - 1)
    return date
  }
  export default {
    data () {
      return {
        loading: false,
        daterange: [],
        options: {
          disabledDate (date) {
            return date && date.getTime() > Date.now();
          },
          date: initTimeDate()
        },
        columns: [
          {
            title: '周报',
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
                    this.$router.push({name: 'projectView', query: {id: params.row.billId}})
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
                    this.$router.push({name: 'projectEdit', query: {id: params.row.billId}})
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
                          await api.project.publishBill({billId: params.row.billId})
                          this.$Message.success("发布成功！")
                          params.row.isCheck = 2
                        } catch (e) {
                          console.log(e)
                          this.$Message.error("操作失败!")
                        }
                      }
                    }
                    this.$Modal.confirm(config)
                    // this.$router.push({name: 'projectEdit', query: {id: params.row.billId}})
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
      this.getDataList()
    },
    computed: {
      user: function() {
        return this.$store.state.user
      }
    },
    methods: {
      add() {
        this.$router.push({name: 'summaryAdd', query: this.$route.query})
      }
    }
  }
</script>

<style lang="css" scoped>
  div>button{
    margin-bottom:5px;
  }
</style>
