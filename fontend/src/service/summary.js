/**
 * Created by JackChen on 2018/4/23.
 */
/***
 * 工作总结接口
 ***/
import qs from 'qs'
import http from '@/service/instance'
export default {
  /**
   * 新增“工作总结”日报(Add)
   **/
  async saveDayReportContent({title, content, centerId, centerName}) {
    let params = {title, content , centerId, centerName}
    let response = await http.post('saveDayReportContent', params, {transformRequest(data, headers) {
      headers['Content-Type'] = 'application/json;charset=UTF-8';
      return JSON.stringify(data)
    }})
    if (response && response.code === 0) {
      return response
    } else {
      throw new Error("操作失败！")
      console.log("报错失败，返回结果", response)
    }
  },
/***
 * 查看日报详情
 ***/
  async getDayReportById({id}) {
    let response =await http.post('getDayReportById', {id})
    return response.data
  },
  /***
  * 更新日报详情
  ***/
  async updateReportContent({id,title,content,centerName,centerId}) {
    let params={id,title,content,centerName,centerId}
    let response = await http.post('updateReportContent', params, {transformRequest(data, headers) {
      headers['Content-Type'] = 'application/json;charset=UTF-8';
      return JSON.stringify(data)
    }})
    if (response && response.code === 0) {
      return response
    } else {
      throw new Error("操作失败！")
      console.log("更新失败，返回结果", response)
    }
    return response
  },
  /**
   * 删除日报
   **/
  async deleteReportInfo(ids) {
    console.log(http)
    let response = await http.post('deleteReportInfo', ids, {transformRequest(data, headers) {
      headers['Content-Type'] = 'application/json;charset=UTF-8';
      return JSON.stringify(data)
    }})
    if (response && response.code === 0) {
      return response
    } else {
      console.log("删除失败，返回结果", response)
      throw new Error("操作失败！")
    }
  },
  /**
   *  发布日报
   **/
  async publishDayReport(ids) {
    let res = await http.post('publishDayReport', ids, {transformRequest(data, headers) {
      headers['Content-Type'] = 'application/json;charset=UTF-8';
      return JSON.stringify(data)
    }})
    if (res && res.code !== 0) {
      throw new Error("操作失败！")
    }
    return res
  },
  /**
   * 查询"工作总结"中的日报清单
   **/
  async getDayReportList({pageIndex, pageSize}) {
    let params = {
      pageIndex,
      pageSize,
      isWeek: 0 // 0-本周;1-历史
    }
    let response = await http.post('getDayReportList', params)
    return response.data
  },

  /***
   * 半周报
   ****/
  /**
   * 查询"工作总结"中的半周报清单(halfweek)
   **/
  async getHalfWeekReportBill({startTime,endTime, pageIndex, pageSize}) {
    let params = {
      pageIndex,
      pageSize,
      isWeek: 1 // 0-本周;1-历史
    }
    let response = await http.post('getHalfWeekReportBill', params)
    return response.data
  },
  /**
   * 新增“工作总结”中的半周报(add)
   **/
  async saveHalfWeekContent(projectName, billId,subProject,workcontent , centerId, centerName) {
    let params = {projectName, billId,subProject,workcontent , centerId, centerName}
    params.pageSize = 1000
    let response = await http.post('saveHalfWeekContent', params)
    return response.data
  },
  /**
   * 发布半周报
   **/
  async publishHalfBill(ids) {
    let response = await http.post('publishHalfBill', ids, {transformRequest(data, headers) {
      headers['Content-Type'] = 'application/json;charset=UTF-8';
      return JSON.stringify(data)
    }})
    return response
  },
  /**
   * 更新半周报内容
   **/
  async updateHalfWeekContent({id}) {
    let response = await http.post('updateHalfWeekContent', {contentIds: id})
    return response
  },
  /**
   * 删除半周报内容
   **/
  async deleteHalfWeekContent(pram) {
    let response = await http.post('deleteHalfWeekContent', pram)
    return response
  },
/**
 * 导出文件
 **/
  async importHalfWeekContent(para) {
    let response = await http.post('imporBatchRecharge', para)
    return response
  },
  /**
   * 查询半周报内容详情(view)
   **/
  async getContentByHalfBill({billId}) {
    let response = await http.post('getContentByHalfBill',  {billId, pageSize: 10000})
    return response.data
  },
  /**
   * 保存半周报(add)
   **/
  async saveHalfWeekContent(projectName, id, subProject, workcontent ) {
    let params = {projectName, id, subProject, workcontent}
    params.pageSize = 1000
    let response = await http.post('saveHalfWeekContent', params)
    return response.data
  },
  /**
   * 修改半周报内容(modify)
   **/
  async getHalfWeekContentById({contentId}) {
    let response = await http.post('getHalfWeekContentById', {contentId})
    return response.data
  }
}
