Src=$2/src/main/webapp
JavaSrc=$2/src/main/java
JS=$Src/js
NodePath=~/ndjs
CCompiler=${NodePath}/closure-compiler.jar
export NODE_PATH=$NodePath/node_modules

if [ "$1" = "install" ]; then
  cd $NodePath
	cnpm install --save-dev babel-cli babel-preset-env less
  cnpm install --save http-proxy express
  cnpm install --save requirejs
	echo '{ "presets": ["env"] }' > $NodePath/.babelrc 
  cd -
	
elif [ "$1" = "build" ]; then
  #date=`date +%s`
  cd $JS
	${NodePath}/node_modules/.bin/babel **/*.js *.js --ignore 'lib/*' -d ./output
  ${NodePath}/node_modules/.bin/r.js -o build.js

elif [ "$1" = "dev" ]; then
  #cd $JS
  #sed -i '' 's/model.jsp/model-dev.jsp/' $Src/WEB-INF/decorators.xml #for OS
  #sed -i 's/model.jsp/model-dev.jsp/' $Src/WEB-INF/decorators.xml
  sed -i "s#DOMAIN_NAME+\"/js/dist\";#DOMAIN_NAME\+\"/js\";#" $JavaSrc/com/metasoft/model/Constant.java
  sed -i "s#es.host=127.0.0.1:9300#es.host=121.201.28.147:9300#" $2/src/test/resources/application.properties
elif [ "$1" = "publish" ]; then
  cd ~/deploy/data-center-frontend
  git pull
  ./build.sh build
  cp -R ./frontend/dist $Src
  VER=`git rev-parse --short HEAD`
  sed -i "s#frontend.version=1.0#frontend.version=${VER}#" $2/src/test/resources/application.properties
elif [ "$1" = "mobile" ]; then
  cd ~/deploy/data-center-mobile-frontend
  git pull
  ./build.sh build
  cp -R ./frontend/mobile-dist $Src  
  
elif [ "$1" = "less" ]; then
  $NodePath/node_modules/.bin/lessc $Src/less/data-center.less $Src/css/data-center.css
elif [ "$1" = "msg-gen" ]; then  
  thrift-0.9.3.exe -r --gen java ./msg.thrift
elif [ "$1" = "pkg" ]; then  
  mvn package
elif [ "$1" = "proxy" ]; then
  #if ! [ -d $Src/node/node_modules ]; then
    #ln -s $NodePath/node_modules $Src/node/node_modules
  #fi
  cd $Src
  node ./node/express.js $3
  #./documents/build.sh proxy http://192.168.0.105:8080
else
  echo "input install,build"
fi
  