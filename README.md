# data-center
sysadmin
k1e2y3@pwd

git@121.201.24.170
yuanyao403
dp00dp00

121.201.7.141
demo/keymobile
027F027f

一,eclipse依赖设置
1,data-center在eclipse的build path中引入AuthService,underware项目.
2,AuthService在eclipse的build path中引入underware项目.

二,ant,maven构建
1,AuthService在ant构建时依赖underware, 因此先将underware jar放入其lib内
2,pom中maven war plugin将maven仓库外的jar包自动打包, 为节省网速没有将AuthService,underware的完整依赖库放入libs内, 在运行launsh.sh rebuild前请人工放入

三,babel
es6路径设置properties->resource->add filter->exclude all, folders->node_modules
将meta*,vue*复制到src中,运行build.sh build,再到webapp/script中运行generate.sh打包所有js,在decorator.sh中设置sitemesh发布模板

四,jetty运行慢的问题
-Xms512m
-Xmx1024m
-XX:+UseParallelGC

五,known issues
1,登录成功后仍然跳转到登录页, 用户角色为空导致

六,db2驱动错误
vm参数加上-Ddb2.jcc.charsetDecoderEncoder=3

七,maven仓库外jar引入
maven仓库外jar请放入青云的/opt/lib中
pom中的路径请写在"<!-- mvn package start--><!--"后面