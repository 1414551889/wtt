<template lang="html">
  <div>
    <div class="ui-toolbar">
    	<Button v-if="$store.state.user && $store.state.user.role === 1" type="primary" icon="android-add" @click="add">新增</Button>
      <span v-if="$store.state.user && $store.state.user.role === 1" style="margin-left:36px"></span>
      <template>
			    <i-input :value="centerId" v-if="$store.state.user && $store.state.user.role === 1" disabled placeholder="centerId" style="width: 160px"></i-input>
			     <Select v-model="model1" v-if="$store.state.user && $store.state.user.role !== 1" style="width:200px" placeholder="请选择中心" >
              <Option v-for="item in $store.state.centers"  :key="item.id" :value="item.id">{{item.name}}</Option>
            </Select>
			</template>
    	 <DatePicker type="daterange"  @on-change="dateChange" placement="bottom-end" placeholder="选择日期" style="width: 200px"></DatePicker>
      <Button type="primary" icon="ios-search" @click="search">搜索</Button>
      <div class="pull-right" style="width:196px;position:relative !important;z-index:222!important;">
           <Button type="info" icon="android-download" @click="downloadModel" v-if="$store.state.user && $store.state.user.role === 1">下载</Button>
            <Upload 
            	:show-upload-list="false"
            	 v-if="$store.state.user && $store.state.user.role === 1" 
            	 class="pull-right" 
            	  style="width:90px;position:relative;z-index:2222;" 
            	   ref="uploadContent"
			            :on-success="uploadSuccess"
			            :format="['xlsx','xls']"
			            name="file"
            	  action="/report/api/imporBatchRecharge"  
            	  accept="application/vnd.ms-excel"
            		>
              <Button type="info" class="upload" icon="ios-cloud-upload-outline" >导入</Button>
           </Upload>
           
       </div>
    </div>
     <Alert type="warning" show-icon style="height:46px;line-height:26px;">已选择{{selectionDataList.length}}项数据
	     	<div class="pull-right" >
	     			<Button type="primary" v-if="$store.state.user && $store.state.user.role === 2" @click="pubMsg" >发布</Button>
	     			<Button type="error"  v-if="$store.state.user && $store.state.user.role !== 2" @click="deleteMsg" >批量删除</Button>
	     	</div>
     </Alert>
    <Table border ref="selection"   :columns="columns4" :data="tableData" :loading="loading" @on-selection-change="tableSelect"></Table>
    <appPage :total="total" show-elevator  :pageIndex="pageIndex" :pageSize="pageSize" @pageChange="getDataList"></appPage>
  	 <!-- 确认删除对话框 -->
	  <Modal v-model="delModal" width="350" @on-ok="delOk">
	        <p style="text-align:center;padding-top:20px;font-size:15px">您确定要删除所选的项目推进信息吗？</p>
	  </Modal>
	   <!-- 确认发布对话框 -->
	  <Modal v-model="pubModal" width="350" @on-ok="pubOk">
	        <p style="text-align:center;padding-top:20px;font-size:15px">您确定要发布所选的项目推进信息吗？</p>
	  </Modal>
	   <!--预览框-->
    <Modal  v-model="modalPre" title="预览"  width="296">
    		<Form ref="form" style="height:366px;overflow-y:scroll;" :model="formData" :rules="formRule" :label-width="100">
	    		<div style="border-bottom:1px solid #eee;position:relative;width:100%;margin:0 auto;box-shadow:0 5px 15px #eee;border-radius:12px;">
	    				 <img src="~@/assets/tuijin.png" style="width:18%;float:right">
	    				
	    				<div style="padding:0 12px;width: 90%;line-height:26px;">
	    						<p class="" style=" font-size:18px;line-height:26px;">
					          {{formData.centerName}}--{{formData.projectName}} 
					        </p>
					       
					        <p style="color:#999;">
					        	<Icon type="star"></Icon>
					        	<span  v-if="aaa" style="color:#EE701E">{{formData.isFocused}}</span>
					        	<span  v-else>{{formData.isFocused}}</span>
					        	&nbsp;&nbsp;&nbsp;&nbsp;{{isImportant}}
					        </p>
	    						<p style="color:#999;line-height:26px;font-size:9px;">{{formData.updateTime}}</p>
	    				</div>
	    				<div style="width:100%;color:#666666;line-height:22px;padding:0 12px;height:46px;background:#ECF6FF;">
	    						<div style="color:#666666;width:100%;"><span style="color:#666666;width:48%;text-overflow:ellipsis;float:left;overflow:hidden;white-space:nowrap;">完成时限：{{formData.timeLimit}}</span><div style="float:right;color:#666666;width:48%;text-overflow:ellipsis;overflow:hidden;white-space:nowrap;">所属中心:<span style="color:#0084cf">{{formData.centerName}}</span></div> </div>
	        				<div style="color:#666666;width:100%;"> <span style="color:#666666;width:48%;text-overflow:ellipsis;float:left;overflow:hidden;white-space:nowrap;"> 责任人：{{formData.responsible}}</span><div style="float:right;color:#666666;width:48%;text-overflow:ellipsis;overflow:hidden;white-space:nowrap;display:inline-block"> 配合人：{{formData.cooperator}}</div></div>
	    				</div>
			        
	    		</div>
	        <div style="text-align:left;">
	        	<div style="width:100%">
	        		<div style="width:6%;margin-top:12px;vertical-align:top;display:inline-block">
	        				<img src="~@/assets/tuijin1.png" >
	        		</div>
	        		<div style="width:92%;display:inline-block;margin-top:12px;border:1px solid #eee;">
			          <p class="label" style="width:100%;font-weight:bold;padding:0 12px;height:22px;line-height:22px;background:#eee;">工作内容</p>
			          <div style="width:100%;padding:0 12px;">{{formData.workContent}}</div>
			        </div>
	        	</div>
	        	<div style="width:100%">
		        	<div style="width:6%;margin-top:12px;vertical-align:top;display:inline-block">
		        		<img src="~@/assets/tuijin1.png" >
		        	</div>
			        <div style="width:92%;display:inline-block;margin-top:12px;border:1px solid #eee;">
			          <p class="label" style="width:100%;font-weight:bold;padding:0 12px;height:22px;line-height:22px;background:#eee;">完成情况</p>
			          <div style="width:100%;padding:0 12px;" id="overState" ></div>
			        </div>
			      </div>
		        <div style="width:100%">
		        	<div style="width:6%;margin-top:12px;vertical-align:top;display:inline-block">
		        		<img src="~@/assets/tuijin1.png" >
		        	</div>
		        	<div style="width:92%;display:inline-block;margin-top:12px;border:1px solid #eee;">
			          <p class="label" style="width:100%;font-weight:bold;padding:0 12px;height:22px;line-height:22px;background:#eee;">开票回款风险</p>
			          <div style="width:100%;padding:0 12px;" :class="{redText: formData.riskSituation !== '无'}">{{formData.riskSituation}}</div>
			        </div>
		        </div>
		        <div style="width:100%">
		        	<div style="width:6%;margin-top:12px;vertical-align:top;display:inline-block">
		        		<img src="~@/assets/tuijin1.png" >
		        	</div>
		        	<div style="width:92%;display:inline-block;margin-top:12px;border:1px solid #eee;">
			          <p class="label" style="width:100%;font-weight:bold;padding:0 12px;height:22px;line-height:22px;background:#eee;">备注</p>
			          <div style="width:100%;padding:0 12px;" >{{formData.remark}}</div>
			        </div>
		        </div>
		      </div>
	      </Form>
	      <div slot="footer">
        </div>
    </Modal>
  </div>
