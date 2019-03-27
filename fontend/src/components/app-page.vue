<template>
  <Page
    class="page"
    :total="tSize"
    :current="pIndex"
    :page-size="pSize"
    @on-change="changePage"
    show-total
    show-elevator>
  </Page>
</template>

<script>
export default {
  name: 'app-page',
  props: {
    total: {
      type: Number,
      default: 0
    },
    pageIndex: {
      type: Number,
      default: 1
    },
    pageSize: {
      type: Number,
      default: 10
    }
  },
  computed: {
    tSize() {
      return this.total ? this.total : 0
    },
    pIndex() {
      let index = this.pageIndex ? this.pageIndex : 1
      if (index > this.total / this.pageSize) {
        index = Math.floor(this.total / this.pageSize)
      }
    },
    pSize() {
      return this.pageSize ? this.pageSize : 10
    }
  },
  methods: {
    // 分页数据
    changePage (page) {
      // 页码改变触发父组件的请求当页数据方法
      this.$emit('pageChange', page)
    }
  }
}
</script>

<style scoped>
  .page{
    margin-top: 30px;
    text-align: center;
  }
</style>
