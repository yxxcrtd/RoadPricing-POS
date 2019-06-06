<template>
    <div class="">
        <div class="content" :style="pageStyles">
            <wxc-tab-bar :tab-titles="tabTitles"
                         :tab-styles="tabStyles"
                         duration=0
                         title-type="iconFont"
                         @wxcTabBarCurrentTabSelected="wxcTabBarCurrentTabSelected">

                <!-- 第一个页面内容-->
                <div class="item-container" :style="contentStyle">
                    <v-home></v-home>
                </div>

                <!-- 第二个页面内容-->
                <div class="item-container" :style="contentStyle">
                    <v-arrearage></v-arrearage>
                </div>

                <!-- 第三个页面内容-->
                <div class="item-container" :style="contentStyle">
                    <v-mine></v-mine>
                </div>
            </wxc-tab-bar>
        </div>
    </div>
</template>

<script>
    import { WxcTabBar, Utils, WxcMinibar} from 'weex-ui'
    import Config from './config'

    import VHome from 'Pages/RoadPricing-POS/home/index.vue'
    import VArrearage from 'Pages/RoadPricing-POS/arrearage/index.vue'
    import VMine from 'Pages/RoadPricing-POS/mine/index.vue'

    import {myMixins} from "../../../config/mixins";

    export default {
        name: "tab",
        components: { WxcTabBar, WxcMinibar, VHome, VArrearage, VMine },
        mixins: [myMixins],
        data: () => ({
            tabTitles: Config.tabIconFontTitles,
            tabStyles: Config.tabIconFontStyles,
        }),
        created () {
            this.$navigator.setNavigationInfo({
                title: "",
                navShow: false,
                statusBarStyle: 'LightContent'
            }, () => {
                console.log('点击了导航条中间位置')
            })

            //有导航栏的高度
            // const tabPageHeight = Utils.env.getPageHeight();

            // 如果页面没有导航栏，可以用下面这个计算高度的方法
            const tabPageHeight = weex.config.env.deviceHeight / weex.config.env.deviceWidth * 750;
            const { tabStyles } = this;
            this.contentStyle = { height: (tabPageHeight - tabStyles.height) + 'px' };
            this.pageStyles = { height: tabPageHeight + 'px' }
        },
        methods: {
            wxcTabBarCurrentTabSelected (e) {
                const index = e.page;
                this.pageIndex = index;
                if (index == 0) {
                    this.title = "首页";
                } else if (index == 1) {
                    this.title = "欠费缴费"
                } else {
                    this.title = "我的"
                }
            }
        }
    }
</script>

<style lang="sass" type="text/scss" scoped>
    @import '~Css/index.scss';
    .item-container {
        width: 750px;
        background-color: #f2f3f4;
        align-items: center;
        justify-content: center;
        border-bottom-width: 1px;
        border-bottom-style: solid;
        border-bottom-color: #C5C5C5;
    }

</style>
