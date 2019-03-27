<template lang="html">
  <div>
      <div class="ui-toolbar">
        <Button type="default" icon="ios-arrow-back" @click="$router.go(-1)">返回</Button>
      </div>
      <Card dis-hover>
          <p slot="title">{{DayReportList.title}}</p>
          <p slot="extra">{{DayReportList.subTime | timeFormat}}</p>
          <p class="content">{{DayReportList.content}}</p>
      </Card>
  </div>
</template>
<script>
import api from '@/service'

export default {
 name: "summary_daily_view",
  data () {
    return {
      DayReportList: {},
      loading: true
    }
  },
  created() {
    this.getDayReportById()
  },
  filters: {
    timeFormat(value) {
      if (!value) {
        return ''
      }
      return value.substring(0,10)
    }
  },
  methods: {
    async getDayReportById() {
      let id = this.$route.query.id
      try {
        this.loading = true
        let res = await api.summary.getDayReportById({id})
        this.DayReportList = res
      }catch (e) {
        console.log(e)
        this.$Message.error(e.message)
      } finally {
        this.loading = false
      }
    }
  }
}
</script>

<style lang="css" scoped>
.content {
  white-space: pre-line;
  word-break: break-word;
  text-align: justify;
}
</style>
