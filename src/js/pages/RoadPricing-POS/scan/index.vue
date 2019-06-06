<template>
    <div class="container">
        <div class="header">
            <v-header title='扫一扫'></v-header>
        </div>
        <div class="web-wrapper">
            <web ref="webview" style="width: 730px; height: 750px" :src="url"></web>
        </div>
        <!--<div class="content" @click="scanClicked">-->
        <!--</div>-->
        <div class="dec">
            <text class="dec-txt">扫一扫完成支付</text>
        </div>
        <div class="dec">
            <v-btn title="扫一扫完成" @btnOkClicked="btnOkClicked"></v-btn>
        </div>
    </div>
</template>

<script>
    import VHeader from 'Components/header/header.vue'
    import VBtn from 'Components/buttonOk/index.vue'
    import {myMixins} from "../../../config/mixins";

    const webview = weex.requireModule('webview')
    export default {
        name: "index",
        components: {VHeader, VBtn},
        mixins: [myMixins],
        data() {
            return {
                url: "",
                timer: ""
            }
        },
        created() {
            this.$router.getParams().then(resData => {
                console.log("======扫一扫接参======", resData)
                // this.url = "http://file.igoosd.com/qr_code.html?url=" + resData.data.qrCodeUrl;
                this.url = "http://47.100.219.142/qr_code.html?url=" + resData.data.qrCodeUrl;
                this.payConfirm(resData.data.orderId)
            }, error => {
            })
        },
        methods: {
            payConfirm(orderId) {
                clearInterval(this.timer);
                this.timer = setInterval(() => {
                    this.$fetch({
                        method: "POST",
                        name: "payConfirm",
                        data: {
                            orderId: orderId
                        }
                    }).then(resData => {
                        console.log("=====轮询订单成功返回=====", resData)
                        if (resData.code == 200 && resData.data.payStatus == 2) {
                            clearInterval(this.timer)
                            this.$router.open({
                                name: "putOutFinish",
                                navShow: false,
                                params: orderId
                            })
                            this.print(orderId);
                        } else {
                            //this.$notice.alert({ message: `preConfirm-code：${resData.code}, preConfirm-message: ${resData.message}` })
                        }
                    }, error => {
                    })
                }, 2000)
            },
            btnOkClicked() {
                this.$router.open({
                    name: "tab",
                    navShow: false
                })
            }
        }
    }
</script>

<style lang="sass" type="text/scss" scoped>
    @import '~Css/index.scss';

    .container {
        width: 750px;
        align-items: center;
    }

    .content {
        margin-top: 100px;
        width: 506px;
        height: 506px;
        border-top-width: 2px;
        border-top-style: solid;
        border-top-color: $fontColor_theme;
        border-bottom-width: 2px;
        border-bottom-style: solid;
        border-bottom-color: $fontColor_theme;
        border-left-width: 2px;
        border-left-style: solid;
        border-left-color: $fontColor_theme;
        border-right-width: 2px;
        border-right-style: solid;
        border-right-color: $fontColor_theme;
    }

    .dec {
        margin-top: 40px;
    }

    .dec-txt {
        font-size: $fontSize_nav_title;
        color: $fontColor_show_l2;
    }
</style>
