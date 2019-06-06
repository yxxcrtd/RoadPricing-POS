<template>
    <div class="container">
        <div class="search-wrapper">
            <div>
                <input type="text" class="search-input" placeholder="请输入欠费车辆的车牌号" @input="inputClicked">
            </div>
            <div class="search-icon" @click="iconClicked">
                <text class="icon">&#xe617;</text>
            </div>
        </div>
    </div>
</template>

<script>
    export default {
        name: "index",
        data() {
          return {
              carNumber: ""
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
            inputClicked(event) {
              this.carNumber = event.value;
            },
            iconClicked() {
                this.$emit("iconClicked", this.carNumber)
            }
        }
    }
</script>

<style lang="sass" type="text/scss" scoped>
    @import "~Css/index.scss";
    .container {

    }
    .search-wrapper {
        position: relative;
    }
    .search-input {
        width: 658px;
        height: 102px;
        background-color: $bgColor-white;
        tint-color: $fontColor_theme;
        font-size: $fontSize_info_title_l2;
        color: $fontColor_show_l1;
        placeholder-color: $fontColor_show_l1;
        padding-left: 28px;
        border-width: 4px;
        border-style: solid;
        border-color: $fontColor_theme;
    }
    .search-icon {
        width: 102px;
        height: 102px;
        position: absolute;
        top: 0px;
        right: 0px;
        align-items: center;
        justify-content: center;
        @include themeBackground();
    }
    .icon {
        font-family: iconfont;
        font-size: $fontSize_nav_title + 20;
        font-weight: 600;
        color: $fontColor_white;
    }
</style>
