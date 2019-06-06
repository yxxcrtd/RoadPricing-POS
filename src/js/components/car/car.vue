<template>
    <div>
        <div class="container">
            <div class="wrapper" v-for="(item, index) in items">
                <text v-if="item.status==0" class="nocar">暂无车辆</text>
                <div class="test" style="flex-direction: row; align-items: center; justify-content: space-between;">
                    <text v-if="item.status==1" class="plate">{{item.carNumber}}</text>
                    <div class="arrearage" v-if="item.totalArrearsAmount>0 || item.totalArrearsAmount" @click="arrearageClicked(item)">
                        <text class="arrearage-text">欠</text>
                    </div>
                </div>
                <div class="car-icon-wrapper">
                    <image v-if="item.status==0" class="car-icon" src="bmlocal://assets/car-disable.png"
                           alt="" @click="putInClicked(item)"></image>
                    <image v-if="item.status==1" class="car-icon" src="bmlocal://assets/car-enable.png"
                           alt="" @click="putOutClicked(item)"></image>
                </div>
                <text class="parking">{{item.code}}</text>
            </div>
        </div>
    </div>
</template>

<script>
    export default {
        name: "car",
        props: {
          items: {
              type: Array,
              default: []
          }
        },
        methods: {
            putInClicked(item) {
                this.$emit("putInClicked", item)
            },
            putOutClicked(item) {
                this.$emit("putOutClicked", item)
            },
            arrearageClicked(item) {
                this.$emit("arrearageClicked", item)
            }
        }
    }
</script>

<style lang="sass" type="text/scss" scoped>
    @import "~Css/index.scss";
    .container {
        width: 750px;
        display: flex;
        flex-direction: row;
        flex-wrap: wrap;
        position: relative;

    }
    .wrapper {
        width: 340px;
        margin-top: 20px;
        margin-left: 15px;
        margin-right: 15px;
        /*padding-top: 16px;*/
        background-color: $bgColor-white;
        border-radius: 10px;
        @include cardShadow();
        display: flex;
        align-items: center;
    }
    .parking {
        font-size: $fontSize_info_title_l2;
        font-weight: 600;
        color: $fontColor_berth;
        line-height: 50px;
        margin-top: 16px;
    }
    .car-icon-wrapper {
        width: 296px;
        height: 128px;
        display: flex;
        align-items: center;
        justify-content: center;
    }
    .car-icon {
        width: 260px;
        height: 100px;
    }
    .plate {
        font-size: $fontSize_berth;
        font-weight: 600;
        color: $fontColor_plate;
        line-height: 90px;
    }
    .nocar {
        font-size: $fontSize_info_title_l2;
        font-weight: 600;
        color: $fontColor_plate;
        line-height: 90px;
    }
    .btn {
        width: 164px;
        height: 56px;
        border-radius: 50%;
        align-items: center;
        justify-content: center;
        @include themeBackground();
    }
    .btn-text {
        font-size: 30px;
        color: $fontColor_white;
    }
    .arrearage {
        width: 56px;
        height: 56px;
        border-radius: 50%;
        @include themeBackground();

        /*position: absolute;*/
        /*top: 12px;*/
        /*right: 12px;*/

        align-items: center;
        justify-content: center;
    }
    .arrearage-text {
        font-size: $fontSize_show;
        font-weight: 600;
        color: $fontColor_white;
    }
</style>
