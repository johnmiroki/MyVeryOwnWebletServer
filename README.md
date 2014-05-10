# My Very Own Weblet Server
## 知识产权
这个项目的创意属于[Java Server Side Programming: The Conceptual Foundation](http://www.amazon.com/Java-Server-Side-Programming-Conceptual-ebook/dp/B00ET2GUOK/ref=sr_1_1?s=digital-text&ie=UTF8&qid=1399693935&sr=1-1&keywords=java+server+side+programming+the+conceptual+foundation)的作者 Mukesh Prasad。
## 介绍
这是一个 Java 联系项目，适合于完成 JavaSE 的初学者。项目通过模拟 Servlet 服务器，帮助初学者熟悉基本编程技巧、了解服务器底层原理，为学习 JavaWeb 打下良好基础。如《知识产权》所述，代码的创意来自那本书的作者，我在这里只不过是把书中的代码搬到这里，虽然代码是一个字母一个字母敲上来的，会有一些重构和改造，但项目整个架构和部分代码都是属于原作者的。
## 源代码结构
    * mvows 核心类
       * MyServer 程序入口，负责为每个连接请求创建 socket，解析请求，并调用相应的类和方法生成响应
       * MyWeblet 抽象类，定义了 Weblet API，供 Weblet 作者继承
       * WebletConfigs 用于定义、储存用户请求 Weblet 名和对应 Weblet 类的关系
       * WebletProcessor 用于调用 MyWeblet，传入参数，并根据 MyWeblet 内部属性，生成相应的响应头信息
    * myapps MyWeblet 的实现类包
    * utls 工具类包
       * Utils 存放静态方法，辅助参数、cookie 等的处理