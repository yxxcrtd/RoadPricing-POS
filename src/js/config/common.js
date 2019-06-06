
export default {
    publicKey: "",

    privateKey: "",

    paramsSort: function (obj) {
        //先用Object内置类的keys方法获取要排序对象的属性名，再利用Array原型上的sort方法对获取的属性名进行排序，newkey是一个数组
        let newkey = Object.keys(obj).sort();
        //创建一个新的对象，用于存放排好序的键值对
        let newObj = {};

        for (let i=0; i<newkey.length; i++) {
            //遍历newkey数组
            newObj[newkey[i]] = obj[newkey[i]];
            //向新创建的对象中按照排好的顺序依次增加键值对
        }

        let sortStr = ""
        for(let key in newObj) {
            sortStr += key + '=' + newObj[key] + '&'
        }
        sortStr = sortStr.substring(0, sortStr.length-1);

        return sortStr;
    }
}
