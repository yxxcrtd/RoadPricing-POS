<template>
    <div>
        <div class="container">
            <div class="header">
                <v-header title='车辆驶出'></v-header>
            </div>
            <scroller class="content" show-scrollbar=false showRefresh=true>
                <v-car-out-info title="车辆信息" :info1="carItem" :info2="carInfo"
                                @imgClicked="imgClicked"
                                @iconClicked="iconClicked"
                                @arrearageClicked="arrearageClicked"
                                @btnOkClicked="btnOkClicked"
                                @btnNoClicked="btnNoClicked"></v-car-out-info>
            </scroller>
        </div>
    </div>
</template>

<script>
    import VHeader from 'Components/header/header.vue'
    import VCarOutInfo from 'Components/carOutInfo/index.vue'
    import VBtn from 'Components/buttonNo/index.vue'
    import {myMixins} from "../../../config/mixins";

    export default {
        name: "index",
        components: { VHeader, VBtn, VCarOutInfo },
        mixins: [myMixins],
        data() {
            return {
                carInfo: {},
                carItem: {},
                arrearsHistory: false,
                contentStyle: ""
            }
        },
        created() {
            this.getData();
        },
        methods: {
            //获取数据
            getData() {
                this.$router.getParams().then(resData => {
                    console.log("======驶出接参=====", resData)
                    this.carItem = resData;
                    this.$fetch({
                        method: "POST",
                        name: "exitCarInfo",
                        data: {
                            parkingSpaceId: resData.id
                        }
                    }).then (resData => {
                        console.log("=====exitCarInfo返回数据=====", resData)
                        if (resData.code === 200) {
                            this.carInfo = resData.data;
                        }else {
                            this.$notice.alert({ message: `exitCarInfo-code：${resData.code}, exitCarInfo-message: ${resData.message}`})
                        }
                    }, error => {})
                }, error => {})
            },
            //车身照片
            imgClicked() {
                this.$image.camera({
                    imageWidth: '800',
                    allowCrop: true
                })
                .then(resData => {
                    console.log('============拍照成功==========', resData)
                    this.$image.upload({
                        url: `http://47.100.219.142:8081/api/exit/upload?parkingSpaceId=${this.carItem.id}`,                     // 自定义图片上传地址，默认上传地址是 eros.native.js 中的 image 地址
                        params: {
                        },                                                                  // 传递的参数
                        header: {
                            "Content-Type": "multipart/form-data"
                        },                                                                  // 自定义请求 header
                        source: resData                                                     // 图片路径
                    })
                    .then(resData => {
                        console.log('============上传成功==========', resData)    // [url1, url2...]

                    }, error => {
                        console.log('============上传失败==========', error)
                    })

                }, error => {})

            },
            //欠费按钮
            iconClicked(clicked) {
                console.log("=====欠费按钮=====", clicked)
                this.arrearsHistory = clicked;
            },
            //历史欠费
            arrearageClicked(item) {
                console.log("====驶出欠费传参=====", item)
                this.$router.open({
                    name: "arrearageList",
                    navShow: false,
                    params: item
                })
            },
            //缴费出场
            btnOkClicked(item) {
                //调用confirmExit
                this.$router.getParams().then(resData => {
                    this.$fetch({
                        method: "POST",
                        name: "exitConfirmExit",
                        header: {
                            "Session-Id": this.sessionId,
                            "Signature": ""
                        },
                        data: {
                            parkingSpaceId: resData.id,
                            isPayingArrears: this.arrearsHistory

                        }
                    }).then(resData => {
                        console.log("=====exitConfirmExit返回数据=====", resData)
                        if (resData.data.isFreeExit) {

                            this.printExit(resData.data.vehicleEntranceRecordId)

                            this.$router.open({
                                name: "putOutFinish",
                                navShow: false,
                                params: {
                                    vehicleEntranceRecordId: resData.data.vehicleEntranceRecordId
                                }
                            })
                        }else {
                            console.log("=====车辆信息传参=====", this.carInfo)
                            this.$router.open({
                                name: "payType",
                                navShow: false,
                                params: {
                                    info: this.carInfo,
                                    arrearsHistory: this.arrearsHistory,
                                    orderType: this.arrearsHistory ? 3 : 1,
                                }
                            })
                        }
                    }, error => {})
                }, error => {})
            },
            //欠费出场
            btnNoClicked(item) {
                let _this = this;
                this.$notice.confirm({
                    message: "确认要欠费出场？",
                    okCallback() {
                        _this.$router.getParams().then(resData => {
                            console.log("======欠费出场接参=====",resData)
                            _this.$fetch({
                                method: "POST",
                                name: "exitArrearsExit",
                                header: {
                                    "Session-Id": _this.sessionId,
                                    "Signature": ""
                                },
                                data: {
                                    parkingSpaceId: resData.id
                                }
                            }).then(resData => {
                                console.log("=====exitArrearsExit返回数据=====", resData)
                                if (resData.code === 200) {
                                    _this.$router.open({
                                        name: "tab",
                                        navShow: false
                                    })
                                }else {
                                    this.$notice.alert({message: `ArrearsExit-code：${resData.code}, ArrearsExit-message: ${resData.message}`})
                                }
                            }, error => {})
                        }, error => {})
                    },
                })
            }
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
        height: 1200px;
    }
</style>
