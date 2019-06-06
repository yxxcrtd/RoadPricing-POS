<template>
    <div class="container">
        <div class="header">
            <div :style="{'height': statusBarHeight}"></div>
                <div class="search-wrapper">
                    <input class="search-input" type="text" placeholder="请输入车牌号或泊位号"
                           @input="input" ref="input" @change="change">
                    <div class="search-icon" @click="iconClick(searchText)">
                        <text class="icon">&#xe617;</text>
                    </div>
                </div>
        </div>
    </div>
</template>

<script>
    import { WxcMinibar } from 'weex-ui'
    export default {
        name: "index",
        components: { WxcMinibar },
        data() {
            return {
                statusBarHeight: Number.parseInt(weex.config.env.statusBarHeight)+'px',
                searchText: "",
            }
        },
        beforeCreate: function () {
            var domModule = weex.requireModule('dom');
            domModule.addRule('fontFace', {
                'fontFamily': 'iconfont',
                'src': 'url(\'bmlocal://iconfont/iconfont.ttf\')'
            });
        },
        methods: {
            input(event) {
                this.searchText = event.value;
                console.log('oninput=====', event.value)
            },
            change(event) {
            },
            iconClick(str) {
                //收起键盘
                this.$tools.resignKeyboard().then(resData => {
                    // 收起成功的回调
                    this.$emit('iconClicked', str)
                    this.searchText = "";
                }, error => {})
            }
        }
    }
</script>

<style lang="sass" type="text/scss" scoped>
    @import '~Css/index.scss';
    .container {
        width: 750px;
    }
    .header {
        width: 750px;
        @include themeBackground();
    }
    .search-wrapper {
        height: 90px;
        align-items: center;
        justify-content: center;
        position: relative;
    }
    .search-input {
        width: 600px;
        height: 56px;       //56px
        border-width: 1px;
        border-style: solid;
        border-color: #FFFFFF;
        border-radius: 28px;    //28px
        padding-left: 20px;
        font-size: 26px;     //26px
        color: #FFFFFF;
        placeholder-color: #FFFFFF;
        tint-color: #FFFFFF;
        background-color: rgba(255, 255, 255, 0.3);
    }
    .search-icon {
        width: 60px;
        height: 90px;
        text-align: center;
        align-items: center;
        position: absolute;
        right: 80px;
        top: 25px;
    }
    .icon {
        font-family: iconfont;
        font-size: $fontSize_nav_title + 8;
        font-weight: 600;
        color: $fontColor_white;
    }
</style>
