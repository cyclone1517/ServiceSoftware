### **报文域配置文件说明文档**

#### 配置文件结构介绍
+ PackageInfos 根节点
   + headProp 公共域(帧头)
      + prop 公共域属性
         + fn 公共属性1 (如起始字节/ 报文长度/ 控制位)
         + fn 公共属性2
         + fn 公共属性3
         + ...
      + fldlen 每个域所占字节数 (每位数和上述域属性一一对应)
      + encode 每个域所用编码 (0: 二进制/ 1: BCD码)
   + userProp 用户数据域
      + prop 用户业务类型1 (如自动上传/ 心跳)
         + id 业务编号
         + function 业务名称
         + fieldNames 用户属性名称
            + fn 用户属性1 (如表数量/ 表编号)
            + fn 用户属性1
            + fn 用户属性1
            + ...
         + fldlen 每个域所占字节数 (每位数和上述域属性一一对应)
         + encode 每个域所用编码 (0: 二进制/ 1: BCD码)
         + info 多数据实体的对应关系说明，如多抄表
            + start 数量标识域编号(如批量上报的表数量是第9个域，则start=9)
            + end 批抄数据域结束编号(如批抄水表有id/data/state三个数据域，且state是第13个域，则end=13)
            + className 需要把批抄数据注入的实体类名
            + fieldName 在对应Package类中的数据域名
      + prop 用户业务类型2
      + prop 用户业务类型3
      + ...

#### 公共域(所有报文都包含的域) headProp
域名|含义
---|---
firstStartChar|起始字符
firstLength|长度L
secondLength|长度L
secondStartChar|起始字符
control|控制域C
address|地址域A
afn|功能编码
seq|序列码
dataId|数据单元标识

#### 用户数据域 userProp
##### 自动上传
域名|含义
---|---
30081548684|业务编号
AUTO_UPLOAD|自动上传
number|抄表数量
id|表编号
data|表读数
state|表状态
cs|校验位
endChar|结束符