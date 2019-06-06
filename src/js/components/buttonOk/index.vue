<template>
    <div class="container">
        <div class="btn" @click="btnClicked">
            <text class="btn-txt">{{title}}</text>
        </div>
    </div>
</template>

<script>
    export default {
        name: "index",
        props: {
            title: {
                type: String,
                default: "确认"
            }
        },
        methods: {
            btnClicked() {
                this.$emit('btnOkClicked')
            }
        }
    }
</script>

<style lang="sass" type="text/scss" scoped>
    @import "~Css/index.scss";
    .container {
        /*align-items: center;*/
    }
    .btn {
        width: 700px;
        height: 100px;
        border-radius: 50px;
        align-items: center;
        justify-content: center;
        @include themeBackground();
    }
    .btn-txt {
        font-size: $fontSize_input;
        font-weight: 600;
        color: $fontColor_white;
    }
</style>
