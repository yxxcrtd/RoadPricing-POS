<template>
    <div class="container">
        <v-header title="入场完成"></v-header>
        <v-finish decText="入场完成"></v-finish>
        <v-btn title="返回首页" @btnOkClicked="btnOkClicked"></v-btn>
        <v-btn-no title="重新打印" @btnNoClicked="print" style="margin-top: 20px;"></v-btn-no>
        <div class="tmp"></div>
    </div>
</template>

<script>
    import VHeader from 'Components/header/header.vue'
    import VFinish from 'Components/finish/index.vue'
    import VBtn from 'Components/buttonOk/index.vue'
    import VBtnNo from 'Components/buttonNo/index.vue'
    import {myMixins} from "../../../config/mixins"

    export default {
        name: "index",
        components: { VHeader, VFinish, VBtn, VBtnNo },
        mixins: [myMixins],
        data() {
            return {
                id: 0
            }
        },
        created() {
            this.$router.getParams().then(resData => {
                this.id = resData
            }, error => {})
        },
        methods: {
            btnOkClicked() {
                this.$router.open({
                    name: "tab",
                    navShow: false
                })
            },

            print(){
                this.printIn(this.id);
            }
        }
    }
</script>

<style scoped>
    .container {
        width: 750px;
        align-items: center;
    }
    .tmp {
        height: 50px;
    }
</style>
