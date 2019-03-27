/**
 * 项目推进接口
 */

import http from '@/service/instance'

export default {
  /**
   * 新增项目推进表
   */
  async saveProject({title, subTime, centerId, centerName}) {
    let params = {title, subTime, centerId, centerName}
    let response = await http.post('saveBill', params)
    return response
  },
  /**
   * 删除项目推荐表
   */
  async deleteProject({billIds}) {
    let res = await http.post('deleteBill', {billIds})
    return res
  },
  async deletContentsManyNew({contentId}) {
    let res = await http.post('deletContentsManyNew', {contentId})
    return res
  },
  //批量发布
   async publishContentsNew({contentId}) {
    let res = await http.post('publishContentsNew', {contentId})
    return res
  },
  //是否已经发布
    async ispublishContentsNew({contentId}) {
    let res = await http.post('ispublishContentsNew', {contentId})
    return res
  },
  /**
   * 获取最新已发布的项目推进表
   */
  async getPublishedProject({centerId, isImportant}) {
    // 最新已发布项目推进表内容
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
   * 获取全部非草稿(发布\已阅)的项目推进表
   */
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
   * 查询项目推进表清单
   */
  async getProjectList({daterange, pageIndex, pageSize}) {
    let params = {
      pageIndex,
      pageSize,
      isWeek: 1 // 0-本周;1-历史
    }
    if (daterange && daterange.length > 0 && daterange[0] && daterange[1]) {
      params.startTime = daterange[0]
      params.endTime = daterange[1]
    }
    let response = await http.post('getBills', params)
    return response.data
  },
  /**
   * 查询项目推进表清单新
   */
  async getProjectNewList({centerId, daterange, pageIndex, pageSize}) {
    let params = {
      pageIndex,
      pageSize,
      centerId,
      isWeek: 1 // 0-本周;1-历史
    }
    if (daterange && daterange.length > 0 && daterange[0] && daterange[1]) {
      params.startTime = daterange[0]
      params.endTime = daterange[1]
    }
    let response = await http.post('getContentsNew', params)
    return response.data
  },
  //获取列表中心的接口
  async getAllCenterNew() {
    let response = await http.post('getAllCenterNew')
    return response.data
  },
  //获取首页列表接口
  async getContentsNew() {
    let response = await http.post('getContentsNew')
    return response.data
  },
  /**
   * 查询项目推进表详情
   */
  async getProjectContent(params) {
    params.pageSize = 1000
    let response = await http.post('getContentByBill', params)
    return response.data
  },
  /**
   * 发布推进表
   */
  async publishBill({billId}) {
    let response = await http.post('publishBill', {billId})
    return response
  },
  /**
   * 新增项目推进表内容
   */
  async saveContent({template,selecttime, billId, picture, contentId, centerId, centerName, isImportant, projectName, workProject, workContent, overState, responsible, cooperator, timeLimit, isRisk, riskSituation, remark, updater, leaders}) {
    let response = await http.post('saveContent', {template,selecttime, picture, billId, contentId, centerId, centerName, isImportant, projectName, workProject, workContent, overState, responsible, cooperator, timeLimit, isRisk, riskSituation, remark, updater, leaders})
    return response
  },
  async saveContentTemplate({selecttime, billId, picture, centerId, centerName, isImportant, projectName, workProject, workContent, overState, responsible, cooperator, timeLimit, isRisk, riskSituation, remark, updater, leaders}) {
    let response = await http.post('saveContentTemplate', {selecttime, picture, billId, centerId, centerName, isImportant, projectName, workProject, workContent, overState, responsible, cooperator, timeLimit, isRisk, riskSituation, remark, updater, leaders})
    return response
  },
  /**
   * 删除项目推进表内容
   */
  async deleteContent({contentId}) {
    let response = await http.post('deleteContent', {contentIds: contentId})
    return response
  },
  /**
   * 查询项目推进表内容详情
   */
  async getContent({contentId}) {
    let response = await http.post('getContentByContentId', {contentId})
    return response.data
  },
   /**
   * 下载推进表内容详情
   */
  async imporBatchRecharge() {
  	let response = await http.post('imporBatchRecharge')
  	return response
  },
   /**
   * 上传图片项目推进表内容详情
   */
  async pictureUpLoad() {
  	let response = await http.post('pictureUpLoad')
  	return response
  }
}