</template>
<script>
import utils from '@/utils'
import api from '@/service'
import * as types from '@/store/mutation-types'
import $ from 'jquery'

const initTimeDate = function() {
  const date = new Date()
  date.setMonth(date.getMonth() - 1)
  return date
}
export default {
  data () {
    return {
    	pageCount :'',
    	aaa:false,
    	isImportant:'',
    	centerId:'',
    	tableHeight: 450,  
    	modalPre: false,
    	projectList: [],
      model1: '',
      loading: false,
      aaaaa: 143434,
      daterange: [],
      overState:'',
      options: {
        disabledDate (date) {
          return date && date.getTime() > Date.now();
        },
        date: initTimeDate()
      },
      formData: {
      	updateTime:'',
        centerId: null,
        isImportant: 0,
        projectName: '',
        workProject: '',
        workContent: '',
        overState: '',
        responsible: '',
        cooperator: '',
        timeLimit: '',
        isRisk: 0,
        riskSituation: '',
        remark: '无',
        leaders:'',
      },
      formRule: {
      	updateTime: {required: true},
        centerId: {required: true},
        isImportant: {required: true},
        projectName: {required: true, message: '请填写项目名称！', trigger: 'blur'},
        workProject: {required: true, message: '请填写工作项目！', trigger: 'blur'},
        workContent: {required: true, message: '请填写工作内容！', trigger: 'blur'},
        overState: {required: true, message: '请填写完成情况！', trigger: 'blur'},
        responsible: {required: true, message: '请填写责任人！', trigger: 'blur'},
        cooperator: {required: true, message: '请填写配合人！', trigger: 'blur'},
        timeLimit: {required: true, message: '请填写完成时限！', trigger: 'blur'},
        isRisk: {required: true},
        riskSituation: {required: true, message: '请填写项目风险！', trigger: 'blur'},
        leaders: {required: true, message: '@领导人为空！', trigger: 'blur'},
      },
      columns4: [
      	{
          type: 'selection',
          width: 60,
          align: 'center'
        },
      	{
      		title:"工作项目",
      		key:'workProject',
      		width:100
      	},
      	{
      		title:"项目名称",
      		key:'projectName',
      		width:110
      	},
      	{
      		title:"完成情况",
      		width:180,
      		className:'Newheight',
      		render:(h,params) => {
      			return h('span',{
      				domProps:{
      					innerHTML: params.row.overState
      				}
      			})
      		}
      	},
      	{
      		title:"责任人",
      		key:'responsible',
      		width:80
      	},
      	{
      		title:"开票回款风险",
      		key:'isRisk',
      		width:120,
      		align: 'center',
      		render: (h, params) => {
            let isRisk = params.row.isRisk
            let text = ''
            switch (isRisk) {
              case 0:
                return h('span', '无')
                break
              case 1:
                return h('span', '有')
                break
              default:
                text = ''
            }
          }
      	},
      	{
      		title:"发布时间",
      		key:'publishTime',
      		align:'center',
      	},
				{
          title: '状态',
          key: 'isPublish',
          width: 80,
          align: 'center',
          render: (h, params) => {
            let isPublish = params.row.isPublish
            let text = ''
            switch (isPublish) {
              case 0:
                return h('span', '草稿')
                break
              case 1:
               // text = '提交'
                return h('span', '发布')
                break
              default:
                text = ''
            }
          }
        }, {
          title: '操作',
          width: 130,
          align:'center',
          render: (h, params) => {
            let user = utils.localStorage.getUser()
            let btnView = h('Button', {
              props: {
                type: 'info',
                size: 'small'
              },
              style: {
                marginRight: '5px'
              },
              on: {
                click: () => {
                	this.contentId = params.row.contentId
                	this.initData()
                	this.modalPre = true
                	                	
                }
              }
            }, '查看')
            let btnEdit = h('Button', {
              props: {
                type: 'warning',
                size: 'small'
              },
              on: {
                click: () => {
                  this.$router.push({name: 'projectModify', query: {id: params.row.contentId}})//组件间通信
                }
              }
            }, '编辑')
            let btnTemplate = h('Button', {
              props: {
                type: 'warning',
                size: 'small'
              },
              on: {
                click: () => {
                  this.$router.push({name: 'projectTemplate',query: {id: params.row.contentId}})//组件间通信
                }
              }
            }, '模板')
            let btnPublish = h('Button', {
              props: {
                type: 'success',
                size: 'small'
              },
              on: {
                click: () => {
                  let config = {
                    title: '发布',
                    content: `确定要发布「 ${params.row.projectName} 」吗?`,
                    width: 350,
                    onOk: async function() {
                      try {
                        await api.project.publishContentsNew({contentId: params.row.contentId})
                        this.$Message.success("发布成功！")
                        params.row.isPublish = 1
                      } catch (e) {
                        console.log(e)
                        this.$Message.error("操作失败!")
                      }
                    }
                  }
                  this.$Modal.confirm(config)
                }
              }
            }, '发布')

						if (params.row.isPublish !== 1) {
	              if (user.role === 1) {
	                // 录入人
	                return h('div', [btnView, btnEdit])
	              } else if (user.role === 2) {
	                // 发布人
	                return h('div', [btnView, btnPublish])
	              }
            } else {
              	return h('div', [btnView, btnTemplate])
            }
          }
        }
      ],
      tableData: [],
      centerData: [],
      selectionDataList: [],  // 表格选中的对象
      total: 0,
      pageIndex: 1,
      pageSize: 10,
      delModal:false,
      pubModal:false,
      contentId:''
    }
  },
  created() {
    this.getDataList()
   	this.getAllCenterNew()
  },
  mounted() {
  	let user = utils.localStorage.getUser()
  	this.centerId = user.center
  },
  watch: {
  	'formData.isFocused':'setDefaultfocus',
  	'formData.isImportant':'setDefaultisImportant',
  },
  computed: {
    user: function() {
      return this.$store.state.user
    },
  },
  methods: {
  	downloadModel() {
      console.log("下载文件")
      window.location.href= "/csreport/api/exportBatchResult";
    },
    uploadSuccess (res, file) {
    	 console.log(res)
      if (res.code === 0) {
        this.$Message.success('导入成功')
        this.getDataList()
      } else {
        this.$Message.error(res.desc)
        this.$refs.uploadContent.clearFiles()
      }
    },
  	async initData() {
  		let contentId = this.contentId
      try {
        let res = await api.project.getContent({contentId})
        console.log(res)
        this.formData = res
        $('#overState').html(this.formData.overState)
        console.log(res)
      } catch (e) {
        console.log(e)
        this.$Message.error("获取初始数据失败！")
      }
    },
  	// 表格多选框事件
    tableSelect (selection) {
      	this.selectionDataList = selection
    },
    // 删除按钮
    deleteMsg () {
      if (this.selectionDataList.length < 1) {
      	this.$Message.info('请选择要删除的信息')
      } else {
        this.selectionDataList.forEach((item, index) => {
        	 this.delModal = true
        })
      }
    },
    //发布按钮
     pubMsg () {
      if (this.selectionDataList.length < 1) {
      	this.$Message.info('请选择要发布的信息')
	        return false
      } else {
        let flag = false
        let user = utils.localStorage.getUser()
        this.selectionDataList.forEach((item, index) => {
        	if (user.role !== 1) {
            flag = true
            this.pubModal = true
          }else{
          	this.$Message.info('您没有发布权限')
          }
        })
      }
    },
    async pubOk () {
    	let arr = []
      this.selectionDataList.forEach((item, index) => {
        arr.push(item.contentId)
      })
      let params = {
        contentId: arr.join(',') + ','
      }
      try {
      	let response = await api.project.ispublishContentsNew(params)
      	console.log(response.code)
      	if(response.code==0){
      		 try {
			      		await api.project.publishContentsNew(params)
			          this.$Message.success("发布成功！")
			          this.isPublish = 1
			          this.getDataList(1)
			        	this.selectionDataList = []
			      } catch (e) {
			          console.log('------------》e')
			          this.$Message.error("操作失败!")
			      }
      	}
      } catch(e) {
      	this.$Message.error('存在已发布！')
      }
    },
    // 确定删除
    async delOk () {
      let arr = []
      this.selectionDataList.forEach((item, index) => {
        arr.push(item.contentId)
      })
      let params = {
        contentId: arr.join(',') + ','
      }
      try {
        this.loading = true
        let response = await api.project.deletContentsManyNew(params)
        this.$Message.info('删除成功')
        	this.search()
        this.selectionDataList = []
      } catch (e) {
        this.$Message.error(e.message)
      } finally {
        this.loading = false
      }
    },

  	add() {
      this.$router.push({name: 'projectAdd', query: this.$route.query})
    },
    search() {
      this.getDataList(1)
    },
    //获取中心信息
    async getAllCenterNew() {
    	try {
        this.loading = true
        let response = await api.project.getAllCenterNew()
        this.projectList = response
      } catch (e) {
        this.$Message.info('获取失败')
      } finally {
        this.loading = false
      }
    },
    async getDataList(pageIndex) {
      if (pageIndex) {
        this.pageIndex = pageIndex
      }
      try {
        let params = {
          daterange: this.daterange,
          pageIndex: this.pageIndex,
          pageSize: this.pageSize,
          centerId: this.model1
        }
        this.loading = true
        let response = await api.project.getProjectNewList(params)
        this.tableData = response.list
        this.pageCount  = response.pageCount
        this.total = response.reultAllCount
        //截取字符串
       var b=this.tableData
        for( var c in b) {
        	var aa = b[c].workProject
        	var tempa = aa.substring(0,18);
        	if(b[c].workProject.length>18){
        		b[c].workProject = tempa+'...'
        	}
        }
        for(var c in b){
        	var a = 	b[c].overState
					var temp = a.substring(0,32);
					if(b[c].overState.length>32){
						b[c].overState=temp+'...';
					}
				}
      } catch (e) {
        this.$Message.error(e.message)
      } finally {
        this.loading = false
      }
    },
    getstr(str) {
      var newstr = $('#overStatea').html(str)
      return newstr
    },
    dateChange(v) {
      this.daterange = v
    },
    getList(selection) {
    	var a =  this.$refs.selection
    	this.selectionDataList = selection
    },
    setDefaultfocus() {
	   	if(this.formData.isFocused === 0) {
	   		this.formData.isFocused = '关注'
	   	}else if(this.formData.isFocused===1){
	   		this.formData.isFocused='已关注'
	   		this.aaa = true
	   	}
	   },
	  setDefaultisImportant() {
	   	if(this.formData.isImportant === 0) {
	   		this.isImportant = ' '
	   	}else if(this.formData.isImportant===1){
	   		this.isImportant = '重点'
	   	}
	   },
  }
}
</script>

<style lang="css" scoped>
	.label{
		width:100%;
		height:12px;
		background:#eee;
	}
	.Newheight{
		height:360px;
		background: red;
	}
	.redText{
		color:#EE701E
	}
	-webkit-scrollbar { width: 1px;}
</style>