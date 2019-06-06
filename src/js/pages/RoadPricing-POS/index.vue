<template>
    <div>
        <div class="container" :style="{'height': pageHeight}">
            <div class="logo-wrapper">
                <div class="logo">
                    <image style="width: 128px; height: 128px;" src="bmlocal://assets/group.png"></image>
                </div>
                <div class="name">
                    <text class="name-text">占道收费系统</text>
                </div>
            </div>
            <div class="login-wrapper">
                <div class="username">
                    <input type="text" class="input input-noIcon"
                           placeholder="收费员编号"
                           style="placeholder-color: #FFFFFF; tint-color: #FFFFFF;"
                           @change="onchangeUsername">
                </div>
                <div class="password">
                    <input type="password" class="input"
                           placeholder="密码"
                           style="placeholder-color: #FFFFFF; tint-color: #FFFFFF;"
                           @change="onchangePassword">
                    <text class="icon">&#xe669;</text>
                </div>
                <div class="login-btn" @click="onLogin">
                    <text class="login-btn-text">登录</text>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    import Config from 'Config/common.js'
    import Crypt from 'Config/Crypt.js'
    import { myMixins } from "../../config/mixins";

    var bmRsa = weex.requireModule('bmRsa')

    export default {
        name: "index.vue",
        mixins: [myMixins],
        data() {
            return {
                username: "AH001",
                password: "123456",
                serverPublicKey: "",
                lat: "",
                lng: ""
            }
        },
        created() {
            this.$navigator.setNavigationInfo({
                title: "",
                navShow: false,
                statusBarStyle: 'LightContent'
            }, () => {
                console.log('点击了导航条中间位置')
            });
            //location
            this.$geo.get().then(data => {
                console.log('========Geo成功=========',data)
                this.lat = data.locationLat;
                this.lng = data.locationLng;
            }, error => {})
            //生成本地的rsa
            bmRsa.rsaKeys(res=>{
                console.log("=======++++++++", res)
                Config.publicKey = res.publicKey;
                Config.privateKey = res.privateKey;
            })
            //rsa请求
            this.$fetch({
                method: "POST",
                name: "rsa",
            }).then(resData => {
                //成功
                console.log("=====rsa成功返回====", resData)
                if(resData.code == 200) {
                    Config.serverPublicKey = resData.data.publicKey;
                }else {
                    this.$notice.alert({ message: `rsa-code：${resData.code}, rsa-message: ${resData.message}`})
                }
            }, error => {})
        },

        methods: {
            onchangeUsername(event) {
                this.username = event.value;
            },
            onchangePassword(event) {
                this.password = event.value;
            },
            onLogin() {
                //获取登录密文
                console.log("====deviceId====", this.deviceId)
                let cryptText = Crypt.encrypt(this.deviceId, this.username, this.password);
                // console.log("====deviceId====", this.deviceId)
                //发送登录请求
                this.$fetch({
                    method: 'POST',
                    name: 'login',
                    data: {
                        cipherText: cryptText,
                        // cipherText: '12ijfsdjfweuioffsd',
                        location: this.lat + ',' + this.lng,
                        publicKey: Config.publicKey,
                        timestamp: new Date().getTime()
                    }
                }).then(resData => {
                    if (resData.code == 200) {
                        let decryptText = Crypt.decrypt(resData.data.cipherText);
                        let decryptTextArr = decryptText.split('&');
                        console.log("=====login成功返回====", resData)
                        this.$event.emit('reportLocation', {
                            reportTimes: resData.reportTimes,
                            sessionId: decryptTextArr[0]
                        });
                        //
                        // let data = resData.data;
                        // data.remove('cipherText');
                        // data.sessionId = decryptTextArr[0];
                        // data.token =decryptTextArr[1];

                        this.$storage.set('loginInfo', {
                            sessionId: decryptTextArr[0],
                            token: decryptTextArr[1],
                            //companyId: resData.data.companyId,
                            collectorName: resData.data.collectorName,
                            parkingName: resData.data.parkingName,
                            parkingId: resData.data.parkingId,
                            parkingAddress: resData.data.parkingAddress,
                            subParkingId: resData.data.subParkingId,
                            deviceType: resData.data.deviceType
                        }
                        )
                        .then(resData => {
                            //存储成功
                            this.$router.open({
                                name: 'tab',
                                navShow: false,
                                canBack: false
                            })
                        }, error => {})
                    } else { this.$notice.alert({ message: `登录失败：${resData.code}：${resData.message}`})}
                }, error => {})
            }
        }
    }
</script>

<style lang="sass" type="text/scss" scoped>
    @import '~Css/index.scss';

    .container {
        width: 750px;
        @include themeBackground();
    }
    .logo-wrapper {
        width: 750px;
        margin-top: 160px;
        margin-bottom: 70px;
        display: flex;
        align-items: center;
    }
    .name-text {
        font-size: $fontSize_APP;
        color: $fontColor_white;
        font-weight: 600;
        line-height: 100px;
    }
    .login-wrapper {
        display: flex;
        align-items: center;
    }
    .roadId, .username, .password {
        width: 636px;
        height: 130px;
        border-bottom-width: 1px;
        border-bottom-style: solid;
        border-bottom-color: $fontColor_white;

        display: flex;
        flex-direction: row;
        align-items: center;
        justify-content: space-around;
    }
    .input {
        width: 580px;
        height: 130px;
        font-size: $fontSize_input;
        color: $fontColor_white;
    }
    .input-noIcon {
        width: 636px;
    }
    .icon {
        font-family: iconfont;
        font-size: $fontSize_info_title_l2;
        font-weight: 600;
        color: $fontColor_white;
    }
    .login-btn {
        width: 636px;
        height: 100px;
        margin-top: 100px;

        background-color: $bgColor-white;
        border-radius: 64px;

        align-items: center;
        justify-content: center;
    }
    .login-btn-text {
        font-size: $fontSize_input;
        color: $fontColor_theme;
        font-weight: 600;
    }
</style>
