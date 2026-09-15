package com.kailin.dao;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.config.rules.IColumnType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.baomidou.mybatisplus.generator.keywords.MySqlKeyWordsHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * generator代码生成器
 * 框架中使用mybatis-plus版本为3.4.3.4，对应generator版本为3.5.1，但由于3.5.1处理关键字的生成有bug
 * 所以此处稍作修改，仍然使用3.4.1的generator
 * @Author chengpuhui
 * @Date 2021/12/23
 */
public class MybatisPlusGenerator {

    //作者名
    private static final String AUTHOR = "杨松";
    //表table的前缀，不加到生成的类名中
    private static final String PREFIX = "";
    //功能模块名称，生成的文件会存放到模块下
    private static final String MODULE_NAME = "";
    //要生成的表名
    private static final String[] TABLES= {"chat"};
    private static final String JDBC_URL = "jdbc:mysql://192.168.21.112:3306/kl_chat?useUnicode=true&characterEncoding=UTF-8" +
            "&useSSL=false&zeroDateTimeBehavior=convertToNull&";
    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String JDBC_USERNAME = "root";
    private static final String JDBC_PASSWORD = "qwer1234";
    private static final String BASE_PACKAGE = "com.kailin.dao.chat";

    public static void main(String[] args) {
        //当前项目路径
        String projectPath = System.getProperty("user.dir");

        // 代码生成器
        AutoGenerator generator = new AutoGenerator();

        //数据库配置
        configDataSource(generator);
        //全局配置
        configGlobal(generator, projectPath);
        //包相关配置
        configPackage(generator);
        //策略配置
        configStrategy(generator);
        //自定义配置
        cofnigCustom(generator, projectPath);
        //模版引擎配置
        configTemplate(generator);

        generator.execute();
    }

    /**
     * 进行数据库相关配置
     * @param generator
     */
    private static void configDataSource(AutoGenerator generator){
        //数据源配置
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setUrl(JDBC_URL);
        dataSourceConfig.setDbType(DbType.MYSQL);
        dataSourceConfig.setDriverName(JDBC_DRIVER);
        dataSourceConfig.setUsername(JDBC_USERNAME);
        dataSourceConfig.setPassword(JDBC_PASSWORD);
        dataSourceConfig.setKeyWordsHandler(new MySqlKeyWordsHandler());
        // 解决tinyint(1)生成Boolean的问题
        dataSourceConfig.setTypeConvert(new MySqlTypeConvert(){
            @Override
            public IColumnType processTypeConvert(GlobalConfig config, String fieldType) {
                if (fieldType.toLowerCase().contains("tinyint")){
                    return DbColumnType.BYTE;
                }
                return super.processTypeConvert(config, fieldType);
            }
        });
        generator.setDataSource(dataSourceConfig);
    }

    /**
     * 进行全局配置
     * @param generator
     * @param projectPath
     */
    private static void configGlobal(AutoGenerator generator, String projectPath) {
        // 全局配置
        GlobalConfig globalConfig = new GlobalConfig();
        //生成文件输出存放路径 = 当前项目路径 + 想存放到项目中的路径
        String fileOutputPatch = projectPath.concat("/java-rag-cloud-data/src/main/java");
        globalConfig.setOutputDir(fileOutputPatch);

        //设置作者
        globalConfig.setAuthor(AUTHOR);
        //生成完后是否打开输出目录
        globalConfig.setOpen(false);
        //是否覆盖生成过的已有文件
        globalConfig.setFileOverride(true);
        //是否开启activeRecord模式
        globalConfig.setActiveRecord(true);
        // 是否在xml中添加二级缓存配置,默认false
        globalConfig.setEnableCache(false);
        // XML文件返回对象定义ResultMap
        globalConfig.setBaseResultMap(true);
        // XML返回对象字段列表columList
        globalConfig.setBaseColumnList(true);
        //设置主键字段类型
        globalConfig.setIdType(IdType.INPUT);
        //生成的文件名字定义，%s 会自动填充表实体属性
        globalConfig.setMapperName("%sMapper");
        globalConfig.setXmlName("%sMapper");
        globalConfig.setEntityName("%s");
        //开启 swagger2 模式,实体属性 Swagger2 注解,默认false
        globalConfig.setSwagger2(true);
        // 修改生成entity的时间类型为java.util.Date
        globalConfig.setDateType(DateType.ONLY_DATE);
        generator.setGlobalConfig(globalConfig);
    }

