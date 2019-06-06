<template>
    <div class="container">
        <v-headersearch @iconClicked="iconClicked" :inputText="inputText"></v-headersearch>
        <scroller show-scrollbar=false showRefresh=true>
            <refresh class="refresh" @refresh="onrefresh" :display="refreshing ? 'show' : 'hide'">
                <text class="refresh-text">正在加载最新车位数据</text>
            </refresh>
            <car :items="carList"
                 @putInClicked="putInClicked"
                 @putOutClicked="putOutClicked"
                 @arrearageClicked="arrearageClicked">
            </car>
        </scroller>
    </div>
</template>

<script>
    import VHeadersearch from 'Components/headerSearch/index.vue'
    import Car from 'Components/car/car.vue'
    import {WxcSearchbar} from 'weex-ui'
    import {myMixins} from "../../../config/mixins";
    import md5 from 'js-md5'

    var bmImage = weex.requireModule('bmImage')
    var bmCar = weex.requireModule('bmCar')
    var bmCamera = weex.requireModule('bmCamera')

    export default {
        name: "home",
        components: {VHeadersearch, Car, WxcSearchbar},
        mixins: [myMixins],
        data() {
            return {
                refreshing: false,
                carList: [],
                inputText: "请输入车牌号或泊位号",
            }
        },
        created() {
            this.$storage.get('loginInfo')
            .then(resData => {
                this.token = resData.token;
                this.sessionId = resData.sessionId;
                //token签名
                this.token2md5 = md5(this.token);
                //获取首页数据
                this.getHomeData();
            }, error => {});
        },
        methods: {
            //页面数据
            getHomeData() {
                this.$fetch({
                    method: "POST",
                    name: "main",
                }).then(resData => {
                    console.log("=====main成功返回====", resData)
                    if (resData.code === 200) {
                        this.carList = resData.data;
                        this.refreshing = false;
                    } else {
                        this.$notice.alert({message: `main-code：${resData.code}, main-message: ${resData.message}`})
                    }
                }, error => {
                })
            },
            //筛选
            iconClicked(str) {
                this.$fetch({
                    method: "POST",
                    name: "main",
                }).then(resData => {
                    console.log("=====main成功返回====", resData)
                    if (resData.code === 200) {
                        let searchCarList = [];
                        resData.data.forEach(item => {
                            if (item.code.indexOf(str) != -1 || (item.carNumber && item.carNumber.indexOf(str) != -1)) {
                                searchCarList = searchCarList.concat(item);
                            }
                        })
                        this.carList = searchCarList
                    } else {
                        this.$notice.alert({message: `main-code：${resData.code}, main-message: ${resData.message}`})
                    }
                }, error => {
                })
            },
            //驶入不拍照
            /*putInClicked(item) {
                console.log("=====入场传参=====", item)
                this.$router.open({
                    name: "identify",
                    navShow: false,
                    params: item
                })
            },*/
             //驶入
            //  putInClicked(item) {
            //     var _this = this;
            //     this.$image.camera({
            //         imageWidth: 600,
            //         allowCrop: true
            //     }).then(resData => {
            //
            //         console.log("===拍照测试成功===", resData["0"])
            //         bmCar.recognize(resData["0"], res=>{
            //
            //             console.log("==========", res);
            //             res = JSON.parse(res);
            //             console.log("=====车辆识别结果=====", res)
            //            if(res.code == 200) {
            //                _this.$router.open({
            //                    name: "identify",
            //                    navShow: false,
            //                    params: {
            //                        item: item,
            //                        carNumber: res.data.carNumber
            //                    }
            //                })
            //            }else {
            //                _this.$notice.alert({
            //                    message: res.code
            //                })
            //            }
            //        })
            //         /*bmImage.base64(resData["0"], function(res){
            //             console.log("===转成base64===", res)
            //             _this.$fetch({
            //                 method: "POST",
            //                 url: "http://hcapi03.market.alicloudapi.com/ocr/car-license",
            //                 header: {
            //                     Authorization: "APPCODE ff5ebcef5da94d7eab2591ae4558fe48",
            //                     // 'Content-Type': 'application/json'
            //                 },
            //                 data: {
            //                     pic: 'data:image/jpeg;base64,' + res
            //                 }
            //             }).then(resData => {
            //                 console.log("=============识别结果成功返回===========", resData)
            //                 if(resData.ret == 200) {
            //                     _this.$router.open({
            //                         name: "identify",
            //                         navShow: false,
            //                         params: {
            //                             item: item,
            //                             carNumber: resData.data.number
            //                         }
            //                     })
            //                 }else {
            //                     _this.$notice.alert({
            //                         message: `identify-ret：${resData.ret}, identify-msg: ${resData.msg}`
            //                     })
            //                 }
            //             }, error => {})
            //         })*/
            //     }, error => {})
            // },


            putInClicked(item) {
                let _this = this;
                bmCamera.ocrRecog({}, res=>{
                    res.data = JSON.parse(res.data);
                    console.log("视频识别======",res);
                    _this.$router.open({
                            name: "identify",
                            navShow: false,
                            params: {
                                item: item,
                                info: res.data
                            }
                        })
                }, err=>{})
            },
            //驶出
            putOutClicked(item) {
                console.log("=====驶出传参=====", item)
                this.$router.open({
                    name: "putOut",
                    navShow: false,
                    params: item
                })
            },
            //欠费
            arrearageClicked(item) {
                this.$router.open({
                    name: "arrearageList",
                    navShow: false,
                    params: item
                })
            },
            //刷新
            onrefresh() {
                this.refreshing = true
                this.getHomeData()
            }
        }
    }
</script>

<style lang="sass" type="text/scss" scoped>
    .container {
        position: absolute;
        left: 0;
        right: 0;
        top: 0;
        bottom: 0;
        align-items: center;
    }

    .refresh {
        width: 750px;
        height: 100px;
        align-items: center;
        justify-content: center;
    }

    .refresh-text {
        font-size: 22px;
        color: #6B6B6B;
    }
</style>
