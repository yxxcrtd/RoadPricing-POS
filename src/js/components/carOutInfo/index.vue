<template>
    <div class="container">
        <div class="content">
                <div class="info-wrapper">
                    <div class="title">
                        <text class="info-title">{{title}}</text>
                    </div>
                    <div class="dec">
                        <text class="info-txt info-type">车牌号码：</text>
                        <text class="info-txt info-dec info-carNo">{{info1.carNumber}}</text>
                    </div>
                    <div class="dec">
                        <text class="info-txt info-type">泊位编号：</text>
                        <text class="info-txt info-dec">{{info1.code}}</text>
                    </div>
                    <div class="dec">
                        <text class="info-txt info-type">会员类型：</text>
                        <text class="info-txt info-dec">{{info2.memberTypeName}}</text>
                    </div>
                    <div class="dec" v-if="info2.pictureUrls ">
                        <text class="info-txt info-type">应收金额：</text>
                        <text class="info-txt info-dec">{{info2.chargeAmount}}元</text>
                    </div>
                    <div class="dec" v-if="info2.pictureUrls ">
                        <text class="info-txt info-type">实收金额：</text>
                        <text class="info-txt info-dec">{{info2.realChargeAmount}}元</text>
                    </div>
                    <div class="dec" v-if="info2.pictureUrls ">
                        <text class="info-txt info-type">停车场：</text>
                        <text class="info-txt info-dec">{{info1.parkingName}}</text>
                    </div>
                    <div class="dec" v-if="info2.pictureUrls ">
                        <text class="info-txt info-type">开始收费员编号：</text>
                        <text class="info-txt info-dec">{{info2.enterColJobNumber}}</text>
                    </div>
                    <div class="dec" v-if="info2.pictureUrls ">
                        <text class="info-txt info-type">结束收费员编号：</text>
                        <text class="info-txt info-dec">{{info2.exitColJobNumber}}</text>
                    </div>
                    <div class="dec" v-if="info2.pictureUrls ">
                        <text class="info-txt info-type">驶入时间：</text>
                        <text class="info-txt info-dec">{{info2.enterTime}}</text>
                    </div>
                    <div class="dec" v-if="info2.pictureUrls ">
                        <text class="info-txt info-type">驶出时间：</text>
                        <text class="info-txt info-dec">{{info2.preExitTime}}</text>
                    </div>
                    <div class="dec">
                        <text class="info-txt info-type">车身照片：</text>
                    </div>
                    <div class="pic-wrapper" v-if="info2.pictureUrls ">
                        <image v-for="(item, index) in info2.pictureUrls" class="pic"  :src=info2.pictureUrls[index]></image>
                        <div class="pic-btn" @click="imgClicked">
                            <text class="icon">&#xe6df;</text>
                        </div>
                    </div>
                </div>
                <div class="operate-wrapper" v-if="info2.totalArrearsAmount">
                    <div class="icon-wrapper" @click="iconClicked">
                        <text class="icon-checked icon-checked-true" v-if="icon">&#xe730;</text>
                        <text class="icon-checked icon-checked-false" v-if="!icon">&#xe72f;</text>
                    </div>
                    <div class="arrearage" @click="arrearageClicked(info1)">
                        <text class="arrearage-title">历史欠费</text>
                        <text class="arrearage-cash">{{info2.totalArrearsAmount}}元</text>
                    </div>
                </div>
                <div class="btn-wrapper">
                    <v-btn title="缴费出场" @btnOkClicked="btnOkClicked(info1)"></v-btn>
                </div>
                <div class="btn-wrapper">
                    <v-btn-no title="欠费出场" @btnNoClicked="btnNoClicked(info1)"></v-btn-no>
                </div>
        </div>
    </div>
</template>

