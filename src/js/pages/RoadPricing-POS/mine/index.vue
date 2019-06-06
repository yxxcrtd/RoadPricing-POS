<template>
    <div class="container" :style="pageStyles">
        <div class="info-wrapper">
            <div :style="{'height': statusBarHeight}"></div>
            <div :style="{'height': navBarHeight}"></div>
            <div class="info">
                <div class="avatar">
                    <text class="icon">&#xe60f;</text>
                </div>
                <div class="name">
                    <text class="name-txt">{{info.collectorName}}</text>
                    <text class="road-txt">{{info.parkingName}}</text>
                </div>
                <div class="changPW" @click="pwClick">
                    <text class="changPW-txt" >修改密码</text>
                    <text class="icon changPW-icon">&#xe81c;</text>
                </div>
            </div>
        </div>
        <div class="card-wrapper">
            <div class="card1">
                <div class="money">
                    <div class="money-sum">
                        <text class="money-txt">{{charge.totalAmount}}</text>
                        <text class="money-unit">元</text>
                    </div>
                    <text class="money-dec">总收费金额</text>
                </div>
                <div class="money">
                    <div class="money-sum">
                        <text class="money-txt">{{charge.onlineAmount}}</text>
                        <text class="money-unit">元</text>
                    </div>
                    <text class="money-dec">移动收费金额</text>
                </div>
                <div class="money">
                    <div class="money-sum">
                        <text class="money-txt">{{charge.offlineAmount}}</text>
                        <text class="money-unit">元</text>
                    </div>
                    <text class="money-dec">现金收费金额</text>
                </div>
            </div>
            <div class="card2">
                <wxc-cell label="扎账"
                          :has-arrow="true"
                          desc=""
                          @wxcCellClicked="wxcCellClicked"
                          :has-top-border="false"></wxc-cell>
                <wxc-cell label="辅警"
                          :has-arrow="true"
                          desc=""
                          :has-top-border="false"></wxc-cell>
                <wxc-cell label="使用说明"
                          :has-arrow="true"
                          :has-top-border="false"></wxc-cell>
                <wxc-cell label="系统升级"
                          :has-arrow="true"
                          :has-top-border="false"
                          :has-bottom-border="false"></wxc-cell>
            </div>
            <div class="btn-wrapper">
                <v-btn-no title="退出" @btnNoClicked="btnNoClicked"></v-btn-no>
            </div>
        </div>
    </div>
</template>

<script>
    import { WxcCell } from 'weex-ui';
    import VBtnNo from 'Components/buttonNo/index.vue'
    import { myMixins } from "../../../config/mixins";

    export default {
        name: "index",
        mixins: [myMixins],
        components: { WxcCell, VBtnNo },
        data() {
            return {
                info: {},
                charge: {}
            }
        },
        created() {
            // 如果页面没有导航栏，可以用下面这个计算高度的方法
            const tabPageHeight = weex.config.env.deviceHeight / weex.config.env.deviceWidth * 750;
            //减去tab的高度******
            this.contentStyle = { height: (tabPageHeight - 120) + 'px' };
            this.pageStyles = { height: tabPageHeight + 'px' }

            this.$storage.get('loginInfo')
            .then(resData => {
               console.log("=====get-loginInfo=====", resData)
                    this.info = resData
                    this.$fetch({
                        method: "POST",
                        name: "totalChargeInfo",
                    }).then(resData => {
                        console.log("=====totalChargeInfo返回数据======", resData)
                        if (resData.code == 200) {
                            this.charge = resData.data
                        }else {
                            this.$notice.alert({ message: `ChargeInfo-code：${resData.code}, ChargeInfo-message: ${resData.message}` })
                        }
                    }, error => {})
            }, error => {})

        },
        methods: {
            wxcCellClicked (e) {
                let _this = this;
                let data = {
                    orderType: 4,
                    total:  this.charge.offlineAmount
                }
                this.$notice.confirm({
                    title: '是否扎账？',
                    message: '扎帐后系统直接打印小票且扎帐后不可登陆，确定要扎帐吗？',
                    okTitle: '确认',
                    cancelTitl: '取消',
                    okCallback() {
                        // 点击确认按钮的回调
                        _this.$router.open({
                            name: "payType",
                            navShow: false,
                            params: data
                        })
                    },
                    cancelCallback() {
                        // 点击取消按钮的回调
                    }
                })
            },
            pwClick() {
                this.$router.open({
                    name: 'setPW',
                    navShow: false
                })
            },
            btnNoClicked() {
                let _this = this;
                this.$notice.confirm({
                    message: '确认退出？',
                    okTitle: '确认',
                    cancelTitl: '取消',
                    okCallback() {
                        // 点击确认按钮的回调
                        _this.logout();
                    },
                    cancelCallback() {
                        // 点击取消按钮的回调
                    }
                })
            },
            logout() {
                this.$fetch({
                    method: 'POST',
                    name: 'logout',
                }).then(resData => {
                    console.log("======", resData)
                    if(resData.code == 200 || resData.code > 410) {
                        this.$storage.delete('loginInfo').then(resData => {
                            this.$router.open({
                                name: 'login',
                            })
                        })
                    }else {
                        this.$notice.alert({ message: `logout-code：${resData.code}, logout-message: ${resData.message}` })
                    }
                }, error => {})
            },
        }
    }
</script>

<style lang="sass" type="text/scss" scoped>
    @import '~Css/index.scss';

    .container {
        width: 750px;
        align-items: center;
    }
    .info-wrapper {
        height: 356px;
        @include themeBackground();
    }
    .info {
        flex-direction: row;
        align-items: center;
        justify-content: space-around;
    }
    .avatar {
        width: 150px;
        align-items: center;
    }
    .name {
        width: 400px;
    }
    .changPW {
        width: 200px;
        align-items: center;
        flex-direction: row;
        justify-content: space-around;
    }
    .icon {
        font-family: iconfont;
        font-size: 100px;
        color: $fontColor_white;
    }
    .name-txt, .road-txt, .changPW-txt {
        color: $fontColor_white;
    }
    .name-txt {
        font-size: $fontSize_info_title_l2;
        font-weight: 600;
    }
    .road-txt {
        font-size: $fontSize_show;
        margin-top: 10px;
    }
    .changPW-txt {
        font-size: $fontSize_detail;
    }
    .changPW-icon {
        font-size: $fontSize_detail;
    }
    .card-wrapper {
        position: relative;
        top: -50px;
        align-items: center;
    }
    .card1 {
        width: 698px;
        height: 226px;
        border-radius: 16px;
        background-color: $bgColor-white;
        @include cardShadow();

        flex-direction: row;
        align-items: center;
        justify-content: space-around;
    }
    .card2 {
        width: 698px;
        border-radius: 16px;
        background-color: $bgColor-white;
        @include cardShadow();

        margin-top: 20px;
        margin-bottom: 20px;
    }
    .money {
        width: 200px;
        align-items: center;
    }
    .money-sum {
        flex-direction: row;
        justify-content: center;
        align-items: flex-end;
    }
    .money-txt {
        font-size: 56px;
        color: $fontColor_theme;
    }
    .money-unit {
        font-size: 32px;
        color: $fontColor_theme;
    }
    .money-dec {
        font-size: 28px;
        color: $fontColor_detail;
        margin-top: 10px;
    }
</style>
