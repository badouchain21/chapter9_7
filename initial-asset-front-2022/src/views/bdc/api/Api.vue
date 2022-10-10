<template>
    <div class="api-container">
        <div class="api-content">
            <div v-for="item in detail" :key="item.index" class="api-content-desc" :id="'content' + item.index">
                <div class="api-content-info">
                    <h3 class="api-title">{{item.name}}</h3>
                    <p class="api-explain font-18">接口请求说明</p>
                    <hr class="api-hr">
                    <p class="api-method font-14">请求方式：
                        <span class="desc-color">{{item.requestType}}</span>
                    </p>
                    <p class="font-14">{{item.requestUrl}}</p>
                </div>
                <!-- <table class="api-content-table" width="100%">
                    <p class="font-18">参数说明</p>
                    <tbody style="display: block;maxHeight: 350px; min-height: 100px;overflowY: scroll;">
                        <tr>
                            <th width="23%">参数</th>
                            <th width="23%">类型</th>
                            <th width="24%">是否必填</th>
                            <th width="30%">说明</th>
                        </tr>
                        <tr v-for="(reqItem, index) in item.requestParam" :key="index">
                            <td>{{reqItem.name}}</td>
                            <td>{{reqItem.type}}</td>
                            <td>{{reqItem.isRequired}}</td>
                            <td class="desc-color">{{reqItem.description}}</td>
                        </tr>
                    </tbody>
                </table> -->
                <div width="100%" style="maxHeight: 500px; overflow: scroll; marginBottom: 1rem;">
                    <div style="display: flex;">
                        <p style="width: 23%; margin: 0; background: #666a69; color: #fff; padding: 18px 10px;">参数</p>
                        <p style="width: 23%; margin: 0; background: #666a69; color: #fff; padding: 18px 10px;borderLeft: 1px solid #f2f2f2;">类型</p>
                        <p style="width: 24%; margin: 0; background: #666a69; color: #fff; padding: 18px 10px;borderLeft: 1px solid #f2f2f2;">是否必填</p>
                        <p style="width: 30%; margin: 0; background: #666a69; color: #fff; padding: 18px 10px;borderLeft: 1px solid #f2f2f2;">说明</p>
                    </div>
                    <div v-for="(reqItem, index) in item.requestParam" :key="index" style="display: flex;">
                        <p style="width: 23%; margin: 0; padding: 18px 10px;border: 1px solid #f2f2f2; borderTop: none;">{{reqItem.name}}</p>
                        <p style="width: 23%; margin: 0; padding: 18px 10px;border: 1px solid #f2f2f2; borderLeft: none; borderTop: none;">{{reqItem.type}}</p>
                        <p style="width: 24%; margin: 0; padding: 18px 10px;border: 1px solid #f2f2f2; borderLeft: none; borderTop: none;">{{reqItem.isRequired}}</p>
                        <p class="desc-color" style="width: 30%; margin: 0; padding: 18px 10px;border: 1px solid #f2f2f2; borderLeft: none; borderTop: none;">{{reqItem.description}}</p>
                    </div>
                </div>
                <table class="api-content-table" width="100%">
                    <p class="font-18">服务器响应</p>
                    <tbody>
                        <tr>
                            <th width="23%">参数</th>
                            <th width="23%">类型</th>
                            <th width="54%">说明</th>
                        </tr>
                        <tr v-for="(resItem, index) in item.responseParam" :key="index">
                            <td>{{resItem.name}}</td>
                            <td>{{resItem.type}}</td>
                            <td  class="desc-color">{{resItem.description}}</td>
                        </tr>
                    </tbody>
                </table>
                <table class="api-content-table" width="100%" v-if="item.returnDataParam">
                    <p class="font-18">返回数据说明</p>
                    <p class="a-color handleFold" @click="handleTableFold($event)">点击展开(收起)详细数据说明</p>
                    <tbody class="content-table-fold" style="maxHeight: 400px; overflowY: scroll;">
                        <tr>
                            <th width="23%">参数</th>
                            <th width="23%">类型</th>
                            <th width="54%">说明</th>
                        </tr>
                        <tr v-for="(dataItem, index) in item.returnDataParam" :key="index">
                            <td>{{dataItem.name}}</td>
                            <td>{{dataItem.type}}</td>
                            <td  class="desc-color">{{dataItem.description}}</td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>
        <div class="api-nav">
            <li id="down-btn" class="down-btn" @click="downloadHTML">下载页面</li>
            <li v-for="(item, index) in menu" :key="index" class="api-nav-li" :class="clickIndex == index ? 'api-nav-li_active': ''">
                <a :href="'#content' + index" @click="handleLiClick(index)">{{item}}</a>
            </li>
        </div>
    </div>    
