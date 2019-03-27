/**
 * 上线计划接口
 */

import http from '@/service/instance'

export default {
  /**
   * 新增上线计划表内容
   **/
  async saveOnlinePlanContent(pram) {
    let response = await http.post('saveOnlinePlanContent', pram)
    return response
  },
  /**
   * 获取最新已发布的上线计划推进表
   **/
  async getPublishedProject({centerId, isImportant}) {
    // 最新已发布项目上线计划表内容
    let response = await http.post('getLastBillContent', {centerId, isImportant, pageSize: 1000})
    // 更新为已读
    http.post('updateBillForCheck', {billId: response.data.billId})
    return response.data
  },
  // 更新为已读
  async updateBillForCheck({billId}) {
    let res = await http.post('updateBillForCheck', {billId})
    return res
  },
  /**
   * 获取全部非草稿(发布\已阅)的上线计划推进表
   **/
  async getAllPublishedProject({pageIndex, pageSize}) {
    let params = {
      pageIndex,
      pageSize,
      isWeek: 1, // 0-本周;1-历史
      isCheck: 5 // 发布\已阅
    }
    let response = await http.post('getBills', params)
    return response.data
  },
  /**
   * 获取上线计划清单
   **/
  async getOnlineBills({daterange, pageIndex, pageSize}) {
    let params = {
      pageIndex,
      pageSize,
      isWeek: 1,// 0-本周;1-历史
      flag:1
    }
    if (daterange && daterange.length > 0 && daterange[0] && daterange[1]) {
      params.startTime = daterange[0]
      params.endTime = daterange[1]
    }
    let response = await http.post('getOnlineBills', params)
    return response.data
  },
  /**
   * 获取上线计划详情
   **/
  async getOnlineContentByBill({billId}) {
    let response = await http.post('getOnlineContentByBill', {billId})
    return response.data
  },
  //获取具体内容
  async getOnlineContentByContentId({contentId}) {
    let response = await http.post('getOnlineContentByContentId', {contentId})
    return response.data
  },
  //exportOnlineBatchResult
  async exportOnlineBatchResult() {
    let response = await http.post('exportOnlineBatchResult')
    return response.data
  },
  //imporOnlineBatchRecharge
  async imporOnlineBatchRecharge({billId,file}) {
    let response = await http.post('imporOnlineBatchRecharge',{ billId, file})
    return response.data
  },
  /**
   * 发布上线计划表
   **/
  async publishOnlineBill({onlineBillId}) {
    let response = await http.post('publishOnlineBill', {onlineBillId})
    return response
  },
  /**
   * 上线计划表内容更新修改
   **/
  async updateOnlinePlanContent({projectNumber, type, content, centerId, centerName, testTime, productStaff, developStaff, testStaff, onlineTime, remark}) {
    let response = await http.post('updateOnlinePlanContent', {projectNumber, type, content, centerId, centerName, testTime, productStaff, developStaff, testStaff, onlineTime, remark})
    return response
  },
  /**
   * 删除上线计划表内容
   **/
  async deleteOnlineContent({contentId}) {
    let response = await http.post('deleteOnlineContent', {contentIds: contentId})
    return response
  },
  /**
   * 批量导入上线计划内容
   **/
  async getContent({contentId}) {
    let response = await http.post('getContentByContentId', {contentId})
    return response.data
  }
}
