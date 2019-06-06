<template>
    <div class="header">
        <div :style="{'height': statusBarHeight}"></div>
        <wxc-minibar :background-color=null
                     right-button=""
                     @wxcMinibarLeftButtonClicked="minibarLeftButtonClick"
                     @wxcMinibarRightButtonClicked="minibarRightButtonClick">
            <text slot='left' class="icon" >&#xe601;</text>
            <div slot='middle'><text class="icon" >{{title}}</text></div>
        </wxc-minibar>
    </div>
</template>

<script>
    import { WxcMinibar, WxcSearchbar } from 'weex-ui'
    export default {
        name: "header",
        components: { WxcMinibar, WxcSearchbar },
        props: {
          title: ""
        },
        data() {
            return {
                statusBarHeight: Number.parseInt(weex.config.env.statusBarHeight)+'px'
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
            this.$navigator.setNavigationInfo({
                title: "",
                navShow: false,
                statusBarStyle: 'LightContent'
            }, () => {
                console.log('点击了导航条中间位置')
            })
        }
    }
</script>

<style lang="sass" type="text/scss" scoped>
    @import "~Css/index.scss";
    .container {
        width: 750px;
    }
    .header {
        width: 750px;
        display: flex;
        justify-content: center;
        @include themeBackground();
    }
    .icon {
        font-family: iconfont;
        font-size: $fontSize_nav_title;
        font-weight: 600;
        color: $fontColor_white;
    }
</style>