</template>

<script>
export default {
    data () {
        return {
            clickIndex: 0,
            menu: [],
            detail: [],
            queryID: '',
            isLock: false
        }
    },
    mounted () {
         this.get(this.INTERFACE.allApiUrl + this.queryID, {}).then(res => {
           if(res.hasOk)
                res = res.bean
                console.log(res)
                this.menu = res.menu
                this.detail = res.detail
                this.loadingClose()
           })
        },
    created () {
        this.queryID = this.$route.params.id
    },
    methods: {
        handleLiClick (index) {
            this.clickIndex = index
        },
        handleTableFold (ele) {
            if (this.isLock) {
                return
            }
            const target = ele.target.nextElementSibling
            // 设置高度和透明度 实现过渡
            const currentHeight = getComputedStyle(target, null).height
            const currentOpacity = getComputedStyle(target, null).opacity
            target.style.height = currentHeight === '0px' ? 'auto' : '0'
            // 解决点击过快 opactiy值异常无法正常判断的问题
            target.style.opacity = currentOpacity > 0 ? '0' : '1'
            this.isLock = true
            setTimeout(() => {
                this.isLock = false
            }, 450)
        },
        downloadHTML () {
            // 下载时往body添加script脚本，用于原生控制dom交互效果
            this.addScript()
            let style = this.addStyle()
            let value = document.documentElement.innerHTML
            const reg = /<li id="down-btn" class="down-btn">下载页面<\/li>/gi
            value = value.replace(reg, '')
            
            let funDownload = function (content, filename) {
                let eleLink = document.createElement('a');
                eleLink.download = filename;
                eleLink.style.display = 'none';
                // 字符内容转变成blob地址
                let blob = new Blob([content]);
                eleLink.href = URL.createObjectURL(blob);
                // 模拟点击生成文件
                document.body.appendChild(eleLink);
                eleLink.click();
                document.body.removeChild(eleLink);
                // 删除新加的style 避免影响页面
                style.innerHTML = ""
            }

            funDownload(value, 'API接口页面.html');    
        },
        addScript () {
            const script = document.createElement('script')
            script.type = 'text/javascript'
            script.innerText = `let isLock = false;const navList = document.querySelectorAll(".api-nav-li"); navList.forEach(item => {item.children[0].addEventListener('click', function() {document.querySelector('.api-nav-li_active').className='api-nav-li';item.className='api-nav-li api-nav-li_active'})});const allClick = document.querySelectorAll(".handleFold");allClick.forEach(item => {item.addEventListener('click',function () {if (isLock) {return}isLock = true;const target = item.nextElementSibling.children[0];const currentHeight = getComputedStyle(target, null).height;const currentOpacity = getComputedStyle(target, null).opacity;target.style.height = currentHeight === '0px' ? 'auto' : '0';target.style.opacity = currentOpacity > 0 ? '0' : '1';setTimeout(()=> {isLock = false}, 450)})})`
            document.body.appendChild(script)
        },
        addStyle () {
            const style = document.createElement('style')
            style.type = 'text/css'
            style.innerHTML = `body,html{background-color:#f2f2f2;scroll-behavior:smooth;}ol,ul,li{list-style-type:none;}a{text-decoration:none;}a:hover{text-decoration:none;}.desc-color{color:#C55911;}.a-color{color:#3382ef;cursor:pointer;}.font-18{font-size:18px;}.font-14{font-size:14px;word-break:break-all}.down-btn{position: fixed;top:0;left:67%;width:140px;text-align:center;margin-top:10px;padding:5px 0;color:#fff;font-size:14px;border-radius:4px;background:#3382ef;cursor:pointer;}.api-container{position:relative;width:65%;margin:0 auto;overflow:hidden;}.api-container .api-content{float:left;width:70%;background:#fff;padding:30px;}.api-container .api-content .api-content-desc{margin-bottom:4rem;}.api-container .api-content .api-title{font-size:30px;line-height:1.5;}.api-container .api-content .api-explain{margin:15px 0;}.api-container .api-content .api-method{margin-bottom:0;}.api-container .api-content .api-content-table{margin-bottom:1rem;border-spacing:0;}.api-container .api-content .api-content-table  th{background:#666a69;color:#fff;font-weight:normal;}.api-container .api-content .api-content-table th,td{font-size:14px;padding:18px 10px;border:1px solid #f2f2f2;}.api-container .api-content .content-table-fold{display:block;transition:all .4s;}.api-container .api-nav{position:fixed;top:6%;left:70%;max-height:650px;overflow-y:scroll;background:#fff;border-radius:4px;padding:10px;}.api-container .api-nav .api-nav-li{padding:0 10px;line-height:2;min-width:200px;border-left:2px solid #c9c9c9;}.api-container .api-nav  .api-nav-li a{color:#333;font-size:14px;}.api-container .api-nav  .api-nav-li a:hover{text-decoration:none;}.api-container .api-nav .api-nav-li_active{border-color:#3382ef;}.api-container .api-nav .api-nav-li_active a{color:#3382ef;}`
            document.getElementsByTagName('head')[0].appendChild(style);
            return style
        }
    }
}
</script>

