<template>
    <div class="container">
        <div class="header">
            <v-header title='确认支付'></v-header>
        </div>
        <div class="content">
            <div class="money-wrapper">
                <text class="money-txt">合计: </text>
                <text class="money-txt money-txt-id">￥</text>
                <text class="money-num">{{this.total}}</text>
            </div>
            <div class="pay-wrapper">
                <div class="pay-item" @click="zfbPayClicked">
                   <div class="left">
                        <div class="left-icon">
                            <text class="icon zfb">&#xe636;</text>
                        </div>
                        <div class="left-txt pay-type-txt">
                            <text class="txt1">支付宝</text>
                        </div>
                   </div>
                   <div class="middle">
                        <div class="middle-icon">
                            <text class="txt2">-</text>
                        </div>
                       <div class="middle-txt">
                           <text class="txt3">推荐有支付宝账号 用户使用</text>
                       </div>
                   </div>
                   <div class="right">
                        <div class="right-icon">
                            <text class="icon">&#xe623;</text>
                        </div>
                   </div>
               </div>
                <div class="pay-item" @click="wxPayClicked">
                    <div class="left">
                        <div class="left-icon">
                            <text class="icon wx">&#xe60c;</text>
                        </div>
                        <div class="left-txt pay-type-txt">
                            <text class="txt1">微信支付</text>
                        </div>
                    </div>
                    <div class="middle">
                        <div class="middle-icon">
                            <text class="txt2">-</text>
                        </div>
                        <div class="middle-txt">
                            <text class="txt3">微信快捷支付</text>
                        </div>
                    </div>
                    <div class="right">
                        <div class="right-icon">
                            <text class="icon">&#xe623;</text>
                        </div>
                    </div>
                </div>
                <div class="pay-item" @click="xjPayClicked" v-if="this.orderType!=4">
                    <div class="left">
                        <div class="left-icon">
                            <text class="icon xj">&#xe605;</text>
                        </div>
                        <div class="left-txt pay-type-txt">
                            <text class="txt1">现金支付</text>
                        </div>
                    </div>
                    <div class="middle">
                        <div class="middle-icon">
                            <text class="txt2">-</text>
                        </div>
                        <div class="middle-txt">
                            <text class="txt3">现金支付更方便</text>
                        </div>
                    </div>
                    <div class="right">
                        <div class="right-icon">
                            <text class="icon">&#xe623;</text>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    import VHeader from 'Components/header/header.vue'
    import { WxcCell } from 'weex-ui'
    import {myMixins} from "../../../config/mixins";

    export default {
        name: "index",
        components: { VHeader, WxcCell },
        mixins: [myMixins],
        data() {
            return {
                payInfo: {},
                total: 0,
                orderType: 1,
                ids: ""
            }
        },
        created() {
            this.$router.getParams().then(resData => {
                console.log("=====车辆信息接参或购物车支付金额接参=====",resData)
                if(resData.orderType == 4) {
                    //扎账
                    this.orderType = resData.orderType;
                    this.total = resData.total;
                }else {
                    //线上支付
                    this.payInfo = resData.info;
                    //订单类型字段
                    this.orderType = resData.orderType;
                    //欠费补缴ID集合
                    this.ids = resData.id;
                    //兼容缴费出场和历史欠费补缴
                    if (resData.orderType == 2) {
                        this.total = resData.total;
                    }else {
                        this.total = resData.arrearsHistory?resData.info.chargeAmount+resData.info.totalArrearsAmount: resData.info.chargeAmount;
                    }
                }
            }, error => {})
        },
        methods: {
            zfbPayClicked() {
                if (this.orderType == 1) {
                    this.prePay(1, 2, this.payInfo.vehicleEntranceRecordId);
                }else if(this.orderType == 2) {
                    this.prePay(2, 2, null, this.ids.toString());
                }else if (this.orderType == 3) {
                    this.prePay(3, 2, this.payInfo.vehicleEntranceRecordId, null, null);
                }else if (this.orderType == 4) {
                    this.prePay(4, 2, null, null, null);
                }
            },
            wxPayClicked() {
                if (this.orderType == 1) {
                    this.prePay(1, 1, this.payInfo.vehicleEntranceRecordId);
                }else if(this.orderType == 2) {
                    this.prePay(2, 1, null, this.ids.toString());
                }else if (this.orderType == 3) {
                    this.prePay(3, 1, this.payInfo.vehicleEntranceRecordId, null, null);
                }else if (this.orderType == 4) {
                    this.prePay(4, 1, null, null, null);
                }
            },
            xjPayClicked() {
                if (this.orderType == 1) {
                    this.cashPay(1, 4, this.payInfo.vehicleEntranceRecordId);
                }else if(this.orderType == 2) {
                    this.cashPay(2, 4, null, this.ids.toString());
                }else if (this.orderType == 3) {
                    this.cashPay(3, 4, this.payInfo.vehicleEntranceRecordId, null, null);
                }
            },
            prePay(orderType, payWay, vehicleEntranceRecordId="", arrearsRecordIds="", accountSummaryId="") {
                let data = {
                    orderType: orderType,
                    payWay: payWay
                };
                if (vehicleEntranceRecordId) {
                    data.vehicleEntranceRecordId = vehicleEntranceRecordId
                }
                if (accountSummaryId) {
                    data.accountSummaryId = accountSummaryId
                }
                if (arrearsRecordIds) {
                    data.arrearsRecordIds = arrearsRecordIds
                }

                this.$fetch({
                    method: "POST",
                    name: "prePay",
                    data: data,
                }).then(resData => {
                    console.log("=====prePay返回数据=====", resData)
                    console.log("=====扫一扫传参=====", resData)
                    if(resData.code == 200) {
                        this.$router.open({
                            name: "scan",
                            navShow: false,
                            params: resData
                        })
                    }else {
                        this.$notice.alert({ message: `prePay-code：${resData.code}, prePay-message: ${resData.message}` })
                    }
                }, error => {})
            },
            cashPay(orderType, payWay, vehicleEntranceRecordId="", arrearsRecordIds="") {
                let data = {
                    orderType: orderType,
                    payWay: payWay
                };
                if (vehicleEntranceRecordId) {
                    data.vehicleEntranceRecordId = vehicleEntranceRecordId
                }
                if (arrearsRecordIds) {
                    data.arrearsRecordIds = arrearsRecordIds
                }
                this.$fetch({
                    method: "POST",
                    name: "cashPay",
                    data: data,
                }).then(resData => {
                    console.log("=================", resData)
                    if (resData.code == 200) {
                        this.$router.open({
                            name: "putOutFinish",
                            navShow: false,
                            params:resData.data.orderId
                        });
                        this.print(resData.data.orderId);
                    }else {
                        this.$notice.alert({ message: `cashPay-code：${resData.code},cashPay-message: ${resData.message}` })
                    }
                }, error => {})
            }
        }
    }
