webpackJsonp([12],{"50Tn":function(t,a,e){"use strict";Object.defineProperty(a,"__esModule",{value:!0});var o={name:"confirmBorrow",data:function(){return{data:{}}},created:function(){this.data=JSON.parse(this.$route.query.info)},methods:{back:function(){this.$router.go(-1)},confirm:function(){var t=this;this.$axios.post("/api/user/borrow",{user_id:"22510",book_id:this.data.book_id}).then(function(a){0===a.data.code&&t.$router.push("/borrowSuccess")})}}},i={render:function(){var t=this,a=t.$createElement,e=t._self._c||a;return e("div",[e("mt-header",{attrs:{title:"借书信息确认",fixed:""}},[e("div",{attrs:{slot:"left"},slot:"left"},[e("mt-button",{attrs:{icon:"back"},on:{click:t.back}},[t._v("返回")])],1)]),t._v(" "),e("div",{staticClass:"introduction-wrapper"},[e("mt-field",{attrs:{label:"书名",readonly:""},model:{value:t.data.title,callback:function(a){t.$set(t.data,"title",a)},expression:"data.title"}}),t._v(" "),e("mt-field",{attrs:{label:"编号",readonly:""},model:{value:t.data.book_id,callback:function(a){t.$set(t.data,"book_id",a)},expression:"data.book_id"}}),t._v(" "),e("mt-field",{attrs:{label:"起始日期",readonly:""},model:{value:t.data.begin_date,callback:function(a){t.$set(t.data,"begin_date",a)},expression:"data.begin_date"}}),t._v(" "),e("mt-field",{attrs:{label:"还书日期",readonly:""},model:{value:t.data.end_date,callback:function(a){t.$set(t.data,"end_date",a)},expression:"data.end_date"}}),t._v(" "),e("mt-field",{attrs:{label:"支付押金",value:"20经验值（还书并打分后自动退还）",readonly:"",disableClear:!0}}),t._v(" "),e("div",{staticClass:"button-wrapper"},[e("mt-button",{attrs:{size:"large",type:"primary"},on:{click:t.confirm}},[t._v("确认借书")])],1)],1)],1)},staticRenderFns:[]};var n=e("C7Lr")(o,i,!1,function(t){e("6QOf")},"data-v-35c8e260",null);a.default=n.exports},"6QOf":function(t,a){}});
//# sourceMappingURL=12.15142bfe98c8f2a0ba1e.js.map