<style lang="scss">
    .test {
        position: relative;
    }
    body,
    html {
        background-color: #f2f2f2;
        scroll-behavior: smooth;
        // ie 不支持该属性
    }
    .desc-color {
        color: #C55911;
    }
    .a-color {
        color: #3382ef;
        cursor: pointer;
    }
    .font-18 {
        font-size: 18px;
    }
    .font-14 {
        font-size: 14px;
        word-break: break-all
    }
    .down-btn {
        position: fixed;
        top: 0;
        left: 67%;
        width: 140px;
        text-align: center;
        margin-top: 10px;
        padding: 5px 0;
        color: #fff;
        font-size: 14px;
        border-radius: 4px;
        background: #3382ef;
        cursor: pointer;
    }
    .api-container {
        position: relative;
        width: 65%;
        margin: 0 auto;
        overflow: hidden;
        .api-content {
            float: left;
            width: 70%;
            background: #fff;
            padding: 30px;
            .api-content-desc {
                margin-bottom: 4rem;
            }
            .api-title {
                font-size: 30px;
                line-height: 1.5;
            }
            .api-hr {
                border-width: 2px;
            }
            .api-explain {
                margin: 15px 0;
            }
            .api-method {
                margin-bottom: 0;
            }
            .api-content-table {
                margin-bottom: 1rem;
                th {
                    background: #666a69;
                    color: #fff;
                    font-weight: normal;
                }
                th, td {
                    font-size: 14px;
                    padding: 18px 10px;
                    border: 1px solid #f2f2f2;
                }
            }
            .content-table-fold {
                // display 为block才可以设置 height = 0
                display: block;
                // height: 0;
                // opacity: 0;
                transition: all .4s;
            }
        }
        .api-nav {
            position: fixed;
            top: 6%;
            left: 65%;
            max-height: 650px;
            overflow: scroll;
            background: #fff;
            border-radius: 4px;
            padding: 10px;
            .api-nav-li {
                padding: 0 10px;
                line-height: 2;
                min-width: 200px;
                border-left: 2px solid #c9c9c9;
                a {
                    color: #333;
                    font-size: 14px;
                    &:hover {
                        text-decoration: none;
                    }
                }
            }
            .api-nav-li_active {
                border-color: #3382ef;
                a {
                    color: #3382ef;
                }
            }
        }
    }
</style>