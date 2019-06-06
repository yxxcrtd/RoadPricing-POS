<template>
    <div>
        <div class="container">
            <div class="header">
                <v-header title='历史欠费详情'></v-header>
            </div>
            <div class="content">
               <v-arrear-info title="车辆详情" :info="carInfo" :memberTypeName="memberTypeName"></v-arrear-info>
            </div>
        </div>
    </div>
</template>

<script>
    import VHeader from 'Components/header/header.vue'
    import VArrearInfo from 'Components/arrearInfo/index.vue'
    import VBtn from 'Components/buttonNo/index.vue'
    import {myMixins} from "../../../config/mixins";

    export default {
        name: "index",
        components: { VHeader, VBtn, VArrearInfo },
        mixins: [myMixins],
        data() {
            return {
                carInfo: {},
                memberTypeName: ""
            }
        },
        created() {
            this.$router.getParams().then(resData => {console.log("=====欠费详情接参=====", resData)
                this.carInfo = resData.data;
                this.$fetch({
                    method: "POST",
                    name: "enterCarInfo",
                    data: {
                        carNumber: resData.carNumber
                    }
                }).then(resData => {
                    console.log("=====enterCarInfo返回数据=====",resData)
                    if (resData.code == 200) {
                        this.memberTypeName = resData.data.memberTypeName;
                    }else {
                        this.$notice.alert({ message: `enterCarInfo-code：${resData.code}, enterCarInfo-message: ${resData.message}` })
                    }
                }, error => {})
            }, error => {})
        },
    }
</script>

<style lang="sass" type="text/scss" scoped>
    @import "~Css/index.scss";
    .container {
        width: 750px;
    }
    .content {
        width: 750px;
    }
    .btn-wrapper {
        position: absolute;
        left: 0;
        right: 0;
        bottom: 0;
        z-index: 100;
    }
</style>
