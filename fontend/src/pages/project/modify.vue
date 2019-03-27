<template lang="html">
  <div>
    <div class="ui-toolbar">
      <Button type="default" icon="ios-arrow-back" @click="$router.go(-1)">返回</Button>
      <Button type="primary" icon="folder" @click="submit">保存</Button>
      <Button type="primary" style="width:80px" @click="preview">预览</Button>
    </div>
    <Row>
      <Col span="12" offset="6">
        <Form ref="form" :model="formData" :rules="formRule" :label-width="100">
       		<FormItem label="日期：" prop="selecttime">
              <DatePicker    type="date"   :value="formData.selecttime" @on-change="handleChange"  placement="bottom-start" placeholder="选择日期" style="width: 200px"></DatePicker>
          </FormItem>
          <FormItem label="所在中心：" prop="centerId">
            <Select v-model="formData.centerId" placeholder="请选择中心" disabled>
              <Option v-for="item in $store.state.centers" :key="item.id" :value="item.id">{{item.name}}</Option>
            </Select>
          </FormItem>
          <FormItem label="重点项目：" prop="isImportant">
            <RadioGroup v-model="formData.isImportant">
              <Radio :label="1">是</Radio>
              <Radio :label="0">否</Radio>
            </RadioGroup>
          </FormItem>
          <FormItem label="项目名称：" prop="projectName">
            <Input v-model="formData.projectName" placeholder="请输入项目名称"></Input>
          </FormItem>
          <FormItem label="工作项目：" prop="workProject">
            <Input v-model="formData.workProject" placeholder="请输入工作项目"></Input>
          </FormItem>
          <FormItem label="工作内容：" prop="workContent">
            <Input v-model="formData.workContent" type="textarea" placeholder="请输入工作内容" :autosize="{minRows: 2}"></Input>
          </FormItem>
          <FormItem label="完成情况：" prop="overState">
          	<quill-editor class="quill-editor"   :options="editorOption" v-model="formData.overState" :content="formData.overState"></quill-editor>
          </FormItem>
          <FormItem label="缩略图上传：" prop="picture">
           	<template>
						    <Upload action="/report/api/pictureUpLoad" :on-success="handleSuccess" :show-upload-list="false">
						        <Button type="ghost" icon="ios-cloud-upload-outline">缩略图上传</Button>
						    </Upload>
						    <img :src="formData.picture" v-if="formData.picture !== ''" style="width:100px;height:100px;margin-top:10px;border：1px solid #ccc">
						</template>
          </FormItem>
          <FormItem label="责任人：" prop="responsible">
            <Input v-model="formData.responsible" placeholder="请输入责任人"></Input>
          </FormItem>
          <FormItem label="配合人：" prop="cooperator">
            <Input v-model="formData.cooperator" placeholder="请输入配合人"></Input>
          </FormItem>
           <!--<FormItem prop="leaders" style="position:relative;">
          	<Checkbox v-model="single" style="position:absolute;z-index:222222;left:-78px;top:0;">@领导：</Checkbox>
		        <Input v-model="formData.leaders" placeholder=""></Input>
		   		</FormItem>-->
         <FormItem label="完成时限：" prop="timeLimit">
          	<DatePicker  :value="formData.timeLimit"  @on-change="dateChangea" :options="options3" placement="top-start" type="date"  placeholder="完成时限" style="width: 200px"></DatePicker>
          </FormItem>
          
          <FormItem label="存在风险：" prop="isRisk">
            <RadioGroup v-model="formData.isRisk">
              <Radio :label="1">是</Radio>
              <Radio :label="0">否</Radio>
            </RadioGroup>
          </FormItem>
          <FormItem label="风险情况：" prop="riskSituation">
            <Input v-if="formData.isRisk === 1" v-model="formData.riskSituation" placeholder="请输入开票回款风险情况" ></Input>
            <Input v-else disabled v-model="formData.riskSituation" placeholder="请输入开票回款风险情况" ref="riskInput"></Input>
          </FormItem>
          <FormItem label="备注：" prop="remark">
            <Input v-model="formData.remark" placeholder="请输入备注"></Input>
          </FormItem>
        </Form>
      </Col>
    </Row>
    
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
	      <div slot="footer"></div>
    </Modal>
  </div>
</template>