</script>

<style lang="sass" type="text/scss" scoped>
    @import "~Css/index.scss";
    .container {
        width: 750px;
    }
    .money-wrapper {
        height: 100px;
        padding-left: 30px;
        padding-right: 30px;
        flex-direction: row;
        align-items: center;
        justify-content: flex-start;
    }
    .money-txt {
        font-size: $fontSize_show;
        color: $fontColor_info_title_l2;
    }
    .money-txt-id {
        color: $fontColor_theme;
        font-weight: 600;
    }
    .money-num {
        font-size: $fontSize_info_title_l1;
        font-weight: 600;
        color: $fontColor_theme;
    }
    .pay-item {
        height: 140px;
        background-color: #FFFFFF;
        padding-left: 30px;
        padding-right: 30px;
        flex-direction: row;
        align-items: center;
        margin-bottom: 20px;
    }
    .left {
        flex: 5;
        flex-direction: row;
        align-items: center;
    }
    .left-icon {
        width: 120px;
        align-items: center;
        justify-content: center;
    }
    .left-txt {
        flex: 1;
    }
    .middle {
        flex: 4;
        flex-direction: row;
    }
    .middle-icon {
        width: 20px;
        height: 140px;
        align-items: center;
        justify-content: center;
    }
    .middle-txt {
        flex: 1;
        height: 140px;
        justify-content: center;
    }
    .right {
        flex: 2;
        flex-direction: row;
        align-items: flex-start;
        justify-content: flex-end;
    }
    .icon {
        font-family: iconfont;
        font-size: $fontSize_nav_title;
        font-weight: 600;
        color: $fontColor_show_l1;
    }
    .zfb, .wx, .yl {
        font-size: 70px;
    }
    .xj {
        font-size: 55px;
        color: #FF8E23;
    }
    .zfb {
        color: #1E94FA;
    }
    .wx {
        color: #00B30D;
    }
    .txt1 {
        font-size: $fontSize_info_title_l2;
        color: $fontColor_info_title_l2;
    }
    .txt2 {
        font-size: $fontSize_show;
        color: $fontColor_show_l1;
    }
    .txt3 {
        font-size: $fontSize_show;
        color: $fontColor_show_l1;
    }

</style>
