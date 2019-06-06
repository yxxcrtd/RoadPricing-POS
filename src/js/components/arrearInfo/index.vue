<template>
    <div class="container">
            <div class="content">
                <list class="info-wrapper">
                    <cell class="title">
                        <text class="info-title">{{title}}</text>
                    </cell>
                    <cell class="dec">
                        <text class="info-txt info-type">车牌号码：</text>
                        <text class="info-txt info-dec info-carNo">{{info.carNumber}}</text>
                    </cell>
                    <cell class="dec">
                        <text class="info-txt info-type">泊位编号：</text>
                        <text class="info-txt info-dec">{{info.parkingSpaceCode}}</text>
                    </cell>
                    <cell class="dec">
                        <text class="info-txt info-type">会员类型：</text>
                        <text class="info-txt info-dec">{{memberTypeName}}</text>
                    </cell>
                    <cell class="dec" >
                        <text class="info-txt info-type">应收金额：</text>
                        <text class="info-txt info-dec">{{info.chargeAmount}}元</text>
                    </cell>
                    <cell class="dec" >
                        <text class="info-txt info-type">实收金额：</text>
                        <text class="info-txt info-dec">{{info.realChargeAmount}}元</text>
                    </cell>
                    <!--<div class="dec" >-->
                        <!--<text class="info-txt info-type">出场方式：</text>-->
                        <!--<text class="info-txt info-dec">POS机</text>-->
                    <!--</div>-->
                    <cell class="dec" >
                        <text class="info-txt info-type">停车场：</text>
                        <text class="info-txt info-dec">{{info.parkingName}}</text>
                    </cell>
                    <cell class="dec" >
                        <text class="info-txt info-type">停车场位置：</text>
                        <text class="info-txt info-dec">{{info.parkingAddress}}</text>
                    </cell>
                    <!--<div class="dec" >-->
                        <!--<text class="info-txt info-type">停车场位置：</text>-->
                        <!--<text class="info-txt info-dec">望江东路与石莲南路交口</text>-->
                    <!--</div>-->
                    <cell class="dec" >
                        <text class="info-txt info-type">开始收费员编号：</text>
                        <text class="info-txt info-dec">{{info.enterCollector}}</text>
                    </cell>
                    <cell class="dec" >
                        <text class="info-txt info-type">结束收费员编号：</text>
                        <text class="info-txt info-dec">{{info.exitCollector}}</text>
                    </cell>
                    <cell class="dec" >
                        <text class="info-txt info-type">驶入时间：</text>
                        <text class="info-txt info-dec">{{info.enterTime}}</text>
                    </cell>
                    <cell class="dec" >
                        <text class="info-txt info-type">驶出时间：</text>
                        <text class="info-txt info-dec">{{info.exitTime}}</text>
                    </cell>
                    <cell class="dec">
                        <text class="info-txt info-type">车身照片：</text>
                    </cell>
                    <cell class="pic-wrapper">
                        <image v-for="(item, index) in info.carPictures"
                               :src=info.carPictures[index]
                               class="pic" @click="imgClicked"></image>
                    </cell>
                </list>
            </div>
    </div>
</template>

<script>
    import VBtn from 'Components/buttonOk/index.vue'
    import VBtnNo from 'Components/buttonNo/index.vue'
    export default {
        name: "index",
        components: { VBtn, VBtnNo },
        props: {
            title: {
                type: String,
                default: ""
            },
            info: {
                type: Object,
                default: {}
            },
            memberTypeName: {
                type: String,
                default: ""
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
            arrearageClicked(info) {
                this.$emit('arrearageClicked', info)
            },
            imgClicked() {
                this.$emit('imgClicked')
            },
            btnOkClicked() {
                this.$emit('btnOkClicked')
            },
            btnNoClicked() {
                this.$emit('btnNoClicked')
            },
        }
    }
</script>

<style lang="sass" type="text/scss" scoped>
    @import "~Css/index.scss";
    .container {
        width: 750px;
        /*background-color: #FFFFFF;*/
    }
    .content {
        width: 750px;
        align-items: center;
        padding-bottom: 50px;
    }
    .info-wrapper {
        width: 700px;
        height: 1000px;
        background-color: $bgColor-white;
        border-radius: 10px;


        align-items: flex-start;
        justify-content: center;
        margin-top: 50px;
        margin-bottom: 50px;
        padding-top: 20px;
        padding-bottom: 20px;
        padding-left: 50px;
        padding-right: 50px;
    }
    .title {
        flex-direction: row;
        align-items: center;
        margin-bottom: 10px;
    }
    .info-title {
        font-size: $fontSize_info_title_l1 - 2;
        color: $fontColor_info_title_l1 - 2;
    }
    .dec {
        flex-direction: row;
        align-items: center;
        justify-content: flex-start;
    }
    .info-txt {
        font-size: $fontSize_info_title_l2 - 2;
        color: $fontColor_info_title_l2 - 2;
        line-height: 54px;
    }
    .info-carNo {
        font-size: $fontSize_info_title_l1;
        color: $fontColor_info_title_l1;
    }
    .pic-wrapper {
        margin-top: 20px;
        margin-bottom: 50px;
        flex-direction: row;
        flex-wrap: wrap;
        align-items: center;
        justify-content: flex-start;
    }
    .pic {
        width: 180px;
        height: 180px;
        border-radius: 14px;
        margin-bottom: 10px;
        margin-right: 20px;
    }
    .icon {
        font-family: iconfont;
        font-size: 160px;
        font-weight: 100;
        color: #D8D8D8;
    }
    .arrearage {
        width: 700px;
        height: 100px;
        padding-left: 50px;
        padding-right: 50px;
        margin-top: 20px;
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
        margin-top: 30px;
    }
</style>
