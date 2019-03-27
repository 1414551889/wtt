<template lang="html">
  <div>
    <Scroll :on-reach-bottom="handleReachBottom" :height="scrollContentHeight">
      <Table stripe :columns="columns" :data="tableData" :show-header="false" :loading="loading"></Table>
    </Scroll>
  </div>
</template>

<script>
import api from '@/service'
import utils from '@/utils'

export default {
  title: '项目推进记录',
  data() {
    return {
      pageIndex: 1,
      pageSize: 15,
      reultAllCount: null,
      scrollContentHeight: 300,
      loading: false,
      columns: [
        {
          title: '标题',
          key: 'title',
          render: (h, params) => {
            let user = utils.localStorage.getUser()
            let checkerId = params.row.checkerId
            let checkerIds = []
            if (checkerId) {
              checkerIds = checkerId.split(',')
            }
            if (checkerIds.indexOf(String(user.userId)) !== -1) {
              return params.row.title
            } else {
              return h('strong', {
                style: {
                  fontWeight: 'bolder'
                }
              }, params.row.title)
            }
          }
        }, {
          title: '状态',
          width: 60,
          align: 'center',
          render: (h, params) => {
            let user = utils.localStorage.getUser()
            let checkerId = params.row.checkerId
            let checkerIds = []
            if (checkerId) {
              checkerIds = checkerId.split(',')
            }
            if (checkerIds.indexOf(String(user.userId)) !== -1) {
              return '已阅'
            } else {
              return '未阅'
            }
          }
        }, {
          title: '操作',
          align: 'center',
          width: 100,
          render: (h, params) => {
            return h('div', [
              h('Button', {
                props: {
                  type: 'info',
                  size: 'small'
                },
                style: {
                  marginRight: '5px'
                },
                on: {
                  click: () => {
                    this.$router.push({name: 'historyView', query: {id: params.row.billId}})
                  }
                }
              }, '查看')
            ])
          }
        }
      ],
      tableData: []
    }
  },
  created() {
    this.initScrollContentHeight()
    this.getBills()
  },
  methods: {
    initScrollContentHeight() {
      this.scrollContentHeight = window.innerHeight - 50
    },
    async getBills() {
      let params = {
        pageIndex: this.pageIndex,
        pageSize: this.pageSize
      }
      try {
        this.loading = true
        let res = await api.project.getAllPublishedProject(params)
        this.tableData = res.list
        this.reultAllCount = res.reultAllCount
      } catch (e) {

      } finally {
        this.loading = false
      }
    },
    handleReachBottom () {
      if (this.reultAllCount && this.reultAllCount / this.pageSize <= this.pageIndex) {
        console.log("已经是最后一页")
        return
      }
      return new Promise(async resolve => {
        let params = {
          pageIndex: this.pageIndex + 1,
          pageSize: this.pageSize
        }
        try {
          let res = await api.project.getAllPublishedProject(params)
          this.tableData = this.tableData.concat(res.list)
          this.pageIndex = res.pageIndex
        } catch (e) {

        } finally {
          resolve()
        }
      })
    }
  }
}
</script>

<style lang="css" scoped>
</style>
