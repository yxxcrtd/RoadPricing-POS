<template>
    <div class="container">
        <div class="header">
            <v-header title="历史欠费"></v-header>
        </div>
        <!--无欠费记录-->
        <div class="content1" v-if="lists.length==0">
            <image class="img_no_arrears" src="bmlocal://assets/no_arrears.jpg"></image>
        </div>
        <!--有欠费记录-->
        <list class="content" v-if="lists.length>0">
            <!--<v-picker :items="lists" :isList="false" :allIcon="allIcon"></v-picker>-->
            <cell>
                <div class="wrapper" v-for="(item, index) in lists" >
                    <div class="icon-wrapper1"  @click="iconClicked(item, index)">
                        <text class="icon-checked icon-checked-true" v-if="item.clicked">&#xe730;</text>
                        <text class="icon-checked icon-checked-false" v-if="!item.clicked">&#xe72f;</text>
                    </div>
                    <div class="picker-wrapper" @click="pickerClicked(item)">
                        <div class="title">
                            <text class="title-txt">{{item.parkingName.substring(0, 6)}}</text>
                            <text class="title-cash">{{item.arrearsAmount}}元</text>
                        </div>
                        <div class="content2">
                            <div class="left">
                                <div class="dec">
                                    <text class="icon">&#xe646;</text>
                                    <text class="dec-txt">入场时间</text>
                                </div>
                                <div class="time">
                                    <text class="time-txt">{{item.enterTime}}</text>
                                </div>
                            </div>
                            <div class="middle-line"></div>
                            <div class="right">
                                <div class="dec">
                                    <text class="icon">&#xe646;</text>
                                    <text class="dec-txt">出场时间</text>
                                </div>
                                <div class="time">
                                    <text class="time-txt">{{item.exitTime}}</text>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </cell>
        </list>
        <div class="footer" v-if="lists.length>0">
            <div class="icon-wrapper" @click="allIconClicked">
                <text class="icon-checked icon-checked-true" v-if="allIcon">&#xe730;</text>
                <text class="icon-checked icon-checked-false" v-if="!allIcon">&#xe72f;</text>
            </div>
            <div class="dec-wrapper" @click="allIconClicked">
                <div class="dec-txt">
                    <text class="footer-txt">全选</text>
                </div>
                <div class="dec-cash">
                    <text class="footer-txt">合计：</text>
                    <text class="money">￥{{this.total}}</text>
                </div>
            </div>
            <div class="btn-wrapper" @click="toPay">
                <text class="btn-txt">去支付</text>
            </div>
        </div>
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
              lists: [],
              allIcon: false,
              total: 0,
              ids: []
          }
        },
        created() {
            this.$router.getParams().then(resData => {
                console.log("=====历史欠费接参=====", resData)
                for (let i=0; i<resData.length; i++) {
                    resData[i].clicked = false;
                }
                this.lists = resData;
            }, error => {})
        },
        methods: {
            //单选
            iconClicked(item, index) {
                this.lists[index].clicked = !item.clicked;
                if(this.lists[index].clicked) {
                    this.ids = this.ids.concat(item.id);
                }
                this.isAll();
                this.totalMoney();
            },
            //全选
            allIconClicked() {
                this.allIcon = !this.allIcon;

                for(let i=0; i<this.lists.length; i++) {
                    this.lists[i].clicked = this.allIcon;
                }

                if(this.allIcon){
                    this.ids = [];
                    for(let i=0; i<this.lists.length; i++) {
                        this.ids = this.ids.concat(this.lists[i].id);
                    }
                }

                this.totalMoney();
            },
            //点击卡片
            pickerClicked(item) {
                console.log("=====历史欠费传参=====", item)
                this.$fetch({
                    method: "POST",
                    name: "arrearsDetailInfo",
                    data: {
                        id: item.id
                    },
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
            },
            toPay() {
                console.log("=====去支付传参=====", this.total)
                if(this.total <=0) {
                    this.$notice.alert({
                        message: "请选择需要支付的欠费记录"
                    })
                }else {
                    this.$router.open({
                        name: "payType",
                        navShow:false,
                        params: {
                            orderType: 2,
                            total: this.total,
                            id: this.ids.toString()
                        }
                    })
                }
            },
            //=========
            isAll() {
                let count = 0;
                for(let i=0; i<this.lists.length; i++) {
                    if(this.lists[i].clicked) {
                        count+=1;
                    }else {
                        count-=1;
                    }
                }
                if(count == this.lists.length) {
                    this.allIcon = true;
                }else {
                    this.allIcon = false;
                }
            },
            totalMoney() {
                let sum = 0
                for(let i=0; i<this.lists.length; i++) {
                    if(this.lists[i].clicked) {
                        sum += this.lists[i].arrearsAmount;
                    }
                }
                this.total = sum;
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
        position: absolute;
        width: 750px;
        top: 160px;
        bottom: 120px;
        flex-direction: column;
        align-items: center;
        justify-content: center;
    }
    .footer {
        position: absolute;
        left: 0;
        right: 0;
        bottom: 0;

        height: 120px;
        background-color: $fontColor_white;

        flex-direction: row;
        align-items: center;

        border-top-width: 1px;
        border-top-style: solid;
        border-top-color: #C5C5C5;
    }
    .content1 {
        height: 600px;
        align-items: center;
        justify-content: center;
    }
    .img_no_arrears{
        width: 500px;
        height: 200px;
    }
    .icon-wrapper {
        width: 65px;
        height: 120px;
        align-items: flex-end;
        justify-content: center;
    }
    .icon-checked {
        font-family: iconfont;
        font-size: 40px;
        font-weight: 600;
    }
    .icon-checked-true {
        color: $fontColor_theme;
    }
    .icon-checked-false {
        color: $fontColor_show_l3;
    }
    .dec-wrapper {
        flex: 1;
        height: 120px;
        margin-left: 30px;
        margin-right: 30px;
        flex-direction: row;
        align-items: center;
        justify-content: space-between;
    }
    .dec-cash {
        flex-direction: row;
        align-items: center;
        justify-content: space-between;
    }
    .btn-wrapper {
        width: 198px;
        height: 120px;
        background-color: $fontColor_theme;
        align-items: center;
        justify-content: center;
        border-top-width: 1px;
        border-top-style: solid;
        border-top-color: $fontColor_theme;
    }
    .footer-txt {
        font-size: $fontSize_detail;
        color: $fontColor_show_l3;
    }
    .money {
        font-size: $fontSize_info_title_l2;
        color: $fontColor_theme;
    }
    .btn-txt {
        font-size: $fontSize_plate;
        color: $fontColor_white;
    }
    .wrapper {
        width: 750px;
        padding-left: 20px;
        padding-right: 20px;
        flex-direction: row;
    }
    .icon-wrapper1 {
        width: 60px;
        height: 198px;
        align-items: flex-start;
        justify-content: center;
    }
    .icon-checked {
        font-family: iconfont;
        font-size: 40px;
        font-weight: 600;
    }
    .icon-checked-true {
        color: $fontColor_theme;
    }
    .icon-checked-false {
        color: $fontColor_show_l3;
    }
    .picker-wrapper {
        flex: 1;
        height: 198px;
        margin-bottom: 50px;
        flex-direction: column;
        align-items: center;
        justify-content: center;
        border-radius: 10px;
        background-color: $bgColor-white;
    }
    .title{
        width: 580px;
        flex-direction: row;
        align-items: center;
        justify-content: space-between;
    }
    .title-txt, .title-cash {
        font-size: $fontSize_info_title_l1;
        color: $fontColor_info_title_l1;
    }
    .content2 {
        flex-direction: row;
        align-items: center;
        justify-content: center;
    }
    .left, .right {
        width: 260px;
        height: 100px;
        justify-content: space-around;
    }
    .dec {
        flex-direction: row;
    }
    .icon {
        flex-basis: 40px;
    }
    .dec-txt {
        flex: 1;
        font-size: $fontSize_show - 6;
        color: $fontColor_detail;
    }
    .icon {
        font-family: iconfont;
        font-size: $fontSize_nav_title - 6;
        font-weight: 600;
        color: $fontColor_theme;
    }
    .time-txt {
        font-size: $fontSize_show - 6;
        color: $fontColor_info_title_l2;
    }
    .middle-line {
        width: 2px;
        height: 70px;
        margin-left: 30px;
        margin-right: 30px;
        background-color: #DDDDDD;
    }
</style>
