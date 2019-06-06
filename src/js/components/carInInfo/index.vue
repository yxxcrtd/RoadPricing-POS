<template>
    <div class="container">
            <div class="content">
                <list class="info-wrapper">
                    <cell class="title">
                        <text class="info-title">{{title}}</text>
                    </cell>
                    <cell class="dec">
                        <text class="info-txt info-type">车牌号码：</text>
                        <text class="info-txt info-dec info-carNo">{{data.carNumber}}</text>
                    </cell>
                    <cell class="dec">
                        <text class="info-txt info-type">泊位编号：</text>
                        <text class="info-txt info-dec">{{code}}</text>
                    </cell>
                    <cell class="dec">
                        <text class="info-txt info-type">会员类型：</text>
                        <text class="info-txt info-dec">{{memberTypeName}}</text>
                    </cell>
                    <!--<cell class="dec">-->
                        <!--<text class="info-txt info-type">车身照片：</text>-->
                    <!--</cell>-->
                    <!--<cell class="pic-wrapper" >-->
                        <!--<image class="pic" @click="imgClicked(data)"></image>-->
                    <!--</cell>-->
                </list>
                <div class="operate-wrapper">
                    <div class="btn-wrapper">
                        <v-btn title="确认入场" @btnOkClicked="btnOkClicked(info)"></v-btn>
                    </div>
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
        props: {
            title: {
                type: String,
                default: ""
            },
            data: {
                type: Object,
                default: {}
            },
            memberTypeName: {
                type: String,
                default: ""
            },
            code: {
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
        created() {

        },
        methods: {
            imgClicked(info) {
                this.$emit('imgClicked', info)
            },
            btnOkClicked(info) {
                this.$emit('btnOkClicked', info)
            }
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
        height: 500px;
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
        margin-bottom: 50px;
        flex-direction: row;
        flex-wrap: wrap;
        align-items: center;
        justify-content: flex-start;
    }
    .pic {
        width: 180px;
        height: 180px;
        background-color: red;
        margin-bottom: 10px;
        margin-right: 10px;
    }
    /*.pic2 {*/
        /*width: 200px;*/
        /*height: 198px;*/
        /*border-radius: 20px;*/
        /*align-items: center;*/
        /*justify-content: center;*/

        /*border-top-width: 10px;*/
        /*border-top-style: solid;*/
        /*border-top-color: #D8D8D8;*/
        /*border-bottom-width: 10px;*/
        /*border-bottom-style: solid;*/
        /*border-bottom-color: #D8D8D8;*/
        /*border-left-width: 10px;*/
        /*border-left-style: solid;*/
        /*border-left-color: #D8D8D8;*/
        /*border-right-width: 10px;*/
        /*border-right-style: solid;*/
        /*border-right-color: #D8D8D8;*/
    /*}*/
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
