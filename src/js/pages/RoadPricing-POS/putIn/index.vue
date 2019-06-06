<template>
    <div>
        <div class="container">
            <div class="header">
                <v-header title='车辆驶入'></v-header>
            </div>
            <div class="content">
                <v-car-in-info title="车辆信息" :data="carInfo" :memberTypeName="memberTypeName" :code="code"
                            @imgClicked="imgClicked"
                            @arrearageClicked="arrearageClicked"
                            @btnOkClicked="btnOkClicked"
                            @btnNoClicked="btnNoClicked"></v-car-in-info>
            </div>
        </div>
    </div>
</template>

<script>
    import VHeader from 'Components/header/header.vue'
    import VCarInInfo from 'Components/carInInfo/index.vue'
    import VBtn from 'Components/buttonOk/index.vue'
    import {myMixins} from "../../../config/mixins";

    var bmPt = weex.requireModule('bmPt')
    export default {
        name: "index",
        components: { VHeader, VBtn, VCarInInfo },
        mixins: [myMixins],
        data() {
            return {
                carInfo: {},
                memberTypeName: "",
                code: "",
            }
        },
        created() {
            this.$router.getParams().then(resData => {
                console.log("=====车辆驶入接参=====", resData)
                this.carInfo = resData;
                this.code = resData.info.item.code;
                this.$fetch({
                    method: "POST",
                    name: "enterCarInfo",
                    data: {
                        carNumber: resData.carNumber
                    }
                }).then(resData => {
                    console.log("=====enterCarInfo成功返回=====", resData)
                    if (resData.code === 200) {
                        this.memberTypeName = resData.data.memberTypeName;
                    }else {
                        this.$notice.alert({ message: `enterCarInfo-code：${resData.code}, enterCarInfo-message: ${resData.message}` })
                    }
                }, error => {})
            }, error => {})
        },
        methods: {
            //历史欠费
            arrearageClicked(item) {
                this.$router.open({
                    name: "arrearageList",
                    navShow: false,
                    params:item
                })
            },
            //确定入场
            btnOkClicked(item) {
                this.$fetch({
                    method: "POST",
                    name: "enterConfirm",
                    data: {
                        carNumber: this.carInfo.carNumber,
                        parkingSpaceId: this.carInfo.info.item.id //this.carInfo.info.item.id
                    }
                }).then(resData => {
                    console.log("======enterConfirm成功返回=====", resData)
                    if (resData.code === 200) {
                        //打印小票
                        this.printIn(resData.data.vehicleEntranceRecordId);

                        this.$router.open({
                            name: "putInFinish",
                            navShow: false,
                            params: resData.data.vehicleEntranceRecordId
                        })
                    }else {
                        this.$notice.alert({ message: `enterConfirm-code：${resData.code}, enterConfirm-message: ${resData.message}` })
                    }
                }, error => {})
            },

        }
    }
</script>

<style lang="sass" type="text/scss" scoped>
    @import "~Css/index.scss";
    .container {
        width: 750px;
    }
    .content {
        width: 750px;
        align-items: center;
    }
    .btn-wrapper {
        margin-top: 80px;
    }
</style>
