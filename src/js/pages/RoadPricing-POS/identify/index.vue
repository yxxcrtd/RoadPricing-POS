<template>
    <div class="container">
        <div class="header">
            <v-header title="车牌识别"></v-header>
        </div>
        <div class="content">
            <img :src="info.imgPath" alt="" style="width: 650px; height: 600px;">
            <input type="text"
                   style="placeholder-color: #FFFFFF; tint-color: #FFFFFF;"
                   class="input"
                   v-model=info.carNumber
                   @change="inputCarNumber">
            <wxc-button @wxcButtonClicked="buttonClicked"></wxc-button>
        </div>
    </div>
</template>

<script>
    import VHeader from 'Components/header/header.vue'
    import { WxcButton } from "weex-ui"
    import {myMixins} from "../../../config/mixins"

    export default {
        name: "index.vue",
        components: { VHeader, WxcButton },
        mixins: [myMixins],
        data() {
            return {
                info: {},
                express: /^[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领A-Z]{1}[A-Z]{1}[A-HJ-NP-Z0-9]{4}[A-Z0-9挂学警港澳]{1}$/,
                express2: /^[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领A-Z]{1}[A-Z]{1}[DF]{1}[0-9]{5}[DF]{1}$/,
            }
        },
        created() {
            this.$router.getParams().then(resData => {
                console.log("=====入场接参=====", resData)
                this.info = resData.info
                this.carNumber = this.info.carNumber
            }, error => {})
        },

        methods: {
            inputCarNumber(event) {
                if(this.express.test(event.value) || this.express2.test(event.value)){
                    // this.carNumber = event.value;
                    console.log('===对了=====')
                }else {
                    console.log("====错了=====")
                }
            },
            buttonClicked() {
                this.$router.getParams().then(resData => {
                    console.log("=====入场接参=====", resData)
                    this.$router.open({
                        name: "putIn",
                        navShow: false,
                        params: {
                            info: resData,
                            carNumber: this.carNumber
                        }
                    })
                    console.log("=====车辆驶入传参======", resData, this.carNumber)
                }, error => {})
            }
        }
    }
</script>

<style lang="sass" type="text/scss" scoped>
    @import "~Css/index.scss";
    .container {
        position: absolute;
        left: 0;
        right: 0;
        top: 0;
        bottom: 0;
    }
    .content {
        /*height: 500px;*/
        align-items: center;
        justify-content: center;
    }
    .wrapper {
        padding: 10px;
    }
    .header{
        margin-bottom: 50px;
    }
    .input {
        width: 700px;
        height: 102px;
        background-color: $bgColor-white;
        tint-color: $fontColor_theme;
        font-size: $fontSize_info_title_l2;
        color: $fontColor_show_l1;
        placeholder-color: $fontColor_show_l1;
        padding-left: 28px;
        margin-top: 50px;
        margin-bottom: 60px;
        border-width: 4px;
        border-style: solid;
        border-color: $fontColor_theme;
    }
</style>
