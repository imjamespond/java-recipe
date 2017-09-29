
requirejs.config({
    baseUrl: '/data-center/js',
    paths: {
        jquery: './lib/jquery-1.12.4',
        bootstrap: './lib/bootstrap/js/bootstrap.min',
        chart:'./lib/Chart',
        sha1: './lib/sha1',
        vue: './lib/vue',
        vue_router: './lib/vue-router.min',
        lodash: './lib/lodash.min',
        nprogress: './lib/nprogress',
        ace: 'lib/ace', 
        app: 'app'
    },
    shim: {
        bootstrap: { 
            deps: ["jquery"]
        }
    }
        //urlArgs: 'bust=' + (new Date()).getTime() // Disable require js cache
});

require(['vue'], function(Vue){
    try {
        new Vue()
    } catch (error) {
        console.log('require ie9 or higher')
        document.body.innerHTML="请使用ie9或更高版本的浏览器"
    }
})

