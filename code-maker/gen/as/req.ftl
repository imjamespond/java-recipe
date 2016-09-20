package ${vo.pkg}
{
<#list vo.items as e>
    <#if e.type="java.lang.String">
    <#elseif e.type="long">
    <#elseif e.type="java.lang.Long">
    <#elseif e.type="int">
    <#elseif e.type="java.lang.Integer">
    <#elseif e.type="Integer">
    <#elseif e.type="float">
    <#elseif e.type="java.lang.Float">
    <#elseif e.type="double">
    <#elseif e.type="java.lang.Double">
    <#elseif e.type="boolean">
    <#elseif e.type="java.lang.Boolean">
    <#elseif e.type="java.util.List">
    <#elseif e.type="java.util.ArrayList">
    <#elseif e.type="Array">
    <#elseif e.type="byte[]">
    import flash.utils.ByteArray;
    <#else>
    import ${e.type};
    </#if>

</#list>
    import com.metasoft.flying.vo.general.GeneralRequest;

    /**
     * ${vo.clsDesc}
     * @author 自动生成请勿修改
     *
     */
    public class ${vo.cls} extends GeneralRequest
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
        <#elseif e.type="byte[]">
        public var ${e.name}:ByteArray;
        <#else>
        public var ${e.name}:${e.type};
        </#if>
    </#list>

        override public function toString():String
        {
        var superToString:String = super.toString();
        var superInfo:String = superToString.substring(1, superToString.length - 1) + ", ";
        return "{" + superInfo  + <#list vo.items as e> "${e.name}:"+${e.name}<#if e_has_next>+","</#if> +</#list> "}";
        }


    }
}