<script>
import api from '@/service'
import $ from 'jquery'
export default {
  name: 'project_modify',
  data() {
    return {
    	options3: {
        disabledDate (date) {
          return date && date.valueOf() < Date.now() - 86400000;
        }
      },
    	isImportant:'',
    	editorOption:{
          modules:{
             toolbar:[
              ['bold', 'italic', 'underline', 'strike'],        // toggled buttons
              [{ 'header': [1, 2, 3, 4, 5, 6, false] }],
              [{ 'color': [] }, { 'background': [] }], 
							[{ 'list': 'ordered'}, { 'list': 'bullet' }],
            ]
          },
          placeholder: "请填写完成情况！"
     },
    	single:false,
    	modalPre:false,
    	aaa:false,
    	content: "",
	    defaultList:'',
      imgName: '',
      aa:'',
      overState:'',
      visible: false,
      uploadList: '',
      formData: {
      	selecttime:'',
        centerId: null,
        isImportant: 0,
        projectName: '',
        workProject: '',
        workContent: '',
        overState: '',
        responsible: '',
        cooperator: '',
        timeLimit:'',
        isRisk: 0,
        riskSituation: '',
        remark: '无',
        picture:''
//      leaders:0,
      },
      formRule: {
      	selecttime: {required: true, type: 'date',mesasge: '请填写日期', trigger: 'change'},
        centerId: {required: true},
        isImportant: {required: true},
        projectName: {required: true, message: '请填写项目名称！', trigger: 'blur'},
        workProject: {required: true, message: '请填写工作项目！', trigger: 'blur'},
        workContent: {required: true, message: '请填写工作内容！', trigger: 'blur'},
        overState: {required: true, message: '请填写完成情况！', trigger: 'blur'},
        responsible: {required: true, message: '请填写责任人！', trigger: 'blur'},
        cooperator: {required: true, message: '请填写配合人！', trigger: 'blur'},
        timeLimit: {required: true, type: 'date',message: '请填写完成时限！', trigger: 'change'},
        isRisk: {required: true},
        picture: {required: false},
        riskSituation: {required: true, message: '请填写项目风险！', trigger: 'blur'},
//      leaders: {required: true, message: '@领导人为空！', trigger: 'blur'},
      }
    }
  },
  watch: {
    'formData.isRisk': 'setDefaultRisk',
    'formData.leaders': 'setDefaultleaser',
    'formData.isFocused':'setDefaultfocus',
    'formData.isImportant':'setDefaultisImportant',
  },
  created() {
  	this.defaultList = this.formData.picture
    this.initData()
  },
  mounted(){
  },
  methods: {
   	preview() {
//			var b = this.formData.overState;
//			this.overState = this.stripHtml(b)
			$('#overState').html(this.formData.overState)
  		this.modalPre = true
  	},
  	stripHtml(str) { 
	      str = str.replace(/<\/?[^>]*>/g, ''); //去除HTML tag
		    str = str.replace(/[ | ]*\n/g, '\n'); //去除行尾空白
		    //str = str.replace(/\n[\s| | ]*\r/g,'\n'); //去除多余空行
		    str = str.replace(/ /ig, ''); //去掉 
		    return str;
		},
    async initData() {
	      let contentId = this.$route.query.id
	      try {
	        let res = await api.project.getContent({contentId})
	        console.log(res)
	        this.formData = res
	      } catch (e) {
	        console.log(e)
	        this.$Message.error("获取数据失败2！")
	      }
    },
		async submit() {
					let params = this.formData
					try {
			      await api.project.saveContent(params)
			      this.$Message.success("保存成功！")
			      this.$router.go(-1)
					}catch (e) {
		    		this.$Message.error('验证失败')
		    	}
    },
    setDefaultRisk() {
    	if (this.formData.isRisk === 0) {
        this.formData.riskSituation = '无'
      } 
	  },
   setDefaultleaser() {
   		if(this.formData.leaders !== ''){
   			this.single = true
   		}
   }, 
   setDefaultfocus() {
   	if(this.formData.isFocused === 0) {
   		this.formData.isFocused = '关注'
   	}else if(this.formData.isFocused===1){
   		this.formData.isFocused='已关注'
   		this.aaa  = true
   	}
   },
   handleView (name) {
    this.imgName = name;
    this.visible = true;
   },
   handleRemove (file) {
     const fileList = this.$refs.upload.fileList;
     this.$refs.upload.fileList.splice(fileList.indexOf(file), 1);
   },
   handleSuccess (res, file) {
	     file.url = res.data;
	     this.formData.picture = res.data
	     console.log(this.formData.picture)
   },
   handleFormatError (file) {
    this.$Notice.warning({
      title: 'The file format is incorrect',
      desc: 'File format of ' + file.name + ' is incorrect, please select jpg or png.'
    });
   },
   handleMaxSize (file) {
    this.$Notice.warning({
      title: 'Exceeding file size limit',
      desc: 'File  ' + file.name + ' is too large, no more than 2M.'
    });
   },
   handleChange(selecttime) {
	      if (selecttime) {
		       this.formData.selecttime = new Date(selecttime)
		    } else {
		       this.formData.selecttime = ''
		   	}
	 },
	 dateChangea(timeLimit) {
	      if (timeLimit) {
		       this.formData.timeLimit = new Date(timeLimit)
		    } else {
		       this.formData.timeLimit = ''
		   	}
	 },
   setDefaultisImportant() {
	   	if(this.formData.isImportant === 0) {
	   		this.isImportant = ' '
	   	}else if(this.formData.isImportant===1){
	   		this.isImportant = '重点'
	   	}
	 },
   handleBeforeUpload () {
    const check = this.uploadList.length < 5;
    if (!check) {
      this.$Notice.warning({
        title: 'Up to five pictures can be uploaded.'
      });
    }
    return check;
   }
  }
}
</script>

<style lang="css" scoped>
 .demo-upload-list{
        display: inline-block;
        width: 60px;
        height: 60px;
        text-align: center;
        line-height: 60px;
        border: 1px solid transparent;
        border-radius: 4px;
        overflow: hidden;
        background: #fff;
        position: relative;
        box-shadow: 0 1px 1px rgba(0,0,0,.2);
        margin-right: 4px;
    }
    .demo-upload-list img{
        width: 100%;
        height: 100%;
    }
    .demo-upload-list-cover{
        display: none;
        position: absolute;
        top: 0;
        bottom: 0;
        left: 0;
        right: 0;
        background: rgba(0,0,0,.6);
    }
    .demo-upload-list:hover .demo-upload-list-cover{
        display: block;
    }
    .redText{
			color:#EE701E
		}
    .demo-upload-list-cover i{
        color: #fff;
        font-size: 20px;
        cursor: pointer;
        margin: 0 2px;
    }
     .info{
    border-radius: 10px;
    line-height: 20px;
    padding: 10px;
    margin: 10px;
    background-color: #ffffff;
  }
</style>
