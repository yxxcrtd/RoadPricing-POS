<template>
    <div class="container">
        <div class="icon-wrapper">
            <!--<text class="icon">&#xe730;</text>-->
            <image class="finish-img" src="bmlocal://assets/finish.jpg"></image>
            <text class="dec-txt">{{decText}}</text>
        </div>
    </div>
</template>

<script>
    export default {
        name: "index",
        props: {
          decText: {
              type: String,
              default: ""
          }
        },
        beforeCreate: function () {
            var domModule = weex.requireModule('dom');
            domModule.addRule('fontFace', {
                'fontFamily': 'iconfont',
                'src': 'url(\'bmlocal://iconfont/iconfont.ttf\')'
            });
        },
    }
</script>

<style lang="sass" type="text/scss" scoped>
    @import "~Css/index.scss";
    .container {
        width: 750px;
    }
    .icon-wrapper {
        height: 700px;
        align-items: center;
        justify-content: center;
    }
    .finish-img {
        width: 360px;
        height: 360px;
        margin-bottom: 30px;
    }
    .icon {
        font-family: iconfont;
        font-size: 300px;
        font-weight: 600;
        color: $fontColor_theme;
    }
    .dec-txt {
        font-size: $fontSize_info_title_l2;
        color: $fontColor_info_title_l2;
    }
</style>
