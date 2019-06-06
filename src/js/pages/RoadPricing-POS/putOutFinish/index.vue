<template>
    <div class="container">
        <v-header title="出场完成"></v-header>
        <v-finish decText="缴费完成"></v-finish>
        <v-btn title="返回首页" @btnOkClicked="btnOkClicked"></v-btn>
        <div class="tmp"></div>
        <v-btn-no title="重新打印" @btnNoClicked="btnNoClicked"></v-btn-no>
    </div>
</template>

<script>
    import VHeader from 'Components/header/header.vue'
    import VFinish from 'Components/finish/index.vue'
    import VBtn from 'Components/buttonOk/index.vue'
    import VBtnNo from 'Components/buttonNo/index.vue'
    import {myMixins} from "../../../config/mixins";

    var bmPt = weex.requireModule('bmPt')
    export default {
        name: "index",
        components: { VHeader, VFinish, VBtn, VBtnNo },
        mixins: [myMixins],
        data() {
            return {
                url: "",
                timer: ""
            }
        },
        methods: {
            btnOkClicked() {
                this.$router.open({
                    name: "tab",
                    navShow: false
                })
            },
            btnNoClicked() {
               this.$router.getParams().then(resData=>{
                   console.log("=========",resData);
                   if(resData.vehicleEntranceRecordId) {
                       this.printExit(resData.vehicleEntranceRecordId)
                   }else {
                       this.print(resData)
                   }
               },error=>{})
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
