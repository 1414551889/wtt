<template lang="html">
  <div>

  </div>
</template>
<script>
import api from '@/service'

export default {
  data () {

  },
  created() {

  },
  watch: {

  },
  /*methods: {
    async getProjectContent() {
      let billId = this.$route.query.id
      try {
        this.loading = true
        let res = await api.project.getProjectContent({billId})
        this.title = res.title
        this.projectList = res.data.list
      } catch (e) {
        this.$Message.error("获取数据失败！")
      } finally {
        this.loading = false
      }
    },
    add() {
      this.$router.push({name: 'projectAdd', query: this.$route.query})
    },
    modify(id) {
      this.$router.push({name: 'projectModify', query: {id}})
    },
    async publishBill() {
      let billId = this.$route.query.id
      try {
        let res = await api.project.publishBill({billId})
        console.log(res)
        this.$Message.success("发布成功！")
        this.$router.go(-1)
      } catch (e) {
        console.log(e)
        this.$Message.error("操作失败!")
      }
    },
    async del(id) {
      try {
        await api.project.deleteContent({contentId: id})
        this.projectList = this.projectList.filter(item => item.contentId !== id)
      } catch (e) {
        console.log(e)
        this.$Message.error("操作失败！")
      }
    },
    openDefaultPanel() {
      if (this.projectList.length === 0) {
        return
      }
      this.$nextTick(function() {
        this.panelId = String(this.projectList[0].contentId)
      })
    }
  }*/
}
</script>

<style lang="css" scoped>

</style>
