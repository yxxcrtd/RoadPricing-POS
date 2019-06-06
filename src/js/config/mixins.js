import md5 from 'js-md5'
let bmPt = weex.requireModule('bmPt')
let bmP990Pt = weex.requireModule('bmP990Pt')
export let myMixins = {
    data() {
        return {
            statusBarHeight: Number.parseInt(weex.config.env.statusBarHeight)+'px',
            navBarHeight: Number.parseInt(weex.config.env.navBarHeight)+'px',
            pageHeight: Number.parseInt(weex.config.env.deviceHeight/weex.config.env.deviceWidth*750)+'px',
            deviceId: weex.config.env.deviceId,

            sessionId: "",
            Signature: "",
            token: "",
            token2md5: "",
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
        //入场打印
        printIn(id) {
            this.$storage.get('loginInfo')
                .then(resData => {
                    let deviceType = resData.deviceType;

                    this.$fetch({
                        method: "POST",
                        name: "printEnter",
                        data: {
                            vehicleEntranceRecordId: id
                        }
                    }).then(resData=>{
                       if(deviceType) {
                           bmP990Pt.btPrint(JSON.stringify(resData.data))
                       }else {
                           console.log("=====printEnter成功返回=====", resData);
                           bmPt.btPrint(JSON.stringify({
                               bluetoothID: 'DC:0D:30:28:EF:8F',
                               content: JSON.stringify(resData.data)
                           }), res=>{
                               console.log('=====蓝牙打印结果=====', res)
                           })
                       }
                    }, error=>{});
                }, error => {});
        },
        //订单打印
        print(id) {
            this.$storage.get('loginInfo')
                .then(resData => {
                    let deviceType = resData.deviceType;

                    this.$fetch({
                        method: "POST",
                        name: "printOrder",
                        data: {
                            orderId: id
                        }
                    }).then(resData => {
                        if(deviceType) {
                            bmP990Pt.btPrint(JSON.stringify(resData.data))
                        }else {
                            console.log("=====printOrder成功返回=====", resData);
                            bmPt.btPrint(JSON.stringify({
                                bluetoothID: 'DC:0D:30:28:EF:8F',
                                content: JSON.stringify(resData.data)
                            }), res => {
                                console.log('=====蓝牙打印结果=====', res)
                            })
                        }
                    }, error => {});
                }, error => {});

        },

        //出场打印
        printExit(id) {
            this.$storage.get('loginInfo')
                .then(resData => {
                    let deviceType = resData.deviceType;

                    this.$fetch({
                        method: "POST",
                        name: "printExit",
                        data: {
                            vehicleEntranceRecordId: id
                        }
                    }).then(resData => {
                        if(deviceType) {
                            bmP990Pt.btPrint(JSON.stringify(resData.data))
                        }else {
                            console.log("=====printOrder成功返回=====", resData);
                            bmPt.btPrint(JSON.stringify({
                                bluetoothID: 'DC:0D:30:28:EF:8F',
                                content: JSON.stringify(resData.data)
                            }), res => {
                                console.log('=====蓝牙打印结果=====', res)
                            })
                        }
                    }, error => {});
                }, error => {});

        },
    }
}

