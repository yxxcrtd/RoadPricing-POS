<template>
    <div class="container" >
        <list ref="list" :showRefresh="true" @refresh="onrefresh">
            <cell class="wrapper" v-for="item in items" >
                <div class="icon-wrapper" v-if="!isList" @click="iconClicked(item)">
                    <text class="icon-checked icon-checked-true" v-if="item.clicked">&#xe730;</text>
                    <text class="icon-checked icon-checked-false" v-if="!item.clicked">&#xe72f;</text>
                </div>
                    <div class="picker-wrapper" @click="pickerClicked(item)">
                        <div class="title">
                            <text class="title-txt">{{item.parkingName.substring(0, 6)}}</text>
                            <text class="title-cash">{{item.arrearsAmount}}元</text>
                        </div>
                        <div class="content">
                            <div class="left">
                                <div class="dec">
                                    <text class="icon">&#xe646;</text>
                                    <text class="dec-txt">入场时间</text>
                                </div>
                                <div class="time">
                                    <text class="time-txt">{{item.enterTime}}</text>
                                </div>
                            </div>
                            <div class="middle-line"></div>
                            <div class="right">
                                <div class="dec">
                                    <text class="icon">&#xe646;</text>
                                    <text class="dec-txt">出场时间</text>
                                </div>
                                <div class="time">
                                    <text class="time-txt">{{item.exitTime}}</text>
                                </div>
                            </div>
                        </div>
                    </div>
            </cell>
        </list>
    </div>
</template>

<script>
    export default {
        name: "index",
        data() {
             return {
                 clicked: true,
             }
        },
        props: {
            items: {
                type: Array,
                default: []
            },
            isList: {
                type: Boolean,
                default: false
            },
            allIcon: {
                type: Boolean,
                default: false
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
            //点击按钮
            iconClicked(item) {
                console.log("=====iconClicked=====", item);
                item.clicked = !item.clicked;

            },
            //点击卡片
            pickerClicked(item) {
                this.$emit("pickerClicked2", item)
            },
            onrefresh() {
                // Promise.all([disease.GET_COMMONDISEASES_LIST(this), banner.GET_BANNER_LIST(this)]).then(() => {
                //     // 请求数据结束后调用 refreshEnd 方法
                //     this.$refs["list"].refreshEnd()
                // })
                this.$notice.toast({
                    message: "下拉刷新获取新数据"
                })

                this.$refs["list"].refreshEnd()

            }
        }
    }
</script>

<style lang="sass" type="text/scss" scoped>
    @import "~Css/index.scss";
    .container {
        width: 750px;
        align-items: center;
    }
    .wrapper {
        width: 700px;
        flex-direction: row;
    }
    .icon-wrapper {
        width: 60px;
        height: 198px;
        align-items: flex-start;
        justify-content: center;
    }
    .icon-checked {
        font-family: iconfont;
        font-size: 40px;
        font-weight: 600;
    }
    .icon-checked-true {
        color: $fontColor_theme;
    }
    .icon-checked-false {
        color: $fontColor_show_l3;
    }
    .picker-wrapper {
        flex: 1;
        height: 198px;
        margin-bottom: 50px;
        flex-direction: column;
        align-items: center;
        justify-content: center;
        border-radius: 10px;
        background-color: $bgColor-white;
    }
    .title{
        width: 580px;
        flex-direction: row;
        align-items: center;
        justify-content: space-between;
    }
    .title-txt, .title-cash {
        font-size: $fontSize_info_title_l1;
        color: $fontColor_info_title_l1;
    }
    .content {
        flex-direction: row;
        align-items: center;
        justify-content: center;
    }
    .left, .right {
        width: 260px;
        height: 100px;
        justify-content: space-around;
    }
    .dec {
        flex-direction: row;
    }
    .icon {
        flex-basis: 40px;
    }
    .dec-txt {
        flex: 1;
        font-size: $fontSize_show - 6;
        color: $fontColor_detail;
    }
    .icon {
        font-family: iconfont;
        font-size: $fontSize_nav_title - 6;
        font-weight: 600;
        color: $fontColor_theme;
    }
    .time-txt {
        font-size: $fontSize_show - 6;
        color: $fontColor_info_title_l2;
    }
    .middle-line {
        width: 2px;
        height: 70px;
        margin-left: 30px;
        margin-right: 30px;
        background-color: #DDDDDD;
    }
</style>
