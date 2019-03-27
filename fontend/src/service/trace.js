/**
 * Created by JackChen on 2018/4/26.
 */
/**
 * 上线追踪接口
 */

import http from '@/service/instance'

export default {
  /**
   * 新增上线追踪表内容
   **/
  async saveOnlineTraceContent(centerId,centerName,projectWork,content,status,projectManager,remark,traceBillId) {
    let params={centerId,centerName,projectWork,content,status,projectManager,remark,traceBillId}
    let response = await http.post('saveOnlineTraceContent', params)
    return response
  },
  /**
   * 获取最新已发布的上线追踪推进表
   **/
  async getPublishedProject({centerId, isImportant}) {
    // 最新已发布项目上线追踪表内容
    let response = await http.post('getLastBillContent', {centerId, isImportant, pageSize: 1000})
    // 更新为已读
    http.post('updateBillForCheck', {billId: response.data.billId})
    return response.data
  },
  /**
   * 获取全部非草稿(发布\已阅)的上线追踪推进表
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
   * 获取上线追踪清单
   **/
  async getOnlineTraceBills({startTime,endTime,pageIndex, pageSize}) {
    let params = {
      startTime,
      endTime,
      pageIndex,
      pageSize,
      isWeek: 1,// 0-本周;1-历史
      flag:1
    }
    /*if (daterange && daterange.length > 0 && daterange[0] && daterange[1]) {
      params.startTime = daterange[0]
      params.endTime = daterange[1]
    }*/
    let response = await http.post('getOnlineTraceBills', params)
    return response.data
  },
  /**
   * 获取上线追踪详情
   **/
  async getOnlineTraceContentByBill({billId}) {
    let response = await http.post('getOnlineTraceContentByBill', {billId})
    return response.data
  },
  //通过内容ID获取上线追踪内容接口
  async getOnlineTraceContentByContentId({contentId}) {
    let response = await http.post('getOnlineTraceContentByContentId', {contentId})
    return response.data
  },
  /**
   * 发布上线追踪表
   **/
  async publishOnlineTraceBill({onlineBillId}) {
    let response = await http.post('publishOnlineTraceBill', {onlineBillId})
    return response
  },
  /**
   * 上线追踪表内容更新修改(modify)
   **/
  async updateOnlineTraceContent({Id, projectWork,content, status,centerId, centerName, projectanager, remark, traceBillId}) {
    let params={Id, projectWork,content, status,centerId, centerName, projectanager, remark, traceBillId}
    let response = await http.post('updateOnlineTraceContent',params )
    return response
  },
  /**
   * 删除上线追踪表内容
   **/
  async deleteOnlineTraceContent({contentId}) {
    let response = await http.post('deleteOnlineTraceContent', {contentIds: contentId})
    return response
  },
  /**
   * 批量导入上线追踪内容
   **/
  async imporOnlineTraceBatchRecharge({billId,file}) {
    let response = await http.post('imporOnlineTraceBatchRecharge', { billId, file})
    return response
  },
  /**
   * 上线追踪内容模板导出
   **/
  async exportOnlineTraceBatchResult() {
    let response = await http.post('exportOnlineTraceBatchResult', )
    return response
  }
}
