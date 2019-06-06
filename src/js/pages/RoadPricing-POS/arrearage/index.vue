<template>
    <div class="container">
        <div class="header">
            <v-header title="欠费补缴"></v-header>
        </div>
        <div class="content">
            <v-search-bar @iconClicked="iconClicked"></v-search-bar>
        </div>
    </div>
</template>

<script>
    import VHeader from 'Components/header/header.vue'
    import VSearchBar from 'Components/searchBar/index.vue'

    export default {
        name: "home",
        components: { VHeader, VSearchBar },
        methods: {
            iconClicked(carNumber) {
                this.$fetch({
                    method: "POST",
                    name: "arrearsQueryList",
                    data: {
                        carNumber: carNumber,
                        companyId: 1
                    },
                }).then(resData => {
                    console.log("=====arrearsQueryList成功返回=====", resData)
                    if (resData.code === 200) {
                        this.$router.open({
                            name: "arrearageHistory",
                            navShow: false,
                            params: resData.data
                        })
                    }else {
                        this.$notice.alert({ message: `QueryList-code：${resData.code}, QueryList-message: ${resData.message}` })
                    }
                }, error => {})
            }
        }
    }
</script>

<style lang="sass" type="text/scss" scoped>
    .container {
        position: absolute;
        left: 0;
        right: 0;
        top: 0;
        bottom: 0;
    }
    .content {
        height: 500px;
        align-items: center;
        justify-content: center;
    }
</style>
