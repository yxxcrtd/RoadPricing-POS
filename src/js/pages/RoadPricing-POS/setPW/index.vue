<template>
    <div class="container">
        <div class="header">
            <v-header title="修改密码"></v-header>
        </div>
        <div class="content">
            <input class="input" type="password" placeholder="旧密码" @change="oldPass">
            <input class="input" type="password" placeholder="新密码" @change="newPass">
            <input class="input" type="password" placeholder="确认新密码" @change="newPassConfirm">
        </div>
        <div class="edit-wrapper">
            <div class="btn" @click="submit">
                <text class="btn-text">确认</text>
            </div>
        </div>
    </div>
</template>

<script>
    import VHeader from 'Components/header/header.vue'
    import { WxcCell } from 'weex-ui'
    import { myMixins } from "../../../config/mixins";

    export default {
        name: "index",
        components: { VHeader, WxcCell },
        mixins: [myMixins],
        methods: {
            submit(){
               this.$fetch({
                   method: "POST",
                   name: "modifyPwd",
                   data: {
                        oldPwd: this.oldPass,
                        newPwd: this.newPassConfirm
                   }
               }).then(resData => {
                   console.log("======modifyPwd返回数据=====", resData)
                   if(resData.code == 200) {
                       this.$router.open({
                           name: 'login',
                           navShow: false
                       })
                   }else {
                       this.$notice.alert({ message: `modifyPwd-code：${resData.code}, modifyPwd-message: ${resData.message}` })
                   }
               }, error => {})
            },
            oldPass(event) {
                if (event.value === "" ) {
                    this.$notice.alert({
                        message: "请输入旧密码"
                    })
                }else {
                    this.oldPass = event.value;
                }
            },
            newPass(event) {
                if (!/^\d{6,10}$/.test(event.value)) {
                    this.$notice.alert({
                        message: "新密码必须是6-10位数字"
                    })
                }else {
                    this.newPass = event.value;
                }
            },
            newPassConfirm(event) {
                if(event.value !== this.newPass) {
                    this.$notice.alert({
                        message: "两次输入的新密码不一致"
                    })
                }else {
                    this.newPassConfirm = event.value;
                }
            }
        }
    }
</script>

<style lang="sass" type="text/scss" scoped>
    @import "~Css/index.scss";
    .container {
        width: 750px;
        align-items: center;
    }
    .input {
        width: 750px;
        height: 100px;
        background-color: #FFFFFF;
        padding-left: 50px;
        padding-right: 50px;
        margin-top: 30px;
        tint-color: #666666;
        color: #666666;
    }
    .btn {
        width: 700px;
        height: 100px;
        margin-top: 50px;
        border-radius: 50px;
        display: flex;
        align-items: center;
        justify-content: center;
        @include themeBackground();
    }
    .btn-text {
        font-size: $fontSize_input;
        font-weight: 600;
        color: $fontColor_white;
    }
</style>
