# poi-spring-boot-starter使用说明

## 1.概述
```
按照Spring Boot Starter插件一贯的风格“开箱即用”。
不需要大量的配置，只需要引入项目，做出少量的配置，就能轻松的实现功能。
本插件主要实现方便读取excel文档，并做一些校验的功能
```
## 2.引入依赖
```
<!-- poi-spring-boot-starter -->
<dependency>
    <groupId>xyz.frt</groupId>
    <artifactId>poi-spring-boot-starter</artifactId>
    <version>0.1.0</version>
</dependency>
```
## 3.添加配置
```
在文件
application.properties
中添加如下配置:

#poi-spring-boot-starter
#是否启用插件
xyz.frt.poi.enable=true
#excel表格标题行号，从0开始
xyz.frt.poi.sheet-header=2
#excel数据开始的行号，从0开始
xyz.frt.poi.start-row=5
#excel数据开始的列号，从0开始
xyz.frt.poi.start-col=1
```
## 4.引入PoiService
```
public class PoiServiceTest {

    @Autowired
    private PoiService poiService;

}
```
其中的UserCar实体类为测试类:
```
public class UserCard {

	private String cid;
	private String name;
	private String cardNo;
	private String unit;

	/**...getter/setter...**/

	@Override
	public String toString() {
		return "UserCard [cid=" + cid + ", name=" + name + ", cardNo=" + cardNo + ", unit=" + unit + "]";
	}

}
```

## 5.使用PoiService
```
public class PoiServiceTest {

    @Autowired
    private PoiService poiService;

    @Test
    public void test() throws FileNotFoundException {

        File file = new File("/home/phw/Downloads/test.xlsx");

        /**
         * 重要:
         * 根据实际业务创建类属性与excel列名的对应关系，
         * 参数依次为
         * excelColumnName:excel列名，
         * fieldName:类属性名，
         * excelDataType:基础校验的预设值，枚举类型，详见下方，
         * length:值限制的最大长度，
         * isNull:是否可以为空
         */
        ExcelMapRule[] rules = {
                new ExcelMapRule("证件号码", "cid", ExcelDataType.IDCARD, 18, true),
                new ExcelMapRule("姓名", "name", ExcelDataType.TXT, 10, true),
                new ExcelMapRule("卡号", "cardNo", ExcelDataType.TXT, 20, true),
                new ExcelMapRule("单位", "unit", ExcelDataType.TXT, 100, true),
        };

        /**
         * 调用readExcel(InputStream, ExcelMapRule[], Clazz<T>)
         * 或者readExcel(File, ExcelMapRule[], Clazz<T>)
         * 如果校验错误则会抛出PoiException异常
         * 否则返回转换后的List<T>
         */
        List<UserCard> items = poiService.readExcel(new FileInputStream(file), rules, UserCard.class);
        if (items != null) {
            for (UserCard item: items) {
                System.out.println(item.toString());
            }
        }

    }

}
```
## 6.结果
如果校验失败，则会抛出PoiException异常:
```
第[6]行, 第[5]列, 字段[单位]:不能为空或长度超出限制;

xyz.frt.poi.service.PoiException: 第[6]行, 第[5]列, 字段[单位]:不能为空或长度超出限制;

	at xyz.frt.poi.service.PoiServiceImpl.readExcel(PoiServiceImpl.java:83)
	at tec.gomoo.oa.PoiServiceTest.test(PoiServiceTest.java:30)
	......
```
如果成功，结果如下:
```
UserCard [cid=652000000000000000, name=外星人, cardNo=88888888888888, unit=中华全国总工会]
```
附上Excel表格:

![Excel](http://119.29.65.188:8080/examples/excel.png  "Excel表格")
## 7.最后
```
如果你发现有Bug或者有好的建议，欢迎联系作者。
如果你觉得此项目对你有帮助，请在最上方为作者添加一颗Star。
作者:four-roud-titans
邮箱:937855602@qq.com
QQ:937855602
```
