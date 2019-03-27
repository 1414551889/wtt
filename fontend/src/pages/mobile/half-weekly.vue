<template>
  <div>
    <div ref="menuWrapper" class="wrapper">
      <ul ref="menuUl" class="ui-menu" :style="{width: mWidth}">
        <li v-for="item in $store.state.centers" :key="item.id" :class="['m-item', {'m-item-active': currentMenu === item.id}]" @click="selectMenu($event, item)">
          {{item.name}}
        </li>
      </ul>
    </div>
    <br>
    <h3 class="mt10 mb10" style="text-align: center;">
      {{new Date().toLocaleDateString('zh-CN', {year: 'numeric', month: 'short', day: '2-digit'})}}--半周报
    </h3>
    <div v-if="loading" class="loading" style="text-align: center">
      加载中…
    </div>
    <div v-else-if="halfWeekList.length === 0" class="loading" style="text-align: center">
      暂无半周报记录
    </div>
    <Collapse v-else v-model="panelId" style="margin: 0 5px;" accordion>
      <h3 class="mt10 mb10" style="text-align: center;">{{title}}</h3>
      <Panel v-for="(item, index) in halfWeekList" :key="item.contentId" :name="String(item.contentId)">
        <p class="ellipsis" style="width: 94%; float: right;">{{item.projectName}} - {{item.workContent}}</p>
        <Icon v-if="item.isImportant === 1" type="ios-star" class="icon" style="color: gold; position: absolute; left:10px; top: 13px;"></Icon>
        <Icon v-if="item.isRisk === 1" type="alert-circled" class="icon" style="color: red; position: absolute; left:10px; top: 13px;"></Icon>
        <p slot="content" class="content">
          <b class="label">项目名称：</b>{{item.projectName}}
          <b class="label">工作内容：</b>
          {{item.workContent}}
          <b class="label">子项目：</b>
          {{item.subProject}}
        </p>
      </Panel>
    </Collapse>
    <!-- <div class="content">一、“和教育门户及客户端”：门户客户端运行平稳；四期门户客户端最新代码验收文档，已完成；正在研发需求门户2个，客户端3个；其他日常各工作要求进展顺利；
二、“和教育集成及基础平台”：运维集成方面: 维护6个平台300余台服务器环境稳定；质量方面：对和教育巡检，直播H5改版、历史数据统计功能、客户端改版、家校互动视频动态，学前管理信息平台等测试工作，执行用例510条，发现bug31个，关闭22个；
基础平台方面：省经理操作台湖北考勤系统数据库设计，数据中心省数据采集接口联调，客服系统问题内容模块开发、工单处理及优化，和宝贝点播计费联调，互动学生卡资源接口对接联调等；
三、“三通两平台”：广东：学校空间开发完成已提测，解决班级名片修改问题；设置学校管理员模块完成，IM功能客户端已提测；
福建：福建日常运营（需求收集，运营支撑，报表汇总，客服等）；和教育平台介入家校互动功能，接入子产品；和校园平台维护；和教育平台维护；
湖北：湖北三通两平台项目6台服务器，每天巡检2次，巡检指标(cpu、内存、磁盘、流量、swap)服务器日志检查，主要日志（服务器安全日志、系统日志、服务日志、用户登陆日志）；
青海：日常运营正常进行；
四、“和宝贝”：福建省常春藤幼儿园和宝贝部分ICT商务收费模式确认，厦门智童科技讨论“数字中国”展会支持；云南省文山州和宝贝客户经理培训，和宝贝使用和产品的商务合作模式及分成模式对接，全通教育作为硬件供应商及渠道sa的合作模式及测算沟通；湖北省公司和宝贝发展下文沟通，孝感市和宝贝代理商签约流程问题协调，随州市和宝贝代理商招募，荆门幼儿园试点落地跟踪；陕西省西安市2所幼儿园和宝贝产品回访、教育局幼教管理人员拜访；河北省和宝贝入园推介情况统计分析，省级和宝贝视频监控SA落地流程三方会议讨论，省级视频监控SA落地实施问题统计；
五、“河北教育云”:系统日常拨测和维护正常；运维周报1份；配合2家新应用联调；配合省公司商讨其他项目借用教育云部分服务器可行性；
六、“学前教育信息平台”：学前信息化平台3个模块继续联调，1个模块bug修复完成90%；一个模块接口开发；学前教育1个模块测试用例编写；1模块系统测试并编写测试报告提交验收；2个模块回归测试；
七、“和教育客服支撑”：共产生投诉4件，咨询236件，商机2件，总计241件，主要集中在两赛活动/当地和教育及校讯通/话费/流量等需归属省处理问题；
</div> -->
  </div>
