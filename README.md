# aliUpdateDomainRecord
1、git项目之后，需要把conf下的所有文件拷贝到项目同级目录，然后打开conf.properties文件按照注释配置；
2、运行后，从窗口中拷贝出一条你要修改的记录，复制到record.properties文件中，如果你只修改ip吧value的值修改成ip即可注意要符合json字符串规范；
3、用mvn package命令打包会出现两个jar，以one-jar.jar结尾的是直接可运行的jar包，通过java -jar可直接运行
4、运行时注意修改PathUtil.getClassPath()方法中的对应路径，测试和打包后的路径使用的不一样；