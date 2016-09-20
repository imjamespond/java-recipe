/**
 * @fileoverview 在jQuery基础上通过扩展方式编写的公用函数，必须引入jQuery.js.
 * @author <a href="mailto:zjlmbm@gmail.com">Jianlin.Zhu</a>
 * @library marry5 js framework
 * @version 0.1
 * @inherits jQuery.js
 */

(function($) {

    if (window.HTMLElement) {    // 让firefox支持outerHTML
        HTMLElement.prototype.__defineSetter__("outerHTML", function (sHTML) {
            var r = this.ownerDocument.createRange();
            r.setStartBefore(this);
            var df = r.createContextualFragment(sHTML);
            this.parentNode.replaceChild(df, this);
            return sHTML;
        });
        HTMLElement.prototype.__defineGetter__("outerHTML", function () {
            var attr;
            var attrs = this.attributes;
            var str = "<" + this.tagName.toLowerCase();
            for (var i = 0; i < attrs.length; i++) {
                attr = attrs[i];
                if (attr.specified) {
                    str += " " + attr.name + "=\"" + attr.value + "\"";
                }
            }
            if (!this.canHaveChildren) {
                return str + ">";
            }
            return str + ">" + this.innerHTML + "</" + this.tagName.toLowerCase() + ">";
        });
        HTMLElement.prototype.__defineGetter__("canHaveChildren", function () {
            switch (this.tagName.toLowerCase()) {
                case "area":
                case "base":
                case "basefont":
                case "col":
                case "frame":
                case "hr":
                case "img":
                case "br":
                case "input":
                case "isindex":
                case "link":
                case "meta":
                case "param":
                    return false;
            }
            return true;
        });
    }

    $.extend({
        /**
         * 相当于document.getElementById,因为用jQuery的$返回的是jQuery对象,
         * 要获取html对象必须调用get方法获取通过数组索引访问,所以写了这个函数进行封装.
         *
         * @function
         * @name $.$
         * @example <code>
         * $.$("div1");
         * </code>
         * @param {String} id dom对象的id.
         * @return {Object} dom html对象.
         */
        $    :    function(id) {
            return document.getElementById(id);
        },

        core:    {
            /**
             * ajax 异步请求
             * @param type - GET 或 POST , 注意必须大写
             * @param url - 路径
             * @param data - 参数
             * @param dataType - 返回格式
             * @param successCallback
             * @param completeCallback
             * @param errorCallback
             */
            ajaxBase : function(type,url,data,dataType,successCallback,completeCallback,errorCallback){
                $.ajax({
                    type: type,
                    url: url,
                    data: data,
                    dataType: dataType,
                    success: successCallback,
                    complete: completeCallback,
                    error: errorCallback
                });
            },
            ajaxAsync : function(type,url,data,dataType,successCallback,completeCallback,errorCallback){
                $.ajax({
                    type: type,
                    url: url,
                    data: data,
                    async : false,
                    dataType: dataType,
                    success: successCallback,
                    complete: completeCallback,
                    error: errorCallback
                });
            },
            /**
             * 用正则表达式判断str里面是否包含某个字符串string,相当于String.indexOf,用正则表达式对大字符串搜索效率较高.
             *
             * @function
             * @name $.inString
             * @example <code>
             * var str = "gif,jpg,bmp";
             * var string = "gif";
             * alert($.inString(str, string));    // true
             * </code>
             * @param {String} str 字符串.
             * @param {String} string 要搜索的子字符串.
             * @return {Boolean} true - str里面包含有string; false - str里面没有string.
             */
            inString    :    function(str, string) {
                var regexp = new RegExp(str, "ig");
                return (string.search(regexp) == -1) ? false : true;
            },
            focusCHK: function autoCHK(obj) {
                $("#autoCHK" + $(obj).attr("itemVal")).attr("checked", true);
            },

            /**
             * 判断数组或者对象是否为空.
             *
             * @function
             * @name $.isEmpty
             * @example <code>
             * var arr = [];
             * var obj = {a:1};
             * alert($.isEmpty(arr));    // true
             * alert($.isEmpty(obj));    // false
             * </code>
             * @param {Array/Object} o 数组或者对象.
             * @return {Boolean} true - 对象为空; false - 对象不为空.
             */
            isEmpty:    function(o) {
                if ($.isUndefined(o)) return false;
                if (this.isArray(o)) {
                    return (o.length == 0) ? true : false;
                } else {
                    for (var i in o) {
                        if (i) return false;
                    }
                    return true;
                }
            },
            /**
             * 参数是否是NULL
             * @param o
             * @return true : null : false : 不为NULL
             */
            isBlank : function(o) {
                if((typeof o == "undefined")){
                    return true;
                }
                if(o ==null || o.length ==0){
                    return true;
                }
                var regu = "^[ ]+$";
                var re = new RegExp(regu);
                if(re.test(o)){
                    return true;
                }
                return false;
            },
            /**
             * 判断对象是否为数组.
             *
             * @function
             * @name $.isArray
             * @example <code>
             * var o = [];
             * alert($.isArray(o));    // true
             * </code>
             * @param {Object} o 任何JS对象.
             * @return {Boolean} true - 对象为数组; false - 对象不是数组.
             */
            isArray    :    function(o) {
                if ($.isUndefined(o)) return false;
                return (o.constructor == "Array" || o instanceof Array);
            },

            /**
             * 判断对象是否为未定义.
             *
             * @function
             * @name $.isUndefined
             * @example <code>
             * var o;
             * alert($.isUndefined(o));    // true
             * </code>
             * @param {Object} o 任何JS对象.
             * @return {Boolean} true - 对象为未定义; false - 对象不是未定义.
             */
            isUndefined    :    function(o) {
                return (typeof o == "undefined");
            },

            /**
             * 判断对象是否为一个格式有效的电子邮箱.
             *
             * @function
             * @name $.isUndefined
             * @example <code>
             * var o;
             * alert($.isEmail("chenhonghong@gmail.com"));    // true
             * alert($.isEmail("chenhonghong@gmailcom"));    // false
             * </code>
             * @param {Object} o 任何JS对象.
             * @return {Boolean} true - 对象为未定义; false - 对象不是未定义.
             */
            isEmail    :    function(str) {
                if (str.length == 0) {
                    return false;
                }
                // TLD checking turned off by default
                var checkTLD = 0;
                var knownDomsPat = /^(com|net|org|edu|int|mil|gov|arpa|biz|aero|name|coop|info|pro|museum)$/;
                var emailPat = /^(.+)@(.+)$/;
                var specialChars = "\\(\\)><@,;:\\\\\\\"\\.\\[\\]";
                var validChars = "\[^\\s" + specialChars + "\]";
                var quotedUser = "(\"[^\"]*\")";
                var ipDomainPat = /^\[(\d{1,3})\.(\d{1,3})\.(\d{1,3})\.(\d{1,3})\]$/;
                var atom = validChars + '+';
                var word = "(" + atom + "|" + quotedUser + ")";
                var userPat = new RegExp("^" + word + "(\\." + word + ")*$");
                var domainPat = new RegExp("^" + atom + "(\\." + atom + ")*$");
                var matchArray = str.match(emailPat);
                if (matchArray == null) {
                    return false;
                }
                var user = matchArray[1];
                var domain = matchArray[2];
                for (i = 0; i < user.length; i++) {
                    if (user.charCodeAt(i) > 127) {
                        return false;
                    }
                }
                for (i = 0; i < domain.length; i++) {
                    if (domain.charCodeAt(i) > 127) {
                        return false;
                    }
                }
                if (user.match(userPat) == null) {
                    return false;
                }
                var IPArray = domain.match(ipDomainPat);
                if (IPArray != null) {
                    for (var i = 1; i <= 4; i++) {
                        if (IPArray[i] > 255) {
                            return false;
                        }
                    }
                    return true;
                }
                var atomPat = new RegExp("^" + atom + "$");
                var domArr = domain.split(".");
                var len = domArr.length;
                for (i = 0; i < len; i++) {
                    if (domArr[i].search(atomPat) == -1) {
                        return false;
                    }
                }
                if (checkTLD && domArr[domArr.length - 1].length != 2
                        && domArr[domArr.length - 1].search(knownDomsPat) == -1) {
                    return false;
                }
                if (len < 2) {
                    return false;
                }
                return true;
            },

            /**
             * 判断对象是否为函数.
             *
             * @function
             * @name $.isFunction
             * @example <code>
             * var o = function() { //... };
             * alert($.isFunction(o));    // true
             * </code>
             * @param {Object} o 任何JS对象.
             * @return {Boolean} true - 对象为函数; false - 对象不是函数.
             */
            isFunction:    function(o) {
                if ($.isUndefined(o)) return false;
                return (typeof o == "function");
            },

            /**
             * HashMap集合.
             *
             * @function
             * @name $.hashTable
             * @example <code>
             * var hashMap = new $.hashTable();
             * hashMap.add("key1", "value1");    // true ( add success )
             * hashMap.add("key2", "value2");    // true ( add success )
             * hashMap.add("key3", "value3");    // true ( add success )
             *
             * alert(hashMap.item("key1"));    // value1
             * alert(hashMap.contains("key1"));    // true
             * alert(hashMap.count());    // 3
             * hashMap.remove("key1");
             * hashMap.clear();
             * </code>
             * @param {Object}this
             * @config {Function}add(key, value) 添加一个item到hashTable.
             * @config {Function}remove(key) 移除一个item.
             * @config {Function}count() 返回hashTable的item数量.
             * @config {Function}item(key) 获取对应的key的值.
             * @config {Function}contains(key) 判断hashTable里面是否包含key.
             * @config {Function}clear() 清空hashTable
             * @config {Function}toObject() 获取hashTable的原生对象.
             * @config {Function}serialize(token) 将hanshTable的所有item序列化成一个字符串,可能是这样的形式返回: key1=123&key2=456
             * (&可替换成其他字符)
             * @return {Map}hashTable
             */
            hashTable:    function() {
                this._hash = {};
                this.add = function(key, value) {
                    if (this.contains(this._hash[key])) {
                        this._hash[key] = $.isUndefined(value) ? null : value;
                        return true;
                    } else {
                        return false;
                    }
                };
                this.remove = function(key) {
                    delete this._hash[key];
                };
                this.count = function() {
                    var i = 0;
                    for (var i in this._hash) i++;
                    return i;
                };
                this.item = function(key) {
                    return this._hash[key];
                };
                this.contains = function(key) {
                    return ($.isUndefined(this._hash[key]));
                };
                this.clear = function() {
                    for (var i in this._hash) delete(this._hash[i]);
                };
                this.toObject = function() {
                    return this._hash
                };
                this.serialize = function(token) {
                    return $.oToStr(this._hash, token)
                }
            }
        },

        /**
         * 获取字符串的长度(双字节长度为2).
         *
         * @function
         * @name $.bytes
         * @example <code>
         * var str1 = "广州, abc";
         * var str2 = escape("广州, abc");
         * alert($.bytes(str1));    // 9
         * alert($.bytes(str2, true));    // 9
         * </code>
         * @param {String} str 字符串.
         * @param {Boolean}[escaped] 该字符串是否经过编码,true - 经过编码, false - 未经过编码.
         * @return {Number} 字符串长度.
         */
        bytes:    function(str, escaped) {
            var len = 0;
            if (escaped) {
                for (var i = 0, n = str.length; i < n; i++,len++) {
                    if (str.charAt(i) == "%") {
                        if (str.charAt(++i) == "u") {
                            i += 3;
                            len++;
                        }
                        i++;
                    }
                }
            } else {
                for (var i = 0, n = str.length; i < n; i++) {
                    iCode = str.charCodeAt(i);
                    if ((iCode >= 0 && iCode <= 255) || (iCode >= 0xff61 && iCode <= 0xff9f)) {
                        len += 1;
                    } else {
                        len += 2;
                    }
                }
            }
            return len;
        },

        /**
         * 将一个对象转换成一个字符串.
         *
         * @function
         * @name $.oToStr
         * @example <code>
         * var o = {a: "abc", name: "memberName", sex: "1"};
         * alert($.oToStr(o)); // a=abc&name=memberName&sex=1; 默认的链接符号是&
         * </code>
         * @param {Object}map 要解析的对象.
         * @param {String}[token] 链接符号,默认是&.
         * @return {String} 包含所有key和value的字符串.
         */
        oToStr:    function(map, token) {
            var str = "";
            var t = (token) ? token : "&";
            for (var i in map) {
                str += i + "=" + map[i] + t;
            }
            return str.substring(0, str.length - 1);
        },

        /**
         * 等比缩放.
         *
         * @function
         * @name $.scaleSize
         * @example <code>
         * var wh = $.scaleSize(600, 500, 1024, 768);
         * alert(wh.width + "---" + wh.height); // 600 --- 375
         * </code>
         * @param {String/Number} maxw 最大宽度.
         * @param {String/Number} maxh 最大高度.
         * @param {String/Number} w 实际宽度.
         * @param {String/Number} h 实际高度.
         * @return {Map} 包含键值 width、height 的对象,{width:_w, height:_h}.
         */
        scaleSize:    function(maxw, maxh, w, h) {
            maxw = parseInt(maxw);
            maxh = parseInt(maxh);
            w = parseInt(w);
            h = parseInt(h);

            var size = {width:0, height:0}, scale;

            if (w <= maxw && h <= maxh) {
                size.width = w;
                size.height = h;
                return size;
            }

            if (maxw > maxh) {    // 长方形 宽大于高
                if (w > h) {    // 按比例
                    scale = h / w;
                    if ((maxh / maxw) < scale) {
                        size.width = Math.floor(maxh / scale);
                        size.height = maxh;
                    } else {
                        size.width = maxw;
                        size.height = Math.floor(maxh * scale);
                    }
                } else if (w < h) {    // 非比例
                    scale = w / h;
                    size.width = Math.floor(maxh * scale);
                    size.height = maxh;
                } else {    // 正方
                    size.width = maxh;
                    size.height = maxh;
                }
            }
            else if (maxw < maxh) {    // 长方形 宽小于高
                if (w > h) {    // 非比例
                    scale = h / w;
                    size.width = maxw;
                    size.height = Math.floor(maxw * scale);
                } else if (w < h) {    // 按比例
                    scale = w / h;
                    if ((maxw / maxh) < scale) {
                        size.width = maxw;
                        size.height = Math.floor(maxw / scale);
                    } else {
                        size.width = Math.floor(maxh * scale);
                        size.height = maxh;
                    }
                } else {    // 正方
                    size.width = maxw;
                    size.height = maxh;
                }
            }
            else {    // 正方
                if (w > h) {
                    scale = h / w;
                    size.width = maxw;
                    size.height = Math.floor(maxw * scale);
                } else if (w < h) {
                    scale = w / h;
                    size.width = Math.floor(maxh * scale);
                    size.height = maxh;
                } else {
                    size.width = maxw;
                    size.height = maxh;
                }
            }
            return size;
        },

        /**
         * 等比缩放图片大小.
         *
         * @function
         * @name $.scalePhoto
         * @example <code>
         * <img onload="$.scalePhoto(this,100,200)"/>
         * </code>
         * @param {Object} [img] 图片对象.
         * @param {Number}[maxWidth] 允许图片显示的最大宽度.
         * @param {Number}[maxHeight] 允许图片显示的最大高度.
         */
        scalePhoto: function (img, maxWidth, maxHeight) {
            var s = $.scaleSize(maxWidth, maxHeight, img.width, img.height);
            img.width = s.width;
            img.height = s.height;
        },

        /**
         * 预加载图片.
         *
         * @function
         * @name $.preloadImg
         * @example <code>
         * var links = [
         *     ["marry5logo", "http://public.marry5.com/home/fifthindex/ad_banner/images/xmas/new_logo1225.jpg"],
         *     ["jiayuanlogo", "http://images.love21cn.com/w/index/i/logo.gif"],
         *     ["baihelogo", "http://www.baihe.com/Images/Head_imgs/logoxmas.gif"],
         *     ["baidu", "http://www.baidu.com/img/logo.gif"],
         *     ["google", "http://www.google.cn/intl/zh-CN/images/logo_cn.gif"]
         * ];
         * $.preloadImg(links, 1); //第二个参数是1：因为索引0是标题，索引1才是图片路径
         * </code>
         * @param {Array} links 包含图片路径的数组.
         * @param {Number}[nav] 图片路径数组中对应图片路径的索引号(默认值为0).
         * @param {Array}[array] 如果传入数组，则该数组保存预加载的图片对象.
         * @param {Function}[loadFn] 如果传入该回调函数,则图片加载完毕即调用该函数.
         * @return {void}
         */
        preloadImg:    function(links, nav, array, loadFn) {
            if (!nav) nav = 0;
            for (var i = 0, n = links.length; i < n; i++) {
                var ni = new Image();
                ni.src = links[i][nav];
                ni.onerror = function() {
                    this.setAttribute("falied", true);
                };
                if (loadFn) {
                    if ($.browser.msie) {
                        ni.onreadystatechange = function() {
                            if (this.readyState == "complete") loadFn(this);
                        };
                    } else {
                        ni.onload = function() {
                            loadFn(this);
                        };
                    }
                }
                if (array) {
                    array[i] = ni;
                }
            }
        },

        date:    {
            /**
             * 根据字符串构造一个日期对象.
             *
             * @private
             * @function
             * @name $.date.D
             * @example <code>
             * $.date.D("2006-1-2");
             * $.date.D("2006.1.2");
             * $.date.D("2006/1/2");
             * </code>
             * @param {Date/String}d 日期字符串.
             * @return {Date} 日期独享.
             */
            D:    function(d) {
                if (d.constructor == Date) {
                    return d;
                }
                var box;
                if (d.indexOf("-") != -1) {    // 2006-01-13
                    box = "-";
                } else if (date.indexOf(".") != -1) {    // 2006.1.30
                    box = ".";
                } else if (date.indexOf("/")) {    // 2005/2/3
                    box = "/";
                }
                var arr = d.split(box);
                return new Date(arr[0], arr[1], arr[2]);
            },

            /**
             * 获取对应日期的星座.<br/>
             * 魔羯座(12/22 - 1/19)、水瓶座(1/20 - 2/18)、双鱼座(2/19 - 3/20)、牡羊座(3/21 - 4/20) <br/>
             * 金牛座(4/21 - 5/20)、双子座(5/21 - 6/21)、巨蟹座(6/22 - 7/22)、狮子座(7/23 - 8/22) <br/>
             * 处女座(8/23 - 9/22)、天秤座(9/23 - 10/22)、天蝎座(10/23 - 11/21)、射手座(11/22 - 12/21) <br/>
             *
             * @function
             * @name $.getAstro
             * @example <code>
             * alert($.getAstro("2003-4-10")); // 牡羊
             * alert($.getAstro("2003-04-8")); // 牡羊
             * alert($.getAstro("2003.4.10")); // 牡羊
             * alert($.getAstro("2003.04.07")); // 牡羊
             * alert($.getAstro("2003.04.7")); // 牡羊
             * </code>
             * @param {String}date 日期格式,以[-]或者[.]作为分隔符,例如:2007-1-20 2005.10.20, 2006/5/10
             * @return {String} 星座.
             */
            getAstro:    function(date) {
                var month, day, box;
                if (date.indexOf("-") != -1) {    // 2006-01-13
                    box = "-";
                } else if (date.indexOf(".") != -1) {    // 2006.1.30
                    box = ".";
                } else if (date.indexOf("/")) {    // 2005/2/3
                    box = "/";
                }
                var s = date.split(box);
                month = parseFloat(s[1]);
                day = parseFloat(s[2]);
                var s = "魔羯水瓶双鱼牡羊金牛双子巨蟹狮子处女天秤天蝎射手魔羯";
                var arr = [20,19,21,21,21,22,23,23,23,23,22,22];
                return s.substr(month * 2 - (day < arr[month - 1] ? 2 : 0), 2) + "座";
            },

            /**
             * 计算两个日期的间隔.
             *
             * @function
             * @name $.between
             * @example <code>
             * var d11 = "2001-2-1";
             * var d22 = "2001-2-9";
             * var between1 = $.between(d11, d22);
             * var between1 = $.between(d11, d22, "time");
             * </cdoe>
             * @param {Date/String}startDate
             * @param {Date/String}endDate
             * @param {String}[type] 间隔类型, day - 日期间隔(默认); time - 时间间隔(毫秒);
             * @return {Number} 间隔数.
             */
            between:    function(startDate, endDate, type) {
                startDate = $.date.D(startDate);
                endDate = $.date.D(endDate);
                if (type == "time") {
                    return Math.abs(endDate - startDate);
                } else {
                    return Math.abs((endDate.valueOf() - startDate.valueOf()) / 86400000);
                }
            }
        },

        window:    {
            /**
             * 获取浏览器垂直滚动高度.
             *
             * @function
             * @name $.scrollTop
             * @example <code>
             * alert($.scrollTop());
             * </code>
             * @return {Number} 垂直滚动高度.
             */
            scrollTop:    function() {
                return window.pageYOffset || document.documentElement.scrollTop || document.body.scrollTop || 0;
            },

            /**
             * 获取浏览器水平滚动距离.
             *
             * @function
             * @name $.scrollLeft
             * @example <code>
             * alert($.scrollLeft());
             * </code>
             * @return {Number} 水平滚动距离.
             */
            scrollLeft:    function() {
                return window.pageXOffset || document.documentElement.scrollLeft || document.body.scrollLeft || 0;
            },

            cookie:    {
                /**
                 * 设置Cookie.
                 *
                 * @function
                 * @name $.setCookie
                 * @example <code>
                 * $.setCookie("memberName", "BaSaRa");
                 * </code>
                 * @param {String} name Cookie's key.
                 * @param {Object}value Cookie's value.
                 * @param {Date}[expires] Cookie's 过期时间.
                 * @param {String}[path] Cookie 对应的页面路径.
                 * @param {String}[domain] Cookie's 域名.
                 * @param {String}[secure] Cookie 是否为安全的,传入值则表示该Cookie只能在安全环境下访问.
                 * @return {void}
                 */
                set:    function(name, value, expires, path, domain, secure) {
                    if (navigator.cookieEnabled) {
                        document.cookie = name + "=" + escape(value) +
                                ((expires) ? "; expires=" + expires.toGMTString() : "") +
                                ((path) ? "; path=" + path : "") +
                                ((domain) ? "; domain=" + domain : "") + ((secure) ? "; secure" : "");
                    }
                },

                /**
                 * 获取Cookie.
                 *
                 * @function
                 * @name $.getCookie
                 * @example <code>
                 * $.getCookie("memberName");
                 * </code>
                 * @param {String} name Cookie's key.
                 * @return {String/Null} 如果Cookie存在则返回unescape后的Cookie,不存在则返回null.
                 */
                get:    function(name) {
                    if (navigator.cookieEnabled) {
                        var aCookie = document.cookie.split("; ");
                        for (var i = 0, n = aCookie.length; i < n; i++) {
                            var aCrumb = aCookie[i].split("=");
                            if (name == aCrumb[0])
                                return unescape(aCrumb[1]);
                        }
                        return null;
                    }
                    return null;
                },

                /**
                 * 删除Cookie.
                 *
                 * @function
                 * @name $.deleteCookie
                 * @example <code>
                 * $.deleteCookie("memberName");
                 * </code>
                 * @param {String} name Cookie's key.
                 * @param {String}[path] Cookie's 对应的路径.
                 * @param {String}[domain] Cookie's 过期时间.
                 * @return {void}
                 */
                del:    function(name, path, domain) {
                    if (navigator.cookieEnabled) {
                        document.cookie = name + "=" +
                                ((path) ? "; path=" + path : "") +
                                ((domain) ? "; domain=" + domain : "") +
                                "; expires=Thu, 01-Jan-70 00:00:01 GMT";
                    }
                }
            },

            uri:    {
                /**
                 * 获取浏览器URL的参数集合,以hashTable形式返回.
                 *
                 * @function
                 * @name $.params
                 * @example <code>
                 * alert($.params());
                 * alert($.params("?hello=true&flag=false"));
                 * </code>
                 * @param {String}[url] 可以传入location search进行解析.
                 * @return {hashTable} 参数集合.
                 */
                getParams:    function(url) {
                    var pMap = new $.hashTable();
                    var params = (url) ? url : location.search;
                    var index = params.indexOf("?");
                    if (index != -1) {
                        params = params.substring(1);
                    }
                    var sp = params.split("&");
                    var pItems;
                    for (var i = 0, n = sp.length; i < n; i++) {
                        pItems = sp[i].split("=");
                        pMap.add(pItems[0], pItems[1]);
                    }
                    return pMap;
                },

                /**
                 * 更新浏览器的URL,如果URL有指定参数则替换值,如过没有指定参数则添加指定参数到URL中.
                 *
                 * @function
                 * @name $.updateParams
                 * @example <code>
                 * var np = $.updateParams({a:123, b:456}); //如果URL有a参数则更新其值为123,如果没有a参数则添加a=123到URL.
                 * window.location = np;
                 * </code>
                 * @param {Object}params URL参数集合.如果没有该参数则添加新参数到URL.
                 * @return {String} 新的URL.
                 */
                updateParams:    function(params) {
                    if (!params) return location;

                    var href = location.href;
                    var qmark = href.indexOf("?");
                    var url = "", search = "", map = null;
                    if (qmark == -1) {
                        url = href;
                        search = "";
                    } else {
                        url = href.split("?")[0];
                        search = location.search.replace("?", "");
                        map = $.getParams();
                    }

                    if (!map) {    // 直接添加参数
                        search += "?";
                        for (var i in params) {
                            search += i + "=" + params[i] + "&";
                        }
                        search = search.substring(0, search.length - 1);
                    } else {    // 判断是否有参数,无参数则添加该参数,有参数则更改其值.
                        for (var i in params) {
                            map.add(i, params[i]);
                        }
                        search = "?" + $.oToStr(map.toObject());
                    }

                    return url + search;
                }
            }
        },

        /**
         * 在客户端验证上传的图片中是否有超出指定文件大小的图片(仅限IE中使用),主要用在上传图片时的检查.
         *
         * @function
         * @name $.validImagesSize
         * @example <code>
         * input.onchange = function() {
         *     $.overImageSize(2, this.value);
         * }
         * </code>
         * @param {Integer} size 文件大小,以兆为单位(M).
         * @param {String} src 图片路径
         * @return {Boolean} true 图片集合中有超过指定大小的图片; false 没有超出指定文件大小.
         */
        overImageSize:    function(size, src) {
            if (!$.browser.msie) {
                alert("该方法只支持IE浏览器");
                return null;
            }
            var b = false;
            var sbyte = size * 1000000; // (1,000,000)
            var img = new Image();
            img.onload = function() {
                if (this.fileSize > sbyte) {
                    b = true;
                }
            }
            img.onerror = function() {
                b = "请检查图片路径";
            }
            img.src = src; // src写在onload后面，因为IE下面缓存的原因，写在onload前面的话会因为执行速度太快而不触发事件
            return b;
        },

        /**
         * 等加载图片超过时，用另一个图片代替.
         *
         * @function
         * @name $.scalePhoto
         * @example <code>
         * <img onerror="$.loadDefaultPhoto(this,'http://image.marry5.com/www/home/default.jpg')"/>
         * </code>
         * @param {Object} [img] 图片对象.
         * @param {String}[defaultPhoto] 新的图片路径.
         */
        loadDefaultPhoto:    function(img, defaultPhoto) {
            if (img != null) {
                //如果图片地址是绝对路径的话，直接使用该图片路径，否则，用统一资源的图片
                if (defaultPhoto.indexOf("http") > -1) {
                    img.src = defaultPhoto;
                    img.onerror = "";		//清楚onerror属性避免死循环
                } else {
                    img.src = image_base + defaultPhoto;
                    img.onerror = "";		//清楚onerror属性避免死循环
                }
            }
        },

        /**
         * 验证手机号码是否正确.
         *
         * @function
         * @name $.isMobilePhone:
         * @example <code>
         * var str1 = 13031140550;
         * var str2 = 11031140550;
         * var str3 = 1103114055;
         * alert($.isMobilePhone:(str1));    // true
         * alert($.isMobilePhone:(str2));    // false
         * alert($.isMobilePhone:(str3));    // false
         * </code>
         * @param {String} str 字符串.
         * @return {Boolean} true - 手机号码正确; false - 手机号码错误.
         */
        isMobilePhone: function(mobilePhone) {
            if (mobilePhone == "") {
                return false;
            }

            //检查手机号码的格式是否正确
            if (mobilePhone.length != 11) {
                return false;
            }

            var mobilePattern = new RegExp("1(3|5|8)[0-9]{9}");
            if (!mobilePattern.test(mobilePhone)) {
                return false;
            }

            return true;
        },

        /**
         * 检查字符串是否是一个有效的嫁我网会员昵称.
         * 会员昵称：
         * 只允许：汉字、数字和字母字母。
         * 长度限制：最少 1 个字符，全部加起来不能超过 10 个字符。一个汉字等于两个字符。
         *
         * @function
         * @name $.isValidMemberName:
         * @example <code>
         * var str1 = "中文中文中文";
         * alert($.isValidMemberName:(str1));    // false
         * </code>
         * @param {String} memberName 字符串.
         * @return {Boolean} true - 昵称有效; false - 昵称无效.
         */
        isValidMemberName: function(memberName) {
            var limitCount = 10;
            var baseCount = 1;
            var count = memberName.length;
            if (count < baseCount) {
                return false;
            }
            //计算长度和检查非法字符
            var chinesePattern = new RegExp("^[^\\x00-\\xff]");
            var numberPattern = new RegExp("^[\\d]");
            var literPattern = new RegExp("^[a-zA-Z]");
            var _total = 0;
            for (i = 0; i < memberName.length; i ++) {
                var c = memberName.charAt(i);
                var validFlag = false;
                if (chinesePattern.test(c)) {
                    _total += 2;
                    //一字汉字算两个字符
                    validFlag = true;
                }
                if (numberPattern.test(c)) {
                    _total += 1;
                    validFlag = true;
                }
                if (literPattern.test(c)) {
                    _total += 1;
                    validFlag = true;
                }
                if (!validFlag || _total > limitCount) {
                    return false;
                }
            }
            return true;
        },
        /**
         * 输入自动完成，支持繁体.
         * Depends:
         *   language.js
         *   autocomplete.js
         * @function
         * @name $.completeAuto(id, names)
         * @example <code>
         * </code>
         * @return {Array[Boolean]} 一个数组,包含了每张图片后缀名是否在允许范围内的布尔值标记.
         */
        autoComplete:    function(idValue, names) {
            var normalize = function(term) {
                var ret = "";
                for (var i = 0; i < term.length; i++) {
                    ret += StranText(term.charAt(i), 0, 0) || term.charAt(i);
                }
                return ret;
            };

            $("#" + idValue).autocomplete({
                source: function(request, response) {
                    var matcher = new RegExp($.ui.autocomplete.escapeRegex(request.term), "i");
                    response($.grep(names, function(value) {
                        value = value.label || value.value || value;
                        t1 = normalize(value);
                        tem = matcher.test(value) || matcher.test(t1);
                        return tem;
                    }));
                },
//                  autoFocus: true,
                delay:0
            });
        }
    });


    $.fn.extend({
        /**
         * 获取一张(一组)图片的后缀名.
         *
         * @function
         * @name $.getPostfix
         * @example <code>
         * &lt;img alt="google" src="http://www.baidu.com/img/logo.gif" /&gt;
         * &lt;img alt="baihe" src="http://www.baihe.com/Images/Head_imgs/logoxmas.gif" /&gt;
         *
         * var arr = $("img").getPostfix(); // arr: ["gif", "gif"]
         * </code>
         * @return {Array[String]} 图片的后缀名数组.
         */
        getPostfix:    function() {
            var arr = [];
            this.each(function(index) {
                var v = $(this).attr("src");
                arr[index] = v.substring(v.lastIndexOf(".") + 1, v.length);
            });
            return arr;
        },

        /**
         * 检测一张(一组)图片的后缀名是否在允许范围内.
         *
         * @function
         * @name $.checkPostfix
         * @example <code>
         * &lt;img alt="google" src="http://www.baidu.com/img/logo.gif" /&gt;
         * &lt;img alt="baihe" src="http://www.baihe.com/Images/Head_imgs/logoxmas.gif" /&gt;
         *
         * var accept = "jpg, bmp, gif";
         * var arr = $("img").checkPostfix(accept); // [true, true]
         * </code>
         * @return {Array[Boolean]} 一个数组,包含了每张图片后缀名是否在允许范围内的布尔值标记.
         */
        checkPostfix:    function(accept) {
            var arr = [];
            var postfixs = $(this).getPostfix();
            $.each(postfixs, function(index, postfix) {
                arr[index] = $.inString(postfix, accept);
            });
            return arr;
        },

        /**
         * 全选所有的INPUT(checkbox, radio).
         *
         * @function
         * @name $.check
         * @example <code>
         * var checkboxs = $("input:checkbox").check();
         * </code>
         * @return {jQuery} 一个jQuery对象,包含全选的所有INPUT.
         */
        check:    function() {
            return this.each(function() {
                this.checked = true;
            });
        },

        /**
         * 反选所有的INPUT(checkbox, radio).
         *
         * @function
         * @name $.uncheck
         * @example <code>
         * var checkboxs = $("input:checkbox").uncheck();
         * </code>
         * @return {jQuery} 一个jQuery对象,包含全选的所有INPUT.
         */
        uncheck:    function() {
            return this.each(function() {
                if (this.checked) {
                    this.checked = false;
                } else {
                    this.checked = true;
                }
            });
        },

        /**
         * 换行显示纯单字节的文本.<br/>
         * IE下可以通过设置css word-wrap:break-word; 来换行显示;<br/>
         * firefox下没有相应的css,一般的做法是设置overflow:auto;来滚动实现,如果需要换行只能通过js来实现.
         *
         * @function
         * @name $.breakWord
         * @example <code>
         * HTML代码:<br/>
         * css: #breakArea div { width: 300px; } <br/>
         * &lt;div id="breakArea" style="margin-bottom: 10px;"&gt;
         * &lt;div&gt;aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa&lt;/div&gt;
         * &lt;div&gt;bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb&lt;/div&gt;
         * &lt;div&gt;aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa&lt;/div&gt;
         * </code>
         * @example <code>
         * $("#breakArea>div").breakWord(36);
         * </code>
         * @param {Number}linelen 每行显示的文本长度.(需自行调整)
         * @return {void}
         */
        breakWord:    function(linelen) {
            this.each(function() {
                var ct = this.innerHTML;
                var tmp = "";
                while (ct.length > linelen) {
                    tmp += ct.substring(0, linelen) + "&#10;";
                    ct = ct.substring(linelen, ct.length);
                }
                tmp += "&#10;" + ct;
                this.innerHTML = tmp;
            });
        },

        /**
         * 计算多行文本输入的剩余字数(文本的所有字符都算是单字节).
         *
         * @function
         * @name $.txtcount
         * @example <code>
         * &lt;textarea id="txt" rows="3" cols="50"&gt;&lt;/textarea&gt;
         * 已输入 &lt;input type="text" id="has" /&gt; 个字；还可以输入 &lt;input type="text" id="rest" /&gt; 个字
         *
         * $("#txt").keydown(function() {
         *   $(this).txtcount($.$("has"), $.$("rest"), 100);
         * });
         * </code>
         * @param {HTMLElement} rest 显示剩余字数的HTML对象,可以是含有value或者innerHTML属性的所有HTML对象.
         * @param {HTMLElement} count 显示已输入字数的HTML对象,可以是含有value或者innerHTML属性的所有HTML对象.
         * @param {Number} maxlen 最多输入字数.
         * @return {void}
         */
        txtcount:    function(rest, count, maxlen) {
            var v = $.trim(this.val());
            var vl = v.length;

            if (vl > maxlen) {
                var nn = v.substring(0, maxlen);
                this.value = nn;
                this.attr("value", nn);
            }

            if (rest) {
                if (rest.tagName.toUpperCase() == "INPUT")
                    rest.value = vl;
                else
                    rest.innerHTML = vl;
            }

            if (count) {
                var co = maxlen - vl;
                if (count.tagName.toUpperCase() == "INPUT")
                    count.value = (co > 0) ? co : 0;
                else
                    count.innerHTML = (co > 0) ? co : 0;
            }

        },

        /**
         * 判断浮动层是否右越界.
         *
         * @function
         * @name $.overBoundary
         * @example <code>
         * &lt;span id="span1" style="float: right;"&gt;浮动层在左边&lt;/span&gt;
         * &lt;div id="div1" style="width: 100px; border: 1px solid green;
         * position: absolute; display: none; background-color: #EFEFEF;"&gt;helloworld&lt;/div&gt;
         *
         * var div1 = $.$("div1");
         * $("#span1").hover(function(){
         *     var ifover = $(this).positionLayer(div1);    // true
         * });
         * }, function(){
         *
         * });
         * </code>
         * @param {HTMLElement/String} layerOrWidth 浮动层或者浮动层的宽度.
         * @return {Boolean} true - 右越界; false - 没有右越界.
         * TODO:添加上下越界.
         */
        overBoundary:    function(layerOrWidth) {
            var left = this.offset().left;
            var oow = this[0].offsetWidth;
            var layerW;
            if (layerOrWidth.tagName) {
                layerOrWidth.style.display = "";
                layerW = layerOrWidth.offsetWidth;
            } else {
                layerW = parseInt(layerOrWidth);
            }
            var dw = document.body.clientWidth;
            return ((left + layerW + oow) > dw) ? true : false;
        },

        /**
         * 定位浮动层layer,当layer在浏览器右边越界时则在左边显示，反之在右边显示.
         *
         * @function
         * @name $.positionLayer
         * @example <code>
         * &lt;span id="span1" style="float: right;"&gt;浮动层在左边&lt;/span&gt;
         * &lt;div id="div1" style="width: 100px; border: 1px solid green;
         * position: absolute; display: none; background-color: #EFEFEF;"&gt;helloworld&lt;/div&gt;
         *
         * var div1 = $.$("div1");
         * $("#span1").hover(function(){
         *     var lt = $(this).positionLayer(div1);
         *  $(div1).css({display: "",left: lt.left+"px",top: lt.top+"px"
         * });
         * }, function(){
         *   div1.style.display = "none";
         * });
         * </code>
         * @param {HTMLElement} layer 浮动层.
         * @return {Map} 包含兼职left和top的对象, {left:_left, top:_top}.
         */
        positionLayer:    function(layer) {
            var offset = this.offset();
            var left = offset.left;
            var top = offset.top;
            var oow = this[0].offsetWidth;
            layer.style.display = "";
            var layerW = layer.offsetWidth;

            var _left;
            var _top = top - 3;

            var dw = document.body.clientWidth;	// 窗口宽度，不能用offsetWidth
            if ((left + layerW + oow) > dw) {
                _left = (dw - layerW) - oow - 10;
            } else {
                _left = left + oow;
            }

            return {left:    _left, top:    _top};
        },

        /**
         * 让HTML元素在浏览器窗口居中显示.
         *
         * @function
         * @name $.placeCenter
         * @example <code>
         * $("#floatDIV").placeCenter();
         * </code>
         * @param {Boolean} onlyTop 仅仅垂直居中.
         * @param {Boolean} onlyLeft 仅仅水平居中.
         * @return {void}
         */
        placeCenter:    function(onlyTop, onlyLeft) {
            var scrollTop = $.scrollTop();
            var clientHeight = document.documentElement.clientHeight;
            var clientWidth = document.documentElement.clientWidth;
            var top = ((((clientHeight) / 2) + scrollTop) - (this.height() / 2)) + "px";
            var left = (clientWidth / 2) - (this.width() / 2) + "px";
            var css = {top:    top,left:    left};
            if (onlyTop) {
                css = {top:    top};
            } else if (onlyLeft) {
                css = {left:    left};
            }
            this.css(css);
        },

        /**
         * @function
         * @name $.parseRegexp
         * @example <code>
         * &lt;div id="area" style="width: 300px;"&gt;
         *     &lt;div id="userTemplate" class="user1"&gt;
         *         &lt;img alt="spring" src="${picsrc}" width="60" height="79"&gt;&lt;br/&gt;
         *         &lt;a href="http://www.yaoyuan.com/id=${id}"&gt;${name}&lt;/a&gt;
         *         &lt;span class="info"&gt;${age}&lt;/span&gt;
         *         &lt;span class="info"&gt;${edu}&lt;/span&gt;
         *     &lt;/div&gt;
         * &lt;/div&gt;
         *
         * var users = {
         *     list : [
         *         {id:1001,name:"十面埋伏1", age:22, edu:"本科", picsrc:"pp02.gif"},
         *         {id:1002,name:"十面埋伏2", age:23, edu:"本科", picsrc:"pp02.gif"},
         *         {id:1003,name:"十面埋伏3", age:24, edu:"本科", picsrc:"pp02.gif"},
         *         {id:1004,name:"十面埋伏4", age:25, edu:"本科", picsrc:"pp02.gif"}
         *     ];
         * };
         * $("#area").parseRegexp(users.list, "userTemplate");
         * </code>
         * @param {Array/Object} json    指定数据源,可以是一个数据对象或者包含多个数据对象的数组.
         * @param {String} templateid 模板ID,根据数据源长度迭代生成N个这样HTML对象后会删除模板.
         * @return {void}
         */
        parseRegexp:    function(json, templateid) {
            var container = this;
            var template = container.find("#" + templateid)[0];
            var str = templateid ? template.outerHTML : container.innerHTML;	// 获取需要复制的字符串

            // 替换${}成json的值
            var xstr = str.replace(/%7B/ig, "{").replace(/%7D/ig, "}");	// FF下会将属性的 ${} 转换成 %7B和%7D
            var re = /\$\{(\w+)\}|\$\{(\w+\.+\w+)\}/ig;	// 匹配 ${id} 或者 ${memberlist.msg}
            var re1 = /(\w+\.*\w*)/ig;	// 匹配 ${} 内的key
            var arr = xstr.match(re);

            // 如果json是数组，则迭代，不是数组则先构造成数组
            var list = [];
            if ($.isArray(json)) {
                list = json;
            } else {
                list[0] = json;
            }

            var recursion = function(o, k) {    // 处理 memberlist.msg 这种深度值
                var a = k[0].split(".");	// ["memberlist", "msg"]
                var oo = o;
                var i = 0;
                while (i < (a.length - 1)) {
                    oo = oo[a[0]];
                    i++;
                }
                return ((oo.length) ? oo : oo[k]);
            };

            var outhtml = "";
            for (var j = 0; j < list.length; j++) {
                var xstr2 = xstr;
                for (var i = 0; i < arr.length; i++) {
                    var rkey = arr[i];
                    var dkey = rkey.match(re1);
                    var data = list[j][dkey] || "";

                    //					var data1 = recursion(list[j], dkey);
                    //					if (typeof data1 == "object") {
                    //						var a = k[0].split(".");
                    //						a = a[a.length-1];
                    //						for (var i=0; i<o.length; i++) {
                    //							xstr2 = xstr2.replace(a, o[i][a]);
                    //						}
                    //					}

                    xstr2 = xstr2.replace(rkey, data);
                }
                outhtml += xstr2;
            }

            template.outerHTML = outhtml;
        }
    });


    $.inString = $.core.inString;
    $.isEmpty = $.core.isEmpty;
    $.isBlank= $.core.isBlank;
    $.isArray = $.core.isArray;
    $.isUndefined = $.core.isUndefined;
    $.isFunction = $.core.isFunction;
    $.isEmail = $.core.isEmail;
    $.sizeof = $.core.sizeof;
    $.hashTable = $.core.hashTable;
    $.focusCHK = $.core.focusCHK;
    $.ajaxBase =  $.core.ajaxBase;
    $.ajaxAsync = $.core.ajaxAsync;

    $.getAstro = $.date.getAstro;
    $.between = $.date.between;

    $.scrollTop = $.window.scrollTop;
    $.scrollLeft = $.window.scrollLeft;

    $.getParams = $.window.uri.getParams;
    $.updateParams = $.window.uri.updateParams;

    $.setCookie = $.window.cookie.set;
    $.getCookie = $.window.cookie.get;
    $.deleteCookie = $.window.cookie.del;

    $.jcommon = true;

})(jQuery);