</template>
<script>
import BScroll from 'better-scroll'
import api from '@/service'

export default {
  name: 'mobileHalfWeekly',
  title: '半周报',
  data() {
    return {
      loading: false,
      title: '',
      mWidth: '100%',
      currentMenu: 1,
      panelId: '',
      halfWeekList: []
    }
  },
  created() {
    this.getHalfWeekReportBill()
  },
  mounted() {
    this.$nextTick(function() {
      this.initMenu()
    })
  },
  methods: {
    initMenu() {
      let width = 0
      let menuItems = this.$refs.menuUl.children
      for (let i = 0; i < menuItems.length; i++) {
        width += menuItems[i].getBoundingClientRect().width
      }
      width = Math.max(window.innerWidth, width)
      this.mWidth = `${Math.ceil(width)}px`
      this.scroll = new BScroll(this.$refs.menuWrapper, {
        scrollX: true,
        click: true,
        refreshDelay: 20,
        freeScroll: false
      })
    },
    selectMenu(event, menu) {
      this.currentMenu = menu.id
      let currentMenuWidth = event.target.getBoundingClientRect().width
      let scrollWidth = (window.innerWidth - currentMenuWidth) / 2
      this.$nextTick(function() {
        this.scroll.scrollToElement('.m-item-active', 300, -scrollWidth)
      })
      // 获取数据
      this.getHalfWeekReportBill()
    },
    async getHalfWeekReportBill() {
      let params = {}
      let id = this.currentMenu
      if (id === -1) {
        params.isImportant = 1
      } else {
        params.centerId = id
      }
      this.loading = true
      this.halfWeekList = []
      try {
        let res = await api.summary.getHalfWeekReportBill(params)
        this.halfWeekList = res.list
        this.title = res.title
        if (this.halfWeekList.length > 0) {
          this.$nextTick(function() {
            this.panelId = String(this.halfWeekList[0].billId)
          })
        }
        console.log('是否知道'+params)
      } catch (e) {
        console.log(e)
        this.$Message.error("获取数据失败！")
      } finally {
        this.loading = false
      }
    }
  }
}
</script>
<style scoped>
.wrapper {
  width: 100%;
  height: 50px;
  overflow: hidden;
}
.ui-menu {
  height: 50px;
  line-height: 50px;
  background-color: #f8f8f8;
  position: relative;
  z-index: 999
}
.ui-menu::after {
  content: "";
  display: block;
  width: 100%;
  height: 1px;
  background: #dddee1;
  position: absolute;
  bottom: 0;
  left: 0;
}
.m-item {
  position: relative;
  float: left;
  height: inherit;
  line-height: inherit;
  padding: 0 15px;
  border-bottom: 2px solid transparent;
  color: #495060;
  font-size: 14px;
  z-index: 3;
}
.m-item-active {
  color: #2d8cf0;
  border-bottom: 2px solid #2d8cf0;
}
.content {
  padding: 0 12px;
  line-height: 24px;
  white-space: pre-line;
  word-break: break-word;
  text-align:justify;
  text-justify:inter-word;
}
</style>
