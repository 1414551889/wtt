import '../../static/utf8-jsp/ueditor.config';
  import '../../static/utf8-jsp/ueditor.all';
  import '../../static/utf8-jsp/lang/zh-cn/zh-cn';
  import Event from '../assets/js/bus.js';//这个是兄弟组件中传输数据的。在本人其他文章中有介绍

<template>
  <div class="set">
    <div class="btn-groups">
      <button @click="save">保存编辑</button>
    </div>
    <div id="ueditor" type="text/plain"></div>
  </div>
</template>
<script>
	export default {
    name: 'set',
    data () {
      return {
        ue: ''
      }
    },
    mounted () {
      this.ue = UE.getEditor('ueditor',{
        BaseUrl: '',
        UEDITOR_HOME_URL: 'static/utf8-jsp/',
        toolbars: [
          ['fullscreen', 'source', 'undo', 'redo'],
          ['bold', 'italic', 'underline', 'fontborder', 'strikethrough', 'superscript', 'subscript', 'removeformat', 'formatmatch', 'autotypeset', 'blockquote', 'pasteplain', '|', 'forecolor', 'backcolor', 'insertorderedlist', 'insertunorderedlist', 'selectall', 'cleardoc']
        ]
      })
    },
    methods: {
      save(){
        let ueditordata = UE.getEditor('ueditor').getContent();
        Event.$emit('seted', ueditordata);
      }
    }
  }

export default {
    name: 'set',
    data () {
      return {
        ue: ''
      }
    },
    mounted () {
      this.ue = UE.getEditor('ueditor',{
        BaseUrl: '',
        UEDITOR_HOME_URL: 'static/utf8-jsp/',
        toolbars: [
          ['fullscreen', 'source', 'undo', 'redo'],
          ['bold', 'italic', 'underline', 'fontborder', 'strikethrough', 'superscript', 'subscript', 'removeformat', 'formatmatch', 'autotypeset', 'blockquote', 'pasteplain', '|', 'forecolor', 'backcolor', 'insertorderedlist', 'insertunorderedlist', 'selectall', 'cleardoc']
        ]
      })
    },
    methods: {
      save(){
        let ueditordata = UE.getEditor('ueditor').getContent();
        Event.$emit('seted', ueditordata);
      }
    }
  }
</script>
