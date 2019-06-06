<template>
    <div class="container">
        <div class="header">
            <v-header title="历史欠费"></v-header>
        </div>
        <list class="content">
            <cell>
             <v-picker :items="dataList" :isList="true" @pickerClicked2="pickerClicked"></v-picker>
            </cell>
        </list>
    </div>
</template>

<script>
    import VHeader from 'Components/header/header.vue'
    import VPicker from 'Components/picker/index.vue'
    import {myMixins} from "../../../config/mixins";

    export default {
        name: "index",
        components: { VHeader, VPicker },
        mixins: [myMixins],
        data() {
            return {
                dataList: []
            }
        },
        created() {
            this.$router.getParams().then(resData => {
                this.$fetch({
                    method: "POST",
                    name: "arrearsQueryList",
                    data: {
                        carNumber: resData.carNumber,
                        companyId: 1
                    }
                }).then(resData => {
                    console.log("===arrearsQueryList成功返回====", resData)
                    if(resData.code == 200) {
                        this.dataList = resData.data
                    }else {
                        this.$notice.alert({ message: `QureyList-code：${resData.code}, QueryList-message: ${resData.message}` })
                    }
                }, error => {})
            }, error => {})
        },

        methods: {
            pickerClicked(item) {
                this.$fetch({
                    method: "POST",
                    name: "arrearsDetailInfo",
                    data: {
                        id: item.id
                    }
                }).then( resData => {
                    console.log("=====arrearsDetailInfo-resData=====", resData)
                    if (resData.code == 200) {
                        this.$router.open({
                            name: "arrearageDetail",
                            navShow: false,
                            params: resData
                        })
                    }else {
                        this.$notice.alert({ message: `DetailInfo-code：${resData.code}, DetailInfo-message: ${resData.message}` })
                    }
                }, error => {})
            }
        }
    }
</script>

<style lang="sass" type="text/scss" scoped>
    @import '~Css/index.scss';
    .container {
        width: 750px;
        align-items: center;
    }
    .content {
        margin-top: 20px;
        margin-bottom: 20px;
    }

</style>