<script>
    import VBtn from 'Components/buttonOk/index.vue'
    import VBtnNo from 'Components/buttonNo/index.vue'
    export default {
        name: "index",
        components: { VBtn, VBtnNo },
        data() {
          return {
              icon: false
          }
        },
        props: {
            title: {
                type: String,
                default: ""
            },
            info1: {
                type: Object,
                default: {}
            },
            info2: {
                type: Object,
                default: {}
            }
        },
        beforeCreate: function () {
            var domModule = weex.requireModule('dom');
            domModule.addRule('fontFace', {
                'fontFamily': 'iconfont',
                'src': 'url(\'bmlocal://iconfont/iconfont.ttf\')'
            });
        },
        methods: {
            iconClicked() {
              this.icon = !this.icon;
              this.$emit("iconClicked", this.icon)
            },
            arrearageClicked(info1) {
                this.$emit('arrearageClicked', info1)
            },
            imgClicked(info1) {
                this.$emit('imgClicked', info1)
            },
            btnOkClicked(info1) {
                this.$emit('btnOkClicked', info1)
            },
            btnNoClicked(info1) {
                this.$emit('btnNoClicked', info1)
            },
        }
    }
</script>

<style lang="scss" type="text/scss" scoped>
    @import "~Css/index.scss";
    .container {
        width: 750px;
    }
    .content {
        width: 750px;
        align-items: center;
        padding-bottom: 50px;
    }
    .info-wrapper {
        width: 700px;
        /*height: 750px;*/
        background-color: $bgColor-white;
        border-radius: 10px;
        align-items: flex-start;
        justify-content: center;
        margin-top: 20px;
        margin-bottom: 10px;
        padding-top: 20px;
        padding-bottom: 20px;
        padding-left: 30px;
        padding-right: 30px;
    }
    .title {
        flex-direction: row;
        align-items: center;
        margin-bottom: 10px;
    }
    .info-title {
        font-size: $fontSize_info_title_l1;
        color: $fontColor_info_title_l1;
    }
    .dec {
        flex-direction: row;
        align-items: center;
    }
    .info-txt {
        font-size: $fontSize_info_title_l2;
        color: $fontColor_info_title_l2;
        line-height: 54px;
    }
    .info-carNo {
        font-size: $fontSize_info_title_l1;
        color: $fontColor_info_title_l1;
    }
    .pic-wrapper {
        margin-top: 20px;
        /*margin-bottom: 20px;*/
        flex-direction: row;
        flex-wrap: wrap;
        align-items: center;
        justify-content: flex-start;
    }
    .pic {
        width: 180px;
        height: 180px;
        border-radius: 14px;
        margin-right: 15px;
    }
    .pic-btn {
        width: 180px;
        height: 180px;
        border-width: 5px;
        border-style: dashed;
        border-color: #eeeeee;
        border-radius: 14px;
        align-items: center;
        justify-content: center;
    }
    .pic2 {
        width: 200px;
        height: 198px;
        border-radius: 20px;
        align-items: center;
        justify-content: center;

        border-top-width: 10px;
        border-top-style: solid;
        border-top-color: #D8D8D8;
        border-bottom-width: 10px;
        border-bottom-style: solid;
        border-bottom-color: #D8D8D8;
        border-left-width: 10px;
        border-left-style: solid;
        border-left-color: #D8D8D8;
        border-right-width: 10px;
        border-right-style: solid;
        border-right-color: #D8D8D8;
    }
    .icon {
        font-family: iconfont;
        font-size: 160px;
        font-weight: 100;
        color: #D8D8D8;
    }
    .arrearage {
        width: 600px;
        height: 100px;
        padding-left: 50px;
        padding-right: 50px;
        background-color: $bgColor-white;
        border-radius: 10px;
        flex-direction: row;
        align-items: center;
        justify-content: space-between;
    }
    .arrearage-title {
        font-size: $fontSize_input;
        color: $fontColor_info_title_l1;
    }
    .arrearage-cash {
        font-size: $fontSize_info_title_l2;
        color: $fontColor_theme;
    }
    .btn-wrapper {
        margin-top: 20px;
    }
    .operate-wrapper {
        width: 700px;
        height: 100px;
        flex-direction: row;
        align-items: center;
        justify-content: center;
    }
    .icon-checked {
        font-family: iconfont;
        font-size: 42px;
        font-weight: 500;
    }
    .icon-checked-true {
        color: $fontColor_theme;
    }
    .icon-checked-false {
        color: $fontColor_show_l3;
    }
    .icon-wrapper {
        width: 60px;
        align-items: center;
        justify-content: center;
    }
    .arrearage {
        flex: 1;
    }
</style>
