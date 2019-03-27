<template lang="html">
  <div>
    <div class="ui-toolbar">
      <Button type="default" icon="ios-arrow-back" @click="$router.go(-1)">返回</Button>
      <Button type="primary" icon="folder" @click="submit('formData')">保存</Button>

    </div>
    <Row>
      <Col span="12" offset="6">
        <Form ref="form" :model="formData" :rules="formRule" :label-width="100">
         	<FormItem label="日期：" prop="selecttime">
              <DatePicker    type="date"   :value="formData.selecttime" @on-change="handleChange"  placement="bottom-start" placeholder="选择日期" style="width: 200px"></DatePicker>
          </FormItem>
          <FormItem label="所在中心：" prop="centerId">
            <Select v-model="formData.centerId" placeholder="请选择中心" >
              <Option v-for="item in $store.state.centers"disabled  :key="item.id" :value="item.id">{{item.name}}</Option>
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
          	<quill-editor class="quill-editor"   :options="editorOption" v-model="formData.overState" :content="formData.overState" ></quill-editor>
          </FormItem>
          <!--图片上传接口出错-->
          <FormItem label="缩略图上传：" prop="picture">
				    <template>
						    <Upload action="/report/api/pictureUpLoad" :on-success="handleSuccess" :show-upload-list="false"  >
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
          <FormItem label="完成时限：" prop="timeLimit">
          	<DatePicker  :value="formData.timeLimit"  @on-change="dateChangea" :options="options3" placement="top-start" type="date"  placeholder="完成时限" style="width: 200px"></DatePicker>
          </FormItem>
          <FormItem label="存在风险：" prop="isRisk">
            <RadioGroup v-model="formData.isRisk">
              <Radio :label="1">是</Radio>
              <Radio :label="0">否</Radio>
            </RadioGroup>
          </FormItem>
          <!--<FormItem prop="leaders" style="position:relative;">
          	<Checkbox v-model="single" style="position:absolute;z-index:222222;left:-78px;top:0;">@领导：</Checkbox>
		        <Input v-model="formData.leaders" placeholder=""></Input>
		   		</FormItem>-->
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
  </div>
</template>

<script>
import api from '@/service'
import { quillEditor } from 'vue-quill-editor'
//import $ from ‘jquery’
export default {
	components: {
    quillEditor
 },
  name: 'project_add',
  data() {
    return {
    	options3: {
        disabledDate (date) {
          return date && date.valueOf() < Date.now() - 86400000;
        }
      },
    	editorOption:{
          modules:{
            toolbar:[
              ['bold', 'italic', 'underline', 'strike'],        // toggled buttons
              [{ 'header': [1, 2, 3, 4, 5, 6, false] }],
              [{ 'color': [] }, { 'background': [] }], 
							[{ 'list': 'ordered'}, { 'list': 'bullet' }],
            ]
          },
          placeholder: "请填写完成情况！",
          theme:'snow'
      },
      a:'0',
      centerId:'1', 
      daterange:'',
      imgName: '',
      single:false,
      defaultList: [
      ],
      imgName: '',
      visible: false,
      uploadList: [],
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
        timeLimit: '',
        isRisk: 0,
        riskSituation: '无',
        remark: '无',
        leaders:'',
        picture:'',
      },
      formRule: {
      	selecttime:{required: true, type: 'date', message: '请填写日期！', trigger: 'change'},
      	uploadImages: {require: true},
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
        riskSituation: {required: true, message: '请填写项目风险！', trigger: 'change'},
//      leaders: {required: false},
        picture:{required: false},
      }
    }
  },
  computed: {
    user: function() {
      return this.$store.state.user
    },
    editor() {
      return this.$refs.myTextEditor.quillEditor
    }
  },
  watch: {
    'formData.isRisk': 'setDefaultRisk',
    'formData.leaders': 'setDefaultleaser',
    'user': 'initData'
  },
  created() {
    this.initData()
    this.defaultList = this.formData.picture
  },
  mounted() {
//	this.uploadList = this.$refs.upload.fileList;
  },
  methods: {
    initData() {
      if (this.user) {
        this.formData.centerId = this.user.centerId
      }
    },
    submit(name) {
    	this.$refs.form.validate(async (valid) => {
    		if(valid){
    			let params = this.formData
          let centerName = this.$store.state.centers.filter(item => item.id === this.formData.centerId)[0].name
          params.billId = this.$route.query.id
          params.centerName = centerName
          params.updater = this.$store.state.user.userId
          await api.project.saveContent(params)
          this.$Message.success("保存成功！")
          this.$router.go(-1)
    		}else{
    			this.$Message.error('验证失败')
    		}
    	})
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
	     console.log('----'+res.data)
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
	   handleBeforeUpload () {
	    const check = this.uploadList.length < 1;
	    if (!check) {
	      this.$Notice.warning({
	        title: 'Up to one pictures can be uploaded.'
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
    .demo-upload-list-cover i{
        color: #fff;
        font-size: 20px;
        cursor: pointer;
        margin: 0 2px;
    }
</style>