    /**
     * 各个包配置
     * @param generator
     */
    private static void configPackage(AutoGenerator generator) {
        PackageConfig packageConfig = new PackageConfig();
        packageConfig.setModuleName(MODULE_NAME);
        packageConfig.setParent(BASE_PACKAGE);//包路径
        packageConfig.setEntity("entity");
        packageConfig.setMapper("mapper");
        packageConfig.setXml("mapper");
        generator.setPackageInfo(packageConfig);
    }

    /**
     * 策略配置
     * @param generator
     */
    private static void configStrategy(AutoGenerator generator) {
        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        //表的前缀
        strategy.setTablePrefix(PREFIX);
        //表名下划线转为驼峰
        strategy.setNaming(NamingStrategy.underline_to_camel);
        //字段名下划线转为驼峰
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        //生成哪些表
        strategy.setInclude(TABLES);
        strategy.setControllerMappingHyphenStyle(true);
        //设置模版引擎的类型 freemarker使用ftl文件，velocity使用vm文件
        generator.setTemplateEngine(new FreemarkerTemplateEngine());
//        generator.setTemplateEngine(new VelocityTemplateEngine());
        //是否使用lombok
        strategy.setEntityLombokModel(true);
        //设置是否restful控制器
        strategy.setRestControllerStyle(true);
        //设置布尔类型字段是否去掉is前缀
        strategy.setEntityBooleanColumnRemoveIsPrefix(true);
        // 自定义实体父类
        // strategy.setSuperEntityClass("com.xxx.demo.TestEntity");
        // 自定义实体，公共字段
        // strategy.setSuperEntityColumns(new String[] { "test_id", "age" });
        // 自定义 mapper 父类
        // strategy.setSuperMapperClass("com.xxx.demo.TestMapper");
        // 自定义 service 父类
        // strategy.setSuperServiceClass("com.xxx.demo.TestService");
        // 自定义 service 实现类父类
        // strategy.setSuperServiceImplClass("com.xxx.demo.TestServiceImpl");
        // 自定义 controller 父类
        // strategy.setSuperControllerClass("com.xxx.demo.TestController");
        // 【实体】是否生成字段常量（默认 false）
        // public static final String ID = "test_id";
        // strategy.setEntityColumnConstant(true);
        // 【实体】是否为构建者模型（默认 false）
        // public User setName(String name) {this.name = name; return this;}
        // strategy.setEntityBuilderModel(true);
        generator.setStrategy(strategy);
    }

    /**
     * 自定义配置
     * @param generator
     * @param projectPath
     */
    private static void cofnigCustom(AutoGenerator generator, final String projectPath) {
        // 自定义配置
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                // to do nothing
            }
        };

        // 如果模板引擎是 freemarker
        String templatePath = "/templates/mapper.xml.ftl";
        // 如果模板引擎是 velocity
        // String templatePath = "/templates/mapper.xml.vm";

        // 自定义输出配置
        List<FileOutConfig> focList = new ArrayList<>();
        // 自定义配置会被优先输出
        focList.add(new FileOutConfig(templatePath) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                //自定义输出文件名 ， 如果 Entity 设置了前后缀、此处 xml 的名称会跟着发生变化
                return projectPath.concat("/java-rag-cloud-data/src/main/resources/mapper/").concat(MODULE_NAME).concat("/")
                        .concat(tableInfo.getEntityName()).concat("Mapper").concat(StringPool.DOT_XML);
            }
        });
        cfg.setFileOutConfigList(focList);
        generator.setCfg(cfg);
    }

    /**
     * 模版引擎配置
     * @param generator
     */
    private static void configTemplate(AutoGenerator generator) {
        //模板引擎配置 默认是VelocityTemplateEngine
        TemplateConfig templateConfig = new TemplateConfig();
        // 解决mybatis-plus升级3.4.3.4后，3.4.1版本generator生成pkVal方法为protected的问题
        templateConfig.setEntity("/mybatistemplate/entity.java");
        templateConfig.setXml(null);
        templateConfig.setService(null);
        templateConfig.setServiceImpl(null);
        templateConfig.setController(null);
        generator.setTemplate(templateConfig);
    }
}
