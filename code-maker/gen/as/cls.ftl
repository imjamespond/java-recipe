package ${vo.pkg}
{
<#list vo.items as e>
    <#if e.type="java.lang.String">
    <#elseif e.type="long">
    <#elseif e.type="java.lang.Long">
    <#elseif e.type="int">
    <#elseif e.type="java.lang.Integer">
    <#elseif e.type="float">
    <#elseif e.type="java.lang.Float">
    <#elseif e.type="double">
    <#elseif e.type="java.lang.Double">
    <#elseif e.type="boolean">
    <#elseif e.type="java.lang.Boolean">
    <#elseif e.type="java.util.List">
    <#elseif e.type="java.util.ArrayList">
    <#elseif e.type="Array">
    <#else>
    import ${e.type};
    </#if>
</#list>
<#if vo.fName?exists>
    import com.metasoft.flying.vo.${vo.fName};
</#if>
    /**
    *名称: ${vo.clsDesc}
    *事件: ${vo.event}
    * @author 自动生成请勿修改
    *
    */
    public class ${vo.cls}<#if vo.fName?exists>  extends ${vo.fName} </#if>
    {
        public function ${vo.cls}()
        {
        }
    <#list vo.items as e>

        /**
        * ${e.nameDesc}
        */
        <#if e.type="java.lang.String">
        public var ${e.name}:String;
        <#elseif e.type="long">
        public var ${e.name}:Number;
        <#elseif e.type="java.lang.Long">
        public var ${e.name}:Number;
        <#elseif e.type="java.lang.Integer">
        public var ${e.name}:int;
        <#elseif e.type="float">
        public var ${e.name}:Number;
        <#elseif e.type="java.lang.Float">
        public var ${e.name}:Number;
        <#elseif e.type="double">
        public var ${e.name}:Number;
        <#elseif e.type="java.lang.Double">
        public var ${e.name}:Number;
        <#elseif e.type="boolean">
        public var ${e.name}:Boolean;
        <#elseif e.type="java.lang.Boolean">
        public var ${e.name}:Boolean;
        <#elseif e.type="java.util.List">
        public var ${e.name}:Array;
        <#elseif e.type="java.util.ArrayList">
        public var ${e.name}:Array;
        <#elseif e.type="Array">
        public var ${e.name}:Array;
        <#else>
        public var ${e.name}:${e.type};
        </#if>
    </#list>

        <#if vo.fName?exists>
        override public function toString():String
        {
        var superToString:String = super.toString();
        var superInfo:String = superToString.substring(1, superToString.length - 1) + ", ";
        return "{" + superInfo  + <#list vo.items as e> "${e.name}:"+${e.name}<#if e_has_next>+","</#if> +</#list> "}";
        }
        <#else>
        public function toString():String
        {
        return "{" + <#list vo.items as e> "${e.name}:"+${e.name} <#if e_has_next>+","</#if> +</#list> "}";

        }

        </#if>

    }
}