# 压力测试工具

1. 以压测百度搜索接口https://m.baidu.com/s?word=xx为例，url可以换为需要压的url，HttpHandler类可修改相应验证逻辑
2. 准备好搜索词列表query.txt，设置适当线程数，编译并执行java程序：
 * mvn package 
 * java -cp target/pressure-test-1.0-SNAPSHOT.jar com.hong.pressure.PressureTest /tmp/query.txt 2
 