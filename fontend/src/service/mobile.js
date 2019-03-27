// import http from '@/service/instance'

export default {
  /**
   * 根据时间获取推进清单详情
   */
  getProjectList(time) {
    return {
      id: '1',
      isImportant: true,
      title: '重点项目',
      project: '门户及客户端工程',
      content: '基于门户及客户端改版升级要求，实施门户、客户端、自研应用、应用对接等需求梳理、开发及上线。',
      done: '1、正式环境周四晚上线门户客户端改版、首页资源专题、cms应用管理、资源专题适配调整，bug修改10个。\n2、继续直播订购数据统计、客户端统一认证对接、互动学生卡资源服务研发工作。 ',
      zerenren: '李星、赵雷',
      peiheren: '张馨丹、刘兴国、杨立拴、高静梅',
      deadline: '2018年9月30日',
      isRisk: false,
      risk: '无',
      riskNum: 0,
      note: '无',
      totalNumber: 2
    }
  },
  getProjectList1(time) {
    return {
      id: '2',
      isImportant: false,
      title: '集团政企和教育项目',
      project: '总集成及运维服务',
      content: '基于门户及客户端改版升级要求，实施门户、客户端、自研应用、应用对接等需求梳理、开发及上线。',
      done: '1、正式环境周四晚上线门户客户端改版、首页资源专题、cms应用管理、资源专题适配调整，bug修改10个。\n2、继续直播订购数据统计、客户端统一认证对接、互动学生卡资源服务研发工作。 ',
      zerenren: '李星',
      peiheren: '张馨丹、刘兴国、高静梅',
      deadline: '2018年9月30日',
      isRisk: true,
      risk: '因某某原因，回款有风险',
      riskNum: 3,
      note: '无',
      totalNumber: 3
    }
  },
  getProjectList2(time) {
    return {
      id: '3',
      isImportant: false,
      title: '质量管理',
      project: '总集成及运维服务',
      content: '基于门户及客户端改版升级要求，实施门户、客户端、自研应用、应用对接等需求梳理、开发及上线。',
      done: '1、正式环境周四晚上线门户客户端改版、首页资源专题、cms应用管理、资源专题适配调整，bug修改10个。\n2、继续直播订购数据统计、客户端统一认证对接、互动学生卡资源服务研发工作。 ',
      zerenren: '赵雷',
      peiheren: '刘兴国、杨立拴、高静梅',
      deadline: '2018年9月30日',
      isRisk: false,
      risk: '无',
      riskNum: 0,
      note: '无',
      totalNumber: 1
    }
  }
}
