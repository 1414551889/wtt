webpackJsonp([6],{"1u4a":function(e,t,a){"use strict";function r(e){a("VJOy")}Object.defineProperty(t,"__esModule",{value:!0});var n=a("3cXf"),o=a.n(n),i=a("lC5x"),s=a.n(i),l=a("J0Oq"),c=a.n(l),u=a("a3Yh"),d=a.n(u),f=a("0xDb"),m=a("0jG4"),p=a("aiqZ"),h={components:{quillEditor:p.quillEditor},name:"fresh_modify",data:function(){var e;return e={options3:{disabledDate:function(e){return e&&e.valueOf()<Date.now()-864e5}},editorOption:{modules:{toolbar:[["bold","italic","underline","strike"],[{header:[1,2,3,4,5,6,!1]}],[{color:[]},{background:[]}],[{list:"ordered"},{list:"bullet"}]]},placeholder:"请填写完成情况！",theme:"snow"},a:"0",centerId:"1",daterange:"",imgName:"",single:!1,defaultList:[]},d()(e,"imgName",""),d()(e,"visible",!1),d()(e,"uploadList",[]),d()(e,"formData",{title:"",level:"公司制度",effectiveTime:"",content:"",enclosures:""}),d()(e,"formRule",{title:{required:!0,type:"date",message:"请填写标题！",trigger:"blur"},level:{required:!0,message:"请选择级别！",trigger:"blur"},content:{required:!0,message:"请填写内容！",trigger:"blur"},effectiveTime:{required:!0,type:"date",message:"请填写生效时间！",trigger:"change"},enclosures:{required:!0,type:"",message:"请上传文件！",trigger:"upload"}}),e},computed:{user:function(){return this.$store.state.user}},created:function(){this.newthingsView(),this.initData()},watch:{"formData.level":"setlevel"},methods:{initData:function(){this.user&&(this.formData.centerId=this.user.centerId)},newthingsView:function(){var e=this;return c()(s.a.mark(function t(){var a,r,n,o,i;return s.a.wrap(function(t){for(;;)switch(t.prev=t.next){case 0:return a=e.$route.query.id,r=f.a.localStorage.getToken(),t.prev=2,n={thingsId:a,jwtToken:r},t.next=6,m.a.fresh.newthingsView(n);case 6:for(o=t.sent,e.defaultList=o.enclosures,i=0;i<e.defaultList.length;i++)e.defaultList[i].name=e.defaultList[i].enclosureName;e.formData=o,1===e.formData.level?e.formData.level="公司制度":2===e.formData.level&&(e.formData.level="部门制度"),e.$Message.success("查看成功！"),t.next=17;break;case 14:t.prev=14,t.t0=t.catch(2),e.$Message.error(t.t0.message);case 17:case"end":return t.stop()}},t,e,[[2,14]])}))()},setlevel:function(){"公司制度"===this.formData.level?this.formData.level=1:"部门制度"===this.formData.level&&(this.formData.level=2)},newthingAddSystem:function(){var e=this;return c()(s.a.mark(function t(){var a,r,n,o;return s.a.wrap(function(t){for(;;)switch(t.prev=t.next){case 0:return a=e.$route.query.id,r=f.a.localStorage.getToken(),t.prev=2,n=e.formData,n.jwtToken=r,n.thingsId=a,"公司制度"===n.level?n.level=1:"部门制度"===n.level&&(n.level=2),t.next=9,m.a.fresh.newthingAddSystem(n);case 9:o=t.sent,e.$Message.success("修改success"),t.next=16;break;case 13:t.prev=13,t.t0=t.catch(2),e.$Message.error(t.t0.message);case 16:case"end":return t.stop()}},t,e,[[2,13]])}))()},setDefaultRisk:function(){0===this.formData.isRisk&&(this.formData.riskSituation="无")},setDefaultleaser:function(){""!==this.formData.leaders&&(this.single=!0)},handleChange:function(e){this.formData.effectiveTime=e?new Date(e):""},dateChangea:function(e){this.formData.timeLimit=e?new Date(e):""},handleView:function(e){this.imgName=e,this.visible=!0},handleRemove:function(e){var t=this.$refs.upload.fileList;this.$refs.upload.fileList.splice(t.indexOf(e),1)},handleSuccess:function(e,t){console.log("----"+e.data),this.formData.enclosures=o()(e.data),console.log(this.formData.enclosures)},handleFormatError:function(e){this.$Notice.warning({title:"The file format is incorrect",desc:"File format of "+e.name+" is incorrect, please select jpg or png."})},handleMaxSize:function(e){this.$Notice.warning({title:"Exceeding file size limit",desc:"File  "+e.name+" is too large, no more than 2M."})},handleBeforeUpload:function(){var e=this.uploadList.length<1;return e||this.$Notice.warning({title:"Up to one pictures can be uploaded."}),e}}},g=function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("div",[a("div",{staticClass:"ui-toolbar"},[a("Button",{attrs:{type:"default",icon:"ios-arrow-back"},on:{click:function(t){e.$router.go(-1)}}},[e._v("返回")]),e._v(" "),a("Button",{attrs:{type:"primary",icon:"folder"},on:{click:e.newthingAddSystem}},[e._v("保存")])],1),e._v(" "),a("Row",[a("Col",{attrs:{span:"12",offset:"6"}},[a("Form",{ref:"form",attrs:{model:e.formData,rules:e.formRule,"label-width":100}},[a("FormItem",{attrs:{label:"标题：",prop:"title"}},[a("Input",{attrs:{placeholder:"请输入标题"},model:{value:e.formData.title,callback:function(t){e.$set(e.formData,"title",t)},expression:"formData.title"}})],1),e._v(" "),a("FormItem",{attrs:{label:"级别：",prop:"level"}},[a("Select",{attrs:{placeholder:"请选择级别"},model:{value:e.formData.level,callback:function(t){e.$set(e.formData,"level",t)},expression:"formData.level"}},e._l(e.$store.state.level,function(t){return a("Option",{key:t.id,attrs:{value:t.id}},[e._v(e._s(t.name))])}))],1),e._v(" "),a("FormItem",{attrs:{label:"生效日期：",prop:"effectiveTime"}},[a("DatePicker",{staticStyle:{width:"200px"},attrs:{type:"date",placement:"bottom-start",placeholder:"选择日期"},on:{"on-change":e.handleChange},model:{value:e.formData.effectiveTime,callback:function(t){e.$set(e.formData,"effectiveTime",t)},expression:"formData.effectiveTime"}})],1),e._v(" "),a("FormItem",{attrs:{label:"内容：",prop:"content"}},[a("Input",{attrs:{type:"textarea",placeholder:"请输入内容",autosize:{minRows:2}},model:{value:e.formData.content,callback:function(t){e.$set(e.formData,"content",t)},expression:"formData.content"}})],1),e._v(" "),a("FormItem",{attrs:{label:"上传附件",prop:"enclosures"}},[[a("Upload",{attrs:{action:"/report/api/uploadEnclosure","default-file-list":e.defaultList,"show-upload-list":!0}},[a("Button",{attrs:{type:"ghost",icon:"ios-cloud-upload-outline"}},[e._v("上传文件")])],1)]],2)],1)],1)],1)],1)},v=[],w={render:g,staticRenderFns:v},D=w,b=a("C7Lr"),k=r,x=b(h,D,!1,k,"data-v-667ffaac",null);t.default=x.exports},"98AF":function(e,t){},AvYF:function(e,t,a){"use strict";function r(e){a("j8VR")}Object.defineProperty(t,"__esModule",{value:!0});var n=a("3cXf"),o=a.n(n),i=a("lC5x"),s=a.n(i),l=a("J0Oq"),c=a.n(l),u=a("a3Yh"),d=a.n(u),f=a("0xDb"),m=a("0jG4"),p=a("aiqZ"),h={components:{quillEditor:p.quillEditor},name:"edit",data:function(){var e;return e={options3:{disabledDate:function(e){return e&&e.valueOf()<Date.now()-864e5}},editorOption:{modules:{toolbar:[["bold","italic","underline","strike"],[{header:[1,2,3,4,5,6,!1]}],[{color:[]},{background:[]}],[{list:"ordered"},{list:"bullet"}]]},placeholder:"请填写完成情况！",theme:"snow"},a:"0",centerId:"1",daterange:"",imgName:"",single:!1,defaultList:[]},d()(e,"imgName",""),d()(e,"visible",!1),d()(e,"uploadList",[]),d()(e,"formData",{title:"",level:"公司制度",effectiveTime:"",content:"",enclosures:""}),d()(e,"formRule",{title:{required:!0,type:"date",message:"请填写标题！",trigger:"change"},level:{required:!0,message:"请选择级别！",trigger:"blur"},content:{required:!0,message:"请填写内容！",trigger:"blur"},effectiveTime:{required:!0,type:"date",message:"请填写生效时间！",trigger:"change"},enclosures:{required:!0,type:"",message:"情上传文件",trigger:"upload"}}),e},computed:{user:function(){return this.$store.state.user}},created:function(){this.initData(),this.newthingsView(),this.defaultList=this.formData.picture},watch:{"formData.level":"setlevel"},methods:{initData:function(){this.user&&(this.formData.centerId=this.user.centerId)},setlevel:function(){"公司制度"===this.formData.level?this.formData.level=1:"部门制度"===this.formData.level&&(this.formData.level=2)},newthingsView:function(){var e=this;return c()(s.a.mark(function t(){var a,r,n,o;return s.a.wrap(function(t){for(;;)switch(t.prev=t.next){case 0:return a=e.$route.query.id,r=f.a.localStorage.getToken(),t.prev=2,n={contentId:a,jwtToken:r},t.next=6,m.a.fresh.newthingsView(n);case 6:o=t.sent,e.$Message.success("查看预览success"),t.next=13;break;case 10:t.prev=10,t.t0=t.catch(2),e.$Message.error(t.t0.message);case 13:case"end":return t.stop()}},t,e,[[2,10]])}))()},submit:function(){var e=this;return c()(s.a.mark(function t(){var a,r;return s.a.wrap(function(t){for(;;)switch(t.prev=t.next){case 0:return a=f.a.localStorage.getToken(),r=e.formData,t.prev=2,r.jwtToken=a,console.log(r),t.next=7,m.a.fresh.newthingAddSystem(r);case 7:e.$Message.success("保存成功！"),e.$router.go(-1),t.next=14;break;case 11:t.prev=11,t.t0=t.catch(2),e.$Message.error("e.message");case 14:case"end":return t.stop()}},t,e,[[2,11]])}))()},setDefaultRisk:function(){0===this.formData.isRisk&&(this.formData.riskSituation="无")},setDefaultleaser:function(){""!==this.formData.leaders&&(this.single=!0)},handleChange:function(e){this.formData.effectiveTime=e?new Date(e):""},dateChangea:function(e){this.formData.timeLimit=e?new Date(e):""},handleView:function(e){this.imgName=e,this.visible=!0},handleRemove:function(e){var t=this.$refs.upload.fileList;this.$refs.upload.fileList.splice(t.indexOf(e),1)},handleSuccess:function(e,t){console.log("----"+e.data),this.formData.enclosures=o()(e.data),console.log(this.formData.enclosures)},handleFormatError:function(e){this.$Notice.warning({title:"The file format is incorrect",desc:"File format of "+e.name+" is incorrect, please select jpg or png."})},handleMaxSize:function(e){this.$Notice.warning({title:"Exceeding file size limit",desc:"File  "+e.name+" is too large, no more than 2M."})},handleBeforeUpload:function(){var e=this.uploadList.length<1;return e||this.$Notice.warning({title:"Up to one pictures can be uploaded."}),e}}},g=function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("div",[a("div",{staticClass:"ui-toolbar"},[a("Button",{attrs:{type:"default",icon:"ios-arrow-back"},on:{click:function(t){e.$router.go(-1)}}},[e._v("返回")]),e._v(" "),a("Button",{attrs:{type:"primary",icon:"folder"},on:{click:e.submit}},[e._v("保存")])],1),e._v(" "),a("Row",[a("Col",{attrs:{span:"12",offset:"6"}},[a("Form",{ref:"form",attrs:{model:e.formData,rules:e.formRule,"label-width":100}},[a("FormItem",{attrs:{label:"标题：",prop:"title"}},[a("Input",{attrs:{placeholder:"请输入标题"},model:{value:e.formData.title,callback:function(t){e.$set(e.formData,"title",t)},expression:"formData.title"}})],1),e._v(" "),a("FormItem",{attrs:{label:"级别：",prop:"level"}},[a("Select",{attrs:{placeholder:"请选择级别"},model:{value:e.formData.level,callback:function(t){e.$set(e.formData,"level",t)},expression:"formData.level"}},e._l(e.$store.state.level,function(t){return a("Option",{key:t.id,attrs:{value:t.name}},[e._v(e._s(t.name))])}))],1),e._v(" "),a("FormItem",{attrs:{label:"生效日期：",prop:"effectiveTime"}},[a("DatePicker",{staticStyle:{width:"200px"},attrs:{type:"date",value:e.formData.effectiveTime,placement:"bottom-start",placeholder:"选择日期"},on:{"on-change":e.handleChange}})],1),e._v(" "),a("FormItem",{attrs:{label:"内容：",prop:"content"}},[a("Input",{attrs:{type:"textarea",placeholder:"请输入内容",autosize:{minRows:2}},model:{value:e.formData.content,callback:function(t){e.$set(e.formData,"content",t)},expression:"formData.content"}})],1),e._v(" "),a("FormItem",{attrs:{label:"上传附件",prop:"enclosures"}},[[a("Upload",{attrs:{action:"/report/api/uploadEnclosure","on-success":e.handleSuccess,"show-upload-list":!1}},[a("Button",{attrs:{type:"ghost",icon:"ios-cloud-upload-outline"}},[e._v("上传文件")])],1)]],2)],1)],1)],1)],1)},v=[],w={render:g,staticRenderFns:v},D=w,b=a("C7Lr"),k=r,x=b(h,D,!1,k,"data-v-11c36f4c",null);t.default=x.exports},E3Zx:function(e,t,a){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var r=a("lC5x"),n=a.n(r),o=a("J0Oq"),i=a.n(o),s=a("a3Yh"),l=a.n(s),c=a("0xDb"),u=a("0jG4"),d=(a("2sCs"),function(){var e=new Date;return e.setMonth(e.getMonth()-1),e}),f={data:function(){var e=this;return l()({pubModal:!1,pubid:"",nopubModal:!1,nopubid:"",delModal:!1,loading:!1,daterange:[],selectionDataList:[],keyword:"",options:{disabledDate:function(e){return e&&e.getTime()>Date.now()},date:d()},columns:[{type:"selection",width:60,align:"center"},{title:"编号",type:"index",width:60},{title:"标题",key:"title",width:60},{title:"生效日期",className:"publishTime",align:"center",render:function(e,t){return e("span",{domProps:{innerHTML:t.row.publishTime}})}},{title:"创建日期",className:"createTime",align:"center",render:function(e,t){return e("span",{domProps:{innerHTML:t.row.createTime}})}},{title:"发布人",key:"createrAccount",width:80},{title:"状态",key:"isPublish",width:80,align:"center",render:function(e,t){switch(t.row.isPublish){case 0:return e("span","草稿");case 1:return e("span","发布");default:text=""}return text}},{title:"浏览量",key:"browseNum",align:"center",width:80},{title:"操作",width:260,align:"center",render:function(t,a){var r=c.a.localStorage.getUser(),n=t("Button",{props:{type:"info",size:"small"},style:{marginRight:"5px"},on:{click:function(){e.$router.push({name:"freshView",query:{id:a.row.id}})}}},"查看"),o=t("Button",{props:{type:"warning",size:"small"},style:{marginRight:"5px"},on:{click:function(){e.$router.push({name:"freshModify",query:{id:a.row.id}})}}},"编辑"),i=t("Button",{props:{type:"warning",size:"small"},on:{click:function(){e.nopubModal=!0,e.nopubid=a.row.id}}},"取消发布"),s=t("Button",{props:{type:"success",size:"small"},on:{click:function(){e.pubModal=!0,e.pubid=a.row.id}}},"发布");return 0!==a.row.isPublish?t("div",[n,o,i]):1===r.role?t("div",[n,o,s]):2===r.role?t("div",[n,o,s]):void 0}}],tableData:[],lizindex:[],total:0,pageIndex:1,pageSize:10},"keyword","")},created:function(){this.newthingsList()},computed:{user:function(){return this.$store.state.user}},methods:{search:function(){this.newthingsList(1)},add:function(){this.$router.push({name:"freshAdd",query:this.$route.query})},newthingsList:function(e){var t=this;return i()(n.a.mark(function a(){var r,o,i,s;return n.a.wrap(function(a){for(;;)switch(a.prev=a.next){case 0:return e&&(t.pageIndex=e),r=c.a.localStorage.getToken(),a.prev=2,o={daterange:t.daterange,page:t.pageIndex,pageSize:t.pageSize,jwtToken:r,type:2,keyword:t.keyword},t.loading=!0,a.next=7,u.a.fresh.newthingsList(o);case 7:for(i=a.sent,t.tableData=i.list,t.total=i.reultAllCount,s=0;s<t.total;s++)t.lizindex.push(s);t.tableData.ids=t.lizindex,a.next=17;break;case 14:a.prev=14,a.t0=a.catch(2),t.$Message.error(a.t0.message);case 17:return a.prev=17,t.loading=!1,a.finish(17);case 20:case"end":return a.stop()}},a,t,[[2,14,17,20]])}))()},deleteMsg:function(){var e=this;this.selectionDataList.length<1?this.$Message.info("请选择要删除的信息"):this.selectionDataList.forEach(function(t,a){e.delModal=!0})},delOk:function(){var e=this;return i()(n.a.mark(function t(){var a,r,o,i;return n.a.wrap(function(t){for(;;)switch(t.prev=t.next){case 0:return a=[],e.selectionDataList.forEach(function(e,t){a.push(e.id)}),r=c.a.localStorage.getToken(),t.prev=3,o={jwtToken:r,thingsIds:a.join(",")+","},e.loading=!0,t.next=8,u.a.fresh.newthingsManyDelete(o);case 8:i=t.sent,e.$Message.info("删除成功"),e.newthingsList(),e.selectionDataList=[],t.next=17;break;case 14:t.prev=14,t.t0=t.catch(3),e.$Message.error(t.t0.message);case 17:return t.prev=17,e.loading=!1,t.finish(17);case 20:case"end":return t.stop()}},t,e,[[3,14,17,20]])}))()},pubOk:function(){var e=this;return i()(n.a.mark(function t(){return n.a.wrap(function(t){for(;;)switch(t.prev=t.next){case 0:return t.prev=0,t.next=3,u.a.fresh.newthingsPublish({thingsId:e.pubid,type:1});case 3:e.$Message.success("发布成功！"),e.newthingsList(),t.next=10;break;case 7:t.prev=7,t.t0=t.catch(0),e.$Message.error("操作失败!");case 10:case"end":return t.stop()}},t,e,[[0,7]])}))()},nopubOk:function(){var e=this;return i()(n.a.mark(function t(){return n.a.wrap(function(t){for(;;)switch(t.prev=t.next){case 0:return t.prev=0,t.next=3,u.a.fresh.newthingsPublish({thingsId:e.nopubid,type:0});case 3:e.$Message.success("取消发布成功！"),e.newthingsList(),t.next=10;break;case 7:t.prev=7,t.t0=t.catch(0),e.$Message.error("操作失败!");case 10:case"end":return t.stop()}},t,e,[[0,7]])}))()},tableSelect:function(e){this.selectionDataList=e},getList:function(e){this.$refs.selection;this.selectionDataList=e},dateChange:function(e){this.daterange=e}}},m=function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("div",[a("div",{staticClass:"ui-toolbar"},[a("Button",{attrs:{type:"primary",icon:"android-add"},on:{click:e.add}},[e._v("新增")]),e._v(" "),a("span",{staticStyle:{"margin-left":"36px"}}),e._v(" "),a("i-input",{staticStyle:{width:"160px"},attrs:{value:e.keyword,placeholder:"请输入关键字"},model:{value:e.keyword,callback:function(t){e.keyword=t},expression:"keyword"}}),e._v(" "),a("span",[e._v("时间段：")]),e._v(" "),a("DatePicker",{staticStyle:{width:"200px"},attrs:{options:e.options,type:"daterange",placement:"bottom-start",placeholder:"选择日期"},on:{"on-change":e.dateChange}}),e._v(" "),a("Button",{attrs:{type:"primary",icon:"ios-search"},on:{click:e.search}},[e._v("搜索")])],1),e._v(" "),a("Alert",{staticStyle:{height:"46px","line-height":"26px"},attrs:{type:"warning","show-icon":""}},[e._v("已选择"+e._s(e.selectionDataList.length)+"项数据\n\t     \t"),a("div",{staticClass:"pull-right"},[a("Button",{attrs:{type:"error"},on:{click:e.deleteMsg}},[e._v("批量删除")])],1)]),e._v(" "),a("Table",{ref:"selection",attrs:{stripe:"",columns:e.columns,data:e.tableData,loading:e.loading},on:{"on-selection-change":e.tableSelect}}),e._v(" "),a("appPage",{attrs:{total:e.total,"show-elevator":"",pageIndex:e.pageIndex,pageSize:e.pageSize},on:{pageChange:e.newthingsList}}),e._v(" "),a("Modal",{attrs:{width:"350"},on:{"on-ok":e.delOk},model:{value:e.delModal,callback:function(t){e.delModal=t},expression:"delModal"}},[a("p",{staticStyle:{"text-align":"center","padding-top":"20px","font-size":"15px"}},[e._v("您确定要删除所选的新鲜事信息吗？")])]),e._v(" "),a("Modal",{attrs:{width:"350"},on:{"on-ok":e.pubOk},model:{value:e.pubModal,callback:function(t){e.pubModal=t},expression:"pubModal"}},[a("p",{staticStyle:{"text-align":"center","padding-top":"20px","font-size":"15px"}},[e._v("您确定要发布所选的新鲜事信息吗？")])]),e._v(" "),a("Modal",{attrs:{width:"350"},on:{"on-ok":e.nopubOk},model:{value:e.nopubModal,callback:function(t){e.nopubModal=t},expression:"nopubModal"}},[a("p",{staticStyle:{"text-align":"center","padding-top":"20px","font-size":"15px"}},[e._v("您确定要取消发布所选的新鲜事信息吗？")])])],1)},p=[],h={render:m,staticRenderFns:p},g=h,v=a("C7Lr"),w=v(f,g,!1,null,null,null);t.default=w.exports},LAhK:function(e,t,a){"use strict";function r(e){a("tgaN")}Object.defineProperty(t,"__esModule",{value:!0});var n=a("lC5x"),o=a.n(n),i=a("J0Oq"),s=a.n(i),l=a("a3Yh"),c=a.n(l),u=a("0xDb"),d=a("0jG4"),f=a("aiqZ"),m={components:{quillEditor:f.quillEditor},name:"fresh_view",data:function(){var e;return e={defaultList:[],options3:{disabledDate:function(e){return e&&e.valueOf()<Date.now()-864e5}},editorOption:{modules:{toolbar:[["bold","italic","underline","strike"],[{header:[1,2,3,4,5,6,!1]}],[{color:[]},{background:[]}],[{list:"ordered"},{list:"bullet"}]]},placeholder:"请填写完成情况！",theme:"snow"},a:"0",centerId:"1",daterange:"",imgName:"",single:!1},c()(e,"imgName",""),c()(e,"visible",!1),c()(e,"uploadList",[]),c()(e,"formData",{title:"",level:"公司制度",effectiveTime:"",content:"",enclosures:""}),c()(e,"formRule",{title:{required:!0,type:"date",message:"请填写标题！",trigger:"blur"},level:{required:!0,message:"请选择级别！",trigger:"blur"},content:{required:!0,message:"请填写内容！",trigger:"blur"},effectiveTime:{required:!0,type:"date",message:"请填写生效时间！",trigger:"change"},enclosures:{required:!0,type:"",message:"请上传文件！",trigger:"upload"}}),e},computed:{user:function(){return this.$store.state.user}},created:function(){this.initData(),this.newthingsView()},watch:{},methods:{handleSuccess:function(e,t){t.url=e.data,console.log("----"+e.data),this.formData.enclosures=e.data},initData:function(){this.user&&(this.formData.centerId=this.user.centerId)},newthingsView:function(){var e=this;return s()(o.a.mark(function t(){var a,r,n,i,s;return o.a.wrap(function(t){for(;;)switch(t.prev=t.next){case 0:return a=e.$route.query.id,r=u.a.localStorage.getToken(),t.prev=2,n={thingsId:a,jwtToken:r},t.next=6,d.a.fresh.newthingsView(n);case 6:for(i=t.sent,e.defaultList=i.enclosures,s=0;s<e.defaultList.length;s++)e.defaultList[s].name=e.defaultList[s].enclosureName;e.formData=i,1===e.formData.level?e.formData.level="公司制度":2===e.formData.level&&(e.formData.level="部门制度"),e.$Message.success("查看成功！"),t.next=17;break;case 14:t.prev=14,t.t0=t.catch(2),e.$Message.error(t.t0.message);case 17:case"end":return t.stop()}},t,e,[[2,14]])}))()},submit:function(){var e=this;return s()(o.a.mark(function t(){var a,r;return o.a.wrap(function(t){for(;;)switch(t.prev=t.next){case 0:return a=u.a.localStorage.getToken(),r=e.formData,t.prev=2,r.jwtToken=a,console.log(r),t.next=7,d.a.fresh.newthingAddSystem(r);case 7:e.$Message.success("保存成功！"),e.$router.go(-1),t.next=14;break;case 11:t.prev=11,t.t0=t.catch(2),e.$Message.error("e.message");case 14:case"end":return t.stop()}},t,e,[[2,11]])}))()},setDefaultRisk:function(){0===this.formData.isRisk&&(this.formData.riskSituation="无")},setDefaultleaser:function(){""!==this.formData.leaders&&(this.single=!0)},handleChange:function(e){this.formData.effectiveTime=e?new Date(e):""},dateChangea:function(e){this.formData.timeLimit=e?new Date(e):""},handleView:function(e){this.imgName=e,this.visible=!0},handleRemove:function(e){var t=this.$refs.upload.fileList;this.$refs.upload.fileList.splice(t.indexOf(e),1)},handleFormatError:function(e){this.$Notice.warning({title:"The file format is incorrect",desc:"File format of "+e.name+" is incorrect, please select jpg or png."})},handleMaxSize:function(e){this.$Notice.warning({title:"Exceeding file size limit",desc:"File  "+e.name+" is too large, no more than 2M."})},handleBeforeUpload:function(){var e=this.uploadList.length<1;return e||this.$Notice.warning({title:"Up to one pictures can be uploaded."}),e}}},p=function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("div",[a("div",{staticClass:"ui-toolbar"},[a("Button",{attrs:{type:"default",icon:"ios-arrow-back"},on:{click:function(t){e.$router.go(-1)}}},[e._v("返回")])],1),e._v(" "),a("Row",[a("Col",{attrs:{span:"12",offset:"6"}},[a("Form",{ref:"form",attrs:{model:e.formData,rules:e.formRule,"label-width":100}},[a("FormItem",{attrs:{label:"标题：",prop:"title"}},[a("Input",{attrs:{placeholder:"请输入标题"},model:{value:e.formData.title,callback:function(t){e.$set(e.formData,"title",t)},expression:"formData.title"}})],1),e._v(" "),a("FormItem",{attrs:{label:"级别：",prop:"level"}},[a("Select",{attrs:{placeholder:"请选择级别"},model:{value:e.formData.level,callback:function(t){e.$set(e.formData,"level",t)},expression:"formData.level"}},e._l(e.$store.state.level,function(t){return a("Option",{key:t.id,attrs:{value:t.id}},[e._v(e._s(t.name))])}))],1),e._v(" "),a("FormItem",{attrs:{label:"生效日期：",prop:"effectiveTime"}},[a("DatePicker",{staticStyle:{width:"200px"},attrs:{type:"date",placement:"bottom-start",placeholder:"选择日期"},on:{"on-change":e.handleChange},model:{value:e.formData.effectiveTime,callback:function(t){e.$set(e.formData,"effectiveTime",t)},expression:"formData.effectiveTime"}})],1),e._v(" "),a("FormItem",{attrs:{label:"内容：",prop:"content"}},[a("Input",{attrs:{type:"textarea",placeholder:"请输入内容",autosize:{minRows:2}},model:{value:e.formData.content,callback:function(t){e.$set(e.formData,"content",t)},expression:"formData.content"}})],1),e._v(" "),a("FormItem",{attrs:{label:"上传附件",prop:"enclosures"}},[[a("Upload",{attrs:{action:"/report/api/uploadEnclosure","default-file-list":e.defaultList,"on-success":e.handleSuccess,"show-upload-list":!0}},[a("Button",{attrs:{type:"ghost",icon:"ios-cloud-upload-outline"}},[e._v("上传文件")])],1)]],2)],1)],1)],1)],1)},h=[],g={render:p,staticRenderFns:h},v=g,w=a("C7Lr"),D=r,b=w(m,v,!1,D,"data-v-2f90dedd",null);t.default=b.exports},VJOy:function(e,t){},j8VR:function(e,t){},lAJP:function(e,t,a){"use strict";function r(e){a("98AF")}Object.defineProperty(t,"__esModule",{value:!0});var n=a("lC5x"),o=a.n(n),i=a("J0Oq"),s=a.n(i),l=a("a3Yh"),c=a.n(l),u=a("0xDb"),d=a("0jG4"),f={name:"fresh_add",data:function(){var e;return e={a:"0",centerId:"1",daterange:"",imgName:"",single:!1,defaultList:[]},c()(e,"imgName",""),c()(e,"visible",!1),c()(e,"uploadList",[]),c()(e,"formData",{title:"",level:"公司制度",effectiveTime:"",content:"",enclosures:""}),c()(e,"formRule",{title:{required:!0,type:"date",message:"请填写标题！",trigger:"blur"},level:{required:!0,message:"请选择级别！",trigger:"blur"},content:{required:!0,message:"请填写内容！",trigger:"blur"},effectiveTime:{required:!0,type:"date",message:"请填写生效时间！",trigger:"change"},enclosures:{required:!0,type:"",message:"请上传文件！",trigger:"upload"}}),e},computed:{user:function(){return this.$store.state.user}},created:function(){this.initData()},methods:{initData:function(){this.user&&(this.formData.centerId=this.user.centerId)},submit:function(){var e=this;return s()(o.a.mark(function t(){var a,r;return o.a.wrap(function(t){for(;;)switch(t.prev=t.next){case 0:return a=u.a.localStorage.getToken(),t.prev=1,r=e.formData,r.jwtToken=a,"公司制度"===r.level?r.level=1:"部门制度"===r.level&&(r.level=2),console.log(r),t.next=8,d.a.fresh.newthingAddSystem(r);case 8:e.$Message.success("保存成功！"),e.$router.go(-1),t.next=15;break;case 12:t.prev=12,t.t0=t.catch(1),e.$Message.error(t.t0.message);case 15:case"end":return t.stop()}},t,e,[[1,12]])}))()},handleChange:function(e){this.formData.effectiveTime=e?new Date(e):""},handleSuccess:function(e,t){t.url=e.data,console.log("----"+e.data),this.formData.enclosures=e.data}}},m=function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("div",[a("div",{staticClass:"ui-toolbar"},[a("Button",{attrs:{type:"default",icon:"ios-arrow-back"},on:{click:function(t){e.$router.go(-1)}}},[e._v("返回")]),e._v(" "),a("Button",{attrs:{type:"primary",icon:"folder"},on:{click:e.submit}},[e._v("保存")])],1),e._v(" "),a("Row",[a("Col",{attrs:{span:"12",offset:"6"}},[a("Form",{ref:"form",attrs:{model:e.formData,rules:e.formRule,"label-width":100}},[a("FormItem",{attrs:{label:"标题：",prop:"title"}},[a("Input",{attrs:{placeholder:"请输入标题"},model:{value:e.formData.title,callback:function(t){e.$set(e.formData,"title",t)},expression:"formData.title"}})],1),e._v(" "),a("FormItem",{attrs:{label:"级别：",prop:"level"}},[a("Select",{attrs:{placeholder:"请选择级别"},model:{value:e.formData.level,callback:function(t){e.$set(e.formData,"level",t)},expression:"formData.level"}},e._l(e.$store.state.level,function(t){return a("Option",{key:t.id,attrs:{value:t.name}},[e._v(e._s(t.name))])}))],1),e._v(" "),a("FormItem",{attrs:{label:"生效日期：",prop:"effectiveTime"}},[a("DatePicker",{staticStyle:{width:"200px"},attrs:{type:"date",placement:"bottom-start",placeholder:"选择日期"},on:{"on-change":e.handleChange},model:{value:e.formData.effectiveTime,callback:function(t){e.$set(e.formData,"effectiveTime",t)},expression:"formData.effectiveTime"}})],1),e._v(" "),a("FormItem",{attrs:{label:"内容：",prop:"content"}},[a("Input",{attrs:{type:"textarea",placeholder:"请输入内容",autosize:{minRows:2}},model:{value:e.formData.content,callback:function(t){e.$set(e.formData,"content",t)},expression:"formData.content"}})],1),e._v(" "),a("FormItem",{attrs:{label:"上传附件",prop:"enclosures"}},[[a("Upload",{attrs:{action:"/report/api/uploadEnclosure","on-success":e.handleSuccess,"show-upload-list":!0}},[a("Button",{attrs:{type:"ghost",icon:"ios-cloud-upload-outline"}},[e._v("上传文件")])],1)]],2)],1)],1)],1)],1)},p=[],h={render:m,staticRenderFns:p},g=h,v=a("C7Lr"),w=r,D=v(f,g,!1,w,"data-v-36fcc16e",null);t.default=D.exports},tgaN:function(e,t){}});